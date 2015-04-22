/*  $Id: BulletHole.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a bullet-hole on a wall or an object.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class BulletHole
    {
        private     static  final   float                   MIN_DISTANCE_FROM_FACE                          = 0.001f;
        private     static  final   float                   STEP_DISTANCE_FROM_FACE                         = 0.0001f;
        private     static  final   float                   MIN_POINT_DISTANCE_FOR_SAME_LAYER_MULTIPLIER    = 4;   //POINT_SIZE * 4;

        private     static          Vector<BulletHole>      bulletHoles                                     = new Vector<BulletHole>();

        private                     GameObject              carrier                                         = null;

        private                     FaceEllipseWall         face                                            = null;
        private                     LibGLTexture            texture                                         = null;
        private                     LibVertex               originalPosition                                = null;
        private                     LibVertex               position                                        = null;
        private                     float                   hitPointX                                       = 0.0f;
        private                     float                   hitPointY                                       = 0.0f;
        private                     float                   horzFaceAngle                                   = 0.0f;
        private                     float                   carriersLastFaceAngle                           = 0.0f;
        private                     LibHoleSize    bulletHoleSize                  = null;

        public BulletHole( HitPoint hitPoint )
        {
            hitPointX           = hitPoint.vertex.x;
            hitPointY           = hitPoint.vertex.y;
            horzFaceAngle       = hitPoint.horzFaceAngle;
            carrier             = hitPoint.carrier;
            texture             = hitPoint.texture;
            bulletHoleSize      = hitPoint.holeSize;

            ShooterDebug.bullethole.out( "new bullet hole: face-angle [" + hitPoint.horzFaceAngle + "]" );

            //get suitable distance to avoid overlapping bullet-holes
            float distanceFromFace  = getSuitableDistanceFromFace();
            float distX             = -distanceFromFace * -LibMath.cosDeg( hitPoint.horzFaceAngle );
            float distY             =  distanceFromFace *  LibMath.sinDeg( hitPoint.horzFaceAngle );

            //get the two holes that hit the wall from both sides
            Point2D.Float point1 = new Point2D.Float( hitPoint.vertex.x + distX, hitPoint.vertex.y + distY );
            Point2D.Float point2 = new Point2D.Float( hitPoint.vertex.x - distX, hitPoint.vertex.y - distY );
            ShooterDebug.bullethole.out( "bullet-points: ["+point1.x+"]["+point1.y+"] ["+point2.x+"]["+point2.y+"]" );

            //select the nearer point
            float distance1 = (float)hitPoint.srcPointHorz.distance( point1 );
            float distance2 = (float)hitPoint.srcPointHorz.distance( point2 );
            Point2D.Float nearerHolePoint = ( distance1 < distance2 ? point1 : point2 );

            //assign the position of the Bullet Hole
            originalPosition  = new LibVertex( nearerHolePoint.x, nearerHolePoint.y, hitPoint.vertex.z );
            position          = new LibVertex( nearerHolePoint.x, nearerHolePoint.y, hitPoint.vertex.z );

            //check distance to Carrier if any
            if ( carrier != null )
            {
                carriersLastFaceAngle = carrier.getCarriersFaceAngle();
            }

            //setup the face
            updateFace();
        }

        private final void updateFace()
        {
            face = new FaceEllipseWall
            (
                texture,
                horzFaceAngle,
                position.x,
                position.y,
                position.z,
                bulletHoleSize.size,
                bulletHoleSize.size
            );
        }

        protected static final void addBulletHole( HitPoint hitPoint )
        {
            //add to bullet-hole-stack, prune stack if overflowing
            bulletHoles.add( new BulletHole( hitPoint ) );
            if ( bulletHoles.size() > ShooterSettings.Performance.MAX_NUMBER_BULLET_HOLES ) bulletHoles.removeElementAt( 0 );
        }

        public static final void drawAll()
        {
            //draw all points
            for ( BulletHole bulletHole : bulletHoles )
            {
                bulletHole.face.draw();
            }
        }

        public static final void removeAll()
        {
            bulletHoles.removeAllElements();
        }

        public static final void translateAll( GameObject bhc, float transX, float transY )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //check if this bullet hole belongs to the specified bot
                if ( bulletHole.carrier == bhc )
                {
                    //translate it!
                    bulletHole.position.x           += transX;
                    bulletHole.position.y           += transY;
                    bulletHole.originalPosition.x   += transX;
                    bulletHole.originalPosition.y   += transY;
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
                if ( bulletHole.carrier == bhc )
                {
                    //reverse last translation
                    LibMatrix transformationMatrix = new LibMatrix( 0.0f, 0.0f, rotZ - bulletHole.carriersLastFaceAngle );
                    LibVertex translatedHitPoint = transformationMatrix.transformVertex( bulletHole.position, bhc.getAnchor() );

                    //assign new face angle
                    bulletHole.horzFaceAngle += ( rotZ - bulletHole.carriersLastFaceAngle );

                    //asssign new hit-point and update face
                    bulletHole.position = translatedHitPoint;
                    bulletHole.updateFace();

                    //assign new rotations
                    bulletHole.carriersLastFaceAngle = rotZ;
                }
            }
        }

        public static final void rotateForWall( Wall bhc, float rotZ )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //check if this bullet hole belongs to the specified mersh
                if ( bulletHole.carrier == bhc )
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
                if ( new Point2D.Float( bulletHole.hitPointX, bulletHole.hitPointY ).distance( new Point2D.Float( hitPointX, hitPointY ) ) < MIN_POINT_DISTANCE_FOR_SAME_LAYER_MULTIPLIER * bulletHoleSize.size )
                {
                    dis += STEP_DISTANCE_FROM_FACE;
                }
            }

            return dis;
        }

        private final void rotateAroundVertex( LibVertex vertex, float rotZ )
        {
            //increase saved rot z
            ShooterDebug.bullethole.out( "turn for [" + rotZ + "]" );

            //transform
            LibMatrix transformationMatrix = new LibMatrix( 0.0f, 0.0f, rotZ );
            LibVertex translatedHitPoint   = transformationMatrix.transformVertex( position, vertex );

            //increase face angle for rotZ
            horzFaceAngle += rotZ;
            ShooterDebug.bullethole.out( "setting bullet hole angle to [" + horzFaceAngle + "]" );

            //asssign new hit-point and update face
            position = translatedHitPoint;

            //update the bullet hole
            updateFace();
        }
    }
