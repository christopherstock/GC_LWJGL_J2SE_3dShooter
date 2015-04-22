/*  $Id: FireArm.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.fx.FXSliver.*;
    import  de.christopherstock.shooter.game.wearpon.Ammo.AmmoType;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;
    import  de.christopherstock.shooter.ui.hud.HUD.*;

    /**************************************************************************************
    *   A Wearpons that uses ammo.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public final class FireArm implements WearponKind
    {
        private                     int                 iMagazineSize                   = 0;
        private                     int                 iMagazineAmmo                   = 0;
        private                     AmmoType            iAmmoType                       = null;
        private                     int                 iWearponIrregularityVert        = 0;
        private                     int                 iWearponIrregularityHorz        = 0;
        private                     int                 iCurrentShotCount               = 0;
        private                     int                 iCurrentShotCountRandomMod      = 0;
        private                     Sound               iUseSound                       = null;
        private                     Sound               iReloadSound                    = null;

        public FireArm( AmmoType aAmmoType, int aMagazineSize, int aWearponIrregularityDepth, int aWearponIrregularityAngle, int aCurrentShotCount, int aCurrentShotCountRandomMod, Sound aUseSound, Sound aReloadSound )
        {
            iMagazineSize               = aMagazineSize;
            iAmmoType                   = aAmmoType;
            iWearponIrregularityVert    = aWearponIrregularityDepth;
            iWearponIrregularityHorz    = aWearponIrregularityAngle;
            iCurrentShotCount           = aCurrentShotCount;
            iCurrentShotCountRandomMod  = aCurrentShotCountRandomMod;
            iUseSound                   = aUseSound;
            iReloadSound                = aReloadSound;
        }

        @Override
        public boolean fire( Wearpon w )
        {
            //only fire if magazine is not empty
            if ( iMagazineAmmo > 0 )
            {
                //reduce magazine-ammo by 1
                --iMagazineAmmo;

                //launch shot-sound-fx
                if ( iUseSound != null )
                {
                    iUseSound.playGlobalFx();
                }

                //launch bullet shell sound-fx
                Sound.EBulletShell1.playGlobalFx( 8 );

                //launch number of shots this gun fires
                int shotsToFire = getCurrentShotCount();
                for ( int i = 0; i < shotsToFire; ++i )
                {
                    //clear all debug fx points before firing!
                    FX.clearDebugPoints();

                    //get shot from player
                    Shot shot = GameLevel.currentPlayer().getShot();

                    //draw a debug shot line
                    shot.drawShotLine();

                    //launch the shot on the current level
                    shot.launch();
                }

                return true;
            }

            return false;
        }

        @Override
        public void reload( Wearpon w )
        {
            //if ammo to reload is available
            if ( GameLevel.currentPlayer().getAmmo().getAmmo( iAmmoType ) > 0 )
            {
                //if the wearpon is not fully loaded
                if ( iMagazineSize != iMagazineAmmo )
                {
                    //launch sound-fx
                    if ( iReloadSound != null )
                    {
                        iReloadSound.playGlobalFx();
                    }

                    //start HUD-animation 'hide'
                    HUD.getSingleton().startHudAnimation( Animation.EAnimationHide, ChangeAction.EActionReload );

                    //put unused ammo back onto the stack!
                    GameLevel.currentPlayer().getAmmo().addAmmo( iAmmoType, iMagazineAmmo );

                    //check ammo stock
                    int ammo = GameLevel.currentPlayer().getAmmo().getAmmo( iAmmoType );
                    int ammoToReload = ( ammo >= iMagazineSize ? iMagazineSize : ammo );

                    //load ammoToReload into the magazine and substract it from the ammo stock
                    iMagazineAmmo = ammoToReload;
                    GameLevel.currentPlayer().getAmmo().substractAmmo( iAmmoType, ammoToReload );

                    //start HUD-animation 'show'
                    HUD.getSingleton().startHudAnimation( Animation.EAnimationShow, null );
                }
            }
        }

        @Override
        public boolean isReloadable()
        {
            return true;
        }

        public final int getCurrentShotCount()
        {
            return iCurrentShotCount + LibMath.getRandom( -iCurrentShotCountRandomMod, iCurrentShotCountRandomMod );
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

        public final String getCurrentAmmoStringMagazineAmmo()
        {
            return String.valueOf( iMagazineAmmo );
        }

        public final String getCurrentAmmoStringTotalAmmo()
        {
            return String.valueOf( GameLevel.currentPlayer().getAmmo().getAmmo( iAmmoType ) );
        }

        public final LibGLImage getAmmoTypeImage()
        {
            return iAmmoType.getImage();
        }

        public final ParticleQuantity getSliverParticleQuantity()
        {
            return iAmmoType.iSliverParticleQuantity;
        }

        public final LibHoleSize getBulletHoleSize()
        {
            return iAmmoType.iBulletHoleSize;
        }
    }
