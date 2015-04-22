/*  $Id: LibGLView.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.gl.LibGLTexture.Translucency;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author         Christopher Stock
    *   @version        0.3.5
    **************************************************************************************/
    public abstract class LibGLView
    {
        public interface LibGLFace
        {
            public abstract LibVertex    getAnchor();
            public abstract LibColors    getColor();
            public abstract float[]      getColor3f();
            public abstract LibVertex    getFaceNormal();
            public abstract LibGLTexture getTexture();
            public abstract LibVertex[]  getVerticesToDraw();
        }

        public enum Align3D
        {
            AXIS_X,
            AXIS_Y,
            AXIS_Z,
            ;
        }

        protected   static  final   float               DEPTH_BUFFER_SIZE           = 1.0f;

        protected   static  final   float               VIEW_ANGLE                  = 45.0f;
        protected   static  final   float               VIEW_MIN                    = 0.1f;
        protected   static  final   float               VIEW_MAX                    = 500.0f;

        public                      Vector<LibGLFace>   faceDrawingQueue            = new Vector<LibGLFace>();

        protected                   LibGLImage[]        textureImages               = null;
        protected                   boolean             lightDebugPointSet          = false;
        public                      LibDebug            iDebug                      = null;

        protected LibGLView( LibDebug aDebug )
        {
            iDebug          = aDebug;
        }

        public void clearFaceQueue()
        {
            faceDrawingQueue.clear();
        }

        public void enqueueFaceToQueue( LibGLFace aFace )
        {
            //opaque or not?
            if ( aFace.getTexture() == null || aFace.getTexture().getTranslucency() == Translucency.EOpaque )
            {
                //immediate draw for opaque faces
                drawFace( aFace.getVerticesToDraw(), aFace.getFaceNormal(), aFace.getTexture(), aFace.getColor3f() );
            }
            else //if ( aFace.getTexture().getTranslucency() == Translucency.EHasMask )
            {
                //add to face queue
                faceDrawingQueue.addElement( aFace );
            }
        }

        public void flushFaceQueue( LibVertex aCameraViewpoint )
        {
          //ShooterDebugSystem.bugfix.out( "draw faces: ["+faceDrawingQueueOpaque.size()+"] opaque ["+faceDrawingQueueMasked.size()+"] masked ["+faceDrawingQueue.size()+" translucent]" );

            //sort & draw translucent faces according to distance
            if ( faceDrawingQueue.size() > 0 )
            {
                faceDrawingQueue = sortFacesAccordingToDistance( faceDrawingQueue, aCameraViewpoint );

              //ShooterDebug.bugfix.out( "elements on face queue: [" + faceDrawingQueue.size() + "]" );

                for ( LibGLFace faceTranslucent : faceDrawingQueue )
                {
                    //draw & remove from faceQueue
                    drawFace( faceTranslucent.getVerticesToDraw(), faceTranslucent.getFaceNormal(), faceTranslucent.getTexture(), faceTranslucent.getColor3f() );
                }
            }

            //flushing gl forces an immediate draw
            flushGL();
        }

        public static final Vector<LibGLFace> sortFacesAccordingToDistance( Vector<LibGLFace> faces, LibVertex cameraViewPoint )
        {
            //debug.out( "sort face queue - [" + faces.size() + "] faces" );

            //fill hashmap with all faces and their distances to the player
            Hashtable<Float,Vector<LibGLFace>> distances = new Hashtable<Float,Vector<LibGLFace>>();
            for ( LibGLFace face : faces )
            {
                Float distance = null;
/*
                //this is inoperative because the face normals are not points but directions!
                if ( face.getFaceNormal() != null ) distance = new Float( LibMathGeometry.getDistanceXY( face.getFaceNormal(), cameraViewPoint ) );
*/
                {
                    distance = new Float( LibMathGeometry.getDistanceXY( face.getAnchor(), cameraViewPoint ) );
                }

                if ( distances.get( distance ) == null )
                {
                    Vector<LibGLFace> vf = new Vector<LibGLFace>();
                    vf.addElement( face );
                    distances.put( distance, vf );
                }
                else
                {
                    Vector<LibGLFace> vf = distances.get( distance );
                    vf.addElement( face );
                    distances.put( distance, vf );
                }
            }

            //sort by distances
            Float[] dists = distances.keySet().toArray( new Float[] {} );
            //debug.out( "different distances: [" + dists.length + "]" );
            Arrays.sort( dists );

            //browse all distances reversed ( ordered by FAREST till LOWEST )
            Vector<LibGLFace> ret = new Vector<LibGLFace>();
            for ( int i = dists.length - 1; i >= 0; --i )
            {
                //debug.out( "distance: [" + dists[ i ] + "]" );

                Vector<LibGLFace> vf2 = distances.get( dists[ i ] );
                ret.addAll( vf2 );
            }

            //debug.out( "return elements: [" + ret.size() + "]" );

            return ret;
        }

        public final void drawOrthoBitmapBytes( LibGLImage glImage, int x, int y )
        {
            drawOrthoBitmapBytes( glImage, x, y, 1.0f );
        }

        public abstract void drawOrthoBitmapBytes( LibGLImage glImage, int x, int y, float alphaF );

        public abstract void drawFace( LibVertex[] verticesToDraw, LibVertex faceNormal, LibGLTexture texture, float[] col );

        public abstract void clearGl( LibColors clearCol );

        protected abstract void flushGL();

        public abstract void setCamera( ViewSet viewSet );

        protected abstract void setOrthoOn();

        protected abstract void setOrthoOff();

        protected abstract void initLight1();

        public abstract void setLightsOff();

        public abstract void setLightsOn();

        public abstract void initTextures( LibGLImage[] texImages );

        protected abstract int getSrcPixelFormat( SrcPixelFormat spf );
    }
