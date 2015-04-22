/*  $Id: BackGround.java 628 2011-04-22 22:16:11Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

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
    *   @version    0.3.3
    **************************************************************************************/
    public enum BackGround
    {
        ECity,
        EMeadow,
        EQatar,

        //EDispatch,
        //ENight1,
        ;

        public                          LibGLImage      bgImage                     = null;
        public                          boolean         ownedByPlayer               = false;

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
