/*  $Id: Face.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import de.christopherstock.lib.g3d.*;
import de.christopherstock.lib.math.*;
import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.*;
import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class FaceEllipseFloor extends Face
    {
        public                  float           x                                           = 0.0f;
        public                  float           y                                           = 0.0f;
        public                  float           z                                           = 0.0f;
        public                  float           radiusX                                     = 0.0f;
        public                  float           radiusY                                     = 0.0f;

        public FaceEllipseFloor( GLTexture textureID, Colors faceColor, float aX, float aY, float aZ, float aRadiusX, float aRadiusY )
        {
            //call super-construct
            super( new LibVertex( aX, aY, aZ, 0.0f, 0.0f ), textureID, faceColor );

            //assign values
            x       = aX;
            y       = aY;
            z       = aZ;
            radiusX = aRadiusX;
            radiusY = aRadiusY;

            //assign collision ellipse  // radiands * 2 ?
            //setCollisionEllipse( new Ellipse2D.Float( x, y, radiusX, radiusY ) );

            //calculate vertices
            LibVertex[] ret = new LibVertex[ ShooterSettings.ELLIPSE_SEGMENTS ];
            for ( int i = 0; i < ShooterSettings.ELLIPSE_SEGMENTS; ++i )
            {
                float u = 0.0f;
                float v = 0.0f;

                switch ( 0 )
                {
                    case 3: //EEast:
                    {
                        u = ( LibMath.sinDeg( i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( LibMath.cosDeg( i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 2: //EWest:
                    {
                        u = ( LibMath.sinDeg( 180.0f + i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( LibMath.cosDeg( 180.0f + i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 1: //ESouth:
                    {
                        u = ( LibMath.sinDeg( 270.0f + i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( LibMath.cosDeg( 270.0f + i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 0:
                    {
                        u = ( LibMath.sinDeg( 90.0f + i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( LibMath.cosDeg( 90.0f + i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }
                }

                ret[ i ] = new LibVertex
                (
                    aX + aRadiusX * LibMath.cosDeg( i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ),
                    aY + aRadiusY * LibMath.sinDeg( i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ),
                    aZ,
                    u,
                    v
                );
            }

            //set vertices
            setOriginalVertices( ret );
        }

        @Override
        public void setCollisionValues()
        {
            //bullet holes do not have collision values! ;)
        }
    }
