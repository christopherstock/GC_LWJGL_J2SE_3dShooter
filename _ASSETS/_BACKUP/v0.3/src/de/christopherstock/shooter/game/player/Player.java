/*  $Id: Player.java 270 2011-02-11 19:11:49Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.player;

    import  java.awt.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;
    import  de.christopherstock.shooter.ShooterSettings.Offset;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;
    import  de.christopherstock.shooter.ui.hud.HUD.*;

    /**************************************************************************************
    *   Represents the player.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class Player implements GameObject, PlayerAttributes
    {
        /** The player's collision cylinger represents his position and hiscollision body. */
        private                     Cylinder        iCylinder                   = null;

        /** Specifies if the player is currently holding a wearpon or a gadget. */
        private                     HUD.ItemGroup   iCurrentItemGroup           = HUD.ItemGroup.EWearpon;

        /** Player's health. 100 is new-born. 0 is dead. */
        private                     float           health                      = 100.0f;

        /** Disables all gravity checks. */
        private                     boolean         iDisableGravity             = false;

        private                     View            iView                       = null;
        private                     Ammo            iAmmo                       = null;
        /** Currently holded wearpon. */
        private                     Wearpon         iWearpon                    = null;
        private                     Vector<Wearpon> iWearpons                   = null;
        /** Currently holded gadget. */
        private                     Gadget          iGadget                     = null; //Gadget.EEnvelope;         //current holded gadget
        private                     Vector<Gadget>  iGadgets                    = null;
        private                     boolean         iDying                      = false;
        private                     float           iWalkingAngle1              = 0.0f;             //X-axis-angle on walking
        private                     float           iWalkingAngle2              = 0.0f;             //X-axis-angle on walking
        private                     float           iWalkingAngle3              = 0.0f;             //X-axis-angle on walking
        private                     float           iCurrentSpeedFalling        = SPEED_FALLING_MIN;    //current falling-speed ( will increase exponential )
        private                     long            iNextPlayerActionMillis     = 0;

        public Player( ViewSet aStartPosition, boolean aDisableGravity )
        {
            //init and set cylinder
            iCylinder   = new Cylinder( this, new LibVertex( aStartPosition.posX, aStartPosition.posY, aStartPosition.posZ ), RADIUS, DEPTH_TOTAL_STANDING );
            iView       = new View(     this, aStartPosition.rotX, aStartPosition.rotY, aStartPosition.rotZ );
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
            if ( !isDying() )
            {
                handleKeysForMovement();          //handle game keys to specify player's new position
                handleKeysForView();              //handle game keys to specify player's new view
                handleKeysForActions();           //handle game keys to invoke actions
            }
        }

        private void handleKeysForView()
        {
            iView.handleKeysForView();
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
                iCylinder.getTarget().x = iCylinder.getTarget().x - LibMath.sinDeg( iView.rotZ ) * SPEED_WALKING;
                iCylinder.getTarget().y = iCylinder.getTarget().y - LibMath.cosDeg( iView.rotZ ) * SPEED_WALKING;

                //increase walkY-axis-angles
                iWalkingAngle1 += SPEED_WALKING_ANGLE_1;
                iWalkingAngle2 += SPEED_WALKING_ANGLE_2;
                iWalkingAngle3 += SPEED_WALKING_ANGLE_3;
                iWalkingAngle1 = iWalkingAngle1 > 360.0f ? iWalkingAngle1 - 360.0f : iWalkingAngle1;
                iWalkingAngle2 = iWalkingAngle2 > 360.0f ? iWalkingAngle2 - 360.0f : iWalkingAngle2;
                iWalkingAngle3 = iWalkingAngle3 > 360.0f ? iWalkingAngle3 - 360.0f : iWalkingAngle3;
            }

            //backwards
            if ( Keys.keyHoldWalkDown )
            {
                //change character's position
                iCylinder.getTarget().x = iCylinder.getTarget().x + LibMath.sinDeg( iView.rotZ ) * SPEED_WALKING;
                iCylinder.getTarget().y = iCylinder.getTarget().y + LibMath.cosDeg( iView.rotZ ) * SPEED_WALKING;

                //increase walkY-axis-angles
                //walkingAngle1 += CHARACTER_WALKING_ANGLE_1_SPEED;
                iWalkingAngle2 += SPEED_WALKING_ANGLE_2;
                iWalkingAngle3 += SPEED_WALKING_ANGLE_3;
                //walkingAngle1 = walkingAngle1 > 360.0f ? walkingAngle1 - 360.0f : walkingAngle1;
                iWalkingAngle2 = iWalkingAngle2 > 360.0f ? iWalkingAngle2 - 360.0f : iWalkingAngle2;
                iWalkingAngle3 = iWalkingAngle3 > 360.0f ? iWalkingAngle3 - 360.0f : iWalkingAngle3;

                //decrease walkY-axis-angle
                iWalkingAngle1 -= SPEED_WALKING_ANGLE_1;
                //walkingAngle2 -= CHARACTER_WALKING_ANGLE_2_SPEED;
                //walkingAngle3 -= CHARACTER_WALKING_ANGLE_3_SPEED;
                iWalkingAngle1 = iWalkingAngle1 < 0.0f ? iWalkingAngle1 + 360.0f : iWalkingAngle1;
                //walkingAngle2 = walkingAngle2 < 0.0f ? walkingAngle2 + 360.0f : walkingAngle2;
                //walkingAngle3 = walkingAngle3 < 0.0f ? walkingAngle3 + 360.0f : walkingAngle3;
            }

            //left
            if ( Keys.keyHoldTurnLeft || Keys.keyHoldStrafeLeft )
            {
                if ( Keys.keyHoldAlternate || Keys.keyHoldStrafeLeft )
                {
                    iCylinder.getTarget().x = iCylinder.getTarget().x - LibMath.cosDeg( iView.rotZ ) * SPEED_STRAFING;
                    iCylinder.getTarget().y = iCylinder.getTarget().y + LibMath.sinDeg( iView.rotZ ) * SPEED_STRAFING;
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
                    iCylinder.getTarget().x = iCylinder.getTarget().x + LibMath.cosDeg( iView.rotZ ) * SPEED_STRAFING;
                    iCylinder.getTarget().y = iCylinder.getTarget().y - LibMath.sinDeg( iView.rotZ ) * SPEED_STRAFING;
                }
                else
                {
                }
            }
        }

        private void handleKeysForActions()
        {
            //gain health
            if ( Keys.keyPressedGainHealth )
            {
                Keys.keyPressedGainHealth = false;

                //fill health!

                //heal player
                heal( 10 );
            }

            //show damage fx
            if ( Keys.keyPressedDamageFx )
            {
                Keys.keyPressedDamageFx = false;

                //hurt player
                hurt( 10 );

            }

            //launch a shot
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
            }
            else
            {
                //stop launching the shot
                ShooterMainThread.launchShot = false;
            }

            //launch reload
            if ( Keys.keyPressedReload )
            {
                //Debug.out( "key fire! PRESSED!!!" );

                //launch the shot!
                ShooterMainThread.launchReload = true;
            }

            //launch an action
            if ( Keys.keyPressedPlayerAction )
            {
                //check action blocker
                if ( iNextPlayerActionMillis <= System.currentTimeMillis() )
                {
                    //set timestamp for next allowed player action
                    iNextPlayerActionMillis = System.currentTimeMillis() + ShooterSettings.Performance.DELAY_AFTER_PLAYER_ACTION;

                    //trigger the action
                    ShooterMainThread.launchPlayerAction   = true;
                }
            }
        }

        public final Cylinder getCylinder()
        {
            return iCylinder;
        }

        public final void performFloorChange()
        {
            //no gravity no performing
            if ( iDisableGravity ) return;

            //browse all faces and set the player to the highest!
            Float highestZ = Level.current().getHighestFloor( iCylinder.getCenterHorz() );
            ShooterDebugSystem.floorChange.out( "highest floor is: [" + highestZ + "]" );

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
            if ( ShooterSettings.INVISIBLE_Z_ZERO_LAYER )
            {
                if ( iCylinder.getAnchor().z < 0.0f ) iCylinder.getAnchor().z = 0.0f;
            }
        }

        public final void drawDebugLog( Graphics2D g )
        {
            //int y = GLPanel.PANEL_HEIGHT - Offset.EBorderHudY;
            int y = Offset.EBorderHudY + 90;

            //HUD.releaseClip( g );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posX:  " + iCylinder.getAnchor().x,  Offset.EBorderHudX, y - 80 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posY:  " + iCylinder.getAnchor().y,  Offset.EBorderHudX, y - 70 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posZ:  " + iCylinder.getAnchor().z,  Offset.EBorderHudX, y - 60 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotX:  " + iView.rotX,               Offset.EBorderHudX, y - 50 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotY:  " + iView.rotY,               Offset.EBorderHudX, y - 40 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotZ:  " + iView.rotZ,               Offset.EBorderHudX, y - 30 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk1: " + iWalkingAngle1,           Offset.EBorderHudX, y - 20 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk2: " + iWalkingAngle2,           Offset.EBorderHudX, y - 10 );
            LibStrings.drawString( g, LibColors.EWhite.colARGB, LibColors.EBlack.colARGB, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk3: " + iWalkingAngle3,           Offset.EBorderHudX, y - 0 );
        }

        public final Shot getShot()
        {
            return new Shot
            (
                Shot.ShotOrigin.EPlayer,
                false,
                getWearpon().getCurrentIrregularityAngle(),
                getWearpon().getCurrentIrregularityDepth(),
                iCylinder.getAnchor().x,
                iCylinder.getAnchor().y,
                iCylinder.getAnchor().z + Level.currentPlayer().iView.iDepthHand,
                Level.currentPlayer().iView.rotZ,
                iView.rotX,
                getWearpon().getShotRange(),
                getWearpon().getBulletHoleSize()
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
                        if ( Level.currentPlayer().getWearpon() == null )
                        {
                            Level.currentPlayer().setWearpon( iWearpons.elementAt( i ) );
                            break;
                        }
                        else if (  Level.currentPlayer().getWearpon() == iWearpons.elementAt( i ) )
                        {
                            int previousIndex = i - 1;
                            if ( previousIndex < 0 )
                            {
                                //only change to gadgets if gadgets are available
                                if ( iGadgets.size() > 0 )
                                {
                                    //pick last gadget
                                    Level.currentPlayer().changeItemGroup( HUD.ItemGroup.EGadget );
                                    Level.currentPlayer().setGadget( iGadgets.elementAt( iGadgets.size() - 1 ) );
                                    HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                                }
                                else
                                {
                                    //pick last wearpon
                                    Level.currentPlayer().setWearpon( iWearpons.elementAt( iWearpons.size() - 1 ) );

                                    //animate only if more than one wearpon is available!
                                    if ( iWearpons.size() > 1 )
                                    {
                                        ShooterDebugSystem.bugfix.out("animate!");
                                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                                    }
                                }
                            }
                            else
                            {
                                Level.currentPlayer().setWearpon( iWearpons.elementAt( previousIndex ) );
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
                        if ( Level.currentPlayer().getGadget() == null )
                        {
                            Level.currentPlayer().setGadget( iGadgets.elementAt( i ) );
                            break;
                        }
                        else if ( Level.currentPlayer().getGadget() == iGadgets.elementAt( i ) )
                        {
                            int previousIndex = i - 1;
                            if ( previousIndex < 0 )
                            {
                                Level.currentPlayer().changeItemGroup( HUD.ItemGroup.EWearpon );
                                Level.currentPlayer().setWearpon( iWearpons.elementAt( iWearpons.size() - 1 ) );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Level.currentPlayer().setGadget( iGadgets.elementAt( previousIndex ) );
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
                        if ( Level.currentPlayer().getWearpon() == null )
                        {
                            Level.currentPlayer().setWearpon( iWearpons.elementAt( i ) );
                            break;
                        }
                        else if ( Level.currentPlayer().getWearpon() == iWearpons.elementAt( i ) )
                        {
                            int nextIndex = i + 1;
                            if ( nextIndex >= iWearpons.size() )
                            {
                                //only change to gadgets if gadgets are available
                                if ( iGadgets.size() > 0 )
                                {
                                    //pick first gadget
                                    Level.currentPlayer().changeItemGroup( HUD.ItemGroup.EGadget );
                                    Level.currentPlayer().setGadget( iGadgets.elementAt( 0 ) );
                                    HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                                }
                                else
                                {
                                    //pick first wearpon
                                    Level.currentPlayer().setWearpon( iWearpons.elementAt( 0 ) );

                                    //animate only if more than one wearpon is available!
                                    if ( iWearpons.size() > 1 )
                                    {
                                        ShooterDebugSystem.bugfix.out("animate!");
                                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                                    }
                                }
                            }
                            else
                            {
                                Level.currentPlayer().setWearpon( iWearpons.elementAt( nextIndex ) );
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
                        if ( Level.currentPlayer().getGadget() == null )
                        {
                            Level.currentPlayer().setGadget( iGadgets.elementAt( i ) );
                            break;
                        }
                        else if ( Level.currentPlayer().getGadget() == iGadgets.elementAt( i ) )
                        {
                            int nextIndex = i + 1;
                            if ( nextIndex >= iGadgets.size() )
                            {
                                Level.currentPlayer().changeItemGroup( HUD.ItemGroup.EWearpon );
                                Level.currentPlayer().setWearpon( iWearpons.elementAt( 0 ) );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Level.currentPlayer().setGadget( iGadgets.elementAt( nextIndex ) );
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
            return LibMath.sinDeg( iWalkingAngle1 );
        }
        public final float getWalkingAngleCarriedModifierX()
        {
            return LibMath.sinDeg( iWalkingAngle2 );
        }
        public final float getWalkingAngleCarriedModifierY()
        {
            return LibMath.sinDeg( iWalkingAngle3 );
        }

        public final void hurt( int descent )
        {
            //player can only lose energy if he is not dying
            if ( !iDying )
            {
                //substract damage - clip rock bottom
                health -= descent;
                if ( health < 0.0f )
                {
                    health = 0.0f;
                    kill();
                }

                //start red screen anim
                HUDFx.launchDamageFX( descent );
            }
        }

        public final void heal( int gain )
        {
            //player can only be healed if it is not too late
            if ( !iDying )
            {
                //substract damage - clip rock bottom
                health += gain;
                if ( health > MAX_HEALTH )
                {
                    health = MAX_HEALTH;
                }

                //start healing screen anim
                HUDFx.launchHealthFX( gain );
            }
        }

        public final void launchAction()
        {
            ShooterDebugSystem.playerAction.out( "launchAction()" );

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
            // the player is being fired

            return null;
        }

        public final void drawStandingCircle()
        {
            iCylinder.drawStandingCircle();
        }

        public final float getHealth()
        {
            return health;
        }

        public final ViewSet getCameraPositionAndRotation()
        {
            //3rd person camera?
            float modX = ( !ShooterSettings.ENABLE_3RD_PERSON_CAMERA ? 0.0f : LibMath.sinDeg( Level.currentPlayer().iView.rotZ ) * 2.0f );
            float modY = ( !ShooterSettings.ENABLE_3RD_PERSON_CAMERA ? 0.0f : LibMath.cosDeg( Level.currentPlayer().iView.rotZ ) * 2.0f );
            float modZ = Level.currentPlayer().iCylinder.getAnchor().z + Level.currentPlayer().iView.iDepthEye + Level.currentPlayer().getWalkingAngleModifier() * PlayerAttributes.AMP_WALKING_Z;

            float posX = 0.0f;
            float posY = 0.0f;
            float posZ = 0.0f;

            //check if user is dying
            if ( Level.currentPlayer().iDying )
            {
                posX = -Level.currentPlayer().iCylinder.getAnchor().x;
                posZ = -Level.currentPlayer().iCylinder.getAnchor().z - PlayerAttributes.DEPTH_DEATH - ( Level.currentPlayer().iView.iDepthEye - Level.currentPlayer().iView.dieModTransZ );
                posY = -Level.currentPlayer().iCylinder.getAnchor().y;
            }
            else
            {
                posX = -Level.currentPlayer().iCylinder.getAnchor().x - modX;
                posZ = -Level.currentPlayer().iCylinder.getAnchor().z - modZ - Level.currentPlayer().iView.dieModTransZ;
                posY = -Level.currentPlayer().iCylinder.getAnchor().y - modY;
            }

            return new ViewSet
            (
                posX,
                posY,
                posZ,
                Level.currentPlayer().iView.rotX + Level.currentPlayer().iView.dieModX,
                Level.currentPlayer().iView.rotY + Level.currentPlayer().iView.dieModY,
                Level.currentPlayer().iView.rotZ - Level.currentPlayer().iView.dieModZ
            );
        }

        /***********************************************************************************
        *   What would this method possibly do to the player?
        ***********************************************************************************/
        public final void kill()
        {
            health  = 0;
            iDying   = true;
        }

        public final boolean isDying()
        {
            return iDying;
        }

        public final Ammo getAmmo()
        {
            return iAmmo;
        }

        public final View getView()
        {
            return iView;
        }

        public final void moveToNewPosition()
        {
            getCylinder().moveToTargetPosition( getView().iDepthTotal );
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
