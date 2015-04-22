/*  $Id: LibGL3D.java 1287 2014-10-11 17:10:14Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.awt.image.*;
    import  org.lwjgl.opengl.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.gl.LibGLFrame.GLCallbackForm;
    import  de.christopherstock.lib.gl.LibGLPanel.*;
    import  de.christopherstock.lib.gl.lwjgl.*;

    public class LibGL3D
    {
        public      static          LibGLView           view                            = null;
        public      static          LibGLPanel          panel                           = null;
        public      static          LibGLForm           form                            = null;

        /**************************************************************************************
        *   A flag being set to true if the init() method of the glView-singleton has been performed.
        **************************************************************************************/
        public      static          boolean         glPanelInitialized              = false;

        public static final void init
        (
            int                 formWidth,
            int                 formHeight,
            String              formTitle,
            GLDrawCallback      drawCallback,
            GLCallbackForm      callbackForm,
            BufferedImage       iconImage,
            BufferedImage       bgImage,
            LibDebug            debug
        )
        {
            //init gl ui components

            //init panel
            panel = new LibLWJGLPanel
            (
                drawCallback,
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
                panel,
                debug,
                formWidth,
                formHeight,
                (float)formWidth / (float)formHeight
            );
        }

        public static final void destroy()
        {
            //destroy the display
            Display.destroy();
            System.exit( 0 );
        }
    }
