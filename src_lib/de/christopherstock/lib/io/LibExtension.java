/*  $Id: LibExtension.java 1276 2014-07-02 19:07:09Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io;

    /**************************************************************************************
    *   All resource extensions that matter.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public enum LibExtension
    {
        /** Portable network graphics. */
        png( ".png" ),

        /** Joint Photographic Experts Group. Compressed For large images */
        jpg( ".jpg" ),

        /** ASCII scene export. */
        ase( ".ase" ),

        /** Wave format. */
        wav( ".wav" ),

        /** MPEG Audio Layer III. */
        mp3( ".mp3" ),

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
