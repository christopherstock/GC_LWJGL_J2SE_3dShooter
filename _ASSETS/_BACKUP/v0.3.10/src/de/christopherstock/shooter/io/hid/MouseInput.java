/*  $Id: MouseInput.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public abstract class MouseInput
    {
        public      static      float       mouseMovementX      = 0;
        public      static      float       mouseMovementY      = 0;

        public      static      float       lastMousePosX       = 0;
        public      static      float       lastMousePosY       = 0;

        public      static      boolean     mouseHoldFire       = false;
        public      static      boolean     mouseHoldReload     = false;
        public      static      boolean     mouseHoldZoom       = false;

        public      static      boolean     mouseWheelDown      = false;
        public      static      boolean     mouseWheelUp        = false;
    }
