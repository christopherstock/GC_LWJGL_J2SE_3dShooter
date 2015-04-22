/*  $Id: LibLWJGLPanel.java 639 2011-04-24 00:44:43Z jenetic.bytemare $
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
    *   @version    0.3.3
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
            if ( LibGL3D.glPanelInitialized )
            {
                //only if a level is assigned
                if ( drawCallback.readyToDisplay() || true )
                {
                    //invoke callback drawables
                    drawCallback.draw3D();

                    //callback3D.flush3Dbuffer();
                    drawCallback.draw2D();
                }

                //update native LWJGL Display each tick ! else it will hang!
                Display.update();
            }
        }
    }
