/*  $Id: LibVertex.java 184 2010-11-16 01:08:40Z jenetic.bytemare $
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
        public LibVertex( float aX, float aY, float aZ )
        {
            this( aX, aY, aZ, 0.0f, 0.0f );
        }

        /**************************************************************************************
        *   Creates a vertex with specified texture coordinates.
        **************************************************************************************/
        public LibVertex( float aX, float aY, float aZ, float aU, float aV )
        {
            x = aX;
            y = aY;
            z = aZ;
            u = aU;
            v = aV;
        }
    }