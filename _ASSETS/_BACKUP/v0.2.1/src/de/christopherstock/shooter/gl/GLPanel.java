/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
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

        public      static          GLPanel             glPanel                 = null;

        public                      int                 width                   = 0;
        public                      int                 height                  = 0;

        public abstract Component getNativePanel();

        public abstract void display();
    }
