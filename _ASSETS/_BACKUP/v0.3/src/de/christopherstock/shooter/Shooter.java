/*  $Id: Shooter.java 271 2011-02-11 20:00:38Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;
                                                                  /**-.        .-"-.
    'real coders may cast to void but never die.'                /   o_O      / O o \
                                                                 \_  (__\     \_ v _/
                                                                 //   \\      //   \\
                                                                ((     ))    ((     ))
    **********************************************************---""---""------""---""---********
    *   The Project's Main-Class.                                  |||          |||
    *   Contains ToDo List, main method and inanity.                |            |
    *
    *   TODO HIGHEST    improve sound threadability!
    *   TODO HIGHEST    suppress alt-context-menu :(
    *   TODO HIGHEST    implement blocker for key actions, fire, reload crouching, etc.
    *   TODO HIGHEST    highest floor is: allow z-change ( and/or allow collision ) only for specified meshes
    *   TODO HIGH       make a startup menu
    *   TODO HIGH       fix sounds .. try 3d surround sounds? ;) make sound source points :)
    *   TODO HIGH       different fire rate for wearpons! ( tick blocker )
    *   TODO HIGH       enable unarmed mode
    *   TODO HIGH       possibility to draw Sprites? ( drawBitmap, text etc. ) in 3d space ? try sprites ? :(    drawTestSprite()
    *   TODO HIGH       change depth sorting algorithm to normals ( assert normal for all Face objects!
    *   TODO NORMAL     sliding on the wall on colliding?
    *   TODO NORMAL     animations via d3ds-importer ?
    *   TODO NORMAL     let wearpon/gadget swinging depends on current wearpon / gadget!
    *   TODO NORMAL     lights?
    *   TODO NORMAL     key-acceleration for turning z
    *   TODO NORMAL     no walking while falling ( player falling flag )
    *   TODO NORMAL     make ticks hide/show dependent on wearpon/gadget
    *   TODO NORMAL     doors shall only move if the target floor is free.
    *   TODO LOW        draw a tree with four (!) masked faces.
    *   TODO LOW        precise aiming with numpad
    *   TODO LOW        matrix mode ( slow mo )
    *   TODO LOW        zoom into camera?? hard ..
    *   TODO LOW        animate items
    *   TODO LOW        one abstract superclass for wearpon/gadget ?
    *   TODO LOW        anti aliasing for all faces (blending:/)?
    *   TODO LOW        drawBulletHole and viewOnly to enums
    *   TODO LOW        gadgets: camera, laser etc.?
    *   TODO LOW        startup loading popup ( improve startup sequence .. )
    *   TODO LOW        footstep sounds
    *   TODO LOW        loading gauge
    *   TODO LOW        BulletHole.rotateForBot() - extend for X and Y
    *   TODO WEAK       clear calls to glLoadIdentity
    *
    *
    *   @version    0.3
    *   @author     Christopher Stock
    ***********************************************************************************/
    public class Shooter
    {
        /**************************************************************************************
        *   The Project's main-method launched on the application's startup.
        *
        *   @param  args    Via space separated arguments transfered from the command-line.
        **************************************************************************************/
        public static final void main( String[] args )
        {
            new ShooterMainThread().start();
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
