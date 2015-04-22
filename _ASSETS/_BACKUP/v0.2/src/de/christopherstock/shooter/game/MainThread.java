/*  $Id: Timer.java,v 1.2 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.opengl.*;
    import  de.christopherstock.shooter.gl.opengl.GLPanel.Drawable;
    import  de.christopherstock.shooter.math.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The main thread.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class MainThread extends Thread implements Drawable
    {
        private     static  final   int             THREAD_DELAY                = 0;
        private     static  final   int             MILLIS_ONE_SECOND           = 1000;

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

        public static final void init()
        {
            singleton = new MainThread();
            singleton.start();
        }

        /***********************************************************************************
        *   The game's main-thread run-method performing and endless loop of ticks.
        ***********************************************************************************/
        @Override
        public void run()
        {
            //ticks as long as the window is running
            while ( !Shooter.destroyed )
            {
                //only perform game-loop if 3d-canvas fully initialized
                if ( !Shooter.glPanelInitialized ) continue;

                //update fps
                updateFps();

                //events
                checkNewEvents();                                       //calculations being performed every tick

                //world
                Level.animate();


                //player
                Player.singleton.handleKeysForMovement();                    //handle game keys to specify player's new position
                Player.singleton.view.handleKeysForView( Player.singleton.cylinder );                   //handle game keys to specify player's new view
                Player.singleton.handleKeysForActions();                     //handle game keys to invoke actions

                Player.singleton.cylinder.moveToNewPosition( Player.singleton.view.depthTotal );   //move player to new position ( collisions may influence new position )
                Player.singleton.performFloorChange();                       //move player according to map collision ( floors )
                Player.singleton.view.centerVerticalLook();                       //change vertical camera
                Player.singleton.handleWearponOrGadget();                    //handle wearpon ( shoot or reload )

                //only animate on dying
                if ( Player.singleton.dying ) Player.singleton.view.animateDying();    //animate death animation

                //bots
                Level.animateAllBots();                                     //animate bots
                Bot.checkCollisionsAll( Player.singleton.cylinder );         //check bot-collisions

                //items
                //Item.animateAll();                                    //animate items?
                Item.checkCollisions();                                 //check item-collisions

                //animate fx
                AvatarMessage.animate();                                //animate msgs
                HUDMessage.animateAll();                                //hud msgs
                HUD.singleton.animateEffects();                                   //animate hud-effects
                FXPoint.handleAnimations();                             //handle animations
                DebugPoint.updateLifetime();                            //update lifetime for debug-points
                HUD.singleton.animateShowHide();                                  //for wearpon & gadgets

                //hot! :-) and OPERATIVE !! :-D  xD xD !!
                //Meshes.office1.rotateXYZ( 0.0f, 0.0f, ++rotTest );
                //Meshes.office1.translate( 0.0f, rotTest += 0.0001f, 0.0f );

                //draw
                GLPanel.glPanel.display();              //launching the panel's display-method draws the whole screen for the current tick

                //delay
                try { Thread.sleep( THREAD_DELAY ); } catch ( InterruptedException ie ) {}

                ++framesDrawn;

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
                HUDMessage.showMessage( StringCollection.HUD_MESSAGES[ UMath.getRandom( 0, StringCollection.HUD_MESSAGES.length - 1 ) ] );

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
                if ( System.currentTimeMillis() - startMeassuringMillis >= MILLIS_ONE_SECOND )
                {
                    currentFramesPerSecond  = framesDrawn;
                    framesDrawn             = 0;
                    startMeassuringMillis   = System.currentTimeMillis();
                }
            }
        }

        @Override
        public final void draw( Graphics2D g2d )
        {
            GLView.singleton.drawSceneBg( Texture.EBgDesert.ordinal() ); //draw background => to Level class
            GLView.singleton.setPlayerCamera();                          //set the player's camera in new identity!
            Level.draw();                               //draw the level
            BulletHole.drawAll();                       //draw bullet holes
            FXPoint.drawFXPoints();                     //draw fx points
            DebugPoint.drawDebugPoints();               //draw debug points
            Player.singleton.cylinder.drawStandingCircle(); //draw circle on players bottom location
        }
    }
