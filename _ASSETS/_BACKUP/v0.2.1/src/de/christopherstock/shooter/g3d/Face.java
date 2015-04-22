/*  $Id: Face.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import de.christopherstock.lib.g3d.*;
import de.christopherstock.lib.math.*;
import  de.christopherstock.shooter.gl.*;
import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   Represents a face with an anchor and a various number of vertices that define the polygon.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public abstract class Face
    {
        protected                   LibVertex            anchor                              = null;
        protected                   GLTexture           texture                             = null;
        public                      Colors              color                               = null;

        protected                   LibVertex[]          originalVertices                    = null;
        protected                   LibVertex[]          transformedVertices                 = null;

        protected                   float               faceAngleHorz                       = 0.0f;
        protected                   float               faceAngleVert                       = 0.0f;

        /**************************************************************************************
        *   Constructs a new face.
        *
        *   @param  aAnchor      The anchor for this face.
        *   @param  aTexture   The texture to use. May be <code>null</code>.
        *   @param  aColor   The color for this face. May be <code>null</code>.
        **************************************************************************************/
        public Face( LibVertex aAnchor, GLTexture aTexture, Colors aColor )
        {
            //assign initial values
            anchor  = aAnchor;
            texture = aTexture;
            color   = ( aColor == null ? Colors.ERed : aColor );
        }

        protected abstract void setCollisionValues();

        public final void setNewAnchor( LibVertex newAnchor )
        {
            anchor = newAnchor;
        }

        /***********************************************************************
        *   Translates the ORIGINAL vertices setting the TRANSFORMED vertices.
        *
        *   @param  tX  The x-modification value to translate all original vertices for.
        *   @param  tY  The y-modification value to translate all original vertices for.
        *   @param  tZ  The z-modification value to translate all original vertices for.
        ***********************************************************************/
        public void translate( float tX, float tY, float  tZ )
        {
            //prune old transformed vertices
            transformedVertices = new LibVertex[ originalVertices.length ];

            //translate all original vertices
            for ( int i = 0; i < originalVertices.length; ++i )
            {
                //remember to copy u and v and to make a new object!
                transformedVertices[ i ] = new LibVertex
                (
                    originalVertices[ i ].x + tX,
                    originalVertices[ i ].y + tY,
                    originalVertices[ i ].z + tZ,
                    originalVertices[ i ].u,
                    originalVertices[ i ].v
                );
            }

            //update collision values
            setCollisionValues();
        }

        /***********************************************************************
        *   Rotates the TRANSFORMED vertices setting the TRANSFORMED vertices.
        *   Rotation operations shall always be performed after translation operations.
        *
        *   @param  tX      The amount to translate this vertex on the x-axis.
        *   @param  tY      The amount to translate this vertex on the y-axis.
        *   @param  tZ      The amount to translate this vertex on the z-axis.
        *   @param  rotX    The x-axis-angle to turn all vertices around.
        *   @param  rotY    The y-axis-angle to turn all vertices around.
        *   @param  rotZ    The z-axis-angle to turn all vertices around.
        ***********************************************************************/
        public void translateAndRotateXYZ( float tX, float tY, float tZ, float rotX, float rotY, float rotZ )
        {
            //translate all original vertices
            translate( tX, tY, tZ );

            //rotate all transformed vertices
            transformedVertices = LibMatrix.transformVertices( transformedVertices, anchor, rotX, rotY, rotZ );

            //update collision values
            setCollisionValues();
        }

        protected final void setFaceAngleHorz( float aFaceAngleHorz )
        {
            faceAngleHorz = aFaceAngleHorz;
        }

        protected final void setFaceAngleVert( float aFaceAngleVert )
        {
            faceAngleVert = aFaceAngleVert;
        }

        public final void setOriginalVertices( LibVertex[] vertices )
        {
            originalVertices    = vertices;
            transformedVertices = vertices;
        }

        public final LibVertex[] getVerticesToDraw()
        {
            return transformedVertices;
        }

        public final void draw()
        {
            GLView.singleton.drawFace( transformedVertices, texture, color.f3 );
        }
    }
