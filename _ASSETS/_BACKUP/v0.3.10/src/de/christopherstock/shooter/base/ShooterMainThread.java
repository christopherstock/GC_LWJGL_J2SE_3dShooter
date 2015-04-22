/*  $Id: ShooterMainThread.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  java.awt.geom.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.fx.LibFX.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGL3D.Engine3D;
    import  de.christopherstock.lib.gl.LibGLFrame.GLCallbackForm;
    import  de.christopherstock.lib.gl.LibGLPanel.*;
    import  de.christopherstock.lib.ui.LibFPS;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.*;
    import  de.christopherstock.shooter.base.ShooterLevels.BotPlayers;
    import  de.christopherstock.shooter.g3d.mesh.BotMeshes.ArmsPosition;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.state.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.io.hid.lwjgl.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The application's main thread. Start this thread to run the application.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class ShooterMainThread extends Thread implements GLDrawCallback, GLCallbackForm
    {
        /**************************************************************************************
        *   Main states the application runs in.
        **************************************************************************************/
        public static enum MainState
        {
            EPreloader,
            EIntroLogo,
            EMainMenu,
            EGame,
        }

        public                      HUD             iHUD                        = null;
        public                      LibFPS          iFPS                        = null;

        /**************************************************************************************
        *   The application's current main state.
        **************************************************************************************/
        private                     MainState       iMainState                  = MainState.EPreloader;

        /**************************************************************************************
        *   The application's main state to enter the next tick.
        **************************************************************************************/
        private                     MainState       iMainStateToChangeTo        = null;

        /**************************************************************************************
        *   A flag being set to true if a closing-event on the main form is invoked.
        **************************************************************************************/
        private                     boolean         iDestroyed                  = false;
        private                     long            iTickStart                  = 0;
        private                     long            iTickTime                   = 0;
        private                     boolean         iTickDelaying               = false;
        private                     long            iTickDelay                  = 0;

        /***********************************************************************************
        *   The game's main-thread run-method performing and endless loop of ticks.
        ***********************************************************************************/
        @Override
        public void run()
        {
            //init ui and rest
            ShooterInit.initUi();
            ShooterInit.initRest();

            //main thread ticks until the app is destroyed
            while ( !iDestroyed )
            {
                //meassure tick time
                iTickStart = System.currentTimeMillis();

                //only perform game-loop if 3d-canvas is fully initialized
                if ( LibGL3D.glPanelInitialized )
                {
                    //change main state if desired
                    performMainStateChange();

                    //switch for mainState
                    switch ( iMainState )
                    {
                        case EIntroLogo:
                        {
                            //check keys and mouse for lwjgl
                            if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLKeys.checkKeys();
                            if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLMouse.checkMouse( false );

                            //check main-menu-action
                            Keys.toggleMainMenu.checkLaunchingAction();

                            //check menu key events
                            checkIntroLogoEvents();

                            //animate the intro logo
                            IntroLogo.getSingleton().onRun();

                            break;
                        }

                        case EPreloader:
                        {
                            //nothing to animate ?
                            break;
                        }

                        case EMainMenu:
                        {
                            //check keys and mouse for lwjgl
                            if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLKeys.checkKeys();
                            if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLMouse.checkMouse( false );

                            //check main menu toggle
                            Keys.toggleMainMenu.checkLaunchingAction();

                            //check menu key events
                            checkMenuKeyEvents();

                            //animate main menu
                            MainMenu.getSingleton().onRun();

                            break;
                        }

                        case EGame:
                        {
                            //check keys and mouse for lwjgl
                            if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLKeys.checkKeys();
                            if ( ShooterSettings.General.ENGINE_3D == Engine3D.LWJGL ) LWJGLMouse.checkMouse( true );

                            //check main menu toggle
                            Keys.toggleMainMenu.checkLaunchingAction();

                            //check game key events
                            checkGameKeyEvents();

                            //animate player and level ( only if a level is assigned )
                            if ( ShooterGameLevel.current() != null )
                            {
                                //animate level
                                ShooterGameLevel.current().onRun();
                            }

                            //maintain sounds
                            ShooterSound.onRun();

                            break;
                        }
                    }

                    //update frames per second
                    iFPS.finishedDrawing();

                    //draw gl for this tick
                    LibGL3D.panel.display();
                }

                //meassure tick time and set delay if desired
                iTickTime = ( System.currentTimeMillis() - iTickStart );
                if ( iTickTime < ShooterSettings.Performance.MIN_THREAD_DELAY )
                {
                    iTickDelay = ShooterSettings.Performance.MIN_THREAD_DELAY - iTickTime;

                    if ( !iTickDelaying )
                    {
                        iTickDelaying = true;
                        //ShooterDebug.mainThreadDelay.out( "start delaying main thread [" + iTickDelay + "]" );
                    }
                }
                else
                {
                    iTickDelay = 0;

                    if ( iTickDelaying )
                    {
                        iTickDelaying = false;
                        //ShooterDebug.mainThreadDelay.out( "stop delaying main thread" );
                    }
                }

                //ShooterDebug.bugfix.out( "ticktime: [" + iTickTime + "] delay: [" + iTickDelay + "]" );

                //delay for specified delay time
                Lib.delay( iTickDelay );
            }
        }

        private void checkMenuKeyEvents()
        {
            //change to game
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                orderMainStateChangeTo( MainState.EGame );
            }

            //change current main menu item
            if ( Keys.keyHoldWalkDown )
            {
                MainMenu.getSingleton().nextItem();
            }

            if ( Keys.keyHoldWalkUp )
            {
                MainMenu.getSingleton().previousItem();
            }
        }

        private void checkIntroLogoEvents()
        {
            //change to game
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                orderMainStateChangeTo(  MainState.EGame );
            }
        }

        private void checkGameKeyEvents()
        {
            //change to main menu
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                orderMainStateChangeTo( MainState.EMainMenu );
            }

            //perform synchronized level change
            ShooterGameLevel.checkChangeTo();

            //start new effects

            //launch exploisions?
            if ( Keys.explosion.iLaunchAction )
            {
                Keys.explosion.iLaunchAction = false;

                LibFXManager.launchExplosion( new LibVertex( 0.0f, 0.0f, 0.05f ), FXSize.ESmall,  FXTime.EShort, FxSettings.LIFETIME_EXPLOSION  );
                LibFXManager.launchExplosion( new LibVertex( 2.0f, 2.0f, 0.05f ), FXSize.EMedium, FXTime.EMedium, FxSettings.LIFETIME_EXPLOSION );
                LibFXManager.launchExplosion( new LibVertex( 4.0f, 4.0f, 0.05f ), FXSize.ELarge,  FXTime.ELong, FxSettings.LIFETIME_EXPLOSION   );

                //play explosion sound
                ShooterSound.EExplosion1.playDistancedFx( new Point2D.Float( 0.0f, 0.0f ) );
            }

            //player action?
            if ( Keys.playerAction.iLaunchAction )
            {
                Keys.playerAction.iLaunchAction = false;
                ShooterGameLevel.currentPlayer().launchAction( null );
            }

            //player action?
            if ( Keys.crouching.iLaunchAction )
            {
                Keys.crouching.iLaunchAction = false;
                ShooterGameLevel.currentPlayer().toggleCrouching();
            }

            //gainHealth
            if ( Keys.gainHealth.iLaunchAction )
            {
                Keys.gainHealth.iLaunchAction = false;

                //heal player
                ShooterGameLevel.currentPlayer().heal( 10 );
            }

            //hurt
            if ( Keys.damageFx.iLaunchAction )
            {
                Keys.damageFx.iLaunchAction = false;

                //hurt player
                ShooterGameLevel.currentPlayer().hurt( 10 );
            }

            //launch msg?
            if ( Keys.avatarMessage.iLaunchAction )
            {
                Keys.avatarMessage.iLaunchAction = false;

                try
                {
                    ShooterGameLevel.current().getBotByID( BotPlayers.OFFICE_PARTNER_1 ).iBotMeshes.assignArmsPosition( ArmsPosition.EAimHighBoth );
                }
                catch ( Throwable t )
                {
                    ShooterDebug.major.out( "Throwable caught in debug context" );
                }

/*
                //launch action on all bots
                if ( ShooterGameLevel.current().iBots.size() > 0 )
                {
                    for ( Bot b : ShooterGameLevel.current().iBots )
                    {
                        //move bot limbs



                        //launch bot sound
                        //b.makeDistancedSound( ShooterSound.EFemaleGiggle1 );
                    }
                }
*/
                //show avatar message
                //AvatarMessage.showDebugMessage();

                //Level.current().iBots.elementAt( 0 ).say( Sound.EFemaleGiggle1 );
            }
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
                        //draw 2d
                        Preloader.getSingleton().draw2D();
                        break;
                    }

                    case EIntroLogo:
                    {
                        //draw 2d
                        IntroLogo.getSingleton().draw2D();
                        break;
                    }

                    case EMainMenu:
                    {
                        //draw 2d
                        MainMenu.getSingleton().draw2D();
                        break;
                    }

                    case EGame:
                    {
                        //draw 2d
                        Ingame.getSingleton().draw2D();
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
                //clear face queue from last tick
                LibGL3D.view.clearFaceQueue();

                //draw 3d according to main state
                switch ( iMainState )
                {
                    case EPreloader:
                    {
                        //clear gl
                        Preloader.getSingleton().draw3D();
                        break;
                    }

                    case EIntroLogo:
                    {
                        //draw 3d
                        IntroLogo.getSingleton().draw3D();
                        break;
                    }

                    case EGame:
                    case EMainMenu:
                    {
                        //draw 3d
                        Ingame.getSingleton().draw3D();
                        break;
                    }
                }
            }
        }

        @Override
        public final void onFormDestroyed()
        {
          //ShooterDebug.major.out( "Main Form was closed - Shooter is destroyed" );
            iDestroyed = true;
        }

        protected final void orderMainStateChangeTo( MainState aFutureMainState )
        {
            iMainStateToChangeTo = aFutureMainState;
        }

        private final void performMainStateChange()
        {
            if ( iMainStateToChangeTo != null )
            {
                //center mouse for new mainstate
                LWJGLMouse.centerMouse();
                ShooterDebug.mouse.out( "Centered Mouse" );

                iMainState = iMainStateToChangeTo;
                iMainStateToChangeTo = null;
            }
        }
    }
