/*  $Id: HUD.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Colors;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;
    import  de.christopherstock.shooter.ShooterSettings.OffsetsOrtho;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.ui.hud.AvatarMessage.AvatarImage;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
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

        public          static          HUD             singleton                       = null;

        protected                       Animation       iAnimationState                 = Animation.EAnimationNone;
        private                         ChangeAction    iActionAfterHide                = null;

        private                         LibGLImage      iAmmoImage                      = null;
        private                         String          iDisplayAmmoString              = null;
        private                         String          iCurrentAmmoString              = null;

        private                         LibGLImage      iHealthImage                    = null;
        private                         String          iDisplayHealthString            = null;
        private                         String          iCurrentHealthString            = null;

        protected                       int             iAnimationPlayerRightHand       = 0;

        public final void draw2D()
        {
            //level may not be initialized
            if ( Level.current() != null )
            {
                //draw player's wearpon or gadget
                Level.currentPlayer().drawWearponOrGadget();
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
            if ( Level.currentPlayer().showAmmoInHUD() )
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
            //create current ammo string
            iCurrentAmmoString =
            (
                    Level.currentPlayer().getWearpon().getMagazineAmmo()
                +   " | " + ( ShooterSettings.HUD.SHOW_MAGAZINE_SIZE ? "" : Level.currentPlayer().getWearpon().getMagazineSize() + " | " )
                +   Level.currentPlayer().getAmmo().getAmmo( Level.currentPlayer().getWearpon().getAmmoType() )
            );

            //recreate ammo image if changed
            if ( iDisplayAmmoString == null || !iDisplayAmmoString.equals( iCurrentAmmoString ) )
            {
                iDisplayAmmoString = iCurrentAmmoString;
                iAmmoImage = LibGLImage.getFromString
                (
                    iDisplayAmmoString,
                    Fonts.EAmmo,
                    Colors.EFpsFg.colARGB,
                    null,
                    Colors.EFpsOutline.colARGB,
                    ShooterDebug.glimage
                );
            }

            //draw ammo image
            LibGL3D.view.drawOrthoBitmapBytes( iAmmoImage, LibGL3D.panel.width - OffsetsOrtho.EBorderHudX - iAmmoImage.width, OffsetsOrtho.EBorderHudY );
        }

        private void drawHealth()
        {
            //draw player health
            iCurrentHealthString = String.valueOf( Level.currentPlayer().getHealth() );

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

        public static final void init()
        {
            //load all images
            singleton = new HUD();
            Wearpon.loadImages();
            Gadget.loadImages();
            BackGround.loadImages();
            AvatarImage.loadImages();
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
                                    Level.currentPlayer().chooseNextWearponOrGadget();
                                    break;
                                }

                                case EActionPrevious:
                                {
                                    Level.currentPlayer().choosePreviousWearponOrGadget();
                                    break;
                                }

                                case EActionReload:
                                {
                                    //reload & just show the same gun
                                    Level.currentPlayer().getWearpon().reload();
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
