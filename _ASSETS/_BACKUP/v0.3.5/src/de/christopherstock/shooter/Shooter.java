/*  $Id: Shooter.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.shooter.ShooterSettings.*;
    import  de.christopherstock.shooter.game.collision.*;
                                                                  /**-.        .-"-.
    'real coders may cast to void but never die.'                /   o_O      / O o \
                                                                 \_  (__\     \_ v _/
                                                                 //   \\      //   \\
                                                                ((     ))    ((     ))
    **********************************************************---""---""------""---""---********
    *   The Project's Main-Class.                                  |||          |||
    *   Contains ToDo List, main method and inanity.                |            |
    *
    *   TODO ASAP       clear sound for java web view
    *   TODO HIGH       gadget actions ( camera, laser etc. ? )
    *   TODO HIGH       different bot-damage according to hit limb
    *   TODO HIGH       let wearpon/gadget swinging depends on current wearpon / gadget?
    *   TODO HIGH       close combat wearpons
    *   TODO NORMAL     sliding on the wall on colliding?
    *   TODO NORMAL     let dead bodies disappear translucent?
    *   TODO NORMAL     make functions for light control! enable lights for different levels!
    *   TODO NORMAL     alter view angle in {@link LibGLView} when player is dizy etc. ?
    *   TODO NORMAL     merge {@link HitPoint} constructor
    *   TODO NORMAL     parallel to Player: Vehicle - 3rd person camera and different control engine!
    *   TODO NORMAL     make subclasses for each wearpon ? ( extend the strategy pattern )
    *   TODO NORMAL     crosshair ? hard. find a way to calculate from 3d to 2d ? draw crosshair in 3d ?
    *   TODO NORMAL     bot startup facing angle?
    *   TODO LOW        aim with right mousekey?
    *   TODO LOW        zoom into camera?? hard ..
    *   TODO LOW        drawBulletHole and viewOnly to enums
    *   TODO LOW        build main menu
    *   TODO WEAK       matrix mode ( slow mo ) ?
    *   TODO WEAK       no walking while falling ( player falling flag )
    *
    *   @version    0.3.5
    *   @author     Christopher Stock
    ***********************************************************************************/
    public class Shooter
    {
        /**************************************************************************************
        *   The Project's main-method launched on the application's startup.
        *   The only purpose is to instanciate and to start the {@link ShooterMainThread}.
        *
        *   @param  args    Via space separated arguments transfered from the command-line.
        **************************************************************************************/
        public static final void main( String[] args )
        {
            //acclaim
            ShooterDebug.major.out( "Welcome to the Shooter project, [" +   ShooterVersion.getCurrentVersionDesc() + "] Debug Mode is [" + DebugSettings.DEBUG_MODE + "]" );

            //start shooter's main thread
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
