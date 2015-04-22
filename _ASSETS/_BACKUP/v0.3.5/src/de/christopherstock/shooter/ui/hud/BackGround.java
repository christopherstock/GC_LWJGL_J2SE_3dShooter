/*  $Id: BackGround.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.image.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;

    /**************************************************************************************
    *   The Gadgets / ( non-wearpons ) the player can hold.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public enum BackGround
    {
        EQatar_hideout,

        ;

        public                          LibGLImage      bgImage                     = null;

        private BackGround()
        {
        }

        public static final void loadImages()
        {
            for ( BackGround bg : values() )
            {
                bg.loadImage();
            }
        }

        public final void loadImage()
        {
            BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.EBackGrounds.iUrl + this.toString() + LibExtension.jpg.getSpecifier(), ShooterDebug.glimage, false );
            bgImage = new LibGLImage( bufferedImage, ImageUsage.EOrtho, ShooterDebug.glimage, true );
        }

        public final void drawOrtho( float playerRotX, float playerRotZ )
        {
            //translate left/right
            int x = (int)( bgImage.width * playerRotZ / 360 );

            //translate up/down
            int y = -bgImage.height / 4;
            if ( playerRotX > 0 )
            {
                y += (int)( bgImage.height / 4 * playerRotX / PlayerAttributes.MAX_LOOKING_X );
            }
            else if ( playerRotX < 0 )
            {
                y += (int)( bgImage.height / 4 * playerRotX / PlayerAttributes.MAX_LOOKING_X );
            }

            //Debug.bugfix.out( "x: [" + x + "] rotZ [" + playerRotZ + "] rotY [" + playerRotX + "]" );

            LibGL3D.view.drawOrthoBitmapBytes( bgImage, x, LibGL3D.panel.height - bgImage.height / 2 + y );
            x -= bgImage.width;
            LibGL3D.view.drawOrthoBitmapBytes( bgImage, x, LibGL3D.panel.height - bgImage.height / 2 + y );
        }
    }
