/*  $Id: HitPoint.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  de.christopherstock.shooter.base.*;
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
    *   @version    0.3.5
    **************************************************************************************/
    public class HitPoint
    {
        public                      GameObject              iCarrier                    = null;

        protected                   LibVertex               iVertex                     = null;
        protected                   LibGLTexture            iTexture                    = null;
        protected                   LibColors[]             iSliverColors               = null;

        private                     float                   iHorzDistance               = 0.0f;
        protected                   float                   iHorzShotAngle              = 0.0f;
        protected                   float                   iHorzInvertedShotAngle      = 0.0f;
        protected                   float                   iHorzSliverAngle            = 0.0f;
        protected                   float                   iHorzFaceAngle              = 0.0f;

        protected                   LibHoleSize             iHoleSize                   = null;
        protected                   Point2D.Float           iSrcPointHorz               = null;
        protected                   Point2D.Float           iSrcPointVert               = null;

        protected                   float                   iVertDistance               = 0.0f;
        protected                   float                   iVertShotAngle              = 0.0f;
        protected                   float                   iVertInvertedShotAngle      = 0.0f;
        private                     float                   iVertSliverAngle            = 0.0f;
        protected                   float                   iVertFaceAngle              = 0.0f;

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
            iCarrier                 = aCarrier;
            iTexture                 = aTexture;
            iSliverColors            = aSliverColors;

            iHoleSize                = aBulletHoleSize;
            iSrcPointHorz            = aSrcPointHorz;
            iSrcPointVert            = aSrcPointVert;
            iVertex                  = aVertex;

            iHorzShotAngle           = aHorzShotAngle;
            iHorzInvertedShotAngle   = aHorzInvertedShotAngle;
            iHorzSliverAngle         = aHorzSliverAngle;
            iHorzFaceAngle           = aHorzFaceAngle;
            iHorzDistance            = aHorzDistance;

            iVertDistance            = aVertDistance;
            iVertShotAngle           = LibMath.getAngleCorrect( iSrcPointVert, new Point2D.Float( (float)iSrcPointHorz.distance( new Point2D.Float( iVertex.x, iVertex.y ) ), iVertex.z ) );
            iVertInvertedShotAngle   = 360.0f - ( iVertShotAngle - 180.0f  );
            iVertSliverAngle         = LibMath.normalizeAngle( 180.0f - iVertShotAngle );
            iVertFaceAngle           = aVertFaceAngle;
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
                    ||  ( nearestDistance > hitPoints.elementAt( i ).iHorzDistance )
                )
                {
                    nearestIndex    = i;
                    nearestDistance = hitPoints.elementAt( i ).iHorzDistance;

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
            for ( float distance = 0.0f; distance < Sliver_RANGE; distance += 0.1f )
            {
                FX.launchStaticPoint
                (
                    new LibVertex
                    (
                        iVertex.x - LibMath.sinDeg( iHorzSliverAngle         ) * distance,
                        iVertex.y - LibMath.cosDeg( iHorzSliverAngle         ) * distance,
                        iVertex.z - LibMath.sinDeg( iVertSliverAngle - 90.0f ) * distance
                    ),
                    LibColors.ERed,
                    50,
                    DebugSettings.DEBUG_POINT_SIZE
                );
            }

            //launch sliver fx on this hole
            FX.launchSliver( iVertex, iSliverColors, iHorzSliverAngle, ( (FireArm)ShooterGameLevel.currentPlayer().getCurrentWearpon().iWearponBehaviour ).getSliverParticleQuantity() );
        }
    }
