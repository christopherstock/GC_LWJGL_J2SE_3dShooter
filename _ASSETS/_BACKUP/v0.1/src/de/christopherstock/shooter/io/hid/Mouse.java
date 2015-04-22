/*  $Id: Keys.java,v 1.1 2007/05/29 22:52:49 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    import  java.awt.event.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.player.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener
    {
        public      static      Mouse       singleton           = new Mouse();

        public      static      float       mouseMovementX      = 0;
        public      static      float       mouseMovementY      = 0;

        public      static      float       lastMousePosX       = 0;
        public      static      float       lastMousePosY       = 0;

        public void mouseWheelMoved( MouseWheelEvent mwe )
        {
            Debug.mouse.out( "Wheel event" );
            if ( mwe.getWheelRotation() == -1 )
            {
                Debug.shot.out( "Wheel rolled up" );
                Player.user.orderNextWearponOrGadget();
            }
            else if ( mwe.getWheelRotation() == 1 )
            {
                Debug.shot.out( "Wheel rolled down" );
                Player.user.orderPreviousWearponOrGadget();
            }
        }

        public void mouseClicked( MouseEvent me )
        {
            //Debug.DEBUG_OUT( "Click event" );
        }

        public void mousePressed( MouseEvent me )
        {
            Debug.mouse.out( "Press event" );
        }

        public void mouseEntered( MouseEvent me )
        {
            //Debug.DEBUG_OUT( "entered event" );
        }

        public void mouseExited( MouseEvent me )
        {
            //Debug.DEBUG_OUT( "exited event" );
        }

        public void mouseReleased( MouseEvent me )
        {
            Debug.mouse.out( "released event" );
        }

        public void mouseDragged( MouseEvent me )
        {
            //Debug.DEBUG_OUT( "dragged event" );
        }

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
