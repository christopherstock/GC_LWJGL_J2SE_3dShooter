/*  $Id: FaceEllipseFloor.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.face;

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
    *   @version    0.3.3
    **************************************************************************************/
    public class FaceEllipseFloor extends LibFace
    {
        public FaceEllipseFloor( LibGLTexture aTexture, LibColors aCol, float aX, float aY, float aZ, float aRadiusX, float aRadiusY )
        {
            //call super-construct
            super( new LibVertex( aX, aY, aZ, 0.0f, 0.0f ), aTexture, aCol, null );

            //calculate vertices
            LibVertex[] ret = new LibVertex[ ShooterSettings.Performance.ELLIPSE_SEGMENTS ];
            for ( int i = 0; i < ShooterSettings.Performance.ELLIPSE_SEGMENTS; ++i )
            {
                float u = 0.0f;
                float v = 0.0f;

                switch ( 0 )
                {
                    case 3: //EEast:
                    {
                        u = ( LibMath.sinDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( LibMath.cosDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 2: //EWest:
                    {
                        u = ( LibMath.sinDeg( 180.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( LibMath.cosDeg( 180.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 1: //ESouth:
                    {
                        u = ( LibMath.sinDeg( 270.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( LibMath.cosDeg( 270.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 0:
                    {
                        u = ( LibMath.sinDeg( 90.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( LibMath.cosDeg( 90.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }
                }

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
        public boolean checkCollision( Cylinder cylinder )
        {
            //empty implementation - debug circles can not be shot :)
            return false;
        }
    }
