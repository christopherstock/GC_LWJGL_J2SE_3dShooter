/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.opengl;

    import  java.awt.*;
    import  java.awt.event.*;
    import  javax.media.opengl.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class GLPanel extends GLJPanel
    {
        public static interface Drawable
        {
            public abstract void draw( Graphics2D g );
        }

        public      static          int             PANEL_WIDTH             = 0;
        public      static          int             PANEL_HEIGHT            = 0;

        public      static          GLPanel         glPanel                 = null;
        public                      Drawable        hud                     = null;

        public GLPanel( GLView glView, Drawable aHud, KeyListener kl, MouseListener ml, MouseMotionListener mml, MouseWheelListener mwl )
        {
            super();

            hud = aHud;

            addGLEventListener(     glView  );
            addKeyListener(         kl      );
            addMouseListener(       ml      );
            addMouseMotionListener( mml     );
            addMouseWheelListener(  mwl     );
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
            hud.draw(                           g2d );      //draw heads up display
        }

        public static final void releaseClip( Graphics g )
        {
            g.setClip( 0, 0, PANEL_WIDTH, PANEL_HEIGHT );
        }
    }
