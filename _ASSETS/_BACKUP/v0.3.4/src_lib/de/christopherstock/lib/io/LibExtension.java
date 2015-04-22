/*  $Id: LibExtension.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public enum LibExtension
    {
        png( ".png" ),
        jpg( ".jpg" ),
        ase( ".ase" ),
        wav( ".wav" ),

        ;

        private                     String      iSpecifier          = null;

        private LibExtension( String aSpecifier )
        {
            iSpecifier = aSpecifier;
        }

        public String getSpecifier()
        {
            return iSpecifier;
        }
    }
