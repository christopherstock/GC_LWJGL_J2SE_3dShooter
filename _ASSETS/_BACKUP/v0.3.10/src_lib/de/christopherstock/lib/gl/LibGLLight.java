/*  $Id: LibGLLight.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author         Christopher Stock
    *   @version        0.3.10
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
