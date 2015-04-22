/*  $Id: Loader3dsmax.cs,v 1.5 2006/11/17 06:46:15 Christopher Stock Exp $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.shooter.io.d3ds;

    public class MaxVertex
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
        public MaxVertex( MaxVertex v )
        {
            x = v.x;
            y = v.y;
            z = v.z;
        }

        public MaxVertex( float initX, float initY, float initZ )
        {
            x = initX;
            y = initY;
            z = initZ;
        }
    }
