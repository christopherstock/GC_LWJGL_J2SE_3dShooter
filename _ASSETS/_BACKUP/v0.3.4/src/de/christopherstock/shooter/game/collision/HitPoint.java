/*  $Id: HitPoint.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  de.christopherstock.shooter.game.wearpon.*;
    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.DebugSettings;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a point of shot collision.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class HitPoint
    {
        public                      GameObject              carrier                     = null;

        protected                   LibVertex               vertex                      = null;
        protected                   LibGLTexture            texture                     = null;
        protected                   LibColors[]             sliverColors                = null;

        private                     float                   horzDistance                = 0.0f;
        protected                   float                   horzShotAngle               = 0.0f;
        protected                   float                   horzInvertedShotAngle       = 0.0f;
        protected                   float                   horzSliverAngle             = 0.0f;
        protected                   float                   horzFaceAngle               = 0.0f;

        protected                   LibHoleSize             holeSize                    = null;
        protected                   Point2D.Float           srcPointHorz                = null;
        protected                   Point2D.Float           srcPointVert                = null;

        protected                   float                   vertDistance                = 0.0f;
        protected                   float                   vertShotAngle               = 0.0f;
        protected                   float                   vertInvertedShotAngle       = 0.0f;
        private                     float                   vertSliverAngle             = 0.0f;
        protected                   float                   vertFaceAngle               = 0.0f;

        public HitPoint
        (
            GameObject              aCarrier,
            LibGLTexture            aTexture,
            LibColors[]             aSliverColors,
            LibVertex               aVertex,

            LibHoleSize             aBulletHoleSize,
            Point2D.Float           aSrcPointHorz,
            Point2D.Float           aSrcPointVert,

            float                   aHorzDistance,
            float                   aHorzShotAngle,
            float                   aHorzInvertedShotAngle,
            float                   aHorzSliverAngle,
            float                   aHorzFaceAngle,

            float                   aVertDistance,
            float                   aVertFaceAngle
        )
        {
            carrier                 = aCarrier;
            texture                 = aTexture;
            sliverColors            = aSliverColors;

            holeSize                = aBulletHoleSize;
            srcPointHorz            = aSrcPointHorz;
            srcPointVert            = aSrcPointVert;
            vertex                  = aVertex;

            horzShotAngle           = aHorzShotAngle;
            horzInvertedShotAngle   = aHorzInvertedShotAngle;
            horzSliverAngle         = aHorzSliverAngle;
            horzFaceAngle           = aHorzFaceAngle;
            horzDistance            = aHorzDistance;

            vertDistance            = aVertDistance;
            vertShotAngle           = LibMath.getAngleCorrect( srcPointVert, new Point2D.Float( vertDistance, vertex.z ) );
            vertInvertedShotAngle   = 360.0f - ( vertShotAngle - 180.0f  );
            vertSliverAngle         = 90.0f + 90.0f - vertShotAngle;
            vertFaceAngle           = aVertFaceAngle;
        }

        public static final HitPoint getNearestHitPoint( Vector<HitPoint> hitPoints )
        {
            //return null if no hit point was found
            if ( hitPoints.size() == 0 )
            {
                ShooterDebug.hitpoint.out( "no hit points found!" );
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

            ShooterDebug.hitpoint.out( "exact nearest hitPoint-distance to player: [" + nearestDistance + "]" );

            //return nearest hit-point
            return hitPoints.elementAt( nearestIndex );
        }

        protected final void drawWallSliver()
        {
            final   float   Sliver_RANGE = 0.5f;

            //draw shot line with own calculations
            for ( float distance = 0.0f; distance < Sliver_RANGE; distance += 0.01f )
            {
                FX.launchDebugPoint
                (
                    new LibVertex
                    (
                        vertex.x - LibMath.sinDeg( horzSliverAngle         ) * distance,
                        vertex.y - LibMath.cosDeg( horzSliverAngle         ) * distance,
                        vertex.z - LibMath.sinDeg( vertSliverAngle - 90.0f ) * distance
                    ),
                    LibColors.ERed,
                    50,
                    DebugSettings.DEBUG_POINT_SIZE
                );
            }

            //launch sliver fx on this hole
            FX.launchSliver( vertex, sliverColors, horzSliverAngle, ( (FireArm)ShooterGameLevel.currentPlayer().getCurrentWearpon().iWearponBehaviour ).getSliverParticleQuantity() );
        }
    }
