/*  $Id: FaceQuad.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.face;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.g3d.*;

    /**************************************************************************************
    *   Represents a triangle face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class FaceQuad extends Face
    {
        /**************************************************************************************
        *   Copy constructor.
        **************************************************************************************/
        public FaceQuad( LibVertex aAnchor, LibVertex aA, LibVertex aB, LibVertex aC, LibVertex aD, LibColors aColor )
        {
            super( aAnchor, null, aColor, null );

            //set vertices
            setOriginalVertices( new LibVertex[] { aA, aB, aC, aD, } );
        }

        @Override
        public boolean checkCollisionHorz( Cylinder cylinder )
        {
            //empty implementation - debug circles can not be shot :)
            return false;
        }

        @Override
        public Vector<Float> checkCollisionVert( Cylinder cylinder )
        {
            //empty implementation - debug circles can not be shot :)
            return new Vector<Float>();
        }

        @Override
        protected void setCollisionValues()
        {
            //no collisions for this kind of face
        }
    }
