/*  $Id: Wall.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.g3d.*;
    import de.christopherstock.lib.g3d.LibCollision.*;
    import  de.christopherstock.shooter.*;
    import de.christopherstock.shooter.ShooterD3dsFiles.*;
    import de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;

    /**************************************************************************************
    *   Represents a mesh with all faces collision enabled.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
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

        public Wall( D3dsFile aD3dsFile, LibVertex aAnchor, float aStartupRotZ, Lib.Scalation aScalation, CollisionEnabled aCollisionEnabled, WallAction aWallAction )
        {
            this( aD3dsFile, aAnchor, aStartupRotZ, aScalation, aCollisionEnabled, aWallAction, LibTransformationMode.EOriginalsToTransformed );
        };

        public Wall( D3dsFile aD3dsFile, LibVertex aAnchor, float aStartupRotZ, Lib.Scalation aScalation, CollisionEnabled aCollisionEnabled, WallAction aWallAction, LibTransformationMode transformationMode )
        {
            super(              ShooterD3dsFiles.getFaces( aD3dsFile ), aAnchor, aStartupRotZ, aScalation.getScaleFactor(), null, transformationMode );
            assignParentOnFaces( this );
            iCollisionEnabled   = aCollisionEnabled;
            iWallAction         = aWallAction;
            iStartupRotZ        = aStartupRotZ;
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
                                    if ( checkCollision( Level.currentPlayer().getCylinder() ) )
                                    {
                                        //open the door
                                        iDoorOpening = true;
                                    }
                                    else
                                    {
                                        //check collision to bots
                                        for ( Bot bot : Level.current().iBots )
                                        {
                                            if ( checkCollision( bot.getCylinder() ) )
                                            {
                                                //open the door
                                                iDoorOpening = true;
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

        public final void launchAction( Cylinder cylinder )
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
                            break;
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
    }
