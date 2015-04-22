/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  java.util.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.util.*;

    /**************************************************************************************
    *   The effect system.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class HitPoint
    {
        public enum Carrier
        {
            EWall,
            EPlayer,
            EBot,
        }

        public                      Carrier                 carrier                     = null;
        protected                   Shot                    shot                        = null;

        /**************************************************************************************
        *   The mesh this hitPoint is connected to
        *   or <code>null</code> if it is not connected to a mesh.
        **************************************************************************************/
        protected                   HitPointCarrier       bhc                        = null;

        protected                   Vertex                  vertex                      = null;

        private                     float                   horzDistance                = 0.0f;
        protected                   float                   horzShotAngle               = 0.0f;
        protected                   float                   horzInvertedShotAngle       = 0.0f;
        private                     float                   horzSliverAngle             = 0.0f;
        protected                   float                   horzFaceAngle               = 0.0f;

        protected                   float                   vertDistance                = 0.0f;
        protected                   float                   vertShotAngle               = 0.0f;
        protected                   float                   vertInvertedShotAngle       = 0.0f;
        private                     float                   vertSliverAngle             = 0.0f;
        protected                   float                   vertFaceAngle               = 0.0f;

        public HitPoint
        (
            Carrier carrier,
            Shot shot,

            float x,
            float y,
            float z,

            float horzDistance,
            float horzShotAngle,
            float horzInvertedShotAngle,
            float horzSliverAngle,
            float horzFaceAngle,

            float vertDistance,
            float vertShotAngle,
            float vertInvertedShotAngle,
            float vertSliverAngle,
            float vertFaceAngle
        )
        {
            this.carrier                = carrier;
            this.shot                   = shot;

            this.vertex                 = new Vertex( x, y, z );

            this.horzShotAngle          = horzShotAngle;
            this.horzInvertedShotAngle  = horzInvertedShotAngle;
            this.horzSliverAngle        = horzSliverAngle;
            this.horzFaceAngle          = horzFaceAngle;
            this.horzDistance           = horzDistance;

            this.vertShotAngle          = vertShotAngle;
            this.vertInvertedShotAngle  = vertInvertedShotAngle;
            this.vertSliverAngle        = vertSliverAngle;
            this.vertFaceAngle          = vertFaceAngle;
            this.vertDistance           = vertDistance;

        }

        public static final HitPoint getNearestHitPoint( Vector<HitPoint> hitPoints )
        {
            //return null if no hit point was found
            if ( hitPoints.size() == 0 )
            {
                Debug.hitpoint.out( "no hit points found!" );
                return null;
            }

            //get the nearest hit-point
            float nearestDistance = 0.0f;
            int   nearestIndex    = -1;
            for ( int i = 0; i < hitPoints.size(); ++i )
            {
                //assign 1st point
                if
                (
                        ( nearestIndex == -1 )
                    ||  ( nearestDistance > hitPoints.elementAt( i ).horzDistance )
                )
                {
                    nearestIndex    = i;
                    nearestDistance = hitPoints.elementAt( i ).horzDistance;

                }
            }

            Debug.hitpoint.out( "exact nearest hitPoint-distance to player: [" + nearestDistance + "]" );

            //return nearest hit-point
            return hitPoints.elementAt( nearestIndex );

        }

        public void debugOut()
        {
            Debug.hitpoint.out( "hit point: [" + vertex.x + "," + vertex.y + ", " + vertex.z + "]" );    //playerAngle [" + Player.rotZ + "]
            Debug.hitpoint.out( "shotAngle [" + horzShotAngle + "] SliverAngle [" + horzSliverAngle + "] invertedShotAngle [" + horzInvertedShotAngle + "] faceAngle [" + horzFaceAngle + "]" );
        }

        protected final void drawWallSliver()
        {
            final   float   Sliver_RANGE = 0.5f;

            //draw shot line with own calculations
            for ( float distance = 0.0f; distance < Sliver_RANGE; distance += 0.01f )
            {
                DebugPoint.add
                (
                    Colors.ERed,
                    vertex.x - UMath.sinDeg( horzSliverAngle         ) * distance,
                    vertex.y - UMath.cosDeg( horzSliverAngle         ) * distance,
                    vertex.z - UMath.sinDeg( vertSliverAngle - 90.0f ) * distance,
                    DebugPoint.ELifetimeMedium
                );

            }
        }

        public final void connectToCarrier( HitPointCarrier aBhc )
        {
            this.bhc = aBhc;
        }
    }
