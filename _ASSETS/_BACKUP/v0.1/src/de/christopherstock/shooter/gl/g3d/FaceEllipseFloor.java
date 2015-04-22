/*  $Id: Face.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.g3d;

    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.ui.*;
import  de.christopherstock.shooter.util.*;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class FaceEllipseFloor extends Face
    {
        public                  float           x                                           = 0.0f;
        public                  float           y                                           = 0.0f;
        public                  float           z                                           = 0.0f;
        public                  float           radiusX                                     = 0.0f;
        public                  float           radiusY                                     = 0.0f;

        public FaceEllipseFloor( Texture textureID, Colors faceColor, float x, float y, float z, float radiusX, float radiusY )
        {
            //call super-construct
            super( new Vertex( x, y, z, 0.0f, 0.0f ), textureID, faceColor );

            //assign values
            this.x       = x;
            this.y       = y;
            this.z       = z;
            this.radiusX = radiusX;
            this.radiusY = radiusY;

            //assign collision ellipse  // radiands * 2 ?
            //setCollisionEllipse( new Ellipse2D.Float( x, y, radiusX, radiusY ) );

            //calculate vertices
            Vertex[] ret = new Vertex[ Shooter.ELLIPSE_SEGMENTS ];
            for ( int i = 0; i < Shooter.ELLIPSE_SEGMENTS; ++i )
            {
                float u = 0.0f;
                float v = 0.0f;

                switch ( 0 )
                {
                    case 3: //EEast:
                    {
                        u = ( UMath.sinDeg( i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( UMath.cosDeg( i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 2: //EWest:
                    {
                        u = ( UMath.sinDeg( 180.0f + i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( UMath.cosDeg( 180.0f + i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 1: //ESouth:
                    {
                        u = ( UMath.sinDeg( 270.0f + i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( UMath.cosDeg( 270.0f + i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }

                    case 0:
                    {
                        u = ( UMath.sinDeg( 90.0f + i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        v = ( UMath.cosDeg( 90.0f + i * 360.0f / Shooter.ELLIPSE_SEGMENTS ) + 1.0f ) / 2.0f;
                        break;
                    }
                }

                ret[ i ] = new Vertex
                (
                    x + radiusX * UMath.cosDeg( i * 360.0f / Shooter.ELLIPSE_SEGMENTS ),
                    y + radiusY * UMath.sinDeg( i * 360.0f / Shooter.ELLIPSE_SEGMENTS ),
                    z,
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
            //bullet holes do not have collision values!
        }
    }
