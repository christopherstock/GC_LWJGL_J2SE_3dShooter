/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    /**************************************************************************************
    *   All paths the application makes use of.
    *   All references are specified absolute.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public enum Path
    {
        ETextures(  "/res/textures/"        ),
        EAvatars(   "/res/hud/avatars/"     ),
        EWearpons(  "/res/hud/guns/"        ),
        EGadgets(   "/res/hud/gadgets/"     ),
        EScreen(    "/res/screen/"          ),
        ESounds(    "/res/mp3/"             ),
        E3dsMax(    "/res/d3ds/"            ),
        ;

        public                      String          url                 = null;

        private Path( String url )
        {
            this.url = url;
        }
    }
