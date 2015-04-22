/*  $Id: Wall.java 630 2011-04-23 12:43:30Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.g3d.LibCollision.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.fx.FX.FXSize;
    import  de.christopherstock.shooter.game.fx.FX.FXTime;
    import  de.christopherstock.shooter.io.hid.*;

    /**************************************************************************************
    *   Represents a mesh with all faces collision enabled.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class Wall extends Mesh implements GameObject
    {
        private     static  final   int                 DOOR_TICKS_OPEN_CLOSE           = 20;
        private     static  final   float               DOOR_SLIDING_TRANSLATION_OPEN   = 1.9f;
        private     static  final   float               DOOR_ANGLE_OPEN                 = -90f;

        private                     boolean             iDoorOpening                    = false;
        private                     int                 iDoorAnimation                  = 0;
        private                     CollisionEnabled    iCollisionEnabled               = CollisionEnabled.ENo;
        private                     WallAction          iWallAction                     = null;
        public                      float               iStartupRotZ                    = 0.0f;
        private                     int                 iHealth                         = 100;
        private                     WallDestroyable     iDestroyable                    = null;
        private                     boolean             iDestroyed                      = false;
        private                     int                 iDestroyedAnimationTicks        = 0;
        private                     boolean             iQuitDestroyAnim                = false;

        public Wall( D3dsFile aD3dsFile, LibVertex aAnchor, float aStartupRotZ, Lib.Scalation aScalation, CollisionEnabled aCollisionEnabled, WallAction aWallAction )
        {
            this( aD3dsFile, aAnchor, aStartupRotZ, aScalation, aCollisionEnabled, aWallAction, LibTransformationMode.EOriginalsToTransformed, WallDestroyable.ENo );
        };

        public Wall( D3dsFile aD3dsFile, LibVertex aAnchor, float aStartupRotZ, Lib.Scalation aScalation, CollisionEnabled aCollisionEnabled, WallAction aWallAction, WallDestroyable aDestroyable )
        {
            this( aD3dsFile, aAnchor, aStartupRotZ, aScalation, aCollisionEnabled, aWallAction, LibTransformationMode.EOriginalsToTransformed, aDestroyable );
        };

        public Wall( D3dsFile aD3dsFile, LibVertex aAnchor, float aStartupRotZ, Lib.Scalation aScalation, CollisionEnabled aCollisionEnabled, WallAction aWallAction, LibTransformationMode transformationMode, WallDestroyable aDestroyable )
        {
            //TODO ASAP set LibTransformationMode.EOriginalsToOriginals
            super(              ShooterD3dsFiles.getFaces( aD3dsFile ), aAnchor, aStartupRotZ, aScalation.getScaleFactor(), null, transformationMode );
            assignParentOnFaces( this );
            iCollisionEnabled   = aCollisionEnabled;
            iWallAction         = aWallAction;
            iStartupRotZ        = aStartupRotZ;
            iDestroyable        = aDestroyable;
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
                                        if ( checkCollision( GameLevel.currentPlayer().getCylinder() ) )
                                        {
                                            //open the door
                                            iDoorOpening = true;
                                            makeDistancedSound( Sound.EDoorOpen1 );
                                        }
                                        else
                                        {
                                            //check collision to bots
                                            for ( Bot bot : GameLevel.current().iBots )
                                            {
                                                if ( checkCollision( bot.getCylinder() ) )
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
            if ( iCollisionEnabled.getBoolean() )
            {
                hitPoints.addAll( super.launchShot( shot ) );
            }

            return hitPoints;
        }

        @Override
        public final boolean checkCollision( Cylinder cylinder )
        {
            //only check collisions  if collision is active
            if ( iCollisionEnabled.getBoolean() )
            {
                return super.checkCollision( cylinder );
            }
            return false;
        }

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
            iCollisionEnabled = CollisionEnabled.ENo;

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
