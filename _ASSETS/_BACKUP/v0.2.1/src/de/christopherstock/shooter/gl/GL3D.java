/*  $Id: GL3D.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  de.christopherstock.shooter.gl.GLPanel.GLCallback2D;
    import  de.christopherstock.shooter.gl.GLPanel.GLCallback3D;
    import  de.christopherstock.shooter.gl.jogl.*;
import  de.christopherstock.shooter.gl.lwjgl.*;
import de.christopherstock.shooter.io.hid.swing.*;

    public class GL3D
    {
        public static enum Engine3D
        {
            /************************************************************************************
            *   The 3D engine of choice.
            ************************************************************************************/
            JOGL,

            /************************************************************************************
            *   The 3D engine of choice.
            ************************************************************************************/
            LWJGL,
        }

        /**************************************************************************************
        *   A flag being set to true if the init() method of the glView-singleton has been performed.
        **************************************************************************************/
        public      static          boolean     glPanelInitialized                  = false;

        public static final void init( Engine3D engine, GLCallback3D callback3D, GLCallback2D callback2D )
        {
            //load textures
            GLTexture.textureImages = GLTexture.loadTextureImages();

            switch ( engine )
            {
                case JOGL:
                {
                    //init 3d engine
                    GLView.singleton = new JOGLView
                    (
                        callback2D,
                        callback3D,
                        GLTexture.textureImages
                    );

                    //init gl panel
                    GLPanel.glPanel = new JOGLPanel
                    (
                        (JOGLView)GLView.singleton,
                        SwingKeys.singleton,
                        SwingMouse.singleton,
                        SwingMouse.singleton,
                        SwingMouse.singleton
                    );

                    //init and show form
                    GLForm.singleton = new GLForm
                    (
                        GLPanel.glPanel.getNativePanel()
                    );
                    break;
                }

                case LWJGL:
                {
                    //init panel
                    GLPanel.glPanel = new LWJGLPanel
                    (
                        callback2D,
                        callback3D
                    );

                    //show lwjgl form
                    GLForm.singleton = new GLForm
                    (
                        GLPanel.glPanel.getNativePanel()
                    );

                    //init lwjgl view
                    GLView.singleton = new LWJGLView
                    (
                        GLTexture.textureImages
                    );
                    break;
                }
            }
        }
    }
