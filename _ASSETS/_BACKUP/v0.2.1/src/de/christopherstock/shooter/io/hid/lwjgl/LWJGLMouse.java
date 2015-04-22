/*  $Id: LWJGLMouse.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.lwjgl;

    import  org.lwjgl.input.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.player.*;

    public class LWJGLMouse extends de.christopherstock.shooter.io.hid.Mouse
    {
        public static final void checkMouse()
        {
            int wheelSpin = Mouse.getDWheel();

            if ( wheelSpin < 0 )
            {
                Debug.mouse.out( "Wheel rolled down - next wearpon" );
                Player.singleton.orderNextWearponOrGadget();
            }
            else if ( wheelSpin > 0 )
            {
                Debug.mouse.out( "Wheel rolled up - previous wearpon" );
                Player.singleton.orderPreviousWearponOrGadget();
            }
        }
    }
