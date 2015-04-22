/*  $Id: Ingame.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.state;

    import  de.christopherstock.shooter.Shooter;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.gl.*;

    /**************************************************************************************
    *   The application's main thread. Start this thread to run the application.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class Ingame
    {
        public      static          Ingame          singleton                   = null;

        private Ingame()
        {
            //singleton constructor
        }

        public static final Ingame getSingleton()
        {
            if ( singleton == null ) singleton = new Ingame();
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
            if ( ShooterGameLevel.current() != null )
            {
                //reset gl
                LibGL3D.view.clearGl( ShooterGameLevel.current().getBackgroundColor() );

                //get camera from player's position and orientation
                ViewSet cam = ShooterGameLevel.currentPlayer().getCameraPositionAndRotation();

                //draw scene bg
                ShooterGameLevel.current().drawBg( cam );

                //set player's camera
                LibGL3D.view.setCamera( cam );

                //this would be the right time to enable lights
                //draw all game components
                ShooterGameLevel.current().draw();                          //draw the level
                ShooterGameLevel.current().drawAllItems();                  //draw all items
                ShooterGameLevel.current().drawAllBots();                   //draw all bots

                //draw player's crosshair if the wearpon uses ammo
              //if ( ShooterGameLevel.currentPlayer().showAmmoInHUD() ) ShooterGameLevel.currentPlayer().getCrosshair().draw();

                //bullet holes and fx points
                BulletHole.drawAll();                                           //draw all bullet holes
                LibFXManager.drawAll();                                         //draw all fx points
                ShooterGameLevel.currentPlayer().drawStandingCircle();          //draw circle on players bottom location

                //flush face queue to force an immediate redraw
                LibGL3D.view.flushFaceQueue( ShooterGameLevel.currentPlayer().getAnchor() );
            }
        }
    }
