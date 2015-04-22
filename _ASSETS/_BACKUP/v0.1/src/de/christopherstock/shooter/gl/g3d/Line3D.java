/*  $Id: Vertex.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.g3d;

    /**************************************************************************************
    *   Represents a face.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class Line3D
    {
        public                  Vertex              v1                  = null;
        public                  Vertex              v2                  = null;

        public Line3D( Vertex v1, Vertex v2 )
        {
            this.v1 = v1;
            this.v2 = v2;
        }

        public static final float getDistance3D( Vertex l1, Vertex l2 )
        {
            float distance = (float)Math.sqrt( Math.pow( l2.x - l1.x, 2 ) + Math.pow( l2.y - l1.y, 2 ) + Math.pow( l2.z - l1.z, 2 ) );
            return distance;

        }
    }
