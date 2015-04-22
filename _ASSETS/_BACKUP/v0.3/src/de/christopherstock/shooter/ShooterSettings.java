/*  $Id: ShooterSettings.java 272 2011-02-11 20:01:03Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  java.awt.*;
    import  de.christopherstock.lib.gl.LibGL3D.*;
    import  de.christopherstock.lib.ui.*;

    /**********************************************************************************
    *   All settings for the project.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    ***********************************************************************************/
    public class ShooterSettings
    {
        /************************************************************************************
        *   The 3D engine of choice. ( LWJGL is recommended )
        ************************************************************************************/
        public      static  final   Engine3D    ENGINE_3D                           = Engine3D.LWJGL;

        /************************************************************************************
        *   Saying NO is definetely equivalent to the boolean <code>false</code>.
        ************************************************************************************/
        public      static  final   boolean     NO                                  = false;
        /************************************************************************************
        *   Saying YES is definetely equivalent to the boolean <code>true</code>.
        ************************************************************************************/
        public      static  final   boolean     YES                                 = true;

        public static final class HUD
        {
            public  static  final   boolean     SHOW_MAGAZINE_SIZE                  = true;

            public  static  final   int         MAX_OPACITY_HEALTH_FX               = 255; //64
            public  static  final   int         MAX_OPACITY_DAMAGE_FX               = 255; //200

            public  static  final   float       LINE_SPACING_RATIO_EMPTY_LINES      = 0.5f;
        }

        public static final class Form
        {
            /** The window's title string. */
            public  static  final   String      FORM_TITLE                          = "Shooter Prototype, " + ShooterVersion.getCurrentVersionDesc();
            public  static  final   int         FORM_WIDTH                          = 640;
            public  static  final   int         FORM_HEIGHT                         = 480;
        }

        // --- debug defines --- //
        public      static  final   boolean     DISABLE_COLLISIONS                  = ShooterSettings.NO;
        public      static  final   boolean     DISABLE_GRAVITY                     = ShooterSettings.YES;
        public      static  final   boolean     DISABLE_LIGHTING                    = ShooterSettings.YES;
        public      static  final   boolean     INVISIBLE_Z_ZERO_LAYER              = ShooterSettings.YES;
        public      static  final   boolean     DISABLE_LOOK_CENTERING_X            = ShooterSettings.YES;

        public      static  final   boolean     DISABLE_SOUND_BG                    = ShooterSettings.YES;
        public      static  final   boolean     DISABLE_SOUND_FX                    = ShooterSettings.NO;


        public static final class DebugSettings
        {
            public      static  final   boolean     DEBUG_MODE                      = ShooterSettings.YES;

            public      static  final   boolean     DEBUG_DRAW_ITEM_CIRCLE          = ShooterSettings.YES;
            public      static  final   boolean     DEBUG_DRAW_BOT_CIRCLES          = ShooterSettings.NO;
            public      static  final   boolean     DEBUG_SHOW_PLAYER_CIRCLE        = ShooterSettings.YES;
            public      static  final   boolean     DEBUG_SHOW_FPS                  = ShooterSettings.YES;
        }

        public      static  final   boolean     ENABLE_3RD_PERSON_CAMERA            = ShooterSettings.NO;

        public static final class Performance
        {
            // --- performance settings --- //
            public      static  final   int         COLLISION_CHECKING_STEPS            = 5;
            public      static  final   int         MAX_NUMBER_BULLET_HOLES             = 256;
            public      static  final   int         ELLIPSE_SEGMENTS                    = 45;

            // --- speed settings --- //
            public      static  final   int         TICKS_WEARPON_HIDE_SHOW             = 4;
            public      static  final   int         TICKS_HEALTH_FX                     = 10;
            public      static  final   int         TICKS_DAMAGE_FX                     = 10;
            public      static  final   long        DELAY_AFTER_PLAYER_ACTION           = 500;              //millis delay after player action
            public      static  final   long        DELAY_AFTER_LEVEL_CHANGE            = 500;              //millis delay after level change
        }

        public static final class Fonts
        {
            public  static  final   Font                EAmmo           = new Font( "verdana",     Font.BOLD,  12 );
            public  static  final   Font                EHealth         = new Font( "verdana",     Font.BOLD,  12 );
            public  static  final   Font                EFps            = new Font( "verdana",     Font.BOLD,  12 );
            public  static  final   Font                EAvatarMessage  = new Font( "verdana",     Font.BOLD,  12 );
            public  static  final   Font                EDebug          = new Font( "courier new", Font.PLAIN, 10 );
        }

        public static final class Offset
        {
            public      static          int     EBorderHudX                         = 0;
            public      static          int     EBorderHudY                         = 0;
            public      static          int     EHudMsgY                            = 0;
            public      static          int     EAvatarBgPanelHeight                = 0;
            public      static          int     EAvatarImageX                       = 0;
            public      static          int     EAvatarImageY                       = 0;
            public      static          int     EAvatarTextX                        = 0;

            public static final void parseOffsets( int panelWidth, int panelHeight )
            {
                EBorderHudX             = panelWidth  * 5 / 100;
                EBorderHudY             = panelHeight * 5 / 100;
                EHudMsgY                = panelHeight * 6 / 100;
                EAvatarBgPanelHeight    = 96;
                EAvatarImageX           = 10;
                EAvatarImageY           = 10;
                EAvatarTextX            = 10;
            }
        }

        public static final class AvatarMessages
        {
            public      static  final   int         OPACITY_PANEL_BG                    = 255;

            public      static  final   int         ANIM_TICKS_POP_UP                   = 5;
            public      static  final   int         ANIM_TICKS_STILL                    = 30;
            public      static  final   int         ANIM_TICKS_POP_DOWN                 = 5;
        }

        public static final class Items
        {
            public      static  final   float       AMMO_RADIUS                         = 0.3f;
            public      static  final   float       WEARPON_RADIUS                      = 0.3f;
            public      static  final   float       GADGET_RADIUS                       = 0.3f;
        }

        /**************************************************************************************
        *   Holding all of player's constant attributes.
        *
        *   @author     Christopher Stock
        *   @version    0.3
        **************************************************************************************/
        public static interface PlayerAttributes
        {
            //basics
            static  final   float       RADIUS                      = 0.4f;             //player's radius

            //depths
            static  final   float       DEPTH_TOTAL_STANDING        = 1.3f;             //for collisions
            static  final   float       DEPTH_TOTAL_CROUCHING       = 0.9f;             //for collisions
            static  final   float       DEPTH_EYE_STANDING          = 1.2f;             //player's eye-distance from floor
            static  final   float       DEPTH_EYE_CROUCHING         = 0.8f;             //player's eye-distance from floor
            static  final   float       DEPTH_HAND_STANDING         = 1.0f;             //player's hand-distance from floor
            static  final   float       DEPTH_HAND_CROUCHING        = 0.6f;             //player's hand-distance from floor
            static  final   float       DEPTH_TOE                   = 0.01f;            //player's tow distance from the currently lowest floor
            static  final   float       DEPTH_DEATH                 = 0.08f;            //player's view height on being dead and a glorious

            //speed
            static  final   float       SPEED_WALKING               = 0.3f;             //walking speed
            static  final   float       SPEED_STRAFING              = 0.2f;             //strafing speed
            static  final   float       SPEED_CROUCH_TOGGLE         = 0.15f;            //strafing speed
            static  final   float       SPEED_FALLING_MIN           = 0.075f;           //falling down on z axis per tick
            static  final   float       SPEED_FALLING_MAX           = 0.3f;             //falling down on z axis per tick
            static  final   float       SPEED_FALLING_MULTIPLIER    = 1.25f;            //falling speed multiplier per tick
            static  final   float       SPEED_TURNING_Z             = 7.5f;             //turning speed in ° (left/right)
            static  final   float       SPEED_TURNING_PRECISE_Z     = 2.5f;             //turning precise in ° (left/right)
            static  final   float       SPEED_LOOKING_X             = 5.0f; //1.25f;    //looking speed in ° (up/down)
            static  final   float       SPEED_CENTERING_X           = 10.0f;            //centering speed in ° back to 0°
            static  final   float       SPEED_DYING_SINKING         = 0.1f;             //rotating speed in ° x y z on dying

            //health
            static  final   float       MAX_HEALTH                  = 100.0f;           //maximum health value

            static  final   float       ROTATION_DYING              = 4.5f;             //rotating speed in ° x y z on dying

            static  final   float       MAX_LOOKING_X               = 45.0f;            //22.5f;    //max. look up/down in °
            static  final   float       AMP_WALKING_Z               = 0.05f;            //0.05f;    //z-amplitude ratio
            static  final   float       MAX_CLIMBING_UP_Z           = 0.5f;             //max auto climbing up on faces z

            static  final   float       SPEED_WALKING_ANGLE_1       = 15.0f;            //y-modification per walking-step in °
            static  final   float       SPEED_WALKING_ANGLE_2       = 30.0f;            //y-modification per walking-step in °
            static  final   float       SPEED_WALKING_ANGLE_3       = 15.0f;            //y-modification per walking-step in °
        }

        public static class Colors
        {
            public  static  final   LibColors   EAvatarMessageText          = LibColors.EWhite;
            public  static  final   LibColors   EAvatarMessageTextOutline   = LibColors.EGreyDark;
            public  static  final   LibColors   EFpsFg                      = LibColors.EWhite;
            public  static  final   LibColors   EFpsOutline                 = LibColors.EGreyDark;
            public  static  final   LibColors   EAmmoFg                     = LibColors.EWhite;
            public  static  final   LibColors   EAmmoOutline                = LibColors.EGreyDark;
            public  static  final   LibColors   EAvatarMessagePanelBg       = LibColors.EGreyLight;
            public  static  final   LibColors   EHudMsgFg                   = LibColors.EWhite;
            public  static  final   LibColors   EHudMsgOutline              = LibColors.EBlack;
/*
            public  static  final   LibColors   EAmmoFg                     = LibColors.EWhite;
            public  static  final   LibColors   EAmmoOutline                = LibColors.EBlack;
            public  static  final   LibColors   EHealthFg                   = LibColors.EWhite;
            public  static  final   LibColors   EHealthOutline              = LibColors.EBlack;
*/
        }

        /**************************************************************************************
        *   All paths the application makes use of.
        *   All references are specified absolute.
        *
        *   @author     Christopher Stock
        *   @version    0.3
        **************************************************************************************/
        public enum Path
        {
            EBackGrounds(   "/res/bgs/"             ),
            ETextures(      "/res/textures/"        ),
            EAvatars(       "/res/hud/avatars/"     ),
            EWearpons(      "/res/hud/guns/"        ),
            EGadgets(       "/res/hud/gadgets/"     ),
            EScreen(        "/res/screen/"          ),
            ESounds(        "/res/sounds/"          ),
            E3dsMax(        "/res/d3ds/"            ),
            ;

            public                      String          url                 = null;

            private Path( String aUrl )
            {
                url = aUrl;

                if ( !url.startsWith( "/" ) ) url = "/" + url;  //assert leading slash
                if ( !url.endsWith(   "/" ) ) url = url + "/";  //assert trailing slash
            }
        }
    }
