/*  $Id: Preloader.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.state;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;

    /**************************************************************************************
    *   The application's main thread. Start this thread to run the application.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class Preloader
    {
        public      static          Preloader       singleton                   = null;

        public                      int             preloaderTest               = 0;
        public                      String          preloaderMsg                = null;
        public                      LibGLImage      bgImage                     = null;
        public                      LibGLImage      preloaderImage              = null;

        private Preloader()
        {
            //singleton constructor
        }

        public static final Preloader getSingleton()
        {
            if ( singleton == null ) singleton = new Preloader();
            return singleton;
        }

        public final void draw2D()
        {
            try
            {
                //create preloader image if not done yet
                LibGL3D.view.drawOrthoBitmapBytes( bgImage, 0, 0 );

                LibGL3D.view.drawOrthoBitmapBytes( preloaderImage, LibGL3D.panel.width / 2 - preloaderImage.width / 2,                       LibGL3D.panel.height / 2 + preloaderImage.height / 2 + 50 );
                LibGL3D.view.drawOrthoBitmapBytes( preloaderImage, LibGL3D.panel.width / 2 - preloaderImage.width / 2 - 100 + preloaderTest, LibGL3D.panel.height / 2 + preloaderImage.height / 2      );

                LibGLImage text = LibGLImage.getFromString( preloaderMsg + " [ " + preloaderTest + " / 100 ]", Fonts.EAmmo, LibColors.EBlack.colABGR, null, LibColors.EGrey.colABGR, ShooterDebug.glimage );
                LibGL3D.view.drawOrthoBitmapBytes( text, LibGL3D.panel.width / 2, LibGL3D.panel.height / 4 + text.height / 2 );
            }
            catch ( Exception e )
            {
                ShooterDebug.error.trace( e );
            }
        }

        public final void draw3D()
        {
            LibGL3D.view.clearGl( LibColors.EBlack );
        }

        public final void increase( String msg )
        {
            ShooterDebug.init.out( "preloader increase [" + msg + "]" );

            preloaderMsg = msg;
            preloaderTest += 20;

            ShooterDebug.init.out( "display ?" );
            LibGL3D.panel.display();
            ShooterDebug.init.out( "display ? Ok" );
        }
    }
