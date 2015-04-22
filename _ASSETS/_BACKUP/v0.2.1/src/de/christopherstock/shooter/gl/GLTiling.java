/*  $Id: Texture.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    public enum GLTiling
    {
        ETilingDeci(        0.1f    ),
        ETilingEigth(       0.125f  ),
        ETilingQuarter(     0.25f   ),
        ETilingThird(       0.333f  ),
        ETilingHalf(        0.5f    ),
        ETilingFull(        1.0f    ),
        ETilingDouble(      2.0f    ),
        ETilingTriple(      3.0f    ),
        ETilingQuadruple(   4.0f    ),
        EEightTimes(        8.0f    ),

        /** draw 1 tile per 1.0f of distX by using distX as value */
        ETilingDefaultX(    -1.0f   ),
        /** draw 1 tile per 1.0f of distZ by using distY as value*/
        ETilingDefaultY(    -1.0f   ),
        ;

        public              float                           val             = 0.0f;

        private GLTiling( float tilingValue )
        {
            val = tilingValue;
        }
    }
