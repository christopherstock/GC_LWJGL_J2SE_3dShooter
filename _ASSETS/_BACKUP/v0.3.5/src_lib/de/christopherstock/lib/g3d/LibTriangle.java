/*  $Id: LibTriangle.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.g3d;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
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
