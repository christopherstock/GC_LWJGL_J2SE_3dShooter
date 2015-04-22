/*  $Id: Wall.java 191 2010-12-13 20:24:11Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.d3ds.*;
    import  de.christopherstock.shooter.d3ds.D3dsImporter.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.g3d.mesh.Mesh.Scalation;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.Collision.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Wall extends GameObject
    {
        private     static  final   int                 DOOR_TICKS_OPEN_CLOSE           = 15;
        private     static  final   float               DOOR_SLIDING_TRANSLATION_OPEN   = 1.5f;
        private     static  final   float               DOOR_ANGLE_OPEN                 = -90f;

        public                      MeshCollection      meshes                          = null;

        private                     boolean             doorOpening                     = false;
        private                     int                 doorAnimation                   = 0;

        public                      CollisionEnabled    collisionEnabled                = CollisionEnabled.NO;
        private                     WallAction          wallAction                      = null;

        public Wall( D3dsFiles aD3dsFile, LibVertex aAnchor, float startupRotZ, Scalation aScalation, CollisionEnabled aCollisionEnabled, WallAction aWallAction )
        {
            super( GameObject.HitPointCarrier.EWall );
            meshes           = new MeshCollection( aAnchor, new Mesh[] { new Mesh( D3dsImporter.getFaces( aD3dsFile ), aAnchor, startupRotZ, aScalation.getScaleFactor(), this ), } );
            collisionEnabled = aCollisionEnabled;
            wallAction       = aWallAction;
        }

        public final void animate()
        {
            //check if this mesh is associated with an action
            switch ( wallAction )
            {
                case EDoorSliding:
                case EDoorTurning:
                {
                    //Debug.note("door anchor is: ["+anchor.x+"]");
                    //Debug.note("door anchor is: ["+anchor.y+"]");
                    //Debug.note("door anchor is: ["+anchor.z+"]\n");

                    //check if the door is being opened or being closed
                    LibVertex anchor = meshes.getAnchor();
                    if ( doorOpening )
                    {
                        //open the door
                        if ( doorAnimation < DOOR_TICKS_OPEN_CLOSE )
                        {
                            //increase animation counter
                            ++doorAnimation;

                            //translate mesh
                            switch ( wallAction )
                            {
                                case ENone:
                                {
                                    //impossible to happen
                                    throw new IllegalFormatCodePointException( 0 );
                                }

                                case EDoorSliding:
                                {
                                    //translate mesh
                                    meshes.translate
                                    (
                                        anchor.x,
                                        anchor.y + doorAnimation * DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE,
                                        anchor.z
                                    );

                                    //translate mesh's bullet holes
                                    BulletHole.translateAll( this, 0.0f, DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                    break;
                                }

                                case EDoorTurning:
                                {
                                    //rotate mesh
                                    float angle = DOOR_ANGLE_OPEN * doorAnimation / DOOR_TICKS_OPEN_CLOSE;

                                    meshes.translateAndRotateXYZ
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
                        if ( doorAnimation > 0 )
                        {
                            //decrease animation counter
                            --doorAnimation;

                            //translate mesh
                            switch ( wallAction )
                            {
                                case ENone:
                                {
                                    //impossible to happen
                                    throw new IllegalFormatCodePointException( 0 );
                                }

                                case EDoorSliding:
                                {
                                    //translate mesh
                                    meshes.translate
                                    (
                                        anchor.x,
                                        anchor.y + doorAnimation * DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE,
                                        anchor.z
                                    );

                                    //translate mesh's bullet holes
                                    BulletHole.translateAll( this, 0.0f, -DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                    break;
                                }

                                case EDoorTurning:
                                {
                                    //rotate mesh
                                    float angle = DOOR_ANGLE_OPEN * doorAnimation / DOOR_TICKS_OPEN_CLOSE;

                                    meshes.translateAndRotateXYZ
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
            if ( wallAction != WallAction.ENone )
            {
                //perform an action if this mesh is affected
                if ( meshes.checkAction( cylinder ) )
                {
                    switch ( wallAction )
                    {
                        case ENone:
                        {
                            break;
                        }

                        case EDoorSliding:
                        case EDoorTurning:
                        {
                            Debug.playerAction.out( "launch door change" );
                            doorOpening = !doorOpening;
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public final LibVertex getAnchor()
        {
            return meshes.getAnchor();
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
            if ( collisionEnabled.flag )
            {
                hitPoints.addAll( meshes.launchShot( shot ) );
            }

            return hitPoints;
        }

        public final boolean checkCollision( Cylinder cylinder )
        {
            //only check collisions  if collision is active
            if ( collisionEnabled.flag )
            {
                return meshes.checkCollision( cylinder );
            }
            return false;
        }

        public final Vector<Float> launchFloorCheck( Point2D.Float point )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //only check floor-change if collision is active
            if ( collisionEnabled.flag )
            {
                vecZ.addAll( meshes.launchFloorCheck( point ) );
            }

            return vecZ;
        }
    }
