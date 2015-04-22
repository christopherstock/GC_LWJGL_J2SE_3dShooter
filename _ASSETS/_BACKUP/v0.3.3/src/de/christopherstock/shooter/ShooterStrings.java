/*  $Id: ShooterStrings.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    /**************************************************************************************
    *   All content strings gather here.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public interface ShooterStrings
    {
        /*********************************************************************************
        *   These characters will break lines.
        *********************************************************************************/
        public static final class HUDMessages
        {
            public  static  final   String      PICKED_UP_BOTTLE            = "Picked up a bottle of soft water";
            public  static  final   String      PICKED_UP_HANDSET           = "Picked up a smartphone";
            public  static  final   String      PICKED_UP_SHOTGUN           = "Picked up a shotgun";
            public  static  final   String      PICKED_UP_PISTOL_9MM        = "Picked up a pistol 9mm";
            public  static  final   String      PICKED_UP_SHOTGUN_SHELLS    = "Picked up a box of shotgun shells";
            public  static  final   String      PICKED_UP_BULLETS_9MM       = "Picked up a box of 9mm bullets";
        };

        public static final class AvatarMessages
        {
            public  static  final   String      HI1                         = "Hi Carter.\nGib mir später nochmal wegen dem Verhandlungsgespräch Bescheid -\nLass uns nochmal alles durchgehen bevor's losgeht okay?";
            public  static  final   String      HI2                         = "Hallo Carter.\nSchön dass Sie mich empfangen können.\n\nMein Name ist Suzy. Ich freue mich,\n Ihnen bei Ihrem Abenteuer bei Seite zu stehen.";
            public  static  final   String      HI3                         = "Moin Carter.\nBring mir doch bitte einen Kaffee mit wenn Du Dir eh schon einen holst. :-p";

            public  static  final   String      TUTORIAL_LOOK_AND_MOVE      = "Use UP and DOWN to walk forewards and backwards.\n\nUse LEFT and RIGHT to turn into all directions.";
            public  static  final   String      TUTORIAL_CYCLE_WEARPONS     = "Use MOUSEWHEEL to cycle through your wearpons and gadgets.\n\nMOUSEWHEEL DOWN cycles forewards.\nMOUSEWHEEL UP cycles backwards.";
            public  static  final   String      TUTORIAL_PERFORM_ACTIONS    = "Use SPACE BAR to performs actions\ne.g. activate doors, push buttons, etc.";
            public  static  final   String      TUTORIAL_CROUCH             = "Use Y key to toggle crouching.";
            public  static  final   String      TUTORIAL_RELOAD             = "Use R key to reload your current equipped wearpon.";
            public  static  final   String      TUTORIAL_FIRE               = "Use STRG to fire or use your current equipped wearpon or gadget.";
            public  static  final   String      TUTORIAL_VIEW_UP_DOWN       = "Use 8 and 2 on the numeric keypad to look up and down.\n\nUse 4 and 6 on the numeric keypad to turn in all direction precisely.";
        }
    }
