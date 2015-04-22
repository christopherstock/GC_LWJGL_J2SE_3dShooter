/*  $Id: Shooter.java,v 1.4 2007/09/02 14:19:18 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.shooter.gl.GL3D.Engine3D;

    /*************************************************""*******************************
    *   All settings for the project.
    *
    *   @version    0.2
    *   @author     Christopher Stock
    ***********************************************************************************/
    public class ShooterSettings
    {
        public static final class HUD
        {
            public      static  final   boolean     SHOW_MAGAZINE_SIZE      = true;


        }

        /************************************************************************************
        *   The window's title string.
        ************************************************************************************/
        public      static  final   String      FORM_TITLE                          = "Shooter Prototype, " + Shooter.Version.getCurrentVersionDesc();

        /************************************************************************************
        *   The 3D engine of choice.
        ************************************************************************************/
        public      static  final   Engine3D    ENGINE_3D                           = Engine3D.LWJGL;

        // --- debug defines --- //
        public      static  final   boolean     DISABLE_COLLISIONS                  = Shooter.NO;
        public      static  final   boolean     DISABLE_GRAVITY                     = Shooter.NO;
        public      static  final   boolean     INVISIBLE_Z_ZERO_LAYER              = Shooter.YES;
        public      static  final   boolean     DISABLE_LOOK_CENTERING_X            = Shooter.YES;
        public      static  final   boolean     DISABLE_SOUND_BG                    = Shooter.YES;
        public      static  final   boolean     DISABLE_SOUND_FX                    = Shooter.YES;
        public      static  final   boolean     DEBUG_DRAW_ITEM_CIRCLE              = Shooter.YES;
        public      static  final   boolean     DEBUG_DRAW_BOT_CIRCLES              = Shooter.NO;
        public      static  final   boolean     DEBUG_SHOW_PLAYER_CIRCLE            = Shooter.YES;
        public      static  final   boolean     ENABLE_FPS                          = Shooter.YES;
        public      static  final   boolean     ENABLE_3RD_PERSON_CAMERA            = Shooter.NO;

        // --- performance settings --- //
        public      static  final   int         COLLISION_CHECKING_STEPS            = 5;
        public      static  final   int         MAX_NUMBER_BULLET_HOLES             = 256;
        public      static  final   int         ELLIPSE_SEGMENTS                    = 45;
        public      static  final   int         FORM_WIDTH                          = 640;
        public      static  final   int         FORM_HEIGHT                         = 480;

        // --- speed settings --- //
        public      static  final   int         TICKS_WEARPON_HIDE_SHOW             = 4;
        public      static  final   int         TICKS_HEALTH_FX                     = 10;
        public      static  final   int         TICKS_DAMAGE_FX                     = 10;
    }
