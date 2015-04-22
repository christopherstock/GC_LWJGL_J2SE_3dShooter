/*  $Id: PlayerView.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  de.christopherstock.lib.Lib.Rotation;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The players field of view.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class PlayerView implements PlayerAttributes
    {
        public                      Rotation        rot                         = null;

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

        public PlayerView( Player aParent, Rotation aRot )
        {
            iParentPlayer = aParent;

            rot = aRot;
        }

        protected final void handleKeysForView()
        {
            //turn left precise
            if ( Keys.keyHoldTurnLeftPrecise )
            {
                rot.z += SPEED_TURNING_PRECISE_Z /* * Keys.ticksKeyLeftHold / 4 */;
                rot.z = rot.z >= 360.0f ? rot.z - 360.0f : rot.z;
            }

            //turn left normal
            if ( Keys.keyHoldTurnLeft && !Keys.keyHoldAlternate )
            {
                rot.z += SPEED_TURNING_Z /* * Keys.ticksKeyLeftHold / 4 */;
                rot.z = rot.z >= 360.0f ? rot.z - 360.0f : rot.z;
            }

            //turn right precise
            if ( Keys.keyHoldTurnRightPrecise )
            {
                rot.z -= SPEED_TURNING_PRECISE_Z /* * Keys.ticksKeyRightHold / 4; */;
                rot.z = rot.z < 0.0f ? rot.z + 360.0f : rot.z;
            }

            //turn right normal
            if ( Keys.keyHoldTurnRight && !Keys.keyHoldAlternate )
            {
                rot.z -= SPEED_TURNING_Z /* * Keys.ticksKeyRightHold / 4; */;
                rot.z = rot.z < 0.0f ? rot.z + 360.0f : rot.z;
            }

            //check mouse movement
            if ( MouseInput.mouseMovementX != 0 )
            {
                ShooterDebug.mouse.out( "HANDLE: mouse movement X ["+MouseInput.mouseMovementX+"]" );

                rot.z -= MouseInput.mouseMovementX * 2 * SPEED_TURNING_Z;
                rot.z = rot.z < 0.0f ? rot.z + 360.0f : rot.z;
                rot.z = rot.z >= 360.0f ? rot.z - 360.0f : rot.z;

                MouseInput.mouseMovementX = 0;
            }

            //check x-looking-centering
            if ( Keys.keyHoldLookUp )
            {
                rot.x -= SPEED_LOOKING_X;
                rot.x = rot.x < -MAX_LOOKING_X ? -MAX_LOOKING_X : rot.x;
            }
            else if ( Keys.keyHoldLookDown )
            {
                rot.x += SPEED_LOOKING_X;
                rot.x = rot.x > MAX_LOOKING_X ? MAX_LOOKING_X : rot.x;
            }
            else if ( Keys.keyHoldWalkUp || Keys.keyHoldWalkDown )
            {
                //center back on walking
                if ( !General.DISABLE_LOOK_CENTERING_X ) centerLookXthisTick = true;
            }

            if ( Keys.keyHoldCenterView )
            {
                centerLookX = true;
            }

            if ( iParentPlayer.iCrouching )
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
            //center rot.x?
            if ( centerLookX || centerLookXthisTick )
            {
                centerLookXthisTick = false;
                if ( rot.x > 0.0f )
                {
                    rot.x -= SPEED_CENTERING_X;
                    if ( rot.x <= 0.0f )
                    {
                        rot.x = 0.0f;
                        centerLookX = false;
                    }
                }
                else if ( rot.x < 0.0f )
                {
                    rot.x += SPEED_CENTERING_X;
                    if ( rot.x >= 0.0f )
                    {
                        rot.x = 0.0f;
                        centerLookX = false;
                    }
                }
            }
        }

        public final void animateDying()
        {
            if ( iParentPlayer.isDead() )
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
