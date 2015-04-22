/*  $Id: ShooterLevels.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.lib.Lib.Scalation;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.ShooterItem.*;
    import  de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.g3d.face.LibFace.DrawMethod;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.objects.GameObject.WallAction;
    import  de.christopherstock.shooter.game.objects.GameObject.WallClimbable;
    import  de.christopherstock.shooter.game.objects.GameObject.WallCollidable;
    import  de.christopherstock.shooter.game.objects.GameObject.WallDestroyable;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The current version enumeration.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class ShooterLevels
    {
        public  static          WallCollection[][]  LEVEL_MESH_COLLECTION_DATA      = null;


        public static enum LevelConfig
        {
            ELevel1
            (
                "Al-Kir'ānah, Qatar - hideout",
                new ViewSet( 3.0f, 59.0f, 0.0f, 0.0f, 0.0f, 225.0f ),
                BackGround.EQatar_hideout,
                new ShooterItem[]
                {
                    //new ShooterItem( ItemTemplate.EItemCrackers,        112.7f,      115.5f,    0.85f, 0.0f ),
                    new ShooterItem( ItemTemplate.EAmmoBullet44mm,      3.0f, 50.0f,  0.0f + 0.01f, 0.0f, Rotating.EYes ),
                    new ShooterItem( ItemTemplate.EAmmoMagnumBullet,    3.0f, 51.0f,  0.0f + 0.01f, 0.0f, Rotating.EYes ),
                    new ShooterItem( ItemTemplate.EAmmoShotgunShell,    3.0f, 52.0f,  0.0f + 0.01f, 0.0f, Rotating.EYes ),
                    new ShooterItem( ItemTemplate.EWearponPistol9mm,    3.0f, 53.0f,  0.0f + 0.01f, 0.0f, Rotating.ENo  ),
                    new ShooterItem( ItemTemplate.EWearponPistol2,      3.0f, 54.0f,  0.0f + 0.01f, 0.0f, Rotating.ENo  ),
                    new ShooterItem( ItemTemplate.EWearponShotgun,      3.0f, 55.0f,  0.0f + 0.01f, 0.0f, Rotating.ENo  ),
                    new ShooterItem( ItemTemplate.EWearponKnife,        3.0f, 56.0f,  0.0f + 0.01f, 0.0f, Rotating.ENo  ),

                    //new ShooterItem( ItemTemplate.EAmmoBullet9mm,       115.0f,  98.0f,  0.0f + 0.01f, 0.0f ),
                    //new ShooterItem( ItemTemplate.EAmmoBullet51mm,      115.0f, 100.0f,  0.0f + 0.01f, 0.0f ),
                    //new ShooterItem( ItemTemplate.EAmmoBullet792mm,     115.0f, 101.0f,  0.0f + 0.01f, 0.0f ),
                },
                new Artefact[]  { Artefact.EMobilePhoneSEW890i },
                new ItemEvent[] {}
            ),

            ELevel2
            (
                "Hacker's Headquarters",
                new ViewSet( 10.0f, 10.0f,  5.0f, 0.0f, 0.0f, 270.0f ),
                BackGround.EQatar_hideout,
                null,
                new Artefact[]  { Artefact.EWaltherPPK, Artefact.EMagnum357, Artefact.EHuntingRifle, },
                new ItemEvent[] { ItemEvent.EGainAmmo20Bullet44mm, ItemEvent.EGainAmmo18MagnumBullet, ItemEvent.EGainAmmo20Bullet51mm,  }
            ),
            //ELevel3( "", new ViewSet( 5.0f, 5.0f,  0.0f, 0.0f, 0.0f, 180.0f ), BackGround.EMeadow, null ),
            ;

            public                  String              iDesc                   = null;
            public                  ViewSet             iStartPosition          = null;
            public                  BackGround          iBg                     = null;
            public                  Artefact[]          iStartupWearpons        = null;
            public                  ShooterItem[]       iItems                  = null;
            public                  ItemEvent[]         iStartupItems           = null;

            private LevelConfig( String aDesc, ViewSet aStartPosition, BackGround aBg, ShooterItem[] aItems, Artefact[] aStartupWearpons, ItemEvent[] aStartupItems )
            {
                iDesc            = aDesc;
                iStartPosition   = aStartPosition;
                iBg              = aBg;
                iItems           = aItems;
                iStartupWearpons = aStartupWearpons;
                iStartupItems    = aStartupItems;
            }
        }

        public static final void init()
        {
            LEVEL_MESH_COLLECTION_DATA = new WallCollection[][]
            {
                //ELevel1, Al-Kir'ānah, Qatar
                new WallCollection[]
                {
                    //the level
                    new WallCollection
                    (
                        new Wall[]
                        {
                            new Wall(   Others.ELevelQatar1,    new LibVertex( 0.0f,    0.0f,   0.0f    ),  0.0f, Scalation.ENone,  WallCollidable.EYes,  WallAction.ENone, WallDestroyable.ENo, WallClimbable.EYes, DrawMethod.EAlwaysDraw    ),
                            new Wall(   Others.ELevelBounds,    new LibVertex( 0.0f,    45.0f,  0.0f    ),  0.0f, Scalation.ENone,  WallCollidable.EYes,  WallAction.ENone, WallDestroyable.ENo, WallClimbable.EYes, DrawMethod.EInvisible     ),
                            new Wall(   Others.ELevelBounds,    new LibVertex( 0.0f,    720.0f, 0.0f    ),  0.0f, Scalation.ENone,  WallCollidable.EYes,  WallAction.ENone, WallDestroyable.ENo, WallClimbable.EYes, DrawMethod.EInvisible     ),
                            new Wall(   Others.ERoad1,          new LibVertex( 12.0f,   0.0f,   0.01f   ),  0.0f, Scalation.ENone,  WallCollidable.EYes,   WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo,  DrawMethod.EAlwaysDraw    ),
                        }
                    ),

                    new WallCollection
                    (
                        new Wall[]
                        {
                            new Wall(   Others.ESign1,          new LibVertex( 10.5f,   80.0f,   0.01f   ),  0.0f            ),
                            new Wall(   Others.ESign2,          new LibVertex( 10.5f,   100.0f,  0.01f   ),  0.0f            ),
                            new Wall(   Others.ECar2,           new LibVertex( 8.0f,    60.0f,  0.0f    ),  90.0f         , Scalation.ENone,  WallCollidable.EYes,   WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo,  DrawMethod.EAlwaysDraw    ),

                            new Wall(   Others.ECactus1,        new LibVertex( 20.0f,   55.0f,   0.0f    ),  0.0f            ),
                            new Wall(   Others.ECactus2,        new LibVertex( 20.0f,   50.0f,   0.0f    ),  0.0f            ),

                            new Wall(   Others.EPalm1,          new LibVertex( 20.0f,   60.0f,   0.0f    ),  0.0f          , Scalation.ENone,       WallCollidable.EYes,   WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo,  DrawMethod.EAlwaysDraw    ),
                            new Wall(   Others.EPalm1,          new LibVertex( 20.0f,   65.0f,   0.0f    ),  0.0f          , Scalation.ENone,       WallCollidable.EYes,   WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo,  DrawMethod.EAlwaysDraw    ),
                            new Wall(   Others.EPalm1,          new LibVertex( 20.0f,   70.0f,   0.0f    ),  0.0f          , Scalation.ENone,       WallCollidable.EYes,   WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo,  DrawMethod.EAlwaysDraw    ),
                            new Wall(   Others.EPalm1,          new LibVertex( 20.0f,   75.0f,   0.0f    ),  0.0f          , Scalation.ENone,       WallCollidable.EYes,   WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo,  DrawMethod.EAlwaysDraw    ),
                            new Wall(   Others.EPalm1,          new LibVertex( 20.0f,   80.0f,   0.0f    ),  0.0f          , Scalation.ENone,       WallCollidable.EYes,   WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo,  DrawMethod.EAlwaysDraw    ),

                            new Wall(   Others.ECrop3,          new LibVertex( 30.0f,  70.0f,    0.0f    ),  0.0f             ),
                            new Wall(   Others.ECrop3,          new LibVertex( 30.0f,  80.0f,    0.0f    ),  0.0f             ),
                            new Wall(   Others.ECrop3,          new LibVertex( 30.0f,  70.0f,    0.0f    ),  270.0f           ),
                            new Wall(   Others.ECrop3,          new LibVertex( 30.0f,  92.0f,    0.0f    ),  270.0f           ),
                            new Wall(   Others.ECrop3,          new LibVertex( 40.0f,  92.0f,    0.0f    ),  270.0f           ),
                            new Wall(   Others.ECrop3,          new LibVertex( 50.0f,  92.0f,    0.0f    ),  270.0f           ),
                            new Wall(   Others.ECrop3,          new LibVertex( 60.0f,  92.0f,    0.0f    ),  270.0f           ),
                            new Wall(   Others.ECrop3,          new LibVertex( 71.0f,  80.0f,    0.0f    ),  0.0f             ),
                            new Wall(   Others.ECrop3,          new LibVertex( 71.0f,  70.0f,    0.0f    ),  0.0f             ),

                            new Wall(   Others.ERock1,          new LibVertex( 30.0f,  100.0f,    0.0f    ),  0.0f            ),
                            new Wall(   Others.ERock1,          new LibVertex( 30.0f,  130.0f,    0.0f    ),  0.0f            ),

                            new Wall(   Others.EFence1,                     new LibVertex(  2.0f,  46.0f,    0.0f    ), 0.0f            ),

                            new Wall(   Others.ETree1,                      new LibVertex(  2.0f,  60.0f,    0.0f    ), 0.0f            ),

                            new Wall(   Others.ETree1,                      new LibVertex(  4.0f,  70.0f,    0.0f    ),  0.0f            ),
                            new Wall(   Others.ETree1,                      new LibVertex(  4.0f,  75.0f,    0.0f    ),  0.0f            ),
                            new Wall(   Others.ETree1,                      new LibVertex(  4.0f,  80.0f,    0.0f    ),  0.0f            ),
                            new Wall(   Others.ETree1,                      new LibVertex(  5.5f,  81.5f,    0.0f    ), 90.0f            ),
/*
                            new Wall(   Others.EExecutiveOffice1GlassDoor,  new LibVertex(  7.5f,  95.0f,    0.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.EDoorSliding, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant ),
                            new Wall(   Others.EFence1,                     new LibVertex(  8.0f,  95.0f,    0.0f    ), 0.0f            ),
                            new Wall(   Others.EExecutiveOffice1GlassDoor,  new LibVertex(  8.5f,  95.0f,    0.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.EDoorSliding, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant ),
                            new Wall(   Others.EFence1,                     new LibVertex(  9.0f,  95.0f,    0.0f    ), 0.0f            ),
*/

