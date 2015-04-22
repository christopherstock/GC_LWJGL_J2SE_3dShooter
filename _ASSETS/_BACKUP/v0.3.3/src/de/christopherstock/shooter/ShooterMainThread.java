/*  $Id: ShooterMainThread.java 641 2011-04-24 01:27:03Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLForm.GLCallbackForm;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.gl.LibGL3D.*;
    import  de.christopherstock.lib.gl.LibGLPanel.*;
    import  de.christopherstock.shooter.ShooterLevels.LevelConfig;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.ShooterSettings.OffsetsOrtho;
    import  de.christopherstock.shooter.g3d.mesh.BotMeshes.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.io.hid.lwjgl.*;
    import  de.christopherstock.shooter.io.hid.swing.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The main thread. Don't mix up with the ui-thread!
    *   Start this thread to rock and roll!
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class ShooterMainThread extends Thread implements GLDrawCallback, GLCallbackForm
    {
        public static enum MainState
        {
            EPreloader,
            EMainMenu,
            EGame,
        }

        /**************************************************************************************
        *   A flag being set to true if a closing-event on the main form is invoked.
        **************************************************************************************/
        public                      boolean         iDestroyed                  = false;

        public                      long            iTickStart                  = 0;
        public                      long            iTickTime                   = 0;
        public                      long            iTickDelay                  = 0;

        public                      MainState       iMainState                  = MainState.EPreloader;

        /**************************************************************************************
        *
        **************************************************************************************/
        public      static          boolean         launchShot                  = false;

        public      static          int             preloaderTest               = 0;
        public      static          LibGLImage      preloaderImage              = null;

        /***********************************************************************************
        *   The game's main-thread run-method performing and endless loop of ticks.
        ***********************************************************************************/
        @Override
        public void run()
        {
            //init everything
            init();

            //main thread ticks until the app is destroyed
            while ( !iDestroyed )
            {
                iTickStart = System.currentTimeMillis();                    //meassure tick time

                //only perform game-loop if 3d-canvas is fully initialized
                if ( LibGL3D.glPanelInitialized )
                {
                    //check keys and mouse for lwjgl
                    if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLKeys.checkKeys();
                    if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLMouse.checkMouse();

                    switch ( iMainState )
                    {
                        case EPreloader:
                        {
                            //do nothing
                            break;
                        }

                        case EMainMenu:
                        {
                            //check main-menu-action
                            Keys.toggleMainMenu.checkLaunchingAction();

                            //check menu key events
                            checkMenuKeyEvents();

                            break;
                        }

                        case EGame:
                        {
                            //check main-menu-action
                            Keys.toggleMainMenu.checkLaunchingAction();

                            //check game key events
                            checkGameEvents();

                            //no level may be assigned for debug proposes
                            if ( GameLevel.current() != null )
                            {
                                //animate player
                                GameLevel.currentPlayer().animate();

                                //animate level
                                GameLevel.current().animate();
                            }

                            AvatarMessage.animate();                        //animate msgs
                            HUDMessage.animateAll();                        //animate hud msgs
                            HUDFx.animateEffects();                         //animate hud-effects
                            FX.animateAll();                                //animate particle systems
                            HUD.getSingleton().animateShowHide();           //animate wearpon & gadgets
                            Sound.maintainAllSounds();                      //maintain sounds

                            break;
                        }
                    }

                    //ShooterDebug.bugfix.out( " check 1: : [" + ( System.currentTimeMillis() - iTickStart ) );

                    //update frames per second
                    Fps.finishedDrawing();

                    //draw gl for this tick
                    LibGL3D.panel.display();

                    //ShooterDebug.bugfix.out( " check 2: : [" + ( System.currentTimeMillis() - iTickStart ) );
                }

                //meassure tick time and set delay if desired
                iTickTime = ( System.currentTimeMillis() - iTickStart );
                if ( iTickTime < ShooterSettings.Performance.MIN_THREAD_DELAY )
                {
                    iTickDelay = ShooterSettings.Performance.MIN_THREAD_DELAY - iTickTime;
                }
                else
                {
                    iTickDelay = 0;
                }

                //ShooterDebug.bugfix.out( "ticktime: [" + iTickTime + "] delay: [" + iTickDelay + "]" );

                //delay
                try { Thread.sleep( iTickDelay ); } catch ( InterruptedException ie ) {}
            }
        }

        private void checkMenuKeyEvents()
        {
            //change to game
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                iMainState = MainState.EGame;
            }
        }

        private void checkGameEvents()
        {
            //change to main menu
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                iMainState = MainState.EMainMenu;
            }

            //perform synchronized level change
            GameLevel.checkChangeTo();

            //start new effects

            //launch exploisions?
            if ( Keys.explosion.iLaunchAction )
            {
                Keys.explosion.iLaunchAction = false;
                FX.launchTestExplosion();
            }

            //player action?
            if ( Keys.playerAction.iLaunchAction )
            {
                Keys.playerAction.iLaunchAction = false;
                GameLevel.currentPlayer().launchAction();
            }

            //player action?
            if ( Keys.crouching.iLaunchAction )
            {
                Keys.crouching.iLaunchAction = false;
                GameLevel.currentPlayer().toggleCrouching();
            }

            //gainHealth
            if ( Keys.gainHealth.iLaunchAction )
            {
                Keys.gainHealth.iLaunchAction = false;

                //heal player
                GameLevel.currentPlayer().heal( 10 );
            }

            //hurt
            if ( Keys.damageFx.iLaunchAction )
            {
                Keys.damageFx.iLaunchAction = false;

                //hurt player
                GameLevel.currentPlayer().hurt( 10 );
            }

            //launch msg?
            if ( Keys.avatarMessage.iLaunchAction )
            {
                Keys.avatarMessage.iLaunchAction = false;

                //move bot limbs
                GameLevel.current().iBots.elementAt( 0 ).iBotMeshes.assignArmsPosition( ArmsPosition.values()[ AvatarMessage.currentDebugMsg % ArmsPosition.values().length ] );
                GameLevel.current().iBots.elementAt( 0 ).iBotMeshes.assignLegsPosition( LegsPosition.values()[ AvatarMessage.currentDebugMsg % LegsPosition.values().length ] );

                AvatarMessage.showDebugMessage();

                //Level.current().iBots.elementAt( 0 ).say( Sound.EFemaleGiggle1 );
            }
        }

        // TODO prune this !
        @Override
        public boolean readyToDisplay()
        {
            return ( GameLevel.current() != null );
        }

        @Override
        public final void draw2D()
        {
            //draw loading screen if not initialized
            if ( LibGL3D.glPanelInitialized )
            {
                switch ( iMainState )
                {
                    case EPreloader:
                    {
                        try
                        {
                            //create preloader image if not done yet
                            if ( preloaderImage == null )
                            {
                                preloaderImage = new LibGLImage( ImageIO.read( LibGLForm.class.getResourceAsStream( ShooterSettings.Path.EScreen.iUrl + "icon.png" ) ), ImageUsage.EOrtho, ShooterDebug.glimage, false );
                            }

                            preloaderTest += 10;
                            LibGL3D.view.drawOrthoBitmapBytes( preloaderImage, LibGL3D.panel.width / 2 + 90, LibGL3D.panel.height / 2 - preloaderImage.height / 2 );

                            LibGL3D.view.drawOrthoBitmapBytes( preloaderImage, LibGL3D.panel.width / 2 + preloaderTest, LibGL3D.panel.height / 2 + preloaderImage.height / 2 );
                        }
                        catch ( Exception e )
                        {
                            ShooterDebug.error.trace( e );
                        }

                        break;
                    }

                    case EMainMenu:
                    {
                        //draw HUD
                        HUD.getSingleton().draw2D();

                        //draw main menu
                        MainMenu.singleton.draw2D();

                        break;
                    }

                    case EGame:
                    {
                        //draw HUD
                        HUD.getSingleton().draw2D();
                        break;
                    }
                }
            }
        }

        @Override
        public final void draw3D()
        {
            //draw loading screen if not initialized
            if ( LibGL3D.glPanelInitialized )
            {
                switch ( iMainState )
                {
                    case EPreloader:
                    {
                        //clear gl and flush draw queue
                        LibGL3D.view.clearGl();
                        LibGL3D.view.clearFaceQueues();

                        //no 3d drawing ops for the preloader
                        break;
                    }

                    case EGame:
                    case EMainMenu:
                    {
                        //clear gl and flush draw queue
                        LibGL3D.view.clearGl();
                        LibGL3D.view.clearFaceQueues();

                        //draw game screen and flush the queue
                        drawGameScreen();
                        LibGL3D.view.flushFaceQueue( GameLevel.currentPlayer().getAnchor() );

                        break;
                    }
                }
            }
        }

        private final void drawGameScreen()
        {
            //level may be null if not set
            if ( GameLevel.current() != null )
            {
                //get player's camera preference
                ViewSet cam = GameLevel.currentPlayer().getCameraPositionAndRotation();

                //draw scene bg
                GameLevel.current().drawBg( cam );

                //set player's camera
                LibGL3D.view.setPlayerCamera( cam.pos.x, cam.pos.y, cam.pos.z, cam.rot.x, cam.rot.y, cam.rot.z );

                //all game components
                GameLevel.current().draw();                         //draw the level
                GameLevel.current().drawAllItems();                 //draw all items
                GameLevel.current().drawAllBots();                  //draw all bots

                //bullet holes and fx points
                BulletHole.drawAll();                               //draw all bullet holes
                FX.drawAll();                                       //draw all fx points
                GameLevel.currentPlayer().drawStandingCircle();     //draw circle on players bottom location
            }
        }

        @Override
        public final void onFormDestroyed()
        {
          //ShooterDebug.major.out( "Main Form was closed - Shooter is destroyed" );
            iDestroyed = true;
        }

        /**************************************************************************************
        *   Inits the ui
        **************************************************************************************/
        protected final void init()
        {
            BufferedImage iconImage = null;
            BufferedImage bgImage   = null;

            //load form utils
            try
            {
                iconImage   = ImageIO.read( LibGLForm.class.getResourceAsStream( ShooterSettings.Path.EScreen.iUrl + "icon.png" ) );
                bgImage     = ImageIO.read( LibGLForm.class.getResourceAsStream( ShooterSettings.Path.EScreen.iUrl + "bg.png"   ) );
            }
            catch ( IOException ioe )
            {
                ShooterDebug.error.err( "ERROR! Screen-Graphics could not be loaded!" );
            }

            //set host-os lookAndFeel
            LibGLForm.setLookAndFeel( ShooterDebug.major );
            ShooterDebug.init.out( "loading 3d engine.." );
            LibGL3D.init                                            //init external gl library
            (
                ShooterSettings.General.ENGINE_3D,
                ShooterSettings.Form.FORM_WIDTH,
                ShooterSettings.Form.FORM_HEIGHT,
                ShooterSettings.Form.FORM_TITLE,
                !General.DISABLE_LIGHTING,
                this,
                this,
                iconImage,
                bgImage,
                SwingKeys.singleton,
                SwingMouse.singleton,
                SwingMouse.singleton,
                SwingMouse.singleton,
                ShooterDebug.gl
            );

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "load textures .." );
            ShooterTexture.loadImages();                                                //load texture images

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "assign textures" );
            LibGL3D.view.initTextures( ShooterTexture.getAllTextureImages() );          //assign texture images

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "parsing offsets .." );
            OffsetsOrtho.parseOffsets( LibGL3D.panel.width, LibGL3D.panel.height );     //parse hud's offsets

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "loading 3dsmax files" );
            ShooterD3dsFiles.init( ShooterDebug.d3ds );                                 //init 3d studio max objects

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "initing levels" );
            ShooterLevels.init();                                                       //init mesh collections

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "initing main menu" );
            MainMenu.init();                                                            //init main menu

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "initing hud" );
            HUD.init();                                                                 //init Heads up display

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "initing sound" );
            Sound.init();                                                               //init sound-engine

            //perform repaint
            LibGL3D.panel.display();

            ShooterDebug.init.out( "launching level 1" );
            GameLevel.orderLevelChange( LevelConfig.ELevel1 );                          //order change to level 1

            //switch main state to 'game'
            iMainState = MainState.EGame;
        }
    }
