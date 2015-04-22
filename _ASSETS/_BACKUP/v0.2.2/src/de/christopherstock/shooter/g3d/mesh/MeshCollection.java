/*  $Id: MeshCollection.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class MeshCollection extends GeomObject
    {
        protected                   Mesh[]              meshes                              = null;

        protected MeshCollection()
        {
        }

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  aFaces              All faces this mesh shall consist of.
        *   @param  iAnchor              The meshes' anchor point.
        *   @param  aCollisionEnabled   Specifies if the triangle-faces of this mesh shall throw collisions on firing.
        *   @param  aSpecialObject      This mesh may have functionality.
        **************************************************************************************/
        public MeshCollection( LibVertex aAnchor, Mesh[] aMeshes )
        {
            anchor  = aAnchor;
            meshes  = aMeshes;
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
            for ( int i = 0; i < meshes.length; ++i )
            {
                meshes[ i ].setNewAnchor( newAnchor, performTranslationOnFaces );
            }
        }

        public void assignParentOnFaces( GameObject aParentGameObject )
        {
            for ( int i = 0; i < meshes.length; ++i )
            {
                meshes[ i ].assignParentOnFaces( aParentGameObject );
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
            for ( Mesh mesh : meshes )
            {
                //translate and init this face
                mesh.translate( tX, tY, tZ );
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
            for ( Mesh mesh : meshes )
            {
                //translate and init this face
                mesh.translateAndRotateXYZ( tX, tY, tZ, rotX, rotY, rotZ );
            }
        }

        /**************************************************************************************
        *   Draws the mesh.
        **************************************************************************************/
        public final void draw()
        {
            //draw all faces
            for ( Mesh mesh : meshes )
            {
                mesh.draw();
            }
        }

        public final boolean checkAction( Cylinder cylinder )
        {
            //browse all faces and check if any face is affected by the action
            for ( Mesh mesh : meshes )
            {
                if ( mesh.checkAction( cylinder ) )
                {
                    return true;
                }
            }

            return false;
        }

        public final Vector<Float> launchFloorCheck( Point2D.Float point )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //check all meshes
            for ( Mesh mesh : meshes )
            {
                vecZ.addAll( mesh.checkCollision( point ) );
            }

            return vecZ;
        }

        @Override
        public final boolean checkCollision( Cylinder cylinder )
        {
            for ( Mesh mesh : meshes )
            {
                if ( mesh.checkCollision( cylinder ) ) return true;
            }
            return false;
        }

        @Override
        public final Vector<HitPoint> launchShot( Shot shot )
        {
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //fire all faces and collect all hit-points
            for ( Mesh mesh : meshes )
            {
                hitPoints.addAll( mesh.launchShot( shot ) );
            }

            return hitPoints;
        }
    }
