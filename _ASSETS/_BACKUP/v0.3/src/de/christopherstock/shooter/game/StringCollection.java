/*  $Id: StringCollection.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    /**************************************************************************************
    *   All content strings gather here.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public interface StringCollection
    {
        /*********************************************************************************
        *   These characters will break lines.
        *********************************************************************************/
        public static final class HUDMessages
        {
            public  static  final   String      PICKED_UP_BOTTLE            = "Picked up a bottle of soft water";
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

            public  static  final   String      TUTORIAL_LOOK_AND_MOVE      = "Use the UP and DOWN to walk forewards or backwards.\n\nUse LEFT and RIGHT to turn into all directions.";
            public  static  final   String      TUTORIAL_CYCLE_WEARPONS     = "Use the MOUSEWHEEL to cycle through your wearpons and gadgets.\n\nMOUSEWHEEL DOWN cycles forewards.\nMOUSEWHEEL UP cycles backwards.";
            public  static  final   String      TUTORIAL_PERFORM_ACTIONS    = "Use SPACE BAR to performs actions\ne.g. activate doors, push buttons, etc.";
        }
    }
