/*  $Id: Player.java,v 1.3 2007/06/05 15:44:27 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.player;

    import  java.awt.*;
    import  javax.media.opengl.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.unit.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.ui.hud.*;
    import  de.christopherstock.shooter.util.*;

    /**************************************************************************************
    *   A player. Can be the user or a bot ( in future .. ).
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class Player implements Attributes
    {
        /**************************************************************************************
         *   This is the player instance that is controlled by the user.
         **************************************************************************************/
        public      static          Player          user                        = null;

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

        public                      boolean         dying                       = false;

        private                     float           walkingAngle1               = 0.0f;             //X-axis-angle on walking
        private                     float           walkingAngle2               = 0.0f;             //X-axis-angle on walking
        private                     float           walkingAngle3               = 0.0f;             //X-axis-angle on walking

        private                     float           currentSpeedFalling         = SPEED_FALLING_MIN;    //current falling-speed ( will increase exponential )

        /**************************************************************************************
        *   Player's health. 100 is new-born. 0 is dead.
        **************************************************************************************/
        private                     int             health                      = 100;

        public Player( float initPosX, float initPosY, float initPosZ, float initRotX, float initRotY, float initRotZ )
        {
            //init and set cylinder
            cylinder    = new Cylinder( HitPoint.Carrier.EPlayer,new Vertex( initPosX, initPosY, initPosZ ), RADIUS, DEPTH_TOTAL_STANDING );
            view        = new View( initRotX, initRotY, initRotZ );
        }

        public static void initUser( float initPosX, float initPosY, float initPosZ, float initRotX, float initRotY, float initRotZ )
        {
            user = new Player( initPosX, initPosY, initPosZ, initRotX, initRotY, initRotZ );
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
            if ( Keys.keyHoldWalkUp )
            {
                //change character's position
                cylinder.getTarget().x = cylinder.getTarget().x - UMath.sinDeg( view.rotZ ) * SPEED_WALKING;
                cylinder.getTarget().y = cylinder.getTarget().y - UMath.cosDeg( view.rotZ ) * SPEED_WALKING;

                //increase walkY-axis-angles
                walkingAngle1 += SPEED_WALKING_ANGLE_1;
                walkingAngle2 += SPEED_WALKING_ANGLE_2;
                walkingAngle3 += SPEED_WALKING_ANGLE_3;
                walkingAngle1 = walkingAngle1 > 360.0f ? walkingAngle1 - 360.0f : walkingAngle1;
                walkingAngle2 = walkingAngle2 > 360.0f ? walkingAngle2 - 360.0f : walkingAngle2;
                walkingAngle3 = walkingAngle3 > 360.0f ? walkingAngle3 - 360.0f : walkingAngle3;
            }

            if ( Keys.keyHoldWalkDown )
            {
                //change character's position
                cylinder.getTarget().x = cylinder.getTarget().x + UMath.sinDeg( view.rotZ ) * SPEED_WALKING;
                cylinder.getTarget().y = cylinder.getTarget().y + UMath.cosDeg( view.rotZ ) * SPEED_WALKING;

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
                Keys.keyPressedPlayerAction     = false;

                MainThread.launchPlayerAction   = true;
            }
        }

        public final void performFloorChange()
        {
            //no gravity no performing
            if ( Shooter.DISABLE_GRAVITY ) return;

            //browse all faces and set the player to the highest!
            Float highestFloor = Level.getHighestFloor( cylinder.getCenterHorz() );
            Debug.floorChange.out( "highest floor is: [" + highestFloor + "]" );

            //check if the player is falling - directly alter his anchor-z-value!
            if ( highestFloor == null )
            {
                if ( currentSpeedFalling > SPEED_FALLING_MAX ) currentSpeedFalling = SPEED_FALLING_MAX;
                cylinder.getAnchor().z  -= currentSpeedFalling;
                currentSpeedFalling     *= SPEED_FALLING_MULTIPLIER;
            }
            else
            {
                currentSpeedFalling     = SPEED_FALLING_MIN;
                cylinder.getAnchor().z  = highestFloor.floatValue() + DEPTH_TOE;
            }

            //Debug.info( "falling speed: " + currentSpeedFalling );

            //check invisible z-0-layer
            if ( Shooter.INVISIBLE_Z_ZERO_LAYER )
            {
                if ( cylinder.getAnchor().z < 0.0f ) cylinder.getAnchor().z = 0.0f;
            }
        }

        public final void drawDebugLog( Graphics2D g )
        {
            //int y = GLPanel.PANEL_HEIGHT - Offset.EBorderHudY;
            int y = Offset.EBorderHudY + 90;

            GLPanel.releaseClip( g );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "posX:  " + cylinder.getAnchor().x,  Offset.EBorderHudX, y - 80 );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "posY:  " + cylinder.getAnchor().y,  Offset.EBorderHudX, y - 70 );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "posZ:  " + cylinder.getAnchor().z,  Offset.EBorderHudX, y - 60 );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "rotX:  " + view.rotX,               Offset.EBorderHudX, y - 50 );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "rotY:  " + view.rotY,               Offset.EBorderHudX, y - 40 );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "rotZ:  " + view.rotZ,               Offset.EBorderHudX, y - 30 );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "walk1: " + walkingAngle1,           Offset.EBorderHudX, y - 20 );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "walk2: " + walkingAngle2,           Offset.EBorderHudX, y - 10 );
            Strings.drawString( g, Colors.EWhite.col, Colors.EBlack.col, null, Fonts.EDebug, Anchor.EAnchorLeftBottom, "walk3: " + walkingAngle3,           Offset.EBorderHudX, y - 0 );

        }

        public static void setPlayerCamera( GL gl )
        {
            gl.glLoadIdentity();                    //new identity please
            gl.glNormal3f(  0.0f, 0.0f, 0.0f );     //normalize

            //modify player's posZ by its height and walking-modifier
            float walkModZ = user.cylinder.getAnchor().z + user.view.depthEye + user.getWalkingAngle1Modifier() * AMP_WALKING_Z;

            gl.glRotatef(       user.view.rotY + user.view.dieModY,          0.0f, 0.0f, 1.0f        );      //rotate z
            gl.glRotatef(       user.view.rotX + user.view.dieModX,          1.0f, 0.0f, 0.0f        );      //rotate x
            gl.glRotatef(       360.0f - user.view.rotZ - user.view.dieModZ, 0.0f, 1.0f, 0.0f        );      //rotate y

            //3rd person camera?
            float modX = 0.0f;
            float modY = 0.0f;
            if ( Shooter.ENABLE_3RD_PERSON_CAMERA )
            {
                modX = UMath.sinDeg( user.view.rotZ ) * 2.0f;
                modY = UMath.cosDeg( user.view.rotZ ) * 2.0f;
            }

            //check if user is dying
            if ( user.dying )
            {
                gl.glTranslatef(    -user.cylinder.getAnchor().x, -user.cylinder.getAnchor().z - DEPTH_DEATH - ( user.view.depthEye - user.view.dieModTransZ ), -user.cylinder.getAnchor().y   );      //translate
            }
            else
            {
                //gl.glTranslatef(    -user.cylinder.getAnchor().x, -user.cylinder.getAnchor().z - walkModZ - user.dieModTransZ, -user.cylinder.getAnchor().y   );      //translate
                gl.glTranslatef(    -user.cylinder.getAnchor().x - modX, -user.cylinder.getAnchor().z - walkModZ - user.view.dieModTransZ, -user.cylinder.getAnchor().y - modY );      //translate
            }
        }

        public static void drawSceneBg( GL gl )
        {
            //assign texture's position on the bg-face
            float   texX        = ( 360.0f - user.view.rotZ ) * 1.0f / 360.0f;
            float   positionY   = -0.95f;

            float   val2        = 2.5f;
            //float   val2        = 2.2f;
            float   val1        = 2.2f;

            //draw bg-face?

            gl.glLoadIdentity();                        //new identity please
            gl.glNormal3f(  0.0f, 0.0f, 0.0f    );      //normalize

            gl.glEnable(    GL.GL_TEXTURE_2D    );      //enable texture-mapping
            gl.glColor3f(   1.0f, 1.0f, 1.0f    );      //reset colors
            gl.glBindTexture( GL.GL_TEXTURE_2D, Texture.EBgDesert.ordinal() );

            gl.glDepthMask( false );                    //disable the depth-mask

            gl.glBegin( GL.GL_QUADS );                  //begin drawing
            gl.glTexCoord2f(    texX,           0.0f );   gl.glVertex3f(      -val1,     positionY,              -3.75f );
            gl.glTexCoord2f(    texX + 0.2f,    0.0f );   gl.glVertex3f(       val1,     positionY,              -3.75f );
            gl.glTexCoord2f(    texX + 0.2f,    1.0f );   gl.glVertex3f(       val1,     positionY + val2,       -3.75f );
            gl.glTexCoord2f(    texX,           1.0f );   gl.glVertex3f(      -val1,     positionY + val2,       -3.75f );
            gl.glEnd();                                 //finish drawing

            gl.glDepthMask( true );                     //enable the depth-mask
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
                cylinder.getAnchor().z + user.view.depthHand,
                user.view.rotZ,
                view.rotX,
                getWearpon().getShotRange(),
                true
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

        public final void drawWearponOrGadget( Graphics2D g )
        {
            //draw gun & ammo
            switch ( currentItemGroup )
            {
                case EWearpon:
                {
                    wearpon.draw2D( g );
                    wearpon.drawAmmo( g );
                    break;
                }
                case EGadget:
                {
                    gadget.draw2D( g );
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
                        if ( Player.user.getWearpon() == wearpons[ i ] )
                        {
                            int previousIndex = Player.user.getWearpon().ordinal() - 1;
                            if ( previousIndex < 0 )
                            {
                                Player.user.changeItemGroup( HUD.ItemGroup.EGadget );
                                Player.user.setGadget( Gadget.values()[ Gadget.values().length - 1 ] );
                                HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Player.user.setWearpon( wearpons[ previousIndex ] );
                                HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );
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
                        if ( Player.user.getGadget() == gadgets[ i ] )
                        {
                            int previousIndex = Player.user.getGadget().ordinal() - 1;
                            if ( previousIndex < 0 )
                            {
                                Player.user.changeItemGroup( HUD.ItemGroup.EWearpon );
                                Player.user.setWearpon( Wearpon.values()[ Wearpon.values().length - 1 ] );
                                HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Player.user.setGadget( gadgets[ previousIndex ] );
                                HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );
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
                        if ( Player.user.getWearpon() == wearpons[ i ] )
                        {
                            int nextIndex = Player.user.getWearpon().ordinal() + 1;
                            if ( nextIndex >= wearpons.length )
                            {
                                Player.user.changeItemGroup( HUD.ItemGroup.EGadget );
                                Player.user.setGadget( Gadget.values()[ 0 ] );
                                HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Player.user.setWearpon( wearpons[ nextIndex ] );
                                HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );
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
                        if ( Player.user.getGadget() == gadgets[ i ] )
                        {
                            int nextIndex = Player.user.getGadget().ordinal() + 1;
                            if ( nextIndex >= gadgets.length )
                            {
                                Player.user.changeItemGroup( HUD.ItemGroup.EWearpon );
                                Player.user.setWearpon( Wearpon.values()[ 0 ] );
                                HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );
                            }
                            else
                            {
                                Player.user.setGadget( gadgets[ nextIndex ] );
                                HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );
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
                    if ( !HUD.animationActive() )
                    {
                        HUD.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionPrevious );
                    }
                    break;
                }
                case EGadget:
                {
                    //if no animation is active
                    if ( !HUD.animationActive() )
                    {
                        HUD.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionPrevious );
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
                    if ( !HUD.animationActive() )
                    {
                        HUD.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionNext );
                    }
                    break;
                }
                case EGadget:
                {
                    //if no animation is active
                    if ( !HUD.animationActive() )
                    {
                        HUD.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionNext );
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
        public final float getWalkingAngle1Modifier()
        {
            return UMath.sinDeg( walkingAngle1 );
        }
        public final float getWalkingAngle2Modifier()
        {
            return UMath.sinDeg( walkingAngle2 );
        }
        public final float getWalkingAngle3Modifier()
        {
            return UMath.sinDeg( walkingAngle3 );
        }

        public final void hurt( int descent )
        {
            //player can only lose energy if he is not dying
            if ( !dying )
            {
                //substract damage - clip rock bottom
                health -= descent;
                if ( health < MIN_HEALTH )
                {
                    health = MIN_HEALTH - 1;
                    kill();
                }

                //start red screen anim
                HUD.launchDamageFX( descent );

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
                HUD.launchHealthFX( gain );

            }
        }

        /***********************************************************************************
        *   What would this method possibly do to the player?
        ***********************************************************************************/
        private final void kill()
        {
            dying = true;
        }

        public final void drawHealth( Graphics2D g )
        {
            //draw player's health
            GLPanel.releaseClip( g );
            Strings.drawString( g, Colors.EHealthFg.col, null, Colors.EHealthOutline.col, Fonts.EHealth, Anchor.EAnchorLeftBottom, String.valueOf( health ), Offset.EBorderHudX, GLPanel.PANEL_HEIGHT - Offset.EBorderHudY );
        }

        public final void launchAction()
        {
            Debug.playerAction.out( "check action..!" );

            //launch the action on the level
            Level.launchAction( cylinder );



        }
    }
