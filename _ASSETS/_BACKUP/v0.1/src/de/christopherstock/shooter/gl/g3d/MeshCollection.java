/*  $Id: MeshCollection.java,v 1.2 2007/09/02 14:48:17 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.g3d;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.shooter.game.collision.*;
import  de.christopherstock.shooter.game.collision.unit.*;
import de.christopherstock.shooter.gl.mesh.*;

    /**************************************************************************************
    *   Represents a mesh-collection.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class MeshCollection
    {
        public  static          MeshCollection[][]  LEVEL_MESH_COLLECTION_DATA      = null;

        public static final void init()
        {
            LEVEL_MESH_COLLECTION_DATA = new MeshCollection[][]
            {
                /*  ELevel1         */
                new MeshCollection[]
                {
                    new MeshCollection
                    (
                        new Mesh[]
                        {
                            //the level
                            Meshes.levelDesert1,

                            //a room
                            Meshes.executiveOffice1,

                            //door for the room
                            Meshes.executiveOffice1Door1,

                            //spawn 2nd door
                            Meshes.executiveOffice1Door2,




                        }
                    ),
                },
                /*  LEVEL_2     */  {},
                /*  LEVEL_3     */  {},
            };
        }

        private                 Mesh[]              meshes                          = null;

        public MeshCollection( Mesh[] initMeshes )
        {
            meshes = initMeshes;
        }

        public final void draw()
        {
            //draw all meshes
            for ( Mesh mesh : meshes )
            {
                mesh.draw();
            }
        }

        public final boolean checkCollisionFree( Cylinder cylinder )
        {
            for ( Mesh mesh : meshes )
            {
                if ( mesh.checkCollisionFree( cylinder ) == false ) return false;
            }
            return true;
        }

        public final Vector<HitPoint> launchShot( Shot shot )
        {
            //collect all hit-points
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();
            for ( Mesh mesh : meshes )
            {
                hitPoints.addAll( mesh.launchShot( shot ) );
            }
            return hitPoints;
        }

        public final void launchAction( Cylinder cylinder )
        {
            //launch action on all meshes
            for ( Mesh mesh : meshes )
            {
                mesh.launchAction( cylinder );
            }
        }

        public final Vector<Float> launchFloorCheck( Point2D.Float point )
        {
            //collect all hit-points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( Mesh mesh : meshes )
            {
                hitPointsZ.addAll( mesh.launchFloorCheck( point ) );
            }
            return hitPointsZ;
        }

        public final void animate()
        {
            //animate all meshes
            for ( Mesh mesh : meshes )
            {
                mesh.animate();
            }
        }
    }
