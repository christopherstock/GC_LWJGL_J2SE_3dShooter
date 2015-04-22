/*  $Id: GLView.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

import de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author         Christopher Stock
    *   @version        0.2
    **************************************************************************************/
    public abstract class GLView
    {
        /**************************************************************************************
        *   Debug point's interface.
        *
        *   @author     Christopher Stock
        *   @version    0.2
        **************************************************************************************/
        public interface DebugPoint3D
        {
            public abstract LibVertex  getPoint();
            public abstract float   getPointSize();
            public abstract float[] getPointColor();
        }

        protected   static  final   float               DEPTH_BUFFER_SIZE       = 1.0f;

        protected   static  final   float               VIEW_ANGLE              = 45.0f;
        protected   static  final   float               VIEW_MIN                = 0.1f;
        protected   static  final   float               VIEW_MAX                = 100.0f;

        public      static          GLView              singleton               = null;

        protected                   GLImage[]           textureImages           = null;

        public abstract void drawFace( LibVertex[] verticesToDraw, GLTexture texture, float[] col );

        public abstract void drawDebugPoint( DebugPoint3D fxp );

        public abstract void setPlayerCamera( float posX, float posY, float posZ, float rotX, float rotY, float rotZ );

        public abstract void clearGl();

        public abstract void drawOrthoBitmapBytes( GLImage glImage, int x, int y );

        public abstract void setOrthoOn();

        public abstract void setOrthoOff();

/*
        public static final void drawLine( Vertex v1, Vertex v2 )
        {
            Debug.shot.out( " >> DRAW LINE: ["+v1.x+"]["+v1.y+"]["+v1.z+"] ["+v2.x+"]["+v2.y+"]["+v2.z+"]" );

            float distX = v1.x - v2.x;
            float distY = v1.y - v2.y;
            float distZ = v1.z - v2.z;

            Debug.shot.out( " >> DIST X: " + distX );
            Debug.shot.out( " >> DIST Y: " + distY );
            Debug.shot.out( " >> DIST Z: " + distZ );

            //draw shot line with own calculations
            for ( float distance = 0.0f; distance < 10.0f; distance += 0.01f )
            {
                new DebugPoint
                (
                    Colors.EPink,
                    v1.x - distX * distance / 10.0f,
                    v1.y - distY * distance / 10.0f,
                    v1.z - distZ * distance / 10.0f,
                    DebugPoint.ELifetimeForever
                );

            }
        }
*/    }
