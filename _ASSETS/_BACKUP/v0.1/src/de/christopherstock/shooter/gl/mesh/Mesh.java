/*  $Id: Mesh.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.mesh;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.Collision.*;
    import  de.christopherstock.shooter.game.collision.unit.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.g3d.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class Mesh implements HitPointCarrier
    {
        public enum MeshAction
        {
            ENone,
            EDoorSliding,
            EDoorTurning,
            ;
        }

        private     static  final   int                 DOOR_TICKS_OPEN_CLOSE           = 15;
        private     static  final   float               DOOR_SLIDING_TRANSLATION_OPEN   = 1.5f;
        private     static  final   float               DOOR_ANGLE_OPEN                 = -90f;

        private                     Vertex              anchor                          = null;
        private                     FreeTriangle[]      faces                           = null;
        private                     CollisionEnabled    collisionEnabled                = CollisionEnabled.NO;
        private                     MeshAction          meshAction                      = null;

        private                     boolean             doorOpening                     = false;
        private                     int                 doorAnimation                   = 0;

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  faces               All faces this mesh shall consist of.
        *   @param  x                   The meshes' anchor point X.
        *   @param  y                   The meshes' anchor point Y.
        *   @param  z                   The meshes' anchor point Z.
        *   @param  collisionEnabled    Specifies if the triangle-faces of this mesh shall throw collisions on firing.
        *   @param  specialObject       This mesh may have functionality.
        **************************************************************************************/
        public Mesh
        (
            FreeTriangle[]      faces,
            Vertex              aAnchor,
            CollisionEnabled    collisionEnabled,
            MeshAction          specialObject
        )
        {
            //assign
            this.faces              = faces;
            this.collisionEnabled   = collisionEnabled;
            this.meshAction         = specialObject;
            setNewAnchor( aAnchor, true );
        }

        /******************************************************************************************
        *   Sets/updates anchor.
        *
        *   @param  newAnchor                   The new anchor vertex point.
        *   @param  performTranslationOnFaces   Determines if the faces shall be translated
        *                                       by the new anchor.
        ******************************************************************************************/
        public void setNewAnchor( Vertex newAnchor, boolean performTranslationOnFaces )
        {
            //assign new anchor for this mesh and for all it's faces - translate all faces by the new anchor!
            anchor = newAnchor;
            for ( int i = 0; i < faces.length; ++i )
            {
                faces[ i ].setNewAnchor( anchor );
                if ( performTranslationOnFaces ) faces[ i ].translate( anchor.x, anchor.y, anchor.z );
                //faces[ i ].setOriginalVertices( faces[ i ].transformedVertices );
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
                face.translate( tX, tY, tZ );
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
                face.translateAndRotateXYZ( tX, tY, tZ, rotX, rotY, rotZ );
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
                GLView.drawFace( face );
            }
        }

        /**************************************************************************************
        *   Fires a shot and returns all hit-points.
        *
        *   @param  shot    The shot to be fired on this mesh.
        *   @return         The nearest hitpoint with lots of informations.
        *                   <code>null</code> if no face was hit.
        **************************************************************************************/
        public final Vector<HitPoint> launchShot( Shot shot )
        {
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //only launch shot on faces if collision is active
            if ( collisionEnabled.flag )
            {
                //fire all faces and collect all hit-points
                for ( FreeTriangle face : faces )
                {
                    HitPoint hitPoint = face.launchShot( shot );
                    if ( hitPoint != null )
                    {
                        hitPoint.connectToCarrier( this );
                        hitPoints.addElement( hitPoint );
                    }
                }
            }

            return hitPoints;
        }

        public final void launchAction( Cylinder cylinder )
        {
            //only launch an action if this mesh has an according action
            if ( meshAction == MeshAction.ENone ) return;

            //browse all faces and check if any face is affected by the action
            boolean meshIsAffected = false;
            for ( FreeTriangle face : faces )
            {
                if ( cylinder.checkAction( face ) )
                {
                    meshIsAffected = true;
                    break;
                }
            }

            //perform an action if this mesh is affected
            if ( meshIsAffected )
            {
                switch ( meshAction )
                {
                    case ENone:
                    {
                        break;
                    }

                    case EDoorSliding:
                    case EDoorTurning:
                    {
                        Debug.playerAction.out( "launch door change" );
                        doorOpening = !doorOpening;
                        break;
                    }
                }
            }
        }

        public final boolean checkCollisionFree( Cylinder cylinder )
        {
            //only check collisions  if collision is active
            if ( collisionEnabled.flag )
            {
                for ( FreeTriangle face : faces )
                {
                    if ( cylinder.checkCollisionFree( face ) == false ) return false;
                }
            }
            return true;
        }

        public final Vector<Float> launchFloorCheck( Point2D.Float point )
        {
            Vector<Float> vecZ = new Vector<Float>();

            //only check floor-change if collision is active
            if ( collisionEnabled.flag )
            {
                //check all faces
                for ( FreeTriangle face : faces )
                {
                    Float f = face.launchFloorCheck( point );

                    if ( f != null )
                    {
                        vecZ.addElement( f );
                    }
                }
            }

            return vecZ;
        }

        public final void animate()
        {
            //check if this mesh is associated with an action
            switch ( meshAction )
            {
                case EDoorSliding:
                case EDoorTurning:
                {
                    //Debug.note("door anchor is: ["+anchor.x+"]");
                    //Debug.note("door anchor is: ["+anchor.y+"]");
                    //Debug.note("door anchor is: ["+anchor.z+"]\n");

                    //check if the door is being opened or being closed
                    if ( doorOpening )
                    {
                        //open the door
                        if ( doorAnimation < DOOR_TICKS_OPEN_CLOSE )
                        {
                            //increase animation counter
                            ++doorAnimation;

                            //translate mesh
                            switch ( meshAction )
                            {
                                case ENone:
                                {
                                    //impossible to happen
                                    throw new IllegalFormatCodePointException( 0 );
                                }

                                case EDoorSliding:
                                {
                                    //translate mesh
                                    translate
                                    (
                                        anchor.x,
                                        anchor.y + doorAnimation * DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE,
                                        anchor.z
                                    );

                                    //translate mesh's bullet holes
                                    BulletHole.translateAll( this, 0.0f, DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                    break;
                                }

                                case EDoorTurning:
                                {
                                    //rotate mesh
                                    float angle = DOOR_ANGLE_OPEN * doorAnimation / DOOR_TICKS_OPEN_CLOSE;
                                    translateAndRotateXYZ
                                    (
                                        anchor.x,
                                        anchor.y,
                                        anchor.z,
                                        0.0f,
                                        0.0f,
                                        angle
                                    );

                                    //rotate mesh's bullet holes
                                    BulletHole.rotateForMesh( this, DOOR_ANGLE_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        //close the door
                        if ( doorAnimation > 0 )
                        {
                            //decrease animation counter
                            --doorAnimation;

                            //translate mesh
                            switch ( meshAction )
                            {
                                case ENone:
                                {
                                    //impossible to happen
                                    throw new IllegalFormatCodePointException( 0 );
                                }

                                case EDoorSliding:
                                {
                                    //translate mesh
                                    translate
                                    (
                                        anchor.x,
                                        anchor.y + doorAnimation * DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE,
                                        anchor.z
                                    );

                                    //translate mesh's bullet holes
                                    BulletHole.translateAll( this, 0.0f, -DOOR_SLIDING_TRANSLATION_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                    break;
                                }

                                case EDoorTurning:
                                {
                                    //rotate mesh
                                    float angle = DOOR_ANGLE_OPEN * doorAnimation / DOOR_TICKS_OPEN_CLOSE;
                                    translateAndRotateXYZ
                                    (
                                        anchor.x,
                                        anchor.y,
                                        anchor.z,
                                        0.0f,
                                        0.0f,
                                        angle
                                    );

                                    //rotate mesh's bullet holes
                                    BulletHole.rotateForMesh( this, -DOOR_ANGLE_OPEN / DOOR_TICKS_OPEN_CLOSE );
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }

                case ENone:
                {
                    //no action for this mesh
                    break;
                }
            }
        }

        public final Vertex getAnchor()
        {
            return anchor;
        }

        public final float getCarriersFaceAngle()
        {
            return 0.0f;
        }
    }
