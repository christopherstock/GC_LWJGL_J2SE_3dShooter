/*  $Id: LibGLPanel.java 200 2011-01-08 12:29:59Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.awt.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public abstract class LibGLPanel
    {
        public static interface GLCallback3D
        {
            public abstract void draw3D();
        }

        public static interface GLCallback2D
        {
            public abstract void draw2D();
        }

        public                      int                 width                   = 0;
        public                      int                 height                  = 0;

        public abstract Component getNativePanel();

        public Graphics2D getGraphics()
        {
            return (Graphics2D)getNativePanel().getGraphics();
        }

        public abstract void display();
    }
