/*  $Id: ShooterSettings.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
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
    *   @version    0.3.2
    ***********************************************************************************/
    public class ShooterSettings
    {
        /************************************************************************************
        *   Saying NO is definetely equivalent to the boolean <code>false</code>.
        ************************************************************************************/
        public      static  final   boolean     NO                                  = false;
        /************************************************************************************
        *   Saying YES is definetely equivalent to the boolean <code>true</code>.
        ************************************************************************************/
        public      static  final   boolean     YES                                 = true;

        public static final class Form
        {
            /** The window's title string. */
            public  static  final   String      FORM_TITLE                          = "Shooter Prototype, " + ShooterVersion.getCurrentVersionDesc();
            public  static  final   int         FORM_WIDTH                          = 800;
            public  static  final   int         FORM_HEIGHT                         = 600;
        }

        public static final class HUD
        {
            public  static  final   boolean     SHOW_MAGAZINE_SIZE                  = true;

            public  static  final   float       MAX_OPACITY_HEALTH_FX               = 1.0f;
            public  static  final   float       MAX_OPACITY_DAMAGE_FX               = 1.0f;

            public  static  final   float       LINE_SPACING_RATIO_EMPTY_LINES      = 0.5f;
        }

        public static final class General
        {
            /************************************************************************************
            *   The 3D engine of choice. ( LWJGL is strongly recommended )
            ************************************************************************************/
            public      static  final   Engine3D    ENGINE_3D                           = Engine3D.LWJGL;

            // --- debug defines --- //
            public      static  final   boolean     DISABLE_PLAYER_TO_WALL_COLLISIONS   = ShooterSettings.NO;
            public      static  final   boolean     DISABLE_PLAYER_TO_BOT_COLLISIONS    = ShooterSettings.NO;
            public      static  final   boolean     DISABLE_GRAVITY                     = ShooterSettings.NO;
            public      static  final   boolean     DISABLE_LIGHTING                    = ShooterSettings.YES;
            public      static  final   boolean     INVISIBLE_Z_ZERO_LAYER              = ShooterSettings.YES;
            public      static  final   boolean     DISABLE_LOOK_CENTERING_X            = ShooterSettings.YES;
            public      static  final   boolean     DISABLE_SOUND_BG                    = ShooterSettings.YES;
            public      static  final   boolean     DISABLE_SOUND_FX                    = ShooterSettings.NO;

            public      static  final   boolean     ENABLE_3RD_PERSON_CAMERA            = ShooterSettings.NO;
        }

        public static final class DebugSettings
        {
            public  static  final   boolean     DEBUG_MODE                          = ShooterSettings.YES;

            public  static  final   boolean     DEBUG_DRAW_PLAYER_CIRCLE            = ShooterSettings.YES;
            public  static  final   boolean     DEBUG_DRAW_ITEM_CIRCLE              = ShooterSettings.YES;
            public  static  final   boolean     DEBUG_DRAW_BOT_CIRCLES              = ShooterSettings.NO;
            public  static  final   boolean     DEBUG_SHOW_FPS                      = ShooterSettings.YES;
        }

        public static final class Performance
        {
            public  static  final   int         MIN_THREAD_DELAY                    = 16;

            // --- performance settings --- //
            public  static  final   int         COLLISION_CHECKING_STEPS            = 5;
            public  static  final   int         MAX_NUMBER_BULLET_HOLES             = 256;
            public  static  final   int         ELLIPSE_SEGMENTS                    = 45;

            // --- speed settings --- //
            public  static  final   int         TICKS_WEARPON_HIDE_SHOW             = 4;
            public  static  final   int         TICKS_HEALTH_FX                     = 10;
            public  static  final   int         TICKS_DAMAGE_FX                     = 10;

            public  static  final   int         DELAY_AFTER_PLAYER_ACTION           = 200;
            public  static  final   int         DELAY_AFTER_LEVEL_CHANGE            = 200;
            public  static  final   int         DELAY_AFTER_AVATAR_MESSAGE          = 200;
            public  static  final   int         DELAY_AFTER_CROUCHING               = 200;
            public  static  final   int         DELAY_AFTER_GAIN_HEALTH             = 200;
            public  static  final   int         DELAY_AFTER_DAMAGE_FX               = 200;
            public  static  final   int         DELAY_AFTER_RELOAD                  = 200;
            public  static  final   int         DELAY_AFTER_EXPLOSION               = 200;
            public  static  final   int         DELAY_AFTER_MAIN_MENU_TOGGLE        = 200;
        }

        public static final class Fonts
        {
            public  static  final   Font        EAmmo                               = new Font( "verdana",     Font.BOLD,  12 );
            public  static  final   Font        EHealth                             = new Font( "verdana",     Font.BOLD,  12 );
            public  static  final   Font        EFps                                = new Font( "verdana",     Font.BOLD,  12 );
            public  static  final   Font        EAvatarMessage                      = new Font( "verdana",     Font.BOLD,  12 );
            public  static  final   Font        EDebug                              = new Font( "courier new", Font.PLAIN, 10 );
        }

        public static final class Sounds
        {
            public  static  final   float       SPEECH_PLAYER_DISTANCE_MAX_VOLUME   = 1.0f;
            public  static  final   float       SPEECH_PLAYER_DISTANCE_MUTE         = 20.0f;
        }

        public static final class BotSettings
        {
            public static final class RotationSpeed
            {
                /**************************************************************************************
                *   The relative speed of the limbs in percent of the full distance.
                **************************************************************************************/
                public  static  final   float       LIMBS                           = 5.0f;
                public  static  final   float       UPPER_ARM                       = 5.0f;
                public  static  final   float       LOWER_ARM                       = 7.5f;
                public  static  final   float       HEAD                            = 2.5f;
                public  static  final   float       HAND                            = 10.0f;
            }

            /**************************************************************************************
            *   The maximum turning speed in degrees per tick.
            **************************************************************************************/
            public  static  final   float       SPEED_TURNING_MAX                   = 10.0f;

            /**************************************************************************************
            *   The minimum turning speed in degrees per tick.
            **************************************************************************************/
            public  static  final   float       SPEED_TURNING_MIN                   = 2.5f;

            public  static  final   float       WAY_POINT_RADIUS                    = 0.25f;

            public  static  final   float       SHOT_RANGE                          = 20.0f;
            public  static  final   float       VIEW_RANGE                          = 25.0f;

            public  static  final   float       SPEED_WALKING                       = 0.05f;
            public  static  final   float       HEIGHT                              = 1.3f;
            public  static  final   float       RADIUS                              = 0.25f;

            public  static  final   float       MAX_LEADING_DISTANCE_TO_PLAYER      = 15.0f;

            public  static  final   int         EYE_CLOSED_INTERVAL                 = 50;
            public  static  final   int         EYE_BLINK_INTERVAL_MIN              = 5000;
            public  static  final   int         EYE_BLINK_INTERVAL_MAX              = 10000;
        }

        public static final class OffsetsOrtho
        {
            public  static          int         EBorderHudX                         = 0;
            public  static          int         EBorderHudY                         = 0;
            public  static          int         EHudMsgY                            = 0;
            public  static          int         EAvatarBgPanelHeight                = 0;
            public  static          int         EAvatarMsgX                         = 0;
            public  static          int         EAvatarMsgY                         = 0;

            public static final void parseOffsets( int panelWidth, int panelHeight )
            {
                EBorderHudX             = panelWidth  * 5 / 100;
                EBorderHudY             = panelHeight * 5 / 100;
                EHudMsgY                = panelHeight * 6 / 100;
                EAvatarBgPanelHeight    = 96;
                EAvatarMsgX             = 10;
                EAvatarMsgY             = 10;
            }
        }

        public static final class AvatarMessages
        {
            public  static  final   float       OPACITY_PANEL_BG                    = 0.5f;
            public  static  final   float       OPACITY_AVATAR_IMG                  = 1.0f;

            public  static  final   int         ANIM_TICKS_POP_UP                   = 30;
            public  static  final   int         ANIM_TICKS_STILL                    = 150;
            public  static  final   int         ANIM_TICKS_POP_DOWN                 = 30;
        }

        public static final class Items
        {
            public  static  final   float       AMMO_RADIUS                         = 0.3f;
            public  static  final   float       WEARPON_RADIUS                      = 0.3f;
            public  static  final   float       GADGET_RADIUS                       = 0.3f;
        }

        /**************************************************************************************
        *   Holding all of player's constant attributes.
        *
        *   @author     Christopher Stock
        *   @version    0.3.2
        **************************************************************************************/
        public static interface PlayerAttributes
        {
            //basics
            public  static  final   float       RADIUS                              = 0.4f;             //player's radius

            //depths
            public  static  final   float       DEPTH_TOTAL_STANDING                = 1.3f;             //for collisions
            public  static  final   float       DEPTH_TOTAL_CROUCHING               = 0.9f;             //for collisions
            public  static  final   float       DEPTH_EYE_STANDING                  = 1.2f;             //player's eye-distance from floor
            public  static  final   float       DEPTH_EYE_CROUCHING                 = 0.8f;             //player's eye-distance from floor
            public  static  final   float       DEPTH_HAND_STANDING                 = 1.0f;             //player's hand-distance from floor
            public  static  final   float       DEPTH_HAND_CROUCHING                = 0.6f;             //player's hand-distance from floor
            public  static  final   float       DEPTH_TOE                           = 0.01f;            //player's tow distance from the currently lowest floor
            public  static  final   float       DEPTH_DEATH                         = 0.08f;            //player's view height on being dead and a glorious

            //speed
            public  static  final   float       SPEED_WALKING                       = 0.15f;            //walking  speed
            public  static  final   float       SPEED_STRAFING                      = 0.1f;             //strafing speed
            public  static  final   float       SPEED_CROUCH_TOGGLE                 = 0.1f;             //speed to crouch / get up
            public  static  final   float       SPEED_FALLING_MIN                   = 0.075f;           //falling down on z axis per tick
            public  static  final   float       SPEED_FALLING_MAX                   = 0.3f;             //falling down on z axis per tick
            public  static  final   float       SPEED_FALLING_MULTIPLIER            = 1.25f;            //falling   speed multiplier per tick
            public  static  final   float       SPEED_TURNING_Z                     = 2.5f;             //turning   speed in ° (left/right)
            public  static  final   float       SPEED_TURNING_PRECISE_Z             = 1.0f;             //turning   speed in ° (left/right)
            public  static  final   float       SPEED_LOOKING_X                     = 5.0f; //1.25f;    //looking   speed in ° (up/down)
            public  static  final   float       SPEED_CENTERING_X                   = 10.0f;            //centering speed in ° back to 0°
            public  static  final   float       SPEED_DYING_SINKING                 = 0.1f;             //rotating  speed in ° x y z on dying

            //health
            public  static  final   int         MAX_HEALTH                          = 100;              //maximum health value

            public  static  final   float       ROTATION_DYING                      = 4.5f;             //rotating speed in ° x y z on dying

            public  static  final   float       MAX_LOOKING_X                       = 45.0f;            //22.5f;    //max. look up/down in °
            public  static  final   float       MAX_CLIMBING_UP_Z                   = 0.5f;             //max auto climbing up on faces z

            public  static  final   float       AMP_WALKING_Z                       = 0.05f;             //0.05f;    //z-amplitude ratio

            public  static  final   float       SPEED_WALKING_ANGLE_Y               = 7.5f;             //y-modification per walking-step in °
            public  static  final   float       SPEED_WALKING_ANGLE_WEARPON_X       = 5.0f;             //y-modification per walking-step in °
            public  static  final   float       SPEED_WALKING_ANGLE_WEARPON_Y       = 2.5f;             //y-modification per walking-step in °
        }

        public static class Colors
        {
            public  static  final   LibColors   EAvatarMessageText                  = LibColors.EWhite;
            public  static  final   LibColors   EAvatarMessageTextOutline           = LibColors.EGreyDark;
            public  static  final   LibColors   EFpsFg                              = LibColors.EWhite;
            public  static  final   LibColors   EFpsOutline                         = LibColors.EGreyDark;
            public  static  final   LibColors   EAmmoFg                             = LibColors.EWhite;
            public  static  final   LibColors   EAmmoOutline                        = LibColors.EGreyDark;
            public  static  final   LibColors   EHealthFg                           = LibColors.EWhite;
            public  static  final   LibColors   EHealthOutline                      = LibColors.EGreyDark;
            public  static  final   LibColors   EHudMsgFg                           = LibColors.EWhite;
            public  static  final   LibColors   EHudMsgOutline                      = LibColors.EBlack;

            public  static  final   LibColors   EAvatarMessagePanelBgRed            = LibColors.ERedLight;
            public  static  final   LibColors   EAvatarMessagePanelBgYellow         = LibColors.EYellow;
            public  static  final   LibColors   EAvatarMessagePanelBgGrey           = LibColors.EGreyLight;
            public  static  final   LibColors   EAvatarMessagePanelBgBlack          = LibColors.EBlack;
            public  static  final   LibColors   EAvatarMessagePanelBgGreen          = LibColors.EGreenLight;
            public  static  final   LibColors   EAvatarMessagePanelBgBlue           = LibColors.EBlueLight;
        }

        /**************************************************************************************
        *   All paths the application makes use of.
        *   All references are specified absolute.
        *
        *   @author     Christopher Stock
        *   @version    0.3.2
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
            E3dsMaxBots(    "/res/d3ds/bots/"       ),
            E3dsMaxOthers(  "/res/d3ds/others/"     ),
            ;

            public                  String      iUrl                                = null;

            private Path( String aUrl )
            {
                iUrl = aUrl;

                //assert leading slash
                if ( !iUrl.startsWith( "/" ) ) iUrl = "/" + iUrl;

                //assert trailing slash
                if ( !iUrl.endsWith(   "/" ) ) iUrl = iUrl + "/";
            }
        }
    }
