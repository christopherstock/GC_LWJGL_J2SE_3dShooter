/*  $Id: LibLWJGLPanel.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl.lwjgl;

    import  java.awt.*;
    import  java.awt.image.*;
    import  org.lwjgl.opengl.*;
    import  de.christopherstock.lib.gl.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class LibLWJGLPanel extends LibGLPanel
    {
        private                     Canvas                  canvas              = null;
        private                     GLDrawCallback          drawCallback        = null;
        protected                   BufferedImage           iBgImage            = null;

        public LibLWJGLPanel( GLDrawCallback aDrawCallback, BufferedImage aBgImage )
        {
            drawCallback  = aDrawCallback;
            iBgImage    = aBgImage;

            try
            {
                canvas  = new LibLWJGLCanvas( iBgImage );

                //set canvas focusable
                canvas.setFocusable( true );
            }
            catch ( Exception e )
            {
                //ignore exception
            }
        }

        @Override
        public final Component getNativePanel()
        {
            return canvas;
        }

        @Override
        public final void display()
        {
            //only if the panel is initialized
            if ( LibGL3D.glPanelInitialized )
            {
                //invoke callback 3d drawing
                drawCallback.draw3D();

                //invoke callback 2d drawing
                drawCallback.draw2D();

                //update native LWJGL Display each tick
                Display.update();
            }
        }
    }
