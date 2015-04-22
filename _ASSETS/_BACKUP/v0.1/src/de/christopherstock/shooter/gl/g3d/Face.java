/*  $Id: Face.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.g3d;

    import  javax.media.opengl.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   Represents a face with an anchor and a various number of vertices that define the polygon.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public abstract class Face
    {
        protected                   Vertex          anchor                              = null;
        protected                   Texture         texture                             = null;
        protected                   Colors          color                               = null;

        protected                   Vertex[]        originalVertices                    = null;
        protected                   Vertex[]        transformedVertices                 = null;

        protected                   float           faceAngleHorz                       = 0.0f;
        protected                   float           faceAngleVert                       = 0.0f;

        /**************************************************************************************
        *   Constructs a new face.
        *
        *   @param  anchor      The anchor for this face.
        *   @param  textureID   The texture to use. May be <code>null</code>.
        *   @param  faceColor   The color for this face. May be <code>null</code>.
        **************************************************************************************/
        public Face( Vertex anchor, Texture textureID, Colors faceColor )
        {
            //assign initial values
            this.anchor  = anchor;
            this.texture = textureID;
            this.color   = ( faceColor == null ? Colors.ERed : faceColor );
        }

        public final void setNewAnchor( Vertex newAnchor )
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
            transformedVertices = new Vertex[ originalVertices.length ];

            //translate all original vertices
            for ( int i = 0; i < originalVertices.length; ++i )
            {
                //remember to copy u and v and to make a new object!
                transformedVertices[ i ] = new Vertex
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
            //translate
            translate( tX, tY, tZ );

            //rotate all original vertices
            transformedVertices = Matrix.transformVertices( transformedVertices, anchor, rotX, rotY, rotZ );
/*
            //originals may not be used yet because they are used for the translation
             *
            //prune old transformed vertices
            transformedVertices = new Vertex[ originalVertices.length ];
            //translate all original vertices
            for ( int i = 0; i < originalVertices.length; ++i )
            {
                //remember to copy u and v ! ;) ( gnarly bug :p )
                //transformedVertices = Matrix.transformVertices( originalVertices, anchor, angleX, angleY, angleZ );
                transformedVertices[ i ] = Matrix.transformVertex( new Vertex( originalVertices[ i ].x, originalVertices[ i ].y, originalVertices[ i ].z, originalVertices[ i ].u, originalVertices[ i ].v ), anchor, angleX, angleY, angleZ );
            }
*/
            //update collision values
            setCollisionValues();
        }

        protected final void setFaceAngleHorz( float faceAngleHorz )
        {
            this.faceAngleHorz = faceAngleHorz;
        }

        protected final void setFaceAngleVert( float faceAngleVert )
        {
            this.faceAngleVert = faceAngleVert;
        }

        public final void draw( GL gl )
        {
            //plain color or texture?
            if ( texture == null )
            {
                //plain
                gl.glDisable(   GL.GL_TEXTURE_2D    );          //disable texture-mapping
              //gl.glEnable(    GL.GL_LIGHTING      );          //lights on
                gl.glColor3fv(  color.f3, 0         );          //set the face's color

                //draw all vertices
                gl.glBegin(      GL.GL_POLYGON   );
                for ( Vertex currentVertex : transformedVertices )
                {
                    gl.glVertex3f( currentVertex.x, currentVertex.z, currentVertex.y );
                }
                gl.glEnd();
              //gl.glDisable(   GL.GL_LIGHTING      );          //lights off
            }
            else
            {
                //texture
                gl.glEnable(        GL.GL_TEXTURE_2D                    );      //enable texture-mapping
                gl.glBindTexture(   GL.GL_TEXTURE_2D, texture.ordinal() );      //bind face's texture

                //draw all vertices
                gl.glBegin(      GL.GL_POLYGON   );
                for ( Vertex currentVertex : transformedVertices )
                {
                    gl.glTexCoord2f( currentVertex.u, currentVertex.v );
                    gl.glVertex3f(   currentVertex.x, currentVertex.z, currentVertex.y   );
                }
                gl.glEnd();
            }
        }

        public final void setOriginalVertices( Vertex[] vertices )
        {
            originalVertices    = vertices;
            transformedVertices = vertices;
        }

        protected abstract void setCollisionValues();
    }
