/*  $Id: Lib.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib;

    import  java.io.*;
    import  de.christopherstock.lib.math.*;

    /**************************************************************************************
    *   Use final instances of this class to declare different log groups.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public abstract class Lib
    {
        public static final class Offset
        {
            public      float   x   = 0;
            public      float   y   = 0;
            public      float   z   = 0;

            public Offset( float aX, float aY, float aZ )
            {
                x = aX;
                y = aY;
                z = aZ;
            }
        }

        public static enum Direction
        {
            ELeft,
            ETop,
            ERight,
            EBottom,
            ;
        }

        public static final class Rotation
        {
            public      float       x           = 0.0f;
            public      float       y           = 0.0f;
            public      float       z           = 0.0f;
            public      float       iSpeed      = 0.0f;

            public Rotation()
            {
                set( 0.0f, 0.0f, 0.0f, 0.0f );
            }

            public Rotation( float aX, float aY, float aZ )
            {
                set( aX, aY, aZ, 0.0f );
            }

            public void set( float aX, float aY, float aZ, float aSpeed )
            {
                x = aX;
                y = aY;
                z = aZ;
                iSpeed = aSpeed;
            }

            public void reachToAbsolute( Rotation targetPitch, float absoluteDistance )
            {
                x = LibMath.reachToAbsolute( x, targetPitch.x, absoluteDistance );
                y = LibMath.reachToAbsolute( y, targetPitch.y, absoluteDistance );
                z = LibMath.reachToAbsolute( z, targetPitch.z, absoluteDistance );
            }

            public Rotation copy()
            {
                return new Rotation( x, y, z );
            }

            public boolean equal( Rotation r )
            {
                return ( r.x == x && r.y == y && r.z == z );
            }

            public boolean equalRounded( Rotation other )
            {
                return
                (
                        Math.round( x ) == Math.round( other.x )
                    &&  Math.round( y ) == Math.round( other.y )
                    &&  Math.round( z ) == Math.round( other.z )
                );
            }

            @Override
            public String toString()
            {
                return "[" + x + "," + y + "," + z + "]";
            }
        }

        /**************************************************************************************
        *   Represents a camera adjustment in 3d space.
        *
        *   @author     Christopher Stock
        *   @version    0.3.10
        **************************************************************************************/
        public static final class ViewSet
        {
            public          Offset                      pos                 = null;
            public          Rotation                    rot                 = null;

            public ViewSet( float aPosX, float aPosY, float aPosZ, float aRotX, float aRotY, float aRotZ )
            {
                pos = new Offset(   aPosX, aPosY, aPosZ );
                rot = new Rotation( aRotX, aRotY, aRotZ );
            }
        }

        public static enum Invert
        {
            EYes,
            ENo,
            ;
        }

        public static enum Scalation
        {
            ELowerThreeQuarters(    0.25f   ),
            ELowerTwoThirds(        0.33f   ),
            ELowerHalf(             0.5f    ),
            ELowerThirds(           0.66f   ),
            ENone(                  1.0f    ),
            EAddQuarter(            1.25f   ),
            EAddThird(              1.33f   ),
            EAddHalf(               1.5f    ),
            EAddTwoThirds(          1.66f   ),
            EDouble(                2.0f    ),
            ETriple(                3.0f    ),
            EQuadruple(             4.0f    ),
            EQuintuple(             5.0f    ),
            ESextuple(              6.0f    ),
            ESeptuple(              7.0f    ),
            EOctuple(               8.0f    ),
            ENinefold(              9.0f    ),
            EDecuple(               10.0f   ),
            ;

            private         float           iScaleFactor            = 0.0f;

            private Scalation( float aScaleFactor )
            {
                iScaleFactor = aScaleFactor;
            }

            public final float getScaleFactor()
            {
                return iScaleFactor;
            }
        }

        public enum LibTransformationMode
        {
            EOriginalsToOriginals,
            EOriginalsToTransformed,
            ETransformedToTransformed,
            ;
        }

        public static enum LibAnimation
        {
            EAnimationNone,
            EAnimationShow,
            EAnimationHide,
            ;
        }

        public static enum ParticleQuantity
        {
            ETiny,
            ELow,
            EMedium,
            EHigh,
            EMassive,
            ;
        }

        public          static  final   int             MILLIS_PER_SECOND           = 1000;
        public          static  final   int             MILLIS_PER_MINUTE           = 60 * MILLIS_PER_SECOND;
        public          static  final   int             MILLIS_PER_HOUR             = 60 * MILLIS_PER_MINUTE;

        private         static  final   int             JAR_BUFFER_SIZE             = 0xffff;

        public static final ByteArrayInputStream preStreamJarResource( String url ) throws IOException
        {
            InputStream             in      = Thread.currentThread().getClass().getResourceAsStream( url );
            ByteArrayOutputStream   byteOut = new ByteArrayOutputStream();
            byte[]                  buffer  = new byte[ JAR_BUFFER_SIZE ];
            for ( int len; ( len = in.read( buffer ) ) != -1; ) byteOut.write( buffer, 0, len );
            in.close();
            ByteArrayInputStream    byteIn = new ByteArrayInputStream( byteOut.toByteArray() );
            return byteIn;
        }

        public static final void delay( long millis )
        {
            try { Thread.sleep( millis ); } catch ( InterruptedException ie ) {}
        }
    }
