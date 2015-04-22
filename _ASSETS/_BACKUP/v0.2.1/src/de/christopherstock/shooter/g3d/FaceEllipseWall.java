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
    public class FaceEllipseWall extends Face
    {
        public FaceEllipseWall( GLTexture textureID, Colors faceColor, float horzFaceAngle, float x, float y, float z, float radiusX, float radiusZ )
        {
            //call super-construct
            super( new LibVertex( x, y, z, 0.0f, 0.0f ), textureID, faceColor );

            //Debug.out( "NEW ELLIPSE FACE [" + faceAngle + "]" );

            //substract 90° from the faceAngle
            horzFaceAngle -= 90.0f;

            //assign face angle
            setFaceAngleHorz( horzFaceAngle );
            setFaceAngleVert( -90.0f );

            //calculate vertices
            LibVertex[] ret = new LibVertex[ ShooterSettings.ELLIPSE_SEGMENTS ];
            for ( int i = 0; i < ShooterSettings.ELLIPSE_SEGMENTS; ++i )
            {
                float u = ( LibMath.sinDeg(  90.0f + i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                float v = ( LibMath.cosDeg( -90.0f + i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;

                ret[ i ] = new LibVertex
                (
                    x + radiusX * LibMath.cosDeg( horzFaceAngle ) * LibMath.cosDeg( i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ),
                    y + radiusX * LibMath.sinDeg( horzFaceAngle ) * LibMath.cosDeg( i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ),
                    z + radiusZ * LibMath.sinDeg( i * 360.0f / ShooterSettings.ELLIPSE_SEGMENTS ),
                    u,
                    v
                );
            }

            setOriginalVertices( ret );
        }

        @Override
        protected void setCollisionValues()
        {
            //bullet holes do not have collision values!
        }
    }
