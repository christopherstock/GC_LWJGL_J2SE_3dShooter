/*  $Id: Vertex.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.lib.g3d;

    /**************************************************************************************
    *   Represents a point in 3d space.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class LibVertex
    {
        /**************************************************************************************
        *   X axis value.
        **************************************************************************************/
        public                  float               x               = 0.0f;

        /**************************************************************************************
        *   Y axis value.
        **************************************************************************************/
        public                  float               y               = 0.0f;

        /**************************************************************************************
        *   Z axis value.
        **************************************************************************************/
        public                  float               z               = 0.0f;

        /**************************************************************************************
        *   The horizontal texture coordinate.
        **************************************************************************************/
        public                  float               u               = 0.0f;

        /**************************************************************************************
        *   The vertical texture coordinate.
        **************************************************************************************/
        public                  float               v               = 0.0f;

        /**************************************************************************************
        *   Creates a vertex ignoring the texture coordinates.
        **************************************************************************************/
        public LibVertex( float initX, float initY, float initZ )
        {
            this( initX, initY, initZ, 0.0f, 0.0f );
        }

        /**************************************************************************************
        *   Creates a vertex with specified texture coordinates.
        **************************************************************************************/
        public LibVertex( float initX, float initY, float initZ, float initU, float initV )
        {
            x = initX;
            y = initY;
            z = initZ;
            u = initU;
            v = initV;
        }
    }
