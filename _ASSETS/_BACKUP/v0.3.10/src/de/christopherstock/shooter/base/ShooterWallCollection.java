/*  $Id: ShooterWallCollection.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  java.util.*;
    import  de.christopherstock.lib.Lib.Invert;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.Lib.Scalation;
    import  de.christopherstock.lib.fx.LibFX.FXSize;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.base.ShooterTexture.Tex;
    import  de.christopherstock.shooter.base.ShooterTexture.WallTex;
    import  de.christopherstock.shooter.base.ShooterD3ds.*;
    import  de.christopherstock.shooter.g3d.face.Face.DrawMethod;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.g3d.wall.Door;
    import  de.christopherstock.shooter.g3d.wall.Wall;
    import  de.christopherstock.shooter.g3d.wall.WallEnergy;
    import  de.christopherstock.shooter.g3d.wall.Wall.WallAction;
    import  de.christopherstock.shooter.g3d.wall.Wall.WallClimbable;
    import  de.christopherstock.shooter.g3d.wall.Wall.WallCollidable;

    /**************************************************************************************
    *   The current version enumeration.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class ShooterWallCollection
    {
        public enum WallStyle
        {
            ENoWall,
            ESolidWall,
            EWindows,
            EWindowsAndCeilingWindows,
            ;
        }

        public enum DoorStyle
        {
            ENoDoor,
            EAnchorDefault,
            EAnchorInverted,
            ;
        }

        public static final WallCollection createGround( Tex tex, float z )
        {
            //ELevel1PlayerOffice
            return new WallCollection
            (
                //environment ( large meshes ) and bounds
                new Wall[]
                {
                    new Wall(   Others.EFloor100x100,  new LibVertex(  0.0f,    0.0f,    z     ), 0.0f,   Scalation.ENone,  Invert.ENo, WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, tex, null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                    new Wall(   Others.EFloor100x100,  new LibVertex(  0.0f,    -100.0f, z     ), 0.0f,   Scalation.ENone,  Invert.ENo, WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, tex, null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                    new Wall(   Others.EFloor100x100,  new LibVertex(  -100.0f, 0.0f,    z     ), 0.0f,   Scalation.ENone,  Invert.ENo, WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, tex, null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                    new Wall(   Others.EFloor100x100,  new LibVertex(  -100.0f, -100.0f, z     ), 0.0f,   Scalation.ENone,  Invert.ENo, WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, tex, null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                }
            );
        }

        public static final WallCollection createElevator( float x, float y, float z, float rotZ, Tex floorTex, Tex doorTex, WallAction action, WallTex wallTex, WallTex ceilingTex )
        {
            Vector<Wall> allWalls = new Vector<Wall>();

            Door wallElevatorFloor      = new Door(   Others.EFloor2x2, x,       y,       z,                                                   rotZ,  WallCollidable.EYes,  action, WallClimbable.EYes, floorTex, WallEnergy.WallHealth.EUnbreakale, null, null, false );
            Door wallElevatorCeiling    = null;
            Door wallDoorTop            = new Door(   Others.EDoor1,    0.0f,    0.0f,    ( action == WallAction.EElevatorUp ? 2.5f : 0.0f  ), 90.0f, WallCollidable.EYes, WallAction.EDoorSlideLeft, WallClimbable.ENo, doorTex, WallEnergy.WallHealth.EUnbreakale, wallElevatorFloor, null, true );
            Door wallDoorBottom         = new Door(   Others.EDoor1,    0.0f,    0.0f,    ( action == WallAction.EElevatorUp ? 0.0f : -2.5f ), 90.0f, WallCollidable.EYes, WallAction.EDoorSlideLeft, WallClimbable.ENo, doorTex, WallEnergy.WallHealth.EUnbreakale, wallElevatorFloor, null, true );

            //create shaft if desired
            if ( wallTex != null )
            {
                allWalls.add( new Wall(   Others.EWall2Solid,  new LibVertex(  0.0f,   0.0f,    0.0f    ), 270.0f,  Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                allWalls.add( new Wall(   Others.EWall2Solid,  new LibVertex(  2.0f,   0.0f,    0.0f    ), 270.0f,  Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                allWalls.add( new Wall(   Others.EWall2Solid,  new LibVertex(  0.0f,   2.0f,    0.0f    ), 180.0f, Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );

                allWalls.add( new Wall(   Others.EWall2Solid,  new LibVertex(  0.0f,   0.0f,    ( action == WallAction.EElevatorUp ? 2.5f : -2.5f )    ), 270.0f,  Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                allWalls.add( new Wall(   Others.EWall2Solid,  new LibVertex(  2.0f,   0.0f,    ( action == WallAction.EElevatorUp ? 2.5f : -2.5f )    ), 270.0f,  Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                allWalls.add( new Wall(   Others.EWall2Solid,  new LibVertex(  0.0f,   2.0f,    ( action == WallAction.EElevatorUp ? 2.5f : -2.5f )    ), 180.0f, Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );

                //door sockets
                allWalls.add( new Wall(   Others.EWall2Door,   new LibVertex(  0.0f,    0.0f,   0.0f                                                   ), 180.0f, Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                allWalls.add( new Wall(   Others.EWall2Door,   new LibVertex(  0.0f,    0.0f,   ( action == WallAction.EElevatorUp ? 2.5f : -2.5f )    ), 180.0f, Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
            }

            //create ceiling if desired
            if ( ceilingTex != null )
            {
                wallElevatorCeiling = new Door(   Others.EFloor2x2, 0.0f, 0.0f, 2.20f, 0.0f,  WallCollidable.EYes,  action, WallClimbable.EYes, ceilingTex, WallEnergy.WallHealth.EUnbreakale, null, null, false );
                allWalls.add( wallElevatorCeiling );
            }

            //set elevator doors
            wallElevatorFloor.setElevatorDoors( wallDoorTop, wallDoorBottom, wallElevatorCeiling );

            allWalls.add( wallDoorBottom );
            allWalls.add( wallDoorTop    );

            //ELevel1PlayerOffice
            return new WallCollection
            (
                wallElevatorFloor,

                //environment ( large meshes ) and bounds
                allWalls.toArray( new Wall[] {} )
            );
        }

        public static final WallCollection createStaircase( float x, float y, float z, boolean toUpper, boolean toLower, float initRotZ, Tex wallTex )
        {
            Wall[] wallsBasement = new Wall[]
            {
                new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   0.0f,   0.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                new Wall(   Others.EWall2Solid,      new LibVertex(  3.0f,   5.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall1Solid,      new LibVertex(  3.0f,   6.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   2.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   2.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   4.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   4.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   6.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   6.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   8.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   8.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall1Solid,      new LibVertex(  0.0f,   9.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall1Solid,      new LibVertex(  6.0f,   9.0f,   0.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   9.0f,   0.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  4.0f,   9.0f,   0.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                new Wall(   Others.EWall2Solid,      new LibVertex(  2.0f,   9.0f,   0.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
            };

            Wall[] wallsUp =
            (
                    toUpper
                ?   new Wall[]
                    {
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   0.0f,   7.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   0.0f,   7.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   3.0f,   7.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   3.0f,   7.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   6.0f,   7.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   6.0f,   7.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),


                        new Wall(   Others.EStairs3x3,      new LibVertex(  0.0f,   3.0f,   0.0f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EMarble1,   WallTex.EMarble2, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EStairs3x3,      new LibVertex(  6.0f,   6.0f,   2.5f   ),  180.0f, Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EMarble1,   WallTex.EMarble2, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   6.0f,   2.5f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   6.0f,   2.5f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   0.0f,   5.0f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   0.0f,   5.0f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   9.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  4.0f,   9.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  2.0f,   9.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   9.0f,   5.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  4.0f,   9.0f,   5.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  2.0f,   9.0f,   5.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   2.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   4.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   6.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   8.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  0.0f,   9.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   2.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   4.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   6.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   8.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  6.0f,   9.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   2.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   4.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   6.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   8.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  0.0f,   9.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   2.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   4.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   6.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   8.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  6.0f,   9.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  3.0f,   5.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  3.0f,   6.0f,   2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  3.0f,   5.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  3.0f,   6.0f,   5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2WindowSocket, new LibVertex(  3.0f,   3.0f,   5.0f    ), 0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1WindowSocket, new LibVertex(  1.0f,   3.0f,   5.0f    ), 0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                    }
                :   new Wall[]
                    {

                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   0.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   0.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   3.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   3.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   6.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   6.0f,   2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECeiling1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2WindowSocket, new LibVertex(  3.0f,   3.0f,   0.0f    ), 0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1WindowSocket, new LibVertex(  1.0f,   3.0f,   0.0f    ), 0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                    }
            );

            Wall[] wallsDown =
            (
                    toLower
                ?   new Wall[]
                    {
                        new Wall(   Others.EStairs3x3,      new LibVertex(  6.0f,   6.0f,   -2.5f   ),  180.0f, Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EMarble1,   WallTex.EMarble2, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EStairs3x3,      new LibVertex(  0.0f,   3.0f,   -5.0f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EMarble1,   WallTex.EMarble2, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   6.0f,   -2.5f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   6.0f,   -2.5f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   0.0f,   -5.0f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   0.0f,   -5.0f   ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   9.0f,   -2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  4.0f,   9.0f,   -2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  2.0f,   9.0f,   -2.5f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   9.0f,   -5.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  4.0f,   9.0f,   -5.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  2.0f,   9.0f,   -5.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   2.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   4.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   6.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   8.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  0.0f,   9.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   2.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   4.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   6.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   8.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  6.0f,   9.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   2.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   4.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   6.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  0.0f,   8.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  0.0f,   9.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   2.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   4.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   6.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  6.0f,   8.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  6.0f,   9.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2Solid,      new LibVertex(  3.0f,   5.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  3.0f,   6.0f,   -2.5f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall2Solid,      new LibVertex(  3.0f,   5.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1Solid,      new LibVertex(  3.0f,   6.0f,   -5.0f    ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2WindowSocket, new LibVertex(  6.0f,   3.0f,   -5.0f   ), 0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1WindowSocket, new LibVertex(  4.0f,   3.0f,   -5.0f   ), 0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                    }
                :   new Wall[]
                    {
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   3.0f,   0.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   3.0f,   0.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  0.0f,   6.0f,   0.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EFloor3x3,       new LibVertex(  3.0f,   6.0f,   0.0f    ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),

                        new Wall(   Others.EWall2WindowSocket, new LibVertex(  6.0f,   3.0f,   0.0f    ), 0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                        new Wall(   Others.EWall1WindowSocket, new LibVertex(  4.0f,   3.0f,   0.0f    ), 0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                    }
            );

            Vector<Wall> allWallsV = new Vector<Wall>();
            allWallsV.addAll( Arrays.asList( wallsUp ) );
            allWallsV.addAll( Arrays.asList( wallsDown ) );
            allWallsV.addAll( Arrays.asList( wallsBasement ) );

            return new WallCollection
            (
                new Wall(       Others.EFloor3x3,   new LibVertex(  x,      y,      z       ),  initRotZ,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.ECarpet1, null, WallEnergy.WallHealth.EUnbreakale, null, null ),
                allWallsV.toArray( new Wall[] {} )
            );
        }

        /**************************************************************************************
        *   Creates a room with maximum possibilities for adjustments.
        *
        *   @author     Christopher Stock
        *   @version    0.3.10
        **************************************************************************************/
        public static final WallCollection createRoom( float x, float y, float z, float rotZ, int sizeX, int sizeY, WallStyle left, WallStyle top, WallStyle right, WallStyle bottom, Tex doorTex, WallEnergy.WallHealth doorHealth, WallAction doorAction, DoorStyle doorStyle, int doorOffset, Tex wallTex, Tex floorTex, Tex ceilingTex, Wall[] furniture )
        {
            Vector<Wall> allWalls = new Vector<Wall>();

            //add floor and ceiling
            for ( int i = 0; i < sizeX; ++i )
            {
                for ( int j = 0; j < sizeY; ++j )
                {
                    //skip base tile
                    if ( i > 0 || j > 0 )
                    {
                        allWalls.add( new Wall(       Others.EFloor1x1,   new LibVertex(  0.0f + i * 1.0f, 0.0f + j * 1.0f, 0.0f ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, floorTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                    }

                    //margin celiling ( for glass ceilings )
                    if ( ceilingTex != null )
                    {
                        if ( j > 0 && j < ( sizeY - 1 ) && i > 0 && i < ( sizeX - 1 ) )
                        {
                            allWalls.add( new Wall(       Others.EFloor1x1,   new LibVertex(  0.0f + i * 1.0f, 0.0f + j * 1.0f, 2.5f ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, ceilingTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                        }
                    }

                    //draw corners
                    if ( i == 0 && j == 0 )
                    {
                        if ( left == WallStyle.EWindowsAndCeilingWindows || top == WallStyle.EWindowsAndCeilingWindows )
                        {
                            allWalls.add( new Wall(         Others.EFloor1x1,           new LibVertex(  0.0f, 0.0f,    2.5f       ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EGlass1, null, WallEnergy.WallHealth.EGlass, null, null ) );
                        }
                        else if ( ceilingTex != null )
                        {
                            allWalls.add( new Wall(       Others.EFloor1x1,   new LibVertex(  0.0f, 0.0f, 2.5f ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, ceilingTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                        }
                    }
                    else if ( i == sizeX - 1 && j == 0 )
                    {
                        if ( left == WallStyle.EWindowsAndCeilingWindows || bottom == WallStyle.EWindowsAndCeilingWindows )
                        {
                            allWalls.add( new Wall(         Others.EFloor1x1,           new LibVertex(  sizeX - 1, 0.0f,    2.5f       ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EGlass1, null, WallEnergy.WallHealth.EGlass, null, null ) );
                        }
                        else if ( ceilingTex != null )
                        {
                            allWalls.add( new Wall(       Others.EFloor1x1,   new LibVertex(  sizeX - 1.0f, 0.0f, 2.5f ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, ceilingTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                        }
                    }
                    else if ( i == sizeX - 1 && j == sizeY - 1 )
                    {
                        if ( bottom == WallStyle.EWindowsAndCeilingWindows || right == WallStyle.EWindowsAndCeilingWindows )
                        {
                            allWalls.add( new Wall(         Others.EFloor1x1,           new LibVertex(  sizeX - 1, sizeY - 1,    2.5f       ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EGlass1, null, WallEnergy.WallHealth.EGlass, null, null ) );
                        }
                        else if ( ceilingTex != null )
                        {
                            allWalls.add( new Wall(       Others.EFloor1x1,   new LibVertex(  sizeX - 1, sizeY - 1, 2.5f ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, ceilingTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                        }
                    }
                    else if ( i == 0 && j == sizeY - 1 )
                    {
                        if ( top == WallStyle.EWindowsAndCeilingWindows || right == WallStyle.EWindowsAndCeilingWindows )
                        {
                            allWalls.add( new Wall(         Others.EFloor1x1,           new LibVertex(  0.0f, sizeY - 1,    2.5f       ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EGlass1, null, WallEnergy.WallHealth.EGlass, null, null ) );
                        }
                        else if ( ceilingTex != null )
                        {
                            allWalls.add( new Wall(       Others.EFloor1x1,   new LibVertex(  0.0f, sizeY - 1, 2.5f ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, ceilingTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                        }
                    }
                }
            }

            //left wall
            for ( int i = 0; i < sizeX; ++i )
            {
                Tex ct = ( i == 0 || i == sizeX - 1 ? null : ceilingTex );
                addStyledWall( allWalls, left, i * 1.0f, 0.0f, 180.0f, wallTex, ct );
            }

            //right wall
            for ( int i = 0; i < sizeX; ++i )
            {
                Tex ct = ( i == 0 || i == sizeX - 1 ? null : ceilingTex );
                addStyledWall( allWalls, right, 1.0f + i * 1.0f, sizeY, 0.0f, wallTex, ct );
            }

            //top wall
            for ( int i = 0; i < sizeY; ++i )
            {
                Tex ct = ( i == 0 || i == sizeY - 1 ? null : ceilingTex );
                Tex wt = (  ( i == doorOffset || i == doorOffset + 1 ) && doorStyle != DoorStyle.ENoDoor ? null : wallTex );
                addStyledWall( allWalls, top, 0.0f, 1.0f + i * 1.0f, 90.0f, wt, ct );
                if ( wt == null && i == doorOffset )
                {
                    allWalls.add( new Wall( Others.EWall2Door,  new LibVertex( 0.0f, 2.0f + i * 1.0f, 0.0f ),  90.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, wallTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                }
            }

            //bottom wall
            for ( int i = 0; i < sizeY; i += 1 )
            {
                Tex ct = ( i == 0 || i == sizeY - 1 ? null : ceilingTex );
                addStyledWall( allWalls, bottom, sizeX, i * 1.0f, 270.0f, wallTex, ct );
            }

            //Wall tile = new Wall(       Others.EFloor2x2,     new LibVertex(  0.0f,      0.0f,      0.0f       ),  0.0f,        Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, floorTex, null, WallEnergy.WallHealth.EUnbreakale, null, null );

            //add door
            //allWalls.add( tile );

            //add furtinure
            if ( furniture != null ) allWalls.addAll( Arrays.asList( furniture ) );

            //switch door style
            switch ( doorStyle )
            {
                case EAnchorDefault:
                {
                    Door door     = new Door( Others.EDoor1, x, y, z, rotZ, WallCollidable.EYes, doorAction, WallClimbable.ENo, doorTex, doorHealth, null, null, true );
                    Wall baseTile = new Wall(       Others.EFloor1x1,   new LibVertex( 0.0f, 0.0f, 0.0f ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, floorTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null );
                    allWalls.add( baseTile );

                    //translate all walls by door ..
                    for ( Wall w : allWalls )
                    {
                        w.translate( 0.0f, -doorOffset, 0.0f, LibTransformationMode.EOriginalsToOriginals );
                    }

                    //return right-anchored door
                    return new WallCollection
                    (
                        door,
                        allWalls.toArray( new Wall[] {} ),
                        new LibVertex
                        (
                            LibMath.sinDeg( rotZ ) * -2.0f,
                            LibMath.cosDeg( rotZ ) * 2.0f,
                            0.0f
                        ),
                        new LibVertex
                        (
                            0.0f,
                            0.0f,
                            0.0f
                        ),
                        false
                    );
                }

                case EAnchorInverted:
                {
                    Door door = new Door( Others.EDoor1, x, y, z, rotZ, WallCollidable.EYes, doorAction, WallClimbable.ENo, doorTex, doorHealth, null, null, true );

                    Wall baseTile = new Wall(       Others.EFloor1x1,   new LibVertex(  0.0f, 0.0f, 0.0f ),  0.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, floorTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null );
                    allWalls.add( baseTile );

                    //translate all walls by door ..
                    for ( Wall w : allWalls )
                    {
                        w.translate( 0.0f, -doorOffset, 0.0f, LibTransformationMode.EOriginalsToOriginals );
                    }

                    //return left-anchored door
                    return new WallCollection
                    (
                        door,
                        allWalls.toArray( new Wall[] {} ),
                        new LibVertex
                        (
                            LibMath.sinDeg( rotZ ) * 0.0f,
                            LibMath.cosDeg( rotZ ) * 0.0f,
                            0.0f
                        ),
                        new LibVertex
                        (
                            0.0f,
                            0.0f,
                            180.0f
                        ),
                        false
                    );
                }

                case ENoDoor:
                default:
                {
                    Wall baseTile = new Wall(       Others.EFloor1x1,   new LibVertex(  x, y, z ), rotZ,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, floorTex,   null, WallEnergy.WallHealth.EUnbreakale, null, null );
                    return new WallCollection
                    (
                        baseTile,
                        allWalls.toArray( new Wall[] {} ),
                        null,
                        null,
                        false
                    );
                }
            }
        }


        public static final WallCollection createShelves( float x, float y, float z, float rotZ )
        {
            Vector<Wall> boxes = new Vector<Wall>();

            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( -0.6f,  0.0f, 0.5f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( -0.25f, 0.0f, 0.5f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( 0.25f,  0.0f, 0.5f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( 0.6f,   0.0f, 0.5f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );

            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( -0.6f,  0.0f, 0.9f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( -0.25f, 0.0f, 0.9f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( 0.25f,  0.0f, 0.9f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( 0.6f,   0.0f, 0.9f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );

            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( -0.6f,  0.0f, 1.29f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( -0.25f, 0.0f, 1.29f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( 0.25f,  0.0f, 1.29f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( 0.6f,   0.0f, 1.29f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );

            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( -0.6f,  0.0f, 1.69f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( -0.25f, 0.0f, 1.69f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( 0.25f,  0.0f, 1.69f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );
            if ( LibMath.getRandom( 0, 2 ) == 0 ) boxes.add( new Wall(   Others.ECrate1, new LibVertex( 0.6f,   0.0f, 1.69f ), 0.0f, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ECrate, FXSize.ESmall, null ) );

            return new WallCollection
            (
                new Wall(   Others.EShelves1, new LibVertex(  x, y, z ), rotZ, Scalation.ENone, Invert.ENo, WallCollidable.EYes, WallAction.ENone, WallClimbable.ENo, DrawMethod.EHideIfTooDistant, WallTex.EWood1, null, WallEnergy.WallHealth.ESolidWood, FXSize.ELarge, null ),

                //boxes
                boxes.toArray( new Wall[] {} )
            );
        }

        private static final void addStyledWall( Vector<Wall> allWalls, WallStyle style, float x, float y, float angle, Tex wallTex, Tex ceilingTex )
        {
            switch ( style )
            {
                case ENoWall:
                {
                    //just the ceiling
                    if ( ceilingTex != null ) allWalls.add( new Wall(         Others.EFloor1x1,           new LibVertex(  x, y,    2.5f       ),  angle + 180.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, ceilingTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                    break;
                }

                case ESolidWall:
                {
                    if ( wallTex    != null ) allWalls.add( new Wall(         Others.EWall1Solid,         new LibVertex(  x, y,    0.0f       ),  angle,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                    if ( ceilingTex != null ) allWalls.add( new Wall(         Others.EFloor1x1,           new LibVertex(  x, y,    2.5f       ),  angle + 180.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, ceilingTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                    break;
                }
                case EWindows:
                {
                    if ( wallTex    != null ) allWalls.add( new Wall(         Others.EWall1WindowSocket,  new LibVertex(  x, y,    0.0f       ),  angle,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, wallTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                    if ( wallTex    != null ) allWalls.add( new Wall(         Others.EWall1WindowGlass,   new LibVertex(  x, y,    0.0f       ),  angle,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo, DrawMethod.EAlwaysDraw, WallTex.EGlass1, null, WallEnergy.WallHealth.EGlass, null, null ) );
                    if ( ceilingTex != null ) allWalls.add( new Wall(         Others.EFloor1x1,           new LibVertex(  x, y,    2.5f       ),  angle + 180.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, ceilingTex, null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                    break;
                }
                case EWindowsAndCeilingWindows:
                {
                    if ( wallTex    != null ) allWalls.add( new Wall(         Others.EWall1WindowSocket,  new LibVertex(  x, y,    0.0f       ),  angle,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo,  DrawMethod.EAlwaysDraw, wallTex,    null, WallEnergy.WallHealth.EUnbreakale, null, null ) );
                    if ( wallTex    != null ) allWalls.add( new Wall(         Others.EWall1WindowGlass,   new LibVertex(  x, y,    0.0f       ),  angle,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.ENo,  DrawMethod.EAlwaysDraw, WallTex.EGlass1,    null, WallEnergy.WallHealth.EGlass, null, null ) );
                    if ( ceilingTex != null )  allWalls.add( new Wall(         Others.EFloor1x1,           new LibVertex(  x, y,    2.5f       ),  angle + 180.0f,   Scalation.ENone, Invert.ENo,   WallCollidable.EYes,  WallAction.ENone, WallClimbable.EYes, DrawMethod.EAlwaysDraw, WallTex.EGlass1, null, WallEnergy.WallHealth.EGlass, null, null ) );
                    break;
                }
            }
        }
    }
