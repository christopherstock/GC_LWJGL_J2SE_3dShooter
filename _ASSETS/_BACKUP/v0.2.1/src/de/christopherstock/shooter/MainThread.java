/*  $Id: Timer.java,v 1.2 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.GL3D.Engine3D;
    import  de.christopherstock.shooter.gl.GLPanel.*;
import de.christopherstock.shooter.io.hid.lwjgl.*;
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
                    //update fps
                    updateFps();

                    //events
                    checkNewEvents();       //calculations being performed every tick

                    //world
                    Level.animate();

                    //check keys and mouse for lwjgl
                    if ( ShooterSettings.ENGINE_3D == Engine3D.LWJGL ) LWJGLKeys.checkKeys();
                    if ( ShooterSettings.ENGINE_3D == Engine3D.LWJGL ) LWJGLMouse.checkMouse();

                    //check keys
                    Player.singleton.handleKeysForMovement();       //handle game keys to specify player's new position
                    Player.singleton.view.handleKeysForView();      //handle game keys to specify player's new view
                    Player.singleton.handleKeysForActions();        //handle game keys to invoke actions

                    Player.singleton.cylinder.moveToNewPosition( Player.singleton.view.depthTotal );   //move player to new position ( collisions may influence new position )
                    Player.singleton.performFloorChange();          //move player according to map collision ( floors )
                    Player.singleton.view.centerVerticalLook();     //change vertical camera
                    Player.singleton.handleWearponOrGadget();       //handle wearpon ( shoot or reload )

                    //only animate on dying
                    if ( Player.singleton.dying ) Player.singleton.view.animateDying();    //animate death animation

                    //bots
                    Level.animateAllBots();                         //animate bots
                    Bot.checkCollisionsAll( Player.singleton.cylinder );         //check bot-collisions

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
                    GLPanel.glPanel.display();                      //launching the panel's display-method draws the whole screen for the current tick

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
                Player.singleton.hurt( Player.MAX_HEALTH );
            }


            //player action?
            if ( launchPlayerAction )
            {
                launchPlayerAction = false;
                Player.singleton.launchAction();
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
            //clear gl
            GLView.singleton.clearGl();

            //get player's camera preference
            float[] cam = Player.singleton.getCameraPositionAndRotation();

            //draw scene bg
            BackGround.EDesert.drawOrtho( cam[ 3 ], cam[ 5 ] );

            //set player's camera
            GLView.singleton.setPlayerCamera( cam[ 0 ], cam[ 1 ], cam[ 2 ], cam[ 3 ], cam[ 4 ], cam[ 5 ] );

            //all game components
            Level.draw();                                       //draw the level
            Item.drawAll();                                     //draw all items
            Bot.drawAll();                                      //draw all bots

            //bullet holes and fx points
            BulletHole.drawAll();                               //draw all bullet holes
            FXPoint.drawAll();                                  //draw all fx points

            //debug drawings
            DebugPoint.drawAll();                               //draw all debug points
            Player.singleton.drawStandingCircle();              //draw circle on players bottom location
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
