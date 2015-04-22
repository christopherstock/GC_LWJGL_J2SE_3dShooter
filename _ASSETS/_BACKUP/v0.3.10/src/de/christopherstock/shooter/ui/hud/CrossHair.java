/*  $Id: CrossHair.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   Represents a pop-up-message with an avatar image and a message.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public enum CrossHair
    {
        ECircle,
        EDefault,
        EPrecise,
        ESmallest,
        ;

        public          LibGLImage          img         = null;

        public static final void loadImages()
        {
            for ( CrossHair crossHairImage : values() )
            {
                crossHairImage.loadImage();
            }
        }

        private final void loadImage()
        {
            img = new LibGLImage( LibImage.load( ShooterSettings.Path.ECrossHair.iUrl + toString() + LibExtension.png.getSpecifier(), ShooterDebug.glimage, false ), ImageUsage.EOrtho, ShooterDebug.glimage, true );
        }
    }
