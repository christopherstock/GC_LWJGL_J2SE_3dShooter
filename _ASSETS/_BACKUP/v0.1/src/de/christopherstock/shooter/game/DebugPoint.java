/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.util.*;
    import  javax.media.opengl.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class DebugPoint
    {
        public      static  final   int                     ELifetimeForever    = -1;
        public      static  final   int                     ELifetimeShort      = 5;
        public      static  final   int                     ELifetimeMedium     = 10;
        public      static  final   int                     ELifetimeLong       = 100;

//        public      static  final   float                   POINT_SIZE          = 0.005f;
        public      static  final   float                   POINT_SIZE          = 0.01f;

        public      static          Vector<DebugPoint>      debugPoints         = new Vector<DebugPoint>();

        public                      Vertex                  point               = null;
        public                      Colors                  color               = null;
        public                      int                     lifetime            = 0;

        private DebugPoint( Colors color, float x, float y, float z, int lifetime )
        {
            this.point      = new Vertex( x, y, z, 0.0f, 1.0f );
            this.color      = color;
            this.lifetime   = lifetime;
        }

        public static final void add( Colors c, float x, float y, float z, int lifetime )
        {
            debugPoints.addElement( new DebugPoint( c, x, y, z, lifetime ) );
        }

        public static final void drawAll( GL gl )
        {
            //disable texture-mapping and set the points' color
            gl.glDisable(    GL.GL_TEXTURE_2D    );

            //draw all points
            for ( DebugPoint debugPoint : debugPoints )
            {
                //draw three faces per point

                gl.glColor3fv( debugPoint.color.f3, 0 );

                gl.glBegin( GL.GL_POLYGON );
                gl.glVertex3f( debugPoint.point.x - POINT_SIZE, debugPoint.point.z, debugPoint.point.y - POINT_SIZE );
                gl.glVertex3f( debugPoint.point.x - POINT_SIZE, debugPoint.point.z, debugPoint.point.y + POINT_SIZE );
                gl.glVertex3f( debugPoint.point.x + POINT_SIZE, debugPoint.point.z, debugPoint.point.y + POINT_SIZE );
                gl.glVertex3f( debugPoint.point.x + POINT_SIZE, debugPoint.point.z, debugPoint.point.y - POINT_SIZE );
                gl.glEnd();

                gl.glBegin( GL.GL_POLYGON );
                gl.glVertex3f( debugPoint.point.x, debugPoint.point.z - POINT_SIZE, debugPoint.point.y - POINT_SIZE );
                gl.glVertex3f( debugPoint.point.x, debugPoint.point.z - POINT_SIZE, debugPoint.point.y + POINT_SIZE );
                gl.glVertex3f( debugPoint.point.x, debugPoint.point.z + POINT_SIZE, debugPoint.point.y + POINT_SIZE );
                gl.glVertex3f( debugPoint.point.x, debugPoint.point.z + POINT_SIZE, debugPoint.point.y - POINT_SIZE );
                gl.glEnd();

                gl.glBegin( GL.GL_POLYGON );
                gl.glVertex3f( debugPoint.point.x - POINT_SIZE, debugPoint.point.z - POINT_SIZE, debugPoint.point.y );
                gl.glVertex3f( debugPoint.point.x + POINT_SIZE, debugPoint.point.z - POINT_SIZE, debugPoint.point.y );
                gl.glVertex3f( debugPoint.point.x + POINT_SIZE, debugPoint.point.z + POINT_SIZE, debugPoint.point.y );
                gl.glVertex3f( debugPoint.point.x - POINT_SIZE, debugPoint.point.z + POINT_SIZE, debugPoint.point.y );
                gl.glEnd();

            }
        }

        public static final void deleteAll()
        {
            debugPoints.removeAllElements();
        }

        public static final void updateLifetime()
        {
            //browse reversed for easy pruning outdated debugPoints
            for ( int i = debugPoints.size() - 1; i >= 0; --i )
            {
                if ( --debugPoints.elementAt( i ).lifetime == 0 ) debugPoints.removeElementAt( i );
            }
        }
    }