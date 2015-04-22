/*  $Id: MeshCollection.java 630 2011-04-23 12:43:30Z jenetic.bytemare $
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
    *   Represents a collection of meshes.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class MeshCollection implements LibGeomObject
    {
        public                      LibVertex           iAnchor                      = null;

        public                      Mesh[]              iMeshes                      = null;

        protected MeshCollection()
        {
        }

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  aAnchor             The meshes' anchor point.
        **************************************************************************************/
        public MeshCollection( LibVertex aAnchor, Mesh[] aMeshes )
        {
            iAnchor  = aAnchor;
            iMeshes  = aMeshes;
        }

        /******************************************************************************************
        *   Sets/updates anchor.
        *
        *   @param  newAnchor                   The new anchor vertex point.
        *   @param  performTranslationOnFaces   Determines if the faces shall be translated
        *                                       by the new anchor.
        ******************************************************************************************/
        @Override
        public void setNewAnchor( LibVertex newAnchor, boolean performTranslationOnFaces, LibTransformationMode transformationMode )
        {
            iAnchor = newAnchor;
            for ( int i = 0; i < iMeshes.length; ++i )
            {
                iMeshes[ i ].setNewAnchor( newAnchor, performTranslationOnFaces, transformationMode );
            }
        }

        public void assignParentOnFaces( GameObject aParentGameObject )
        {
            for ( int i = 0; i < iMeshes.length; ++i )
            {
                iMeshes[ i ].assignParentOnFaces( aParentGameObject );
            }
        }

        /**************************************************************************************
        *   Translates all faces.
        *
        *   @param  tX  The translation for axis x.
        *   @param  tY  The translation for axis y.
        *   @param  tZ  The translation for axis z.
        **************************************************************************************/
        @Override
        public void translate( float tX, float tY, float tZ, LibTransformationMode transformationMode )
        {
            //translate all faces ( resetting the rotation! )
            for ( Mesh mesh : iMeshes )
            {
                //translate and init this face
                mesh.translate( tX, tY, tZ, transformationMode );
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
        public void translateAndRotateXYZ( float tX, float tY, float tZ, float rotX, float rotY, float rotZ, LibVertex alternateAnchor, LibTransformationMode transformationMode )
        {
            //rotate all faces
            for ( Mesh mesh : iMeshes )
            {
                //translate and init this face
                mesh.translateAndRotateXYZ( tX, tY, tZ, rotX, rotY, rotZ, alternateAnchor, transformationMode );
            }
        }

        /**************************************************************************************
        *   Draws the mesh.
        **************************************************************************************/
        public final void draw()
        {
            //draw all faces
            for ( Mesh mesh : iMeshes )
            {
                mesh.draw();
            }
        }

        public final Vector<Float> checkCollision( Point2D.Float point )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //check all meshes
            for ( Mesh mesh : iMeshes )
            {
                vecZ.addAll( mesh.checkCollision( point ) );
            }

            return vecZ;
        }

        @Override
        public final boolean checkCollision( Cylinder cylinder )
        {
            for ( Mesh mesh : iMeshes )
            {
                if ( mesh.checkCollision( cylinder ) ) return true;
            }
            return false;
        }

        public final Vector<HitPoint> launchShot( Shot shot )
        {
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //fire all faces and collect all hit-points
            for ( Mesh mesh : iMeshes )
            {
                hitPoints.addAll( mesh.launchShot( shot ) );
            }

            return hitPoints;
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iAnchor;
        }
    }
