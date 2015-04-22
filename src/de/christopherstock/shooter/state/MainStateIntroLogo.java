/*  $Id: IntroLogo.java 1240 2013-01-02 14:43:47Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.state;

    import  de.christopherstock.lib.Lib.Invert;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.Lib.Scalation;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.g3d.face.LibFace.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;
    import  de.christopherstock.shooter.base.ShooterD3ds.*;
    import  de.christopherstock.shooter.g3d.wall.*;
    import  de.christopherstock.shooter.g3d.wall.Wall.*;
    import  de.christopherstock.shooter.io.hid.Keys;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public class MainStateIntroLogo
    {
        private         static  final   float                   SPEED_ROTATION                  = 2.5f;
        private         static  final   float                   SPEED_ROTATION_APPEARING        = 12.5f;
        private         static  final   float                   SPEED_APPEARING_Y               = 2.5f;
        private         static  final   float                   START_APPEARING_Y               = 100.0f;
        private         static  final   float                   TARGET_APPEARING_Y              = 10.0f;

        private         static          MainStateIntroLogo               singleton                       = null;

        private                         LibGLImage              iTextTop                        = null;
        private                         LibGLImage              iTextBottom                     = null;
        private                         Wall                    iLogo                           = null;
        private                         float                   iZ                              = 0.0f;
        private                         float                   iLogoY                          = 0.0f;
        private                         float                   iAlphaText                      = 0.0f;

        private MainStateIntroLogo()
        {
            iLogoY      = START_APPEARING_Y;
            iLogo       = new Wall( Menu.ELogoIpco, new LibVertex( 0.0f, 0.0f, 0.0f ), 0.0f, Scalation.ENone, Invert.ENo, Wall.WallCollidable.EYes,  Wall.WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, null, null, 0, WallHealth.EUnbreakale, null, null );
            iTextTop    = LibGLImage.getFromString( "Brought to you by the members of", Fonts.EIntro, LibColors.EGreyDark.colARGB, null, null, ShooterDebug.glImage );
            iTextBottom = LibGLImage.getFromString( "The International PC Owners",      Fonts.EIntro, LibColors.EGreyDark.colARGB, null, null, ShooterDebug.glImage );
        }

        public static final MainStateIntroLogo getSingleton()
        {
            if ( singleton == null )
            {
                singleton = new MainStateIntroLogo();
            }

            return singleton;
        }

        public final void draw2D()
        {
            //draw FPS
            Shooter.mainThread.iHUD.draw2D();

            if ( iLogoY <= TARGET_APPEARING_Y )
            {
                //draw total ammo
                LibGL3D.view.drawOrthoBitmapBytes( iTextTop,    ( LibGL3D.panel.width - iTextTop.width    ) / 2, 500, iAlphaText );
                LibGL3D.view.drawOrthoBitmapBytes( iTextBottom, ( LibGL3D.panel.width - iTextBottom.width ) / 2, 150, iAlphaText );
                iAlphaText += 0.005f;
                if ( iAlphaText > 1.0f ) iAlphaText = 1.0f;
            }
        }

        public final void draw3D()
        {
            //clear gl
            LibGL3D.view.clearGl( LibColors.EBlack );

            //set global camera
            LibGL3D.view.setCamera( new ViewSet( 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 180.0f ) );

            //specify lights
            LibGLLight[] lights = new LibGLLight[]
            {
                new LibGLLight
                (
                    new LibVertex( 0.0f, 0.0f, 2.0f ),
                    0.0f,
                    18.0f,
                    LibColors.ELightIntroLogoSpot
                ),
            };

            //enable light
            LibGL3D.view.setLightsOn( lights, LibColors.ELightIntroLogoAmbient );

            //draw logo
            iLogo.draw();

            //draw debug cross in front of the logo
          //new Wall(   Others.ECross,              new LibVertex( 0.0f,    1.0f,   0.0f    ), 0.0f, Scalation.ENone, WallCollidable.ENo, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EAlwaysDraw              ).draw();

            //disable lights
            LibGL3D.view.setLightsOff();

            //flush face queue to force an immediate redraw
            LibGL3D.view.flushFaceQueue( new LibVertex( 0.0f, 0.0f, 0.0f ) );
        }

        public final void onRun()
        {
            iLogo.translateAndRotateXYZ( 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, iZ,    null, LibTransformationMode.EOriginalsToTransformed   );
            iLogo.translateAndRotateXYZ( 0.0f, iLogoY, 0.0f, 0.0f, 0.0f, 0.0f, null, LibTransformationMode.ETransformedToTransformed );

            if ( iLogoY > TARGET_APPEARING_Y )
            {
                iLogoY -= SPEED_APPEARING_Y;
/*
                float diffY = ( logoY - TARGET_APPEARING_Y  ) / 4;
                if ( diffY < 0.1f ) diffY = 0.1f;
                logoY -= diffY; //TARGET_APPEARING_Y SPEED_APPEARING_Y;
*/
                iZ = LibMath.normalizeAngle( iZ + SPEED_ROTATION_APPEARING );
            }
            else
            {
                //turn till next 0° position
                if ( iZ != 0.0f )
                {
                    iZ = LibMath.normalizeAngle( iZ + SPEED_ROTATION );
                }
            }
        }

        public void checkIntroLogoEvents()
        {
            //check main-menu-action
            Keys.toggleMainMenu.checkLaunchingAction();

            //change to game if key pressed
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                Shooter.mainThread.orderMainStateChangeTo( MainState.EIngame );
            }
        }
    }
