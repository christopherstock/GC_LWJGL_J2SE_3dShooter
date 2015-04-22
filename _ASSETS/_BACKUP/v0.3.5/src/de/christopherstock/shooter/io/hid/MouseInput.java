/*  $Id: MouseInput.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public abstract class MouseInput
    {
        public      static      float       mouseMovementX      = 0;
        public      static      float       mouseMovementY      = 0;

        public      static      float       lastMousePosX       = 0;
        public      static      float       lastMousePosY       = 0;

        public      static      boolean     mouseHoldFire       = false;
        public      static      boolean     mouseWheelDown      = false;
        public      static      boolean     mouseWheelUp        = false;
    }
