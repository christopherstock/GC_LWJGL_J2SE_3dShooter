/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui;

    import  java.awt.*;

import de.christopherstock.lib.math.*;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum Colors
    {
        // --- palette --- //
        EBlack(                         0x000000    ),

        ERed(                           0xff0000    ),
        ERedDark(                       0x662222    ),
        EGreen(                         0x00ff00    ),
        EGreenLight(                    0x55ff55    ),
        EBlue(                          0x0000ff    ),
        EBlueLight(                     0x98c4f9    ),
        EBlueDark(                      0x000099    ),

        ESkin(                          0xf2cec5    ),

        EOrange(                        0xffb300    ),
        EBrown(                         0xc55d11    ),
        EPink(                          0xff5555    ),
        EYellow(                        0xffff00    ),
        EGrey(                          0x909090    ),
        EGreyLight(                     0xbbbbbb    ),
        EGreyDark(                      0x444444    ),

        EWhite(                         0xffffff    ),

        // --- HUD elements --- //
        EAvatarMessageText(             EWhite      ),
        EAvatarMessageTextOutline(      EBlack      ),
        EAvatarMessagePanelBg(          EGreyLight  ),
        EHudMsgFg(                      EWhite      ),
        EHudMsgOutline(                 EBlack      ),
        EAmmoFg(                        EWhite      ),
        EAmmoOutline(                   EBlack      ),
        EHealthFg(                      EWhite      ),
        EHealthOutline(                 EBlack      ),
        EFpsFg(                         EWhite      ),
        EFpsOutline(                    EBlack      ),

        ;

        public              int         rgb             = 0;
        public              float[]     f3              = null;
        public              Color       col             = null;

        private Colors( Colors col )
        {
            this( col.rgb );
        }

        private Colors( int aRgb )
        {
            rgb =               aRgb;
            f3  = LibMath.col2f3( aRgb );
            col = new Color(    aRgb );
        }

        public static final Colors getByName( String name )
        {
            if ( name == null ) return null;

            for ( Colors col : values() )
            {
                if ( col.name().compareToIgnoreCase( name ) == 0 )
                {
                    return col;
                }
            }

            return null;

        }
    }