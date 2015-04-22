/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.GLImage.ImageUsage;

    /**************************************************************************************
    *   The Gadgets / ( non-wearpons ) the player can hold.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum BackGround
    {
        EDesert,
        ;

        public          static          GLImage[]       bgImages                = null;

        public                          boolean         ownedByPlayer               = false;

        private BackGround()
        {
        }

        public static final void loadImages()
        {
            bgImages = GLImage.loadAll( BackGround.values().length, Path.EBackGrounds.url, ".png", ImageUsage.EOrtho );
        }

        public final void drawOrtho( float playerRotX, float playerRotZ )
        {
            GLImage img = bgImages[ ordinal() ];
            int x = (int)( img.width * playerRotZ / 360 );

            int y = 0;

            if ( playerRotX > 0 )
            {
                y = (int)( img.height / 2 * playerRotX / Player.MAX_LOOKING_X );
            }
            else if ( playerRotX < 0 )
            {
                y = (int)( img.height / 2 * playerRotX / Player.MAX_LOOKING_X );
            }

            //Debug.bugfix.out( "x: [" + x + "] rotZ [" + playerRotZ + "] rotY [" + playerRotX + "]" );

            GLView.singleton.drawOrthoBitmapBytes( img, x, GLPanel.glPanel.height - img.height / 2 + y );
            x -= img.width;
            GLView.singleton.drawOrthoBitmapBytes( img, x, GLPanel.glPanel.height - img.height / 2 + y );
        }
    }
