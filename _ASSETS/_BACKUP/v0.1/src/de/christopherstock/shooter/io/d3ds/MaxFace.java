/*  $Id: Loader3dsmax.cs,v 1.5 2006/11/17 06:46:15 stock Exp $
 *  =================================================================================
 *  The 3dsmax-loader.
 */
    package de.christopherstock.shooter.io.d3ds;

    public class MaxFace
    {
        public  MaxVertex  vertex1 = null;
        public  MaxVertex  vertex2 = null;
        public  MaxVertex  vertex3 = null;

        public MaxFace( MaxVertex initVertex1, MaxVertex initVertex2, MaxVertex initVertex3 )
        {
            vertex1 = new MaxVertex( initVertex1 );
            vertex2 = new MaxVertex( initVertex2 );
            vertex3 = new MaxVertex( initVertex3 );
        }
    }
