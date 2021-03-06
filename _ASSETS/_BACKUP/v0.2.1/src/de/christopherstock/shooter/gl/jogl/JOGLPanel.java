/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.jogl;

    import  java.awt.*;
    import  java.awt.event.*;
    import  javax.media.opengl.*;
    import  de.christopherstock.shooter.gl.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class JOGLPanel extends GLPanel
    {
        public                      GLJPanel            glJPanel            = null;

        public JOGLPanel( JOGLView glView, KeyListener kl, MouseListener ml, MouseMotionListener mml, MouseWheelListener mwl )
        {
            glJPanel = new GLJPanel();

            //set gl, key and mouse listener
            glJPanel.addGLEventListener(     glView  );
            glJPanel.addKeyListener(         kl      );
            glJPanel.addMouseListener(       ml      );
            glJPanel.addMouseMotionListener( mml     );
            glJPanel.addMouseWheelListener(  mwl     );
        }

        @Override
        public final Component getNativePanel()
        {
            return glJPanel;
        }

        @Override
        public final void display()
        {
            //call native display() method
            glJPanel.display();
        }
    }
