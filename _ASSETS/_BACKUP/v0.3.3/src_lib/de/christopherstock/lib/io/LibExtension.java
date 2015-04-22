/*  $Id: LibExtension.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
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
