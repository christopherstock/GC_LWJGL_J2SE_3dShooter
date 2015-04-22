/*  $Id: Keys.java 249 2011-02-02 18:13:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public abstract class Keys
    {
        //--- walking ---//
        public  static          boolean             keyHoldWalkUp           = false;
        public  static          boolean             keyHoldWalkDown         = false;

        //--- turning ---//
        public  static          boolean             keyHoldTurnLeft         = false;
        public  static          boolean             keyHoldTurnRight        = false;

        //--- strafing ---//
        public  static          boolean             keyHoldStrafeLeft       = false;
        public  static          boolean             keyHoldStrafeRight      = false;

        //--- precise turning ---//
        public  static          boolean             keyHoldTurnLeftPrecise  = false;
        public  static          boolean             keyHoldTurnRightPrecise = false;

        //--- look ---//
        public  static          boolean             keyHoldLookUp           = false;
        public  static          boolean             keyHoldLookDown         = false;

        //--- modificators ---//
        public  static          boolean             keyHoldAlternate        = false;
        public  static          boolean             keyHoldFire             = false;
        public  static          boolean             keyHoldCenterView       = false;

        //--- actions ---//
        public  static          boolean             keyPressedCrouch        = false;
        public  static          boolean             keyPressedGainHealth    = false;
        public  static          boolean             keyPressedDamageFx      = false;
        public  static          boolean             keyPressedPlayerAction  = false;
        public  static          boolean             keyPressedReload        = false;
    }
