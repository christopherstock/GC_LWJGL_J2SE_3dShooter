/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;

    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     stock
    *   @version    1.0
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

        private         static          int             MAX_OPACITY_HEALTH_FX       = 255; //64
        private         static          int             MAX_OPACITY_DAMAGE_FX       = 255; //200

        private         static          int             opacityHealthFx             = 0;
        private         static          int             opacityDamageFx             = 0;

        protected       static          Animation       animationState              = Animation.EAnimationNone;
        private         static          ChangeAction    actionAfterHide             = null;
        protected       static          int             animation                   = 0;

        private         static          int             animationHUDHealthFX        = 0;
        private         static          int             animationHUDDamageFX        = 0;

        public static final void draw( Graphics2D g )
        {
            //draw player's wearpon or gadget
            Player.user.drawWearponOrGadget(    g );
            Player.user.drawHealth(             g );

            //draw avatar & hud-msgs ( if active )
            AvatarMessage.drawMessage(          g );
            HUDMessage.drawAllMessages(         g );

            //draw fraes per second
            drawFps(                            g );

            //draw effects last
            drawHUDEffects(                     g );

        }

        public static final void init()
        {
            //load all images
            Wearpon.loadImages();
            Gadget.loadImages();
            AvatarMessage.loadImages();
        }

        public static final void animateEffects()
        {
            if ( animationHUDHealthFX > 0 ) --animationHUDHealthFX;
            if ( animationHUDDamageFX > 0 ) --animationHUDDamageFX;
        }

        public static final void animateShowHide()
        {
            if ( HUD.animation > 0 )
            {
                switch ( HUD.animationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }

                    case EAnimationShow:
                    {
                        --HUD.animation;

                        //check if animation is over
                        if ( HUD.animation == 0 )
                        {
                            HUD.animationState = HUD.Animation.EAnimationNone;
                        }
                        break;
                    }

                    case EAnimationHide:
                    {
                        --HUD.animation;

                        //check if animation is over
                        if ( HUD.animation == 0 )
                        {
                            switch ( HUD.actionAfterHide )
                            {
                                case EActionNext:
                                {
                                    Player.user.chooseNextWearponOrGadget();
                                    break;
                                }

                                case EActionPrevious:
                                {
                                    Player.user.choosePreviousWearponOrGadget();
                                    break;
                                }

                                case EActionReload:
                                {
                                    //reload & just show the same gun
                                    Player.user.getWearpon().reload();
                                    HUD.animationState = HUD.Animation.EAnimationShow;
                                    HUD.animation = Shooter.TICKS_WEARPON_HIDE_SHOW;
                                    break;
                                }

                            }
                        }
                        break;
                    }
                }
            }
        }

        public static final void launchDamageFX( int descent )
        {
            //set new opacity value and clip it
            opacityDamageFx = MAX_OPACITY_DAMAGE_FX * descent / 15;
            if ( opacityDamageFx > MAX_OPACITY_DAMAGE_FX ) opacityDamageFx = MAX_OPACITY_DAMAGE_FX;

            //start damage animation
            animationHUDDamageFX = Shooter.TICKS_DAMAGE_FX;
        }

        public static final void launchHealthFX( int gain )
        {
            //set new opacity value and clip it
            opacityHealthFx = MAX_OPACITY_HEALTH_FX * gain / 15;
            if ( opacityHealthFx > MAX_OPACITY_HEALTH_FX ) opacityHealthFx = MAX_OPACITY_HEALTH_FX;

            //start health animation
            animationHUDHealthFX = Shooter.TICKS_HEALTH_FX;
        }

        public static final void startHudAnimation( Animation newAnimationState, ChangeAction newActionAfterHide )
        {
            animation       = Shooter.TICKS_WEARPON_HIDE_SHOW;
            animationState  = newAnimationState;
            actionAfterHide = newActionAfterHide;
        }

        public static final boolean animationActive()
        {
            return ( animation != 0 );
        }

        private static final void drawHUDEffects( Graphics2D g )
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

        private static final void drawFps( Graphics2D g )
        {
            //draw player's health
            GLPanel.releaseClip( g );
            Strings.drawString( g, Colors.EFpsFg.col, null, Colors.EFpsOutline.col, Fonts.EFps, Anchor.EAnchorRightTop, MainThread.currentFramesPerSecond + " fps", GLPanel.PANEL_WIDTH - Offset.EBorderHudX, Offset.EBorderHudY );
        }
    }
