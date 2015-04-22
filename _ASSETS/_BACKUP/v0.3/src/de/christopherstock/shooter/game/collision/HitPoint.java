/*  $Id: HitPoint.java 224 2011-01-25 18:05:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
import de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a point of shot collision.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class HitPoint
    {
        public                      GameObject              gameObject                  = null;
        protected                   Shot                    shot                        = null;
        protected                   LibVertex               vertex                      = null;
        protected                   Texture                 bulletHoleTexture           = null;

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
            GameObject  aGameObject,
            Shot        aShot,
            LibGLTexture   aBulletHoleTexture,
            LibVertex   aVertex,

            float       aHorzDistance,
            float       aHorzShotAngle,
            float       aHorzInvertedShotAngle,
            float       aHorzSliverAngle,
            float       aHorzFaceAngle,

            float       aVertDistance,
            float       aVertShotAngle,
            float       aVertInvertedShotAngle,
            float       aVertSliverAngle,
            float       aVertFaceAngle
        )
        {
            gameObject              = aGameObject;
            shot                    = aShot;

            if ( aBulletHoleTexture == null )
            {
                bulletHoleTexture = Texture.getBulletHoleTextureForMaterial( LibGLMaterial.EUndefined );
            }
            else
            {
                bulletHoleTexture = Texture.getBulletHoleTextureForMaterial( aBulletHoleTexture.getMaterial() );
            }

            vertex                  = aVertex;

            horzShotAngle           = aHorzShotAngle;
            horzInvertedShotAngle   = aHorzInvertedShotAngle;
            horzSliverAngle         = aHorzSliverAngle;
            horzFaceAngle           = aHorzFaceAngle;
            horzDistance            = aHorzDistance;

            vertShotAngle           = aVertShotAngle;
            vertInvertedShotAngle   = aVertInvertedShotAngle;
            vertSliverAngle         = aVertSliverAngle;
            vertFaceAngle           = aVertFaceAngle;
            vertDistance            = aVertDistance;
        }

        public static final HitPoint getNearestHitPoint( Vector<HitPoint> hitPoints )
        {
            //return null if no hit point was found
            if ( hitPoints.size() == 0 )
            {
                ShooterDebugSystem.hitpoint.out( "no hit points found!" );
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

            ShooterDebugSystem.hitpoint.out( "exact nearest hitPoint-distance to player: [" + nearestDistance + "]" );

            //return nearest hit-point
            return hitPoints.elementAt( nearestIndex );
        }

        public void debugOut()
        {
            ShooterDebugSystem.hitpoint.out( "hit point: [" + vertex.x + "," + vertex.y + ", " + vertex.z + "]" );    //playerAngle [" + Player.rotZ + "]
            ShooterDebugSystem.hitpoint.out( "shotAngle [" + horzShotAngle + "] SliverAngle [" + horzSliverAngle + "] invertedShotAngle [" + horzInvertedShotAngle + "] faceAngle [" + horzFaceAngle + "]" );
        }

        protected final void drawWallSliver()
        {
            final   float   Sliver_RANGE = 0.5f;

            //draw shot line with own calculations
            for ( float distance = 0.0f; distance < Sliver_RANGE; distance += 0.01f )
            {
                LibGLDebugPoint.add
                (
                    LibColors.ERed,
                    vertex.x - LibMath.sinDeg( horzSliverAngle         ) * distance,
                    vertex.y - LibMath.cosDeg( horzSliverAngle         ) * distance,
                    vertex.z - LibMath.sinDeg( vertSliverAngle - 90.0f ) * distance,
                    LibGLDebugPoint.ELifetimeMedium
                );

            }
        }
/*
        public final void connectToCarrier( HitPointCarrier aBhc )
        {
            bhc = aBhc;
        }
*/
    }
