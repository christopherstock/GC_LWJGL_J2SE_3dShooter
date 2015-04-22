/*  $Id: FaceQuad.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
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
    *   @version    0.3.5
    **************************************************************************************/
    public class FaceQuad extends LibFace
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
        }
    }
