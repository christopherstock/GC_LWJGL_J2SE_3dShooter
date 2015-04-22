/*  $Id: ShooterInit.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.ui.LibFPS;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.*;
    import  de.christopherstock.shooter.game.state.*;
    import  de.christopherstock.shooter.io.hid.lwjgl.*;
    import  de.christopherstock.shooter.io.hid.swing.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The application's main thread. Start this thread to run the application.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class ShooterInit
    {
        /**************************************************************************************
        *   Inits the ui.
        **************************************************************************************/
        protected static final void initUi()
        {
            ShooterDebug.init.out( "initUi 1" );

            BufferedImage iconImage = null;
            BufferedImage bgImage   = null;

            //load form utils
            try
            {
                iconImage   = ImageIO.read( Lib.preStreamJarResource( ShooterSettings.Path.EScreen.iUrl + "icon.png" ) );
                bgImage     = ImageIO.read( Lib.preStreamJarResource( ShooterSettings.Path.EScreen.iUrl + "bg.jpg"   ) );

                Preloader.getSingleton().preloaderImage  = new LibGLImage( iconImage, LibGLImage.ImageUsage.EOrtho, ShooterDebug.glimage, false );
                Preloader.getSingleton().bgImage         = new LibGLImage( bgImage,   LibGLImage.ImageUsage.EOrtho, ShooterDebug.glimage, true );
            }
            catch ( IOException ioe )
            {
                ShooterDebug.error.err( "ERROR! Screen-Graphics could not be loaded!" );
            }
            ShooterDebug.init.out( "initUi 2" );

            //set host-os lookAndFeel
            LibGLForm.setLookAndFeel( ShooterDebug.error );
            ShooterDebug.init.out( "initUi 3" );

            //init external gl library
            LibGL3D.init
            (
                ShooterSettings.General.ENGINE_3D,
                ShooterSettings.Form.FORM_WIDTH,
                ShooterSettings.Form.FORM_HEIGHT,
                ShooterSettings.Form.FORM_TITLE,
                Shooter.mainThread,
                Shooter.mainThread,
                iconImage,
                bgImage,
                SwingKeys.singleton,
                SwingMouse.singleton,
                SwingMouse.singleton,
                SwingMouse.singleton,
                ShooterDebug.init
            );

            ShooterDebug.init.out( "initUi 4" );
        }

        /**************************************************************************************
        *   Inits the rest.
        **************************************************************************************/
        protected static final void initRest()
        {
            ShooterDebug.init.out( "initUi 5" );

            //center mouse and make it invisible
            LWJGLMouse.centerMouse();
            LWJGLMouse.hideCursor();

            //load texture images and perform repaint
            Preloader.getSingleton().increase( "Loading textures" );
            ShooterTexture.loadImages();

            ShooterDebug.init.out( "initUi 6" );

            //assign textures and perform repaint
            Preloader.getSingleton().increase( "Assigning textures" );
            LibGL3D.view.initTextures( ShooterTexture.getAllTextureImages() );

            ShooterDebug.init.out( "initUi 7" );

            //init 3d studio max objects and perform repaint
            Preloader.getSingleton().increase( "Loading 3dsmax files" );
            ShooterD3ds.init( ShooterDebug.d3ds );

            ShooterDebug.init.out( "initUi 8" );

            //init hud
            Preloader.getSingleton().increase( "Initing HUD and sound" );
            Shooter.mainThread.iHUD = new HUD();
            Shooter.mainThread.iFPS = new LibFPS( Fonts.EFps, ShooterSettings.Colors.EFpsFg.colABGR, ShooterSettings.Colors.EFpsOutline.colABGR, ShooterDebug.glimage );

            //init sound - play mute sound to init java sound library
            ShooterSound.init();
            //2x is more assertion?
            ShooterSound.EMute.playGlobalFx();
            ShooterSound.EMute.playGlobalFx();

            ShooterDebug.init.out( "initUi 9" );

            //init main menu
            MainMenu.init();

            //switch main state to 'game' and order change to level 1
            Preloader.getSingleton().increase( "Init levels and launch game with startup level" );
            ShooterLevels.init();

            //init all game levels
            ShooterGameLevel.init();

            //reset and change to startup level
            ShooterGameLevel.orderLevelChange( General.STARTUP_LEVEL, true );
            Shooter.mainThread.orderMainStateChangeTo( ShooterSettings.General.STARTUP_MAIN_STATE );

            ShooterDebug.init.out( "initUi 10" );
        }
    }
