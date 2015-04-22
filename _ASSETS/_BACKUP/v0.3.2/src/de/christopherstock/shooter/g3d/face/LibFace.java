/*  $Id: Face.java 292 2011-02-20 22:34:00Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.face;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLView.LibGLFace;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.g3d.*;

    /**************************************************************************************
    *   Represents a face with an anchor and a various number of vertices that define the polygon.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public abstract class LibFace implements LibGLFace, LibGeomObject
    {
        private                     LibVertex           iAnchor                             = null;
        private                     LibGLTexture        iTexture                            = null;
        private                     LibColors           iColor                              = null;
        private                     LibVertex           iFaceNormal                         = null;

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
        public LibFace( LibVertex aAnchor, LibGLTexture aTexture, LibColors aColor, LibVertex aFaceNormal )
        {
            //assign initial values
            iAnchor       = aAnchor;
            iTexture     = aTexture;
            iColor       = ( aColor == null ? LibColors.EWhite : aColor );
            iFaceNormal  = aFaceNormal;
        }

        protected abstract void setCollisionValues();

        @Override
        public final void setNewAnchor( LibVertex newAnchor, boolean performTranslationOnFaces, LibTransformationMode transformationMode )
        {
            iAnchor = newAnchor;
            if ( performTranslationOnFaces )
            {
                translate( iAnchor.x, iAnchor.y, iAnchor.z, transformationMode );
            }
        }

        /***********************************************************************
        *   Translates the ORIGINAL vertices setting the TRANSFORMED vertices.
        *
        *   @param  tX  The x-modification value to translate all original vertices for.
        *   @param  tY  The y-modification value to translate all original vertices for.
        *   @param  tZ  The z-modification value to translate all original vertices for.
        ***********************************************************************/
        @Override
        public void translate( float tX, float tY, float  tZ, LibTransformationMode transformationMode )
        {
            LibVertex[] iNewTransformedVertices = new LibVertex[ iOriginalVertices.length ];

            LibVertex[] srcVertices        = null;

            //choose source
            switch ( transformationMode )
            {
                case ETransformedToTransformed:
                {
                    srcVertices = iTransformedVertices;
                    break;
                }

                case EOriginalsToOriginals:
                case EOriginalsToTransformed:
                default:
                {
                    srcVertices = iOriginalVertices;
                    break;
                }
            }

            //translate all original vertices
            for ( int i = 0; i < srcVertices.length; ++i )
            {
                //remember to copy u and v and to make a new object!
                iNewTransformedVertices[ i ] = new LibVertex
                (
                    srcVertices[ i ].x + tX,
                    srcVertices[ i ].y + tY,
                    srcVertices[ i ].z + tZ,
                    srcVertices[ i ].u,
                    srcVertices[ i ].v
                );
            }

            //choose destination
            switch ( transformationMode )
            {
                case EOriginalsToOriginals:
                {
                    iOriginalVertices    = iNewTransformedVertices;
                    iTransformedVertices = iNewTransformedVertices;
                    break;
                }

                case EOriginalsToTransformed:
                case ETransformedToTransformed:
                {
                    iTransformedVertices = iNewTransformedVertices;
                    break;
                }
            }

            //update collision values
            setCollisionValues();
        }

        /***********************************************************************
        *   Rotates the TRANSFORMED vertices setting the TRANSFORMED vertices.
        *   Rotation operations shall always be performed after translation operations.
        *
        *   @param  transMatrix     The transformation-matrix to turn all vertices around.
        *   @param  tX              The amount to translate this vertex on the x-axis.
        *   @param  tY              The amount to translate this vertex on the y-axis.
        *   @param  tZ              The amount to translate this vertex on the z-axis.
        ***********************************************************************/
        public void translateAndRotateXYZ( LibMatrix transMatrix, float tX, float tY, float tZ, LibTransformationMode transformationMode, LibVertex alternateAnchor )
        {
            //translate all original vertices
            translate( tX, tY, tZ, transformationMode );

            //rotate all transformed vertices
            transMatrix.transformVertices( iTransformedVertices, ( alternateAnchor == null ? iAnchor : alternateAnchor ) );

            //alter originals?
            if ( transformationMode == LibTransformationMode.EOriginalsToOriginals )
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

        public final void mirror( boolean x, boolean y, boolean z )
        {
            //mirror all originals
            for ( int i = 0; i < iOriginalVertices.length; ++i )
            {
                //remember to copy u and v and to make a new object!
                iOriginalVertices[ i ] = new LibVertex
                (
                    ( x ? -1 : 1 ) * iOriginalVertices[ i ].x,
                    ( y ? -1 : 1 ) * iOriginalVertices[ i ].y,
                    ( z ? -1 : 1 ) * iOriginalVertices[ i ].z,
                    iOriginalVertices[ i ].u,
                    iOriginalVertices[ i ].v
                );
            }

            //assign to transformed too !
            iTransformedVertices = iOriginalVertices;
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
        public final LibVertex[] getVerticesToDraw()
        {
            return iTransformedVertices;
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iAnchor;
        }

        public void changeTexture( LibGLTexture tex1, LibGLTexture tex2 )
        {
            if ( iTexture == tex1 )
            {
                iTexture = tex2;
            }
        }
    }
