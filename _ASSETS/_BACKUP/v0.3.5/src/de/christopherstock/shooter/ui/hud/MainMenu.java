/*  $Id: MainMenu.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class MainMenu
    {
        private         static          MainMenu                singleton                       = null;

        private                         LibGLImage              blackPane                       = null;

        private MainMenu()
        {
        }

        public final void draw2D()
        {
            //draw frames per second last
            Fps.draw();

            //draw black pane ( create if not available )
            if ( blackPane == null )
            {
                blackPane = LibGLImage.getFullOpaque( LibColors.EBlack.colABGR, LibGL3D.panel.width, LibGL3D.panel.height, ShooterDebug.glimage );
            }

            LibGL3D.view.drawOrthoBitmapBytes( blackPane, 0, 0, 0.5f );
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
