/*  $Id: FreeTriangle.java 396 2011-03-13 22:16:58Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.face;

    import  java.awt.geom.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.io.d3ds.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a triangle face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class FaceTriangle extends LibFace
    {
        public                  GameObject          iParentGameObject           = null;

        //highest point z and lowest point z
        public                  float               iLowestZ                    = 0.0f;
        public                  float               iHighestZ                   = 0.0f;

        public                  Line2D.Float        iCollisionLineHorz          = null;

        /**************************************************************************************
        *   Copy constructor.
        **************************************************************************************/
        public FaceTriangle( LibMaxTriangle maxTriangle )
        {
            this
            (
                maxTriangle.iAnchor.copy(),
                ( ShooterTexture.getByName( maxTriangle.iTextureName ) == null ? null : ShooterTexture.getByName( maxTriangle.iTextureName ).iTexture ),
                maxTriangle.iCol,
                maxTriangle.iA.copy(),
                maxTriangle.iB.copy(),
                maxTriangle.iC.copy(),
                ( maxTriangle.iFaceNormal == null ? null : maxTriangle.iFaceNormal.copy() )
            );
        }

        public FaceTriangle( LibVertex ank, LibGLTexture texture, LibColors col, LibVertex a, LibVertex b, LibVertex c, LibVertex aFaceNormal )
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
            iParentGameObject = aParentGameObject;
        }

        @Override
        protected final void setCollisionValues()
        {
/*
            //set collission values
            ShooterDebug.freetriangle.out( "setting collision values for free triangle .." );

            ShooterDebug.freetriangle.out( "a: ["+iTransformedVertices[ 0 ].x+"]["+iTransformedVertices[ 0 ].y+"]["+iTransformedVertices[ 0 ].z+"]" );
            ShooterDebug.freetriangle.out( "b: ["+iTransformedVertices[ 1 ].x+"]["+iTransformedVertices[ 1 ].y+"]["+iTransformedVertices[ 1 ].z+"]" );
            ShooterDebug.freetriangle.out( "c: ["+iTransformedVertices[ 2 ].x+"]["+iTransformedVertices[ 2 ].y+"]["+iTransformedVertices[ 2 ].z+"]" );
*/
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
            iLowestZ  = Math.min( z1, z2 );
            iHighestZ = Math.max( z1, z2 );

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

            iCollisionLineHorz = new Line2D.Float( p1.x, p1.y, p2.x, p2.y );
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
            //horizontal collission line has to be present
            if ( iCollisionLineHorz == null ) return null;

            //check horizontal collision
            if ( !shot.iLineShotHorz.intersectsLine( iCollisionLineHorz  ) )
            {
                //return null if missed
                //Debug.shot.out( "\n\nHORZ FACE MISSED!" );
                return null;
            }

            //get intersection point horz
            ShooterDebug.shot.out( "==============\nHORZ FACE HIT!" );
            Point2D.Float intersectionPointHorz = LibMathGeometry.findLineSegmentIntersection( shot.iLineShotHorz, iCollisionLineHorz, ShooterDebug.error );
            if ( intersectionPointHorz == null )
            {
                ShooterDebug.shot.out( "Intersection Point not calculated due to buggy external API." );
                return null;
            }

            //get horizontal values
            float           exactDistanceHorz     = (float)shot.iSrcPointHorz.distance( intersectionPointHorz );    //get exact distance
            float           shotAngleHorz         = LibMath.getAngleCorrect( shot.iSrcPointHorz, intersectionPointHorz );        //get angle between player and hit-point
            float           invertedShotAngleHorz = 360.0f - ( shotAngleHorz - 180.0f  );              //get opposite direction of shot
            float           SliverAngleHorz       = shotAngleHorz - iFaceAngleHorz * 2;              //get Sliver angle

            ShooterDebug.shot.out( "exactDistanceHorz:     [" + exactDistanceHorz + "]" );
            ShooterDebug.shot.out( "shotAngleHorz:         [" + shotAngleHorz + "]" );
            ShooterDebug.shot.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz + "]" );
            ShooterDebug.shot.out( "SliverAngleHorz:       [" + SliverAngleHorz + "]" );

            //calculate face's vertical collision line
            Line2D.Float collisionLineVert = new Line2D.Float( new Point2D.Float( exactDistanceHorz, iLowestZ ), new Point2D.Float( exactDistanceHorz, iHighestZ ) );
            ShooterDebug.shot.out( "Wall's collision line vert is: [" + collisionLineVert + "]" );

            if ( !shot.iLineShotVert.intersectsLine( collisionLineVert ) )
            {
                ShooterDebug.shot.out( "VERTICAL FACE MISSED!" );
                return null;
            }

            //get then intersection point for the vertical axis
            ShooterDebug.shot.out( "VERTICAL FACE HIT!" );
            Point2D.Float   intersectionPointVert   = LibMathGeometry.findLineSegmentIntersection( shot.iLineShotVert, collisionLineVert, ShooterDebug.error );
            float           z                       = intersectionPointVert.y;
            ShooterDebug.shot.out( ">> INTERSECTION POINT VERT: " + intersectionPointVert );

            //TODO MERGE 1

            //get vertical values
            float exactDistanceVert = (float)shot.iSrcPointVert.distance( intersectionPointVert );       //get exact distance

            ShooterDebug.shot.out( ">> EXACT DISTANCE VERT: " + exactDistanceVert );
//            ShooterDebug.shot.out( ">> SHOT-ANGLE-VERT: " + shotAngleVert );
//            ShooterDebug.shot.out( ">> SLIVER-ANGLE-VERT: " + SliverAngleVert );

            //return hit point
            return new HitPoint
            (
                iParentGameObject,
                ShooterTexture.getBulletHoleTextureForMaterial( getTexture().getMaterial() ).iTexture,
                new LibVertex( intersectionPointHorz.x, intersectionPointHorz.y, z ),
                shot.iBulletHoleSize,
                shot.iSrcPointHorz,
                shot.iSrcPointVert,
                exactDistanceHorz,
                shotAngleHorz,
                invertedShotAngleHorz,
                SliverAngleHorz,
                iFaceAngleHorz,
                exactDistanceVert,
                iFaceAngleVert
            );
        }

        public final FaceTriangle copy()
        {
            LibVertex ankCopy  = getAnchor().copy();
            LibVertex aCopy    = new LibVertex( iOriginalVertices[ 0 ] );
            LibVertex bCopy    = new LibVertex( iOriginalVertices[ 1 ] );
            LibVertex cCopy    = new LibVertex( iOriginalVertices[ 2 ] );
            LibVertex normCopy = ( getFaceNormal() == null ? null : getFaceNormal().copy() );
            return new FaceTriangle
            (
                ankCopy,
                getTexture(),
                getColor(),
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

        @Override
        public boolean checkCollision( Cylinder cylinder )
        {
            //empty implementation - debug circles can not be shot :)
            return false;
        }
    }
