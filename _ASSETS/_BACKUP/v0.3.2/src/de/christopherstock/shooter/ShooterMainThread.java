/*  $Id: MainThread.java 266 2011-02-10 20:25:39Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLForm.GLCallbackForm;
    import  de.christopherstock.lib.gl.LibGLView.GLCallbackInit;
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
    *   @version    0.3.2
    **************************************************************************************/
    public class ShooterMainThread extends Thread implements GLDrawCallback, GLCallbackForm, GLCallbackInit
    {
        public static enum MainState
        {
            EMainMenu,
            EGame,
        }

        /**************************************************************************************
        *   A flag being set to true if a closing-event on the main form is invoked.
        **************************************************************************************/
        public                      boolean         iDestroyed                  = false;

        public                      long            iTickStart                  = 0;
        public                      long            iTickTime                   = 0;
        public                      long            iDelay                      = 0;

        public                      MainState       iMainState                  = MainState.EGame;

        /**************************************************************************************
        *
        **************************************************************************************/
        public      static          boolean         launchShot                  = false;

        /***********************************************************************************
        *   The game's main-thread run-method performing and endless loop of ticks.
        ***********************************************************************************/
        @Override
        public void run()
        {
            //perform threaded initialization
            init();

            //main thread ticks until the app is destroyed
            while ( !iDestroyed )
            {
                //only perform game-loop if 3d-canvas fully initialized
                if ( LibGL3D.glPanelInitialized )
                {
                    iTickStart = System.currentTimeMillis();                    //meassure tick time

                    //check keys and mouse for lwjgl
                    if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLKeys.checkKeys();
                    if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLMouse.checkMouse();

                    switch ( iMainState )
                    {
                        case EMainMenu:
                        {
                            //check action
                            Keys.toggleMainMenu.checkLaunchingAction();

                            //check key events
                            checkNewMenuEvents();

                            break;
                        }

                        case EGame:
                        {
                            //check key events
                            checkNewGameEvents();

                            //no level may be assigned for debug proposes
                            if ( Level.current() != null )
                            {
                                Level.currentPlayer().getCylinder().setAnchorAsTargetPosition();    //set current position as the target-position
                                Level.currentPlayer().handleKeys();                     //handle all game keys for the player
                                Level.currentPlayer().getView().centerVerticalLook();   //change vertical camera
                                Level.currentPlayer().getView().animateDying();         //animate dying
                                Level.currentPlayer().moveToNewPosition();              //move player to new position ( collisions may influence new position )
                                Level.currentPlayer().performFloorChange();             //move player according to map collision ( floors )
                                Level.currentPlayer().handleWearponOrGadget();          //handle wearpon ( shoot or reload )
                                Level.current().animateWalls();                         //animate level
                                Level.current().animateBots();                          //animate bots
                                Level.current().checkItemCollisions();                  //check item-collisions
                            }

                            //items
                            //Item.animateAll();                                        //animate items?

                            AvatarMessage.animate();                        //animate msgs
                            HUDMessage.animateAll();                        //animate hud msgs
                            HUDFx.animateEffects();                         //animate hud-effects
                            FXPoint.handleAnimations();                     //animate particle system
                            LibGLDebugPoint.updateLifetime();               //update lifetime for debug-points
                            HUD.singleton.animateShowHide();                //animate wearpon & gadgets

                            break;
                        }
                    }

                    LibGL3D.panel.display();                        //draw gl for this tick

                    Fps.finishedDrawing();                          //update frames per second

                    //meassure tick time
                    iTickTime = ( System.currentTimeMillis() - iTickStart );

                    //set delay if desired
                    if ( iTickTime < ShooterSettings.Performance.MIN_THREAD_DELAY )
                    {
                        iDelay = ShooterSettings.Performance.MIN_THREAD_DELAY - iTickTime;
                    }
                    else
                    {
                        iDelay = 0;
                    }

                  //ShooterDebug.bugfix.out( "ticktime: [" + iTickTime + "] delay: [" + iDelay + "]" );

                    //delay
                    try { Thread.sleep( iDelay ); } catch ( InterruptedException ie ) {}
                }
            }
        }

        private void checkNewMenuEvents()
        {
            //change to game
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                iMainState = MainState.EGame;
            }
        }

        private void checkNewGameEvents()
        {
            //change to main menu
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                iMainState = MainState.EMainMenu;
            }

            //perform synchronized level change
            Level.checkChangeTo();

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
                Level.currentPlayer().launchAction();
            }

            //player action?
            if ( Keys.crouching.iLaunchAction )
            {
                Keys.crouching.iLaunchAction = false;
                Level.currentPlayer().toggleCrouching();
            }

            //gainHealth
            if ( Keys.gainHealth.iLaunchAction )
            {
                Keys.gainHealth.iLaunchAction = false;

                //heal player
                Level.currentPlayer().heal( 10 );
            }

            //hurt
            if ( Keys.damageFx.iLaunchAction )
            {
                Keys.damageFx.iLaunchAction = false;

                //hurt player
                Level.currentPlayer().hurt( 10 );
            }

            //launch msg?
            if ( Keys.avatarMessage.iLaunchAction )
            {
                Keys.avatarMessage.iLaunchAction = false;

                //move bot limbs
                Level.current().iBots.elementAt( 0 ).iBotMeshes.assignArmsPosition( ArmsPosition.values()[ AvatarMessage.currentDebugMsg % ArmsPosition.values().length ] );
                Level.current().iBots.elementAt( 0 ).iBotMeshes.assignLegsPosition( LegsPosition.values()[ AvatarMessage.currentDebugMsg % LegsPosition.values().length ] );

                AvatarMessage.showDebugMessage();

                //Level.current().iBots.elementAt( 0 ).say( Sound.EFemaleGiggle1 );
            }
        }

        @Override
        public boolean readyToDisplay()
        {
            return ( Level.current() != null );
        }

        @Override
        public final void draw2D()
        {
            //draw loading screen if not initialized
            if ( LibGL3D.glPanelInitialized )
            {
                switch ( iMainState )
                {
                    case EMainMenu:
                    {
                        //draw main menu
                        MainMenu.singleton.draw2D();
                        break;
                    }

                    case EGame:
                    {
                        //draw HUD
                        HUD.singleton.draw2D();
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
                //clear gl and flush draw queue
                LibGL3D.view.clearGl();
                LibGL3D.view.clearFaceQueues();

                switch ( iMainState )
                {
                    case EGame:
                    {
                        drawGameScreen();
                        break;
                    }

                    case EMainMenu:
                    {
                        drawGameScreen();
                        break;
                    }
                }

                LibGL3D.view.flushFaceQueue( Level.currentPlayer().getAnchor() );
/*
                GLView.singleton.clearFaceQueues();
                BulletHole.drawAll();                               //draw all bullet holes
                GLView.singleton.flushFaceQueue();
*/
                //debug drawings outside of the face queue
                LibGLDebugPoint.drawAll();                               //draw all debug points
            }
        }

        private final void drawGameScreen()
        {
            //level may be null if not set
            if ( Level.current() != null )
            {
                //get player's camera preference
                ViewSet cam = Level.currentPlayer().getCameraPositionAndRotation();

                //draw scene bg
                Level.current().drawBg( cam );

                //set player's camera
                LibGL3D.view.setPlayerCamera( cam.pos.x, cam.pos.y, cam.pos.z, cam.rot.x, cam.rot.y, cam.rot.z );

//GL11.glLoadIdentity();

                //all game components
                Level.current().draw();                             //draw the level
                Level.current().drawAllItems();                     //draw all items
                Level.current().drawAllBots();                      //draw all bots

                //bullet holes and fx points
                BulletHole.drawAll();                               //draw all bullet holes
                FXPoint.drawAll();                                  //draw all fx points

                //if ( false ) LWJGLView.drawTestSprite();

                Level.currentPlayer().drawStandingCircle();              //draw circle on players bottom location
            }
        }

        @Override
        public final void onFormDestroyed()
        {
          //ShooterDebug.major.out( "Main Form was closed - Shooter is destroyed" );
            iDestroyed = true;
        }

        @Override
        public final void onCompletedViewInits()
        {
            //parse hud's offsets
            OffsetsOrtho.parseOffsets( LibGL3D.panel.width, LibGL3D.panel.height );
        }

        /**************************************************************************************
        *   Inits all components of the project in a threaded manner.
        **************************************************************************************/
        private final void init()
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

            LibGLForm.setLookAndFeel( ShooterDebug.major );         //set host-os lookAndFeel
            ShooterD3dsFiles.init( ShooterDebug.d3ds );                    //import 3d studio max objects
            ShooterLevels.init();                                   //init mesh collections
            MainMenu.init();                                        //init main menu
            HUD.init();                                             //init Heads up display
            Sound.init();                                           //init sound-engine
            ShooterTexture.loadImages();                                   //load texture images
            Level.orderLevelChange( LevelConfig.ELevel1 );          //order change to level 1
            LibGL3D.init                                            //init external gl library
            (
                ShooterSettings.General.ENGINE_3D,
                ShooterSettings.Form.FORM_WIDTH,
                ShooterSettings.Form.FORM_HEIGHT,
                ShooterSettings.Form.FORM_TITLE,
                !General.DISABLE_LIGHTING,
                this,
                this,
                this,
                iconImage,
                bgImage,
                SwingKeys.singleton,
                SwingMouse.singleton,
                SwingMouse.singleton,
                SwingMouse.singleton,
                ShooterTexture.getAllTextureImages(),
                ShooterDebug.gl
            );
        }
    }
