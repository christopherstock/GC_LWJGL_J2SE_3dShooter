/*  $Id: LWJGLKeys.java 491 2011-03-31 22:07:15Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.lwjgl;

    import  org.lwjgl.input.*;

    import de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterLevels.LevelConfig;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;

    public class LWJGLKeys extends Keys
    {
        public static final void checkKeys()
        {
            //view and walking keys
            keyHoldStrafeLeft                   =   Keyboard.isKeyDown( Keyboard.KEY_A          );
            keyHoldStrafeRight                  =   Keyboard.isKeyDown( Keyboard.KEY_D          );
            keyHoldWalkUp                       = ( Keyboard.isKeyDown( Keyboard.KEY_W          ) || Keyboard.isKeyDown( Keyboard.KEY_UP    ) );
            keyHoldWalkDown                     = ( Keyboard.isKeyDown( Keyboard.KEY_S          ) || Keyboard.isKeyDown( Keyboard.KEY_DOWN  ) );
            keyHoldTurnLeft                     = ( Keyboard.isKeyDown( Keyboard.KEY_Q          ) || Keyboard.isKeyDown( Keyboard.KEY_LEFT  ) );
            keyHoldTurnRight                    = ( Keyboard.isKeyDown( Keyboard.KEY_E          ) || Keyboard.isKeyDown( Keyboard.KEY_RIGHT ) );
            keyHoldTurnLeftPrecise              =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD4    );
            keyHoldTurnRightPrecise             =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD6    );
            keyHoldLookUp                       =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD8    );
            keyHoldLookDown                     =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD2    );
            keyHoldCenterView                   =   Keyboard.isKeyDown( Keyboard.KEY_NUMPAD5    );
            keyHoldAlternate                    =   Keyboard.isKeyDown( Keyboard.KEY_LMENU      );
            keyHoldFire                         =   Keyboard.isKeyDown( Keyboard.KEY_LCONTROL   );

            //action keys
            playerAction.iKeyHold               =   Keyboard.isKeyDown( Keyboard.KEY_SPACE      );
            crouching.iKeyHold                  =   Keyboard.isKeyDown( Keyboard.KEY_Y          );
            reload.iKeyHold                     =   Keyboard.isKeyDown( Keyboard.KEY_R          );

            //debug keys
            gainHealth.iKeyHold                 =   Keyboard.isKeyDown( Keyboard.KEY_H          );
            damageFx.iKeyHold                   =   Keyboard.isKeyDown( Keyboard.KEY_T          );
            avatarMessage.iKeyHold              =   Keyboard.isKeyDown( Keyboard.KEY_RETURN     );
            explosion.iKeyHold                  =   Keyboard.isKeyDown( Keyboard.KEY_X          );
            toggleMainMenu.iKeyHold             =   Keyboard.isKeyDown( Keyboard.KEY_ESCAPE     );

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

            if ( Keyboard.isKeyDown( Keyboard.KEY_ADD ) )
            {
                new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_SHOTGUN_SHELLS ).show();
            }
        }
    }
