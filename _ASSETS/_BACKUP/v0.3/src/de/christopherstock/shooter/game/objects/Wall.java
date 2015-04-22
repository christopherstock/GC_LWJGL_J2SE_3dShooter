/*  $Id: Wall.java 265 2011-02-10 19:48:54Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.g3d.mesh.Mesh.Scalation;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.Collision.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class Wall implements GameObject
    {
        private     static  final   int                 DOOR_TICKS_OPEN_CLOSE           = 15;
        private     static  final   float               DOOR_SLIDING_TRANSLATION_OPEN   = 1.5f;
        private     static  final   float               DOOR_ANGLE_OPEN                 = -90f;

        public                      MeshCollection      iMeshes                          = null;

        private                     boolean             iDoorOpening                    = false;
        private                     int                 iDoorAnimation                  = 0;

        private                     CollisionEnabled    iCollisionEnabled               = CollisionEnabled.NO;
        private                     WallAction          iWallAction                     = null;

        public Wall( D3dsFiles aD3dsFile, LibVertex aAnchor, float startupRotZ, Scalation aScalation, CollisionEnabled aCollisionEnabled, WallAction aWallAction )
        {
            iMeshes           = new MeshCollection( aAnchor, new Mesh[] { new Mesh( D3dsFiles.getFaces( aD3dsFile ), aAnchor, startupRotZ, aScalation.getScaleFactor(), this ), } );
            iCollisionEnabled = aCollisionEnabled;
            iWallAction       = aWallAction;
        }

        public final void animate()
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
                    LibVertex anchor = iMeshes.getAnchor();
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
                                    iMeshes.translate
                                    (
                                        anchor.x,
                                        anchor.y + iDoorAnimation * DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE,
                                        anchor.z
                                    );

                                    //translate mesh's bullet holes
                                    BulletHole.translateAll( this, 0.0f, DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                    break;
                                }

                                case EDoorTurning:
                                {
                                    //rotate mesh
                                    float angle = DOOR_ANGLE_OPEN * iDoorAnimation / DOOR_TICKS_OPEN_CLOSE;

                                    iMeshes.translateAndRotateXYZ
                                    (
                                        anchor.x,
                                        anchor.y,
                                        anchor.z,
                                        0.0f,
                                        0.0f,
                                        angle
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
                                    iMeshes.translate
                                    (
                                        anchor.x,
                                        anchor.y + iDoorAnimation * DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE,
                                        anchor.z
                                    );

                                    //translate mesh's bullet holes
                                    BulletHole.translateAll( this, 0.0f, -DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                    break;
                                }

                                case EDoorTurning:
                                {
                                    //rotate mesh
                                    float angle = DOOR_ANGLE_OPEN * iDoorAnimation / DOOR_TICKS_OPEN_CLOSE;

                                    iMeshes.translateAndRotateXYZ
                                    (
                                        anchor.x,
                                        anchor.y,
                                        anchor.z,
                                        0.0f,
                                        0.0f,
                                        angle
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

        public final void launchAction( Cylinder cylinder )
        {
            //only launch an action if this mesh has an according action
            if ( iWallAction != WallAction.ENone )
            {
                //perform an action if this mesh is affected
                if ( iMeshes.checkAction( cylinder ) )
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
                            ShooterDebugSystem.playerAction.out( "launch door change" );
                            iDoorOpening = !iDoorOpening;
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iMeshes.getAnchor();
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
            if ( iCollisionEnabled.flag )
            {
                hitPoints.addAll( iMeshes.launchShot( shot ) );
            }

            return hitPoints;
        }

        public final boolean checkCollision( Cylinder cylinder )
        {
            //only check collisions  if collision is active
            if ( iCollisionEnabled.flag )
            {
                return iMeshes.checkCollision( cylinder );
            }
            return false;
        }

        public final Vector<Float> launchFloorCheck( Point2D.Float point )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //only check floor-change if collision is active
            if ( iCollisionEnabled.flag )
            {
                vecZ.addAll( iMeshes.launchFloorCheck( point ) );
            }

            return vecZ;
        }

        @Override
        public HitPointCarrier getHitPointCarrier()
        {
            return HitPointCarrier.EWall;
        }
    }
