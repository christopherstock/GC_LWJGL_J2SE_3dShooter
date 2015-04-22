/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  java.awt.*;
    import  javax.media.opengl.*;

    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class GLPanel extends GLJPanel
    {
        public      static          int             PANEL_WIDTH                     = 0;
        public      static          int             PANEL_HEIGHT                    = 0;

        public      static          GLPanel         glPanel                         = null;

        public GLPanel( GLView glView )
        {
            super();

            addGLEventListener(     glView          );
            addKeyListener(         Keys.singleton  );
            addMouseListener(       Mouse.singleton );
            addMouseMotionListener( Mouse.singleton );
            addMouseWheelListener(  Mouse.singleton );
        }

        @Override
        public void update( Graphics g )
        {
            paint( g );
        }

        @Override
        public void paint( Graphics g )
        {
            Graphics2D      g2d             = (Graphics2D)g;
            super.paint(                    g2d );      //invoke super-paint

            //draw panel if gl is initialized
            if ( Shooter.glPanelInitialized )
            {
                HUD.draw(                   g2d );      //draw heads up display
                Player.user.drawDebugLog(   g2d );      //draw debug logs
            }
        }

        public static final void releaseClip( Graphics g )
        {
            g.setClip( 0, 0, PANEL_WIDTH, PANEL_HEIGHT );
        }
    }
