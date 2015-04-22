/*  $Id: GLView.java 199 2011-01-08 12:27:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.gl.LibGLTexture.Translucency;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author         Christopher Stock
    *   @version        0.3
    **************************************************************************************/
    public abstract class LibGLView
    {
        public static interface GLCallbackInit
        {
            public abstract void onCompletedViewInits();
        }

        public interface GLFace
        {
            public abstract LibVertex getAnchor();
            public abstract float[] getColor3f();
            public abstract LibVertex getFaceNormal();
            public abstract LibGLTexture getTexture();
            public abstract LibVertex[] getVerticesToDraw();
        }

        /**************************************************************************************
        *   Debug point's interface.
        *
        *   @author     Christopher Stock
        *   @version    0.3
        **************************************************************************************/
        public interface DebugPoint3D
        {
            public abstract LibVertex  getPoint();
            public abstract float   getPointSize();
            public abstract float[] getPointColor();
        }

        protected   static  final   float               DEPTH_BUFFER_SIZE           = 1.0f;

        protected   static  final   float               VIEW_ANGLE                  = 45.0f;
        protected   static  final   float               VIEW_MIN                    = 0.1f;
        protected   static  final   float               VIEW_MAX                    = 100.0f;

        public                      Vector<GLFace>      faceDrawingQueueOpaque      = null;
        public                      Vector<GLFace>      faceDrawingQueueMasked      = null;
        public                      Vector<GLFace>      faceDrawingQueueTranslucent = null;

        protected                   LibGLImage[]        textureImages               = null;
        protected                   boolean             lightDebugPointSet          = false;
        public                      LibDebug            iDebug                      = null;

        public                      int                 iFormWidth                  = 0;
        public                      int                 iFormHeight                 = 0;

        protected                   GLCallbackInit      iCallbackGLinit             = null;
        protected                   boolean             iEnableLights               = false;

        protected LibGLView( GLCallbackInit aCallbackGLinit, LibDebug aDebug, int aFormWidth, int aFormHeight, boolean aEnableLights )
        {
            iDebug          = aDebug;
            iFormWidth      = aFormWidth;
            iFormHeight     = aFormHeight;
            iCallbackGLinit = aCallbackGLinit;
            iEnableLights   = aEnableLights;
        }

        public void clearFaceQueues()
        {
            faceDrawingQueueOpaque      = new Vector<GLFace>();
            faceDrawingQueueTranslucent = new Vector<GLFace>();
            faceDrawingQueueMasked      = new Vector<GLFace>();
        }

        public void enqueueFaceToQueue( GLFace aFace )
        {
            //opaque or not?
            if ( aFace.getTexture() == null || aFace.getTexture().getTranslucency() == Translucency.EOpaque )
            {
                faceDrawingQueueOpaque.addElement( aFace );
            }
            else if ( aFace.getTexture().getTranslucency() == Translucency.EHasMask )
            {
                faceDrawingQueueMasked.addElement( aFace );
            }
            else
            {
                faceDrawingQueueTranslucent.addElement( aFace );
            }
        }

        public void flushFaceQueue( LibVertex aCameraViewpoint )
        {
            //enable lights
            if ( iEnableLights ) enableLights();

            //draw all opaque faces
            for ( GLFace faceOpaque : faceDrawingQueueOpaque )
            {
                //draw & remove from faceQueue
                drawFace( faceOpaque.getVerticesToDraw(), faceOpaque.getFaceNormal(), faceOpaque.getTexture(), faceOpaque.getColor3f() );
            }

            //sort translucent faces according to distance
            faceDrawingQueueTranslucent = sortFacesAccordingToDistance( iDebug, faceDrawingQueueTranslucent, aCameraViewpoint );

            //draw all translucent faces
            for ( GLFace faceTranslucent : faceDrawingQueueTranslucent )
            {
                //draw & remove from faceQueue
                drawFace( faceTranslucent.getVerticesToDraw(), faceTranslucent.getFaceNormal(), faceTranslucent.getTexture(), faceTranslucent.getColor3f() );
            }

            //draw all masked faces
            for ( GLFace faceMasked : faceDrawingQueueMasked )
            {
                //draw & remove from faceQueue
                drawFace( faceMasked.getVerticesToDraw(), faceMasked.getFaceNormal(), faceMasked.getTexture(), faceMasked.getColor3f() );
            }

            //flushing gl forces an immediate draw
            flushGL();

            //disable lights
            if ( iEnableLights ) disableLights();
        }

        public static final Vector<GLFace> sortFacesAccordingToDistance( LibDebug debug, Vector<GLFace> faces, LibVertex cameraViewPoint )
        {
            debug.out( "sort face queue - [" + faces.size() + "] faces" );

            //fill hashmap with all faces and their distances to the player
            Hashtable<Float,Vector<GLFace>> distances = new Hashtable<Float,Vector<GLFace>>();
            for ( GLFace face : faces )
            {
                Float distance = new Float( LibLine3D.getDistanceXY( face.getAnchor(), cameraViewPoint ) );
              //Float distance = new Float( LibLine3D.getDistanceXY( face.getFaceNormal(), cameraViewPoint ) );

                if ( distances.get( distance ) == null )
                {
                    Vector<GLFace> vf = new Vector<GLFace>();
                    vf.addElement( face );
                    distances.put( distance, vf );
                }
                else
                {
                    Vector<GLFace> vf = distances.get( distance );
                    vf.addElement( face );
                    distances.put( distance, vf );
                }
            }

            //sort by distances
            Float[] dists = distances.keySet().toArray( new Float[] {} );
            debug.out( "different distances: [" + dists.length + "]" );
            Arrays.sort( dists );

            //browse all distances reversed ( ordered by FAREST till LOWEST )
            Vector<GLFace> ret = new Vector<GLFace>();
            for ( int i = dists.length - 1; i >= 0; --i )
            {
                debug.out( "distance: [" + dists[ i ] + "]" );

                Vector<GLFace> vf2 = distances.get( dists[ i ] );
                ret.addAll( vf2 );
            }

            debug.out( "return elements: [" + ret.size() + "]" );

            return ret;
        }


        public abstract void drawFace( LibVertex[] verticesToDraw, LibVertex faceNormal, LibGLTexture texture, float[] col );

        public abstract void drawDebugPoint( DebugPoint3D fxp );

        public abstract void clearGl();

        protected abstract void flushGL();

        public final void drawOrthoBitmapBytes( LibGLImage glImage, int x, int y )
        {
            drawOrthoBitmapBytes( glImage, x, y, 255 );
        }

        public abstract void drawOrthoBitmapBytes( LibGLImage glImage, int x, int y, int alpha );

        public abstract void setPlayerCamera( float posX, float posY, float posZ, float rotX, float rotY, float rotZ );

        protected abstract void setOrthoOn();

        protected abstract void setOrthoOff();

        protected abstract void initLight1();

        protected abstract void disableLights();

        protected abstract void enableLights();

        protected abstract int getSrcPixelFormat( SrcPixelFormat spf );
    }
