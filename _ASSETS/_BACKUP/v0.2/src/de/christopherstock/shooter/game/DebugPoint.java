/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.util.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.gl.opengl.*;
    import  de.christopherstock.shooter.gl.opengl.GLView.DebugPoint3D;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class DebugPoint implements DebugPoint3D
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

        private DebugPoint( Colors aColor, float x, float y, float z, int aLifetime )
        {
            point      = new Vertex( x, y, z, 0.0f, 1.0f );
            color      = aColor;
            lifetime   = aLifetime;
        }

        public static final void add( Colors c, float x, float y, float z, int lifetime )
        {
            debugPoints.addElement( new DebugPoint( c, x, y, z, lifetime ) );
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

        @Override
        public Vertex getPoint()
        {
            return point;
        }

        @Override
        public float getPointSize()
        {
            return POINT_SIZE;
        }

        @Override
        public float[] getPointColor()
        {
            return color.f3;
        }

        public static final void drawDebugPoints()
        {
            //draw all points
            for ( DebugPoint3D debugPoint : debugPoints )
            {
                GLView.singleton.drawDebugPoint( debugPoint );
            }
        }
    }
