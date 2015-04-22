/*  $Id: LibLine3D.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.g3d;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class LibLine3D
    {
        public                  LibVertex              from                 = null;
        public                  LibVertex              to                   = null;

        public LibLine3D( LibVertex aFrom, LibVertex aTo )
        {
            from    = aFrom;
            to      = aTo;
        }

        public static final float getLength3D( LibVertex l1, LibVertex l2 )
        {
            float distance = (float)Math.sqrt( Math.pow( l2.x - l1.x, 2 ) + Math.pow( l2.y - l1.y, 2 ) + Math.pow( l2.z - l1.z, 2 ) );
            return distance;
        }

        public static final float getLengthXY( LibVertex l1, LibVertex l2 )
        {
            float distance = (float)Math.sqrt( Math.pow( l2.x - l1.x, 2 ) + Math.pow( l2.y - l1.y, 2 ) );
            return distance;
        }
    }
