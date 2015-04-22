/*  $Id: GLPanel.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  java.awt.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public abstract class GLPanel
    {
        public static interface GLCallback3D
        {
            public abstract void draw3D();
        }

        public static interface GLCallback2D
        {
            public abstract void draw2D();
        }

        public      static          GLPanel             singleton                 = null;

        public                      int                 width                   = 0;
        public                      int                 height                  = 0;

        public abstract Component getNativePanel();

        public abstract void display();
    }
