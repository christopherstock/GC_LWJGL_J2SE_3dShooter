/*  $Id: LWJGLPanel.java 200 2011-01-08 12:29:59Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl.lwjgl;

    import  java.awt.*;
import java.awt.image.*;

    import  org.lwjgl.opengl.*;
    import  de.christopherstock.lib.gl.*;
import  de.christopherstock.shooter.game.*;

    /**************************************************************************************
    *   The Form.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class LibLWJGLPanel extends LibGLPanel
    {
        private                     Canvas                  canvas              = null;
        private                     GLCallback2D            callback2D          = null;
        private                     GLCallback3D            callback3D          = null;
        protected                   BufferedImage           iBgImage            = null;

        public LibLWJGLPanel( GLCallback2D aCallbackDrawing2D, GLCallback3D aCallbackDrawing3D, BufferedImage aBgImage )
        {
            callback2D  = aCallbackDrawing2D;
            callback3D  = aCallbackDrawing3D;
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
            //only if a level is assigned
            if ( Level.current() != null )
            {
                //invoke callback drawables
                callback3D.draw3D();
                //callback3D.flush3Dbuffer();
                callback2D.draw2D();

                //update native LWJGL Display
                Display.update();
            }
        }
    }
