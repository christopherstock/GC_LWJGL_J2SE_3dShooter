/*  $Id: LibMaxMaterial.java 591 2011-04-18 00:58:58Z jenetic.bytemare $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.lib.io.d3ds;

    import  de.christopherstock.lib.ui.*;

    class LibMaxMaterial
    {
        public      String      name        = null;
        public      LibColors   color       = null;
        public      float       offsetU     = 0.0f;
        public      float       offsetV     = 0.0f;
        public      float       tilingU     = 0.0f;
        public      float       tilingV     = 0.0f;

        public LibMaxMaterial( String aName, LibColors aColor, float aOffsetU, float aOffsetV, float aTilingU, float aTilingV )
        {
            name       = aName;
            color      = aColor;
            offsetU    = aOffsetU;
            offsetV    = aOffsetV;
            tilingU    = aTilingU;
            tilingV    = aTilingV;
        }
    }
