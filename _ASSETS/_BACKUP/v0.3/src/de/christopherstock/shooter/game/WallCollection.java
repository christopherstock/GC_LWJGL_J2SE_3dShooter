/*  $Id: WallCollection.java 265 2011-02-10 19:48:54Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.g3d.mesh.Mesh.Scalation;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.Collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.objects.GameObject.*;

    /**************************************************************************************
    *   Represents a mesh-collection.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class WallCollection
    {
        public  static          WallCollection[][]  LEVEL_MESH_COLLECTION_DATA      = null;

        public static final void init()
        {
            LEVEL_MESH_COLLECTION_DATA = new WallCollection[][]
            {
                //ELevel1
                new WallCollection[]
                {
                    new WallCollection
                    (
                        new Wall[]
                        {
                            //the level
                            new Wall( D3dsFiles.ELevelParkingLot1,          new LibVertex(  0.0f,           0.0f,           0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
/*
                            new Wall( D3dsFiles.EItemShotgun,               new LibVertex(  2.0f,           3.0f,           0.2f    ), 45.0f,   Scalation.ENone, CollisionEnabled.NO,  WallAction.ENone           ),
                            new Wall( D3dsFiles.EItemPistol1,               new LibVertex(  3.0f,           3.0f,           0.2f    ), 45.0f,   Scalation.ENone, CollisionEnabled.NO,  WallAction.ENone           ),

                            new Wall( D3dsFiles.EItemAmmoShotgunShells,     new LibVertex(  4.0f,           3.0f,           0.2f    ), 45.0f,   Scalation.ENone, CollisionEnabled.NO,  WallAction.ENone           ),
                            new Wall( D3dsFiles.EItemAmmoBullet9mm,         new LibVertex(  5.0f,           3.0f,           0.2f    ), 45.0f,   Scalation.ENone, CollisionEnabled.NO,  WallAction.ENone           ),
*/

/*
                            //office1
                            new Wall( D3dsFiles.EExecutiveOffice1,          new LibVertex(  14.0f -0.0f,    8.0f   - 0.0f,  0.01f   ), 0.0f,    Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EExecutiveOffice1GlassDoor, new LibVertex(  7.95f -0.0f,    9.225f - 0.0f,  0.01f   ), 0.0f,    Scalation.ENone, CollisionEnabled.YES, WallAction.EDoorSliding    ),
                            new Wall( D3dsFiles.EDesk1,                     new LibVertex(  12.0f -0.0f,    9.0f   - 0.0f,  0.01f   ), 0.0f,    Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EPlant1,                    new LibVertex(  10.0f -0.0f,    8.0f   - 0.0f,  0.01f   ), 0.0f,    Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EExecutiveOffice1GlassDoor, new LibVertex(  14.0f -0.0f,    9.225f - 0.0f,  0.01f   ), 0.0f,    Scalation.ENone, CollisionEnabled.YES, WallAction.EDoorSliding    ),
*/
/*
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 0.0f,   Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 45.0f,  Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 90.0f,  Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 135.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 180.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 225.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 270.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 315.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),

                            //cars
                            new Wall( D3dsFiles.ECar1,                      new LibVertex( 20.0f,           19.0f,          0.01f   ), 0.0f,    Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.ECar1,                      new LibVertex( 25.0f,           19.0f,          0.01f   ), 0.0f,    Scalation.EHigherHalf, CollisionEnabled.YES, WallAction.ENone           ),
*/
                        }
                    ),
                },

                //ELevel2
                new WallCollection[]
                {
                    new WallCollection
                    (
                        new Wall[]
                        {
                            //the level
                            new Wall( D3dsFiles.ELevelDesert1,              new LibVertex( 0.0f,    0.0f,   0.0f    ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
/*
                            //office3
                            new Wall( D3dsFiles.EExecutiveOffice1,          new LibVertex( 0.0f + 14.0f,   15.0f + 8.0f,   0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EExecutiveOffice1GlassDoor, new LibVertex( 0.0f + 7.95f,   15.0f + 9.225f, 0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.EDoorSliding    ),
                            new Wall( D3dsFiles.EDesk1,                     new LibVertex( 0.0f + 12.0f,   15.0f + 9.0f,   0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EPlant1,                    new LibVertex( 0.0f + 10.0f,   15.0f + 8.0f,   0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EShelves1,                  new LibVertex( 0.0f + 8.0f,    15.0f + 6.0f,   0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.EExecutiveOffice1GlassDoor, new LibVertex( 0.0f + 14.0f,   15.0f + 9.225f, 0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.EDoorSliding    ),

                            //cars
                            new Wall( D3dsFiles.ECar1,                      new LibVertex( 20.0f,   19.0f,  0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.ECar2,                      new LibVertex( 20.0f,   22.0f,  0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.ECar3,                      new LibVertex( 20.0f,   25.0f,  0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
*/

/*
                            new Wall( D3dsFiles.ECar1,                      new LibVertex( 20.0f,   28.0f,  0.01f   ), CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.ECar2,                      new LibVertex( 20.0f,   31.0f,  0.01f   ), CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.ECar3,                      new LibVertex( 20.0f,   34.0f,  0.01f   ), CollisionEnabled.YES, WallAction.ENone           ),

                            new Wall( D3dsFiles.ECar1,                      new LibVertex( 20.0f,   37.0f,  0.01f   ), CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.ECar2,                      new LibVertex( 20.0f,   40.0f,  0.01f   ), CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( D3dsFiles.ECar3,                      new LibVertex( 20.0f,   43.0f,  0.01f   ), CollisionEnabled.YES, WallAction.ENone           ),
*/
                        }
                    ),
                },

                //ELevel3
                new WallCollection[]
                {
                    new WallCollection
                    (
                        new Wall[]
                        {
                            //the level
                            new Wall( D3dsFiles.ELevelDispatch1,              new LibVertex( 0.0f,    0.0f,   0.0f    ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),

                            new Wall( D3dsFiles.ECar1,                      new LibVertex( 20.0f,   22.0f,  0.01f   ), 0.0f, Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                        }
                    ),
                },
            };
        }

        private                 Wall[]              walls                          = null;

        public static final WallCollection[] getLevelWalls( LevelConfig config )
        {
            WallCollection[] ret = LEVEL_MESH_COLLECTION_DATA[ config.ordinal() ];
            return ret;
        }

        public WallCollection( Wall[] initWalls )
        {
            walls = initWalls;
        }

        public final void draw()
        {
            //draw all meshes
            for ( Wall wall : walls )
            {
                wall.iMeshes.draw();
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
