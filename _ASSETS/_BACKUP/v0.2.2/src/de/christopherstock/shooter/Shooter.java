/*  $Id: Shooter.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.shooter.d3ds.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;
                                                                  /**-.        .-"-.
    'real coders may cast to void but never die!'                /   o_O      / O o \
                                                                 \_  (__\     \_ v _/
                                                                 //   \\      //   \\
                                                                ((     ))    ((     ))
    ********************************************************---""---""------""---""---********
    *   The Project's Main-Class.                                |||          |||
    *                                                             |            |
    *   TODO HIGH       create different levels to select :)
    *
    *   TODO HIGHEST    implement blocker for key actions, fire, reload etc.
    *
    *   TODO HIGH       hold one player instance per level ? yes - to prove orthogonality ;)
    *
    *   TODO HIGHEST    highest floor is: allow z-change ( and/or allow collision ) only for specified meshes
    *   TODO HIGHEST    key blocker for crouching
    *   TODO HIGH       draw a tree with four (!) masked faces.
    *   TODO HIGH       different fire rate for wearpons! ( tick blocker )
    *   TODO HIGHEST    use trextures instead of drawPixels on gl !
    *   TODO HIGH       change depth sorting algorithm to normals
    *
    *   TODO HIGH       fix sounds .. try 3d surround sounds? ;) make sound source points :)
    *   TODO HIGH       possibility to draw Sprites? ( drawBitmap, text etc. ) in 3d space ?
    *   TODO HIGHEST    clear calls to glLoadIdentity
    *   TODO HIGH       gl to lib ?
    *   TODO HIGHEST    private for gls !
    *   TODO HIGHEST    suppress alt-context-menu :(
    *
    *   TODO NORMAL     sliding on the wall on colliding?
    *   TODO NORMAL     animations via d3ds-importer ?
    *   TODO NORMAL     let wearpon/gadget swinging depends on current wearpon / gadget!
    *   TODO NORMAL     lights?
    *   TODO NORMAL     key-acceleration for turning z
    *   TODO NORMAL     no walking while falling ( player falling flag )
    *   TODO NORMAL     make ticks hide/show dependent on wearpon/gadget
    *   TODO NORMAL     doors shall only move if the target floor is free.
    *
    *   TODO LOW        precise aiming with numpad
    *   TODO LOW        zoom into camera?? hard ..
    *   TODO LOW        checkout JUnit
    *   TODO LOW        checkout code formatting
    *   TODO LOW        animate items
    *   TODO LOW        one abstract superclass for wearpon/gadget ?
    *   TODO LOW        smooth turning for bots.
    *   TODO LOW        anti aliasing for all faces (blending:/)?
    *   TODO LOW        drawBulletHole and viewOnly to enums
    *   TODO LOW        gadgets: camera, laser? etc. etc.
    *   TODO LOW        startup loading popup ( improve startup sequence .. )
    *   TODO LOW        footstep sounds
    *   TODO LOW        loading gauge
    *   TODO LOW        different bullet hole sizes ?
    *   TODO LOW        BulletHole.rotateForBot() - extend for X and Y
    *
    *   DONE            possibility to scale or rotate meshes on being created.
    *   DONE            all res names to enum constants.
    *   DONE            transparent bullet holes
    *   DONE            draw translucent objects after opaque objects !!
    *   DONE            alter debug system - hold static instances of LibDebug.
    *   DONE            bg to ortho ! own class for bg
    *   DONE            prune unused textures, d3ds files etc.
    *   DONE            lights
    *   DONE            initial rotation (z-axis) for 3ds meshes.
    *
    *   @version    0.2
    *   @author     Christopher Stock
    ***********************************************************************************/
    public class Shooter
    {
        /***********************************************************************************
        *   The current version enumeration.
        ***********************************************************************************/
        public enum Version
        {
            V_0_2_2(    "0.2.2",    "09.11.2010 19:48:12 GMT+2", "switchable levels" ),
            V_0_2_1(    "0.2.1",    "07.11.2010 17:13:32 GMT+2", "New res names. solved textured 3dsmax imports." ),
            V_0_2(      "0.2",      "17.10.2010 00:02:52 GMT+2", "Separated opengl engine. Implementing LWJGL" ),
            V_0_1_1(    "0.1.1",    "27.06.2010 00:30:06 GMT+2", "fixed bullets for swinging doors completed!" ),
            V_0_1(      "0.1",      "27.06.2010 23:44:21 GMT+2", "fixing bullets for swinging doors not completed." ),
            ;

            public  String  version = null;
            public  String  date    = null;
            public  String  log     = null;

            private Version( String aVersion, String aDate, String aLog )
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

        /************************************************************************************
        *   Saying NO is definetely equivalent to the boolean <code>false</code>.
        ************************************************************************************/
        public      static  final   boolean     NO                                  = false;

        /************************************************************************************
        *   Saying YES is definetely equivalent to the boolean <code>true</code>.
        ************************************************************************************/
        public      static  final   boolean     YES                                 = true;

        /**************************************************************************************
        *   A flag being set to true if a closing-event on the main form is invoked.
        **************************************************************************************/
        public      static          boolean     destroyed                           = false;

        /**************************************************************************************
        *   The Project's main-method launched on the application's startup.
        *
        *   @param  args    Via space separated arguments transfered from the command-line.
        **************************************************************************************/
        public static final void main( String[] args )
        {
            MainThread.singleton = new MainThread();
            MainThread.singleton.start();
        }

        /**************************************************************************************
        *   Inits all components of the project in a threaded manner.
        **************************************************************************************/
        public static final void initFromMainThread()
        {
            GLForm.setLookAndFeel();                                                //set host-os lookAndFeel
            D3dsImporter.init( Debug.d3ds, Path.E3dsMax.url );      //import 3d studio max objects
            WallCollection.init();                                                  //init mesh collections
            HUD.init();                                                             //init Heads up display
            GL3D.init( ShooterSettings.ENGINE_3D, MainThread.singleton, MainThread.singleton ); //init 3d engine
            Sound.init();                                                           //init sound-engine

            //start with level 1
            Level.levelToChangeTo = LevelConfig.ELevel1;

          //if ( !ShooterSettings.DISABLE_SOUND_BG ) Sound.playSoundBg( Sound.ESoundBackground1 );       //test sound-engine
        }

        /*********************************************************************************
        *   The lady in red.
        *
        *   @deprecated     unused ascii art.
        *********************************************************************************/
        @Deprecated
        protected   static  final   String  LADY_IN_RED                 = new String
        (
                "                                        . ...                                              "
            +   "                                    .''.' .    '.                                          "
            +   "                               . '' =.'.:I:.'..  '.                                        "
            +   "                             .'.:.:..,,:II:'.'.'.. '.                                      "
            +   "                           .':.'.:.:I:.:II:'.'.'.'.. '.                                    "
            +   "                         .'.'.'.'::.:.:.:I:'.'.'.'. .  '                                   "
            +   "                        ..'.'.'.:.:I::.:II:.'..'.'..    .                                  "
            +   "                       ..'.'':.:.::.:.::II::.'.'.'.'..   .                                 "
            +   "                      ..'.'.'.:.::. .:::II:..'.'.'.'.'.   .                                "
            +   "                     .':.''.':'.'.'.:.:I:'.'.'.'.'.. '..  ..                               "
            +   "                     ':. '.':'. ..:.::.::.:.'..'  ':.'.'.. ..                              "
            +   "                    .:.:.':'.   '.:':I:.:.. .'.'.  ': .'.. . ..                            "
            +   "                    '..:.:'.   .:.II:.:..   . .:.'. '.. '. .  ..                           "
            +   "                   .. :.:.'.  .:.:I:.:. .  . ..:..:. :..':. .  '.                          "
            +   "                  .:. :.:.   .:.:I:.:. .    . ..:I::. :: ::  .. ..                         "
            +   "                  .. :'.'.:. .:.:I:'.        ..:.:I:. :: ::.   . '.                        "
            +   "                  '..:. .:.. .:II:'         ,,;IIIH.  ::. ':.      .                       "
            +   "                 .:.::'.:::..:.AII;,      .::=,,  :I .::. ':.       .                      "
            +   "                 :..:'.:II:.:I:  ,,;'   ' .;:FBT=X:: ..:.. ':.    . .                      "
            +   "                .. :':III:. :.:A=PBF;.  . .P,IP;;=:: :I:..'::. .    ..                     "
            +   "                . .:.:II: A.'.';,PP:= .  . ..'..' .: :.::. ':...  . ..                     "
            +   "                . .: .:IIIH:.   ' '.' .  ... .    .:. :.:.. :...    .'                     "
            +   "                . .I.::I:IIA.        ..   ...    ..::.'.'.'.: ..  . .                      "
            +   "                 .:II.'.':IA:.      ..    ..:.  . .:.: .''.'  ..  . .                      "
            +   "                ..::I:,'.'::A:.  . .:'-, .-.:..  .:.::AA.. ..:.' .. .                      "
            +   "                 ':II:I:.  ':A:. ..:'   ''.. . : ..:::AHI: ..:..'.'.                       "
            +   "                .':III.::.   'II:.:.,,;;;:::::=. .:::AHV:: .::'' ..                        "
            +   "                ..=:IIHI::. .  =I:..=:;,,,,;;=. . .:AII:: :.:'  . .                        "
            +   "                . . IIHHI:..'.'.'V::. =:;;;=   ...:AIIV:'.:.'  .. .                        "
            +   "                 . . :IIHI:. .:.:.V:.   ' ' . ...:HI:' .:: :. .  ..                        "
            +   "                 . .  ':IHII:: ::.IA..      .. .A .,,:::' .:.    .                         "
            +   "                 :.  ...'I:I:.: .,AHHA, . .'..AHIV::' . .  :     ..                        "
            +   "                 :. '.::::II:.I:.HIHHIHHHHHIHHIHV:'..:. .I.':. ..  '.                      "
            +   "              . . .. '':::I:'.::IHHHHHHHHMHMHIHI. '.'.:IHI..  '  '  '.                     "
            +   "               ':... .  ''= .::'.HMHI:HHHHMHHIHI. :IIHHII:. . . .    .                     "
            +   "                :.:.. . ..::.' .IV=.:I:IIIHIHHIH. .:IHI::'.': '..  .  .                    "
            +   "              . .:.:: .. ::'.'.'..':.::I:I:IHHHIA.'.II.:...:' .' ... . '..                 "
            +   "             '..::::' ...::'.IIHII:: .:.:..:..:III:.'::' .'    .    ..  . .                "
            +   "             '::.:' .''     .. :IIHI:.:.. ..: . .:I:=' ...:.:.  ..    .. ..                "
            +   "                .:..::I:.  . . . .IHII:.:'   .. ..=.::.:II:.:. .  ...   . ..               "
            +   "             .. . .::.:.,,...-::II:.:'    . ...... . .. .:II:.::  ...  .. ..               "
            +   "              ..:.::.I .    . . .. .:. .... ...:.. . . ..:.::.   :..   . ..                "
            +   "               .'.::I:.      . .. ..:.... . ..... .. . ..::. .. .I:. ..' .                 "
            +   "             .'':.: I.       . .. ..:.. .  . .. ..... .:. .:.. .:I.'.''..                  "
            +   "             . .:::I:.       . . .. .:. .    .. ..  . ... .:.'.'I'  .  ...                 "
            +   "             . ::.:I:..     . . . ....:. . .   .... ..   .:...:.:.:. ''.''                 "
            +   "             '.'::'I:.       . .. ....:. .     .. . ..  ..'  .'.:..:..    '                "
            +   "                   :. .     . .. .. .:.... .  .  .... ...   .  .:.:.:..    '.              "
            +   "                   :.      .  . . .. .:.... . . ........       .:.:.::. .    .             "
            +   "                   :. .     . . . . .. .::..:  . ..:.. .        ::.:.:.. .    .            "
            +   "                   :.. .    . . .  . .. ..:.:  .. .. .:. ..     ':::.::.:. .   .           "
            +   "                   ':.. .  . . . .. .. ...::' .. ..  . .:. .     V:I:::::.. .   :.         "
            +   "                    ::. .  . .. .. ... .:.::  .. .  . .. .. .     VI:I:::::..   ''B        "
            +   "                     :.. .   . .. ..:.. ..I:... . .  . .. ... .    VII:I:I:::. .'::        "
            +   "                     ':.. . . . .. ..:..:.:I:.:. .  . .. . .:. .    VHIII:I::.:..':        "
            +   "                      ::..   . . .. ..:..:.HI:. .      . . .... .   :HHIHIII:I::..:        "
            +   "                      ':. .  . .. .. ..:.:.:HI:.    . . .. ..... .   HHHHIHII:I::.'        "
            +   "                       :.. .  . . .. .:.:.:.HI:.      . . .. ... .   IHHHHIHHIHI:'         "
            +   "                        :..  .  . . .. ..:..IH:.     . . .. .. ,,, . 'HHHHHHHHI:'          "
            +   "                        ':..   . . .. ..:.:.:HI..   .  . .. . :::::.  MIH:==='             "
            +   "                         :. . .  . .. ..::.:.VI:.     . . .. .:::'::. HIH                  "
            +   "                          :..  .  . .. .:.:.:.V:.    . . . ...::I=A:. HHV                  "
            +   "                           :. .  .  . .. ..:.:.V:.     . . ....::I::'.HV:                  "
            +   "                            :. .  . . . .. .:..II:.  . . . ....':::' AV.'                  "
            +   "                             :.. . . .. ... .:..VI:. . . .. .:. ..:.AV'.                   "
            +   "                             ':.. . .  .. ..:.:.:HAI:.:...:.:.:.:.AII:.                    "
            +   "                              I:. .  .. ... .:.:.VHHII:..:.:..:A:'.:..                     "
            +   "                              IA..  . . .. ..:.:.:VHHHHIHIHHIHI:'.::.                      "
            +   "                              'HA:.  . . .. ..:.:.:HHHIHIHHHIHI:..:.                       "
            +   "                               HIA: .  . . .. ...:.VHHHIHIIHI::.:...                       "
            +   "                               HIHI:. .  .. ... .::.HHHIIHIIHI:::..                        "
            +   "                               HII:.:.  .  .. ... .::VHHIHI:I::.:..                        "
            +   "                               AI:..:..  .  . .. ..:.VHIII:I::.:. .                        "
            +   "                              AI:. ..:..  .  . .. ..' VHIII:I;... .                        "
            +   "                             AI:. .  .:.. .  .  . ...  VHIII::... .                        "
            +   "                           .A:. .      :.. .  . .. .:.. VHII::..  .                        "
            +   "                          A:. . .       ::. .. .. . .:.. =VHI::.. .                        "
            +   "                        .:.. .  .        :.. .:..... .::.. VHI:..                          "
            +   "                       ... . .  .     . . :.:. ..:. . .::.. VI:..  .                       "
            +   "                      .. .. .  .    . . ...:... . .. . .:::. V:..  .                       "
            +   "                     '.. ..  .   .  .. ..:::.... .:. . ..::.. V..  .                       "
            +   "                   . . .. . .   . . .. ..:::A. ..:. . . .::.. :..                          "
            +   "                  . .. .. .. . .  . ... ..::IA.. .. . .  ..::. :..  .                      "
            +   "                 .. .. ... . .  .. .... .:.::IA. . .. . ..:.::. :.  .                      "
            +   "                . . . .. .   . . .. ..:..:.::IIA. . .  .. .:.::. :. .                      "
            +   "               .. . .  .   . . .. ... ..:.::I:IHA. .  . . ..:.::. . .                      "
            +   "              .: ..  .  .   . . ... .:.. .:I:IIHHA. .  . .. .::I:. .                       "
            +   "             .::.  .     . . .. ..:. .::.:IIHIIHHHA.  .  .. ..:I:. . .                     "
            +   "             A::..      .  .  ...:..:.::I:IHIHIHHHHA.  .  . ..::I:. .                      "
            +   "            :HI:.. .       . .. .:.:.::I:IHIHIIHIHHHA. .   .. .::I:. ..                    "
            +   "            AI:.. .. .    . .. .:.:.::II:IHIIIHIHIHHHA.  .  . ..::I:. ..                   "
            +   "           :HI:.. . .   .  . .. .::.:I:IHIHIIIHIHIIHHHA..  . .. .::I:. ..                  "
            +   "           AI:.:.. .  .  .  ... .::.::I:IHIIHIHIHIHIHIHHA. .  . ..::I:. .                  "
            +   "           HI:. .. . .  .  . .. .:..::IIHIHIHIIIIWHIIHHMWA.  . . .:::I:. . .               "
            +   "           HI:.. . .  .   . .. ..:.::I:IIHHIIHIHIHIHHMMW=  '.. . ..:::II: . .              "
            +   "           HI::.. .  .   .  .. .:..:::IIHIHIIWIWIIWMWW= .    .. . ..::III: .  .            "
            +   "           HI::... . . .  . ... ..:.:::IIHIWIWIWMWMWW. .  .   . .. .:.:III. .   .          "
            +   "           II::.:.. . .  .  .. ......:..IHWHIWWMWMW=.. . . . . '... .:.:IHI:..    .        "
            +   "           II:I::.. .  .   .  . .....::.:IHWMWWWMW:.. .  .  . .  .:..:::IIHII..            "
            +   "           :II:.:.:.. .  .   . ......:.:.:IWWMWWW:.:.. .  .  .  . :...:.:IHHI:..           "
            +   "            HI::.:. . . .  .  . ...:.::.::.VWMWW::.:.:.. .  . .. . :.. ..:IHHI::.'-        "
            +   "            HII::.:.. .  .  . .. .:..:.'.  'WWWI::.::.:.. . .  . .. ':...:II:IIII::        "
            +   "            III::.:... .  .  . ...:.:... .   WII:I::.:.. .  .  .. . . :.:::...::.::        "
            +   "             VII::.:.. . . . .. ...:....      VHI:I::.:.. .  . ... .. .::.:..:.:..:        "
            +   "              VII::.:.. . .  . ..:.::.. .     :HHII:I::.:.. . . .. ..  .'::':......        "
            +   "              III:I::.. .. . . .. .:.:.. .    :VHIHI:I::.:... . . .. .. .':. .. .AH        "
            +   "             AA:II:I::.. . . .  .. ..:.. . .  ::HHIHII:I::.:... .. .. ... .:.::AHHH        "
            +   "            AHH:I:I::.:.. .  . .. ..:.:.. .   ::VHHHVHI:I::.:.:.. ..:. .::.A:.AHHHM        "
            +   "            HHHAII:I::.:.. . . . .. ..:.. . . :::HIHIHIHII:I::.:.. .. .:. ..AHHMMM:        "
            +   "           AHHHH:II:I::.:.. . . .. ..:.:.. . .:I:MMIHHHIHII:I:::.:. ..:.:.AHHHMMM:M        "
            +   "           HHHHHA:II:I::.. .. . . .. .:... . .:IIVMMMHIHHHIHII:I::. . .. AHHMMMM:MH        "
            +   "           HHHHHHA:I:I:::.. . . . ... ..:.. ..:IHIVMMHHHHIHHHIHI:I::. . AHMMMMM:HHH        "
            +   "           HHHHHMM:I::.:.. . . . .. ...:.:...:IIHHIMMHHHII:.:IHII::.  AHMMMMMM:HHHH        "
            +   "           HHHHHMMA:I:.:.:.. . . . .. ..:.:..:IIHHIMMMHHII:...:::.:.AHMMMMMMM:HHHHH        "
            +   "           HHHHHMMMA:I::... . . . . .. ..:.::.:IHHHIMMMHI:.:.. .::AHMMMMMMM:HHHHHHH        "
            +   "           VHHHHMMMMA:I::.. . .  . . .. .:.::I:IHHHIMMMMHI:.. . AHMMMMMMMM:HHHHHHHH        "
            +   "            HHHMMMMMM:I:.:.. . .  . . ...:.:IIHIHHHIMMMMMHI:.AHMMMMMMMMM:HHHHHHHHHH        "
            +   "            HHHHMMMMMA:I:.:.. .  .  . .. .:IIHIHHHHIMMMMMH:AMMMMMMMMMMM:HHHHHHHHHHH        "
            +   "            VHHHMMMMMMA:I:::.:. . . . .. .:IHIHHHHHIMMMV=AMMMMMMMMMMMM:HHHHHHHHHHHH        "
            +   "             HHHHHMMMMMA:I::.. .. .  . ...:.:IHHHHHHIM=AMMMMMMMMMMMM:HHHHHHHHHHHHHH        "
            +   "             VHHHHHMMMMMA:I:.:.. . . .  .. .:IHIHHHHI:AMMMMMMMMMMMIHHHHHHHHHHHHHHHH        "
            +   "              VHHHHHMMMMMA:I::.:. . .  .. .:.:IHHHV:MMMMMIMMMMMMMMMMMMMHHHHHHHHV::.        "
            +   "               VHHHHMMMMMMA:::.:..:.. . .. .:::AMMMMMMMM:IIIIIHHHHHHHHHHHHHHHV:::..        "
            +   "                HHHHHMMMIIIA:I::.:.:..:... AMMMMMMMMMM:IIIIIIHHHHHHHHHHHHHHHV::::::        "
            +   "                VHHHHMMIIIIMA:I::::.::..AMMMMMMMMMMM:IIIIIIIHHHHHHHHHHHHHHV::::::::        "
            +   "                 HHHHMIIIIMMMA:II:I::AIIIMMMMMMMMMM:IIIIIIIHHHHHHHHHHHHHHV:::::::::        "
            +   "                 VHHHHIIIMMMMMMA:I:AIIIIIIMMMMMM:IIIIIIIIHHHHHHHHHHHHHHV::::::::='         "
            +   "                  HHHHHIIMMMMMMIMAAIIIIIIIIMMM:IIIIIIIIHHHHHHHHHHHHHHHV:::::=='            "
            +   "                  VHHHIIIIMMMMIIIIIIIIIIIIII:IIIIIIIIHHHHHHHHHHHHHHHV::=='                 "
            +   "                   VHHIIIMMMMMIIIIIIIIIIIIIIIIIIIIIHHHHHHHHHHHHHHHV                        "
            +   "                    VHHIMMMMMMMIIIIIIIIIIIIIIIIIHHHHHHHHHHHHHV                             "
            +   "                     VHHHMMMMMMMMIIIIIIIIIIIHHHHHHHHHHHV                                   "
            +   "                      VHHHMMMMMMMMMMMMMHHHHHHHHHHHHHV                                      "
        );
    }
