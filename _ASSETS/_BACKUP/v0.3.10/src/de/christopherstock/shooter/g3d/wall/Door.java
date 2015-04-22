/*  $Id: Door.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.wall;

    import  java.util.IllegalFormatCodePointException;
    import  de.christopherstock.lib.Lib.Invert;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.Lib.Scalation;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.LibMath;
    import  de.christopherstock.shooter.ShooterDebug;
    import  de.christopherstock.shooter.ShooterSettings.DoorSettings;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.base.ShooterTexture.Tex;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.face.Face.DrawMethod;
    import  de.christopherstock.shooter.game.objects.Bot;
    import  de.christopherstock.shooter.game.wearpon.*;

    /**************************************************************************************
    *   Represents a mesh with all faces collision enabled.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class Door extends Wall
    {
        private                     boolean             iDoorOpening                    = false;
        private                     boolean             iDoorLocked                     = false;
        private                     ArtefactType        iDoorKey                        = null;
        private                     int                 iDoorAnimation                  = 0;

        private                     int                 iDoorUnlockedCountdown          = 0;
        private                     int                 iDoorLockedCountdown            = 0;

        private                     Door                iWallElevator                   = null;
        private                     Door                iWallElevatorDoorTop            = null;
        private                     Door                iWallElevatorDoorBottom         = null;
        private                     Wall                iWallElevatorCeiling            = null;

        private                     boolean             iAutoLock                       = false;

        private                     boolean             toggleDoorTopNextTick           = false;
        private                     boolean             toggleDoorBottomNextTick        = false;

        public Door( ShooterD3ds.D3dsFile file, float x, float y, float z, float rotZ, WallCollidable aCollidable, WallAction doorAction, WallClimbable aClimbable, Tex tex, WallEnergy.WallHealth wallHealth, Door connectedElevator, ArtefactType aDoorKey, boolean aAutoLock )
        {
            super( file, new LibVertex( x, y, z ), rotZ, Scalation.ENone, Invert.ENo, aCollidable, doorAction, aClimbable, DrawMethod.EHideIfTooDistant, tex, null, wallHealth, null, null );

            iDoorKey            = aDoorKey;
            iWallElevator       = connectedElevator;
            iAutoLock           = aAutoLock;
            if ( iDoorKey != null )
            {
                iDoorLocked = true;
            }
        }

        protected void animateDoor()
        {
            if ( toggleDoorTopNextTick )
            {
                toggleDoorTopNextTick = false;

                //toggle connected door if any
                if ( iWallElevatorDoorBottom != null )
                {
                    if ( iWallAction == WallAction.EElevatorUp )
                    {
                        iWallElevatorDoorTop.toggleWallOpenClose( null );
                    }
                    else
                    {
                        iWallElevatorDoorBottom.toggleWallOpenClose( null );
                    }
                }
            }

            if ( toggleDoorBottomNextTick )
            {
                toggleDoorBottomNextTick = false;

                if ( iWallElevatorDoorBottom != null )
                {
                    if ( iWallAction == WallAction.EElevatorUp )
                    {
                        iWallElevatorDoorBottom.toggleWallOpenClose( null );
                    }
                    else
                    {
                        iWallElevatorDoorTop.toggleWallOpenClose( null );
                    }
                }
            }

            //check if the door is being opened or being closed
            if ( iDoorOpening )
            {
                //open the door
                if ( iDoorAnimation < DoorSettings.DOOR_TICKS_OPEN_CLOSE )
                {
                    //increase animation counter
                    ++iDoorAnimation;

                    //translate mesh
                    switch ( iWallAction )
                    {
                        case ENone:
                        case ESprite:
                        {
                            //impossible to happen
                            throw new IllegalFormatCodePointException( 0 );
                        }

                        case EDoorSlideLeft:
                        {
                            slideAsDoor( true, true );
                            break;
                        }

                        case EDoorSlideRight:
                        {
                            slideAsDoor( true, false );
                            break;
                        }

                        case EDoorSwingClockwise:
                        {
                            swingAsDoor( true, false );
                            break;
                        }

                        case EDoorSwingCounterClockwise:
                        {
                            swingAsDoor( true, true );
                            break;
                        }

                        case EElevatorUp:
                        {
                            moveAsElevator( true );
                            break;
                        }

                        case EElevatorDown:
                        {
                            moveAsElevator( false );
                            break;
                        }

                        case EDoorHatch:
                        {
                            //rotate mesh
                            float angle  = DoorSettings.DOOR_ANGLE_OPEN / DoorSettings.DOOR_TICKS_OPEN_CLOSE;
                            float rotX   = LibMath.cosDeg( iStartupRotZ - 90.0f ) * angle;
                            float rotY   = LibMath.sinDeg( iStartupRotZ - 90.0f ) * angle;

                            translateAndRotateXYZ
                            (
                                0.0f,
                                0.0f,
                                0.0f,
                                rotX * iDoorAnimation,
                                rotY * iDoorAnimation,
                                0.0f,
                                getAnchor(),
                                LibTransformationMode.EOriginalsToTransformed
                            );

                            //rotate mesh's bullet holes
                            BulletHole.rotateForWall( this, rotX, rotY, 0.0f );
                            break;
                        }
                    }

                    //check if door is open now
                    if ( iDoorAnimation == DoorSettings.DOOR_TICKS_OPEN_CLOSE )
                    {
                        toggleDoorTopNextTick = true;

                        //lock the door automatically after countdown
                        if ( iAutoLock )
                        {
                            iDoorLockedCountdown = 100;
                        }
                    }
                }
            }
            else
            {
                //close the door
                if ( iDoorAnimation > 0 )
                {
                    //decrease animation counter
                    --iDoorAnimation;

                    //disable auto lock
                    iDoorLockedCountdown = -1;

                    //translate mesh
                    switch ( iWallAction )
                    {
                        case ENone:
                        case ESprite:
                        {
                            //impossible to happen
                            throw new IllegalFormatCodePointException( 0 );
                        }

                        case EDoorSlideLeft:
                        {
                            slideAsDoor( false, true );
                            break;
                        }

                        case EDoorSlideRight:
                        {
                            slideAsDoor( false, false );
                            break;
                        }

                        case EDoorSwingClockwise:
                        {
                            swingAsDoor( false, false );
                            break;
                        }

                        case EDoorSwingCounterClockwise:
                        {
                            swingAsDoor( false, true );
                            break;
                        }

                        case EElevatorUp:
                        {
                            moveAsElevator( false );
                            break;
                        }

                        case EElevatorDown:
                        {
                            moveAsElevator( true );
                            break;
                        }

                        case EDoorHatch:
                        {
                            //rotate mesh
                            float angle  = DoorSettings.DOOR_ANGLE_OPEN / DoorSettings.DOOR_TICKS_OPEN_CLOSE;
                            float rotX   = LibMath.cosDeg( iStartupRotZ - 90.0f ) * angle;
                            float rotY   = LibMath.sinDeg( iStartupRotZ - 90.0f ) * angle;

                            translateAndRotateXYZ
                            (
                                0.0f,
                                0.0f,
                                0.0f,
                                rotX * iDoorAnimation,
                                rotY * iDoorAnimation,
                                0.0f,
                                getAnchor(),
                                LibTransformationMode.EOriginalsToTransformed
                            );

                            //rotate mesh's bullet holes
                            BulletHole.rotateForWall( this, -rotX, -rotY, 0.0f );
                            break;
                        }
                    }

                    //check if door is closed now
                    if ( iDoorAnimation == 0 )
                    {
                        //toggle connected elevator if any
                        if ( iWallElevator != null )
                        {
                            //only if player stands on the elevator
                            if ( iWallElevator.checkAction( ShooterGameLevel.currentPlayer().getCylinder(), true, true ) )
                            {
                                iWallElevator.toggleWallOpenClose( null );
                            }
                        }

                        //toggle connected door if any
                        toggleDoorBottomNextTick = true;
                    }
                }
            }

            //countdown door unlocking
            if ( iDoorUnlockedCountdown > 0 )
            {
                if ( --iDoorUnlockedCountdown == 0 )
                {
                    ShooterDebug.playerAction.out( "unlock the door!" );

                    //unlock and open the door
                    iDoorLocked  = false;
                    iDoorOpening = true;

                    makeDistancedSound( ShooterSound.EDoorOpen1 );
                }
            }

            //countdown door locking
            if ( iDoorLockedCountdown > 0 )
            {
                if ( --iDoorLockedCountdown == 0 )
                {
                    ShooterDebug.playerAction.out( "lock the door!" );

                    //lock the door
                    iDoorOpening = false;

                    makeDistancedSound( ShooterSound.EDoorClose1 );
                }
            }
        }

        protected void slideAsDoor( boolean open, boolean left )
        {
            float transX = LibMath.cosDeg( iStartupRotZ - 90.0f ) * ( DoorSettings.DOOR_SLIDING_TRANSLATION_OPEN / DoorSettings.DOOR_TICKS_OPEN_CLOSE );
            float transY = LibMath.sinDeg( iStartupRotZ - 90.0f ) * ( DoorSettings.DOOR_SLIDING_TRANSLATION_OPEN / DoorSettings.DOOR_TICKS_OPEN_CLOSE );

            //translate mesh
            translate
            (
                0.0f + transX * iDoorAnimation * ( left ? -1.0f : +1.0f ),
                0.0f + transY * iDoorAnimation * ( left ? -1.0f : +1.0f ),
                0.0f,
                LibTransformationMode.EOriginalsToTransformed
            );

            //translate mesh's bullet holes
            BulletHole.translateAll( this, ( open ? 1.0f : -1.0f ) * ( iWallAction == WallAction.EDoorSlideRight ? 1.0f : -1.0f ) * transX, ( open ? 1.0f : -1.0f ) * ( iWallAction == WallAction.EDoorSlideRight ? 1.0f : -1.0f ) * transY, 0.0f );

            //check collision to player
            checkOnDoorAnimation( open );
        }

        protected final void swingAsDoor( boolean open, boolean counterClockwise )
        {
            //rotate mesh
            float angle = DoorSettings.DOOR_ANGLE_OPEN * iDoorAnimation / DoorSettings.DOOR_TICKS_OPEN_CLOSE;

            translateAndRotateXYZ
            (
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                ( counterClockwise ? 1.0f : -1.0f ) * angle,
                getAnchor(),
                LibTransformationMode.EOriginalsToTransformed
            );

            //rotate mesh's bullet holes
            BulletHole.rotateForWall( this, 0.0f, 0.0f, ( open ? ( counterClockwise ? 1.0f : -1.0f ) : ( counterClockwise ? -1.0f : 1.0f ) ) * DoorSettings.DOOR_ANGLE_OPEN / DoorSettings.DOOR_TICKS_OPEN_CLOSE );

            //check collision to player
            checkOnDoorAnimation( open );
        }

        protected final void moveAsElevator( boolean up )
        {
            //translate mesh and bullet hole
            translate( 0.0f, 0.0f, ( up ? 0.125f : -0.125f ), LibTransformationMode.EOriginalsToOriginals );
            BulletHole.translateAll( this, 0.0f, 0.0f, ( up ? 0.125f : -0.125f ) );

            //same for ceiling
            if ( iWallElevatorCeiling != null )
            {
                iWallElevatorCeiling.translate( 0.0f, 0.0f, ( up ? 0.125f : -0.125f ), LibTransformationMode.EOriginalsToOriginals );
                BulletHole.translateAll( iWallElevatorCeiling, 0.0f, 0.0f, ( up ? 0.125f : -0.125f ) );
            }
        }

        protected final void checkOnDoorAnimation( boolean opening )
        {
            boolean toggleDoor = false;

            if ( checkCollisionHorz( ShooterGameLevel.currentPlayer().getCylinder() ) )
            {
                //toggle door
                toggleDoor = true;
            }
            else
            {
                //check collision to bots
                for ( Bot bot : ShooterGameLevel.current().getBots() )
                {
                    if ( checkCollisionHorz( bot.getCylinder() ) )
                    {
                        toggleDoor = true;
                    }
                }
            }

            if ( toggleDoor )
            {
                iDoorOpening = !opening;
                makeDistancedSound( ( opening ? ShooterSound.EDoorClose1 : ShooterSound.EDoorOpen1 ) );
            }
        }

        protected final void toggleWallOpenClose( Gadget artefact )
        {
            if ( artefact == null )
            {
                if ( iDoorLocked )
                {
                    ShooterDebug.playerAction.out( "door is locked!" );
                    makeDistancedSound( ShooterSound.ELocked1 );
                }
                else
                {
                    //check if this is an elevator door - doors can't be toggled while elevator is moving!
                    if ( iWallElevator != null )
                    {
                        if ( this == iWallElevator.iWallElevatorDoorTop || this == iWallElevator.iWallElevatorDoorBottom )
                        {
                            if ( iWallElevator.iDoorAnimation != 0 && iWallElevator.iDoorAnimation != DoorSettings.DOOR_TICKS_OPEN_CLOSE )
                            {
                                //deny wall toggle
                                //ShooterDebug.bugfix.out( "deny wall toggle" );
                                return;
                            }
                        }
                    }

                    iDoorOpening = !iDoorOpening;
                    ShooterDebug.playerAction.out( "launch door change, doorOpening now [" + iDoorOpening + "]" );
                    if ( iDoorOpening )
                    {
                        makeDistancedSound( ShooterSound.EDoorOpen1 );
                    }
                    else
                    {
                        makeDistancedSound( ShooterSound.EDoorClose1 );
                    }
                }
            }
            else
            {
                ShooterDebug.playerAction.out( "launch gadget action on door" );
                if ( iDoorLocked )
                {
                    if ( artefact.iParentKind == iDoorKey && iDoorUnlockedCountdown == 0 )
                    {
                        ShooterDebug.playerAction.out( "use doorkey!" );
                        makeDistancedSound( ShooterSound.EUnlocked1 );

                        iDoorUnlockedCountdown = 100;

                        //open the door - later?
                        //iDoorOpening = true;
                    }
                }
            }
        }

        public final void setElevatorDoors( Door wallDoorTop, Door wallDoorBottom, Wall wallCeiling )
        {
            iWallElevatorDoorTop    = wallDoorTop;
            iWallElevatorDoorBottom = wallDoorBottom;
            iWallElevatorCeiling    = wallCeiling;
        }
    }
