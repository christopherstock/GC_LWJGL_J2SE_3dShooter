/*  $Id: FaceQuad.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.face;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.g3d.*;

    /**************************************************************************************
    *   Represents a triangle face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
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
        public boolean checkCollision( Cylinder c )
        {
            return false;
        }

        @Override
        protected void setCollisionValues()
        {
        }
    }
