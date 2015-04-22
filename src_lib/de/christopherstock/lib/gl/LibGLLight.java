/*  $Id: LibGLLight.java 1224 2012-12-31 12:32:24Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author         Christopher Stock
    *   @version        0.3.11
    **************************************************************************************/
    public class LibGLLight
    {
        public                  LibVertex               iAnk                = null;
        public                  float                   iRotZ               = 0.0f;
        public                  float                   iSpotCutoff         = 0.0f;
        public                  LibColors               iColDiffuse         = null;

        public LibGLLight( LibVertex aAnk, float aRotZ, float aSpotCutoff, LibColors aColDiffuse )
        {
            iAnk        = aAnk;
            iRotZ       = aRotZ;
            iSpotCutoff = aSpotCutoff;
            iColDiffuse = aColDiffuse;
        }
    }
