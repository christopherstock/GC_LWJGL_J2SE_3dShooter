/*  $Id: ShooterLevels.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  java.util.*;
    import  de.christopherstock.lib.Lib.*;
    import  de.christopherstock.lib.fx.LibFX.FXSize;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.base.ShooterBotFactory.BotKind;
    import  de.christopherstock.shooter.base.ShooterD3ds.Others;
    import  de.christopherstock.shooter.base.ShooterGameLevel.InvisibleZeroLayerZ;
    import  de.christopherstock.shooter.base.ShooterTexture.*;
    import  de.christopherstock.shooter.base.ShooterWallCollection.*;
    import  de.christopherstock.shooter.g3d.face.Face.DrawMethod;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.g3d.wall.*;
    import  de.christopherstock.shooter.g3d.wall.WallEnergy.WallHealth;
    import  de.christopherstock.shooter.g3d.wall.Wall.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.objects.Bot.BotAction;
    import  de.christopherstock.shooter.game.objects.Bot.BotGiveAction;
    import  de.christopherstock.shooter.game.objects.Bot.BotUseAction;
    import  de.christopherstock.shooter.game.objects.ItemToPickUp.Rotating;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The current version enumeration.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class ShooterLevels
    {
        public  static          WallCollection[]        LEVEL_MESH_GLOBAL_DATA          = null;
        public  static          WallCollection[][]      LEVEL_MESH_SECTION_DATA         = null;

        //temp
        private static  final   float                   x                               = 0.0f;
        private static  final   float                   y                               = 0.0f;
        private static  final   float                   z                               = 0.0f;

        private static          List<WallCollection>    globalWalls                 = null;

        public static class BotPlayers
        {
            public  static  final   int                 GENERAL                         = 0;
            public  static  final   int                 OFFICE_PARTNER_1                = 1;
            public  static  final   int                 OFFICE_PARTNER_2                = 2;
        }

        public static enum LevelConfig
        {
            ELevel1Offices1
            (
                "Player's Office",
                new ViewSet( x + 4.0f, y + 4.0f, z + 2.5f, 0.0f, 0.0f, 30.0f ),
                //new ViewSet( x + 0.0f, y + 0.0f, 0.0f, 0.0f, 0.0f, 270.0f ),
                LibColors.EBlack,
                BackGround.ENight1,
                new ItemToPickUp[]
                {
                    new ItemToPickUp( ItemKind.EGameEventLevel1ChangeToNextSection, null, 2.0f, 15.0f, 0.0f, 0.0f, Rotating.ENo ),

                    //new PickUpItem( ItemTemplate.EWearponShotgun, 5.0f, 10.0f, 3.3f, 0.0f, Rotating.EYes ),
                    //new PickUpItem( ItemTemplate.EGameEventLevel1ExplainAction, 4.2f, 2.1f,  0.8f, 0.0f, Rotating.EYes ),
                    //new PickUpItem( ItemTemplate.EGameEventLevel1AcclaimPlayer, 6.0f, 12.0f, 2.5f, 0.0f, Rotating.EYes ),
                },
                new ArtefactType[]   {  ArtefactType.ETranquilizerGun,  ArtefactType.EWaltherPPK,  ArtefactType.EMagnum357, ArtefactType.ESpaz12,  ArtefactType.ERCP180,  ArtefactType.ESniperRifle, ArtefactType.EAdrenaline, },
                new ItemEvent[]  {  ItemEvent.EGainAmmo20TranquilizerDarts, ItemEvent.EGainAmmo20TranquilizerDarts, ItemEvent.EGainAmmo20TranquilizerDarts, ItemEvent.EGainAmmo20TranquilizerDarts, ItemEvent.EGainAmmo20Bullet44mm,  ItemEvent.EGainAmmo20Bullet44mm, ItemEvent.EGainAmmo20Bullet44mm,ItemEvent.EGainAmmo20Bullet44mm,ItemEvent.EGainAmmo20Bullet44mm, ItemEvent.EGainAmmo20ShotgunShells, ItemEvent.EGainAmmo18MagnumBullet, ItemEvent.EGainGadgetHandset1, ItemEvent.EGainGadgetCrackers, ItemEvent.EGainAmmo120Bullet792mm, ItemEvent.EGainAmmo120Bullet792mm, },
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
                InvisibleZeroLayerZ.EYes
            ),

            ELevel1Offices2
            (
                "Player's Hallway",
                new ViewSet( x - 5.0f, y - 1.0f, z + 2.5f, 0.0f, 0.0f, 30.0f ),
                LibColors.EBlack,
                BackGround.ENight1,
                new ItemToPickUp[]
                {
                    new ItemToPickUp( ItemKind.EGameEventLevel1ChangeToPreviousLevel, null, 2.0f, 15.0f, 2.5f, 0.0f, Rotating.ENo ),
                },
                new ArtefactType[]   { ArtefactType.EWaltherPPK, ArtefactType.EMagnum357, ArtefactType.ESpaz12, ArtefactType.ERCP180,  ArtefactType.ESniperRifle, },
                new ItemEvent[]  { ItemEvent.EGainAmmo20Bullet44mm, ItemEvent.EGainAmmo20Bullet44mm, ItemEvent.EGainAmmo20Bullet44mm, ItemEvent.EGainAmmo20ShotgunShells, ItemEvent.EGainAmmo18MagnumBullet, ItemEvent.EGainGadgetHandset1, ItemEvent.EGainGadgetCrackers, ItemEvent.EGainAmmo120Bullet792mm, ItemEvent.EGainAmmo120Bullet792mm, },
                new ShooterBotFactory[]
                {
                    new ShooterBotFactory( BotPlayers.GENERAL,          BotKind.EUnitSecurity, new LibVertex( 3.75f, 2.0f, 0.0f ), 180.0f, new BotAction[] {} ),

                    new ShooterBotFactory( BotPlayers.GENERAL,          BotKind.EUnitSecurity, new LibVertex( 7.75f, 3.0f, 0.0f ), 180.0f, new BotAction[] {} ),
                },
                InvisibleZeroLayerZ.EYes
            ),

            ;

            public                  String                  iDesc                   = null;
            public                  ViewSet                 iStartPosition          = null;
            public                  BackGround              iBg                     = null;
            public                  LibColors               iBgCol                  = null;
            public                  ArtefactType[]          iStartupWearpons        = null;
            public                  ItemToPickUp[]          iItems                  = null;
            public                  ItemEvent[]             iStartupItems           = null;
            public                  ShooterBotFactory[]     iStartupBots            = null;
            public                  InvisibleZeroLayerZ     iHasInvisibleZLayer     = null;

            private LevelConfig( String aDesc, ViewSet aStartPosition, LibColors aBgCol, BackGround aBg, ItemToPickUp[] aItems, ArtefactType[] aStartupWearpons, ItemEvent[] aStartupItems, ShooterBotFactory[] aStartupBots, InvisibleZeroLayerZ aHasInvisibleZLayer )
            {
                iDesc               = aDesc;
                iStartPosition      = aStartPosition;
                iBgCol              = aBgCol;
                iBg                 = aBg;
                iItems              = aItems;
                iStartupWearpons    = aStartupWearpons;
                iStartupItems       = aStartupItems;
                iStartupBots        = aStartupBots;
                iHasInvisibleZLayer = aHasInvisibleZLayer;
            }
        }

        public static final void init()
        {
            LEVEL_MESH_GLOBAL_DATA =  new WallCollection[]
            {
                //elevator
                ShooterWallCollection.createElevator(       x + 1.0f,     y + 13.0f,   z + 2.5f, 0.0f, WallTex.EMarble1, WallTex.EWood1, WallAction.EElevatorDown, WallTex.EBricks2, WallTex.ECeiling1 ),

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
            };

            LEVEL_MESH_SECTION_DATA = new WallCollection[][]
            {
                //EPlayersOffice
                new WallCollection[]
                {
                    //ground
                    ShooterWallCollection.createGround( WallTex.EGrass1, -0.01f ),

                    //office desk ( must be a separate wallCollection! .. cannot be applied to small office?
                    new WallCollection
                    (
//                        new Door(       Others.EDoor1,    +2.0f,    -3.0f,    2.5f, 90.0f, WallCollidable.EYes, WallAction.EDoorSlideLeft, WallClimbable.ENo, WallTex.EWood1, WallEnergy.WallHealth.EUnbreakale, null, null, true ),
                        new Wall[]
                        {
                        }
                    ),

                    //casino
                    ShooterWallCollection.createRoom
                    (
                        x + 0.0f,   y + 0.0f,   z + 2.5f,   0.0f,   9,  14,
                        WallStyle.ENoWall, WallStyle.ESolidWall, WallStyle.ENoWall, WallStyle.EWindowsAndCeilingWindows,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSlideLeft,
                        DoorStyle.EAnchorDefault, 6,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                            new Sprite( Others.ESprite1, 5.5f, 0.75f, 0.0f, Scalation.EAddThird,   WallCollidable.EYes, WallTex.EPlant1 ),
                            new Wall(   Others.ESodaMachine1,   new LibVertex( 7.5f, 0.5f,  0.0f ), 270.0f, Scalation.ENone,   Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.ESodaMachine2,   null,           WallHealth.EVendingMachine, FXSize.ELarge, ShooterSound.EExplosion1 ),

                            new Sprite( Others.ESprite1, 8.0f, 3.0f,  0.0f, Scalation.EAddHalf,   WallCollidable.EYes, WallTex.EPlant2 ),
                            new Sprite( Others.ESprite1, 8.0f, 7.5f,  0.0f, Scalation.EAddHalf,   WallCollidable.EYes, WallTex.EPlant1 ),
                            new Sprite( Others.ESprite1, 8.0f, 12.5f, 0.0f, Scalation.EAddHalf,   WallCollidable.EYes, WallTex.EPlant2 ),
                            new Wall(   Others.ESofa1,          new LibVertex( 8.5f, 10.0f, 0.0f ), 0.0f,   Scalation.ENone,   Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EClothDarkRed,         WallTex.ETest,  WallHealth.EFurniture,      FXSize.ELarge, null                     ),
                            new Wall(   Others.ESofa1,          new LibVertex( 8.5f, 5.0f,  0.0f ), 0.0f,   Scalation.ENone,   Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EClothDarkRed,         WallTex.ETest,  WallHealth.EFurniture,      FXSize.ELarge, null                     ),
                        }
                    ),

                    //big office
                    ShooterWallCollection.createRoom
                    (
                        x - 8.0f,   y - 6.0f,   z + 2.5f,   0.0f,   8, 14,
                        WallStyle.EWindows, WallStyle.EWindowsAndCeilingWindows, WallStyle.EWindows, WallStyle.ENoWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSlideRight,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                        }
                    ),

                    //right hallway
                    ShooterWallCollection.createRoom
                    (
                        x + 0.0f,   y - 12.0f,   z + 2.5f,   0.0f,   4,  6,
                        WallStyle.EWindowsAndCeilingWindows, WallStyle.ESolidWall, WallStyle.ENoWall, WallStyle.ENoWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        null
                    ),

                    //small office
                    ShooterWallCollection.createRoom
                    (
                        x + 4.0f,   y + 10.0f,   z + 2.5f,   0.0f,   5,  5,
                        WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.EWindowsAndCeilingWindows,
                        WallTex.EWood1, WallHealth.EUnbreakale, WallAction.EDoorSwingClockwise,
                        DoorStyle.EAnchorDefault, 2,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                            new Sprite( Others.ESprite1, 1.0f, 1.0f, 0.0f, Scalation.EAddQuarter, WallCollidable.EYes, WallTex.EPlant2 ),
                            new Wall(   Others.EPoster1,        new LibVertex(  3.0f,   0.01f, 0.7f   ), 270.0f,  Scalation.EAddHalf,     Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EPoster1,      null,             WallHealth.ESolidWood, FXSize.ESmall, null   ),
                            new Wall(   Others.EChairOffice1,   new LibVertex(  4.0f,   1.0f,  0.0f   ), 290.0f,  Scalation.ENone,        Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.ELeather1,     WallTex.EChrome2, WallHealth.ESolidWood, FXSize.ESmall, null   ),
                            new Wall(   Others.EWhiteboard1,    new LibVertex(  3.0f,   4.8f,  0.9f   ), 90.0f,   Scalation.EAddThird,    Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWhiteboard1,  null,             WallHealth.ESolidWood, FXSize.ESmall, null   ),
                        }
                    ),

                    //office desk ( must be a separate wallCollection! .. cannot be applied to small office?
                    new WallCollection
                    (
                        new Wall(       Others.EDeskOffice1,    new LibVertex(  x + 7.5f,   y + 10.0f,  z + 2.5f    ), 0.0f,   Scalation.ENone,        Invert.ENo, WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1,        WallTex.EScreen2, WallEnergy.WallHealth.ESolidWood, FXSize.ESmall, null   ),
                        new Wall[]
                        {
                            new Wall(   Others.EKeyboard1,      new LibVertex(  -0.25f,  0.0f,   0.8f    ), 180.0f,    Scalation.ENone,        Invert.EYes, WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1,        WallTex.EScreen2, WallEnergy.WallHealth.EElectricalDevice, FXSize.ESmall, null   ),
                            new Wall(   Others.EScreen1,        new LibVertex(  -0.75f,  0.0f,  0.8f    ), 180.0f,    Scalation.ENone,        Invert.EYes, WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1,        WallTex.EScreen2, WallEnergy.WallHealth.EElectricalDevice, FXSize.ESmall, null   ),
                        }
                    ),

                    //left hallway
                    ShooterWallCollection.createRoom
                    (
                        x + 0.0f,   y + 8.0f,   z + 2.5f,   0.0f,   4,  5,
                        WallStyle.ENoWall, WallStyle.ESolidWall, WallStyle.ENoWall, WallStyle.ENoWall,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSwingCounterClockwise,
                        DoorStyle.ENoDoor, 0,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
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
/*
                    //staircase
                    ShooterWallCollection.createStaircase(      x + -34.0f,  y + -1.0f, z + 0.0f, true, false, 90.0f   ),
*/




                },

                //EPlayersOffices2
                new WallCollection[]
                {
                    //ground
                    ShooterWallCollection.createGround( WallTex.EGrass1, -0.01f ),

                    //small office
                    ShooterWallCollection.createRoom
                    (
                        x + 6.0f,   y + 2.0f,   z + 0.0f,   90.0f,   5,  5,
                        WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.ESolidWall, WallStyle.EWindowsAndCeilingWindows,
                        WallTex.EGlass1, WallHealth.ESolidGlass, WallAction.EDoorSlideRight,
                        DoorStyle.EAnchorDefault, 2,
                        WallTex.EBricks2, WallTex.ECarpet1, WallTex.ECeiling1,
                        new Wall[]
                        {
                            new Sprite( Others.ESprite1, 1.0f, 1.0f, 0.0f, Scalation.EAddQuarter, WallCollidable.EYes, WallTex.EPlant2 ),
                            new Wall(   Others.EPoster1,        new LibVertex(  3.0f,   0.01f, 0.7f   ), 270.0f,  Scalation.EAddHalf,     Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EPoster1,      null,             WallHealth.ESolidWood, FXSize.ESmall, null   ),
                            new Wall(   Others.EChairOffice1,   new LibVertex(  4.0f,   1.0f,  0.0f   ), 290.0f,  Scalation.ENone,        Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.ELeather1,     WallTex.EChrome2, WallHealth.ESolidWood, FXSize.ESmall, null   ),
                            new Wall(   Others.EWhiteboard1,    new LibVertex(  3.0f,   4.8f,  0.9f   ), 90.0f,   Scalation.EAddThird,    Invert.ENo,  WallCollidable.EYes, WallAction.ENone,   WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWhiteboard1,  null,             WallHealth.ESolidWood, FXSize.ESmall, null   ),
                        }
                    ),



                },
            };
        }

        public static final WallCollection[] getLevelWalls( LevelConfig config )
        {
            if ( globalWalls == null ) globalWalls = Arrays.asList( LEVEL_MESH_GLOBAL_DATA );

            Vector<WallCollection> levelWalls = new Vector<WallCollection>();
            levelWalls.addAll( globalWalls );
            levelWalls.addAll( Arrays.asList( LEVEL_MESH_SECTION_DATA[ config.ordinal() ] ) );
            return levelWalls.toArray( new WallCollection[] {} );
        }
    }
