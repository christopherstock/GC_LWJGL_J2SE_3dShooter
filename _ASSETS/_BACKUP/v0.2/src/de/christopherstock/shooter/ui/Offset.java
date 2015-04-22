/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui;

    import de.christopherstock.shooter.gl.opengl.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public final class Offset
    {
        public      static          int     EBorderHudX                         = 0;
        public      static          int     EBorderHudY                         = 0;
        public      static          int     EHudMsgY                            = 0;
        public      static          int     EAvatarBgPanelHeight                = 0;
        public      static          int     EAvatarImageX                       = 0;
        public      static          int     EAvatarTextX                        = 0;

        public static final void parseOffsets()
        {
            EBorderHudX             = GLPanel.PANEL_WIDTH  * 5 / 100;
            EBorderHudY             = GLPanel.PANEL_HEIGHT * 5 / 100;
            EHudMsgY                = GLPanel.PANEL_HEIGHT * 6 / 100;
            EAvatarBgPanelHeight    = 100;
            EAvatarImageX           = 0;
            EAvatarTextX            = 10;
        }
    }
