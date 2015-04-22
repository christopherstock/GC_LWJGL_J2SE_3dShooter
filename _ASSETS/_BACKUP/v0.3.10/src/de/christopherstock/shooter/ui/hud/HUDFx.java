/*  $Id: HUDFx.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.image.*;
    import  javax.imageio.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.gl.*;
import de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.ui.*;
import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.base.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class HUDFx
    {
        private         static          LibGLImage      damagePane                  = null;
        private         static          LibGLImage      healthPane                  = null;
        private         static          LibGLImage      deadPane                    = null;
        private         static          LibGLImage      adrenalinePane              = null;
        private         static          BufferedImage   reincarnationTube           = null;
        private         static          LibGLImage      gli                         = null;

        private         static          int             animationHUDHealthFX        = 0;
        private         static          int             animationHUDDamageFX        = 0;
        private         static          int             animationHUDDyingFX         = 0;
        public          static          int             animationHUDDeadFX          = 0;
        public          static          int             animationHUDReincarnationFX = 0;

        public          static          int             animationHUDDamageFXticks   = 0;

        private         static          float           opacityHealthFx             = 0.0f;
        private         static          float           opacityDamageFx             = 0.0f;

        public          static          boolean         drawDyingFx                 = false;
        public          static          boolean         drawDeadFx                  = false;
        public          static          boolean         drawReincarnationFx         = false;

        public static final void drawHUDEffects()
        {
            //draw healing effect?
            if ( animationHUDHealthFX > 0 )
            {
                float alpha = opacityHealthFx * animationHUDHealthFX / ShooterSettings.Performance.TICKS_HEALTH_FX;
                LibGL3D.view.drawOrthoBitmapBytes( healthPane, 0, 0, alpha );
            }

            //draw damage effect?
            if ( animationHUDDamageFX > 0 )
            {
                float alpha = opacityDamageFx * animationHUDDamageFX / animationHUDDamageFXticks;
                LibGL3D.view.drawOrthoBitmapBytes( damagePane, 0, 0, alpha );
            }

            //draw dying effect
            if ( drawDyingFx )
            {
                float alpha = (float)animationHUDDyingFX / (float)ShooterSettings.Performance.TICKS_DYING_FX;
                LibGL3D.view.drawOrthoBitmapBytes( damagePane, 0, 0, alpha );
            }

            //draw dead effect
            if ( drawDeadFx )
            {
                float alpha = (float)animationHUDDeadFX / (float)ShooterSettings.Performance.TICKS_DEAD_FX;
                LibGL3D.view.drawOrthoBitmapBytes( deadPane, 0, 0, alpha );
            }

            //draw adrenaline effect
            if ( ShooterGameLevel.current().adrenalineTicks > 0 )
            {
                float alpha = 0.05f;
                LibGL3D.view.drawOrthoBitmapBytes( adrenalinePane, 0, 0, alpha );
            }

            //draw reincarnation effect
            if ( drawReincarnationFx )
            {
                float size = ( 5 * (float)animationHUDReincarnationFX ) / ShooterSettings.Performance.TICKS_DEAD_FX;
                LibGL3D.view.drawOrthoBitmapBytes( gli, (int)( ( LibGL3D.panel.width - gli.width * size ) / 2 ), (int)( ( LibGL3D.panel.height - gli.height * size ) / 2 ), 1.0f, size, size, false );
            }
        }

        public static final void launchDamageFX( int descent )
        {
            //set new opacity value and clip it
            opacityDamageFx = ShooterSettings.HUDSettings.MAX_OPACITY_DAMAGE_FX * descent / 15;
            if ( opacityDamageFx > ShooterSettings.HUDSettings.MAX_OPACITY_DAMAGE_FX ) opacityDamageFx = ShooterSettings.HUDSettings.MAX_OPACITY_DAMAGE_FX;

            //start damage animation
            animationHUDDamageFXticks   = ShooterSettings.Performance.MAX_TICKS_DAMAGE_FX * descent / 15;
            animationHUDDamageFX        = animationHUDDamageFXticks;

            //create pane if not done yet
            if ( damagePane == null )
            {
                damagePane = LibGLImage.getFullOpaque
                (
                    LibColors.ERed.colABGR,
                    LibGL3D.panel.width,
                    LibGL3D.panel.height,
                    ShooterDebug.glimage
                );
            }
        }

        public static final void launchHealthFX( int gain )
        {
            //set new opacity value and clip it
            opacityHealthFx = ShooterSettings.HUDSettings.MAX_OPACITY_HEALTH_FX * gain / 15;
            if ( opacityHealthFx > ShooterSettings.HUDSettings.MAX_OPACITY_HEALTH_FX ) opacityHealthFx = ShooterSettings.HUDSettings.MAX_OPACITY_HEALTH_FX;

            //start health animation
            animationHUDHealthFX = ShooterSettings.Performance.TICKS_HEALTH_FX;

            //create pane if not done yet
            if ( healthPane == null )
            {
                healthPane = LibGLImage.getFullOpaque
                (
                    LibColors.EWhite.colABGR,
                    LibGL3D.panel.width,
                    LibGL3D.panel.height,
                    ShooterDebug.glimage
                );
            }
        }

        public static final void launchDyingFX()
        {
            if ( !drawDyingFx )
            {
                //start health animation
                drawDyingFx         = true;
                animationHUDDyingFX = 0;

                //create pane if not done yet
                if ( damagePane == null )
                {
                    damagePane = LibGLImage.getFullOpaque
                    (
                        LibColors.ERed.colABGR,
                        LibGL3D.panel.width,
                        LibGL3D.panel.height,
                        ShooterDebug.glimage
                    );
                }
            }
        }

        public static final void launchDeadFX()
        {
            if ( !drawDeadFx )
            {
                //start health animation
                drawDeadFx          = true;
                animationHUDDeadFX  = 0;

                //create pane if not done yet
                if ( deadPane == null )
                {
                    deadPane = LibGLImage.getFullOpaque
                    (
                        LibColors.EBlack.colABGR,
                        LibGL3D.panel.width,
                        LibGL3D.panel.height,
                        ShooterDebug.glimage
                    );
                }
            }
        }

        public static final void startAdrenalineFx()
        {
            //create pane if not done yet
            if ( adrenalinePane == null )
            {
                adrenalinePane = LibGLImage.getFullOpaque
                (
                    LibColors.EYellowLight.colABGR,
                    LibGL3D.panel.width,
                    LibGL3D.panel.height,
                    ShooterDebug.glimage
                );
            }
        }

        public static final void launchReincarnationFX()
        {
            if ( !drawReincarnationFx )
            {
                //start health animation
                drawReincarnationFx          = true;
                animationHUDReincarnationFX  = 0;

                //create pane if not done yet
                if ( reincarnationTube == null )
                {
                    try
                    {
                        reincarnationTube = ImageIO.read( Lib.preStreamJarResource( ShooterSettings.Path.EScreen.iUrl + "reincarnation.png" ) );
                        gli = new LibGLImage( reincarnationTube, ImageUsage.EOrtho, ShooterDebug.glimage, true );
                    }
                    catch ( Exception e )
                    {
                        ShooterDebug.error.trace( e );
                        System.exit( 0 );
                    }
                }
            }
        }

        public static final void animateEffects()
        {
            if ( animationHUDHealthFX       > 0     ) --animationHUDHealthFX;
            if ( animationHUDDamageFX       > 0     ) --animationHUDDamageFX;

            if ( drawDeadFx )
            {
                if ( animationHUDDeadFX  < ShooterSettings.Performance.TICKS_DEAD_FX ) ++animationHUDDeadFX;
            }
            if ( drawDyingFx )
            {
                if ( animationHUDDyingFX < ShooterSettings.Performance.TICKS_DYING_FX ) ++animationHUDDyingFX;
            }
            if ( drawReincarnationFx )
            {
                if ( animationHUDReincarnationFX < ShooterSettings.Performance.TICKS_REINCARNATION_FX ) ++animationHUDReincarnationFX;
            }
        }
    }
