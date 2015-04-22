/*  $Id: BackGround.java 193 2010-12-13 22:29:14Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.util.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
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
        EMeadow,
        EDispatch,
        ;

        public          static          GLImage[]       bgImages                = null;

        public                          boolean         ownedByPlayer               = false;

        private BackGround()
        {
        }

        public static final void loadImages()
        {
            //bgImages = GLImage.loadAll( BackGround.values().length, Path.EBackGrounds.url, ".png", ImageUsage.EOrtho );
            bgImages = GLImage.loadAllEnumNames( new Vector<Object>( Arrays.asList( BackGround.values() ) ), Path.EBackGrounds.url, ".png", ImageUsage.EOrtho );
        }

        public final void drawOrtho( float playerRotX, float playerRotZ )
        {
            GLImage img = bgImages[ ordinal() ];

            //translate left/right
            int x = (int)( img.width * playerRotZ / 360 );

            //translate up/down
            int y = -img.height / 4;
            if ( playerRotX > 0 )
            {
                y += (int)( img.height / 4 * playerRotX / PlayerAttributes.MAX_LOOKING_X );
            }
            else if ( playerRotX < 0 )
            {
                y += (int)( img.height / 4 * playerRotX / PlayerAttributes.MAX_LOOKING_X );
            }

            //Debug.bugfix.out( "x: [" + x + "] rotZ [" + playerRotZ + "] rotY [" + playerRotX + "]" );

            GLView.singleton.drawOrthoBitmapBytes( img, x, GLPanel.singleton.height - img.height / 2 + y );
            x -= img.width;
            GLView.singleton.drawOrthoBitmapBytes( img, x, GLPanel.singleton.height - img.height / 2 + y );
        }
    }
