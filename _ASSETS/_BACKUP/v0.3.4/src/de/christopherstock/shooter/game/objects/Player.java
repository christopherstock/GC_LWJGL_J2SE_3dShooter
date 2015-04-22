/*  $Id: Player.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.awt.*;
    import  java.util.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.Shot.ShotType;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   Represents the player.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class Player implements GameObject, PlayerAttributes
    {
        public static interface HealthChangeListener
        {
            public abstract void healthChanged();
        }

        /** The player's collision cylinger represents his position and hiscollision body. */
        private                     Cylinder                iCylinder                   = null;

        /** Player's health. 100 is new-born. 0 is dead. */
        private                     int                     iHealth                     = 100;

        /** Disables all gravity checks. */
        private                     boolean                 iDisableGravity             = false;

        private                     PlayerView              iView                       = null;
        private                     Ammo                    iAmmo                       = null;
        /** Currently holded wearpon. */
        private                     Artefact                 iWearpon                    = null;
        private                     Vector<Artefact>         iWearpons                   = null;
        private                     boolean                 iDead                       = false;
        /** X-axis-angle on walking. */
        private                     float                   iWalkingAngleY              = 0.0f;
        private                     float                   iWalkingAngleWearponX       = 0.0f;
        private                     float                   iWalkingAngleWearponY       = 0.0f;
        private                     float                   iCurrentSpeedFalling        = SPEED_FALLING_MIN;

        /**************************************************************************************
        *   Specifies if the player is currently crouching.
        **************************************************************************************/
        protected                   boolean                 iCrouching                  = false;

        /**************************************************************************************
        *
        **************************************************************************************/
        private                     boolean                 iLaunchShot                 = false;

        private                     HealthChangeListener    iHealthChangeCallback       = null;

        public Player( ViewSet aStartPosition, boolean aDisableGravity )
        {
            //init and set cylinder
            iCylinder               = new Cylinder( this, new LibVertex( aStartPosition.pos.x, aStartPosition.pos.y, aStartPosition.pos.z ), RADIUS, DEPTH_TOTAL_STANDING, ShooterSettings.Performance.COLLISION_CHECKING_STEPS, ShooterDebug.playerCylinder, false, PlayerAttributes.MAX_CLIMBING_UP_Z, PlayerAttributes.MIN_CLIMBING_UP_Z );
            iView                   = new PlayerView(     this, aStartPosition.rot );
            iAmmo                   = new Ammo();
            iWearpons               = new Vector<Artefact>();
            iHealthChangeCallback   = HUD.getSingleton();
            iWearpon                = Artefact.EHands;
            iDisableGravity         = aDisableGravity;

            //deliver default wearpons
            iWearpons.add( Artefact.EHands );
        }

        private final void handleKeys()
        {
            //only if alive
            if ( !iDead )
            {
                handleKeysForMovement();        //handle game keys to specify player's new position
                iView.handleKeysForView();      //handle game keys to specify player's new view
                handleKeysForActions();         //handle game keys to invoke actions
            }
        }

        /**************************************************************************************
        *   Affects a game-key per game-tick and alters the Player's values.
        **************************************************************************************/
        private void handleKeysForMovement()
        {
/*
            Keys.ticksKeyLeftHold     = ( Keys.keyLeftHold     ? Keys.ticksKeyLeftHold     + 1 : 0 );
            Keys.ticksKeyRightHold    = ( Keys.keyRightHold    ? Keys.ticksKeyRightHold    + 1 : 0 );
            Keys.ticksKeyPageUpHold   = ( Keys.keyPageUpHold   ? Keys.ticksKeyPageUpHold   + 1 : 0 );
            Keys.ticksKeyPageDownHold = ( Keys.keyPageDownHold ? Keys.ticksKeyPageDownHold + 1 : 0 );
*/
            //forewards
            if ( Keys.keyHoldWalkUp )
            {
                //change character's position
                iCylinder.getTarget().x = iCylinder.getTarget().x - LibMath.sinDeg( iView.rot.z ) * SPEED_WALKING;
                iCylinder.getTarget().y = iCylinder.getTarget().y - LibMath.cosDeg( iView.rot.z ) * SPEED_WALKING;

                //increase walkY-axis-angles
                iWalkingAngleY          += SPEED_WALKING_ANGLE_Y;
                iWalkingAngleWearponX   += SPEED_WALKING_ANGLE_WEARPON_X;
                iWalkingAngleWearponY   += SPEED_WALKING_ANGLE_WEARPON_Y;
                iWalkingAngleY          = iWalkingAngleY        > 360.0f ? iWalkingAngleY - 360.0f        : iWalkingAngleY;
                iWalkingAngleWearponX   = iWalkingAngleWearponX > 360.0f ? iWalkingAngleWearponX - 360.0f : iWalkingAngleWearponX;
                iWalkingAngleWearponY   = iWalkingAngleWearponY > 360.0f ? iWalkingAngleWearponY - 360.0f : iWalkingAngleWearponY;
            }

            //backwards
            if ( Keys.keyHoldWalkDown )
            {
                //change character's position
                iCylinder.getTarget().x = iCylinder.getTarget().x + LibMath.sinDeg( iView.rot.z ) * SPEED_WALKING;
                iCylinder.getTarget().y = iCylinder.getTarget().y + LibMath.cosDeg( iView.rot.z ) * SPEED_WALKING;

                //increase walkY-axis-angles
                //walkingAngle1 += CHARACTER_WALKING_ANGLE_1_SPEED;
                iWalkingAngleWearponX += SPEED_WALKING_ANGLE_WEARPON_X;
                iWalkingAngleWearponY += SPEED_WALKING_ANGLE_WEARPON_Y;
                //walkingAngle1 = walkingAngle1 > 360.0f ? walkingAngle1 - 360.0f : walkingAngle1;
                iWalkingAngleWearponX = iWalkingAngleWearponX > 360.0f ? iWalkingAngleWearponX - 360.0f : iWalkingAngleWearponX;
                iWalkingAngleWearponY = iWalkingAngleWearponY > 360.0f ? iWalkingAngleWearponY - 360.0f : iWalkingAngleWearponY;

                //decrease walkY-axis-angle
                iWalkingAngleY -= SPEED_WALKING_ANGLE_Y;
                //walkingAngle2 -= CHARACTER_WALKING_ANGLE_2_SPEED;
                //walkingAngle3 -= CHARACTER_WALKING_ANGLE_3_SPEED;
                iWalkingAngleY = iWalkingAngleY < 0.0f ? iWalkingAngleY + 360.0f : iWalkingAngleY;
                //walkingAngle2 = walkingAngle2 < 0.0f ? walkingAngle2 + 360.0f : walkingAngle2;
                //walkingAngle3 = walkingAngle3 < 0.0f ? walkingAngle3 + 360.0f : walkingAngle3;
            }

            //left
            if ( Keys.keyHoldTurnLeft || Keys.keyHoldStrafeLeft )
            {
                if ( Keys.keyHoldAlternate || Keys.keyHoldStrafeLeft )
                {
                    iCylinder.getTarget().x = iCylinder.getTarget().x - LibMath.cosDeg( iView.rot.z ) * SPEED_STRAFING;
                    iCylinder.getTarget().y = iCylinder.getTarget().y + LibMath.sinDeg( iView.rot.z ) * SPEED_STRAFING;
                }
                else
                {
                }
            }

            //right
            if ( Keys.keyHoldTurnRight || Keys.keyHoldStrafeRight )
            {
                if ( Keys.keyHoldAlternate  || Keys.keyHoldStrafeRight )
                {
                    iCylinder.getTarget().x = iCylinder.getTarget().x + LibMath.cosDeg( iView.rot.z ) * SPEED_STRAFING;
                    iCylinder.getTarget().y = iCylinder.getTarget().y - LibMath.sinDeg( iView.rot.z ) * SPEED_STRAFING;
                }
                else
                {
                }
            }
        }

        private void handleKeysForActions()
        {
            //launch a shot - delay after shot depends on current wearpon
            if ( Keys.keyHoldFire || MouseInput.mouseHoldFire )
            {
                //perform nothing if the key must be released
                if ( Keys.keyHoldFireMustBeReleased )
                {
                    //stop launching the shot
                    iLaunchShot = false;
                }
                else
                {
                    //launch the shot!
                    iLaunchShot = true;

                    //release the key if the gun requires this
                    if ( iWearpon.getCurrentShotNeedsKeyRelease() )
                    {
                        //force release after number of shots withput release is reached
                        if ( iWearpon.iCurrentShotsWithoutKeyRelease >= iWearpon.iShotsTillKeyReleaseRequired )
                        {
                            //key-release required before next shot will be launched :)
                            Keys.keyHoldFireMustBeReleased = true;
                        }
                    }
                }
            }
            else
            {
                //reset wearpon's shots withput key release
                iWearpon.iCurrentShotsWithoutKeyRelease = 0;

                //reset
                Keys.keyHoldFireMustBeReleased = false;

                //stop launching the shot
                iLaunchShot = false;
            }

            //launch crouching
            Keys.crouching.checkLaunchingAction();

            //launch reload
            Keys.reload.checkLaunchingAction();

            //launch an action
            Keys.playerAction.checkLaunchingAction();

            //launch avatar message
            Keys.avatarMessage.checkLaunchingAction();

            //launch explosion
            Keys.explosion.checkLaunchingAction();

            //launch health fx
            Keys.gainHealth.checkLaunchingAction();

            //launch damage fx
            Keys.damageFx.checkLaunchingAction();
        }

        public final Cylinder getCylinder()
        {
            return iCylinder;
        }

        public final void toggleCrouching()
        {
            iCrouching = !iCrouching;
        }

        private final void performFloorChange()
        {
            //no gravity no performing
            if ( iDisableGravity ) return;

            //browse all faces and set the player to the highest!

            //try algo with cylinder first
            Float highestZ = ShooterGameLevel.current().getHighestFloor( iCylinder  );

            //check if the player is falling - if no highest point or highest point is too far away
            if ( highestZ == null || highestZ.floatValue() < iCylinder.getAnchor().z - PlayerAttributes.MAX_CLIMBING_UP_Z / 2  )
            {
                if ( iCurrentSpeedFalling > SPEED_FALLING_MAX ) iCurrentSpeedFalling = SPEED_FALLING_MAX;

                iCylinder.getAnchor().z  -= iCurrentSpeedFalling;
                iCurrentSpeedFalling     *= SPEED_FALLING_MULTIPLIER;
            }
            else
            {
                //assign face's z
                iCurrentSpeedFalling     = SPEED_FALLING_MIN;
                iCylinder.getAnchor().z  = highestZ.floatValue();
            }

            //Debug.info( "falling speed: " + currentSpeedFalling );

            //check invisible z-0-layer
            if ( General.INVISIBLE_Z_ZERO_LAYER )
            {
                if ( iCylinder.getAnchor().z < 0.0f ) iCylinder.getAnchor().z = 0.0f;
            }
        }

        @Deprecated
        public final void drawDebugLog( Graphics2D g )
        {
            //int y = GLPanel.PANEL_HEIGHT - Offset.EBorderHudY;
            int y = OffsetsOrtho.EBorderHudY + 90;

            //HUD.releaseClip( g );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posX:  " + iCylinder.getAnchor().x,  OffsetsOrtho.EBorderHudX, y - 80 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posY:  " + iCylinder.getAnchor().y,  OffsetsOrtho.EBorderHudX, y - 70 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posZ:  " + iCylinder.getAnchor().z,  OffsetsOrtho.EBorderHudX, y - 60 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotX:  " + iView.rot.x,              OffsetsOrtho.EBorderHudX, y - 50 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotY:  " + iView.rot.y,              OffsetsOrtho.EBorderHudX, y - 40 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotZ:  " + iView.rot.z,              OffsetsOrtho.EBorderHudX, y - 30 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk1: " + iWalkingAngleY,           OffsetsOrtho.EBorderHudX, y - 20 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk2: " + iWalkingAngleWearponX,           OffsetsOrtho.EBorderHudX, y - 10 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk3: " + iWalkingAngleWearponY,           OffsetsOrtho.EBorderHudX, y - 0 );
        }

        public final Shot getShot()
        {
            return new Shot
            (
                ShotType.ESharpAmmo,
                Shot.ShotOrigin.EPlayer,
                ( (FireArm)( getWearpon().iWearponBehaviour ) ).getCurrentIrregularityHorz(),
                ( (FireArm)( getWearpon().iWearponBehaviour ) ).getCurrentIrregularityVert(),
                iCylinder.getAnchor().x,
                iCylinder.getAnchor().y,
                iCylinder.getAnchor().z + iView.iDepthHand,
                iView.rot.z,
                iView.rot.x,
                getWearpon().getShotRange(),
                getWearpon().getBulletHoleSize(),
                ShooterDebug.shot
            );
        }

        public final Artefact getWearpon()
        {
            return iWearpon;
        }

        public final void setWearpon( Artefact newWearpon )
        {
            iWearpon = newWearpon;
        }

        public final void drawWearponOrGadget()
        {
            if ( iWearpon != null ) iWearpon.drawOrtho();
        }

        public final void choosePreviousWearponOrGadget()
        {
            //browse all wearpons
            for ( int i = 0; i < iWearpons.size(); ++i )
            {
                //find current wearpon
                if ( getWearpon() == null )
                {
                    setWearpon( iWearpons.elementAt( i ) );
                    break;
                }
                else if (  getWearpon() == iWearpons.elementAt( i ) )
                {
                    int previousIndex = i - 1;
                    if ( previousIndex < 0 )
                    {
                        //pick last wearpon
                        setWearpon( iWearpons.elementAt( iWearpons.size() - 1 ) );

                        //animate only if more than one wearpon is available!
                        if ( iWearpons.size() > 1 )
                        {
                            HUD.getSingleton().startHudAnimation( HUD.Animation.EAnimationShow, null );
                        }
                    }
                    else
                    {
                        setWearpon( iWearpons.elementAt( previousIndex ) );
                        HUD.getSingleton().startHudAnimation( HUD.Animation.EAnimationShow, null );
                    }
                    break;
                }
            }
        }

        public final void chooseNextWearponOrGadget()
        {
            //browse all holded wearpons
            for ( int i = 0; i < iWearpons.size(); ++i )
            {
                //find current wearpon
                if ( getWearpon() == null )
                {
                    setWearpon( iWearpons.elementAt( i ) );
                    break;
                }
                else if ( getWearpon() == iWearpons.elementAt( i ) )
                {
                    int nextIndex = i + 1;
                    if ( nextIndex >= iWearpons.size() )
                    {
                        //pick first wearpon
                        setWearpon( iWearpons.elementAt( 0 ) );

                        //animate only if more than one wearpon is available!
                        if ( iWearpons.size() > 1 )
                        {
                            HUD.getSingleton().startHudAnimation( HUD.Animation.EAnimationShow, null );
                        }
                    }
                    else
                    {
                        setWearpon( iWearpons.elementAt( nextIndex ) );
                        HUD.getSingleton().startHudAnimation( HUD.Animation.EAnimationShow, null );
                    }
                    break;
                }
            }
        }

        public final void orderWearponOrGadget( HUD.ChangeAction changeAction )
        {
            //if no animation is active
            if ( !HUD.getSingleton().animationActive() )
            {
                HUD.getSingleton().startHudAnimation( HUD.Animation.EAnimationHide, changeAction );
            }
        }

        public final void handleWearponOrGadget()
        {
            if ( iWearpon != null ) iWearpon.handleWearpon();
        }

        /***********************************************************************************
        *
        *   @return     A value from -1.0 to 1.0.
        ***********************************************************************************/
        public final float getWalkingAngleModifier()
        {
            boolean disable = General.DISABLE_PLAYER_WALKING_ANGLE_Y;
            return ( disable ? 0.0f : LibMath.sinDeg( iWalkingAngleY ) );
        }
        public final float getWalkingAngleCarriedModifierX()
        {
            return LibMath.sinDeg( iWalkingAngleWearponX );
        }
        public final float getWalkingAngleCarriedModifierY()
        {
            return LibMath.sinDeg( iWalkingAngleWearponY );
        }

        public final void hurt( int descent )
        {
            //player can only lose energy if he is not dying
            if ( !iDead && iHealth > 0 )
            {
                //substract damage - clip rock bottom
                setHealth( iHealth - descent );

                //start red screen anim
                HUDFx.launchDamageFX( descent );
            }
        }

        public final void heal( int gainer )
        {
            //player can only be healed if it is not too late
            if ( !iDead && iHealth < MAX_HEALTH )
            {
                //substract damage - clip rock bottom
                setHealth( iHealth + gainer );

                //start healing screen anim
                HUDFx.launchHealthFX( gainer );
            }
        }

        public final void launchAction()
        {
            ShooterDebug.playerAction.out( "launchAction()" );

            //launch the action on the level
            ShooterGameLevel.current().launchAction( iCylinder );
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iCylinder.getAnchor();
        }

        @Override
        public final float getCarriersFaceAngle()
        {
            return 0.0f;
        }

        @Override
        public final Vector<HitPoint> launchShot( Shot shot )
        {
            return getCylinder().launchShot( shot );
        }

        public final void drawStandingCircle()
        {
            iCylinder.drawStandingCircle();
        }

        public final int getHealth()
        {
            return iHealth;
        }

        private final void setHealth( int health )
        {
            iHealth = health;

            //clip roof
            if ( iHealth > MAX_HEALTH )
            {
                iHealth = MAX_HEALTH;
            }

            //clip ceiling - kill if player falls to death
            if ( iHealth <= 0 )
            {
                iHealth = 0;
                kill();
            }

            //health changed
            iHealthChangeCallback.healthChanged();
        }

        public final ViewSet getCameraPositionAndRotation()
        {
            //3rd person camera?
            float modX = ( !General.ENABLE_3RD_PERSON_CAMERA ? 0.0f : LibMath.sinDeg( iView.rot.z ) * 2.0f );
            float modY = ( !General.ENABLE_3RD_PERSON_CAMERA ? 0.0f : LibMath.cosDeg( iView.rot.z ) * 2.0f );
            float modZ = iView.iDepthEye + getWalkingAngleModifier() * PlayerAttributes.AMP_WALKING_Z;

            float posX = 0.0f;
            float posY = 0.0f;
            float posZ = 0.0f;

            //check if user is dying
            if ( iDead )
            {
                posX = -iCylinder.getAnchor().x;
                posZ = -iCylinder.getAnchor().z - PlayerAttributes.DEPTH_DEATH - ( iView.iDepthEye - iView.dieModTransZ );
                posY = -iCylinder.getAnchor().y;
            }
            else
            {
                posX = -iCylinder.getAnchor().x - modX;
                posZ = -iCylinder.getAnchor().z - modZ - iView.dieModTransZ;
                posY = -iCylinder.getAnchor().y - modY;
            }

            return new ViewSet
            (
                posX,
                posY,
                posZ,
                iView.rot.x + iView.dieModX,
                iView.rot.y + iView.dieModY,
                iView.rot.z - iView.dieModZ
            );
        }

        /***********************************************************************************
        *   What would this method possibly do to the player?
        ***********************************************************************************/
        public final void kill()
        {
            iHealth = 0;
            iDead   = true;
        }

        public final boolean isDead()
        {
            return iDead;
        }

        public final Ammo getAmmo()
        {
            return iAmmo;
        }

        public final PlayerView getView()
        {
            return iView;
        }

        public final void moveToNewPosition()
        {
            getCylinder().moveToTargetPosition( getView().iDepthTotal, General.DISABLE_PLAYER_TO_WALL_COLLISIONS, General.DISABLE_PLAYER_TO_BOT_COLLISIONS );
        }

        @Override
        public HitPointCarrier getHitPointCarrier()
        {
            return HitPointCarrier.EPlayer;
        }

        public final void deliverWearpon( Artefact wearpon )
        {
            //add to wearpon stack if not already holded
            if ( !iWearpons.contains( wearpon ) )
            {
                //sort wearpons according to enum
                int targetIndex = 0;
                for ( Artefact iW : iWearpons )
                {
                    if ( wearpon.ordinal() > iW.ordinal() )
                    {
                        ++targetIndex;
                    }
                }
                iWearpons.insertElementAt( wearpon, targetIndex );
            }
        }

        public final boolean showAmmoInHUD()
        {
            return
            (
                    //iCurrentItemGroup == ItemGroup.EWearpon
                    iWearpon != null
                &&  iWearpon.isReloadable()
            );
        }

        public final void animate()
        {
            getCylinder().setAnchorAsTargetPosition();  //set current position as the target-position
            handleKeys();                               //handle all game keys for the player
            getView().centerVerticalLook();             //change vertical camera
            getView().animateDying();                   //animate dying
            moveToNewPosition();                        //move player to new position ( collisions may influence new position )
            performFloorChange();                       //move player according to map collision ( floors )
            handleWearponOrGadget();                    //handle wearpon ( shoot or reload )
        }

        public Artefact getCurrentWearpon()
        {
            return iWearpon;
        }

        public final boolean getLaunchShot()
        {
            //operate synchronous
            return iLaunchShot;
        }

        public final boolean isHealthLow()
        {
            return ( iHealth <= HUDSettings.PLAYER_LOW_HEALTH_WARNING_PERCENT );
        }
    }
