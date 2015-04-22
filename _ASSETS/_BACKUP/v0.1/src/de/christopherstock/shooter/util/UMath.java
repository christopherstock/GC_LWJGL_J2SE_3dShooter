/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.util;

    import  java.awt.geom.*;

    /**************************************************************************************
    *   Simple math-wrapper-class.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public abstract class UMath
    {
        /**************************************************************************************
        *   Delivers the sin-value from a degree-value.
        *
        *   @param  degrees     The degrees to get the sin-value for.
        *   @return             The sin-value from -1.0f to 1.0f of the given degree value.
        **************************************************************************************/
        public static final float sinDeg( float degrees )
        {
            return (float)Math.sin( degrees * Math.PI / 180.0f );
        }

        /**************************************************************************************
        *   Delivers the cos-value from a degree-value.
        *
        *   @param  degrees     The degrees to get the cos-value for.
        *   @return     The cos-value from -1.0f to 1.0f of the given degree value.
        **************************************************************************************/
        public static final float cosDeg( float degrees )
        {
            return (float)Math.cos( degrees * Math.PI / 180.0f );
        }

        public static final int getRandom( int from, int to )
        {
            double rand = Math.random() * 1000;
            return ( ( (int)rand % ( to + 1 - from ) ) + from );
        }

        public static final float[] col2f3( int col )
        {
            float r = ( col & 0xff0000 )  >>  16;
            float g = ( col & 0x00ff00 )  >>  8;
            float b = ( col & 0x0000ff )  >>  0;

            return new float[] { r / 255, g / 255, b / 255 };

        }

        public static final float getAngleCorrect( Point2D.Float a, Point2D.Float b ) {

            double dx = b.getX() - a.getX();
            double dy = b.getY() - a.getY();
            double angle = 0.0;

            if (dx == 0.0) {
                if(dy == 0.0)     angle = 0.0;
                else if(dy > 0.0) angle = Math.PI / 2.0;
                else              angle = (Math.PI * 3.0) / 2.0;
            }
            else if(dy == 0.0) {
                if(dx > 0.0)      angle = 0.0;
                else              angle = Math.PI;
            }
            else {
                if(dx < 0.0)      angle = Math.atan(dy/dx) + Math.PI;
                else if(dy < 0.0) angle = Math.atan(dy/dx) + (2*Math.PI);
                else              angle = Math.atan(dy/dx);
            }

            //to degree
            float ret = (float)( ( angle * 180 ) / Math.PI );
                  ret += 90.0f;

            return normalizeAngle( ret );
        }

        public static final float normalizeAngle( float angle )
        {
            //Debug.out( "normalizing [" + angle + "]" );
            while ( angle <    0.0f ) angle += 360.0f;
            while ( angle >= 360.0f ) angle -= 360.0f;
            return angle;
        }
    }
