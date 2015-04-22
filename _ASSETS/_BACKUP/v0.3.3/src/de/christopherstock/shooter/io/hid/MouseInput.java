/*  $Id: Mouse.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public abstract class MouseInput
    {
        public      static      float       mouseMovementX      = 0;
        public      static      float       mouseMovementY      = 0;

        public      static      float       lastMousePosX       = 0;
        public      static      float       lastMousePosY       = 0;

        public      static      boolean     mouseHoldFire       = false;
    }
