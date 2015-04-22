/*  $Id: SwingMouse.java 269 2011-02-11 18:44:52Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.swing;

    import  java.awt.event.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.HUD.ChangeAction;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class SwingMouse extends Mouse implements MouseListener, MouseMotionListener, MouseWheelListener
    {
        public  static          SwingMouse          singleton               = new SwingMouse();

        @Override
        public void mouseWheelMoved( MouseWheelEvent mwe )
        {
            ShooterDebugSystem.mouse.out( "Wheel event" );
            if ( mwe.getWheelRotation() == -1 )
            {
                ShooterDebugSystem.mouse.out( "Wheel rolled down" );
                Level.currentPlayer().orderWearponOrGadget( ChangeAction.EActionNext );
            }
            else if ( mwe.getWheelRotation() == 1 )
            {
                ShooterDebugSystem.mouse.out( "Wheel rolled up" );
                Level.currentPlayer().orderWearponOrGadget( ChangeAction.EActionPrevious );
            }
        }

        @Override
        public void mouseClicked( MouseEvent me )
        {
            //Debug.DEBUG_OUT( "Click event" );
        }

        @Override
        public void mousePressed( MouseEvent me )
        {
            ShooterDebugSystem.mouse.out( "Press event" );
        }

        @Override
        public void mouseEntered( MouseEvent me )
        {
            //Debug.DEBUG_OUT( "entered event" );
        }

        @Override
        public void mouseExited( MouseEvent me )
        {
            //Debug.DEBUG_OUT( "exited event" );
        }

        @Override
        public void mouseReleased( MouseEvent me )
        {
            ShooterDebugSystem.mouse.out( "released event" );
        }

        @Override
        public void mouseDragged( MouseEvent me )
        {
            //Debug.DEBUG_OUT( "dragged event" );
        }

        @Override
        public void mouseMoved( MouseEvent me )
        {
            /*
            Debug.DEBUG_OUT( "Moved event" );
            Debug.DEBUG_OUT( " ["+me.getX()+","+me.getY()+"]["+me.getXOnScreen()+","+me.getYOnScreen()+"]" );

            mouseMovementX = me.getX() - lastMousePosX;
            mouseMovementY = me.getY() - lastMousePosY;

            lastMousePosX = me.getX();
            lastMousePosY = me.getY();
            */

        }
    }