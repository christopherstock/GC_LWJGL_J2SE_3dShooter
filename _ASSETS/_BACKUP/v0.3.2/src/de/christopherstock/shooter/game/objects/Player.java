/*  $Id: Player.java 488 2011-03-26 18:10:13Z jenetic.bytemare $
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
    import de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
import de.christopherstock.shooter.game.collision.Shot.ShotType;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;
    import  de.christopherstock.shooter.ui.hud.HUD;
import  de.christopherstock.shooter.ui.hud.HUD.*;

    /**************************************************************************************
    *   Represents the player.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class Player implements GameObject, PlayerAttributes
    {
        /** The player's collision cylinger represents his position and hiscollision body. */
        private                     Cylinder        iCylinder                   = null;

        /** Specifies if the player is currently holding a wearpon or a gadget. */
        private                     HUD.ItemGroup   iCurrentItemGroup           = HUD.ItemGroup.EWearpon;

        /** Player's health. 100 is new-born. 0 is dead. */
        private                     int             iHealth                     = 100;

        /** Disables all gravity checks. */
        private                     boolean         iDisableGravity             = false;

        private                     PlayerView            iView                       = null;
        private                     Ammo            iAmmo                       = null;
        /** Currently holded wearpon. */
        private                     Wearpon         iWearpon                    = null;
        private                     Vector<Wearpon> iWearpons                   = null;
        /** Currently holded gadget. */
        private                     Gadget          iGadget                     = null; //Gadget.EEnvelope;         //current holded gadget
        private                     Vector<Gadget>  iGadgets                    = null;
        private                     boolean         iDead                       = false;
        private                     float           iWalkingAngleY              = 0.0f;             //X-axis-angle on walking
        private                     float           iWalkingAngleWearponX       = 0.0f;             //X-axis-angle on walking
        private                     float           iWalkingAngleWearponY       = 0.0f;             //X-axis-angle on walking
        private                     float           iCurrentSpeedFalling        = SPEED_FALLING_MIN;    //current falling-speed ( will increase exponential )

        /**************************************************************************************
        *   Specifies if the player is currently crouching.
        **************************************************************************************/
        protected                   boolean         iCrouching                  = false;

        public Player( ViewSet aStartPosition, boolean aDisableGravity )
        {
            //init and set cylinder
            iCylinder   = new Cylinder( this, new LibVertex( aStartPosition.pos.x, aStartPosition.pos.y, aStartPosition.pos.z ), RADIUS, DEPTH_TOTAL_STANDING, ShooterSettings.Performance.COLLISION_CHECKING_STEPS, ShooterDebug.playerCylinder, false );
            iView       = new PlayerView(     this, aStartPosition.rot );
            iAmmo       = new Ammo();
            iWearpons   = new Vector<Wearpon>();
            iGadgets    = new Vector<Gadget>();

            //deliver default wearpons
            iWearpons.add( Wearpon.EHands );
            iWearpon    = Wearpon.EHands;

            iDisableGravity = aDisableGravity;
        }

        public final void handleKeys()
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
            if ( Keys.keyHoldFire )
            {
                //Debug.out( "key fire! PRESSED!!!" );

                //launch the shot!
                ShooterMainThread.launchShot = true;

                //release the key if the gun requires this
                if ( iWearpon.getCurrentShotNeedsKeyRelease() )
                {
                    //key-release required before next shot will be launched :)
                    Keys.keyHoldFire = false;
                }

                //delay depends on current wearpon !

                // ...


            }
            else
            {
                //stop launching the shot
                ShooterMainThread.launchShot = false;
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

            //launch main menu change
            Keys.toggleMainMenu.checkLaunchingAction();

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

        public final void performFloorChange()
        {
            //no gravity no performing
            if ( iDisableGravity ) return;

            //browse all faces and set the player to the highest!
            Float highestZ = Level.current().getHighestFloor( iCylinder.getCenterHorz() );
            //ShooterDebug.floorChange.out( "highest floor is: [" + highestZ + "]" );

            //check if the player is falling - directly alter his anchor-z-value!
            if ( highestZ == null )
            {
                if ( iCurrentSpeedFalling > SPEED_FALLING_MAX ) iCurrentSpeedFalling = SPEED_FALLING_MAX;
                iCylinder.getAnchor().z  -= iCurrentSpeedFalling;
                iCurrentSpeedFalling     *= SPEED_FALLING_MULTIPLIER;
            }
            else
            {
                iCurrentSpeedFalling     = SPEED_FALLING_MIN;
                iCylinder.getAnchor().z  = highestZ.floatValue() + DEPTH_TOE;
            }

            //Debug.info( "falling speed: " + currentSpeedFalling );

            //check invisible z-0-layer
            if ( General.INVISIBLE_Z_ZERO_LAYER )
            {
                if ( iCylinder.getAnchor().z < 0.0f ) iCylinder.getAnchor().z = 0.0f;
            }
        }

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
                getWearpon().getCurrentIrregularityAngle(),
                getWearpon().getCurrentIrregularityDepth(),
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

        public final Wearpon getWearpon()
        {
            return iWearpon;
        }

        public final Gadget getGadget()
        {
            return iGadget;
        }

        public final void setWearpon( Wearpon newWearpon )
        {
            iWearpon = newWearpon;
        }

        public final void setGadget( Gadget newGadget )
        {
            iGadget = newGadget;
        }

        public final void drawWearponOrGadget()
        {
            //draw gun & ammo
            switch ( iCurrentItemGroup )
            {
                case EWearpon:
                {
                    if ( iWearpon != null ) iWearpon.drawOrtho();
//                    wearpon.drawAmmo( g );
                    break;
                }

                case EGadget:
                {
                    if ( iGadget != null ) iGadget.drawOrtho();
                    break;
                }
            }
        }

        public final void choosePreviousWearponOrGadget()
        {
            switch ( iCurrentItemGroup )
            {
                case EWearpon:
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
                                //only change to gadgets if gadgets are available
                                if ( iGadgets.size() > 0 )
                                {
                                    //pick last gadget
                                    changeItemGroup( HUD.ItemGroup.EGadget );
                                    setGadget( iGadgets.elementAt( iGadgets.size() - 1 ) );
                                    HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                                }
                                else
                                {
                                    //pick last wearpon
                                    setWearpon( iWearpons.elementAt( iWearpons.size() - 1 ) );

                                    //animate only if more than one wearpon is available!
                                    if ( iWearpons.size() > 1 )
                                    {
                                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                                    }
                                }
                            }
                            else
                            {
                                setWearpon( iWearpons.elementAt( previousIndex ) );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            break;
                        }
                    }
                    break;
                }

                case EGadget:
                {
                    //browse all gadgets
                    for ( int i = 0; i < iGadgets.size(); ++i )
                    {
                        //find current gadget
                        if ( getGadget() == null )
                        {
                            setGadget( iGadgets.elementAt( i ) );
                            break;
                        }
                        else if ( getGadget() == iGadgets.elementAt( i ) )
                        {
                            int previousIndex = i - 1;
                            if ( previousIndex < 0 )
                            {
                                changeItemGroup( HUD.ItemGroup.EWearpon );
                                setWearpon( iWearpons.elementAt( iWearpons.size() - 1 ) );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                setGadget( iGadgets.elementAt( previousIndex ) );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            return;
                        }
                    }
                    break;
                }
            }
        }

        public final void chooseNextWearponOrGadget()
        {
            switch ( iCurrentItemGroup )
            {
                case EWearpon:
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
                                //only change to gadgets if gadgets are available
                                if ( iGadgets.size() > 0 )
                                {
                                    //pick first gadget
                                    changeItemGroup( HUD.ItemGroup.EGadget );
                                    setGadget( iGadgets.elementAt( 0 ) );
                                    HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                                }
                                else
                                {
                                    //pick first wearpon
                                    setWearpon( iWearpons.elementAt( 0 ) );

                                    //animate only if more than one wearpon is available!
                                    if ( iWearpons.size() > 1 )
                                    {
                                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                                    }
                                }
                            }
                            else
                            {
                                setWearpon( iWearpons.elementAt( nextIndex ) );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            break;
                        }
                    }
                    break;
                }

                case EGadget:
                {
                    //browse all holded gadgets
                    for ( int i = 0; i < iGadgets.size(); ++i )
                    {
                        //find current gadget
                        if ( getGadget() == null )
                        {
                            setGadget( iGadgets.elementAt( i ) );
                            break;
                        }
                        else if ( getGadget() == iGadgets.elementAt( i ) )
                        {
                            int nextIndex = i + 1;
                            if ( nextIndex >= iGadgets.size() )
                            {
                                changeItemGroup( HUD.ItemGroup.EWearpon );
                                setWearpon( iWearpons.elementAt( 0 ) );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                setGadget( iGadgets.elementAt( nextIndex ) );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            return;
                        }
                    }
                    break;
                }
            }
        }

        public final void orderWearponOrGadget( HUD.ChangeAction changeAction )
        {
            //if no animation is active
            if ( !HUD.singleton.animationActive() )
            {
                switch ( iCurrentItemGroup )
                {
                    case EWearpon:
                    {
                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationHide, changeAction );
                        break;
                    }

                    case EGadget:
                    {
                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationHide, changeAction );
                        break;
                    }
                }
            }
        }

        public final void handleWearponOrGadget()
        {
            switch ( iCurrentItemGroup )
            {
                case EWearpon:
                {
                    if ( iWearpon != null ) iWearpon.handleWearpon();
                    break;
                }

                case EGadget:
                {
                    if ( iGadget != null ) iGadget.handleGadget();
                    break;
                }
            }
        }

        public final void changeItemGroup( HUD.ItemGroup newItemGroup )
        {
            iCurrentItemGroup = newItemGroup;
        }

        /***********************************************************************************
        *
        *   @return     A value from -1.0 to 1.0.
        ***********************************************************************************/
        public final float getWalkingAngleModifier()
        {
            return LibMath.sinDeg( iWalkingAngleY );
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
                iHealth -= descent;
                if ( iHealth <= 0 )
                {
                    iHealth = 0;
                    kill();
                }

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
                iHealth += gainer;
                if ( iHealth > MAX_HEALTH )
                {
                    iHealth = MAX_HEALTH;
                }

                //start healing screen anim
                HUDFx.launchHealthFX( gainer );
            }
        }

        public final void launchAction()
        {
            ShooterDebug.playerAction.out( "launchAction()" );

            //launch the action on the level
            Level.current().launchAction( iCylinder );
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

        public final ViewSet getCameraPositionAndRotation()
        {
            //3rd person camera?
            float modX = ( !General.ENABLE_3RD_PERSON_CAMERA ? 0.0f : LibMath.sinDeg( iView.rot.z ) * 2.0f );
            float modY = ( !General.ENABLE_3RD_PERSON_CAMERA ? 0.0f : LibMath.cosDeg( iView.rot.z ) * 2.0f );
            float modZ = iCylinder.getAnchor().z + iView.iDepthEye + getWalkingAngleModifier() * PlayerAttributes.AMP_WALKING_Z;

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
            iHealth  = 0;
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

        public final void deliverWearpon( Wearpon wearpon )
        {
            //add to wearpon stack if not already holded
            if ( !iWearpons.contains( wearpon ) )
            {
                //sort wearpons according to enum
                int targetIndex = 0;
                for ( Wearpon iW : iWearpons )
                {
                    if ( wearpon.ordinal() > iW.ordinal() )
                    {
                        ++targetIndex;
                    }
                }
                iWearpons.insertElementAt( wearpon, targetIndex );
            }
        }

        public final void deliverGadget( Gadget gadget )
        {
            //add to wearpon stack if not already holded
            if ( !iGadgets.contains( gadget ) )
            {
                int targetIndex = 0;
                for ( Gadget iG : iGadgets )
                {
                    if ( gadget.ordinal() > iG.ordinal() )
                    {
                        ++targetIndex;
                    }
                }
                iGadgets.insertElementAt( gadget, targetIndex );
            }
        }

        public final boolean showAmmoInHUD()
        {
            return
            (
                    iCurrentItemGroup == ItemGroup.EWearpon
                &&  iWearpon != null
                &&  iWearpon.getAmmoType() != null
            );
        }
    }
