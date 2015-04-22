/*  $Id: Vertex.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

import de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Line3D
    {
        public                  LibVertex              from                  = null;
        public                  LibVertex              to                  = null;

        public Line3D( LibVertex aFrom, LibVertex aTo )
        {
            from    = aFrom;
            to      = aTo;
        }

        public static final float getDistance3D( LibVertex l1, LibVertex l2 )
        {
            float distance = (float)Math.sqrt( Math.pow( l2.x - l1.x, 2 ) + Math.pow( l2.y - l1.y, 2 ) + Math.pow( l2.z - l1.z, 2 ) );
            return distance;
        }
    }
