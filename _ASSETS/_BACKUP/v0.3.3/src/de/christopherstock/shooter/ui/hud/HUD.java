/*  $Id: HUD.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Colors;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;
    import  de.christopherstock.shooter.ShooterSettings.OffsetsOrtho;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.ui.hud.AvatarMessage.AvatarImage;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class HUD
    {
        public enum ItemGroup
        {
            EWearpon,
            EGadget,
            ;
        }

        public enum Animation
        {
            EAnimationNone,
            EAnimationShow,
            EAnimationHide,
            ;
        }

        public enum ChangeAction
        {
            EActionNext,
            EActionPrevious,
            EActionReload,
            ;
        }

        private         static          HUD             singleton                       = null;

        public                          Animation       iAnimationState                 = Animation.EAnimationNone;
        private                         ChangeAction    iActionAfterHide                = null;

        private                         LibGLImage      iAmmoImageMagazineAmmo          = null;
        private                         LibGLImage      iAmmoImageTotalAmmo             = null;
        private                         String          iDisplayAmmoStringMagazineAmmo  = null;
        private                         String          iDisplayAmmoStringTotalAmmo     = null;
        private                         String          iCurrentAmmoStringMagazineAmmo  = null;
        private                         String          iCurrentAmmoStringTotalAmmo     = null;

        private                         LibGLImage      iHealthImage                    = null;
        private                         String          iDisplayHealthString            = null;
        private                         String          iCurrentHealthString            = null;

        public                          int             iAnimationPlayerRightHand       = 0;

        public static final HUD getSingleton()
        {
            if ( singleton == null ) singleton = new HUD();
            return singleton;
        }

        public static final void init()
        {
            //load all images
            getSingleton();
            Wearpon.loadImages();
            Ammo.loadImages();
            Gadget.loadImages();
            BackGround.loadImages();
            AvatarImage.loadImages();
        }

        public final void draw2D()
        {
            //level may not be initialized
            if ( GameLevel.current() != null )
            {
                //draw player's wearpon or gadget
                GameLevel.currentPlayer().drawWearponOrGadget();
            }

            //draw avatar message ( if active )
            AvatarMessage.drawMessage();

            //draw all hud messages
            HUDMessage.drawAllMessages();

            //draw fullscreen hud effects
            HUDFx.drawHUDEffects();

            //draw frames per second last
            Fps.draw();

            //draw ammo if the wearpon uses ammo
            if ( GameLevel.currentPlayer().showAmmoInHUD() )
            {
                drawAmmo();
            }

            //draw health
            drawHealth();

            //draw debug logs
            //Level.currentPlayer().drawDebugLog(      g );
        }

        private void drawAmmo()
        {
            Wearpon     currentWearpon = GameLevel.currentPlayer().getWearpon();

            //only if this is a reloadable wearpon
            if ( currentWearpon.isReloadable() )
            {
                FireArm fireWearpon = (FireArm)currentWearpon.iWearponBehaviour;

                //create current ammo string
                iCurrentAmmoStringMagazineAmmo  = fireWearpon.getCurrentAmmoStringMagazineAmmo();
                iCurrentAmmoStringTotalAmmo     = fireWearpon.getCurrentAmmoStringTotalAmmo();

                //recreate ammo image if changed
                if ( iDisplayAmmoStringMagazineAmmo == null || !iDisplayAmmoStringTotalAmmo.equals( iCurrentAmmoStringTotalAmmo ) || !iDisplayAmmoStringMagazineAmmo.equals( iCurrentAmmoStringMagazineAmmo ) )
                {
                    iDisplayAmmoStringMagazineAmmo = iCurrentAmmoStringMagazineAmmo;
                    iDisplayAmmoStringTotalAmmo    = iCurrentAmmoStringTotalAmmo;
                    iAmmoImageMagazineAmmo = LibGLImage.getFromString
                    (
                        iDisplayAmmoStringMagazineAmmo,
                        Fonts.EAmmo,
                        Colors.EFpsFg.colARGB,
                        null,
                        Colors.EFpsOutline.colARGB,
                        ShooterDebug.glimage
                    );
                    iAmmoImageTotalAmmo = LibGLImage.getFromString
                    (
                        iDisplayAmmoStringTotalAmmo,
                        Fonts.EAmmo,
                        Colors.EFpsFg.colARGB,
                        null,
                        Colors.EFpsOutline.colARGB,
                        ShooterDebug.glimage
                    );
                }

                //draw shell image
                LibGL3D.view.drawOrthoBitmapBytes( fireWearpon.getAmmoTypeImage(), LibGL3D.panel.width - OffsetsOrtho.EBorderHudX - 50, OffsetsOrtho.EBorderHudY );

                //draw magazine ammo
                LibGL3D.view.drawOrthoBitmapBytes( iAmmoImageMagazineAmmo, LibGL3D.panel.width - OffsetsOrtho.EBorderHudX - 50 - fireWearpon.getAmmoTypeImage().width - iAmmoImageMagazineAmmo.width, OffsetsOrtho.EBorderHudY );

                //draw total ammo
                LibGL3D.view.drawOrthoBitmapBytes( iAmmoImageTotalAmmo, LibGL3D.panel.width - OffsetsOrtho.EBorderHudX - iAmmoImageTotalAmmo.width, OffsetsOrtho.EBorderHudY );
            }
        }

        private void drawHealth()
        {
            //draw player health
            iCurrentHealthString = String.valueOf( GameLevel.currentPlayer().getHealth() );

            //recreate ammo image if changed
            if ( iDisplayHealthString == null || !iDisplayHealthString.equals( iCurrentHealthString ) )
            {
                iDisplayHealthString = iCurrentHealthString;
                iHealthImage = LibGLImage.getFromString
                (
                    iDisplayHealthString,
                    Fonts.EHealth,
                    Colors.EHealthFg.colARGB,
                    null,
                    Colors.EHealthOutline.colARGB,
                    ShooterDebug.glimage
                );
            }

            //draw health image
            LibGL3D.view.drawOrthoBitmapBytes( iHealthImage, OffsetsOrtho.EBorderHudX, OffsetsOrtho.EBorderHudY );
        }

        public final void animateShowHide()
        {
            if ( iAnimationPlayerRightHand > 0 )
            {
                switch ( iAnimationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }

                    case EAnimationShow:
                    {
                        --iAnimationPlayerRightHand;

                        //check if animation is over
                        if ( iAnimationPlayerRightHand == 0 )
                        {
                            iAnimationState = HUD.Animation.EAnimationNone;
                        }
                        break;
                    }

                    case EAnimationHide:
                    {
                        --iAnimationPlayerRightHand;

                        //check if animation is over
                        if ( iAnimationPlayerRightHand == 0 )
                        {
                            switch ( iActionAfterHide )
                            {
                                case EActionNext:
                                {
                                    GameLevel.currentPlayer().chooseNextWearponOrGadget();
                                    break;
                                }

                                case EActionPrevious:
                                {
                                    GameLevel.currentPlayer().choosePreviousWearponOrGadget();
                                    break;
                                }

                                case EActionReload:
                                {
                                    //reload & just show the same gun
                                    GameLevel.currentPlayer().getWearpon().reload();
                                    iAnimationState = Animation.EAnimationShow;
                                    iAnimationPlayerRightHand = ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                                    break;
                                }

                            }
                        }
                        break;
                    }
                }
            }
        }

        public final void startHudAnimation( Animation newAnimationState, ChangeAction newActionAfterHide )
        {
            iAnimationPlayerRightHand    = ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
            iAnimationState              = newAnimationState;
            iActionAfterHide             = newActionAfterHide;
        }

        public final boolean animationActive()
        {
            return ( iAnimationPlayerRightHand != 0 );
        }
    }
