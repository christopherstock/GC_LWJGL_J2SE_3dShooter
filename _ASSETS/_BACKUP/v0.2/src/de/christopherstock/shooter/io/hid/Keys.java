/*  $Id: Keys.java,v 1.1 2007/05/29 22:52:49 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    import  java.util.*;
    import  java.awt.event.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.player.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Keys implements KeyListener
    {
        public  static          Keys                singleton               = new Keys();

        public  static          boolean             keyHoldWalkUp           = false;
        public  static          boolean             keyHoldWalkDown         = false;
        public  static          boolean             keyHoldTurnLeft         = false;
        public  static          boolean             keyHoldTurnRight        = false;
        public  static          boolean             keyHoldStrafeLeft       = false;
        public  static          boolean             keyHoldStrafeRight      = false;
        public  static          boolean             keyHoldTurnLeftPrecise  = false;
        public  static          boolean             keyHoldTurnRightPrecise = false;
        public  static          boolean             keyHoldLookUp           = false;
        public  static          boolean             keyHoldLookDown         = false;
        public  static          boolean             keyHoldAlternate        = false;
        public  static          boolean             keyHoldFire             = false;
        public  static          boolean             keyHoldCenterView       = false;
        public  static          boolean             keyPressedCrouch        = false;
        public  static          boolean             keyPressedGainHealth    = false;
        public  static          boolean             keyPressedDamageFx      = false;
        public  static          boolean             keyPressedPlayerAction  = false;

        public  static          int                 ticksKeyLeftHold        = 0;
        public  static          int                 ticksKeyRightHold       = 0;
        public  static          int                 ticksKeyPageUpHold      = 0;
        public  static          int                 ticksKeyPageDownHold    = 0;


        public  static          Vector<Integer>     keyStack                = new Vector<Integer>();

        @Override
        public void keyPressed( KeyEvent e )
        {
            if ( !keyStack.contains( new Integer( e.getKeyCode() ) ) )
            {
                keyStack.addElement( new Integer( e.getKeyCode() ) );
                onKeyDown( e.getKeyCode() );
            }
        }

        @Override
        public void keyReleased( KeyEvent e )
        {
            if ( keyStack.contains( new Integer( e.getKeyCode() ) ) )
            {
                keyStack.removeElement( new Integer( e.getKeyCode() ) );
                onKeyUp( e.getKeyCode() );
            }
        }

        @Override
        public void keyTyped( KeyEvent e )
        {
            //not handled
        }

        public static void onKeyDown( int keyCode )
        {
            switch ( keyCode )
            {
                case KeyEvent.VK_A:
                {
                    keyHoldStrafeLeft = true;
                    break;
                }

                case KeyEvent.VK_D:
                {
                    keyHoldStrafeRight = true;
                    break;
                }

                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                {
                    keyHoldWalkUp = true;
                    break;
                }

                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                 {
                    keyHoldWalkDown = true;
                    break;
                }

                case KeyEvent.VK_Q:
                case KeyEvent.VK_LEFT:
                {
                    keyHoldTurnLeft = true;
                    break;
                }

                case KeyEvent.VK_NUMPAD4:
                {
                    keyHoldTurnLeftPrecise = true;
                    break;
                }

                case KeyEvent.VK_E:
                case KeyEvent.VK_RIGHT:
                {
                    keyHoldTurnRight = true;
                    break;
                }

                case KeyEvent.VK_K:
                {
                    MainThread.letPlayerDie = true;
                    break;
                }

                case KeyEvent.VK_NUMPAD6:
                {
                    keyHoldTurnRightPrecise = true;
                    break;
                }

                case KeyEvent.VK_NUMPAD8:
                {
                    keyHoldLookUp = true;
                    break;
                }

                case KeyEvent.VK_NUMPAD2:
                {
                    keyHoldLookDown = true;
                    break;
                }

                case KeyEvent.VK_NUMPAD5:
                {
                    keyHoldCenterView = true;
                    break;
                }

                case KeyEvent.VK_ALT:
                {
                    keyHoldAlternate = true;
                    break;
                }

                case KeyEvent.VK_H:
                {
                    //launch msg
                    MainThread.launchHUDMessage = true;

                    break;
                }

                case KeyEvent.VK_ENTER:
                {
                    Player.singleton.cylinder.setDebugPoint();

                    //launch msg
                    MainThread.launchAvatarMessage = true;

                    break;
                }

                case KeyEvent.VK_SPACE:
                {
                    //test explosion
                    //Debug.DEBUG_OUT( "launch explosion!" );
                    keyPressedPlayerAction = true;

                    break;
                }

                case KeyEvent.VK_X:
                {
                    //test explosion
                    //Debug.DEBUG_OUT( "launch explosion!" );
                    MainThread.launchExplosion = true;

                    break;
                }

                case KeyEvent.VK_CONTROL:
                {
                    keyHoldFire = true;
                    break;
                }

                case KeyEvent.VK_R:
                {
                    //reload!
                    MainThread.launchWearponReload = true;
                    break;
                }

                case KeyEvent.VK_Y:
                {
                    keyPressedCrouch = true;
                    break;
                }

                case KeyEvent.VK_M:
                {
                    keyPressedGainHealth = true;
                    break;
                }

                case KeyEvent.VK_T:
                {
                    keyPressedDamageFx = true;
                    break;
                }
            }
        }

        public static void onKeyUp( int keyCode )
        {
            switch ( keyCode )
            {
                case KeyEvent.VK_A:
                {
                    keyHoldStrafeLeft = false;
                    break;
                }

                case KeyEvent.VK_D:
                {
                    keyHoldStrafeRight = false;
                    break;
                }

                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                {
                    keyHoldWalkUp = false;
                    break;
                }

                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                {
                    keyHoldWalkDown = false;
                    break;
                }

                case KeyEvent.VK_Q:
                case KeyEvent.VK_LEFT:
                {
                    keyHoldTurnLeft = false;
                    break;
                }

                case KeyEvent.VK_NUMPAD4:
                {
                    keyHoldTurnLeftPrecise = false;
                    break;
                }

                case KeyEvent.VK_E:
                case KeyEvent.VK_RIGHT:
                {
                    keyHoldTurnRight = false;
                    break;
                }

                case KeyEvent.VK_NUMPAD6:
                {
                    keyHoldTurnRightPrecise = false;
                    break;
                }

                case KeyEvent.VK_NUMPAD8:
                {
                    keyHoldLookUp = false;
                    break;
                }

                case KeyEvent.VK_NUMPAD2:
                {
                    keyHoldLookDown = false;
                    break;
                }

                case KeyEvent.VK_NUMPAD5:
                {
                    keyHoldCenterView = false;
                    break;
                }

                case KeyEvent.VK_ALT:
                {
                    keyHoldAlternate = false;
                    break;
                }

                case KeyEvent.VK_CONTROL:
                {
                    keyHoldFire = false;
                    break;
                }

                case KeyEvent.VK_R:
                {
                    //reload!
                    MainThread.launchWearponReload = false;
                    break;
                }

/*
                case KeyEvent.VK_Y:
                {
                    keyHoldCrouch = false;
                    break;
                }
*/
            }
        }
    }
