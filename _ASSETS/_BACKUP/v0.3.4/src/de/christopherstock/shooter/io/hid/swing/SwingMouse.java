/*  $Id: SwingMouse.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.swing;

    import  java.awt.event.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.HUD.ChangeAction;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class SwingMouse extends MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener
    {
        public  static          SwingMouse          singleton               = new SwingMouse();

        @Override
        public void mouseWheelMoved( MouseWheelEvent mwe )
        {
            ShooterDebug.mouse.out( "Wheel event" );
            if ( mwe.getWheelRotation() == -1 )
            {
                ShooterDebug.mouse.out( "Wheel rolled down" );
                ShooterGameLevel.currentPlayer().orderWearponOrGadget( ChangeAction.EActionNext );
            }
            else if ( mwe.getWheelRotation() == 1 )
            {
                ShooterDebug.mouse.out( "Wheel rolled up" );
                ShooterGameLevel.currentPlayer().orderWearponOrGadget( ChangeAction.EActionPrevious );
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
            ShooterDebug.mouse.out( "Press event" );
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
            ShooterDebug.mouse.out( "released event" );
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
