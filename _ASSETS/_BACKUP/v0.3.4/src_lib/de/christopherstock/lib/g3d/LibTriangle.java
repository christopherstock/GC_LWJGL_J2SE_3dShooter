/*  $Id: LibLine3D.java 572 2011-04-16 20:52:37Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.g3d;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class LibTriangle
    {
        public                  LibVertex              a                    = null;
        public                  LibVertex              b                    = null;
        public                  LibVertex              c                    = null;

        public LibTriangle( LibVertex aA, LibVertex aB, LibVertex aC )
        {
            a   = aA;
            b   = aB;
            c   = aC;
        }
    }
