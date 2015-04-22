/*  $Id: GL3D.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import de.christopherstock.shooter.game.*;
import  de.christopherstock.shooter.gl.GLPanel.GLCallback2D;
    import  de.christopherstock.shooter.gl.GLPanel.GLCallback3D;
    import  de.christopherstock.shooter.gl.jogl.*;
    import  de.christopherstock.shooter.gl.lwjgl.*;
import  de.christopherstock.shooter.io.hid.swing.*;

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
            Texture.textureImages = Texture.loadTextureImages();

            switch ( engine )
            {
                case JOGL:
                {
                    //init 3d engine
                    GLView.singleton = new JOGLView
                    (
                        callback2D,
                        callback3D,
                        Texture.textureImages
                    );

                    //init gl panel
                    GLPanel.singleton = new JOGLPanel
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
                        GLPanel.singleton.getNativePanel()
                    );
                    break;
                }

                case LWJGL:
                {
                    //init panel
                    GLPanel.singleton = new LWJGLPanel
                    (
                        callback2D,
                        callback3D
                    );

                    //show lwjgl form
                    GLForm.singleton = new GLForm
                    (
                        GLPanel.singleton.getNativePanel()
                    );

                    //init lwjgl view
                    GLView.singleton = new LWJGLView
                    (
                        Texture.textureImages
                    );
                    break;
                }
            }
        }
    }
