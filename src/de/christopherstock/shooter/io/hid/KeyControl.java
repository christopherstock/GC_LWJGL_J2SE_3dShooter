/*  $Id: KeyControl.java 1224 2012-12-31 12:32:24Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    /**************************************************************************************
    *   Control for one key enables key blocking after being pressed.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public class KeyControl
    {
        public                          boolean         iKeyHold                = false;
        public                          boolean         iLaunchAction           = false;
        public                          long            iNextMillis             = 0;
        public                          long            iDelayAfterReload       = 0;

        public KeyControl( int aDelayAfterReload )
        {
            iDelayAfterReload = aDelayAfterReload;
        }

        public void checkLaunchingAction()
        {
            if ( iKeyHold )
            {
                //check reload blocker
                if ( iNextMillis <= System.currentTimeMillis() )
                {
                    //set timestamp for next allowed player action
                    iNextMillis = System.currentTimeMillis() + iDelayAfterReload;

                    //trigger the action
                    iLaunchAction = true;
                }
            }
        }
    }
