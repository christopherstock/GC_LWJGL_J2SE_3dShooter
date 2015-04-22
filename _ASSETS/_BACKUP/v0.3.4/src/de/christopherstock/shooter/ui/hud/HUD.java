/*  $Id: HUD.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  javax.imageio.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Colors;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;
    import  de.christopherstock.shooter.ShooterSettings.HUDSettings;
    import  de.christopherstock.shooter.ShooterSettings.OffsetsOrtho;
    import  de.christopherstock.shooter.game.objects.Player.HealthChangeListener;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.ui.hud.AvatarMessage.AvatarImage;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class HUD implements HealthChangeListener
    {
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

        private                         LibGLImage      iCrosshairImg                   = null;

        public                          int             iAnimationPlayerRightHand       = 0;
        public                          int             iHealthShowTimer                = 0;

        public static final HUD getSingleton()
        {
            if ( singleton == null ) singleton = new HUD();
            return singleton;
        }

        public static final void init()
        {
            //load all images
            getSingleton();
            Artefact.loadImages();
            Ammo.loadImages();
            BackGround.loadImages();
            AvatarImage.loadImages();
        }

        public final void draw2D()
        {
            //level may not be initialized
            if ( ShooterGameLevel.current() != null )
            {
                //draw player's wearpon or gadget
                ShooterGameLevel.currentPlayer().drawWearponOrGadget();
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
            if ( ShooterGameLevel.currentPlayer().showAmmoInHUD() )
            {
                //draw ammo
                drawAmmo();

                //draw crosshair
                //drawCrosshair();
            }

            //draw health if currently changed
            boolean drawHealthWarning = ShooterGameLevel.currentPlayer().isHealthLow();
            if ( iHealthShowTimer > 0 || drawHealthWarning )
            {
                drawHealth( drawHealthWarning );
            }

            //draw debug logs
            //Level.currentPlayer().drawDebugLog(      g );
        }

        private void drawAmmo()
        {
            Artefact     currentWearpon = ShooterGameLevel.currentPlayer().getWearpon();

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

        @Override
        public final void healthChanged()
        {
            iHealthShowTimer = HUDSettings.TICKS_SHOW_HEALTH_AFTER_CHANGE;
        }

        private void drawHealth( boolean drawHealthWarning )
        {
            //draw player health
            iCurrentHealthString = String.valueOf( ShooterGameLevel.currentPlayer().getHealth() );

            //recreate ammo image if changed
            if ( iDisplayHealthString == null || !iDisplayHealthString.equals( iCurrentHealthString ) )
            {
                iDisplayHealthString = iCurrentHealthString;



                iHealthImage = LibGLImage.getFromString
                (
                    iDisplayHealthString,
                    Fonts.EHealth,
                    ( drawHealthWarning ? Colors.EHealthFgWarning.colABGR : Colors.EHealthFgNormal.colABGR ),
                    null,
                    Colors.EHealthOutline.colARGB,
                    ShooterDebug.glimage
                );
            }

            //fade last displayed ticks ( not for health warning )
            float alpha = 1.0f;
            if ( iHealthShowTimer < HUDSettings.TICKS_FADE_OUT_HEALTH && !drawHealthWarning )
            {
                alpha = iHealthShowTimer / (float)HUDSettings.TICKS_FADE_OUT_HEALTH;
            }

            //if ( iHealthShowTimer <  )

            //draw health image
            LibGL3D.view.drawOrthoBitmapBytes( iHealthImage, OffsetsOrtho.EBorderHudX, OffsetsOrtho.EBorderHudY, alpha );
        }

        @Deprecated
        public final void drawCrosshair()
        {
            //create crosshair img if not done yet
            if ( iCrosshairImg == null )
            {
                try
                {
                    iCrosshairImg = new LibGLImage( ImageIO.read( LibGLForm.class.getResourceAsStream( ShooterSettings.Path.EScreen.iUrl + "crosshair.png" ) ), ImageUsage.EOrtho, ShooterDebug.glimage, true );
                }
                catch ( Exception e )
                {
                    ShooterDebug.error.trace( e );
                    System.exit( 0 );
                }
            }

            //draw crosshair
            LibGL3D.view.drawOrthoBitmapBytes( iCrosshairImg, LibGL3D.panel.width / 2 - iCrosshairImg.width / 2, LibGL3D.panel.height / 2 - iCrosshairImg.height / 2 );
        }

        public final void animate()
        {
            //animate HUD-effects
            if ( iHealthShowTimer > 0 ) --iHealthShowTimer;

            //animate right hand
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
                                    ShooterGameLevel.currentPlayer().chooseNextWearponOrGadget();
                                    break;
                                }

                                case EActionPrevious:
                                {
                                    ShooterGameLevel.currentPlayer().choosePreviousWearponOrGadget();
                                    break;
                                }

                                case EActionReload:
                                {
                                    //reload & just show the same gun
                                    ShooterGameLevel.currentPlayer().getWearpon().reload( true );
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
