/*  $Id: GLView.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.Texture.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author         Christopher Stock
    *   @version        0.2
    **************************************************************************************/
    public abstract class GLView
    {
        /**************************************************************************************
        *   Debug point's interface.
        *
        *   @author     Christopher Stock
        *   @version    0.2
        **************************************************************************************/
        public interface DebugPoint3D
        {
            public abstract LibVertex  getPoint();
            public abstract float   getPointSize();
            public abstract float[] getPointColor();
        }

        protected   static  final   float               DEPTH_BUFFER_SIZE       = 1.0f;

        protected   static  final   float               VIEW_ANGLE              = 45.0f;
        protected   static  final   float               VIEW_MIN                = 0.1f;
        protected   static  final   float               VIEW_MAX                = 100.0f;

        public      static          GLView              singleton               = null;

        public                      Vector<Face>        faceDrawingQueueOpaque  = null;
        public                      Vector<Face>        faceDrawingQueueMasked  = null;
        public                      Vector<Face>        faceDrawingQueueTranslucent = null;

        protected                   GLImage[]           textureImages           = null;
        protected                   boolean             lightDebugPointSet      = false;

        public void clearFaceQueues()
        {
            faceDrawingQueueOpaque      = new Vector<Face>();
            faceDrawingQueueTranslucent = new Vector<Face>();
            faceDrawingQueueMasked      = new Vector<Face>();
        }

        public void enqueueFaceToDraw( Face aFace )
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

        public void flushFaceQueue()
        {
            //enable lights
            if ( !ShooterSettings.DISABLE_LIGHTING ) enableLights();

            //draw all opaque faces
            for ( Face faceOpaque : faceDrawingQueueOpaque )
            {
                //draw & remove from faceQueue
                drawFace( faceOpaque.getVerticesToDraw(), faceOpaque.getFaceNormal(), faceOpaque.getTexture(), faceOpaque.getColor().f3 );
            }

            //sort translucent faces according to distance
            faceDrawingQueueTranslucent = Face.sortQueueAccordingToDistance( faceDrawingQueueTranslucent );

            //draw all translucent faces
            for ( Face faceTranslucent : faceDrawingQueueTranslucent )
            {
                //draw & remove from faceQueue
                drawFace( faceTranslucent.getVerticesToDraw(), faceTranslucent.getFaceNormal(), faceTranslucent.getTexture(), faceTranslucent.getColor().f3 );
            }

            //draw all masked faces
            for ( Face faceMasked : faceDrawingQueueMasked )
            {
                //draw & remove from faceQueue
                drawFace( faceMasked.getVerticesToDraw(), faceMasked.getFaceNormal(), faceMasked.getTexture(), faceMasked.getColor().f3 );
            }

            //flushing gl forces an immediate draw
            flushGL();

            //disable lights
            if ( !ShooterSettings.DISABLE_LIGHTING ) disableLights();
        }

        public abstract void drawFace( LibVertex[] verticesToDraw, LibVertex faceNormal, Texture texture, float[] col );

        public abstract void drawDebugPoint( DebugPoint3D fxp );

        public abstract void clearGl();

        public abstract void drawOrthoBitmapBytes( GLImage glImage, int x, int y );

        public abstract void setPlayerCamera( float posX, float posY, float posZ, float rotX, float rotY, float rotZ );

        protected abstract void flushGL();

        protected abstract void setOrthoOn();

        protected abstract void setOrthoOff();

        protected abstract void initLight1();

        protected abstract void disableLights();

        protected abstract void enableLights();
    }
