/*  $Id: MaxFace.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.shooter.d3ds;

    class MaxFace
    {
        public  MaxVertex   iFaceNormal     = null;
        public  MaxVertex   vertex1         = null;
        public  MaxVertex   vertex2         = null;
        public  MaxVertex   vertex3         = null;

        public MaxFace( MaxVertex aFaceNormal, MaxVertex initVertex1, MaxVertex initVertex2, MaxVertex initVertex3 )
        {
            iFaceNormal = aFaceNormal;
            vertex1     = new MaxVertex( initVertex1 );
            vertex2     = new MaxVertex( initVertex2 );
            vertex3     = new MaxVertex( initVertex3 );
        }
    }
