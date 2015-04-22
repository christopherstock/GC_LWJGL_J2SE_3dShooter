/*  $Id: Mesh.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.g3d.face.LibFace.DrawMethod;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.objects.GameObject.*;
    import  de.christopherstock.shooter.io.hid.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class Mesh implements LibGeomObject
    {
        public                  LibVertex           iAnchor                         = null;

        private                 FaceTriangle[]      iFaces                          = null;

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  aFaces              All faces this mesh shall consist of.
        *   @param  aAnchor             The meshes' anchor point.
        **************************************************************************************/
        public Mesh( FaceTriangle[] aFaces, LibVertex aAnchor, float aInitRotZ, float aInitScale, GameObject aParentGameObject, LibTransformationMode transformationMode, DrawMethod aDrawMethod )
        {
            iFaces = aFaces;

            //rotate all faces
            performOriginalRotationOnFaces(  aInitRotZ  );
            performOriginalScalationOnFaces( aInitScale );

            //set and translate by new anchor
            setNewAnchor( aAnchor, true, transformationMode );

            //assign parent game object
            assignParentOnFaces(     aParentGameObject  );
            assignDrawMethodOnFaces( aDrawMethod        );
        }

        public FaceTriangle[] getFaces()
        {
            return iFaces;
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
            //assign new anchor for this mesh and for all it's faces - translate all faces by the new anchor!
            iAnchor = newAnchor;
            for ( int i = 0; i < iFaces.length; ++i )
            {
                iFaces[ i ].setNewAnchor( iAnchor, performTranslationOnFaces, transformationMode );
            }
        }

        protected void assignParentOnFaces( GameObject aParentGameObject )
        {
            for ( int i = 0; i < iFaces.length; ++i )
            {
                iFaces[ i ].assignParentGameObject( aParentGameObject );
            }
        }

        protected void assignDrawMethodOnFaces( DrawMethod aDrawMethod )
        {
            for ( int i = 0; i < iFaces.length; ++i )
            {
                iFaces[ i ].setDrawMethod( aDrawMethod );
            }
        }

        private void performOriginalRotationOnFaces( float aRotZ )
        {
            LibMatrix transformationMatrix = new LibMatrix( 0.0f, 0.0f, aRotZ );

            for ( int i = 0; i < iFaces.length; ++i )
            {
                iFaces[ i ].translateAndRotateXYZ( transformationMatrix, 0.0f, 0.0f, 0.0f, LibTransformationMode.EOriginalsToOriginals, null );
            }
        }

        private void performOriginalScalationOnFaces( float aScale )
        {
            for ( int i = 0; i < iFaces.length; ++i )
            {
                iFaces[ i ].scale( aScale, true );
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
            for ( FaceTriangle face : iFaces )
            {
                //translate and init this face
                face.translate( tX, tY, tZ, transformationMode );
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
            LibMatrix transformationMatrix = new LibMatrix( rotX, rotY, rotZ );

            //rotate all faces
            for ( FaceTriangle face : iFaces )
            {
                //translate and init this face
                face.translateAndRotateXYZ( transformationMatrix, tX, tY, tZ, transformationMode, alternateAnchor );
            }
        }

        public final void mirrorFaces( boolean x, boolean y, boolean z )
        {
            //draw all faces
            for ( FaceTriangle face : iFaces )
            {
                face.mirror( x, y, z );
            }
        }

        /**************************************************************************************
        *   Draws the mesh.
        **************************************************************************************/
        public void draw()
        {
            //draw all faces
            for ( FaceTriangle face : iFaces )
            {
                face.draw();
            }
        }

        public final boolean checkAction( Cylinder cylinder )
        {
            //browse all faces and check if any face is affected by the action
            for ( FaceTriangle face : iFaces )
            {
                if ( cylinder.checkCollisionHorzLines( face, true, false ) )
                {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean checkCollisionHorz( Cylinder cylinder )
        {
            return checkCollisionHorz( cylinder, WallClimbable.EYes );
        }

        public boolean checkCollisionHorz( Cylinder cylinder, WallClimbable wallClimbable )
        {
            //check all faces
            for ( FaceTriangle face : iFaces )
            {
                boolean b = face.checkCollisionHorz( cylinder, wallClimbable );
                if ( b ) return true;
            }

            return false;
        }

        @Override
        public Vector<Float> checkCollisionVert( Cylinder cylinder )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //check all faces
            for ( FaceTriangle face : iFaces )
            {
                vecZ.addAll( face.checkCollisionVert( cylinder ) );
            }

            return vecZ;
        }

        public void changeTexture( LibGLTexture tex1, LibGLTexture tex2 )
        {
            for ( FaceTriangle face : iFaces )
            {
                face.changeTexture( tex1, tex2 );
            }
        }

        public Vector<HitPoint> launchShot( Shot shot )
        {
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //fire all faces and collect all hit-points
            for ( FaceTriangle face : iFaces )
            {
                //try new shot algo
                HitPoint hp = face.launchShotNew( shot );
                //if ( hp != null ) ShooterDebug.bugfix.out( "hitpoint from NEW algo" );
                if ( hp == null )
                {
                    //try old shot algo if no hitpoint was returned
                    hp = face.launchShotOld( shot );
                    //if ( hp != null ) ShooterDebug.bugfix.out( "hitpoint from OLD algo" );
                }

                //add to collection if hitpoint is available
                if ( hp != null )
                {
                    hitPoints.add( hp );
                }
            }

            return hitPoints;
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iAnchor;
        }

        public final void makeDistancedSound( Sound fx )
        {
            fx.playDistancedFx( new Point2D.Float( iAnchor.x, iAnchor.y ) );
        }

        public final Point2D.Float getCenterPointXY()
        {
            Float lowestX  = null;
            Float lowestY  = null;
            Float highestX = null;
            Float highestY = null;

            for ( LibFace f : getFaces() )
            {
                for ( LibVertex v : f.getOriginalVertices() )
                {
                    if ( lowestX  == null || v.x < lowestX.floatValue()  ) lowestX  = new Float( v.x );
                    if ( lowestY  == null || v.y < lowestY.floatValue()  ) lowestY  = new Float( v.y );
                    if ( highestX == null || v.x > highestX.floatValue() ) highestX = new Float( v.x );
                    if ( highestY == null || v.y > highestY.floatValue() ) highestY = new Float( v.y );
                }
            }

            if ( lowestX == null || lowestY == null || highestX == null || highestY == null )
            {
                return null;
            }

            return new Point2D.Float
            (
                lowestX.floatValue() + ( highestX.floatValue() - lowestX.floatValue() ) / 2,
                lowestY.floatValue() + ( highestY.floatValue() - lowestY.floatValue() ) / 2
            );
        }

/*
        public Vector<Float> checkCollision( Point2D.Float point )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //check all faces
            for ( FaceTriangle face : iFaces )
            {
                Float f = face.checkCollision( point );

                if ( f != null )
                {
                    vecZ.addElement( f );
                }
            }

            return vecZ;
        }
*/
    }
