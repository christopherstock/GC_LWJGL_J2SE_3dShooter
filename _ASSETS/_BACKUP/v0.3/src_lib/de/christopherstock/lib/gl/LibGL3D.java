/*  $Id: GL3D.java 202 2011-01-09 17:41:33Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.awt.event.*;
    import  java.awt.image.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.gl.LibGLForm.GLCallbackForm;
    import  de.christopherstock.lib.gl.LibGLView.*;
    import  de.christopherstock.lib.gl.LibGLPanel.*;
    import  de.christopherstock.lib.gl.jogl.*;
    import  de.christopherstock.lib.gl.lwjgl.*;

    public class LibGL3D
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

        public      static          LibGLView           view                            = null;
        public      static          LibGLPanel          panel                           = null;
        public      static          LibGLForm           form                            = null;

        /**************************************************************************************
        *   A flag being set to true if the init() method of the glView-singleton has been performed.
        **************************************************************************************/
        public      static          boolean         glPanelInitialized              = false;

        public static final void init
        (
            Engine3D            engine,
            int                 formWidth,
            int                 formHeight,
            String              formTitle,
            boolean             enableLights,
            GLCallbackInit      callbackGLinit,
            GLCallback3D        callback3D,
            GLCallback2D        callback2D,
            GLCallbackForm      callbackForm,
            BufferedImage       iconImage,
            BufferedImage       bgImage,
            KeyListener         kl,
            MouseListener       ml,
            MouseMotionListener mml,
            MouseWheelListener  mwl,
            LibGLImage[]        textureImages,
            LibDebug            debug
        )
        {
            //init gl ui components
            switch ( engine )
            {
                case JOGL:
                {
                    //init 3d engine
                    view = new LibJOGLView
                    (
                        callbackGLinit,
                        callback2D,
                        callback3D,
                        textureImages,
                        debug,
                        formWidth,
                        formHeight,
                        enableLights
                    );

                    //init gl panel
                    panel = new LibJOGLPanel
                    (
                        (LibJOGLView)view,
                        kl,
                        ml,
                        mml,
                        mwl
                    );

                    //init and show form
                    form = new LibGLForm
                    (
                        callbackForm,
                        formTitle,
                        panel.getNativePanel(),
                        formWidth,
                        formHeight,
                        iconImage,
                        bgImage
                    );
                    break;
                }

                case LWJGL:
                {
                    //init panel
                    panel = new LibLWJGLPanel
                    (
                        callback2D,
                        callback3D,
                        bgImage
                    );

                    //show lwjgl form
                    form = new LibGLForm
                    (
                        callbackForm,
                        formTitle,
                        panel.getNativePanel(),
                        formWidth,
                        formHeight,
                        iconImage,
                        bgImage
                    );

                    //init lwjgl view
                    view = new LibLWJGLView
                    (
                        callbackGLinit,
                        panel,
                        textureImages,
                        debug,
                        formWidth,
                        formHeight,
                        enableLights
                    );
                    break;
                }
            }
        }
    }
