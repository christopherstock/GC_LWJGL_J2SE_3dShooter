/*  $Id: FaceEllipseFloor.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.face;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.*;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class FaceEllipseFloor extends LibFace
    {
        public FaceEllipseFloor( LibGLTexture aTexture, LibColors aCol, float aX, float aY, float aZ, float aRadiusX, float aRadiusY )
        {
            this( aTexture, aCol, aX, aY, aZ, aRadiusX, aRadiusY,LibMath.getRandom( 0, 360 ) );
        }

        public FaceEllipseFloor( LibGLTexture aTexture, LibColors aCol, float aX, float aY, float aZ, float aRadiusX, float aRadiusY, float textureRotation )
        {
            //call super-construct
            super( new LibVertex( aX, aY, aZ, 0.0f, 0.0f ), aTexture, aCol, null );

            //calculate vertices
            LibVertex[] ret = new LibVertex[ ShooterSettings.Performance.ELLIPSE_SEGMENTS ];
            for ( int i = 0; i < ShooterSettings.Performance.ELLIPSE_SEGMENTS; ++i )
            {
                float u = 0.0f;
                float v = 0.0f;

                u = ( LibMath.sinDeg(  90.0f + textureRotation + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                v = ( LibMath.cosDeg( -90.0f + textureRotation + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
              //v = ( LibMath.cosDeg(  90.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;

                ret[ i ] = new LibVertex
                (
                    aX + aRadiusX * LibMath.cosDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ),
                    aY + aRadiusY * LibMath.sinDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ),
                    aZ,
                    u,
                    v
                );
            }

            //set original vertices
            setOriginalVertices( ret );
        }

        @Override
        public void setCollisionValues()
        {
            //bullet holes do not have collision values
        }

        @Override
        public boolean checkCollisionHorz( Cylinder cylinder )
        {
            //empty implementation - debug circles can not be shot :)
            return false;
        }

        @Override
        public Vector<Float> checkCollisionVert( Cylinder cylinder )
        {
            //empty implementation - debug circles can not be shot :)
            return new Vector<Float>();
        }
    }
