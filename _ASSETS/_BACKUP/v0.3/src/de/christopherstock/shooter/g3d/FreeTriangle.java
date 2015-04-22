/*  $Id: FreeTriangle.java 248 2011-02-02 01:21:57Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  java.awt.geom.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.io.d3ds.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a triangle face.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class FreeTriangle extends Face
    {
        public                  GameObject          parentGameObject            = null;

        //the points that have distances!
        public                  float               lowestZ                     = 0.0f;
        public                  float               highestZ                    = 0.0f;

        public                  Line2D.Float        collisionLineHorz           = null;

        public FreeTriangle( LibMaxTriangle maxTriangle )
        {
            this
            (
                maxTriangle.iAnchor,
                ( Texture.getByName( maxTriangle.iTextureName ) == null ? null : Texture.getByName( maxTriangle.iTextureName ).iTexture ),
                maxTriangle.iCol,
                maxTriangle.iA,
                maxTriangle.iB,
                maxTriangle.iC,
                maxTriangle.iFaceNormal
            );
        }

        public FreeTriangle( LibVertex ank, LibGLTexture texture, LibColors col, LibVertex a, LibVertex b, LibVertex c, LibVertex aFaceNormal )
        {
            super( ank, texture, col, aFaceNormal );

            //set vertices
            setOriginalVertices( new LibVertex[] { a, b, c, } );

            //init all values
            setCollisionValues();
        }

        public final void assignParentGameObject( GameObject aParentGameObject )
        {
            //connect to parent mesh
            parentGameObject = aParentGameObject;
        }

        @Override
        protected final void setCollisionValues()
        {
            //set collission values
            ShooterDebugSystem.freetriangle.out( "setting collision values for free triangle .." );

            ShooterDebugSystem.freetriangle.out( "a: ["+iTransformedVertices[ 0 ].x+"]["+iTransformedVertices[ 0 ].y+"]["+iTransformedVertices[ 0 ].z+"]" );
            ShooterDebugSystem.freetriangle.out( "b: ["+iTransformedVertices[ 1 ].x+"]["+iTransformedVertices[ 1 ].y+"]["+iTransformedVertices[ 1 ].z+"]" );
            ShooterDebugSystem.freetriangle.out( "c: ["+iTransformedVertices[ 2 ].x+"]["+iTransformedVertices[ 2 ].y+"]["+iTransformedVertices[ 2 ].z+"]" );

            float x1 = iTransformedVertices[ 0 ].x;
            float x2 = iTransformedVertices[ 1 ].x;
            float x3 = iTransformedVertices[ 2 ].x;
            if ( x1 == x2 ) x2 = x3;

            float y1 = iTransformedVertices[ 0 ].y;
            float y2 = iTransformedVertices[ 1 ].y;
            float y3 = iTransformedVertices[ 2 ].y;
            if ( y1 == y2 ) y2 = y3;

            float z1 = iTransformedVertices[ 0 ].z;
            float z2 = iTransformedVertices[ 1 ].z;
            float z3 = iTransformedVertices[ 2 ].z;
            if ( z1 == z2 ) z2 = z3;

            //specify lowest and highest z value
            lowestZ  = Math.min( z1, z2 );
            highestZ = Math.max( z1, z2 );

            //calculate angle horz
            Point2D.Float   p1              = new Point2D.Float( x1, y1 );
            Point2D.Float   p2              = new Point2D.Float( x2, y2 );
            float           angleHorz       = LibMath.getAngleCorrect( p1, p2 );

            //Debug.out( "original horz angle: " + UMath.getAngleCorrect( p1, p2 ) );
            //Debug.out( "normalized horz angle: " + angleHorz );

            //mesh translated?

            //assign angles anf collision lines
            setFaceAngleHorz( angleHorz );
            setFaceAngleVert( 0.0f      );

            collisionLineHorz = new Line2D.Float( p1.x, p1.y, p2.x, p2.y );

        }

        /***********************************************************************************
        *   Only suitable for faces that have collision lines specified!
        *
        *   @param  shot    The shot to be fired on this mesh.
        *   @return         The nearest hit-point with lost of additional information
        *                   or <code>null</code> if no hit-point was found.
        ***********************************************************************************/
        public HitPoint launchShot( Shot shot )
        {
            //horizontal and vertical collission lines have to be present
            if ( collisionLineHorz == null /* || collisionLineVert == null */ ) return null;

            //check horizontal collision
            if ( !shot.lineShotHorz.intersectsLine( collisionLineHorz  ) )
            {
                //return null if missed
                //Debug.shot.out( "\n\nHORZ FACE MISSED!" );
                return null;
            }

            //get intersection point horz
            ShooterDebugSystem.shot.out( "==============\nHORZ FACE HIT!" );
            Point2D.Float intersectionPointHorz = LibMathGeometry.findLineSegmentIntersection( shot.lineShotHorz, collisionLineHorz, ShooterDebugSystem.error );
            if ( intersectionPointHorz == null )
            {
                ShooterDebugSystem.shot.out( "Intersection Point not calculated due to buggy external API." );
                return null;
            }

            //get horizontal values
            float           exactDistanceHorz     = (float)shot.srcPointHorz.distance( intersectionPointHorz );    //get exact distance
            float           shotAngleHorz         = LibMath.getAngleCorrect( shot.srcPointHorz, intersectionPointHorz );        //get angle between player and hit-point
            float           invertedShotAngleHorz = 360.0f - ( shotAngleHorz - 180.0f  );              //get opposite direction of shot
            float           SliverAngleHorz       = shotAngleHorz - iFaceAngleHorz * 2;              //get Sliver angle

            ShooterDebugSystem.shot.out( "exactDistanceHorz:     [" + exactDistanceHorz + "]" );
            ShooterDebugSystem.shot.out( "shotAngleHorz:         [" + shotAngleHorz + "]" );
            ShooterDebugSystem.shot.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz + "]" );
            ShooterDebugSystem.shot.out( "SliverAngleHorz:       [" + SliverAngleHorz + "]" );

            //calculate face's vertical collision line
            Line2D.Float collisionLineVert = new Line2D.Float( new Point2D.Float( exactDistanceHorz, lowestZ ), new Point2D.Float( exactDistanceHorz, highestZ ) );
            ShooterDebugSystem.shot.out( "Wall's collision line vert is: [" + collisionLineVert + "]" );

            if ( !shot.lineShotVert.intersectsLine( collisionLineVert ) )
            {
                ShooterDebugSystem.shot.out( "VERTICAL FACE MISSED!" );
                return null;
            }

            //get then intersection point for the vertical axis
            ShooterDebugSystem.shot.out( "VERTICAL FACE HIT!" );
            Point2D.Float   intersectionPointVert   = LibMathGeometry.findLineSegmentIntersection( shot.lineShotVert, collisionLineVert, ShooterDebugSystem.error );
            float           z                       = intersectionPointVert.y;
            ShooterDebugSystem.shot.out( ">> INTERSECTION POINT VERT: " + intersectionPointVert );

            //get vertical values
            float exactDistanceVert = (float)shot.srcPointVert.distance( intersectionPointVert );       //get exact distance
            float shotAngleVert         = LibMath.getAngleCorrect( shot.srcPointVert, new Point2D.Float( exactDistanceVert, z ) );    //get angle between player and hit-point
            float invertedShotAngleVert = 360.0f - ( shotAngleVert - 180.0f  );                             //get opposite direction of shot
            float SliverAngleVert       = 90.0f + 90.0f - shotAngleVert ;                                //get Sliver angle
            ShooterDebugSystem.shot.out( ">> EXACT DISTANCE VERT: " + exactDistanceVert );
            ShooterDebugSystem.shot.out( ">> SHOT-ANGLE-VERT: " + shotAngleVert );
            ShooterDebugSystem.shot.out( ">> SLIVER-ANGLE-VERT: " + SliverAngleVert );

            //return hit point
            return new HitPoint
            (
                parentGameObject,
                shot,
                iTexture,
                new LibVertex( intersectionPointHorz.x, intersectionPointHorz.y, z ),

                exactDistanceHorz,
                shotAngleHorz,
                invertedShotAngleHorz,
                SliverAngleHorz,
                iFaceAngleHorz,

                exactDistanceVert,
                shotAngleVert,
                invertedShotAngleVert,
                SliverAngleVert,
                iFaceAngleVert
            );
        }

        public final FreeTriangle copy()
        {
            LibVertex ankCopy  = new LibVertex( iAnchor.x, iAnchor.y, iAnchor.z, iAnchor.u, iAnchor.v );
            LibVertex aCopy    = new LibVertex( iOriginalVertices[ 0 ].x, iOriginalVertices[ 0 ].y, iOriginalVertices[ 0 ].z, iOriginalVertices[ 0 ].u, iOriginalVertices[ 0 ].v );
            LibVertex bCopy    = new LibVertex( iOriginalVertices[ 1 ].x, iOriginalVertices[ 1 ].y, iOriginalVertices[ 1 ].z, iOriginalVertices[ 1 ].u, iOriginalVertices[ 1 ].v );
            LibVertex cCopy    = new LibVertex( iOriginalVertices[ 2 ].x, iOriginalVertices[ 2 ].y, iOriginalVertices[ 2 ].z, iOriginalVertices[ 2 ].u, iOriginalVertices[ 2 ].v );
            LibVertex normCopy = ( iFaceNormal == null ? null : new LibVertex( iFaceNormal.x, iFaceNormal.y, iFaceNormal.z ) );
            return new FreeTriangle
            (
                ankCopy,
                iTexture,
                iColor,
                aCopy,
                bCopy,
                cCopy,
                normCopy
            );
        }


        public final Float checkCollision( Point2D.Float point )
        {
            //to rectangular-face!
            float x1 = iTransformedVertices[ 0 ].x;
            float x2 = iTransformedVertices[ 1 ].x;
            float x3 = iTransformedVertices[ 2 ].x;
            float y1 = iTransformedVertices[ 0 ].y;
            float y2 = iTransformedVertices[ 1 ].y;
            float y3 = iTransformedVertices[ 2 ].y;
            float z1 = iTransformedVertices[ 0 ].z;
            //float z2 = transformedVertices[ 1 ].z;
            //float z3 = transformedVertices[ 2 ].z;

            if ( x1 == x2 ) x2 = x3;
            if ( y1 == y2 ) y2 = y3;
            //if ( z1 == z2 ) z2 = z3;

            float width  = x2 - x1;
            float height = y2 - y1;

            //turning width and height absolute is important!!
            if ( width < 0.0f )
            {
                width = -width;
                x1 -= width;
            }

            if ( height < 0.0f )
            {
                height = -height;
                y1 -= height;
            }

            //create the collision rectangle of this triangle face
            Rectangle2D.Float faceBaseHorz = new Rectangle2D.Float( x1, y1, width, height );

            //check if player is in rect
            if ( faceBaseHorz.contains( point ) )    //if ( Player.user.cylinder.checkHorzCenterCollision( x1, y1, x2, y2 ) )    //old solution
            {
                //add this z point to the values
                return new Float( z1 );
            }

            return null;
        }
    }
