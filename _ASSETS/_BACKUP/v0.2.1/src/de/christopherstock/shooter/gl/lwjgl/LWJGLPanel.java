/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.lwjgl;

    import  java.awt.*;
    import  org.lwjgl.opengl.*;
    import  de.christopherstock.shooter.gl.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class LWJGLPanel extends GLPanel
    {
        public                      Canvas              canvas              = null;

        public                      GLCallback2D          callbackDrawing2D   = null;
        public                      GLCallback3D          callbackDrawing3D   = null;

        public LWJGLPanel( GLCallback2D aCallbackDrawing2D, GLCallback3D aCallbackDrawing3D )
        {
            callbackDrawing2D   = aCallbackDrawing2D;
            callbackDrawing3D   = aCallbackDrawing3D;
            canvas              = new Canvas();
        }

        @Override
        public final Component getNativePanel()
        {
            return canvas;
        }

        @Override
        public final void display()
        {
            //invoke callback drawables
            callbackDrawing3D.draw3D();
            //callback3D.flush3Dbuffer();
            callbackDrawing2D.draw2D();

            //update native LWJGL Display
            Display.update();
        }
    }
