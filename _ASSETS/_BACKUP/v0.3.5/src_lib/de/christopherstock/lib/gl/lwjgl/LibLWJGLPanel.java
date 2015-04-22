/*  $Id: LibLWJGLPanel.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
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
    *   @version    0.3.5
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
                canvas  = new Canvas()
                {
                    @Override
                    public void paint( Graphics g )
                    {
                        //calling super-paint here is required
                        super.paint( g );

                        //cast to 2d
                        Graphics2D g2d = (Graphics2D)g;

                        //draw 2D
                        g2d.setClip( 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE );
                        g2d.drawImage( iBgImage, 0, 0, null );
                    }
                };

                //set canvas focusable
                canvas.setFocusable( true );
            }
            catch ( Exception e )
            {
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
