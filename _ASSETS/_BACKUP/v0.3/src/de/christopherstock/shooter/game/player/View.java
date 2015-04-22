/*  $Id: View.java 232 2011-01-27 18:08:24Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.player;

    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The players field of view.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class View implements PlayerAttributes
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

        public                      Player          iParentPlayer               = null;

        public                      float           iDepthTotal                 = DEPTH_TOTAL_STANDING;
        public                      float           iDepthEye                   = DEPTH_EYE_STANDING;
        public                      float           iDepthHand                  = DEPTH_HAND_STANDING;

        public                      float           dieModX                     = 0.0f;
        public                      float           dieModY                     = 0.0f;
        public                      float           dieModZ                     = 0.0f;
        public                      float           dieModTransZ                = 0.0f;

        public                      int             dyingAnimation              = 0;

        public View( Player aParent, float aRotX, float aRotY, float aRotZ )
        {
            iParentPlayer = aParent;

            rotX = aRotX;
            rotY = aRotY;
            rotZ = aRotZ;
        }

        protected final void handleKeysForView()
        {
            //turn left precise
            if ( Keys.keyHoldTurnLeftPrecise )
            {
                rotZ += SPEED_TURNING_PRECISE_Z /* * Keys.ticksKeyLeftHold / 4 */;
                rotZ = rotZ >= 360.0f ? rotZ - 360.0f : rotZ;
            }

            //turn left normal
            if ( Keys.keyHoldTurnLeft && !Keys.keyHoldAlternate )
            {
                rotZ += SPEED_TURNING_Z /* * Keys.ticksKeyLeftHold / 4 */;
                rotZ = rotZ >= 360.0f ? rotZ - 360.0f : rotZ;
            }

            //turn right precise
            if ( Keys.keyHoldTurnRightPrecise )
            {
                rotZ -= SPEED_TURNING_PRECISE_Z /* * Keys.ticksKeyRightHold / 4; */;
                rotZ = rotZ < 0.0f ? rotZ + 360.0f : rotZ;
            }

            //turn right normal
            if ( Keys.keyHoldTurnRight && !Keys.keyHoldAlternate )
            {
                rotZ -= SPEED_TURNING_Z /* * Keys.ticksKeyRightHold / 4; */;
                rotZ = rotZ < 0.0f ? rotZ + 360.0f : rotZ;
            }

            //check mouse movement
            if ( Mouse.mouseMovementX != 0 )
            {
                ShooterDebugSystem.mouse.out( "HANDLE: mouse movement X ["+Mouse.mouseMovementX+"]" );

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
                if ( !ShooterSettings.DISABLE_LOOK_CENTERING_X ) centerLookXthisTick = true;
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
                iDepthEye  -= SPEED_CROUCH_TOGGLE;
                iDepthHand -= SPEED_CROUCH_TOGGLE;
                iDepthTotal = DEPTH_TOTAL_CROUCHING;

                if ( iDepthEye  < DEPTH_EYE_CROUCHING  ) iDepthEye  = DEPTH_EYE_CROUCHING;
                if ( iDepthHand < DEPTH_HAND_CROUCHING ) iDepthHand = DEPTH_HAND_CROUCHING;
            }
            else
            {
                iDepthEye  += SPEED_CROUCH_TOGGLE;
                iDepthHand += SPEED_CROUCH_TOGGLE;
                iDepthTotal = DEPTH_TOTAL_STANDING;

                if ( iDepthEye  > DEPTH_EYE_STANDING  ) iDepthEye  = DEPTH_EYE_STANDING;
                if ( iDepthHand > DEPTH_HAND_STANDING ) iDepthHand = DEPTH_HAND_STANDING;
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
            if ( iParentPlayer.isDying() )
            {
                //stop turning if the player lies on the floor
                if ( dieModTransZ >= iDepthEye )
                {
                    return;
                }

                //shake player's head
                dieModX -= ROTATION_DYING / 10;
                dieModY += ROTATION_DYING;
                dieModZ += ROTATION_DYING;  // * UMath.getRandom( 0, 10 ) / 10;

                //let player sink
                dieModTransZ += SPEED_DYING_SINKING;
                if ( dieModTransZ >= iDepthEye ) dieModTransZ = iDepthEye;

                //start anim each x ticks
                if ( ( dyingAnimation++ ) % 4 == 0 ) HUDFx.launchDamageFX( 10 );
            }
        }
    }
