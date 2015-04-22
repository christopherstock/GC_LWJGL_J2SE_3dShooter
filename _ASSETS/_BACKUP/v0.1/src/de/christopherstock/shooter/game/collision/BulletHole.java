/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.util.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.gl.mesh.*;

    /**************************************************************************************
    *   Represents a bullet-hole on a wall or an object.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class BulletHole
    {
        private     static  final   float                   POINT_SIZE                      = 0.025f;
        private     static  final   float                   MIN_DISTANCE_FROM_FACE          = 0.001f;
        private     static  final   float                   STEP_DISTANCE_FROM_FACE         = 0.0001f;
        private     static  final   float                   MIN_POINT_DISTANCE_SAME_LAYER   = POINT_SIZE * 4;

        private     static          Vector<BulletHole>      bulletHoles                     = new Vector<BulletHole>();

        private                     float                   hitPointX                       = 0.0f;
        private                     float                   hitPointY                       = 0.0f;
        private                     float                   horzFaceAngle                   = 0.0f;

        private                     float                   carriersFaceAngle                  = 0.0f;

        private                     Vertex                  originalPosition                = null;
        private                     Vertex                  position                        = null;

        private                     HitPointCarrier       bhc                             = null;

        private                     Texture                 textureID                       = null;
        private                     FaceEllipseWall         face                            = null;

        public BulletHole( HitPoint hitPoint )
        {
            this.textureID          = Texture.EBullet1;
            this.hitPointX          = hitPoint.vertex.x;
            this.hitPointY          = hitPoint.vertex.y;
            this.horzFaceAngle      = hitPoint.horzFaceAngle;
            this.bhc                = hitPoint.bhc;

            Debug.bullethole.out( "new bullet hole: face-angle [" + hitPoint.horzFaceAngle + "]" );

            //get suitable distance to avoid overlapping bullet-holes
            float distanceFromFace  = getSuitableDistanceFromFace();
            float distX             = -distanceFromFace * -UMath.cosDeg( hitPoint.horzFaceAngle );
            float distY             =  distanceFromFace *  UMath.sinDeg( hitPoint.horzFaceAngle );

            //get the two holes that hit the wall from both sides
            Point2D.Float point1 = new Point2D.Float( hitPoint.vertex.x + distX, hitPoint.vertex.y + distY );
            Point2D.Float point2 = new Point2D.Float( hitPoint.vertex.x - distX, hitPoint.vertex.y - distY );
            Debug.bullethole.out( "bullet-points: ["+point1.x+"]["+point1.y+"] ["+point2.x+"]["+point2.y+"]" );

            //select the nearer point
            float distance1 = (float)hitPoint.shot.srcPointHorz.distance( point1 );
            float distance2 = (float)hitPoint.shot.srcPointHorz.distance( point2 );
            Point2D.Float nearerHolePoint = ( distance1 < distance2 ? point1 : point2 );

            //assign the position of the Bullet Hole
            originalPosition  = new Vertex( nearerHolePoint.x, nearerHolePoint.y, hitPoint.vertex.z );
            position          = new Vertex( nearerHolePoint.x, nearerHolePoint.y, hitPoint.vertex.z );

            //check distance to Carrier if any
            if ( bhc != null )
            {
                this.carriersFaceAngle = bhc.getCarriersFaceAngle();
            }

            //setup the face
            updateFace();
        }

        public final void updateFace()
        {
            face = new FaceEllipseWall
            (
                textureID,
                Colors.EBlue,
                horzFaceAngle,
                position.x,
                position.y,
                position.z,
                POINT_SIZE,
                POINT_SIZE
            );
        }

        protected static final void addBulletHole( HitPoint hitPoint )
        {
            //add to bullet-hole-stack, prune stack if overflowing
            bulletHoles.add( new BulletHole( hitPoint ) );
            if ( bulletHoles.size() > Shooter.MAX_NUMBER_BULLET_HOLES ) bulletHoles.removeElementAt( 0 );
        }

        public static final void drawAll()
        {
            //draw all points
            for ( BulletHole bulletHole : bulletHoles )
            {
                GLView.drawFace( bulletHole.face );
            }
        }

        public static final void translateAll( HitPointCarrier bhc, float transX, float transY )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //check if this bullet hole belongs to the specified bot
                if ( bulletHole.bhc == bhc )
                {
                    System.out.println("yes !");
                    //translate it!
                    bulletHole.position.x += transX;
                    bulletHole.position.y += transY;
                    bulletHole.originalPosition.x += transX;
                    bulletHole.originalPosition.y += transY;
                    bulletHole.updateFace();
                }
            }
        }

        //only z-rotation are allowed for bullet-holes!
        public static final void rotateForBot( Bot bhc, float rotZ )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //check if this bullet hole belongs to the specified bot
                if ( bulletHole.bhc == bhc )
                {
                    //reverse last translation
                    Vertex translatedHitPoint = Matrix.transformVertex
                    (
                        //TODO extend for X and Y
                        bulletHole.position,
                        bhc.getAnchor(),
                        0.0f,
                        0.0f,
                        rotZ - bulletHole.carriersFaceAngle

                        //  rotZ

                    );

                    //assign new face angle
                    bulletHole.horzFaceAngle += ( rotZ - bulletHole.carriersFaceAngle );

                    //asssign new hit-point and update face
                    bulletHole.position = translatedHitPoint;
                    bulletHole.updateFace();

                    //assign new rotations
                    bulletHole.carriersFaceAngle = rotZ;
                }
            }
        }

        public static final void rotateForMesh( Mesh bhc, float rotZ )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //check if this bullet hole belongs to the specified mersh
                if ( bulletHole.bhc == bhc )
                {
                    //rotate bulletHole for mesh
                    bulletHole.rotateAroundVertex( bhc.getAnchor(), rotZ );
                }
            }
        }

        private final float getSuitableDistanceFromFace()
        {
            float dis = MIN_DISTANCE_FROM_FACE;

            //check distance to all other bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //should be distance3d :/
                if ( new Point2D.Float( bulletHole.hitPointX, bulletHole.hitPointY ).distance( new Point2D.Float( hitPointX, hitPointY ) ) < MIN_POINT_DISTANCE_SAME_LAYER )
                {
                    dis += STEP_DISTANCE_FROM_FACE;
                }
            }

            return dis;
        }

        private final void rotateAroundVertex( Vertex vertex, float rotZ )
        {
            //increase saved rot z
            Debug.bullethole.out( "turn for [" + rotZ + "]" );

            //transform
            Vertex translatedHitPoint = Matrix.transformVertex
            (
                position,
                vertex,
                0.0f,
                0.0f,
                rotZ
            );

            //increase face angle for rotZ
            horzFaceAngle += rotZ;
            Debug.bullethole.out( "setting bullet hole angle to [" + horzFaceAngle + "]" );

            //asssign new hit-point and update face
            position = translatedHitPoint;

            //update the bullet hole
            updateFace();
        }
    }
