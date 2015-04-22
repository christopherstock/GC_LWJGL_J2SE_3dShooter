/*  $Id: LibExtension.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
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
