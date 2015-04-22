/*  $Id: Face.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.gl.*;
    import de.christopherstock.shooter.math.*;
import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class FaceEllipseWall extends Face
    {
        public FaceEllipseWall( Texture textureID, Colors faceColor, float horzFaceAngle, float x, float y, float z, float radiusX, float radiusZ )
        {
            //call super-construct
            super( new Vertex( x, y, z, 0.0f, 0.0f ), textureID, faceColor );

            //Debug.out( "NEW ELLIPSE FACE [" + faceAngle + "]" );

            //substract 90° from the faceAngle
            horzFaceAngle -= 90.0f;

            //assign face angle
            setFaceAngleHorz( horzFaceAngle );
            setFaceAngleVert( -90.0f );

            //calculate vertices
            Vertex[] ret = new Vertex[ Shooter.ELLIPSE_SEGMENTS ];
            for ( int i = 0; i < Shooter.ELLIPSE_SEGMENTS; ++i )
            {
                float u = ( UMath.sinDeg(  90.0f + i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                float v = ( UMath.cosDeg( -90.0f + i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;

                ret[ i ] = new Vertex
                (
                    x + radiusX * UMath.cosDeg( horzFaceAngle ) * UMath.cosDeg( i * 360.0f / Shooter.ELLIPSE_SEGMENTS ),
                    y + radiusX * UMath.sinDeg( horzFaceAngle ) * UMath.cosDeg( i * 360.0f / Shooter.ELLIPSE_SEGMENTS ),
                    z + radiusZ * UMath.sinDeg( i * 360.0f / Shooter.ELLIPSE_SEGMENTS ),
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
