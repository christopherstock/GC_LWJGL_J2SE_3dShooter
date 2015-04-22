/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.opengl.*;
    import  de.christopherstock.shooter.gl.opengl.GLPanel.Drawable;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class HUD implements Drawable
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

        private                         int             opacityHealthFx             = 0;
        private                         int             opacityDamageFx             = 0;

        protected                       Animation       animationState              = Animation.EAnimationNone;
        private                         ChangeAction    actionAfterHide             = null;
        protected                       int             animation                   = 0;

        private                         int             animationHUDHealthFX        = 0;
        private                         int             animationHUDDamageFX        = 0;

        @Override
        public final void draw( Graphics2D g )
        {
            //draw player's wearpon or gadget
            Player.singleton.drawWearponOrGadget(    g );
            Player.singleton.drawHealth(             g );

            //draw avatar & hud-msgs ( if active )
            AvatarMessage.drawMessage(          g );
            HUDMessage.drawAllMessages(         g );

            //draw fraes per second
            drawFps(                            g );

            //draw effects last
            drawHUDEffects(                     g );

            //draw debug logs
            Player.singleton.drawDebugLog(      g );
        }

        public static final void init()
        {
            //load all images
            singleton = new HUD();
            Wearpon.loadImages();
            Gadget.loadImages();
            AvatarMessage.loadImages();
        }

        public final void animateEffects()
        {
            if ( animationHUDHealthFX > 0 ) --animationHUDHealthFX;
            if ( animationHUDDamageFX > 0 ) --animationHUDDamageFX;
        }

        public final void animateShowHide()
        {
            if ( animation > 0 )
            {
                switch ( animationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }

                    case EAnimationShow:
                    {
                        --animation;

                        //check if animation is over
                        if ( animation == 0 )
                        {
                            animationState = HUD.Animation.EAnimationNone;
                        }
                        break;
                    }

                    case EAnimationHide:
                    {
                        --animation;

                        //check if animation is over
                        if ( animation == 0 )
                        {
                            switch ( actionAfterHide )
                            {
                                case EActionNext:
                                {
                                    Player.singleton.chooseNextWearponOrGadget();
                                    break;
                                }

                                case EActionPrevious:
                                {
                                    Player.singleton.choosePreviousWearponOrGadget();
                                    break;
                                }

                                case EActionReload:
                                {
                                    //reload & just show the same gun
                                    Player.singleton.getWearpon().reload();
                                    animationState = Animation.EAnimationShow;
                                    animation = Shooter.TICKS_WEARPON_HIDE_SHOW;
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
            animationHUDDamageFX = Shooter.TICKS_DAMAGE_FX;
        }

        public final void launchHealthFX( int gain )
        {
            //set new opacity value and clip it
            opacityHealthFx = MAX_OPACITY_HEALTH_FX * gain / 15;
            if ( opacityHealthFx > MAX_OPACITY_HEALTH_FX ) opacityHealthFx = MAX_OPACITY_HEALTH_FX;

            //start health animation
            animationHUDHealthFX = Shooter.TICKS_HEALTH_FX;
        }

        public final void startHudAnimation( Animation newAnimationState, ChangeAction newActionAfterHide )
        {
            animation       = Shooter.TICKS_WEARPON_HIDE_SHOW;
            animationState  = newAnimationState;
            actionAfterHide = newActionAfterHide;
        }

        public final boolean animationActive()
        {
            return ( animation != 0 );
        }

        private final void drawHUDEffects( Graphics2D g )
        {
            //draw healing effect?
            if ( animationHUDHealthFX > 0 )
            {
                Color originalTextCol = Colors.EWhite.col;
                int alpha = opacityHealthFx * animationHUDHealthFX / Shooter.TICKS_HEALTH_FX;
                Color healthCol   = new Color( originalTextCol.getRed(), originalTextCol.getGreen(), originalTextCol.getBlue(), alpha );

                GLPanel.releaseClip( g );
                g.setColor( healthCol );
                g.fillRect( 0, 0, GLPanel.PANEL_WIDTH, GLPanel.PANEL_HEIGHT );
            }

            //draw healing effect?
            if ( animationHUDDamageFX > 0 )
            {
                Color originalTextCol = Colors.ERed.col;
                int alpha = opacityDamageFx * animationHUDDamageFX / Shooter.TICKS_DAMAGE_FX;

                Color damageCol   = new Color( originalTextCol.getRed(), originalTextCol.getGreen(), originalTextCol.getBlue(), alpha );

                GLPanel.releaseClip( g );
                g.setColor( damageCol );
                g.fillRect( 0, 0, GLPanel.PANEL_WIDTH, GLPanel.PANEL_HEIGHT );
            }
        }

        private final void drawFps( Graphics2D g )
        {
            //draw player's health
            GLPanel.releaseClip( g );
            Strings.drawString( g, Colors.EFpsFg.col, null, Colors.EFpsOutline.col, Fonts.EFps, Anchor.EAnchorRightTop, MainThread.currentFramesPerSecond + " fps", GLPanel.PANEL_WIDTH - Offset.EBorderHudX, Offset.EBorderHudY );
        }
    }
