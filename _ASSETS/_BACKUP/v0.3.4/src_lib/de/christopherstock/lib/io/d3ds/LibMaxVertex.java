/*  $Id: LibMaxVertex.java 572 2011-04-16 20:52:37Z jenetic.bytemare $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.lib.io.d3ds;

    class LibMaxVertex
    {
        public float    x   = 0.0f;
        public float    y   = 0.0f;
        public float    z   = 0.0f;
        public float    u   = 0.0f;
        public float    v   = 0.0f;

        /***********************************************************************************
        *   returns a copy!
        *
        *   @param  v   The max-vertex to create a copy from.
        ***********************************************************************************/
        public LibMaxVertex( LibMaxVertex v )
        {
            x = v.x;
            y = v.y;
            z = v.z;
        }

        public LibMaxVertex( float initX, float initY, float initZ )
        {
            x = initX;
            y = initY;
            z = initZ;
        }
    }
