/*  $Id: Offset.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui;

    import de.christopherstock.shooter.gl.*;

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
            EBorderHudX             = GLPanel.singleton.width  * 5 / 100;
            EBorderHudY             = GLPanel.singleton.height * 5 / 100;
            EHudMsgY                = GLPanel.singleton.height * 6 / 100;
            EAvatarBgPanelHeight    = 100;
            EAvatarImageX           = 0;
            EAvatarTextX            = 10;
        }
    }
