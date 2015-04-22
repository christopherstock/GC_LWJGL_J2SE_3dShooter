/*  $Id: BulletHole.java 642 2011-04-24 01:38:53Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a bullet-hole on a wall or an object.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class BulletHole
    {
        private     static  final   float                   MIN_DISTANCE_FROM_FACE                          = 0.001f;
        private     static  final   float                   STEP_DISTANCE_FROM_FACE                         = 0.0001f;
        private     static  final   float                   MIN_POINT_DISTANCE_FOR_SAME_LAYER_MULTIPLIER    = 4;   //POINT_SIZE * 4;

        private     static          Vector<BulletHole>      bulletHoles                                     = new Vector<BulletHole>();

        private                     GameObject              iCarrier                                        = null;
        private                     FaceEllipseWall         iFace                                           = null;
        private                     LibGLTexture            iTexture                                        = null;
        private                     LibVertex               iOriginalPosition                               = null;
        private                     LibVertex               iPosition                                       = null;
        private                     float                   iHitPointX                                      = 0.0f;
        private                     float                   iHitPointY                                      = 0.0f;
        private                     float                   iHitPointZ                                      = 0.0f;
        private                     float                   iHorzFaceAngle                                  = 0.0f;
        private                     float                   iVertFaceAngle                                  = 0.0f;
        private                     float                   iCarriersLastFaceAngle                          = 0.0f;
        private                     LibHoleSize             iBulletHoleSize                                 = null;

        public BulletHole( HitPoint hitPoint )
        {
            iHitPointX          = hitPoint.vertex.x;
            iHitPointY          = hitPoint.vertex.y;
            iHitPointZ          = hitPoint.vertex.z;
            iHorzFaceAngle      = hitPoint.horzFaceAngle;
            iVertFaceAngle      = hitPoint.vertFaceAngle;
            iCarrier            = hitPoint.carrier;
            iTexture            = hitPoint.texture;
            iBulletHoleSize     = hitPoint.holeSize;

//ShooterDebug.bugfix.out( "Vert face angle set to [" + iVertFaceAngle + "]" );

            ShooterDebug.bullethole.out( "new bullet hole: face-angle h [" + iHorzFaceAngle + "] v [" + iVertFaceAngle + "]" );

            //get suitable distance to avoid overlapping bullet-holes
            float distanceHorzFromFace  = getSuitableDistanceFromHorzFace();
            float distanceVertFromFace  = getSuitableDistanceFromVertFace();
            float distX                 = -distanceHorzFromFace * -LibMath.cosDeg( hitPoint.horzFaceAngle );
            float distY                 =  distanceHorzFromFace *  LibMath.sinDeg( hitPoint.horzFaceAngle );
            float distZ                 = -distanceVertFromFace *  LibMath.sinDeg( hitPoint.vertFaceAngle );

            //get the two holes that hit the wall from both sides
            Point2D.Float pointHorz1 = new Point2D.Float( hitPoint.vertex.x + distX, hitPoint.vertex.y + distY );
            Point2D.Float pointHorz2 = new Point2D.Float( hitPoint.vertex.x - distX, hitPoint.vertex.y - distY );
            ShooterDebug.bullethole.out( "bullet-hole: ["+pointHorz1.x+"]["+pointHorz1.y+"] ["+pointHorz2.x+"]["+pointHorz2.y+"]" );

            Point2D.Float pointVert1 = new Point2D.Float( hitPoint.srcPointVert.x, hitPoint.vertex.z - distZ );
            Point2D.Float pointVert2 = new Point2D.Float( hitPoint.srcPointVert.x, hitPoint.vertex.z + distZ );

            //select the nearer point
            float distanceHorz1 = (float)hitPoint.srcPointHorz.distance( pointHorz1 );
            float distanceHorz2 = (float)hitPoint.srcPointHorz.distance( pointHorz2 );
            Point2D.Float nearerHolePointHorz = ( distanceHorz1 < distanceHorz2 ? pointHorz1 : pointHorz2 );

            float distanceVert1 = (float)hitPoint.srcPointVert.distance( pointVert1 );
            float distanceVert2 = (float)hitPoint.srcPointVert.distance( pointVert2 );
/*
            if ( distanceVert1 < distanceVert2 )
            {
                ShooterDebug.bugfix.out( "higher is nearer" );
            }
            else
            {
                ShooterDebug.bugfix.out( "lower is nearer" );
            }
*/
            Point2D.Float nearerHolePointVert = ( distanceVert1 < distanceVert2 ? pointVert1 : pointVert2 );

            //ShooterDebug.bugfix.out( "dis v: "  + distZ );

            //assign the position of the Bullet Hole
            iOriginalPosition  = new LibVertex( nearerHolePointHorz.x, nearerHolePointHorz.y, nearerHolePointVert.y );
            iPosition          = new LibVertex( nearerHolePointHorz.x, nearerHolePointHorz.y, nearerHolePointVert.y );

            //check distance to Carrier if any
            if ( iCarrier != null )
            {
                iCarriersLastFaceAngle = iCarrier.getCarriersFaceAngle();
            }

            //setup the face
            updateFace();
        }

        private final void updateFace()
        {
            iFace = new FaceEllipseWall
            (
                iTexture,
                iHorzFaceAngle,
                iVertFaceAngle,
                iPosition.x,
                iPosition.y,
                iPosition.z,
                iBulletHoleSize.size,
                iBulletHoleSize.size,
                LibMath.getRandom( 0, 360 )     // get random texture rotation for the bullet hole
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
                bulletHole.iFace.draw();
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
                if ( bulletHole.iCarrier == bhc )
                {
                    //translate it!
                    bulletHole.iPosition.x           += transX;
                    bulletHole.iPosition.y           += transY;
                    bulletHole.iOriginalPosition.x   += transX;
                    bulletHole.iOriginalPosition.y   += transY;
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
                if ( bulletHole.iCarrier == bhc )
                {
                    //reverse last translation
                    LibMatrix transformationMatrix = new LibMatrix( 0.0f, 0.0f, rotZ - bulletHole.iCarriersLastFaceAngle );
                    LibVertex translatedHitPoint = transformationMatrix.transformVertex( bulletHole.iPosition, bhc.getAnchor() );

                    //assign new face angle
                    bulletHole.iHorzFaceAngle += ( rotZ - bulletHole.iCarriersLastFaceAngle );

                    //asssign new hit-point and update face
                    bulletHole.iPosition = translatedHitPoint;
                    bulletHole.updateFace();

                    //assign new rotations
                    bulletHole.iCarriersLastFaceAngle = rotZ;
                }
            }
        }

        public static final void rotateForWall( Wall bhc, float rotZ )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //check if this bullet hole belongs to the specified mersh
                if ( bulletHole.iCarrier == bhc )
                {
                    //rotate bulletHole for mesh
                    bulletHole.rotateAroundVertex( bhc.getAnchor(), rotZ );
                }
            }
        }

        public static final void removeForWall( Wall bhc )
        {
            //browse all bullet-holes reversed
            for ( int i = bulletHoles.size() - 1; i >= 0; --i )
            {
                //check if this bullet hole belongs to the specified mersh
                if ( bulletHoles.elementAt( i ).iCarrier == bhc )
                {
                    //rotate bulletHole for mesh
                    bulletHoles.removeElementAt( i );
                }
            }
        }

        private final float getSuitableDistanceFromHorzFace()
        {
            float dis = MIN_DISTANCE_FROM_FACE;

            //check distance to all other bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //should be distance3d :/
                if ( new Point2D.Float( bulletHole.iHitPointX, bulletHole.iHitPointY ).distance( new Point2D.Float( iHitPointX, iHitPointY ) ) < MIN_POINT_DISTANCE_FOR_SAME_LAYER_MULTIPLIER * iBulletHoleSize.size )
                {
                    dis += STEP_DISTANCE_FROM_FACE;
                }
            }

            return dis;
        }

        private final float getSuitableDistanceFromVertFace()
        {
            float dis = MIN_DISTANCE_FROM_FACE;

            //check distance to all other bullet-holes
            for ( BulletHole bulletHole : bulletHoles )
            {
                //should be distance3d :/
                if ( Math.abs( bulletHole.iHitPointZ - iHitPointZ ) < MIN_POINT_DISTANCE_FOR_SAME_LAYER_MULTIPLIER * iBulletHoleSize.size )
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
            LibVertex translatedHitPoint   = transformationMatrix.transformVertex( iPosition, vertex );

            //increase face angle for rotZ
            iHorzFaceAngle += rotZ;
            ShooterDebug.bullethole.out( "setting bullet hole angle to [" + iHorzFaceAngle + "]" );

            //asssign new hit-point and update face
            iPosition = translatedHitPoint;

            //update the bullet hole
            updateFace();
        }
    }
