/*  $Id: HUD.java 231 2011-01-27 17:10:53Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.ui;

    import  java.awt.*;
    import  de.christopherstock.lib.math.*;

    /**************************************************************************************
    *   Offeres predefined colors.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public enum LibColors
    {
        EBlack(                         0xff000000      ),
        ERed(                           0xffff0000      ),
        ERedDark(                       0xff662222      ),
        EGreen(                         0xff00ff00      ),
        EGreenLight(                    0xff55ff55      ),
        EBlue(                          0xff0000ff      ),
        EBlueLight(                     0xff98c4f9      ),
        EBlueDark(                      0xff000099      ),
        EWhite(                         0xffffffff      ),

        ESkin(                          0xfff2cec5      ),
        EOrange(                        0xffffb300      ),
        EBrown(                         0xffc55d11      ),
        EPink(                          0xffff5555      ),
        EYellow(                        0xffffff00      ),
        EGrey(                          0xff909090      ),
        EGreyLight(                     0xffbbbbbb      ),
        EGreyDark(                      0xff444444      ),

        ;

        public              int         rgb             = 0;
        public              float[]     f3              = null;
        public              Color       colARGB         = null;
        public              Color       colABGR         = null;

        private LibColors( int aArgb )
        {
            rgb     =                   aArgb;
            f3      = LibMath.col2f3(   aArgb       );
            colARGB = new Color(        aArgb, true );
            colABGR = new Color( colARGB.getBlue(), colARGB.getGreen(), colARGB.getRed(), colARGB.getAlpha() );
        }

        public static final LibColors getByName( String name )
        {
            if ( name == null ) return null;

            for ( LibColors col : values() )
            {
                if ( col.name().compareToIgnoreCase( name ) == 0 )
                {
                    return col;
                }
            }

            return null;
        }
    }
