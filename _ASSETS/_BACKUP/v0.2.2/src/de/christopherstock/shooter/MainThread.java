/*  $Id: MainThread.java 190 2010-12-13 20:12:09Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.GL3D.Engine3D;
    import  de.christopherstock.shooter.gl.GLPanel.*;
    import  de.christopherstock.shooter.gl.lwjgl.*;
    import  de.christopherstock.shooter.io.hid.lwjgl.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The main thread.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class MainThread extends Thread implements GLCallback3D, GLCallback2D
    {
        private     static  final   int             THREAD_DELAY                = 0;

        public      static          MainThread      singleton                   = null;

        public      static          boolean         launchShot                  = false;
        public      static          boolean         launchWearponReload         = false;
        public      static          boolean         launchExplosion             = false;
        public      static          boolean         letPlayerDie                = false;
        public      static          boolean         launchAvatarMessage         = false;
        public      static          boolean         launchHUDMessage            = false;
        public      static          boolean         launchPlayerAction          = false;

        private     static          long            startMeassuringMillis       = 0;
        private     static          int             framesDrawn                 = 0;
        public      static          int             currentFramesPerSecond      = 0;

        /***********************************************************************************
        *   The game's main-thread run-method performing and endless loop of ticks.
        ***********************************************************************************/
        @Override
        public void run()
        {
            //init threaded
            Shooter.initFromMainThread();

            //ticks as long as the window is running
            while ( !Shooter.destroyed )
            {
                //only perform game-loop if 3d-canvas fully initialized
                if ( GL3D.glPanelInitialized )
                {
                    //perform synchronized level change if desired
                    Level.checkChangeTo();

                    //update fps
                    updateFps();

                    //events
                    checkNewEvents();                                       //calculations being performed every tick

                    //check keys and mouse for lwjgl
                    if ( ShooterSettings.ENGINE_3D == Engine3D.LWJGL ) LWJGLKeys.checkKeys();
                    if ( ShooterSettings.ENGINE_3D == Engine3D.LWJGL ) LWJGLMouse.checkMouse();

                    //no level may be assigned for debug proposes
                    if ( Level.current() != null )
                    {
                        //check keys TODO group
                        Level.currentPlayer().handleKeysForMovement();      //handle game keys to specify player's new position
                        Level.currentPlayer().handleKeysForView();          //handle game keys to specify player's new view
                        Level.currentPlayer().handleKeysForActions();       //handle game keys to invoke actions

                        Level.currentPlayer().view.centerVerticalLook();    //change vertical camera
                        Level.currentPlayer().view.animateDying();          //animate dying

                        //animate player // TODO encapsulate
                        Level.currentPlayer().cylinder.moveToNewPosition( Level.currentPlayer().view.depthTotal );   //move player to new position ( collisions may influence new position )


                        Level.currentPlayer().performFloorChange();          //move player according to map collision ( floors )
                        Level.currentPlayer().handleWearponOrGadget();       //handle wearpon ( shoot or reload )

                        //animate level and bots
                        Level.current().animate();
                        Level.current().animateAllBots();               //animate bots
                        Level.current().checkCollisionOnBots( Level.currentPlayer().cylinder );    //check bot-collisions
                    }

                    //items
                    //Item.animateAll();                            //animate items?
                    Item.checkCollisions();                         //check item-collisions

                    //animate fx
                    AvatarMessage.animate();                        //animate msgs
                    HUDMessage.animateAll();                        //hud msgs
                    HUD.singleton.animateEffects();                 //animate hud-effects
                    FXPoint.handleAnimations();                     //handle animations
                    DebugPoint.updateLifetime();                    //update lifetime for debug-points
                    HUD.singleton.animateShowHide();                //for wearpon & gadgets

                    //draw gl
                    GLPanel.singleton.display();                      //launching the panel's display-method draws the whole screen for the current tick

                    //delay
                    try { Thread.sleep( THREAD_DELAY ); } catch ( InterruptedException ie ) {}

                    ++framesDrawn;
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
                AvatarMessage.showMessage( "Welcome to the Shooter-Engine ! This is just a demonstration of the line-break algorithm." );
            }

            //launch HUD-msg?
            if ( launchHUDMessage )
            {
                launchHUDMessage = false;

                //add msg to msg-queue
                HUDMessage.showMessage( StringCollection.HUD_MESSAGES[ LibMath.getRandom( 0, StringCollection.HUD_MESSAGES.length - 1 ) ] );
            }
        }

        private static final void updateFps()
        {
            //init fps-counter
            if ( startMeassuringMillis == 0 )
            {
                framesDrawn             = 0;
                startMeassuringMillis   = System.currentTimeMillis();
            }
            else
            {
                //check if 1 sec is over
                if ( System.currentTimeMillis() - startMeassuringMillis >= Lib.MILLIS_ONE_SECOND )
                {
                    currentFramesPerSecond  = framesDrawn;
                    framesDrawn             = 0;
                    startMeassuringMillis   = System.currentTimeMillis();
                }
            }
        }

        @Override
        public final void draw3D()
        {
            //clear gl and flush draw queue
            GLView.singleton.clearGl();
            GLView.singleton.clearFaceQueues();

            //level may be null if not set
            if ( Level.current() != null )
            {
                //get player's camera preference
                ViewSet cam = Level.currentPlayer().getCameraPositionAndRotation();

                //draw scene bg
                Level.current().drawBg( cam );

                //set player's camera
                GLView.singleton.setPlayerCamera( cam.posX, cam.posY, cam.posZ, cam.rotX, cam.rotY, cam.rotZ );

                //all game components
                Level.current().draw();                             //draw the level
                Item.drawAll();                                     //draw all items
                Level.current().drawAllBots();                      //draw all bots

                //bullet holes and fx points
                BulletHole.drawAll();                               //draw all bullet holes
                FXPoint.drawAll();                                  //draw all fx points

                if ( false ) LWJGLView.drawTestSprite();

                Level.currentPlayer().drawStandingCircle();              //draw circle on players bottom location
            }

            GLView.singleton.flushFaceQueue();

/*
            GLView.singleton.clearFaceQueues();
            BulletHole.drawAll();                               //draw all bullet holes
            GLView.singleton.flushFaceQueue();
*/

            //debug drawings outside of the face queue
            DebugPoint.drawAll();                               //draw all debug points
        }

        @Override
        public final void draw2D()
        {
            //draw loading screen if not initialized
            if ( GL3D.glPanelInitialized )
            {
                //draw HUD if gl is initialized
                HUD.singleton.draw2D();
            }
        }
    }
