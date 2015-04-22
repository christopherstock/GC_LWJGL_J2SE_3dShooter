/*  $Id: LibMaxFace.java 572 2011-04-16 20:52:37Z jenetic.bytemare $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.lib.io.d3ds;

    class LibMaxFace
    {
        public  LibMaxVertex   iFaceNormal     = null;
        public  LibMaxVertex   vertex1         = null;
        public  LibMaxVertex   vertex2         = null;
        public  LibMaxVertex   vertex3         = null;

        public LibMaxFace( LibMaxVertex aFaceNormal, LibMaxVertex initVertex1, LibMaxVertex initVertex2, LibMaxVertex initVertex3 )
        {
            iFaceNormal = aFaceNormal;
            vertex1     = new LibMaxVertex( initVertex1 );
            vertex2     = new LibMaxVertex( initVertex2 );
            vertex3     = new LibMaxVertex( initVertex3 );
        }
    }
