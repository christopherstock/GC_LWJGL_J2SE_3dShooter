/*  $Id: MainMenu.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
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
    *   @version    0.3.3
    **************************************************************************************/
    public class MainMenu
    {
        public          static          MainMenu                singleton                       = null;

        private                         LibGLImage              blackPane                       = null;

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

        public static final void init()
        {
            //load all images
            singleton = new MainMenu();
        }
    }