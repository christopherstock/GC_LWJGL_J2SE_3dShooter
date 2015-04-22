/*  $Id: FaceEllipseWall.java 396 2011-03-13 22:16:58Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.face;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import de.christopherstock.shooter.g3d.*;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class FaceEllipseWall extends LibFace
    {
        public FaceEllipseWall( LibGLTexture aTextureID, float aHorzFaceAngle, float aX, float aY, float aZ, float aRadiusX, float aRadiusZ )
        {
            //call super-construct
            super( new LibVertex( aX, aY, aZ, 0.0f, 0.0f ), aTextureID, LibColors.EWhite, null );

            //substract 90° from the faceAngle
            aHorzFaceAngle -= 90.0f;

            //assign face angle
            setFaceAngleHorz( aHorzFaceAngle );
            setFaceAngleVert( -90.0f );

            //calculate vertices
            LibVertex[] ret = new LibVertex[ ShooterSettings.Performance.ELLIPSE_SEGMENTS ];
            for ( int i = 0; i < ShooterSettings.Performance.ELLIPSE_SEGMENTS; ++i )
            {
                float u = ( LibMath.sinDeg(  90.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                float v = ( LibMath.cosDeg( -90.0f + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;

                ret[ i ] = new LibVertex
                (
                    aX + aRadiusX * LibMath.cosDeg( aHorzFaceAngle ) * LibMath.cosDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ),
                    aY + aRadiusX * LibMath.sinDeg( aHorzFaceAngle ) * LibMath.cosDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ),
                    aZ + aRadiusZ * LibMath.sinDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ),
                    u,
                    v
                );
            }

            //set original vertices
            setOriginalVertices( ret );
        }

        @Override
        protected void setCollisionValues()
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
