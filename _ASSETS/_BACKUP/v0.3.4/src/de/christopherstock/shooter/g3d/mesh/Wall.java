/*  $Id: Wall.java 685 2011-05-01 02:04:30Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.Lib.Scalation;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.g3d.face.LibFace.DrawMethod;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.fx.FX.FXSize;
    import  de.christopherstock.shooter.game.fx.FX.FXTime;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.io.hid.*;

    /**************************************************************************************
    *   Represents a mesh with all faces collision enabled.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class Wall extends Mesh implements GameObject
    {
        private     static  final   int                 DOOR_TICKS_OPEN_CLOSE           = 20;
        private     static  final   float               DOOR_SLIDING_TRANSLATION_OPEN   = 1.9f;
        private     static  final   float               DOOR_ANGLE_OPEN                 = -90f;

        private                     WallAction          iWallAction                     = null;
        private                     WallDestroyable     iDestroyable                    = null;
        private                     WallClimbable       iClimbable                      = null;
        private                     WallCollidable      iCollisionEnabled               = WallCollidable.ENo;
        private                     boolean             iDestroyed                      = false;
        private                     boolean             iQuitDestroyAnim                = false;
        private                     boolean             iDoorOpening                    = false;
        private                     int                 iDoorAnimation                  = 0;
        public                      float               iStartupRotZ                    = 0.0f;
        private                     int                 iHealth                         = 100;
        private                     int                 iDestroyedAnimationTicks        = 0;

        public Wall( D3dsFile aD3dsFile, LibVertex aAnchor, float aStartupRotZ )
        {
            this( aD3dsFile, aAnchor, aStartupRotZ, Scalation.ENone,  Wall.WallCollidable.EYes,  WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant );
        }

        public Wall( D3dsFile aD3dsFile, LibVertex aAnchor, float aStartupRotZ, Lib.Scalation aScalation, Wall.WallCollidable aCollisionEnabled, WallAction aWallAction, WallDestroyable aDestroyable, WallClimbable aClimbable, DrawMethod aDrawMethod )
        {
            super(              ShooterD3dsFiles.getFaces( aD3dsFile ), aAnchor, aStartupRotZ, aScalation.getScaleFactor(), null, LibTransformationMode.EOriginalsToOriginals, aDrawMethod );
            assignParentOnFaces( this );
            iCollisionEnabled   = aCollisionEnabled;
            iWallAction         = aWallAction;
            iStartupRotZ        = aStartupRotZ;
            iDestroyable        = aDestroyable;
            iClimbable          = aClimbable;
        }

        public final void animate()
        {
            if ( !iDestroyed )
            {
                //check if this mesh is associated with an action
                switch ( iWallAction )
                {
                    case EDoorSliding:
                    case EDoorTurning:
                    {
                        //Debug.note("door anchor is: ["+anchor.x+"]");
                        //Debug.note("door anchor is: ["+anchor.y+"]");
                        //Debug.note("door anchor is: ["+anchor.z+"]\n");

                        //check if the door is being opened or being closed
                        LibVertex currentAnk = getAnchor();
                        if ( iDoorOpening )
                        {
                            //open the door
                            if ( iDoorAnimation < DOOR_TICKS_OPEN_CLOSE )
                            {
                                //increase animation counter
                                ++iDoorAnimation;

                                //translate mesh
                                switch ( iWallAction )
                                {
                                    case ENone:
                                    {
                                        //impossible to happen
                                        throw new IllegalFormatCodePointException( 0 );
                                    }

                                    case EDoorSliding:
                                    {
                                        //translate mesh
                                        translate
                                        (
                                            0.0f, //anchor.x,
                                            0.0f + ( iDoorAnimation * DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE ),
                                            0.0f, //anchor.z,
                                            LibTransformationMode.EOriginalsToTransformed
                                        );

                                        //no need to check collisions while opening sliding doors!

                                        //translate mesh's bullet holes
                                        BulletHole.translateAll( this, 0.0f, DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                        break;
                                    }

                                    case EDoorTurning:
                                    {
                                        //rotate mesh
                                        float angle = DOOR_ANGLE_OPEN * iDoorAnimation / DOOR_TICKS_OPEN_CLOSE;

                                        translateAndRotateXYZ
                                        (
                                            currentAnk.x,
                                            currentAnk.y,
                                            currentAnk.z,
                                            0.0f,
                                            0.0f,
                                            angle,
                                            null,
                                            LibTransformationMode.EOriginalsToTransformed
                                        );

                                        //rotate mesh's bullet holes
                                        BulletHole.rotateForWall( this, DOOR_ANGLE_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                        break;
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

                                //translate mesh
                                switch ( iWallAction )
                                {
                                    case ENone:
                                    {
                                        //impossible to happen
                                        throw new IllegalFormatCodePointException( 0 );
                                    }

                                    case EDoorSliding:
                                    {
                                        //translate mesh
                                        translate
                                        (
                                            0.0f, //anchor.x,
                                            0.0f + ( iDoorAnimation * DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE ),
                                            0.0f, //anchor.z,
                                            LibTransformationMode.EOriginalsToTransformed
                                        );

                                        //check collision to player
                                        if ( checkCollisionHorz( ShooterGameLevel.currentPlayer().getCylinder() ) )
                                        {
                                            //open the door
                                            iDoorOpening = true;
                                            makeDistancedSound( Sound.EDoorOpen1 );
                                        }
                                        else
                                        {
                                            //check collision to bots
                                            for ( Bot bot : ShooterGameLevel.current().iBots )
                                            {
                                                if ( checkCollisionHorz( bot.getCylinder() ) )
                                                {
                                                    //open the door
                                                    iDoorOpening = true;
                                                    makeDistancedSound( Sound.EDoorOpen1 );
                                                }
                                            }
                                        }

                                        //translate mesh's bullet holes
                                        BulletHole.translateAll( this, 0.0f, -DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                        break;
                                    }

                                    case EDoorTurning:
                                    {
                                        //rotate mesh
                                        float angle = DOOR_ANGLE_OPEN * iDoorAnimation / DOOR_TICKS_OPEN_CLOSE;

                                        translateAndRotateXYZ
                                        (
                                            currentAnk.x,
                                            currentAnk.y,
                                            currentAnk.z,
                                            0.0f,
                                            0.0f,
                                            angle,
                                            null,
                                            LibTransformationMode.EOriginalsToTransformed
                                        );

                                        //rotate mesh's bullet holes
                                        BulletHole.rotateForWall( this, -DOOR_ANGLE_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }

                    case ENone:
                    {
                        //no action for this mesh
                        break;
                    }
                }
            }
        }

        public final void launchAction( Cylinder cylinder )
        {
            if ( !iDestroyed )
            {
                //only launch an action if this mesh has an according action
                if ( iWallAction != WallAction.ENone )
                {
                    //perform an action if this mesh is affected
                    if ( checkAction( cylinder ) )
                    {
                        switch ( iWallAction )
                        {
                            case ENone:
                            {
                                break;
                            }

                            case EDoorSliding:
                            case EDoorTurning:
                            {
                                ShooterDebug.playerAction.out( "launch door change" );
                                iDoorOpening = !iDoorOpening;
                                if ( iDoorOpening )
                                {
                                    makeDistancedSound( Sound.EDoorOpen1 );
                                }
                                else
                                {
                                    makeDistancedSound( Sound.EDoorClose1 );
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public final float getCarriersFaceAngle()
        {
            return 0.0f;
        }

        /**************************************************************************************
        *   Fires a shot and returns all hit-points.
        *
        *   @param  shot    The shot to be fired on this mesh.
        *   @return         The nearest hitpoint with lots of informations.
        *                   <code>null</code> if no face was hit.
        **************************************************************************************/
        @Override
        public final Vector<HitPoint> launchShot( Shot shot )
        {
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //only launch shot on faces if collision is active
            if ( iCollisionEnabled == WallCollidable.EYes )
            {
                hitPoints.addAll( super.launchShot( shot ) );
            }

            return hitPoints;
        }

        @Override
        public final boolean checkCollisionHorz( Cylinder cylinder )
        {
            //only check collisions  if collision is active
            if ( iCollisionEnabled == WallCollidable.EYes )
            {
                return super.checkCollisionHorz( cylinder, iClimbable );
            }

            return false;
        }

        @Override
        public final Vector<Float> checkCollisionVert( Cylinder cylinder )
        {
            //only check collisions  if collision is active
            if ( iClimbable == WallClimbable.EYes )
            {
                return super.checkCollisionVert( cylinder );
            }

            return new Vector<Float>();
        }
