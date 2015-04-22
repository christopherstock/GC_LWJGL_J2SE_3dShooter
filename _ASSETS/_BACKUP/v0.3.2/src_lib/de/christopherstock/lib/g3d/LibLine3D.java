/*  $Id: Line3D.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.g3d;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class LibLine3D
    {
        public                  LibVertex              from                  = null;
        public                  LibVertex              to                  = null;

        public LibLine3D( LibVertex aFrom, LibVertex aTo )
        {
            from    = aFrom;
            to      = aTo;
        }

        public static final float getDistance3D( LibVertex l1, LibVertex l2 )
        {
            float distance = (float)Math.sqrt( Math.pow( l2.x - l1.x, 2 ) + Math.pow( l2.y - l1.y, 2 ) + Math.pow( l2.z - l1.z, 2 ) );
            return distance;
        }

        public static final float getDistanceXY( LibVertex l1, LibVertex l2 )
        {
            float distance = (float)Math.sqrt( Math.pow( l2.x - l1.x, 2 ) + Math.pow( l2.y - l1.y, 2 ) );
            return distance;
        }
    }
