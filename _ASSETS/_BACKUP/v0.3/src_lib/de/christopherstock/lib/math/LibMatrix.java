/*  $Id: LibMatrix.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.math;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   What is the matrix?
    *   Simple math-class exclusively containg static functionality for transforming matrices.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public abstract class LibMatrix
    {
        @SuppressWarnings( "unused" )
        private     static  final   float[][]       UNIT_MATRIX         = new float[][]
        {
            new float[] {   1,  0,  0,  0,   },
            new float[] {   0,  1,  0,  0,   },
            new float[] {   0,  0,  1,  0,   },
            new float[] {   0,  0,  0,  1,   },
        };

        public static final LibVertex[] transformVertices( LibVertex[] va, LibVertex ank, float angleX, float angleY, float angleZ )
        {
            Vector<LibVertex> ret = new Vector<LibVertex>();
            for ( LibVertex v : va )
            {
                v = transformVertex( v, ank, angleX, angleY, angleZ );
                ret.add( v );
            }
            return ret.toArray( new LibVertex[] {} );
        }

        public static final LibVertex transformVertex( LibVertex v, LibVertex ank, float angleX, float angleY, float angleZ )
        {
            //get ( and multiply ) rotation-matrices for X Y and Z axis
            float[][] trans =                  getRotXMatrix( angleX );
                      trans = LibMatrix.multiply( getRotYMatrix( angleY ), trans );
                      trans = LibMatrix.multiply( getRotZMatrix( angleZ ), trans );

            //translate by anchor
            LibVertex vReset = new LibVertex( v.x - ank.x, v.y - ank.y, v.z - ank.z, v.u, v.v );

            //assign transformation matrix
            float oldX = vReset.x;
            float oldY = vReset.y;
            float oldZ = vReset.z;
            vReset.x = oldX * trans[ 0 ][ 0 ] + oldY * trans[ 1 ][ 0 ] + oldZ * trans[  2 ][ 0 ] + trans[  3 ][ 0 ];
            vReset.y = oldX * trans[ 0 ][ 1 ] + oldY * trans[ 1 ][ 1 ] + oldZ * trans[  2 ][ 1 ] + trans[  3 ][ 1 ];
            vReset.z = oldX * trans[ 0 ][ 2 ] + oldY * trans[ 1 ][ 2 ] + oldZ * trans[  2 ][ 2 ] + trans[  3 ][ 2 ];

            //translate back by anchor
            LibVertex vFinal = new LibVertex( vReset.x + ank.x, vReset.y + ank.y, vReset.z + ank.z, vReset.u, vReset.v );

            return vFinal;
        }

        @SuppressWarnings( "unused" )
        private static final float[][] getTranslationMatrix( float x, float y, float z )
        {
            return new float[][]
            {
                new float[] { 1, 0, 0, 0 },
                new float[] { 0, 1, 0, 0 },
                new float[] { 0, 0, 1, 0 },
                new float[] { x, y, z, 1 },
            };
        }

        @SuppressWarnings( "unused" )
        private static final float[][] getScalationMatrix( float x, float y, float z )
        {
            return new float[][]
            {
                new float[] { x, 0, 0, 0 },
                new float[] { 0, y, 0, 0 },
                new float[] { 0, 0, z, 0 },
                new float[] { 0, 0, 0, 1 },
            };
        }

        private static final float[][] getRotXMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            return new float[][]
            {
                new float[] {   1,    0,     0,      0   },
                new float[] {   0,    cos,   sin,    0   },
                new float[] {   0,    sin,   cos,    0   },
                new float[] {   0,    0,     0,      1   },
            };
        }

        private static final float[][] getRotYMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            return new float[][]
            {
                new float[] {   cos,    0,      sin,    0   },
                new float[] {   0,      1,      0,      0   },
                new float[] {   -sin,   0,      cos,    0   },
                new float[] {   0,      0,      0,      1   },
            };
        }

        private static final float[][] getRotZMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            return new float[][]
            {
                new float[] {   cos,    sin,        0,      0   },
                new float[] {   -sin,   cos,        0,      0   },
                new float[] {   0,      0,          1,      0   },
                new float[] {   0,      0,          0,      1   },
            };
        }

        private static final float[][] multiply( float[][] matrix1, float[][] matrix2 )
        {
            float[][] mRet = new float[][]
            {
                new float[]     {   0, 0, 0, 0, },
                new float[]     {   0, 0, 0, 0, },
                new float[]     {   0, 0, 0, 0, },
                new float[]     {   0, 0, 0, 0, },
            };

            //Multiplizieren, und in MTemp ablegen
            for( int j = 0; j < 4; j++ )
            {
                for( int i = 0; i < 4; i++ )
                {
                    mRet[ i ][ j ] = 0;
                    for ( int n = 0; n < 4; n++ )
                    {
                        mRet[ i ][ j ] += matrix1[ i ][ n ] * matrix2[ n ][ j ];
                    }
                }
            }

            return mRet;

        }
    }
