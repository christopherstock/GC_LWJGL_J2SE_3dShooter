/*  $Id: LibJOGLPanel.java 1224 2012-12-31 12:32:24Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl.jogl;

    import  java.awt.*;
    import  java.awt.event.*;
    import  javax.media.opengl.*;
    import  de.christopherstock.lib.gl.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public class LibJOGLPanel extends LibGLPanel
    {
        private                     GLJPanel            glJPanel            = null;

        public LibJOGLPanel( LibJOGLView glView, KeyListener kl, MouseListener ml, MouseMotionListener mml, MouseWheelListener mwl )
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
