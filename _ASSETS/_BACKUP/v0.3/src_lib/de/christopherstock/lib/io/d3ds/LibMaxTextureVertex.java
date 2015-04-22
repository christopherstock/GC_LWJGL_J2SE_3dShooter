/*  $Id: MaxTextureVertex.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.lib.io.d3ds;

    class LibMaxTextureVertex
    {
        public float    u   = 0;
        public float    v   = 0;

        public LibMaxTextureVertex( float initU, float initV )
        {
            u = initU;
            v = initV;
        }
    }
