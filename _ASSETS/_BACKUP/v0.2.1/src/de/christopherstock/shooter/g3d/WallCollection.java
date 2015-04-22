/*  $Id: WallCollection.java,v 1.2 2007/09/02 14:48:17 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  java.awt.geom.*;
    import  java.util.*;

import de.christopherstock.lib.g3d.*;
import de.christopherstock.shooter.d3ds.D3dsImporter.*;
import de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.Collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.objects.GameObject.*;

    /**************************************************************************************
    *   Represents a mesh-collection.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class WallCollection
    {
        public  static          WallCollection[][]  LEVEL_MESH_COLLECTION_DATA      = null;

        public static final void init()
        {
            LEVEL_MESH_COLLECTION_DATA = new WallCollection[][]
            {
                /*  ELevel1         */
                new WallCollection[]
                {
                    new WallCollection
                    (
                        new Wall[]
                        {
                            //the level
                            new Wall( D3dsFiles.EMeshLevelDesert1,          new LibVertex( 0.0f,   0.0f,   0.0f    ), CollisionEnabled.YES, WallAction.ENone           ),

                            //a room
                            new Wall( D3dsFiles.EMeshExecutiveOffice1,      new LibVertex( 14.0f,  8.0f,   0.01f   ), CollisionEnabled.YES, WallAction.ENone           ),


                            //door for the room
                            new Wall( D3dsFiles.EMeshExecutiveOffice1GlassDoor, new LibVertex( 7.95f,  9.225f, 0.01f   ), CollisionEnabled.YES, WallAction.EDoorSliding    ),
                            //door for the room
                            new Wall( D3dsFiles.EMeshExecutiveOffice1GlassDoor, new LibVertex( 8.95f,  9.225f, 0.01f   ), CollisionEnabled.YES, WallAction.EDoorSliding    ),
                            //door for the room
                            new Wall( D3dsFiles.EMeshExecutiveOffice1GlassDoor, new LibVertex( 9.95f,  9.225f, 0.01f   ), CollisionEnabled.YES, WallAction.EDoorSliding    ),





                            //desk?
                            new Wall( D3dsFiles.ETest, new LibVertex( 12.0f,  9.0f, 0.01f   ), CollisionEnabled.YES, WallAction.ENone    ),
                            //ambulance?
                            //new Wall( D3dsFiles.ETest2, new GLVertex( -1.0f,  -1.0f, 0.01f   ), CollisionEnabled.YES, WallAction.ENone    ),




                            //door for the room
                            new Wall( D3dsFiles.EMeshExecutiveOffice1Door1, new LibVertex( 6.95f, 2.225f, 0.01f   ), CollisionEnabled.YES, WallAction.EDoorSliding    ),

                            //spawn 2nd door
                            new Wall( D3dsFiles.EMeshExecutiveOffice1Door1, new LibVertex( 5.95f,  5.225f, 0.01f   ), CollisionEnabled.YES, WallAction.EDoorTurning    ),



                            //spawn 2nd door
                            //new Wall( Meshes.woman1, CollisionEnabled.YES, WallAction.EDoorTurning ),


                        }
                    ),
                },
                /*  LEVEL_2     */  {},
                /*  LEVEL_3     */  {},
            };
        }

        private                 Wall[]              walls                          = null;

        public WallCollection( Wall[] initWalls )
        {
            walls = initWalls;
        }

        public final void draw()
        {
            //draw all meshes
            for ( Wall wall : walls )
            {
                wall.meshes.draw();
            }
        }

        public final boolean checkCollision( Cylinder cylinder )
        {
            for ( Wall mesh : walls )
            {
                if ( mesh.checkCollision( cylinder ) ) return true;
            }
            return false;
        }

        public final Vector<HitPoint> launchShot( Shot shot )
        {
            //collect all hit-points
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();
            for ( Wall wall : walls )
            {
                hitPoints.addAll( wall.launchShot( shot ) );
            }
            return hitPoints;
        }

        public final void launchAction( Cylinder cylinder )
        {
            //launch action on all meshes
            for ( Wall wall : walls )
            {
                wall.launchAction( cylinder );
            }
        }

        public final Vector<Float> launchFloorCheck( Point2D.Float point )
        {
            //collect all hit-points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( Wall wall : walls )
            {
                hitPointsZ.addAll( wall.launchFloorCheck( point ) );
            }
            return hitPointsZ;
        }

        public final void animate()
        {
            //animate all meshes
            for ( Wall wall : walls )
            {
                wall.animate();
            }
        }
    }