/*
                            new Wall(   Others.ECrop2,          new LibVertex( 34.0f,  70.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop2,          new LibVertex( 32.0f,  70.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop2,          new LibVertex( 30.0f,  70.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop2,          new LibVertex( 30.0f,  72.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop2,          new LibVertex( 30.0f,  74.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop2,          new LibVertex( 30.0f,  76.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop2,          new LibVertex( 30.0f,  78.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop2,          new LibVertex( 30.0f,  80.0f,     0.0f    ),  0.0f            ),
*/
/*
                            new Wall(   Others.ECrop1,          new LibVertex( 20.0f,  100.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop1,          new LibVertex( 22.0f,  100.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop1,          new LibVertex( 20.0f,  102.0f,     0.0f    ),  0.0f            ),
                            new Wall(   Others.ECrop1,          new LibVertex( 22.0f,  102.0f,     0.0f    ),  0.0f            ),
*/


                            //new Wall(   Others.ECrop2,          new LibVertex( 3.0f,    20.0f,     0.0f    ),  0.0f            ),
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
                            new Wall(   Others.ETestFace,           new LibVertex(  -2.0f,   0.0f,   0.0f    ),  45.0f  )
                        }
                    ),



                    new WallCollection
                    (
                        new Wall[]
                        {
                        }
                    ),


                    new WallCollection
                    (
                        new Wall[]
                        {
                            //the level
                            new Wall(   Others.ELevelDispatch1,     new LibVertex( 0.0f,    0.0f,   0.0f    ), 0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.EYes, DrawMethod.EAlwaysDraw           ),
                            new Wall(   Others.ECar1,               new LibVertex(  -5.0f,   -5.0f,   0.0f    ),  0.0f,  Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant     ),
                        }
                    ),


                    new WallCollection
                    (
                        new Wall(       Others.EExecutiveOffice3,    new LibVertex(  5.0f,   15.0f,  0.01f   ),  180.0f,   Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                        new Wall[]
                        {
                            new Wall(   Others.EDesk1,               new LibVertex(  4.0f,   2.0f,   0.0f    ),  0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                        }
                    ),

                    new WallCollection
                    (
                        new Wall(       Others.EExecutiveOffice3,    new LibVertex(  5.0f,   25.0f,  0.01f   ),  180.0f,   Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                        new Wall[]
                        {
                            new Wall(   Others.EDesk1,               new LibVertex(  4.0f,   2.0f,   0.0f    ),  0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                            new Wall(   Others.EShelves1,            new LibVertex(  5.0f,   5.0f,   0.0f    ),  0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                        }
                    ),


                    new WallCollection
                    (
                        new Wall(       Others.EExecutiveOffice3,        new LibVertex(  5.0f,   35.0f,  0.01f   ), 120.0f,   Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                        new Wall[]
                        {
                            new Wall(   Others.EDesk1,                   new LibVertex(  4.0f,   2.0f,   0.0f    ),  0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                            new Wall(   Others.EExecutiveOffice1GlassDoor, new LibVertex(  0.0f,   4.0f,   0.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.EDoorSliding, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant    )
                        }
                    ),


                    new WallCollection
                    (
                        new Wall(       Others.EExecutiveOffice2,    new LibVertex(  8.0f,   5.0f,  0.01f   ),  0.0f,   Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                        new Wall[]
                        {
                            new Wall(   Others.EDesk1,               new LibVertex(  2.0f,   2.0f,   0.0f    ),  0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                            new Wall(   Others.EDesk1,               new LibVertex(  5.0f,   2.0f,   0.0f    ),  0.0f,  Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                            new Wall(   Others.EDesk1,               new LibVertex(  8.0f,   2.0f,   0.0f    ),  0.0f,  Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                            new Wall(   Others.EExecutiveOffice1GlassDoor, new LibVertex(  0.0f,           5.0f,          0.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.EDoorSliding, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant    ),
                            new Wall(   Others.EExecutiveOffice1GlassDoor, new LibVertex(  0.0f,           11.0f,          0.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.EDoorSliding, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant    ),
                        }
                    ),


                    new WallCollection
                    (
                        new Wall[]
                        {
                            new Wall(   Others.EFloorMarble3x10,            new LibVertex(  5.0f,           5.0f,           0.01f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                            new Wall(   Others.EFloorMarble3x10,            new LibVertex(  5.0f,           15.0f,          0.01f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                            new Wall(   Others.EFloorMarble3x10,            new LibVertex(  5.0f,           25.0f,          0.01f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),

                            new Wall(   Others.ECeilingTiles3x10,           new LibVertex(  5.0f,           5.0f,           2.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant           ),
                            new Wall(   Others.ECeilingTiles3x10,           new LibVertex(  5.0f,           15.0f,          2.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant          ),
                            new Wall(   Others.ECeilingTiles3x10,           new LibVertex(  5.0f,           25.0f,          2.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo, DrawMethod.EHideIfTooDistant          ),

                            new Wall(   Others.EExecutiveOffice1GlassDoor,  new LibVertex(  10.0f,           2.0f,          0.0f    ), 0.0f,    Scalation.ENone, WallCollidable.EYes, WallAction.EDoorSliding, WallDestroyable.EYes, WallClimbable.ENo, DrawMethod.EHideIfTooDistant ),

                          //new Wall(   Others.ETrashBin,                   new LibVertex(  10.0f,           5.0f,          0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone, LibTransformationMode.EOriginalsToOriginals, true ),
                          //new Wall(   Others.EStairs1,                   new LibVertex(  20.0f,           4.0f,          0.0f    ), 0.0f,    Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone, LibTransformationMode.EOriginalsToOriginals ),

                        }
                    ),

                },

/*
                    //hacker's hideout
                    new WallCollection
                    (
                        new Wall(   Others.EContainer1,     new LibVertex( 65.0f,      55.0f,     0.01f      ),  315.0f, Scalation.ENone,  WallCollidable.EYes,  WallAction.ENone, WallDestroyable.ENo, WallClimbable.ENo            ),
                        new Wall[]
                        {
                            new Wall(   Others.EChairOffice1,   new LibVertex( 5.0f,      5.0f,     0.1f        ),  0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.EYes, WallClimbable.ENo  ),
                            new Wall(   Others.ETable2,         new LibVertex( 4.0f,      4.0f,     0.0f        ),  0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.EYes, WallClimbable.ENo  ),
                            new Wall(   Items.EApple,           new LibVertex( 3.8f,      4.7f,     0.85f       ),  0.0f, Scalation.ENone, WallCollidable.EYes, WallAction.ENone, WallDestroyable.EYes, WallClimbable.ENo  ),
                        }
                    ),
*/
/*
                    new WallCollection
                    (
                        new Wall(   Others.EContainer1,     new LibVertex( 110.0f,      110.0f,     0.01f      ),  0.0f, Scalation.ENone,  CollisionEnabled.EYes,  WallAction.ENone, WallDestroyable.ENo            ),
                        new Wall[]
                        {
                            new Wall(   Others.EChairOffice1,   new LibVertex( 5.0f,      5.0f,     0.1f        ),  0.0f, Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone, WallDestroyable.EYes  ),
                            new Wall(   Others.ETable2,         new LibVertex( 4.0f,      4.0f,     0.0f        ),  0.0f, Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone, WallDestroyable.EYes  ),
                            new Wall(   Items.EApple,           new LibVertex( 3.8f,      4.7f,     0.85f       ),  0.0f, Scalation.ENone, CollisionEnabled.EYes, WallAction.ENone, WallDestroyable.EYes  ),
                        }
                    ),
*/
            };
        }

        public static final WallCollection[] getLevelWalls( LevelConfig config )
        {
            WallCollection[] ret = LEVEL_MESH_COLLECTION_DATA[ config.ordinal() ];
            return ret;
        }
    }
