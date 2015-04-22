/*  $Id: Keys.java,v 1.1 2007/05/29 22:52:49 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid.swing;

    import  java.awt.event.*;
    import  de.christopherstock.shooter.*;
import  de.christopherstock.shooter.game.player.*;
import de.christopherstock.shooter.io.hid.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class SwingMouse extends Mouse implements MouseListener, MouseMotionListener, MouseWheelListener
    {
        public  static          SwingMouse          singleton               = new SwingMouse();

        @Override
        public void mouseWheelMoved( MouseWheelEvent mwe )
        {
            Debug.mouse.out( "Wheel event" );
            if ( mwe.getWheelRotation() == -1 )
            {
                Debug.shot.out( "Wheel rolled up" );
                // TODO perform this next tick!
                Player.singleton.orderNextWearponOrGadget();
            }
            else if ( mwe.getWheelRotation() == 1 )
            {
                Debug.shot.out( "Wheel rolled down" );
                // TODO perform this next tick!
                Player.singleton.orderPreviousWearponOrGadget();
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
            Debug.mouse.out( "Press event" );
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
            Debug.mouse.out( "released event" );
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
