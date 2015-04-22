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
    public class Vertex
    {
        public                  float               x               = 0.0f;
        public                  float               y               = 0.0f;
        public                  float               z               = 0.0f;
        public                  float               u               = 0.0f;
        public                  float               v               = 0.0f;

        public Vertex( float initX, float initY, float initZ )
        {
            this( initX, initY, initZ, 0.0f, 0.0f );
        }

        public Vertex( float initX, float initY, float initZ, float initU, float initV )
        {
            x = initX;
            y = initY;
            z = initZ;
            u = initU;
            v = initV;
        }
    }
