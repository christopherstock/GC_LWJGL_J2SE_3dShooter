/*  $Id: LWJGLMouse.java 793 2011-05-27 22:36:57Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.lwjgl;

    import  org.lwjgl.input.Mouse;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.io.hid.*;

    public class LWJGLMouse extends MouseInput
    {
        public static final void checkMouse()
        {
            //check if mouse event occured
            if ( Mouse.next() )
            {
                //check wheel event
                int wheelSpin = Mouse.getEventDWheel();
                if ( wheelSpin < 0 )
                {
                    ShooterDebug.mouse.out( "Wheel rolled down - next wearpon" );
                    MouseInput.mouseWheelDown = true;
                }
                else if ( wheelSpin > 0 )
                {
                    ShooterDebug.mouse.out( "Wheel rolled up - previous wearpon" );
                    MouseInput.mouseWheelUp   = true;
                }

                //check button event
                int     buttonPress = Mouse.getEventButton();
                boolean down        = Mouse.getEventButtonState();
                switch ( buttonPress )
                {
                    //left mousekey
                    case 0:
                    {
                        //assign to fire
                        MouseInput.mouseHoldFire = down;
                        break;
                    }

                    //right mousekey
                    case 1:
                    {
                        //if ( down ) ShooterDebug.bugfix.out( "right down"  );
                        break;
                    }
                }
            }
        }
    }
