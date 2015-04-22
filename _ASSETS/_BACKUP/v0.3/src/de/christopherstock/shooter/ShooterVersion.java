/*  $Id: Path.java 181 2010-11-13 13:31:37Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    /**************************************************************************************
    *   The current version enumeration.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public enum ShooterVersion
    {
        V_0_3(      "0.3",      "11.02.2011 20:41:02 GMT+2", "completed nearly all lwjgl challenges. smooth turning for bots, freed gl to lib, separate lib and src path, found solution for hud-strings: create them once! ( on power-change etc. ! ), draw avatar message for lwjgl?, checked out JUnit, different bullet hole sizes, private for gls, encapsulated d3ds, transparency for ortho pixel drawing, checked out code formatting, draw bg image for lwjgl startup display, system for constant framerate .." ),
        V_0_2_3(    "0.2.3",    "25.01.2011 18:06:33 GMT+2", "file extensions to lib, fixed wrong texture-colors for gls! ( windows 7 ), create different levels to select :), initial rotation (z-axis) for 3ds meshes., lights, prune unused textures, d3ds files etc., bg to ortho ! own class for bg, alter debug system - hold static instances of LibDebug., draw translucent objects after opaque objects !!, transparent bullet holes, all res names to enum constants, possibility to scale or rotate meshes on being created." ),
        V_0_2_2(    "0.2.2",    "09.11.2010 19:48:12 GMT+2", "switchable levels" ),
        V_0_2_1(    "0.2.1",    "07.11.2010 17:13:32 GMT+2", "New res names. solved textured 3dsmax imports." ),
        V_0_2(      "0.2",      "17.10.2010 00:02:52 GMT+2", "Separated opengl engine. Implementing LWJGL" ),
        V_0_1_1(    "0.1.1",    "27.06.2010 00:30:06 GMT+2", "fixed bullets for swinging doors completed!" ),
        V_0_1(      "0.1",      "27.06.2010 23:44:21 GMT+2", "fixing bullets for swinging doors not completed." ),
        ;

        public  String  version = null;
        public  String  date    = null;
        public  String  log     = null;

        private ShooterVersion( String aVersion, String aDate, String aLog )
        {
            version = aVersion;
            date    = aDate;
            log     = aLog;
        }

        public static final String getCurrentVersionDesc()
        {
            return "v. " + values()[ 0 ].version + ", " + values()[ 0 ].date;
        }
    }
