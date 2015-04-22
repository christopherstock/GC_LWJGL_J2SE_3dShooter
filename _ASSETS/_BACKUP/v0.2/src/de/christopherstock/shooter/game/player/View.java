/*  $Id: Player.java,v 1.3 2007/06/05 15:44:27 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.player;

    import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.io.hid.*;
import de.christopherstock.shooter.math.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   A player. Can be the user or a bot.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class View implements Attributes
    {
        public                      float           rotX                        = 0.0f;             //rotation X
        public                      float           rotY                        = 0.0f;             //rotation Y
        public                      float           rotZ                        = 0.0f;             //rotation Z

        /**************************************************************************************
        *   Specifies if the player is currently crouching.
        **************************************************************************************/
        private                     boolean         crouching                   = false;

        /**************************************************************************************
        *   Force x-centering till centered.
        **************************************************************************************/
        public                      boolean         centerLookX                 = false;

        /**************************************************************************************
        *   Force x-centering this tick
        **************************************************************************************/
        public                      boolean         centerLookXthisTick         = false;

        public                      float           depthTotal                  = DEPTH_TOTAL_STANDING;
        public                      float           depthEye                    = DEPTH_EYE_STANDING;
        public                      float           depthHand                   = DEPTH_HAND_STANDING;

        public                      float           dieModX                     = 0.0f;
        public                      float           dieModY                     = 0.0f;
        public                      float           dieModZ                     = 0.0f;
        public                      float           dieModTransZ                = 0.0f;

        public                      int             dyingAnimation              = 0;

        public View( float aRotX, float aRotY, float aRotZ )
        {
            rotX = aRotX;
            rotY = aRotY;
            rotZ = aRotZ;
        }

        public final void handleKeysForView( Cylinder cylinder )
        {
            //turn left - precise or fast ..
            if ( Keys.keyHoldTurnLeftPrecise )
            {
                rotZ += SPEED_TURNING_PRECISE_Z /* * Keys.ticksKeyLeftHold / 4 */;
                rotZ = rotZ >= 360.0f ? rotZ - 360.0f : rotZ;
            }

            if ( Keys.keyHoldTurnLeft || Keys.keyHoldStrafeLeft )
            {
                if ( Keys.keyHoldAlternate || Keys.keyHoldStrafeLeft )
                {
                    cylinder.getTarget().x = cylinder.getTarget().x - UMath.cosDeg( rotZ ) * SPEED_STRAFING;
                    cylinder.getTarget().y = cylinder.getTarget().y + UMath.sinDeg( rotZ ) * SPEED_STRAFING;
                }
                else
                {
                    rotZ += SPEED_TURNING_Z /* * Keys.ticksKeyLeftHold / 4 */;
                    rotZ = rotZ >= 360.0f ? rotZ - 360.0f : rotZ;
                }
            }

            //turn right - precise or fast ..
            if ( Keys.keyHoldTurnRightPrecise )
            {
                rotZ -= SPEED_TURNING_PRECISE_Z /* * Keys.ticksKeyRightHold / 4; */;
                rotZ = rotZ < 0.0f ? rotZ + 360.0f : rotZ;
            }

            if ( Keys.keyHoldTurnRight || Keys.keyHoldStrafeRight )
            {
                if ( Keys.keyHoldAlternate  || Keys.keyHoldStrafeRight )
                {
                    cylinder.getTarget().x = cylinder.getTarget().x + UMath.cosDeg( rotZ ) * SPEED_STRAFING;
                    cylinder.getTarget().y = cylinder.getTarget().y - UMath.sinDeg( rotZ ) * SPEED_STRAFING;
                }
                else
                {
                    rotZ -= SPEED_TURNING_Z /* * Keys.ticksKeyRightHold / 4; */;
                    rotZ = rotZ < 0.0f ? rotZ + 360.0f : rotZ;
                }
            }

            //check mouse movement
            if ( Mouse.mouseMovementX != 0 )
            {
                Debug.mouse.out( "HANDLE: mouse movement X ["+Mouse.mouseMovementX+"]" );

                rotZ -= Mouse.mouseMovementX * 2 * SPEED_TURNING_Z;
                rotZ = rotZ < 0.0f ? rotZ + 360.0f : rotZ;
                rotZ = rotZ >= 360.0f ? rotZ - 360.0f : rotZ;

                Mouse.mouseMovementX = 0;
            }

            //check x-looking-centering
            if ( Keys.keyHoldLookUp )
            {
                rotX -= SPEED_LOOKING_X;
                rotX = rotX < -MAX_LOOKING_X ? -MAX_LOOKING_X : rotX;
            }
            else if ( Keys.keyHoldLookDown )
            {
                rotX += SPEED_LOOKING_X;
                rotX = rotX > MAX_LOOKING_X ? MAX_LOOKING_X : rotX;
            }
            else if ( Keys.keyHoldWalkUp || Keys.keyHoldWalkDown )
            {
                //center back on walking
                if ( !Shooter.DISABLE_LOOK_CENTERING_X ) centerLookXthisTick = true;
            }

            if ( Keys.keyHoldCenterView )
            {
                centerLookX = true;
            }

            //crouch
            if ( Keys.keyPressedCrouch )
            {
                Keys.keyPressedCrouch = false;
                crouching = !crouching;
            }
            if ( crouching )
            {
                depthEye  -= SPEED_CROUCH_TOGGLE;
                depthHand -= SPEED_CROUCH_TOGGLE;
                depthTotal = DEPTH_TOTAL_CROUCHING;

                if ( depthEye  < DEPTH_EYE_CROUCHING  ) depthEye  = DEPTH_EYE_CROUCHING;
                if ( depthHand < DEPTH_HAND_CROUCHING ) depthHand = DEPTH_HAND_CROUCHING;
            }
            else
            {
                depthEye  += SPEED_CROUCH_TOGGLE;
                depthHand += SPEED_CROUCH_TOGGLE;
                depthTotal = DEPTH_TOTAL_STANDING;

                if ( depthEye  > DEPTH_EYE_STANDING  ) depthEye  = DEPTH_EYE_STANDING;
                if ( depthHand > DEPTH_HAND_STANDING ) depthHand = DEPTH_HAND_STANDING;
            }
        }

        public final void centerVerticalLook()
        {
            //center rotX?
            if ( centerLookX || centerLookXthisTick )
            {
                centerLookXthisTick = false;
                if ( rotX > 0.0f )
                {
                    rotX -= SPEED_CENTERING_X;
                    if ( rotX <= 0.0f )
                    {
                        rotX = 0.0f;
                        centerLookX = false;
                    }
                }
                else if ( rotX < 0.0f )
                {
                    rotX += SPEED_CENTERING_X;
                    if ( rotX >= 0.0f )
                    {
                        rotX = 0.0f;
                        centerLookX = false;
                    }
                }
            }
        }

        public final void animateDying()
        {
            //stop turning if the player lies on the floor
            if ( dieModTransZ >= depthEye )
            {
                return;
            }

            //shake player's head
            dieModX -= ROTATION_DYING / 10;
            dieModY += ROTATION_DYING;
            dieModZ += ROTATION_DYING;  // * UMath.getRandom( 0, 10 ) / 10;

            //let player sink
            dieModTransZ += SPEED_DYING_SINKING;
            if ( dieModTransZ >= depthEye ) dieModTransZ = depthEye;

            //start anim each x ticks
            if ( ( dyingAnimation++ ) % 4 == 0 ) HUD.singleton.launchDamageFX( 10 );

        }
    }
