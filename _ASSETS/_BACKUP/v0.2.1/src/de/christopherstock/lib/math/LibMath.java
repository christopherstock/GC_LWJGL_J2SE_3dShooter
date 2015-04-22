/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.lib.math;

    import  java.awt.geom.*;
    import  java.io.*;

    /**************************************************************************************
    *   Simple math-wrapper-class.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public abstract class LibMath
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

        /**************************************************************************************
        *   Creates a random integer of the specified range.
        *   There is no need to set a random seed.
        *   {@link Math#random()} does this on the first invocation.
        **************************************************************************************/
        public static final int getRandom( int from, int to )
        {
            double  rand    = Math.random() * 1000;
            int     randI   = ( ( (int)rand % ( to + 1 - from ) ) + from );
            return randI;
        }

        public static final float[] col2f3( int col )
        {
            float r = ( col & 0xff0000 )  >>  16;
            float g = ( col & 0x00ff00 )  >>  8;
            float b = ( col & 0x0000ff )  >>  0;

            return new float[] { r / 255, g / 255, b / 255 };

        }

        public static final float getAngleCorrect( Point2D.Float a, Point2D.Float b ) {

            double distX = b.getX() - a.getX();
            double distY = b.getY() - a.getY();
            double angle = 0.0;

            if ( distX == 0.0 )
            {
                if ( distY == 0.0 )     angle = 0.0;
                else if( distY > 0.0 )  angle = Math.PI / 2.0;
                else                    angle = ( Math.PI * 3.0 ) / 2.0;
            }
            else if ( distY == 0.0 )
            {
                if ( distX > 0.0 )      angle = 0.0;
                else                    angle = Math.PI;
            }
            else
            {
                if ( distX < 0.0 )      angle = Math.atan( distY / distX ) + Math.PI;
                else if ( distY < 0.0 ) angle = Math.atan( distY / distX ) + ( 2 * Math.PI );
                else                    angle = Math.atan( distY / distX );
            }

            //to degree
            float ret = (float)( ( angle * 180 ) / Math.PI );
                  ret += 90.0f;

            return normalizeAngle( ret );
        }

        /**************************************************************************************
        *   Returns the (nearest) angle distance from angle1 to angle2.
        **************************************************************************************/
        public static final float getAngleDistance( float angleSrc, float angleDest )
        {
            float distance = 0.0f;

            distance = angleDest - angleSrc;
            while ( distance < -180.0f ) distance += 360.0f;
            while ( distance >  180.0f ) distance -= 360.0f;

            return distance;
            /*
            if ( angleSrc > angleDest )
            {
                distance = angleSrc - angleDest;
                if ( distance > 180.0f ) distance -= 360.0f;
            }
            else if ( angleDest > angleSrc )
            {
                distance = angleDest - angleSrc;
                if ( distance > 180.0f ) distance -= 360.0f;
            }
*/
  //          return distance;
        }

        public static final float normalizeAngle( int angle )
        {
            //Debug.out( "normalizing [" + angle + "]" );
            while ( angle <    0 ) angle += 360;
            while ( angle >= 360 ) angle -= 360;
            return angle;
        }

        public static final float normalizeAngle( float angle )
        {
            //Debug.out( "normalizing [" + angle + "]" );
            while ( angle <    0.0f ) angle += 360.0f;
            while ( angle >= 360.0f ) angle -= 360.0f;
            return angle;
        }

        public static final byte[] intArrayToByteArray( int[] ints )
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            for ( int i = 0; i < ints.length; ++i )
            {
                baos.write( ( ints[ i ] >> 0  ) & 0xff );
                baos.write( ( ints[ i ] >> 8  ) & 0xff );
                baos.write( ( ints[ i ] >> 16 ) & 0xff );
                baos.write( ( ints[ i ] >> 24 ) & 0xff );
            }

            return baos.toByteArray();
        }
    }