/*
        @Override
        public final Vector<Float> checkCollision( Point2D.Float point )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //only check floor-change if collision is active
            if ( iCollisionEnabled.getBoolean() )
            {
                vecZ.addAll( super.checkCollision( point ) );
            }

            return vecZ;
        }
*/
        @Override
        public HitPointCarrier getHitPointCarrier()
        {
            return HitPointCarrier.EWall;
        }

        public void hurt( int h )
        {
            //only if destroyable
            if ( iDestroyable == WallDestroyable.EYes )
            {
                iHealth -= h;
                if ( iHealth <= 0 )
                {
                    //kill wall :p
                    kill();
                }
            }
        }

        private void kill()
        {
            ShooterDebug.major.out( "wall-mesh destroyed!" );
            iHealth           = 0;
            iDestroyed        = true;
            iCollisionEnabled = WallCollidable.ENo;

            //remove all bullet holes this wall carries
            BulletHole.removeForWall( this );

            //get mesh's center and launch explosion from there
            Point2D.Float p = getCenterPointXY();
            FX.launchExplosion( new LibVertex( p.x, p.y, 0.0f ), FXSize.EMedium, FXTime.EMedium );
        }

        @Override
        public void draw()
        {
            //draw if not destroyed
            if ( iDestroyed )
            {
                if ( !iQuitDestroyAnim )
                {
                    ++iDestroyedAnimationTicks;
                    boolean allFacesDone = true;
                    for ( FaceTriangle f : getFaces() )
                    {
                        int z = iDestroyedAnimationTicks * iDestroyedAnimationTicks;

                        //leave glass a bit over ground
                        if ( f.iHighestZ >= 0.25f )
                        {
                            f.translate
                            (
                                LibMath.getRandom( -8, +8 ) * 0.01f,
                                LibMath.getRandom( -8, +8 ) * 0.01f,
                                -z                          * 0.0005f,
                                LibTransformationMode.EOriginalsToOriginals
                            );
                            allFacesDone = false;
                        }
                    }

                    //quit destroy anim if all faces are done
                    if ( allFacesDone ) iQuitDestroyAnim = true;
                }
            }

            //draw wall
            super.draw();
        }
    }
