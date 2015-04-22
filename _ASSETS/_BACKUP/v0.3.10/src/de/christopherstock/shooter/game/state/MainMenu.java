/*  $Id: MainMenu.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.state;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;
    import  de.christopherstock.shooter.base.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class MainMenu
    {
        public enum MainMenuItem
        {
            itemStartGame   ( "START NEW GAME"  ),
            itemContinueGame( "CONTINUE GAME"   ),
            itemLoadGame    ( "LOAD GAME"       ),
            itemQuitGame    ( "QUIT GAME"       ),
            itemCredits     ( "CREDITS"         ),
            ;

            protected   LibGLImage  US                      = null;
            protected   LibGLImage  S                       = null;

            private MainMenuItem( String label )
            {
                US = LibGLImage.getFromString( label,   Fonts.EMainMenu, LibColors.ERedDark.colABGR,    null, null, ShooterDebug.glimage );
                S  = LibGLImage.getFromString( label,   Fonts.EMainMenu, LibColors.EWhite.colABGR,      null, null, ShooterDebug.glimage );
            }

            public void draw( int x, int y, MainMenuItem selected )
            {
                LibGL3D.view.drawOrthoBitmapBytes( ( this == selected ? S : US ), x, y, 1.0f );
            }
        }

        private         static          MainMenu                singleton                       = null;

        private         static          MainMenuItem            currentMainMenuItem             = null;

        private                         LibGLImage              blackPane                       = null;

        private                         int                     menuChangeBlocker               = 0;

        private MainMenu()
        {
            blackPane = LibGLImage.getFullOpaque( LibColors.EBlack.colABGR, LibGL3D.panel.width, LibGL3D.panel.height, ShooterDebug.glimage );

        }

        public static final void init()
        {
            getSingleton();
            currentMainMenuItem = MainMenuItem.itemStartGame;
        }

        public final void draw2D()
        {
            //draw hud
            Shooter.mainThread.iHUD.draw2D();

            //draw black pane ( create if not available )
            if ( blackPane == null )
            {

            }

            LibGL3D.view.drawOrthoBitmapBytes( blackPane,               0,   0,   0.5f );

            for ( MainMenuItem m : MainMenuItem.values() )
            {
                m.draw( 100, 600 - m.ordinal() * 100, currentMainMenuItem );
            }
        }

        public final void previousItem()
        {
            if ( menuChangeBlocker == 0 )
            {
                if ( currentMainMenuItem.ordinal() > 0 )
                {
                    currentMainMenuItem = MainMenuItem.values()[ currentMainMenuItem.ordinal() - 1 ];
                    menuChangeBlocker = 6;
                    ShooterSound.ELocked1.playGlobalFx();
                }
            }
        }

        public final void nextItem()
        {
            if ( menuChangeBlocker == 0 )
            {
                if ( currentMainMenuItem.ordinal() < MainMenuItem.values().length - 1 )
                {
                    currentMainMenuItem = MainMenuItem.values()[ currentMainMenuItem.ordinal() + 1 ];
                    menuChangeBlocker = 6;
                    ShooterSound.ELocked1.playGlobalFx();
                }
            }
        }

        public final void onRun()
        {
            if ( menuChangeBlocker > 0 ) --menuChangeBlocker;
        }

        public static final MainMenu getSingleton()
        {
            if ( singleton == null )
            {
                singleton = new MainMenu();
            }

            return singleton;
        }
    }
