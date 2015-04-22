/*  $Id: Player.java,v 1.3 2007/06/05 15:44:27 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.player;

    /**************************************************************************************
    *   Holding all of player's constant attributes.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    interface Attributes
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
        static  final   float       SPEED_TURNING_Z             = 5.0f;             //turning speed in ° (left/right)
        static  final   float       SPEED_TURNING_PRECISE_Z     = 2.5f;             //turning precise in ° (left/right)
        static  final   float       SPEED_LOOKING_X             = 5.0f; //1.25f;    //looking speed in ° (up/down)
        static  final   float       SPEED_CENTERING_X           = 10.0f;            //centering speed in ° back to 0°
        static  final   float       SPEED_DYING_SINKING         = 0.1f;             //rotating speed in ° x y z on dying

        //health
        static  final   int         MIN_HEALTH                  = 1;                //lowest health value before dying
        static  final   int         MAX_HEALTH                  = 100;              //maximum health value

        static  final   float       ROTATION_DYING              = 4.5f;             //rotating speed in ° x y z on dying

        static  final   float       MAX_LOOKING_X               = 45.0f;            //22.5f;    //max. look up/down in °
        static  final   float       AMP_WALKING_Z               = 0.05f;            //0.05f;    //z-amplitude ratio
        static  final   float       MAX_CLIMBING_UP_Z           = 0.5f;             //max auto climbing up on faces z

        static  final   float       SPEED_WALKING_ANGLE_1       = 15.0f;            //y-modification per walking-step in °
        static  final   float       SPEED_WALKING_ANGLE_2       = 30.0f;            //y-modification per walking-step in °
        static  final   float       SPEED_WALKING_ANGLE_3       = 15.0f;            //y-modification per walking-step in °

        static  final   long        DELAY_AFTER_PLAYER_ACTION   = 500;              //millis delay after player action


    }
