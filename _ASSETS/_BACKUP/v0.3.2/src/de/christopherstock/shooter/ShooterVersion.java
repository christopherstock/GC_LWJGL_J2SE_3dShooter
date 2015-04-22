/*  $Id: Path.java 181 2010-11-13 13:31:37Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    /**************************************************************************************
    *   The current version enumeration.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public enum ShooterVersion
    {
        V_0_3_2(    "0.3.2",    "13.04.2011 16:40:14 GMT+2", "fixed leaking sound system, item into hands of bot, finished exact textures for bots, cleared calls to glLoadIdentity, improve sound threadability, define two toggled armstates / legstates for bot animations, group resources, enable drawing of translucent transparent pixels in ortho, doors shall only move if the target floor is free, dismissed: animations via d3ds-importer ? later .., implemented main states, make a startup menu, let the bot blink his eyes, dismissed: startup loading popup ( improve startup sequence .. ), bots eye blink, sunglasses + hats for bots, smallered textures to 256x256." ),
        V_0_3_1(    "0.3.1",    "17.03.2011 22:42:08 GMT+2", "dismissed make ticks hide/show dependent on wearpon/gadget, fixed turning objects on the y axis - fixed rotation matrix., increased performance for vertex transformation ( cached matrices for meshes )., enabled unarmed mode., precise aiming with numpad, class Rotation and Offset, completed all limbs, cleaned original & translated vertices mess." ),
        V_0_3(      "0.3",      "11.02.2011 20:41:02 GMT+2", "completed nearly all lwjgl challenges. smooth turning for bots, freed gl to lib, separate lib and src path, found solution for hud-strings: create them once! ( on power-change etc. ! ), draw avatar message for lwjgl?, checked out JUnit, different bullet hole sizes, private for gls, encapsulated d3ds, transparency for ortho pixel drawing, checked out code formatting, draw bg image for lwjgl startup display, system for constant framerate." ),
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
