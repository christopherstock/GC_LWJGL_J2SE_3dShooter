/*  $Id: Player.java 189 2010-12-13 20:02:19Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.player;

    import  java.awt.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   A player. Can be the user or a bot ( in future .. ) ?
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Player extends GameObject implements PlayerAttributes
    {
        /**************************************************************************************
        *   The player's collision cylinger represents his position and hiscollision body.
        **************************************************************************************/
        public                      Cylinder        cylinder                    = null;

        /**************************************************************************************
        *   Currently holded wearpon.
        **************************************************************************************/
        private                     Wearpon         wearpon                     = Wearpon.EAssaultRifle;    //current holded wearpon

        /**************************************************************************************
        *   Currently holded gadget.
        **************************************************************************************/
        private                     Gadget          gadget                      = Gadget.EEnvelope;         //current holded gadget

        /**************************************************************************************
        *   Specifies if the player is currently holding a wearpon or a gadget.
        **************************************************************************************/
        private                     HUD.ItemGroup   currentItemGroup            = HUD.ItemGroup.EWearpon;

        public                      View            view                        = null;
        public                      Ammo            ammo                        = null;

        private                     float           walkingAngle1               = 0.0f;             //X-axis-angle on walking
        private                     float           walkingAngle2               = 0.0f;             //X-axis-angle on walking
        private                     float           walkingAngle3               = 0.0f;             //X-axis-angle on walking

        public                      boolean         dying                       = false;

        private                     float           currentSpeedFalling         = SPEED_FALLING_MIN;    //current falling-speed ( will increase exponential )

        private                     long            nextPlayerActionMillis      = 0;

        /**************************************************************************************
        *   Player's health. 100 is new-born. 0 is dead.
        **************************************************************************************/
        private                     float           health                      = 100.0f;

        /**************************************************************************************
        *   Disables all gravity checks.
        **************************************************************************************/
        private                     boolean         iDisableGravity             = false;

        public Player( ViewSet aStartPosition, boolean aDisableGravity )
        {
            super( GameObject.HitPointCarrier.EPlayer );

            //init and set cylinder
            cylinder    = new Cylinder( this, new LibVertex( aStartPosition.posX, aStartPosition.posY, aStartPosition.posZ ), RADIUS, DEPTH_TOTAL_STANDING );
            view        = new View(     this, aStartPosition.rotX, aStartPosition.rotY, aStartPosition.rotZ );
            ammo        = new Ammo();

            iDisableGravity = aDisableGravity;
        }

        public void handleKeysForView()
        {
            view.handleKeysForView();
        }

        /**************************************************************************************
        *   Affects a game-key per game-tick and alters the Player's values.
        **************************************************************************************/
        public void handleKeysForMovement()
        {
            //set current position as the target-position
            cylinder.setTargetPosition( cylinder.getAnchor().x, cylinder.getAnchor().y, cylinder.getAnchor().z );
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
                cylinder.getTarget().x = cylinder.getTarget().x - LibMath.sinDeg( view.rotZ ) * SPEED_WALKING;
                cylinder.getTarget().y = cylinder.getTarget().y - LibMath.cosDeg( view.rotZ ) * SPEED_WALKING;

                //increase walkY-axis-angles
                walkingAngle1 += SPEED_WALKING_ANGLE_1;
                walkingAngle2 += SPEED_WALKING_ANGLE_2;
                walkingAngle3 += SPEED_WALKING_ANGLE_3;
                walkingAngle1 = walkingAngle1 > 360.0f ? walkingAngle1 - 360.0f : walkingAngle1;
                walkingAngle2 = walkingAngle2 > 360.0f ? walkingAngle2 - 360.0f : walkingAngle2;
                walkingAngle3 = walkingAngle3 > 360.0f ? walkingAngle3 - 360.0f : walkingAngle3;
            }

            //backwards
            if ( Keys.keyHoldWalkDown )
            {
                //change character's position
                cylinder.getTarget().x = cylinder.getTarget().x + LibMath.sinDeg( view.rotZ ) * SPEED_WALKING;
                cylinder.getTarget().y = cylinder.getTarget().y + LibMath.cosDeg( view.rotZ ) * SPEED_WALKING;

                //increase walkY-axis-angles
                //walkingAngle1 += CHARACTER_WALKING_ANGLE_1_SPEED;
                walkingAngle2 += SPEED_WALKING_ANGLE_2;
                walkingAngle3 += SPEED_WALKING_ANGLE_3;
                //walkingAngle1 = walkingAngle1 > 360.0f ? walkingAngle1 - 360.0f : walkingAngle1;
                walkingAngle2 = walkingAngle2 > 360.0f ? walkingAngle2 - 360.0f : walkingAngle2;
                walkingAngle3 = walkingAngle3 > 360.0f ? walkingAngle3 - 360.0f : walkingAngle3;

                //decrease walkY-axis-angle
                walkingAngle1 -= SPEED_WALKING_ANGLE_1;
                //walkingAngle2 -= CHARACTER_WALKING_ANGLE_2_SPEED;
                //walkingAngle3 -= CHARACTER_WALKING_ANGLE_3_SPEED;
                walkingAngle1 = walkingAngle1 < 0.0f ? walkingAngle1 + 360.0f : walkingAngle1;
                //walkingAngle2 = walkingAngle2 < 0.0f ? walkingAngle2 + 360.0f : walkingAngle2;
                //walkingAngle3 = walkingAngle3 < 0.0f ? walkingAngle3 + 360.0f : walkingAngle3;
            }

            //left
            if ( Keys.keyHoldTurnLeft || Keys.keyHoldStrafeLeft )
            {
                if ( Keys.keyHoldAlternate || Keys.keyHoldStrafeLeft )
                {
                    cylinder.getTarget().x = cylinder.getTarget().x - LibMath.cosDeg( view.rotZ ) * SPEED_STRAFING;
                    cylinder.getTarget().y = cylinder.getTarget().y + LibMath.sinDeg( view.rotZ ) * SPEED_STRAFING;
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
                    cylinder.getTarget().x = cylinder.getTarget().x + LibMath.cosDeg( view.rotZ ) * SPEED_STRAFING;
                    cylinder.getTarget().y = cylinder.getTarget().y - LibMath.sinDeg( view.rotZ ) * SPEED_STRAFING;
                }
                else
                {
                }
            }
        }

        public void handleKeysForActions()
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
                MainThread.launchShot = true;

                //release the key if the gun requires this
                if ( wearpon.getCurrentShotNeedsKeyRelease() )
                {
                    //key-release required before next shot will be launched :)
                    Keys.keyHoldFire = false;
                }
            }
            else
            {
                //stop launching the shot
                MainThread.launchShot = false;
            }

            //launch an action
            if ( Keys.keyPressedPlayerAction )
            {
                //check action blocker
                if ( nextPlayerActionMillis <= System.currentTimeMillis() )
                {
                    //set timestamp for next allowed player action
                    nextPlayerActionMillis = System.currentTimeMillis() + ShooterSettings.DELAY_AFTER_PLAYER_ACTION;

                    //trigger the action
                    MainThread.launchPlayerAction   = true;
                }
            }
        }

        public final void performFloorChange()
        {
            //no gravity no performing
            if ( iDisableGravity ) return;

            //browse all faces and set the player to the highest!
            Float highestZ = Level.current().getHighestFloor( cylinder.getCenterHorz() );
            Debug.floorChange.out( "highest floor is: [" + highestZ + "]" );

            //check if the player is falling - directly alter his anchor-z-value!
            if ( highestZ == null )
            {
                if ( currentSpeedFalling > SPEED_FALLING_MAX ) currentSpeedFalling = SPEED_FALLING_MAX;
                cylinder.getAnchor().z  -= currentSpeedFalling;
                currentSpeedFalling     *= SPEED_FALLING_MULTIPLIER;
            }
            else
            {
                currentSpeedFalling     = SPEED_FALLING_MIN;
                cylinder.getAnchor().z  = highestZ.floatValue() + DEPTH_TOE;
            }

            //Debug.info( "falling speed: " + currentSpeedFalling );

            //check invisible z-0-layer
            if ( ShooterSettings.INVISIBLE_Z_ZERO_LAYER )
            {
                if ( cylinder.getAnchor().z < 0.0f ) cylinder.getAnchor().z = 0.0f;
            }
        }

        public final void drawDebugLog( Graphics2D g )
        {
            //int y = GLPanel.PANEL_HEIGHT - Offset.EBorderHudY;
            int y = Offset.EBorderHudY + 90;

            //HUD.releaseClip( g );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posX:  " + cylinder.getAnchor().x,  Offset.EBorderHudX, y - 80 );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posY:  " + cylinder.getAnchor().y,  Offset.EBorderHudX, y - 70 );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "posZ:  " + cylinder.getAnchor().z,  Offset.EBorderHudX, y - 60 );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotX:  " + view.rotX,               Offset.EBorderHudX, y - 50 );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotY:  " + view.rotY,               Offset.EBorderHudX, y - 40 );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "rotZ:  " + view.rotZ,               Offset.EBorderHudX, y - 30 );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk1: " + walkingAngle1,           Offset.EBorderHudX, y - 20 );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk2: " + walkingAngle2,           Offset.EBorderHudX, y - 10 );
            LibStrings.drawString( g, LibColors.EWhite.col, LibColors.EBlack.col, null, Fonts.EDebug, LibAnchor.EAnchorLeftBottom, "walk3: " + walkingAngle3,           Offset.EBorderHudX, y - 0 );
        }

        public final Shot getShot()
        {
            return new Shot
            (
                Shot.ShotOrigin.EPlayer,
                false,
                getWearpon().getCurrentIrregularityAngle(),
                getWearpon().getCurrentIrregularityDepth(),
                cylinder.getAnchor().x,
                cylinder.getAnchor().y,
                cylinder.getAnchor().z + Level.currentPlayer().view.depthHand,
                Level.currentPlayer().view.rotZ,
                view.rotX,
                getWearpon().getShotRange(),
                getWearpon().getBulletHoleSize()
            );
        }

        public final Wearpon getWearpon()
        {
            return wearpon;
        }

        public final Gadget getGadget()
        {
            return gadget;
        }

        public final void setWearpon( Wearpon newWearpon )
        {
            wearpon = newWearpon;
        }

        public final void setGadget( Gadget newGadget )
        {
            gadget = newGadget;
        }

        public final void drawWearponOrGadget()
        {
            //draw gun & ammo
            switch ( currentItemGroup )
            {
                case EWearpon:
                {
                    wearpon.drawOrtho();
//                    wearpon.drawAmmo( g );
                    break;
                }
                case EGadget:
                {
                    gadget.drawOrtho();
                    break;
                }
            }
        }

        public final void choosePreviousWearponOrGadget()
        {
            switch ( currentItemGroup )
            {
                case EWearpon:
                {
                    Wearpon[] wearpons = Wearpon.values();
                    for ( int i = 0; i < wearpons.length; ++i )
                    {
                        if ( Level.currentPlayer().getWearpon() == wearpons[ i ] )
                        {
                            int previousIndex = Level.currentPlayer().getWearpon().ordinal() - 1;
                            if ( previousIndex < 0 )
                            {
                                Level.currentPlayer().changeItemGroup( HUD.ItemGroup.EGadget );
                                Level.currentPlayer().setGadget( Gadget.values()[ Gadget.values().length - 1 ] );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Level.currentPlayer().setWearpon( wearpons[ previousIndex ] );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            return;
                        }
                    }
                    break;
                }
                case EGadget:
                {
                    Gadget[] gadgets = Gadget.values();
                    for ( int i = 0; i < gadgets.length; ++i )
                    {
                        if ( Level.currentPlayer().getGadget() == gadgets[ i ] )
                        {
                            int previousIndex = Level.currentPlayer().getGadget().ordinal() - 1;
                            if ( previousIndex < 0 )
                            {
                                Level.currentPlayer().changeItemGroup( HUD.ItemGroup.EWearpon );
                                Level.currentPlayer().setWearpon( Wearpon.values()[ Wearpon.values().length - 1 ] );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Level.currentPlayer().setGadget( gadgets[ previousIndex ] );
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
            switch ( currentItemGroup )
            {
                case EWearpon:
                {
                    Wearpon[] wearpons = Wearpon.values();
                    for ( int i = 0; i < wearpons.length; ++i )
                    {
                        if ( Level.currentPlayer().getWearpon() == wearpons[ i ] )
                        {
                            int nextIndex = Level.currentPlayer().getWearpon().ordinal() + 1;
                            if ( nextIndex >= wearpons.length )
                            {
                                Level.currentPlayer().changeItemGroup( HUD.ItemGroup.EGadget );
                                Level.currentPlayer().setGadget( Gadget.values()[ 0 ] );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Level.currentPlayer().setWearpon( wearpons[ nextIndex ] );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            return;
                        }
                    }
                    break;
                }
                case EGadget:
                {
                    Gadget[] gadgets = Gadget.values();
                    for ( int i = 0; i < gadgets.length; ++i )
                    {
                        if ( Level.currentPlayer().getGadget() == gadgets[ i ] )
                        {
                            int nextIndex = Level.currentPlayer().getGadget().ordinal() + 1;
                            if ( nextIndex >= gadgets.length )
                            {
                                Level.currentPlayer().changeItemGroup( HUD.ItemGroup.EWearpon );
                                Level.currentPlayer().setWearpon( Wearpon.values()[ 0 ] );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Level.currentPlayer().setGadget( gadgets[ nextIndex ] );
                                HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            return;
                        }
                    }
                    break;
                }
            }
        }

        public final void orderNextWearponOrGadget()
        {
            switch ( currentItemGroup )
            {
                case EWearpon:
                {
                    //if no animation is active
                    if ( !HUD.singleton.animationActive() )
                    {
                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionPrevious );
                    }
                    break;
                }
                case EGadget:
                {
                    //if no animation is active
                    if ( !HUD.singleton.animationActive() )
                    {
                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionPrevious );
                    }
                    break;
                }
            }
        }

        public final void orderPreviousWearponOrGadget()
        {
            switch ( currentItemGroup )
            {
                case EWearpon:
                {
                    //if no animation is active
                    if ( !HUD.singleton.animationActive() )
                    {
                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionNext );
                    }
                    break;
                }
                case EGadget:
                {
                    //if no animation is active
                    if ( !HUD.singleton.animationActive() )
                    {
                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionNext );
                    }
                    break;
                }
            }
        }

        public final void handleWearponOrGadget()
        {
            switch ( currentItemGroup )
            {
                case EWearpon:
                {
                    wearpon.handleWearpon();
                    break;
                }
                case EGadget:
                {
                    gadget.handleGadget();
                    break;
                }
            }
        }

        public final void changeItemGroup( HUD.ItemGroup newItemGroup )
        {
            currentItemGroup = newItemGroup;
        }

        /***********************************************************************************
        *
        *   @return     A value from -1.0 to 1.0.
        ***********************************************************************************/
        public final float getWalkingAngleModifier()
        {
            return LibMath.sinDeg( walkingAngle1 );
        }
        public final float getWalkingAngleCarriedModifierX()
        {
            return LibMath.sinDeg( walkingAngle2 );
        }
        public final float getWalkingAngleCarriedModifierY()
        {
            return LibMath.sinDeg( walkingAngle3 );
        }

        public final void hurt( int descent )
        {
            //player can only lose energy if he is not dying
            if ( !dying )
            {
                //substract damage - clip rock bottom
                health -= descent;
                if ( health < 0.0f )
                {
                    health = 0.0f;
                    kill();
                }

                //start red screen anim
                HUD.singleton.launchDamageFX( descent );
            }
        }

        public final void heal( int gain )
        {
            //player can only be healed if it is not too late
            if ( !dying )
            {
                //substract damage - clip rock bottom
                health += gain;
                if ( health > MAX_HEALTH )
                {
                    health = MAX_HEALTH;
                }

                //start healing screen anim
                HUD.singleton.launchHealthFX( gain );
            }
        }

        public final void launchAction()
        {
            Debug.playerAction.out( "launchAction()" );

            //launch the action on the level
            Level.current().launchAction( cylinder );
        }

        @Override
        public final LibVertex getAnchor()
        {
            return cylinder.getAnchor();
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
            cylinder.drawStandingCircle();
        }

        public final float getHealth()
        {
            return health;
        }

        public final ViewSet getCameraPositionAndRotation()
        {
            //3rd person camera?
            float modX = ( !ShooterSettings.ENABLE_3RD_PERSON_CAMERA ? 0.0f : LibMath.sinDeg( Level.currentPlayer().view.rotZ ) * 2.0f );
            float modY = ( !ShooterSettings.ENABLE_3RD_PERSON_CAMERA ? 0.0f : LibMath.cosDeg( Level.currentPlayer().view.rotZ ) * 2.0f );
            float modZ = Level.currentPlayer().cylinder.getAnchor().z + Level.currentPlayer().view.depthEye + Level.currentPlayer().getWalkingAngleModifier() * PlayerAttributes.AMP_WALKING_Z;

            float posX = 0.0f;
            float posY = 0.0f;
            float posZ = 0.0f;

            //check if user is dying
            if ( Level.currentPlayer().dying )
            {
                posX = -Level.currentPlayer().cylinder.getAnchor().x;
                posZ = -Level.currentPlayer().cylinder.getAnchor().z - PlayerAttributes.DEPTH_DEATH - ( Level.currentPlayer().view.depthEye - Level.currentPlayer().view.dieModTransZ );
                posY = -Level.currentPlayer().cylinder.getAnchor().y;
            }
            else
            {
                posX = -Level.currentPlayer().cylinder.getAnchor().x - modX;
                posZ = -Level.currentPlayer().cylinder.getAnchor().z - modZ - Level.currentPlayer().view.dieModTransZ;
                posY = -Level.currentPlayer().cylinder.getAnchor().y - modY;
            }

            return new ViewSet
            (
                posX,
                posY,
                posZ,
                Level.currentPlayer().view.rotX + Level.currentPlayer().view.dieModX,
                Level.currentPlayer().view.rotY + Level.currentPlayer().view.dieModY,
                Level.currentPlayer().view.rotZ - Level.currentPlayer().view.dieModZ
            );
        }

        /***********************************************************************************
        *   What would this method possibly do to the player?
        ***********************************************************************************/
        public final void kill()
        {
            health  = 0;
            dying   = true;
        }
    }
