/*  $Id: FireArm.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.awt.geom.Point2D;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.fx.LibFX.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.game.*;
    import  de.christopherstock.lib.game.LibShot.ShotSpender;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.base.*;

    /**************************************************************************************
    *   A Wearpons that uses ammo.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public final class FireArm extends WearponKind
    {
        public                      ShooterSound        iSoundFire                      = null;
        private                     ShooterSound        iSoundReload                    = null;
        private                     ShooterSound        iSoundBulletShell               = null;
        public                      AmmoType            iAmmoType                       = null;
        private                     int                 iWearponIrregularityVert        = 0;
        private                     int                 iWearponIrregularityHorz        = 0;
        private                     int                 iShotCount                      = 0;
        private                     int                 iShotCountRandomMod             = 0;
        protected                   int                 iMagazineSize                   = 0;

        public FireArm( AmmoType aAmmoType, int aMagazineSize, int aWearponIrregularityDepth, int aWearponIrregularityAngle, int aShotCount, int aShotCountRandomMod, ShooterSound aUseSound, ShooterSound aReloadSound, ShooterSound aBulletShellSound )
        {
            iMagazineSize               = aMagazineSize;
            iAmmoType                   = aAmmoType;
            iWearponIrregularityVert    = aWearponIrregularityDepth;
            iWearponIrregularityHorz    = aWearponIrregularityAngle;
            iShotCount                  = aShotCount;
            iShotCountRandomMod         = aShotCountRandomMod;
            iSoundFire                  = aUseSound;
            iSoundReload                = aReloadSound;
            iSoundBulletShell           = aBulletShellSound;
        }

        @Override
        public int getDamage()
        {
            return iAmmoType.getDamage();
        }

        @Override
        public boolean use( Artefact a, ShotSpender ss, Point2D.Float shooterXY )
        {
            //ShooterDebug.bugfix.out( "Fire FireArm .." );

            //only fire if magazine is not empty
            if ( a.iMagazineAmmo > 0 )
            {
                //ShooterDebug.bugfix.out( "No ammo!" );

                //reduce magazine-ammo by 1
                a.iMagazineAmmo -= 1;

                //launch shot-sound-fx
                if ( iSoundFire != null )
                {
                    if ( shooterXY == null )
                    {
                        iSoundFire.playGlobalFx();
                    }
                    else
                    {
                        iSoundFire.playDistancedFx( shooterXY );
                    }
                }

                //launch bullet shell sound-fx
                if ( iSoundBulletShell != null )
                {
                    if ( shooterXY == null )
                    {
                        iSoundBulletShell.playGlobalFx( 8 );
                    }
                    else
                    {
                        iSoundBulletShell.playDistancedFx( shooterXY, 8 );
                    }
                }

                //launch number of shots this gun fires
                int shotsToFire = getCurrentShotCount();
                for ( int i = 0; i < shotsToFire; ++i )
                {
                    //clear all debug fx points before firing!
                    //LibFXManager.clearDebugPoints();

                    //get shot from player and launch it
                    LibShot s = ss.getShot( 0.0f );
                    //draw the shot line and launch the shot
                    //s.drawShotLine( FxSettings.LIFETIME_DEBUG );
                    ShooterGameLevel.current().launchShot( s );
                }

                return true;
            }

            return false;
        }

        public final int getCurrentShotCount()
        {
            return iShotCount + LibMath.getRandom( -iShotCountRandomMod, iShotCountRandomMod );
        }

        public final float getCurrentIrregularityVert()
        {
            //return modifier-z for the current wearpon
            return ( LibMath.getRandom( -iWearponIrregularityVert, iWearponIrregularityVert ) * 0.01f );
        }

        public final float getCurrentIrregularityHorz()
        {
            //return modifier-z for the current wearpon
            return ( LibMath.getRandom( -iWearponIrregularityHorz, iWearponIrregularityHorz ) * 0.01f );
        }

        public final LibGLImage getAmmoTypeImage()
        {
            return iAmmoType.getImage();
        }

        @Override
        public final Lib.ParticleQuantity getSliverParticleQuantity()
        {
            return iAmmoType.iSliverParticleQuantity;
        }

        @Override
        public final FXSize getSliverParticleSize()
        {
            return iAmmoType.iSliverParticleSize;
        }

        @Override
        public final LibHoleSize getBulletHoleSize()
        {
            return iAmmoType.iBulletHoleSize;
        }

        protected final ShooterSound getReloadSound()
        {
            return iSoundReload;
        }
    }