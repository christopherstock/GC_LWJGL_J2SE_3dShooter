/*  $Id: DebugPoint.java 202 2011-01-09 17:41:33Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class LibGLDebugPoint implements LibGLView.DebugPoint3D
    {
        public      static  final   int                     ELifetimeForever    = -1;
        public      static  final   int                     ELifetimeShort      = 5;
        public      static  final   int                     ELifetimeMedium     = 10;
        public      static  final   int                     ELifetimeLong       = 100;

        public      static  final   float                   POINT_SIZE          = 0.01f;

        public      static          Vector<LibGLDebugPoint>   debugPoints         = new Vector<LibGLDebugPoint>();

        public                      LibVertex               point               = null;
        public                      LibColors               color               = null;
        public                      int                     lifetime            = 0;

        private LibGLDebugPoint( LibColors aColor, float x, float y, float z, int aLifetime )
        {
            point      = new LibVertex( x, y, z, 0.0f, 1.0f );
            color      = aColor;
            lifetime   = aLifetime;
        }

        public static final void add( LibColors c, float x, float y, float z, int lifetime )
        {
            debugPoints.addElement( new LibGLDebugPoint( c, x, y, z, lifetime ) );
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
        public LibVertex getPoint()
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

        public static final void drawAll()
        {
            //draw all points
            for ( LibGLView.DebugPoint3D debugPoint : debugPoints )
            {
                LibGL3D.view.drawDebugPoint( debugPoint );
            }
        }
    }
