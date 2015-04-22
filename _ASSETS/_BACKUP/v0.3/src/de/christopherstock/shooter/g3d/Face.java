/*  $Id: Face.java 247 2011-02-02 01:04:55Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLView.GLFace;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   Represents a face with an anchor and a various number of vertices that define the polygon.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public abstract class Face implements GLFace
    {
        protected                   LibVertex           iAnchor                             = null;
        protected                   LibGLTexture        iTexture                            = null;
        public                      LibColors           iColor                              = null;

        protected                   LibVertex           iFaceNormal                         = null;

        protected                   LibVertex[]         iOriginalVertices                   = null;
        protected                   LibVertex[]         iTransformedVertices                = null;

        protected                   float               iFaceAngleHorz                      = 0.0f;
        protected                   float               iFaceAngleVert                      = 0.0f;

        /**************************************************************************************
        *   Constructs a new face.
        *
        *   @param  aAnchor      The anchor for this face.
        *   @param  aTexture   The texture to use. May be <code>null</code>.
        *   @param  aColor   The color for this face. May be <code>null</code>.
        **************************************************************************************/
        public Face( LibVertex aAnchor, LibGLTexture aTexture, LibColors aColor, LibVertex aFaceNormal )
        {
            //assign initial values
            iAnchor      = aAnchor;
            iTexture     = aTexture;
            iColor       = ( aColor == null ? LibColors.EWhite : aColor );
            iFaceNormal  = aFaceNormal;
        }

        protected abstract void setCollisionValues();

        public final void setNewAnchor( LibVertex newAnchor )
        {
            iAnchor = newAnchor;
        }

        /***********************************************************************
        *   Translates the ORIGINAL vertices setting the TRANSFORMED vertices.
        *
        *   @param  tX  The x-modification value to translate all original vertices for.
        *   @param  tY  The y-modification value to translate all original vertices for.
        *   @param  tZ  The z-modification value to translate all original vertices for.
        ***********************************************************************/
        public void translate( float tX, float tY, float  tZ, boolean performOnOriginals )
        {
            //prune old transformed vertices
            iTransformedVertices = new LibVertex[ iOriginalVertices.length ];

            //translate all original vertices
            for ( int i = 0; i < iOriginalVertices.length; ++i )
            {
                //remember to copy u and v and to make a new object!
                iTransformedVertices[ i ] = new LibVertex
                (
                    iOriginalVertices[ i ].x + tX,
                    iOriginalVertices[ i ].y + tY,
                    iOriginalVertices[ i ].z + tZ,
                    iOriginalVertices[ i ].u,
                    iOriginalVertices[ i ].v
                );
            }

            //alter originals?
            if ( performOnOriginals )
            {
                iOriginalVertices = iTransformedVertices;
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
        public void translateAndRotateXYZ( float tX, float tY, float tZ, float rotX, float rotY, float rotZ, boolean performOnOriginals )
        {
            //translate all original vertices
            translate( tX, tY, tZ, performOnOriginals );

            //rotate all transformed vertices
            iTransformedVertices = LibMatrix.transformVertices( iTransformedVertices, iAnchor, rotX, rotY, rotZ );

            //alter originals?
            if ( performOnOriginals )
            {
                iOriginalVertices = iTransformedVertices;
            }

            //update collision values
            setCollisionValues();
        }

        /***********************************************************************
        *   Translates the ORIGINAL vertices setting the TRANSFORMED vertices.
        *
        *   @param  scaleFactor     1.0 performs scalation to equal.
        ***********************************************************************/
        public void scale( float scaleFactor, boolean performOnOriginals )
        {
            //prune old transformed vertices
            iTransformedVertices = new LibVertex[ iOriginalVertices.length ];

            //translate all original vertices
            for ( int i = 0; i < iOriginalVertices.length; ++i )
            {
                //remember to copy u and v and to make a new object!
                iTransformedVertices[ i ] = new LibVertex
                (
                    iOriginalVertices[ i ].x * scaleFactor,
                    iOriginalVertices[ i ].y * scaleFactor,
                    iOriginalVertices[ i ].z * scaleFactor,
                    iOriginalVertices[ i ].u,
                    iOriginalVertices[ i ].v
                );
            }

            //alter originals?
            if ( performOnOriginals )
            {
                iOriginalVertices = iTransformedVertices;
            }

            //update collision values
            setCollisionValues();
        }

        protected final void setFaceAngleHorz( float aFaceAngleHorz )
        {
            iFaceAngleHorz = aFaceAngleHorz;
        }

        protected final void setFaceAngleVert( float aFaceAngleVert )
        {
            iFaceAngleVert = aFaceAngleVert;
        }

        public final void setOriginalVertices( LibVertex[] vertices )
        {
            iOriginalVertices    = vertices;
            iTransformedVertices = vertices;
        }

        public final LibColors getColor()
        {
            return iColor;
        }

        public final void draw()
        {
            LibGL3D.view.enqueueFaceToQueue( this );
        }

        @Override
        public final LibVertex getFaceNormal()
        {
            return iFaceNormal;
        }

        @Override
        public final LibGLTexture getTexture()
        {
            return iTexture;
        }

        @Override
        public final float[] getColor3f()
        {
            return iColor.f3;
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iAnchor;
        }

        @Override
        public final LibVertex[] getVerticesToDraw()
        {
            return iTransformedVertices;
        }
    }
