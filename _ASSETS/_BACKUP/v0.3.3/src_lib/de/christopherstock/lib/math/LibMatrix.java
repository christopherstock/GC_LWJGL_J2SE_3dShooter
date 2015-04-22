/*  $Id: LibMatrix.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.math;

    import  de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   What is the matrix?
    *   Simple math-class exclusively containg static functionality for transforming matrices.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class LibMatrix
    {
        @SuppressWarnings( "unused" )
        private     static  final   float[][]                       UNIT_MATRIX         = new float[][]
        {
            new float[] {   1,  0,  0,  0,   },
            new float[] {   0,  1,  0,  0,   },
            new float[] {   0,  0,  1,  0,   },
            new float[] {   0,  0,  0,  1,   },
        };

        private                     float[][]                       matrix              = null;

        public LibMatrix( float angleX, float angleY, float angleZ )
        {
          //float[][] transformationMatrix =                     getTranslationMatrix( 0.0f, 0.0f, 0.0f );

            matrix  =                     getAxisRotXMatrix( angleX );
            matrix  = LibMatrix.multiply( getAxisRotYMatrix( angleY ), matrix );
            matrix  = LibMatrix.multiply( getAxisRotZMatrix( angleZ ), matrix );
        }

        public final void transformVertices( LibVertex[] va, LibVertex ank )
        {
            for ( int i = 0; i < va.length; ++i )
            {
                va[ i ] = transformVertex( va[ i ], ank );
            }
        }

        public final LibVertex transformVertex( LibVertex vertex, LibVertex ank )
        {
            //translate by anchor
            vertex.x -= ank.x;
            vertex.y -= ank.y;
            vertex.z -= ank.z;

            //assign transformation matrix
            float oldX = vertex.x;
            float oldY = vertex.y;
            float oldZ = vertex.z;
            vertex.x = oldX * matrix[ 0 ][ 0 ] + oldY * matrix[ 1 ][ 0 ] + oldZ * matrix[  2 ][ 0 ] + matrix[  3 ][ 0 ];
            vertex.y = oldX * matrix[ 0 ][ 1 ] + oldY * matrix[ 1 ][ 1 ] + oldZ * matrix[  2 ][ 1 ] + matrix[  3 ][ 1 ];
            vertex.z = oldX * matrix[ 0 ][ 2 ] + oldY * matrix[ 1 ][ 2 ] + oldZ * matrix[  2 ][ 2 ] + matrix[  3 ][ 2 ];

            //translate back by anchor
            vertex.x += ank.x;
            vertex.y += ank.y;
            vertex.z += ank.z;

            return vertex;
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

        @Deprecated
        @SuppressWarnings( "unused" )
        private static final float[][] getVertexRotXMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            float[][] newMatrix = new float[][]
            {
                new float[] {   1,    0,     0,      0   },
                new float[] {   0,    cos,   -sin,   0   },
                new float[] {   0,    sin,   cos,    0   },
                new float[] {   0,    0,     0,      1   },
            };
            return newMatrix;
        }

        @Deprecated
        @SuppressWarnings( "unused" )
        private static final float[][] getVertexRotYMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            float[][] newMatrix = new float[][]
            {
                new float[] {   cos,    0,      sin,    0   },
                new float[] {   0,      1,      0,      0   },
                new float[] {   -sin,   0,      cos,    0   },
                new float[] {   0,      0,      0,      1   },
            };
            return newMatrix;
        }

        @Deprecated
        @SuppressWarnings( "unused" )
        private static final float[][] getVertexRotZMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            float[][] newMatrix = new float[][]
            {
                new float[] {   cos,    -sin,       0,      0   },
                new float[] {   sin,    cos,        0,      0   },
                new float[] {   0,      0,          1,      0   },
                new float[] {   0,      0,          0,      1   },
            };
            return newMatrix;
        }

        private static final float[][] getAxisRotXMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            float[][] newMatrix = new float[][]
            {
                new float[] {   1,    0,     0,      0   },
                new float[] {   0,    cos,   sin,    0   },
                new float[] {   0,   -sin,   cos,    0   },
                new float[] {   0,    0,     0,      1   },
            };
            return newMatrix;
        }

        private static final float[][] getAxisRotYMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            float[][] newMatrix = new float[][]
            {
                new float[] {   cos,    0,      -sin,   0   },
                new float[] {   0,      1,      0,      0   },
                new float[] {   sin,    0,      cos,    0   },
                new float[] {   0,      0,      0,      1   },
            };
            return newMatrix;
        }

        private static final float[][] getAxisRotZMatrix( float angle )
        {
            float sin = LibMath.sinDeg( angle );
            float cos = LibMath.cosDeg( angle );
            float[][] newMatrix = new float[][]
            {
                new float[] {   cos,    sin,        0,      0   },
                new float[] {   -sin,   cos,        0,      0   },
                new float[] {   0,      0,          1,      0   },
                new float[] {   0,      0,          0,      1   },
            };
            return newMatrix;
        }

        private static final float[][] multiply( float[][] matrix1, float[][] matrix2 )
        {
            float[][] ret = new float[][]
            {
                new float[]     {   0, 0, 0, 0, },
                new float[]     {   0, 0, 0, 0, },
                new float[]     {   0, 0, 0, 0, },
                new float[]     {   0, 0, 0, 0, },
            };

            //multiply and store in ret
            for( int j = 0; j < 4; j++ )
            {
                for( int i = 0; i < 4; i++ )
                {
                    ret[ i ][ j ] = 0;
                    for ( int n = 0; n < 4; n++ )
                    {
                        ret[ i ][ j ] += matrix1[ i ][ n ] * matrix2[ n ][ j ];
                    }
                }
            }

            return ret;
        }
    }
