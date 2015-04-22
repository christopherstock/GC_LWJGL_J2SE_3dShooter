/*  $Id: Face.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  java.awt.geom.*;
    import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
import de.christopherstock.shooter.game.objects.*;
import de.christopherstock.shooter.gl.*;
    import de.christopherstock.shooter.math.*;
import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   Represents a triangle face.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class FreeTriangle extends Face
    {
        public                  GameObject          parentGameObject            = null;

        //the points that have distances!
        public                  float               lowestZ                     = 0.0f;
        public                  float               highestZ                    = 0.0f;

        public                  Line2D.Float        collisionLineHorz           = null;

        public FreeTriangle( Vertex ank, Texture texture, Colors col, Vertex a, Vertex b, Vertex c )
        {
            super( ank, texture, col );
/*
            if ( false ) //always false, invertTextureU
            {
                a.u = -a.u;
                b.u = -b.u;
                c.u = -c.u;
            }
            if ( false ) //always false, invertTextureV
            {
                a.v = -a.v;
                b.v = -b.v;
                c.v = -c.v;
            }
*/
            //set vertices
            setOriginalVertices( new Vertex[] { a, b, c, } );

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
            Debug.freetriangle.out( "setting collision values for free triangle .." );

            Debug.freetriangle.out( "a: ["+transformedVertices[ 0 ].x+"]["+transformedVertices[ 0 ].y+"]["+transformedVertices[ 0 ].z+"]" );
            Debug.freetriangle.out( "b: ["+transformedVertices[ 1 ].x+"]["+transformedVertices[ 1 ].y+"]["+transformedVertices[ 1 ].z+"]" );
            Debug.freetriangle.out( "c: ["+transformedVertices[ 2 ].x+"]["+transformedVertices[ 2 ].y+"]["+transformedVertices[ 2 ].z+"]" );

            float x1 = transformedVertices[ 0 ].x;
            float x2 = transformedVertices[ 1 ].x;
            float x3 = transformedVertices[ 2 ].x;
            if ( x1 == x2 ) x2 = x3;

            float y1 = transformedVertices[ 0 ].y;
            float y2 = transformedVertices[ 1 ].y;
            float y3 = transformedVertices[ 2 ].y;
            if ( y1 == y2 ) y2 = y3;

            float z1 = transformedVertices[ 0 ].z;
            float z2 = transformedVertices[ 1 ].z;
            float z3 = transformedVertices[ 2 ].z;
            if ( z1 == z2 ) z2 = z3;

            //specify lowest and highest z value
            lowestZ  = Math.min( z1, z2 );
            highestZ = Math.max( z1, z2 );

            //calculate angle horz
            Point2D.Float   p1              = new Point2D.Float( x1, y1 );
            Point2D.Float   p2              = new Point2D.Float( x2, y2 );
            float           angleHorz       = UMath.getAngleCorrect( p1, p2 );

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
            Debug.shot.out( "==============\nHORZ FACE HIT!" );
            Point2D.Float intersectionPointHorz = UMathGeometry.findLineSegmentIntersection( shot.lineShotHorz, collisionLineHorz );
            if ( intersectionPointHorz == null )
            {
                Debug.shot.out( "Intersection Point not calculated due to buggy external API." );
                return null;
            }

            //get horizontal values
            float           exactDistanceHorz     = (float)shot.srcPointHorz.distance( intersectionPointHorz );    //get exact distance
            float           shotAngleHorz         = UMath.getAngleCorrect( shot.srcPointHorz, intersectionPointHorz );        //get angle between player and hit-point
            float           invertedShotAngleHorz = 360.0f - ( shotAngleHorz - 180.0f  );              //get opposite direction of shot
            float           SliverAngleHorz       = shotAngleHorz - faceAngleHorz * 2;              //get Sliver angle

            Debug.shot.out( "exactDistanceHorz:     [" + exactDistanceHorz + "]" );
            Debug.shot.out( "shotAngleHorz:         [" + shotAngleHorz + "]" );
            Debug.shot.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz + "]" );
            Debug.shot.out( "SliverAngleHorz:       [" + SliverAngleHorz + "]" );

            //calculate face's vertical collision line
            Line2D.Float collisionLineVert = new Line2D.Float( new Point2D.Float( exactDistanceHorz, lowestZ ), new Point2D.Float( exactDistanceHorz, highestZ ) );
            Debug.shot.out( "Wall's collision line vert is: [" + collisionLineVert + "]" );

            if ( !shot.lineShotVert.intersectsLine( collisionLineVert ) )
            {
                Debug.shot.out( "VERTICAL FACE MISSED!" );
                return null;
            }

            //get then intersection point for the vertical axis
            Debug.shot.out( "VERTICAL FACE HIT!" );
            Point2D.Float   intersectionPointVert   = UMathGeometry.findLineSegmentIntersection( shot.lineShotVert, collisionLineVert );
            float           z                       = intersectionPointVert.y;
            Debug.shot.out( ">> INTERSECTION POINT VERT: " + intersectionPointVert );

            //get vertical values
            float exactDistanceVert = (float)shot.srcPointVert.distance( intersectionPointVert );       //get exact distance
            float shotAngleVert         = UMath.getAngleCorrect( shot.srcPointVert, new Point2D.Float( exactDistanceVert, z ) );    //get angle between player and hit-point
            float invertedShotAngleVert = 360.0f - ( shotAngleVert - 180.0f  );                             //get opposite direction of shot
            float SliverAngleVert       = 90.0f + 90.0f - shotAngleVert ;                                //get Sliver angle
            Debug.shot.out( ">> EXACT DISTANCE VERT: " + exactDistanceVert );
            Debug.shot.out( ">> SHOT-ANGLE-VERT: " + shotAngleVert );
            Debug.shot.out( ">> SLIVER-ANGLE-VERT: " + SliverAngleVert );

            //return hit point
            return new HitPoint
            (
                parentGameObject,
                shot,
                ( texture == null ? Material.EUndefined.getBulletHoleTexture() : texture.getMaterial().getBulletHoleTexture() ),
                new Vertex( intersectionPointHorz.x, intersectionPointHorz.y, z ),

                exactDistanceHorz,
                shotAngleHorz,
                invertedShotAngleHorz,
                SliverAngleHorz,
                faceAngleHorz,

                exactDistanceVert,
                shotAngleVert,
                invertedShotAngleVert,
                SliverAngleVert,
                faceAngleVert
            );
        }

        public final FreeTriangle copy()
        {
            Vertex ankCopy = new Vertex( anchor.x, anchor.y, anchor.z, anchor.u, anchor.v );
            Vertex aCopy   = new Vertex( originalVertices[ 0 ].x, originalVertices[ 0 ].y, originalVertices[ 0 ].z, originalVertices[ 0 ].u, originalVertices[ 0 ].v );
            Vertex bCopy   = new Vertex( originalVertices[ 1 ].x, originalVertices[ 1 ].y, originalVertices[ 1 ].z, originalVertices[ 1 ].u, originalVertices[ 1 ].v );
            Vertex cCopy   = new Vertex( originalVertices[ 2 ].x, originalVertices[ 2 ].y, originalVertices[ 2 ].z, originalVertices[ 2 ].u, originalVertices[ 2 ].v );
            return new FreeTriangle
            (
                ankCopy,
                texture,
                color,
                aCopy,
                bCopy,
                cCopy
            );
        }


        public final Float checkCollision( Point2D.Float point )
        {
            //to rectangular-face!
            float x1 = transformedVertices[ 0 ].x;
            float x2 = transformedVertices[ 1 ].x;
            float x3 = transformedVertices[ 2 ].x;
            float y1 = transformedVertices[ 0 ].y;
            float y2 = transformedVertices[ 1 ].y;
            float y3 = transformedVertices[ 2 ].y;
            float z1 = transformedVertices[ 0 ].z;
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
