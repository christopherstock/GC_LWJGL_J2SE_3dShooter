/*  $Id: Loader3dsmax.cs,v 1.5 2006/11/17 06:46:15 Christopher Stock Exp $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.shooter.io.d3ds;

    import de.christopherstock.shooter.ui.*;

    public class MaxMaterial
    {
        public      String      name        = null;
        public      Colors      color       = null;
        public      float       offsetU     = 0.0f;
        public      float       offsetV     = 0.0f;
        public      float       tilingU     = 0.0f;
        public      float       tilingV     = 0.0f;

        public MaxMaterial( String aName, Colors aColor, float aOffsetU, float aOffsetV, float aTilingU, float aTilingV )
        {
            name       = aName;
            color      = aColor;
            offsetU    = aOffsetU;
            offsetV    = aOffsetV;
            tilingU    = aTilingU;
            tilingV    = aTilingV;
        }
    }
