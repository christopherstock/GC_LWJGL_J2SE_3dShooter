/*  $Id: HUD.java 231 2011-01-27 17:10:53Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class HUDFx
    {
        private         static          LibGLImage      damagePane                  = null;

        private         static          int             animationHUDHealthFX        = 0;
        private         static          int             animationHUDDamageFX        = 0;

        private         static          int             opacityHealthFx             = 0;
        private         static          int             opacityDamageFx             = 0;

        public static final void drawHUDEffects()
        {
            //draw healing effect?
            if ( animationHUDHealthFX > 0 )
            {
/*
                Color originalTextCol = LibColors.EWhite.col;
                int alpha = opacityHealthFx * animationHUDHealthFX / ShooterSettings.Performance.TICKS_HEALTH_FX;
                Color healthCol   = new Color( originalTextCol.getRed(), originalTextCol.getGreen(), originalTextCol.getBlue(), alpha );

                //HUD.releaseClip( g );
                g.setColor( healthCol );
                g.fillRect( 0, 0, LibGL3D.panel.width, LibGL3D.panel.height );
*/
            }

            //draw damage effect?
            if ( animationHUDDamageFX > 0 )
            {
/*
                Color originalTextCol = LibColors.ERed.col;
*/
                int alpha = opacityDamageFx * animationHUDDamageFX / ShooterSettings.Performance.TICKS_DAMAGE_FX;
/*
                Color damageCol   = new Color( originalTextCol.getRed(), originalTextCol.getGreen(), originalTextCol.getBlue(), alpha );

                //HUD.releaseClip( g );
                g.setColor( damageCol );
                g.fillRect( 0, 0, LibGL3D.panel.width, LibGL3D.panel.height );
*/
                ShooterDebugSystem.bugfix.out( "damage fx alpha: [" + alpha + "]" );
                LibGL3D.view.drawOrthoBitmapBytes( damagePane, 0, 0, alpha );
            }
        }

        public static final void launchDamageFX( int descent )
        {
            //set new opacity value and clip it
            opacityDamageFx = ShooterSettings.HUD.MAX_OPACITY_DAMAGE_FX * descent / 15;
            if ( opacityDamageFx > ShooterSettings.HUD.MAX_OPACITY_DAMAGE_FX ) opacityDamageFx = ShooterSettings.HUD.MAX_OPACITY_DAMAGE_FX;

            //start damage animation
            animationHUDDamageFX = ShooterSettings.Performance.TICKS_DAMAGE_FX;

            //create pane if not done yet
            if ( damagePane == null )
            {
                damagePane = LibGLImage.getFullOpaque
                (
                    LibColors.ERed.colABGR,
                    LibGL3D.panel.width,
                    LibGL3D.panel.height,
                    ShooterDebugSystem.glimage
                );
            }
        }

        public static final void launchHealthFX( int gain )
        {
            //set new opacity value and clip it
            opacityHealthFx = ShooterSettings.HUD.MAX_OPACITY_HEALTH_FX * gain / 15;
            if ( opacityHealthFx > ShooterSettings.HUD.MAX_OPACITY_HEALTH_FX ) opacityHealthFx = ShooterSettings.HUD.MAX_OPACITY_HEALTH_FX;

            //start health animation
            animationHUDHealthFX = ShooterSettings.Performance.TICKS_HEALTH_FX;
        }

        public static final void animateEffects()
        {
            if ( animationHUDHealthFX > 0 ) --animationHUDHealthFX;
            if ( animationHUDDamageFX > 0 ) --animationHUDDamageFX;
        }
    }
