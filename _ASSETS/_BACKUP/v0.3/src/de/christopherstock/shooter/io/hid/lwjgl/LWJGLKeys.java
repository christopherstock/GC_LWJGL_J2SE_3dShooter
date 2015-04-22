/*  $Id: LWJGLKeys.java 255 2011-02-09 18:53:37Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.lwjgl;

    import  org.lwjgl.input.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.io.hid.*;

    public class LWJGLKeys extends Keys
    {
        public static final void checkKeys()
        {
            //isn't that easy?
            keyHoldStrafeLeft               =   Keyboard.isKeyDown( Keyboard.KEY_A          );
            keyHoldStrafeRight              =   Keyboard.isKeyDown( Keyboard.KEY_D          );
            keyHoldWalkUp                   = ( Keyboard.isKeyDown( Keyboard.KEY_W          ) || Keyboard.isKeyDown( Keyboard.KEY_UP    ) );
            keyHoldWalkDown                 = ( Keyboard.isKeyDown( Keyboard.KEY_S          ) || Keyboard.isKeyDown( Keyboard.KEY_DOWN  ) );
            keyHoldTurnLeft                 = ( Keyboard.isKeyDown( Keyboard.KEY_Q          ) || Keyboard.isKeyDown( Keyboard.KEY_LEFT  ) );
            keyHoldTurnRight                = ( Keyboard.isKeyDown( Keyboard.KEY_E          ) || Keyboard.isKeyDown( Keyboard.KEY_RIGHT ) );
            keyHoldTurnLeftPrecise          =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD4    );
            keyHoldTurnRightPrecise         =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD6    );
            keyHoldLookUp                   =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD8    );
            keyHoldLookDown                 =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD2    );
            keyHoldCenterView               =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD5    );
            keyHoldAlternate                =   Keyboard.isKeyDown( Keyboard.KEY_LMENU      );
            keyHoldFire                     =   Keyboard.isKeyDown( Keyboard.KEY_LCONTROL   );

            keyPressedPlayerAction          =   Keyboard.isKeyDown( Keyboard.KEY_SPACE      );
            keyPressedCrouch                =   Keyboard.isKeyDown( Keyboard.KEY_Y          );
            keyPressedGainHealth            =   Keyboard.isKeyDown( Keyboard.KEY_H          );
            keyPressedDamageFx              =   Keyboard.isKeyDown( Keyboard.KEY_T          );
            keyPressedReload                =   Keyboard.isKeyDown( Keyboard.KEY_R          );

            //debug flags
            ShooterMainThread.letPlayerDie         =   Keyboard.isKeyDown( Keyboard.KEY_K          );
            ShooterMainThread.launchAvatarMessage  =   Keyboard.isKeyDown( Keyboard.KEY_RETURN     );
            ShooterMainThread.launchExplosion      =   Keyboard.isKeyDown( Keyboard.KEY_X          );


            if ( Keyboard.isKeyDown( Keyboard.KEY_1 ) )
            {
                Level.orderLevelChange( LevelConfig.ELevel1 );
            }
            if ( Keyboard.isKeyDown( Keyboard.KEY_2 ) )
            {
                Level.orderLevelChange( LevelConfig.ELevel2 );
            }
            if ( Keyboard.isKeyDown( Keyboard.KEY_3 ) )
            {
                Level.orderLevelChange( LevelConfig.ELevel3 );
            }
        }
    }
