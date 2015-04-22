/*  $Id: MainThread.java 266 2011-02-10 20:25:39Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLForm.GLCallbackForm;
    import  de.christopherstock.lib.gl.LibGLView.GLCallbackInit;
    import  de.christopherstock.lib.gl.LibGL3D.*;
    import  de.christopherstock.lib.gl.LibGLPanel.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.ShooterSettings.Offset;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.io.hid.lwjgl.*;
    import  de.christopherstock.shooter.io.hid.swing.*;
    import  de.christopherstock.shooter.ui.hud.*;
    import  de.christopherstock.shooter.ui.hud.AvatarMessage.AvatarImage;

    /**************************************************************************************
    *   The main thread. Don't mix up with the ui-thread!
    *   Start this thread to rock and roll!
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class ShooterMainThread extends Thread implements GLCallback3D, GLCallback2D, GLCallbackForm, GLCallbackInit
    {
        private     static  final   int             MIN_THREAD_DELAY            = 20;

        public      static          boolean         launchShot                  = false;
        public      static          boolean         launchReload                = false;
        public      static          boolean         launchExplosion             = false;
        public      static          boolean         letPlayerDie                = false;
        public      static          boolean         launchAvatarMessage         = false;
        public      static          boolean         launchPlayerAction          = false;

        /**************************************************************************************
        *   A flag being set to true if a closing-event on the main form is invoked.
        **************************************************************************************/
        public                      boolean         destroyed                   = false;

        public                      long            tickStart                   = 0;
        public                      long            tickTime                    = 0;
        public                      long            iDelay                      = 0;

        /***********************************************************************************
        *   The game's main-thread run-method performing and endless loop of ticks.
        ***********************************************************************************/
        @Override
        public void run()
        {
            //init threaded
            init();

            //ticks as long as the window is running
            while ( !destroyed )
            {
                //only perform game-loop if 3d-canvas fully initialized
                if ( LibGL3D.glPanelInitialized )
                {
                    tickStart = System.currentTimeMillis();

                    //perform synchronized level change if desired
                    Level.checkChangeTo();

                    //events
                    checkNewEvents();                                       //calculations being performed every tick

                    //check keys and mouse for lwjgl
                    if ( ShooterSettings.ENGINE_3D == Engine3D.LWJGL ) LWJGLKeys.checkKeys();
                    if ( ShooterSettings.ENGINE_3D == Engine3D.LWJGL ) LWJGLMouse.checkMouse();

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
                        Level.current().checkBotCollisions();                   //check bot-collisions
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

                    //draw gl
                    LibGL3D.panel.display();                      //launching the panel's display-method draws the whole screen for the current tick

                    //update fps
                    Fps.finishedDrawing();

                    tickTime = ( System.currentTimeMillis() - tickStart );

                    //set delay if desired
                    if ( tickTime < MIN_THREAD_DELAY )
                    {
                        iDelay = MIN_THREAD_DELAY - tickTime;
                    }
                    else
                    {
                        iDelay = 0;
                    }

                    //delay
                    //DebugSystem.bugfix.out( "delay: [" + iDelay + "]" );
                    try { Thread.sleep( iDelay ); } catch ( InterruptedException ie ) {}
                }
            }
        }

        private void checkNewEvents()
        {
            //start new effects

            //launch exploisions?
            if ( launchExplosion )
            {
                launchExplosion = false;
                FX.launchTestExplosion();
            }

            if ( letPlayerDie )
            {
                letPlayerDie        = false;
                Level.currentPlayer().kill();
            }

            //player action?
            if ( launchPlayerAction )
            {
                launchPlayerAction = false;
                Level.currentPlayer().launchAction();
            }

            //launch msg?
            if ( launchAvatarMessage )
            {
                launchAvatarMessage = false;

                switch ( LibMath.getRandom( 0, 2 ) )
                {
                    case 0:
                    {
                        AvatarMessage.showMessage( AvatarImage.EWoman, StringCollection.AvatarMessages.TUTORIAL_CYCLE_WEARPONS );
                        break;
                    }

                    case 1:
                    {
                        AvatarMessage.showMessage( AvatarImage.EWoman2, StringCollection.AvatarMessages.TUTORIAL_LOOK_AND_MOVE );
                        break;
                    }

                    case 2:
                    {
                        AvatarMessage.showMessage( AvatarImage.EMan, StringCollection.AvatarMessages.TUTORIAL_PERFORM_ACTIONS );
                        break;
                    }
                }
            }
        }

        @Override
        public final void draw2D()
        {
            //draw loading screen if not initialized
            if ( LibGL3D.glPanelInitialized )
            {
                //draw HUD if gl is initialized
                HUD.singleton.draw2D();
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

                //level may be null if not set
                if ( Level.current() != null )
                {
                    //get player's camera preference
                    ViewSet cam = Level.currentPlayer().getCameraPositionAndRotation();

                    //draw scene bg
                    Level.current().drawBg( cam );

                    //set player's camera
                    LibGL3D.view.setPlayerCamera( cam.posX, cam.posY, cam.posZ, cam.rotX, cam.rotY, cam.rotZ );

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

        @Override
        public final void onFormDestroyed()
        {
            ShooterDebugSystem.bugfix.out( "Main Form was closed - Shooter is destroyed" );
            destroyed = true;
        }

        @Override
        public final void onCompletedViewInits()
        {
            //parse hud's offsets
            Offset.parseOffsets( LibGL3D.panel.width, LibGL3D.panel.height );
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
                iconImage   = ImageIO.read( LibGLForm.class.getResourceAsStream( ShooterSettings.Path.EScreen.url + "icon.png" ) );
                bgImage     = ImageIO.read( LibGLForm.class.getResourceAsStream( ShooterSettings.Path.EScreen.url + "bg.png"   ) );
            }
            catch ( IOException ioe )
            {
                ShooterDebugSystem.error.err("ERROR! Screen-Graphics could not be loaded!");
            }

            LibGLForm.setLookAndFeel( ShooterDebugSystem.bugfix );                             //set host-os lookAndFeel
            D3dsFiles.init( ShooterDebugSystem.d3ds, ShooterSettings.Path.E3dsMax.url );       //import 3d studio max objects
            WallCollection.init();                                                      //init mesh collections
            HUD.init();                                                                 //init Heads up display
            Sound.init();                                                               //init sound-engine
            Texture.loadImages();                                                       //load texture images
            Level.orderLevelChange( LevelConfig.ELevel1 );                              //order change to level 1
            LibGL3D.init                                                                //init external gl library
            (
                ShooterSettings.ENGINE_3D,
                ShooterSettings.Form.FORM_WIDTH,
                ShooterSettings.Form.FORM_HEIGHT,
                ShooterSettings.Form.FORM_TITLE,
                !ShooterSettings.DISABLE_LIGHTING,
                this,
                this,
                this,
                this,
                iconImage,
                bgImage,
                SwingKeys.singleton,
                SwingMouse.singleton,
                SwingMouse.singleton,
                SwingMouse.singleton,
                Texture.getAllTextureImages(),
                ShooterDebugSystem.gl
            );
        }
    }
