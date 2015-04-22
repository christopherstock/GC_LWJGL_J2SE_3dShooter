/*  $Id: ShooterVersion.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    /**************************************************************************************
    *   The current version enumeration.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    enum ShooterVersion
    {
        V_0_3_10(   "0.3.10",   "12.03.2012 20:45:02 GMT+2", "sliding on the wall on colliding ( for straight x and y axis ), fixed: elevator door open while moving elevator, global player instance, view angle clip for player actions to bot and walls - only launch action on walls and bots if player sees them, lower elevator door displays bullet holes wrong, wall sliver as sprites, build main menu, different crosshairs for different wearpons ( circles for shotguns etc. ), adrenaline ( matrix-mode ), improved: key-releaser on focus lost, frame is unmovable, fixed: sound appears in bg of reload-sound, let shot pass through glass! allow multiple HitPoints, wall-breaking wearpons, tidy _BAUSTELLE, make tranquilizer gun, projectiles that stuck in the wall, projectiles stuck in the wall move with doors, improve wearpon images and swing visibility, place according sounds & particle-colors etc. in LibGLMaterial" ),
        V_0_3_9(    "0.3.9",    "03.03.2012 01:04:27 GMT+2", "fixed: wall sliver drops through floors, seamless level transition, sequenced bot actions, enemy interactions, merged bot and pickup items, encapsulated artefact type, created artifact set, improved distant sound effects, wearpon system to fire with for bots, flattened HitPoint constructor, fixed buggy bot limb animations, complete artefacts for bots, sliver fx when player is hit, left and right-handed bots, check collision while standing up, build dying animation - states, let bots drop artefact(s) on dying, smooth bot wearpon falling" ),
        V_0_3_8(    "0.3.8",    "19.02.2012 02:32:41 GMT+2", "let doors close after delay, darken damaged walls, explosion sound for walls, more 3d segments for posters and doors, shoot on floor -> sliver is not animated, let doors slide left or right and turn clockwise and counter-clockwise, lower wearpon on dying, completed all doors with different anchors and different animations, close combat wearpons attack, subclass wall for elevator, door etc., adventure and interaction engine, created trigger points from items, z collision for items, function for shelves creation, sound for bullet hitting Wall, dismissed: delay for reload - different reloads for different wearpons, fixed bot drop dead angle to all directions, elevators, fixed: 1st sound lags, 3rd person camera ride?, solved!: crosshair, precise aiming, 3d point distance calculation was wrong, deprecated old shot algo, mouse movement, solved!: hard: zoom into camera, sniper rifle, aim with right mousekey, wearpon zooms into camera, different bot-damage according to hit limb," ),
        V_0_3_7(    "0.3.7",    "30.01.2012 23:27:35 GMT+2", "fixed: no hit if player is too near on the wall, pruned wall-destroyable redundancy with wallHealth, cleared sound for java web view ( and jars ), improved bot limb animation caching, prune redundant lib-folder! rename ext to lib, grouped destroyed child walls on wall destroy, let dead bodies disappear translucent," ),
        V_0_3_6(    "0.3.6",    "11.11.2011 18:23:13 GMT+2", "staircases, different wall-damage-values for different ammo, pruned textures and 3dsmax files, functions for light control, switchable lights for different game states, bot startup facing angle, doors also check collision on opening, drawBulletHole and viewOnly to enums, class for single light source, light source system, bot actions, gadget actions, sliding doors + bullet holes to all directions, swinging doors + bullet holes to all directions, more faces for glass door, chest doors + bullet holes to 90° steps, wall crash fx according to shot angle, fixing sprites fixed getting stick in them, fixed items being animated in main menu" ),
        V_0_3_5(    "0.3.5",    "28.05.2011 00:45:12 GMT+2", "dismissed: key-acceleration for turning z, pruned plural s for all resource folders, possibility to draw Sprites? ( drawBitmap, text etc. ) in 3d space, fixed tweak on bot's dying anim, grouped bot coloring, improved bot dying anim, invisible walls ( level bounds etc ) can not be shot, random muzzle flashes for hud-wearpon, translated shot line for x and y to correct shot perspective, bot hair according to skin type, fixed wrong depth sorting ( inoperative normals ), fixed sprites ( sprites sometimes changed position cause centerXY has been taken from transformed vertices ), completed lights for intro-logo, level-setting for invisible zero-z-layer, calculated and assigned face normals," ),
        V_0_3_4(    "0.3.4",    "04.05.2011 23:35:54 GMT+2", "control z-layer! enabled stairs - improved ( separated ) vertical cylinder algo, pruned obsolete redundant z-assignment-algo, preloader / loading gauge for startup ( immediate form showup ), make bullet holes different ( turn from 0 to 360 ° by random ), fixed vertical intersection algo for cylinders, set LibTransformationMode.EOriginalsToOriginals, fixed a bug in defining player's camera-z, fadeout health, dismissed: anti aliasing for all faces, climbable flag for walls ( allow z-change ( and/or allow collision ) only for specified meshes ), different bottom z tolerance for cylinder on non-climbable faces, only draw faces with a max distance to player, unified wearpons & gadgets, drawing queue for masked faces, merge masked and translucent faces for combined drawing, transparent fence, draw a tree with four masked faces, change depth sorting algorithm to normals ( assert normal for all Face objects, composition for wearpon and gadget, items may rotate." ),
        V_0_3_3(    "0.3.3",    "22.04.2011 23:40:17 GMT+2", "destroyable mesh objects, bots delayed disappearing after dying, fx particles rotate independent, wall rubble on fire, bg sounds - stationary bg sounds that change volume! .. try 3d surround sounds? ;) make sound source points :), sounds for rubble, bullet falling on floor and fire, bots can now die, delay after fire. implemented wearpon-dependent delay after fire., destroyable ( glass ) walls, make multiple shots till key release needed ( hunting rifle etc ), improved icon, fixed bullet hole alignment for ceiling and floor!, draw shell icon near ammo :), crash fx / crumbling glass, suppress alt-context-menu :( and enable ALT-LEFT-UP, improved face-ray-intersection-algorithm ( enable bullet holes on ceiling / floor )!, footstep sounds, grouped textures." ),
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

        private     String  version = null;
        private     String  date    = null;

        @SuppressWarnings( "unused" )
        private     String  log     = null;

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