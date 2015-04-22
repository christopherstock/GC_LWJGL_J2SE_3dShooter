/*  $Id: FaceEllipseWall.java 642 2011-04-24 01:38:53Z jenetic.bytemare $
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
    public class FaceEllipseWall extends LibFace
    {
        public FaceEllipseWall( LibGLTexture aTextureID, float aHorzFaceAngle, float aVertFaceAngle, float aX, float aY, float aZ, float aRadiusX, float aRadiusZ, float textureRotation )
        {
            //call super-construct
            super( new LibVertex( aX, aY, aZ, 0.0f, 0.0f ), aTextureID, LibColors.EWhite, null );

            //substract 90° from the horz faceAngle ! ( not from vert ! )
            aHorzFaceAngle -= 90.0f;

            //assign face angle
            setFaceAngleHorz( aHorzFaceAngle );
            setFaceAngleVert( aVertFaceAngle );

            //calculate vertices
            LibVertex[] ret = new LibVertex[ ShooterSettings.Performance.ELLIPSE_SEGMENTS ];
            for ( int i = 0; i < ShooterSettings.Performance.ELLIPSE_SEGMENTS; ++i )
            {
                float u = ( LibMath.sinDeg(  90.0f + textureRotation + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                float v = ( LibMath.cosDeg( -90.0f + textureRotation + i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;

                //turn ?
                float a = 0.0f; //LibMath.getRandom( 0, 360 ); //0.0f; //aHorzFaceAngle;

                LibVertex ve = new LibVertex
                (
                    aX + aRadiusX * LibMath.cosDeg( a ) * LibMath.cosDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ),
                    aY + aRadiusX * LibMath.sinDeg( a ) * LibMath.cosDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ),
                    aZ + aRadiusZ * LibMath.sinDeg( i * 360.0f / ShooterSettings.Performance.ELLIPSE_SEGMENTS ),
                    u,
                    v
                );

                //ve.rotateXYZ( 0.0f, 0.0f, 0.0f, new LibVertex( aX, aY, aZ ) );
                //ve.rotateXYZ( aVertFaceAngle * LibMath.sinDeg( aHorzFaceAngle ), aVertFaceAngle * LibMath.cosDeg( aHorzFaceAngle ), aHorzFaceAngle, new LibVertex( aX, aY, aZ ) );
                //ve.rotateXYZ( aVertFaceAngle * LibMath.sinDeg( aHorzFaceAngle ), aVertFaceAngle * LibMath.cosDeg( aHorzFaceAngle ), aHorzFaceAngle, new LibVertex( aX, aY, aZ ) );

                //rotate x ( vert face angle )
                ve.rotateXYZ( aVertFaceAngle, 0.0f, 0.0f, new LibVertex( aX, aY, aZ ) );

                //rotate z ( horz face angle )
                ve.rotateXYZ( 0.0f, 0.0f, aHorzFaceAngle, new LibVertex( aX, aY, aZ ) );

                ret[ i ] = ve;
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
