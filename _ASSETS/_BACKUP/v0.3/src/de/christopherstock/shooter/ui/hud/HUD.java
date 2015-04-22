/*  $Id: HUD.java 269 2011-02-11 18:44:52Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLPanel.*;
    import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.ShooterSettings.Colors;
import de.christopherstock.shooter.ShooterSettings.Fonts;
import de.christopherstock.shooter.ShooterSettings.Offset;
    import  de.christopherstock.shooter.game.*;
import  de.christopherstock.shooter.ui.hud.AvatarMessage.AvatarImage;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class HUD implements GLCallback2D
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

        public          static          HUD             singleton                   = null;
        public          static          LibGLImage      bgImage                     = null;

        protected                       Animation       animationState              = Animation.EAnimationNone;
        private                         ChangeAction    actionAfterHide             = null;

        protected                       int             animationPlayerRightHand    = 0;

        @Override
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
                //TODO HIGHEST cache the image !! to private!
                LibGLImage ammoImg = LibGLImage.getFromString
                (
                                Level.currentPlayer().getWearpon().getMagazineAmmo()
                    + " | " +   ( ShooterSettings.HUD.SHOW_MAGAZINE_SIZE ? "" : Level.currentPlayer().getWearpon().getMagazineSize() + " | " )
                    +           Level.currentPlayer().getAmmo().getAmmo( Level.currentPlayer().getWearpon().getAmmoType() ),
                    Fonts.EAmmo,
                    Colors.EFpsFg.colARGB,
                    null,
                    Colors.EFpsOutline.colARGB,
                    ShooterDebugSystem.glimage,
                    null
                );
                LibGL3D.view.drawOrthoBitmapBytes( ammoImg, LibGL3D.panel.width - Offset.EBorderHudX - ammoImg.width, Offset.EBorderHudY );
            }

/*
*/
//LibGLView.singleton.drawOrthoBitmapBytes( ammoImg, GLPanel.singleton.width - Offset.EBorderHudX - ammoImg.width, Offset.EBorderHudY );

/*
            //draw player's health
            GLImage healthImg = GLImage.getFromString( String.valueOf( Level.getCurrentPlayer().getHealth() ), Fonts.EHealth, Colors.EHealthFg.col, null, Colors.EHealthOutline.col );
            GLView.singleton.drawOrthoBitmapBytes( healthImg, Offset.EBorderHudX, GLPanel.singleton.height - Offset.EBorderHudY - healthImg.height );
*/
            //draw player's health
            //HUD.releaseClip( g );
            //Strings.drawString( g, , , GLPanel.glPanel.height - Offset.EBorderHudY );

            //draw debug logs
            //Level.currentPlayer().drawDebugLog(      g );
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
            if ( animationPlayerRightHand > 0 )
            {
                switch ( animationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }

                    case EAnimationShow:
                    {
                        --animationPlayerRightHand;

                        //check if animation is over
                        if ( animationPlayerRightHand == 0 )
                        {
                            animationState = HUD.Animation.EAnimationNone;
                        }
                        break;
                    }

                    case EAnimationHide:
                    {
                        --animationPlayerRightHand;

                        //check if animation is over
                        if ( animationPlayerRightHand == 0 )
                        {
                            switch ( actionAfterHide )
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
                                    animationState = Animation.EAnimationShow;
                                    animationPlayerRightHand = ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
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
            animationPlayerRightHand    = ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
            animationState              = newAnimationState;
            actionAfterHide             = newActionAfterHide;
        }

        public final boolean animationActive()
        {
            return ( animationPlayerRightHand != 0 );
        }
    }
