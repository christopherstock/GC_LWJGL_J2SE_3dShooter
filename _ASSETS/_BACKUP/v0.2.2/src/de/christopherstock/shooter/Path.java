/*  $Id: Path.java 181 2010-11-13 13:31:37Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    /**************************************************************************************
    *   All paths the application makes use of.
    *   All references are specified absolute.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum Path
    {
        EBackGrounds(   "/res/bgs/"             ),
        ETextures(      "/res/textures/"        ),
        EAvatars(       "/res/hud/avatars/"     ),
        EWearpons(      "/res/hud/guns/"        ),
        EGadgets(       "/res/hud/gadgets/"     ),
        EScreen(        "/res/screen/"          ),
        ESounds(        "/res/mp3/"             ),
        E3dsMax(        "/res/d3ds/"            ),
        ;

        public                      String          url                 = null;

        private Path( String aUrl )
        {
            url = aUrl;

            //assert leading and trailing slash
            if ( !url.startsWith( "/" ) ) url = "/" + url;
            if ( !url.endsWith(   "/" ) ) url = url + "/";
        }
    }
