/*  $Id: LWJGLMouse.java 269 2011-02-11 18:44:52Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.lwjgl;

    import  org.lwjgl.input.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.ui.hud.HUD.*;

    public class LWJGLMouse extends de.christopherstock.shooter.io.hid.Mouse
    {
        public static final void checkMouse()
        {
            int wheelSpin = Mouse.getDWheel();

            if ( wheelSpin < 0 )
            {
                ShooterDebugSystem.mouse.out( "Wheel rolled down - next wearpon" );
                Level.currentPlayer().orderWearponOrGadget( ChangeAction.EActionNext );
            }
            else if ( wheelSpin > 0 )
            {
                ShooterDebugSystem.mouse.out( "Wheel rolled up - previous wearpon" );
                Level.currentPlayer().orderWearponOrGadget( ChangeAction.EActionPrevious );
            }
        }
    }
