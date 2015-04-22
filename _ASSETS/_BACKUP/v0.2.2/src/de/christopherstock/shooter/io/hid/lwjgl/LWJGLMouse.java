/*  $Id: LWJGLMouse.java 191 2010-12-13 20:24:11Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.lwjgl;

    import  org.lwjgl.input.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;

    public class LWJGLMouse extends de.christopherstock.shooter.io.hid.Mouse
    {
        public static final void checkMouse()
        {
            int wheelSpin = Mouse.getDWheel();

            if ( wheelSpin < 0 )
            {
                Debug.mouse.out( "Wheel rolled down - next wearpon" );
                Level.currentPlayer().orderNextWearponOrGadget();
            }
            else if ( wheelSpin > 0 )
            {
                Debug.mouse.out( "Wheel rolled up - previous wearpon" );
                Level.currentPlayer().orderPreviousWearponOrGadget();
            }
        }
    }
