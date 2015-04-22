/*  $Id: Shooter.java,v 1.4 2007/09/02 14:19:18 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.io.d3ds.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.ui.hud.*;

                                                 /**-.        .-"-.
    'real coders may cast to void               /   o_O      / O o \
    but never die!'                             \_  (__\     \_ v _/
                                                //   \\      //   \\
                                               ((     ))    ((     ))
    *****************************************---""---""------""---""---***************
    *   The Project's Main-Class.                 |||          |||
    *                                              |            |
    *
    *   TODO    HIGH    give mesh a reference to all bullet-holes it carries
    *
    *   TODO    PAR     sliding on the wall on colliding?
    *   TODO    PAR     animations via d3ds-importer :)
    *   TODO    PAR     let wearpon/gadget swinging depends on current wearpon / gadget!
    *   TODO    PAR     lights?
    *   TODO    PAR     key-acceleration for turning z
    *   TODO    PAR     no walking while falling ( player falling flag )
    *   TODO    PAR     make ticks hide/show dependent on wearpon/gadget
    *   TODO    PAR     stick bullet-holes to meshes!
    *   TODO    PAR     only call BulletHole.rotateForBot() if the rotation changes!
    *   TODO    PAR     low startup performance?
    *   TODO    PAR     different bullet-holes for different materials ( stick to texture )
    *   TODO    LOW     precise aiming with numpad
    *   TODO    LOW     zoom into camera??
    *   TODO    LOW     animate items
    *   TODO    LOW     one abstract superclass for wearpon/gadget ?
    *   TODO    LOW     anti aliasing for all faces (blending:/)?
    *   TODO    LOW     gadgets: camera, laser? etc. etc.
    *   TODO    LOW     startup loading popup ( improve startup sequence .. )
    *   TODO    LOW     footstep sounds
    *   TODO    LOW     Player.centerVerticalLook() - to look class ?
    *
    *   @version    1.0                                                 )\._.,--....,'``.
    *   @author     stock                                              /,   _.. \   _\  (`._ ,.
    ****************************************************************---`._.-(,_..'--(,_..'`-.;.'---****/
    public class Shooter
    {
        /***********************************************************************************
        *   The current version enumeration.
        ***********************************************************************************/
        public enum Version
        {
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
        *   The window's title string.
        ************************************************************************************/
        public      static  final   String      FORM_TITLE                          = "Shooter Prototype, " + Version.getCurrentVersionDesc();

        /************************************************************************************
        *   Saying NO is definetely equivalent to the boolean <code>true</code>.
        ************************************************************************************/
        public      static  final   boolean     NO                                  = false;

        /************************************************************************************
        *   Saying YES is definetely equivalent to the boolean <code>true</code>.
        ************************************************************************************/
        public      static  final   boolean     YES                                 = true;

        // --- debug defines --- //
        public      static  final   boolean     DISABLE_COLLISIONS                  = NO;
        public      static  final   boolean     DISABLE_GRAVITY                     = NO;
        public      static  final   boolean     INVISIBLE_Z_ZERO_LAYER              = YES;

        public      static  final   boolean     DISABLE_LOOK_CENTERING_X            = YES;
        public      static  final   boolean     DISABLE_SOUND_BG                    = YES;
        public      static  final   boolean     DISABLE_SOUND_FX                    = YES;
        public      static  final   boolean     DEBUG_DRAW_ITEM_CIRCLE              = YES;
        public      static  final   boolean     DEBUG_DRAW_BOT_CIRCLES              = NO;
        public      static  final   boolean     DEBUG_SHOW_PLAYER_CIRCLE            = YES;
        public      static  final   boolean     ENABLE_FPS                          = YES;
        public      static  final   boolean     ENABLE_3RD_PERSON_CAMERA            = NO;

        // --- performance settings --- //
        public      static  final   int         COLLISION_CHECKING_STEPS            = 5;
        public      static  final   int         MAX_NUMBER_BULLET_HOLES             = 256;
        public      static  final   int         ELLIPSE_SEGMENTS                    = 45;

        // --- speed settings --- //
        public      static  final   int         TICKS_WEARPON_HIDE_SHOW             = 4;
        public      static  final   int         TICKS_HEALTH_FX                     = 10;
        public      static  final   int         TICKS_DAMAGE_FX                     = 10;

        /**************************************************************************************
        *   A flag being set to true if a closing-event on the main form is invoked.
        **************************************************************************************/
        public      static          boolean     destroyed                           = false;

        /**************************************************************************************
        *   A flag being set to true if the init() method of the glView-singleton has been performed.
        **************************************************************************************/
        public      static          boolean     glPanelInitialized                  = false;

        /**************************************************************************************
        *   The Project's main-method launched on the application's startup.
        *
        *   @param  args    Via space separated arguments transfered from the command-line.
        **************************************************************************************/
        public static final void main( String[] args )
        {
            Form.setLookAndFeel();                              //set host-os lookAndFeel
            D3dsImporter.init();                                //import 3d studio max objects
            Meshes.init();                                      //init all meshes
            MeshCollection.init();                              //init mesh collections

            Level.setupNewLevel( Level.ELevel1 );               //init mesh collection for startup level

            HUD.init();                                         //init Heads up display
            MainThread.init();                                  //init main-thread engine
            GLView.init();                                      //init 3d engine
            Form.init();                                        //init form
            Sound.init();                                       //init sound-engine

          //Sound.playSoundBg( Sound.ESoundBackground1 );       //test sound-engine
            Player.user.getWearpon().cheatAmmo();               //gain 99999 ammo
        }

        /*********************************************************************************
        *   The lady in red - unused ascii art.
        *********************************************************************************/
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
