/*  $Id: Ingame.java 1240 2013-01-02 14:43:47Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.state;

    import  de.christopherstock.shooter.Shooter;
    import  de.christopherstock.shooter.ShooterDebug;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.BotMeshes.ArmsPosition;
    import  de.christopherstock.shooter.io.hid.Keys;
    import  de.christopherstock.shooter.level.*;
    import  de.christopherstock.shooter.level.setup.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.fx.*;
    import  de.christopherstock.lib.gl.*;

    /**************************************************************************************
    *   The application's main thread. Start this thread to run the application.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public class MainStateIngame
    {
        public      static          MainStateIngame          singleton                   = null;

        private MainStateIngame()
        {
            //singleton constructor
        }

        public static final MainStateIngame getSingleton()
        {
            if ( singleton == null ) singleton = new MainStateIngame();
            return singleton;
        }

        public final void draw2D()
        {
            //draw hud
            Shooter.mainThread.iHUD.draw2D();
        }

        public final void draw3D()
        {
            //level may be null if not set
            if ( Level.currentSection() != null )
            {
                //reset gl
                LibGL3D.view.clearGl( Level.currentSection().getBackgroundColor() );

                //get camera from player's position and orientation
                ViewSet cam = Level.currentPlayer().getCameraPositionAndRotation();

                //draw scene bg
                Level.currentSection().drawBg( cam );

                //set player's camera
                LibGL3D.view.setCamera( cam );

                //this would be the right time to enable lights
                //draw all game components
                Level.currentSection().draw();                          //draw the level
                Level.currentSection().drawAllItems();                  //draw all items
                Level.currentSection().drawAllBots();                   //draw all bots

                //draw player's crosshair if the wearpon uses ammo
              //if ( ShooterGameLevel.currentPlayer().showAmmoInHUD() ) ShooterGameLevel.currentPlayer().getCrosshair().draw();

                //bullet holes and fx points
                BulletHole.drawAll();                                           //draw all bullet holes
                LibFXManager.drawAll();                                         //draw all fx points
                Level.currentPlayer().drawStandingCircle();          //draw circle on players bottom location

                //flush face queue to force an immediate redraw
                LibGL3D.view.flushFaceQueue( Level.currentPlayer().getAnchor() );
            }
        }


        public void checkGameKeyEvents()
        {
            //check main menu toggle
            Keys.toggleMainMenu.checkLaunchingAction();

            //change to main menu
            if ( Keys.toggleMainMenu.iLaunchAction )
            {
                Keys.toggleMainMenu.iLaunchAction = false;
                Shooter.mainThread.orderMainStateChangeTo( MainState.EMainMenu );
            }

            //launch test exploisions?
            if ( Keys.explosion.iLaunchAction )
            {
                Keys.explosion.iLaunchAction = false;
/*
                float baseZ = ShooterGameLevel.currentPlayer().getAnchor().z;

                LibFXManager.launchExplosion( new LibVertex( 0.0f, 0.0f, 0.05f ), FXSize.ESmall,  FXTime.EShort, FxSettings.LIFETIME_EXPLOSION,  baseZ  );
                LibFXManager.launchExplosion( new LibVertex( 2.0f, 2.0f, 0.05f ), FXSize.EMedium, FXTime.EMedium, FxSettings.LIFETIME_EXPLOSION, baseZ  );
                LibFXManager.launchExplosion( new LibVertex( 4.0f, 4.0f, 0.05f ), FXSize.ELarge,  FXTime.ELong, FxSettings.LIFETIME_EXPLOSION,   baseZ  );

                //play explosion sound
                ShooterSound.EExplosion1.playDistancedFx( new Point2D.Float( 0.0f, 0.0f ) );
*/
            }

            //player action?
            if ( Keys.playerAction.iLaunchAction )
            {
                Keys.playerAction.iLaunchAction = false;
                Level.currentPlayer().launchAction( null );
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
            if ( Keys.enterKey.iLaunchAction )
            {
                Keys.enterKey.iLaunchAction = false;

                try
                {
                    Level.currentSection().getBotByID( LevelSetupTestFacility.OFFICE_PARTNER_1 ).iBotMeshes.assignArmsPosition( ArmsPosition.EAimHighBoth );
                }
                catch ( Throwable t )
                {
                    ShooterDebug.error.out( "Throwable caught in debug code!" );
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
    }
