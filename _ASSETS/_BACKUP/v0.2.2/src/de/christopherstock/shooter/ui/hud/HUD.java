/*  $Id: HUD.java 191 2010-12-13 20:24:11Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.GLPanel.GLCallback2D;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.2
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

        private         static          int             MAX_OPACITY_HEALTH_FX       = 255; //64
        private         static          int             MAX_OPACITY_DAMAGE_FX       = 255; //200

        public          static          HUD             singleton                   = null;
        public          static          GLImage         bgImage                     = null;

        private                         int             opacityHealthFx             = 0;
        private                         int             opacityDamageFx             = 0;

        protected                       Animation       animationState              = Animation.EAnimationNone;
        private                         ChangeAction    actionAfterHide             = null;

        protected                       int             animationPlayerRightHand    = 0;
        private                         int             animationHUDHealthFX        = 0;
        private                         int             animationHUDDamageFX        = 0;

//private static BufferedImage test = null;
//private static GLImage test2 = null;

        @Override
        public final void draw2D()
        {
            //level may not be initialized
            if ( Level.current() != null )
            {
                //draw player's wearpon or gadget
                Level.currentPlayer().drawWearponOrGadget();
            }

            //draw frames per second
            GLImage fpsImg = GLImage.getFromString( MainThread.currentFramesPerSecond + " fps", Fonts.EFps, ShooterSettings.Colors.EFpsFg.col, null, ShooterSettings.Colors.EFpsOutline.col );
            GLView.singleton.drawOrthoBitmapBytes( fpsImg, GLPanel.singleton.width - Offset.EBorderHudX - fpsImg.width, GLPanel.singleton.height - Offset.EBorderHudY - fpsImg.height );
/*
//try full buffered surface :) way too slow :( 14fps
if ( test == null )
{
    test = new BufferedImage( GLPanel.singleton.width, GLPanel.singleton.height, BufferedImage.TYPE_INT_ARGB );
    test2 = new GLImage( test, ImageUsage.EOrtho );
}
*/
/*
            //draw player's health
            GLImage healthImg = GLImage.getFromString( String.valueOf( Level.getCurrentPlayer().getHealth() ), Fonts.EHealth, Colors.EHealthFg.col, null, Colors.EHealthOutline.col );
            GLView.singleton.drawOrthoBitmapBytes( healthImg, Offset.EBorderHudX, GLPanel.singleton.height - Offset.EBorderHudY - healthImg.height );

            //draw ammo if the wearpon uses ammo
            if ( Level.getCurrentPlayer().getWearpon().getAmmoType() != null )
            {
                //HUD.releaseClip( g );
                GLImage ammoImg = GLImage.getFromString( Level.getCurrentPlayer().getWearpon().getMagazineAmmo() + " | " + ( ShooterSettings.HUD.SHOW_MAGAZINE_SIZE ? "" : Level.getCurrentPlayer().getWearpon().getMagazineSize() + " | "  ) + Level.getCurrentPlayer().ammo.getAmmo( Level.getCurrentPlayer().getWearpon().getAmmoType() ), Fonts.EAmmo, Colors.EAmmoFg.col, null, Colors.EAmmoOutline.col );
                GLView.singleton.drawOrthoBitmapBytes( ammoImg, GLPanel.singleton.width - Offset.EBorderHudX - ammoImg.width, Offset.EBorderHudY );
            }
*/
            //draw player's health
            //HUD.releaseClip( g );
            //Strings.drawString( g, , , GLPanel.glPanel.height - Offset.EBorderHudY );
/*
            //draw avatar & hud-msgs ( if active )
            AvatarMessage.drawMessage(          g );
            HUDMessage.drawAllMessages(         g );
*/

//GLView.singleton.drawOrthoBitmapBytes( test2, 0, 0 );

/*
            //draw effects last
            drawHUDEffects(                     g );

            //draw debug logs
            Level.getCurrentPlayer().drawDebugLog(      g );
*/
        }

        public static final void init()
        {
            //load all images
            singleton = new HUD();
            Wearpon.loadImages();
            Gadget.loadImages();
            BackGround.loadImages();
            AvatarMessage.loadImages();
        }

        public final void animateEffects()
        {
            if ( animationHUDHealthFX > 0 ) --animationHUDHealthFX;
            if ( animationHUDDamageFX > 0 ) --animationHUDDamageFX;
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
                                    animationPlayerRightHand = ShooterSettings.TICKS_WEARPON_HIDE_SHOW;
                                    break;
                                }

                            }
                        }
                        break;
                    }
                }
            }
        }

        public final void launchDamageFX( int descent )
        {
            //set new opacity value and clip it
            opacityDamageFx = MAX_OPACITY_DAMAGE_FX * descent / 15;
            if ( opacityDamageFx > MAX_OPACITY_DAMAGE_FX ) opacityDamageFx = MAX_OPACITY_DAMAGE_FX;

            //start damage animation
            animationHUDDamageFX = ShooterSettings.TICKS_DAMAGE_FX;
        }

        public final void launchHealthFX( int gain )
        {
            //set new opacity value and clip it
            opacityHealthFx = MAX_OPACITY_HEALTH_FX * gain / 15;
            if ( opacityHealthFx > MAX_OPACITY_HEALTH_FX ) opacityHealthFx = MAX_OPACITY_HEALTH_FX;

            //start health animation
            animationHUDHealthFX = ShooterSettings.TICKS_HEALTH_FX;
        }

        public final void startHudAnimation( Animation newAnimationState, ChangeAction newActionAfterHide )
        {
            animationPlayerRightHand       = ShooterSettings.TICKS_WEARPON_HIDE_SHOW;
            animationState  = newAnimationState;
            actionAfterHide = newActionAfterHide;
        }

        public final boolean animationActive()
        {
            return ( animationPlayerRightHand != 0 );
        }

        @SuppressWarnings( "unused" )
        private final void drawHUDEffects( Graphics2D g )
        {
            //draw healing effect?
            if ( animationHUDHealthFX > 0 )
            {
                Color originalTextCol = LibColors.EWhite.col;
                int alpha = opacityHealthFx * animationHUDHealthFX / ShooterSettings.TICKS_HEALTH_FX;
                Color healthCol   = new Color( originalTextCol.getRed(), originalTextCol.getGreen(), originalTextCol.getBlue(), alpha );

                //HUD.releaseClip( g );
                g.setColor( healthCol );
                g.fillRect( 0, 0, GLPanel.singleton.width, GLPanel.singleton.height );
            }

            //draw healing effect?
            if ( animationHUDDamageFX > 0 )
            {
                Color originalTextCol = LibColors.ERed.col;
                int alpha = opacityDamageFx * animationHUDDamageFX / ShooterSettings.TICKS_DAMAGE_FX;

                Color damageCol   = new Color( originalTextCol.getRed(), originalTextCol.getGreen(), originalTextCol.getBlue(), alpha );

                //HUD.releaseClip( g );
                g.setColor( damageCol );
                g.fillRect( 0, 0, GLPanel.singleton.width, GLPanel.singleton.height );
            }
        }
/*
        public static final void releaseClip( Graphics g )
        {
            g.setClip( 0, 0, GLPanel.glPanel.width, GLPanel.glPanel.height );
        }
*/
    }
