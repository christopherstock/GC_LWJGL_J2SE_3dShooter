/*  $Id: Path.java 181 2010-11-13 13:31:37Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.Lib.Scalation;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.g3d.LibCollision.*;
    import de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
    import de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.objects.GameObject.WallAction;
    import de.christopherstock.shooter.game.objects.Item.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   The current version enumeration.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class ShooterLevels
    {
        public  static          WallCollection[][]  LEVEL_MESH_COLLECTION_DATA      = null;


        public static enum LevelConfig
        {
            ELevel1
            (
                new ViewSet( 6.5f, 4.0f, PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 180.0f ),
                BackGround.ECity,
                new Item[]
                {
                    new Item( ItemTemplate.EAmmoBullet9mm,      1.0f, 1.0f,  0.0f + 0.01f ),
                    new Item( ItemTemplate.EAmmoShotgunShells,  1.0f, 2.5f,  0.0f + 0.01f ),
                    new Item( ItemTemplate.EWearponPistol9mm,   1.0f, 4.0f,  0.0f + 0.01f ),
                    new Item( ItemTemplate.EWearponShotgun,     1.0f, 5.5f,  0.0f + 0.01f ),

                }
            ),
            ELevel2( new ViewSet( 2.0f, 2.0f,  PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 270.0f ), BackGround.EMeadow,   null ),
            ELevel3( new ViewSet( 5.0f, 5.0f,  PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 180.0f ), BackGround.EMeadow, null ),
            ;

            public                  ViewSet             iStartPosition      = null;
            public                  BackGround          iBg                 = null;
            public                  Item[]              iItems              = null;

            private LevelConfig( ViewSet aStartPosition, BackGround aBg, Item[] aItems )
            {
                iStartPosition  = aStartPosition;
                iBg             = aBg;
                iItems          = aItems;
            }
        }

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
                            new Wall(   Others.ELevelDispatch1,              new LibVertex( 0.0f,    0.0f,   0.0f    ), 0.0f, Lib.Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall(   Others.ECar1,               new LibVertex(  1.0f,   1.0f,   0.0f    ),  0.0f,  Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                        }
                    ),
/*
                    new WallCollection
                    (
                        new Wall(       Others.EExecutiveOffice3,    new LibVertex(  5.0f,   15.0f,  0.001f   ),  180.0f,   Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                        new Wall[]
                        {
                            new Wall(   Others.EDesk1,               new LibVertex(  4.0f,   2.0f,   0.0f    ),  0.0f, Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                        }
                    ),

                    new WallCollection
                    (
                        new Wall(       Others.EExecutiveOffice3,    new LibVertex(  5.0f,   25.0f,  0.001f   ),  180.0f,   Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                        new Wall[]
                        {
                            new Wall(   Others.EDesk1,               new LibVertex(  4.0f,   2.0f,   0.0f    ),  0.0f, Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                        }
                    ),

                    new WallCollection
                    (
                        new Wall(       Others.EExecutiveOffice3,        new LibVertex(  5.0f,   35.0f,  0.001f   ), 180.0f,   Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                        new Wall[]
                        {
                            new Wall(   Others.EDesk1,                   new LibVertex(  4.0f,   2.0f,   0.0f    ),  0.0f, Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall( Others.EExecutiveOffice1GlassDoor, new LibVertex(  0.0f,   4.0f,   0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.EDoorSliding    )
                        }
                    ),

                    new WallCollection
                    (
                        new Wall(       Others.EExecutiveOffice2,    new LibVertex(  8.0f,   5.0f,  0.001f   ),  0.0f,   Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                        new Wall[]
                        {
                            new Wall(   Others.EDesk1,               new LibVertex(  2.0f,   2.0f,   0.0f    ),  0.0f, Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall(   Others.EDesk1,               new LibVertex(  5.0f,   2.0f,   0.0f    ),  0.0f,  Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall(   Others.EDesk1,               new LibVertex(  8.0f,   2.0f,   0.0f    ),  0.0f,  Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall(   Others.EExecutiveOffice1GlassDoor, new LibVertex(  0.0f,           5.0f,          0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.EDoorSliding    ),
                            new Wall(   Others.EExecutiveOffice1GlassDoor, new LibVertex(  0.0f,           11.0f,          0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.EDoorSliding    ),
                        }
                    ),
*/
                    new WallCollection
                    (
                        new Wall[]
                        {
                            new Wall( Others.EFloorMarble3x10,           new LibVertex(  5.0f,           5.0f,           0.001f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall( Others.EFloorMarble3x10,           new LibVertex(  5.0f,           15.0f,          0.001f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall( Others.EFloorMarble3x10,           new LibVertex(  5.0f,           25.0f,          0.001f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),

                            new Wall( Others.ECeilingTiles3x10,          new LibVertex(  5.0f,           5.0f,           2.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall( Others.ECeilingTiles3x10,          new LibVertex(  5.0f,           15.0f,          2.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                            new Wall( Others.ECeilingTiles3x10,          new LibVertex(  5.0f,           25.0f,          2.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),

                            new Wall(   Others.EExecutiveOffice1GlassDoor, new LibVertex(  10.0f,           2.0f,          0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.EDoorSliding, LibTransformationMode.EOriginalsToOriginals ),
                          //new Wall(   Others.EStairs1,                   new LibVertex(  20.0f,           4.0f,          0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone, LibTransformationMode.EOriginalsToOriginals ),

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
                            new Wall( Others.ELevelDesert1,              new LibVertex( 0.0f,    0.0f,   0.0f    ), 0.0f, Lib.Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone           ),
                        }
                    ),
                },

                //ELevel2
                new WallCollection[]
                {
                            //the level
/*
                            new Wall( Test.EShelves1,                  new LibVertex(  18.0f -0.0f,    17.0f   - 0.0f,  0.01f   ), 0.0f,   Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),

                            new Wall( Test.ELevelParkingLot1,        new LibVertex(  0.0f,           0.0f,           0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.YES, WallAction.ENone           ),
                            new Wall( Test.EItemShotgun,               new LibVertex(  2.0f,           3.0f,           0.2f    ), 45.0f,   Scalation.ENone, CollisionEnabled.NO,  WallAction.ENone           ),
                            new Wall( Test.EItemPistol1,               new LibVertex(  3.0f,           3.0f,           0.2f    ), 45.0f,   Scalation.ENone, CollisionEnabled.NO,  WallAction.ENone           ),
                            new Wall( Test.EItemAmmoShotgunShells,     new LibVertex(  4.0f,           3.0f,           0.2f    ), 45.0f,   Scalation.ENone, CollisionEnabled.NO,  WallAction.ENone           ),
                            new Wall( Test.EItemAmmoBullet9mm,         new LibVertex(  5.0f,           3.0f,           0.2f    ), 45.0f,   Scalation.ENone, CollisionEnabled.NO,  WallAction.ENone           ),
*/
                },



            };
        }

        public static final WallCollection[] getLevelWalls( LevelConfig config )
        {
            WallCollection[] ret = LEVEL_MESH_COLLECTION_DATA[ config.ordinal() ];
            return ret;
        }
    }
