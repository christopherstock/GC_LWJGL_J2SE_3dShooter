/*  $Id: ShooterLevelTestOfficeCasino.java 1283 2014-10-08 21:21:35Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.level.setup;

    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.base.ShooterTexture.WallTex;
    import  de.christopherstock.shooter.base.ShooterWallCollection.DoorStyle;
    import  de.christopherstock.shooter.base.ShooterWallCollection.WallStyle;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.Lib.Invert;
    import  de.christopherstock.lib.Lib.Scalation;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.fx.LibFX.FXSize;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.g3d.face.LibFace.*;
    import  de.christopherstock.lib.gl.LibTexture;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.base.ShooterD3ds.Others;
    import  de.christopherstock.shooter.g3d.mesh.WallCollection;
    import  de.christopherstock.shooter.g3d.wall.Article;
    import  de.christopherstock.shooter.g3d.wall.Sprite;
    import  de.christopherstock.shooter.g3d.wall.Wall;
    import  de.christopherstock.shooter.g3d.wall.WallHealth;
    import  de.christopherstock.shooter.g3d.wall.Wall.WallAction;
    import  de.christopherstock.shooter.g3d.wall.Wall.WallClimbable;
    import  de.christopherstock.shooter.g3d.wall.Wall.WallCollidable;
    import 	de.christopherstock.shooter.game.artefact.ArtefactType;
    import  de.christopherstock.shooter.game.bot.*;
    import  de.christopherstock.shooter.game.bot.BotFactory.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.io.sound.*;
    import  de.christopherstock.shooter.level.*;
    import  de.christopherstock.shooter.level.Level.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   All settings for level 'Test Office Casino'.
    *
    *   @version    0.3.11
    *   @author     Christopher Stock
    **************************************************************************************/
    public class LevelSetupTestOfficeCasino extends LevelSetup
    {
        private     static      final   int         SECTION_ONE                 = 0;
        private     static      final   int         SECTION_TWO                 = 1;

        public      static      final   int         GENERAL                     = 0;
        public      static      final   int         OFFICE_PARTNER_1            = 1;
        public      static      final   int         OFFICE_PARTNER_2            = 2;

        @Override
        public final LevelConfigMain createNewLevelConfig()
        {
            return new LevelConfigMain
            (
                "Player's Office",
                new ViewSet( 7.0f, 0.0f, 2.5f, 0.0f, 0.0f, 120.0f ),
                new ArtefactType[]   {  ArtefactType.ETranquilizerGun,  ArtefactType.EWaltherPPK,  ArtefactType.EMagnum357, ArtefactType.EAutoShotgun, ArtefactType.ESpaz12,  ArtefactType.ERCP180,  ArtefactType.ESniperRifle, ArtefactType.EAdrenaline, },
                new ItemEvent[]  {  ItemEvent.EGainAmmo20TranquilizerDarts, ItemEvent.EGainAmmo20TranquilizerDarts, ItemEvent.EGainAmmo20TranquilizerDarts, ItemEvent.EGainAmmo20TranquilizerDarts, ItemEvent.EGainAmmo20Bullet44mm,  ItemEvent.EGainAmmo20Bullet44mm, ItemEvent.EGainAmmo20Bullet44mm,ItemEvent.EGainAmmo20Bullet44mm,ItemEvent.EGainAmmo20Bullet44mm, ItemEvent.EGainAmmo40ShotgunShells, ItemEvent.EGainAmmo40ShotgunShells, ItemEvent.EGainAmmo40ShotgunShells, ItemEvent.EGainAmmo18MagnumBullet, ItemEvent.EGainGadgetHandset1, ItemEvent.EGainGadgetCrackers, ItemEvent.EGainAmmo120Bullet792mm, ItemEvent.EGainAmmo120Bullet792mm, },
                InvisibleZeroLayerZ.ENo,
                SoundBg.EExtraction

                //new ViewSet( x + 0.0f, y + 0.0f, 0.0f, 0.0f, 0.0f, 270.0f ),
/*
                new ItemToPickUp[]
                {
                    new ItemToPickUp( ItemKind.EGameEventLevel1ChangeToNextSection, null, 2.0f, 15.0f, 0.0f, 0.0f, Rotating.ENo ),

                    //new PickUpItem( ItemTemplate.EWearponShotgun, 5.0f, 10.0f, 3.3f, 0.0f, Rotating.EYes ),
                    //new PickUpItem( ItemTemplate.EGameEventLevel1ExplainAction, 4.2f, 2.1f,  0.8f, 0.0f, Rotating.EYes ),
                    //new PickUpItem( ItemTemplate.EGameEventLevel1AcclaimPlayer, 6.0f, 12.0f, 2.5f, 0.0f, Rotating.EYes ),
                },
*/
/*
                new ShooterBotFactory[]
                {
                    new ShooterBotFactory( BotPlayers.OFFICE_PARTNER_1, BotKind.EUnitPilot,          new LibVertex( 5.0f,  1.0f, 2.5f ), 225.0f, new BotAction[] { new BotUseAction( BotEvent.ELevel1AcclaimPlayer ), new BotGiveAction( ArtefactType.EMobilePhoneSEW890i, BotEvent.ETakeMobileTest ), new BotGiveAction( ArtefactType.EChips, BotEvent.ETakeCrackersTest ) } ),
                    new ShooterBotFactory( BotPlayers.OFFICE_PARTNER_2, BotKind.EUnitOfficeEmployee, new LibVertex( 3.0f,  0.0f, 2.5f ), 225.0f, new BotAction[] { new BotUseAction( BotEvent.ELevel1AcclaimPlayer ), new BotGiveAction( ArtefactType.EMobilePhoneSEW890i, BotEvent.ETakeMobileTest ), new BotGiveAction( ArtefactType.EChips, BotEvent.ETakeCrackersTest ) } ),

                    //new ShooterBotFactory( BotPlayers.OFFICE_PARTNER_1, BotKind.EUnitSecurity,          new LibVertex( 4.0f,  2.0f, 2.5f ), 225.0f, new BotAction[] {  } ),
                    //new ShooterBotFactory( BotPlayers.GENERAL,          BotKind.EUnitSecurity, new LibVertex( 5.75f, 2.0f, 2.5f ), 180.0f, new BotAction[] {} ),
                    //new ShooterBotFactory( BotPlayers.GENERAL,          BotKind.EUnitSecurity, new LibVertex( 7.75f, 2.0f, 2.5f ), 180.0f, new BotAction[] {} ),
                    //new ShooterBotFactory( BotPlayers.GENERAL,          BotKind.EUnitSecurity, new LibVertex( 1.75f, 2.0f, 2.5f ), 180.0f, new BotAction[] {} ),
                    //new ShooterBotFactory( BotKind.EUnitSpecialForces,     new LibVertex( 6.5f, 62.5f, 0.0f + 0.001f ) ),
                    //new ShooterBotFactory( BotKind.EUnitPilot,             new LibVertex( 7.5f, 2.5f, 0.0f + 0.001f ), 90.0f ),
                    //new ShooterBotFactory( BotKind.EUnitOfficeEmployee,    new LibVertex( 4.5f, 62.5f, 0.0f + 0.001f ) ),
                    //new ShooterBotFactory( BotKind.EUnitOfficeEmployee,    new LibVertex( 3.5f, 62.5f, 0.0f + 0.001f ) ),
                    //new ShooterBotFactory( BotKind.EUnitOfficeEmployee,    new LibVertex( 2.5f, 62.5f, 0.0f + 0.001f ) ),

                },
*/
            );
        }

        @Override
        public final WallCollection[] createNewGlobalMeshData()
        {
            return new WallCollection[]
            {
                //ground
                ShooterWallCollection.createGround( WallTex.EGrass1, -0.01f ),

                //elevator
                ShooterWallCollection.createElevator(       1.0f,     13.0f,   2.5f, 0.0f, WallTex.EMarble1, WallTex.EWood1, WallAction.EElevatorDown, WallTex.EBricks2, WallTex.ECeiling1, SECTION_ONE, SECTION_TWO ),
/*
                //walls around elevator
                ShooterWallCollection.createRoom
                (
                    x + 3.0f,   y + 14.0f,   z + 2.5f,   270.0f,   1,  1,
                    WallStyle.ENoWall, WallStyle.ENoWall, WallStyle.ENoWall, WallStyle.ESolidWall,
                    WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                    DoorStyle.ENoDoor, 0,
                    WallTex.EBricks2, null, null,
                    null
                ),
                ShooterWallCollection.createRoom
                (
                    x + 0.0f,   y + 14.0f,   z + 2.5f,   270.0f,   1,  1,
                    WallStyle.ENoWall, WallStyle.ENoWall, WallStyle.ENoWall, WallStyle.ESolidWall,
                    WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                    DoorStyle.ENoDoor, 0,
                    WallTex.EBricks2, null, null,
                    null
                ),
*/
            };
        }

        @Override
        public final WallCollection[][] createNewSectionMeshData()
        {
            return new WallCollection[][]
            {
                //EPlayersOffice
                new WallCollection[]
                {
                    //office desk ( must be a separate wallCollection! .. cannot be applied to small office?
                    new WallCollection
                    (
//                          new Door(       Others.EDoor1,    +2.0f,    -3.0f,    2.5f, 90.0f, WallCollidable.EYes, WallAction.EDoorSlideLeft, WallClimbable.ENo, WallTex.EWood1, WallEnergy.WallHealth.EUnbreakale, null, null, true ),
                        new Wall[]
                        {
                        }
                    ),

                    //casino
                    ShooterWallCollection.createRoom
                    (
                        iOffsetX + 0.0f,   iOffsetY + 0.0f,   iOffsetZ + 2.5f,   0.0f,   9,  14,
                        WallStyle.ENoWall, WallStyle.ESolidWall, WallStyle.ENoWall, WallStyle.EWindowsAndCeilingWindows,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSlideLeft,
                        DoorStyle.EAnchorDefault, 6,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                            new Sprite( Others.ESprite1, new LibVertex( 5.5f, 0.75f, 0.0f ), Scalation.EAddThird,   WallCollidable.EYes, WallTex.EPlant1 ),
                            new Wall(   Others.ESodaMachine1,   new LibVertex( 7.5f, 0.5f,  0.0f ), 270.0f, Scalation.ENone,   Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.ESodaMachine2,   null, 0,         WallHealth.EVendingMachine, FXSize.ELarge, SoundFg.EExplosion1 ),

                            new Sprite( Others.ESprite1, new LibVertex( 8.0f, 3.0f,  0.0f ), Scalation.EAddHalf,   WallCollidable.EYes, WallTex.EPlant2 ),
                            new Sprite( Others.ESprite1, new LibVertex( 8.0f, 7.5f,  0.0f ), Scalation.EAddHalf,   WallCollidable.EYes, WallTex.EPlant1 ),
                            new Sprite( Others.ESprite1, new LibVertex( 8.0f, 12.5f, 0.0f ), Scalation.EAddHalf,   WallCollidable.EYes, WallTex.EPlant2 ),
                            new Wall(   Others.ESofa1,          new LibVertex( 8.5f, 10.0f, 0.0f ), 0.0f,   Scalation.ENone,   Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EClothDarkRed, new LibTexture[] { WallTex.ETest, }, 0, WallHealth.EFurniture,      FXSize.ELarge, null                     ),
                            new Wall(   Others.ESofa1,          new LibVertex( 8.5f, 5.0f,  0.0f ), 0.0f,   Scalation.ENone,   Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EClothDarkRed, new LibTexture[] { WallTex.ETest, }, 0, WallHealth.EFurniture,      FXSize.ELarge, null                     ),
                        },
                        null,
                        null,
                        null,
                        null
                    ),

                    //big office
                    ShooterWallCollection.createRoom
                    (
                        iOffsetX - 8.0f,   iOffsetY - 6.0f,   iOffsetZ + 2.5f,   0.0f,   8, 14,
                        WallStyle.EWindows, WallStyle.EWindowsAndCeilingWindows, WallStyle.EWindows, WallStyle.ENoWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSlideRight,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                        },
                        null,
                        null,
                        null,
                        null
                    ),

                    //right hallway
                    ShooterWallCollection.createRoom
                    (
                        iOffsetX + 0.0f,   iOffsetY - 12.0f,   iOffsetZ + 2.5f,   0.0f,   4,  6,
                        WallStyle.EWindowsAndCeilingWindows, WallStyle.ESolidWall, WallStyle.ENoWall, WallStyle.ENoWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        null,
                        null,
                        null,
                        null,
                        null
                    ),

                    //small office
                    ShooterWallCollection.createRoom
                    (

                        iOffsetX + 4.0f,   iOffsetY + 10.0f,   iOffsetZ + 2.0f,   0.0f,   5,  5,

                        WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.EWindowsAndCeilingWindows,
                        WallTex.EWood1, WallHealth.EUnbreakale, WallAction.EDoorSwingClockwise,
                        DoorStyle.EAnchorDefault, 2,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                            new Sprite( Others.ESprite1, new LibVertex( 1.0f, 1.0f, 0.0f ), Scalation.EAddQuarter, WallCollidable.EYes, WallTex.EPlant2 ),
                            new Wall(   Others.EPoster1,        new LibVertex(  3.0f,   0.01f, 0.7f   ), 270.0f,  Scalation.EAddHalf,     Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EPoster1,      null,                                   0, WallHealth.ESolidWood, FXSize.ESmall, null   ),
                            new Wall(   Others.EChairOffice1,   new LibVertex(  4.0f,   1.0f,  0.0f   ), 290.0f,  Scalation.ENone,        Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.ELeather1,     new LibTexture[] { WallTex.EChrome2, }, 0, WallHealth.ESolidWood, FXSize.ESmall, null   ),
                            new Article( Others.EWhiteboard1,  3.0f,   4.8f,  0.9f, 90.0f, Scalation.EAddThird, Invert.ENo, WallTex.EWhiteboard1, WallHealth.ESolidWood, FXSize.ESmall, null ),
                        },
                        null,
                        null,
                        null,
                        null
                    ),

                    //office desk ( must be a separate wallCollection! .. cannot be applied to small office?
                    new WallCollection
                    (
                        new Wall(       Others.EDeskOffice1,    new LibVertex(  iOffsetX + 7.5f,   iOffsetY + 10.0f,  iOffsetZ + 2.5f    ), 0.0f,   Scalation.ENone,        Invert.ENo, WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EWood1, new LibTexture[] { WallTex.EScreen2, }, 0, WallHealth.ESolidWood, FXSize.ESmall, null   ),
                        new Wall[]
                        {
                            new Wall(   Others.EKeyboard1,      new LibVertex(  -0.25f,  0.0f,   0.8f    ), 180.0f,    Scalation.ENone,        Invert.EYes, WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EWood1, new LibTexture[] { WallTex.EScreen2, }, 0, WallHealth.EElectricalDevice, FXSize.ESmall, null   ),
                            new Wall(   Others.EScreen1,        new LibVertex(  -0.75f,  0.0f,  0.8f    ), 180.0f,    Scalation.ENone,        Invert.EYes, WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EWood1,  new LibTexture[] { WallTex.EScreen2, }, 0, WallHealth.EElectricalDevice, FXSize.ESmall, null   ),
                        }
                    ),

                    //left hallway
                    ShooterWallCollection.createRoom
                    (
                        iOffsetX + 0.0f,   iOffsetY + 8.0f,   iOffsetZ + 2.5f,   0.0f,   4,  5,
                        WallStyle.ENoWall, WallStyle.ESolidWall, WallStyle.ENoWall, WallStyle.ENoWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        null,
                        null,
                        null,
                        null,
                        null
                    ),
/*
                    //left hallway around corner
                    ShooterWallCollection.createRoom
                    (
                        x + 4.0f,   y + 13.0f,   z + 2.5f,   0.0f,   12,  4,
                        WallStyle.ESolidWall, WallStyle.ENoWall, WallStyle.EWindowsAndCeilingWindows, WallStyle.ENoWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        null
                    ),
*/
/*
                    //next level's hallway
                    ShooterWallCollection.createRoom
                    (
                        x + 16.0f,   y + 13.0f,   z + 2.5f,   0.0f,   4,  4,
                        WallStyle.ENoWall, WallStyle.ENoWall, WallStyle.EWindowsAndCeilingWindows, WallStyle.ESolidWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        null
                    ),
*/
/*
                    //next level's hallway
                    ShooterWallCollection.createRoom
                    (
                        x + 16.0f,   y + 9.0f,   z + 2.5f,   0.0f,   4,  4,
                        WallStyle.ENoWall, WallStyle.ENoWall, WallStyle.ENoWall, WallStyle.ESolidWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        null
                    ),
*/
/*
                    //storage room
                    ShooterWallCollection.createRoom
                    (
                        x + 4.0f,   y - 10.0f,   z + 2.5f,   0.0f,   5,  8,
                        WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.EWindowsAndCeilingWindows,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingClockwise,
                        DoorStyle.EAnchorDefault, 4,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                        }
                    ),

                    //shelves
                    ShooterWallCollection.createShelves( x + 8.0f,   y - 12.5f,  z + 2.5f, 70.0f  ),
                    ShooterWallCollection.createShelves( x + 8.0f,   y - 10.0f, z + 2.5f, 90.0f  ),
                    ShooterWallCollection.createShelves( x + 8.0f,   y - 7.5f,   z + 2.5f, 110.0f ),
*/
/*
                    //staircase
                    ShooterWallCollection.createStaircase(      x + -34.0f,  y + -1.0f, z + 0.0f, true, false, 90.0f   ),
*/
                },

                //EPlayersOffices2
                new WallCollection[]
                {
/*
                    //ground
                    ShooterWallCollection.createGround( WallTex.EGrass1, -0.01f ),
*/
                    //normal office
                    ShooterWallCollection.createRoom
                    (
                        iOffsetX + 6.0f,   iOffsetY + 2.0f,   iOffsetZ + 0.0f,   90.0f,   5,  5,
                        WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.EWindowsAndCeilingWindows,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSlideRight,
                        DoorStyle.EAnchorDefault, 2,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                            new Sprite( Others.ESprite1,        new LibVertex(  1.0f,   1.0f,  0.0f   ),          Scalation.EAddQuarter, WallCollidable.EYes, WallTex.EPlant2 ),
                            new Wall(   Others.EPoster1,        new LibVertex(  3.0f,   0.01f, 0.7f   ), 270.0f,  Scalation.EAddHalf,     Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EPoster1,      null, 0,             WallHealth.ESolidWood, FXSize.ESmall, null   ),
                            new Wall(   Others.EChairOffice1,   new LibVertex(  4.0f,   1.0f,  0.0f   ), 290.0f,  Scalation.ENone,        Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.ELeather1,     new LibTexture[] { WallTex.EChrome2, }, 0, WallHealth.ESolidWood, FXSize.ESmall, null   ),
                            new Wall(   Others.EWhiteboard1,    new LibVertex(  3.0f,   4.8f,  0.9f   ), 90.0f,   Scalation.EAddThird,    Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EWhiteboard1,  null, 0,             WallHealth.ESolidWood, FXSize.ESmall, null   ),
                        },
                        null,
                        null,
                        null,
                        null
                    ),
                },
            };
        }

        @Override
        public final LevelConfigSection[] createNewSectionConfigData()
        {
            return new LevelConfigSection[]
            {
                new LevelConfigSection
                (
                    "Test Offices - 1st floor",
                    LibColors.EBlack,
                    BackGround.ECountry1,
                    new ItemToPickUp[]
                    {
                        new ItemToPickUp( ItemKind.EGameEventLevel1ChangeToNextSection, null, 2.0f, 15.0f, 0.0f, 0.0f, Lib.Rotating.ENo ),
/*
                        new ItemToPickUp( ItemKind.EWearponShotgun, 5.0f, 10.0f, 3.3f, 0.0f, Rotating.EYes ),
                        new ItemToPickUp( ItemKind.EGameEventLevel1ExplainAction, 4.2f, 2.1f,  0.8f, 0.0f, Rotating.EYes ),
                        new ItemToPickUp( ItemKind.EGameEventLevel1AcclaimPlayer, 6.0f, 12.0f, 2.5f, 0.0f, Rotating.EYes ),
*/
                    },
                    new BotFactory[]
                    {
                        new BotFactory( OFFICE_PARTNER_1, BotKind.EUnitPilot,          new LibVertex( 5.0f,  1.0f, 2.5f ), 225.0f, new Bot.BotAction[] { new Bot.BotUseAction( BotEvent.ELevel1AcclaimPlayer ), new Bot.BotGiveAction( ArtefactType.EMobilePhoneSEW890i, BotEvent.ETakeMobileTest ), new Bot.BotGiveAction( ArtefactType.EChips, BotEvent.ETakeCrackersTest ) } ),
                        new BotFactory( OFFICE_PARTNER_2, BotKind.EUnitOfficeEmployee, new LibVertex( 3.0f,  0.0f, 2.5f ), 225.0f, new Bot.BotAction[] { new Bot.BotUseAction( BotEvent.ELevel1AcclaimPlayer ), new Bot.BotGiveAction( ArtefactType.EMobilePhoneSEW890i, BotEvent.ETakeMobileTest ), new Bot.BotGiveAction( ArtefactType.EChips, BotEvent.ETakeCrackersTest ) } ),

                        //new ShooterBotFactory( BotPlayers.OFFICE_PARTNER_1, BotKind.EUnitSecurity,          new LibVertex( 4.0f,  2.0f, 2.5f ), 225.0f, new BotAction[] {  } ),
                        //new ShooterBotFactory( BotPlayers.GENERAL,          BotKind.EUnitSecurity, new LibVertex( 5.75f, 2.0f, 2.5f ), 180.0f, new BotAction[] {} ),
                        //new ShooterBotFactory( BotPlayers.GENERAL,          BotKind.EUnitSecurity, new LibVertex( 7.75f, 2.0f, 2.5f ), 180.0f, new BotAction[] {} ),
                        //new ShooterBotFactory( BotPlayers.GENERAL,          BotKind.EUnitSecurity, new LibVertex( 1.75f, 2.0f, 2.5f ), 180.0f, new BotAction[] {} ),
                        //new ShooterBotFactory( BotKind.EUnitSpecialForces,     new LibVertex( 6.5f, 62.5f, 0.0f + 0.001f ) ),
                        //new ShooterBotFactory( BotKind.EUnitPilot,             new LibVertex( 7.5f, 2.5f, 0.0f + 0.001f ), 90.0f ),
                        //new ShooterBotFactory( BotKind.EUnitOfficeEmployee,    new LibVertex( 4.5f, 62.5f, 0.0f + 0.001f ) ),
                        //new ShooterBotFactory( BotKind.EUnitOfficeEmployee,    new LibVertex( 3.5f, 62.5f, 0.0f + 0.001f ) ),
                        //new ShooterBotFactory( BotKind.EUnitOfficeEmployee,    new LibVertex( 2.5f, 62.5f, 0.0f + 0.001f ) ),
                    }
                ),

                new LevelConfigSection
                (
                    "Test Offices - Basement",
                    LibColors.EBlack,
                    BackGround.ENight1,
                    new ItemToPickUp[]
                    {
                        new ItemToPickUp( ItemKind.EGameEventLevel1ChangeToPreviousLevel, null, 2.0f, 15.0f, 2.5f, 0.0f, Lib.Rotating.ENo ),
                    },
                    new BotFactory[]
                    {
                        new BotFactory( GENERAL,          BotKind.EUnitSecurity, new LibVertex( 3.75f, 2.0f, 0.0f ), 180.0f, new Bot.BotAction[] {} ),
                        new BotFactory( GENERAL,          BotKind.EUnitSecurity, new LibVertex( 7.75f, 3.0f, 0.0f ), 180.0f, new Bot.BotAction[] {} ),
                    }
                ),
            };
        }
    }
