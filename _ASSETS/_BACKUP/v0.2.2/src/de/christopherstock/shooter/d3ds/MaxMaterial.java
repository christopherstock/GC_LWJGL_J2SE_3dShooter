/*  $Id: MaxMaterial.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.shooter.d3ds;

    import de.christopherstock.lib.ui.*;

    class MaxMaterial
    {
        public      String      name        = null;
        public      LibColors   color       = null;
        public      float       offsetU     = 0.0f;
        public      float       offsetV     = 0.0f;
        public      float       tilingU     = 0.0f;
        public      float       tilingV     = 0.0f;

        public MaxMaterial( String aName, LibColors aColor, float aOffsetU, float aOffsetV, float aTilingU, float aTilingV )
        {
            name       = aName;
            color      = aColor;
            offsetU    = aOffsetU;
            offsetV    = aOffsetV;
            tilingU    = aTilingU;
            tilingV    = aTilingV;
        }
    }
