/*  $Id: Mesh.java 258 2011-02-10 00:11:05Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class Mesh extends GeomObject
    {
        public static enum Scalation
        {
            ESmallerThird(  0.33f ),
            ESmallerHalf(   0.5f  ),
            ENone(          1.0f  ),
            EHigherThird(   1.33f ),
            EHigherHalf(    1.5f  ),
            EHigherDouble(  2.0f  ),
            ;

            private         float           iScaleFactor            = 0.0f;

            private Scalation( float aScaleFactor )
            {
                iScaleFactor = aScaleFactor;
            }

            public final float getScaleFactor()
            {
                return iScaleFactor;
            }
        }

        private                 FreeTriangle[]      faces                           = null;

        protected Mesh( FreeTriangle[] aFaces )
        {
            faces = aFaces;
        }

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  aFaces              All faces this mesh shall consist of.
        *   @param                      The meshes' anchor point.
        *   @param  aCollisionEnabled   Specifies if the triangle-faces of this mesh shall throw collisions on firing.
        *   @param  aSpecialObject      This mesh may have functionality.
        **************************************************************************************/
        public Mesh( Mesh aMesh, LibVertex aAnchor, float aInitRotZ, float aInitScale, GameObject aParentGameObject )
        {
            this( aMesh.faces );

            //rotate all faces
            performOriginalRotationOnFaces(  aInitRotZ  );
            performOriginalScalationOnFaces( aInitScale );

            //set and translate by new anchor
            setNewAnchor( aAnchor, true );
            assignParentOnFaces( aParentGameObject );
        }

        /******************************************************************************************
        *   Sets/updates anchor.
        *
        *   @param  newAnchor                   The new anchor vertex point.
        *   @param  performTranslationOnFaces   Determines if the faces shall be translated
        *                                       by the new anchor.
        ******************************************************************************************/
        public void setNewAnchor( LibVertex newAnchor, boolean performTranslationOnFaces )
        {
            //assign new anchor for this mesh and for all it's faces - translate all faces by the new anchor!
            anchor = newAnchor;
            for ( int i = 0; i < faces.length; ++i )
            {
                faces[ i ].setNewAnchor( anchor );
                if ( performTranslationOnFaces ) faces[ i ].translate( anchor.x, anchor.y, anchor.z, false );
            }
        }

        protected void assignParentOnFaces( GameObject aParentGameObject )
        {
            for ( int i = 0; i < faces.length; ++i )
            {
                faces[ i ].assignParentGameObject( aParentGameObject );
            }
        }

        private void performOriginalRotationOnFaces( float aRotZ )
        {
            for ( int i = 0; i < faces.length; ++i )
            {
                faces[ i ].translateAndRotateXYZ( 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, aRotZ, true );
            }
        }

        private void performOriginalScalationOnFaces( float aScale )
        {
            for ( int i = 0; i < faces.length; ++i )
            {
                faces[ i ].scale( aScale, true );
            }
        }

        /**************************************************************************************
        *   Translates all faces.
        *
        *   @param  tX  The translation for axis x.
        *   @param  tY  The translation for axis y.
        *   @param  tZ  The translation for axis z.
        **************************************************************************************/
        public void translate( float tX, float tY, float tZ )
        {
            //translate all faces ( resetting the rotation! )
            for ( FreeTriangle face : faces )
            {
                //translate and init this face
                face.translate( tX, tY, tZ, false );
            }
        }

        /**************************************************************************************
        *   Rotates all faces of this mesh.
        *
        *   @param  tX          The amount to translate this vertex on the x-axis.
        *   @param  tY          The amount to translate this vertex on the y-axis.
        *   @param  tZ          The amount to translate this vertex on the z-axis.
        *   @param  rotX        The x-axis-angle to turn all vertices around.
        *   @param  rotY        The y-axis-angle to turn all vertices around.
        *   @param  rotZ        The z-axis-angle to turn all vertices around.
        **************************************************************************************/
        public void translateAndRotateXYZ( float tX, float tY, float tZ, float rotX, float rotY, float rotZ )
        {
            //rotate all faces
            for ( FreeTriangle face : faces )
            {
                //translate and init this face
                face.translateAndRotateXYZ( tX, tY, tZ, rotX, rotY, rotZ, false );
            }
        }

        /**************************************************************************************
        *   Draws the mesh.
        **************************************************************************************/
        public final void draw()
        {
            //draw all faces
            for ( FreeTriangle face : faces )
            {
                face.draw();
            }
        }

        public final boolean checkAction( Cylinder cylinder )
        {
            //browse all faces and check if any face is affected by the action
            for ( FreeTriangle face : faces )
            {
                if ( cylinder.checkCollision( face, true ) )
                {
                    return true;
                }
            }

            return false;
        }

        public final Vector<Float> checkCollision( Point2D.Float point )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //check all faces
            for ( FreeTriangle face : faces )
            {
                Float f = face.checkCollision( point );

                if ( f != null )
                {
                    vecZ.addElement( f );
                }
            }

            return vecZ;
        }

        @Override
        public final boolean checkCollision( Cylinder cylinder )
        {
            for ( FreeTriangle face : faces )
            {
                if ( cylinder.checkCollision( face, false ) ) return true;
            }
            return false;
        }

        @Override
        public final Vector<HitPoint> launchShot( Shot shot )
        {
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //fire all faces and collect all hit-points
            for ( FreeTriangle face : faces )
            {
                HitPoint hitPoint = face.launchShot( shot );
                if ( hitPoint != null )
                {
                    hitPoints.addElement( hitPoint );
                }
            }

            return hitPoints;
        }
    }
