/*  $Id: LibJOGLView.java 637 2011-04-24 00:15:03Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl.jogl;

    import  java.nio.*;
    import  javax.media.opengl.*;
    import  javax.media.opengl.glu.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.SrcPixelFormat;
    import  de.christopherstock.lib.gl.LibGLPanel.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class LibJOGLView extends LibGLView implements GLEventListener
    {
        private                     GL                  gl                      = null;
        private                     GLU                 glu                     = null;
        private                     GLDrawCallback      drawCallback            = null;

        public LibJOGLView( GLDrawCallback aDrawCallback, LibDebug aDebug, boolean aEnableLights )
        {
            super( aDebug, aEnableLights );

            drawCallback    = aDrawCallback;
        }

        @Override
        public void init( GLAutoDrawable glDrawable )
        {
            iDebug.out( "invoked init-method of GLView" );

            //assign the panel's dimensions and parse its offsets
            LibGL3D.panel.width  = LibGL3D.panel.getNativePanel().getSize().width;
            LibGL3D.panel.height = LibGL3D.panel.getNativePanel().getSize().height;

            //create gl
            gl  = glDrawable.getGL();
            glu = new GLU();    //glu.createGLU( gl );  //old version

            gl.glClearColor(    0.0f, 0.0f, 0.0f, 0.0f                          );  //clear the bg color to black
            gl.glShadeModel(    GL.GL_SMOOTH                                    );  //smooth Shading ( GL_FLAT: flat shading )
            gl.glClearDepth(    DEPTH_BUFFER_SIZE                               );  //set depth-buffer's size
            gl.glEnable(        GL.GL_DEPTH_TEST                                );  //enable depth-sorting [ jams the scene :-( ]
            gl.glDepthFunc(     GL.GL_LEQUAL                                    );  //less or equal depth-testing! GL.GL_LESS caused problems in combination with blending!
            gl.glHint(          GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST );  //really nice perspective-calculations
            gl.glDisable(       GL.GL_LIGHTING                                  );  //disable lighting
            gl.glEnable(        GL.GL_TEXTURE_2D                                );  //enable textures
            gl.glMatrixMode(    GL.GL_PROJECTION                                );  //switch to projection-matrix-mode
            //gl.glLoadIdentity();

            //set perspective
            glu.gluPerspective( VIEW_ANGLE, ( (float)LibGL3D.panel.width / (float)LibGL3D.panel.height ), VIEW_MIN, VIEW_MAX );

            //enter matrix mode modelview :)
            gl.glMatrixMode( GL.GL_MODELVIEW );

            //init all textures
            LibGL3D.panel.getNativePanel().requestFocus();

            //complete
            //iCallbackGLinit.onCompletedViewInits();

            //gl fully inited
            LibGL3D.glPanelInitialized = true;
        }

        @Override
        public void display( GLAutoDrawable glDrawable )
        {
            //only if a level is assigned
            if ( drawCallback.readyToDisplay() )
            {
                //invoke callback drawable
                drawCallback.draw3D();
                drawCallback.draw2D();
            }
        }

        @Override
        public void reshape( GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4 )
        {
        }

        @Override
        public void displayChanged( GLAutoDrawable arg0, boolean arg1, boolean     arg2 )
        {
        }

        @Override
        public void setPlayerCamera( float posX, float posY, float posZ, float rotX, float rotY, float rotZ )
        {
            gl.glLoadIdentity();                                                //new identity please
            gl.glNormal3f(      0.0f,           0.0f,   0.0f            );      //normalize

            gl.glRotatef(       rotY,           0.0f,   0.0f,   1.0f    );      //rotate z (!)
            gl.glRotatef(       rotX,           1.0f,   0.0f,   0.0f    );      //rotate x
            gl.glRotatef(       360.0f - rotZ,  0.0f,   1.0f,   0.0f    );      //rotate y (!)

            gl.glTranslatef(    posX,           posZ,   posY            );      //translate x z y
        }

        @Override
        public final void drawFace( LibVertex[] verticesToDraw, LibVertex faceNormal, LibGLTexture texture, float[] col )
        {
            //draw plain color if texture is missing
            if ( texture == null )
            {
                //draw plain without texture
                gl.glDisable(   GL.GL_TEXTURE_2D    );

                //set face color
                gl.glColor3fv(  col, 0              );

                //draw all vertices
                gl.glBegin(      GL.GL_POLYGON   );

                //set face normal
                if ( faceNormal != null ) gl.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                //draw all vertices
                for ( LibVertex currentVertex : verticesToDraw )
                {
                    drawVertex( currentVertex );
                }
                gl.glEnd();
            }

            //texture
            else
            {
                switch ( texture.getTranslucency() )
                {
                    case EGlass:
                    {
                        gl.glEnable(      GL.GL_BLEND                                   );
                      //gl.glBlendFunc(   GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA    );
                        gl.glBlendFunc(   GL.GL_SRC_ALPHA, GL.GL_ONE                    );

                        //texture
                        gl.glEnable(      GL.GL_TEXTURE_2D                              );      //enable texture-mapping
                        gl.glBindTexture( GL.GL_TEXTURE_2D, texture.getId()             );      //bind face's texture

                        //set glass color
                        gl.glColor4f(     0.5f, 0.5f, 0.5f, 0.25f                       );

                        //draw all vertices
                        gl.glBegin(       GL.GL_POLYGON                                 );

                        //set face normal
                        if ( faceNormal != null ) gl.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                        //draw all vertices
                        for ( LibVertex currentVertex : verticesToDraw )
                        {
                            drawTexturedVertex( currentVertex );
                        }
                        gl.glEnd();

                        //disable blending
                        gl.glDisable(     GL.GL_BLEND                                   );
                        break;
                    }

                    case EHasMask:
                    {
                        //enable texture-mapping
                        gl.glEnable(      GL.GL_TEXTURE_2D                              );

                        //set face color
                        gl.glColor3f( col[ 0 ], col[ 1 ], col[ 2 ] );

                        //blend for mask
                        gl.glEnable(      GL.GL_BLEND                                   );
                        gl.glBlendFunc( GL.GL_DST_COLOR, GL.GL_ZERO );

//                      gl.glBlendFunc(   gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA  );
//                      gl.glColor4f(     0.5f, 0.5f, 0.5f, 0.25f                         );

                        //draw mask
                        gl.glBindTexture( GL.GL_TEXTURE_2D, texture.getMaskId()         );
                        gl.glBegin(       GL.GL_POLYGON                                 );

                        //set face normal
                        if ( faceNormal != null ) gl.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                        //draw all vertices
                        for ( LibVertex currentVertex : verticesToDraw )
                        {
                            drawTexturedVertex( currentVertex );
                        }
                        gl.glEnd();

                        //blend for texture
                        gl.glBlendFunc( GL.GL_ONE, GL.GL_ONE );

                        //draw texture
                        gl.glBindTexture( GL.GL_TEXTURE_2D, texture.getId()             );
                        gl.glBegin(       GL.GL_POLYGON                                 );

                        //set face normal
                        if ( faceNormal != null ) gl.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                        //draw all vertices
                        for ( LibVertex currentVertex : verticesToDraw )
                        {
                            drawTexturedVertex( currentVertex );
                        }
                        gl.glEnd();

                        //disable blending
                        gl.glDisable(         GL.GL_BLEND                                   );

                        break;
                    }

                    case EOpaque:
                    {
                        //texture
                        gl.glEnable(          GL.GL_TEXTURE_2D                        );      //enable texture-mapping
                        gl.glBindTexture(     GL.GL_TEXTURE_2D, texture.getId()       );      //bind face's texture

                        //set face color
                        gl.glColor3f( col[ 0 ], col[ 1 ], col[ 2 ] );

                        //draw all vertices
                        gl.glBegin(         GL.GL_POLYGON   );

                        //set face normal
                        if ( faceNormal != null ) gl.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                        //draw all vertices
                        for ( LibVertex currentVertex : verticesToDraw )
                        {
                            drawTexturedVertex( currentVertex );
                        }
                        gl.glEnd();

                        break;
                    }
                }
            }
        }

        public final void drawVertex( LibVertex v )
        {
            gl.glVertex3f(   v.x, v.z, v.y   );
        }

        public final void drawTexturedVertex( LibVertex v )
        {
            gl.glTexCoord2f( v.u, v.v );
            gl.glVertex3f(   v.x, v.z, v.y   );
        }

        @Override
        public final void initTextures( LibGLImage[] texImages )
        {
            textureImages = texImages;
            gl.glGenTextures( textureImages.length, IntBuffer.allocate( textureImages.length ) );

            for ( int i = 0; i < textureImages.length; ++i )
            {
                gl.glBindTexture(GL.GL_TEXTURE_2D, i );
                makeRGBTexture( textureImages[ i ] );

                //this line disabled the lights on textures ! do NOT uncomment it !!
                //gl.glTexEnvf(         GL.GL_TEXTURE_ENV,  GL.GL_TEXTURE_ENV_MODE,     GL.GL_REPLACE   );
            }
        }

        /**************************************************************************************
        *   information about the data rewinding can be found at
        *   http://www.experts-exchange.com/Programming/Languages/Java/Q_22397090.html?sfQueryTermInfo=1+jogl
        **************************************************************************************/
        private final void makeRGBTexture( LibGLImage img )
        {
            //bind texture to gl
            gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );
            gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR );
            gl.glTexImage2D(    GL.GL_TEXTURE_2D, 0, getSrcPixelFormat( img.srcPixelFormat ), img.width, img.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.bytes );
        }

        @Override
        public void clearGl()
        {
            //clear the gl and reset modelview
            gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
        }

        @Override
        public void flushGL()
        {
            //force all drawing
            gl.glFlush();
        }

        @Override
        public void drawOrthoBitmapBytes( LibGLImage glImage, int x, int y, float alpha )
        {
            //prepare rendering 2D
            setOrthoOn();

            //be sure to disable texturing - bytes will not be drawn otherwise
            gl.glDisable(        GL.GL_TEXTURE_2D                        );

            //blending prunes transparent pixels
            gl.glEnable(      GL.GL_BLEND           );

            if ( alpha == 1.0f )
            {
                //filter complete alpha channel for pngs' transparency
                gl.glBlendFunc(   GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA  );
            }
            else
            {
                //blend opaque images translucent
                gl.glBlendColor( 1.0f, 1.0f, 1.0f, alpha );
                gl.glBlendFunc(   GL.GL_CONSTANT_ALPHA, GL.GL_ONE_MINUS_CONSTANT_ALPHA );
            }

            //set and draw pixels
            gl.glRasterPos2f( 0, 0 );
            gl.glBitmap( 0, 0, 0, 0, x, y, glImage.bytes );  //workaround to allow negative coordinates
            gl.glDrawPixels( glImage.width, glImage.height, getSrcPixelFormat( glImage.srcPixelFormat ), GL.GL_UNSIGNED_BYTE, glImage.bytes );

            //disable blending
            gl.glDisable(      GL.GL_BLEND           );

            //restore previous perspective and model views
            setOrthoOff();
        }

        @Override
        public void setOrthoOn()
        {
            // prepare to render in 2D
            gl.glDisable( GL.GL_DEPTH_TEST );                                       // so 2D stuff stays on top of 3D scene
            gl.glMatrixMode( GL.GL_PROJECTION );
            gl.glPushMatrix();                                                      // preserve perspective view
            gl.glLoadIdentity();                                                    // clear the perspective matrix
            gl.glOrtho( 0, LibGL3D.panel.width, 0, LibGL3D.panel.height, -1, 1 );   // turn on 2D
            gl.glMatrixMode( GL.GL_MODELVIEW );
            gl.glPushMatrix();                                                      // Preserve the Modelview Matrix
            gl.glLoadIdentity();                                                    // clear the Modelview Matrix
        }

        @Override
        public void setOrthoOff()
        {
            // restore the original positions and views
            gl.glMatrixMode( GL.GL_PROJECTION );
            gl.glPopMatrix();
            gl.glMatrixMode( GL.GL_MODELVIEW );
            gl.glPopMatrix();
            gl.glEnable( GL.GL_DEPTH_TEST );      // turn Depth Testing back on
        }

        @Override
        protected void enableLights()
        {
            gl.glEnable( GL.GL_LIGHTING    );
            gl.glEnable( GL.GL_LIGHT1   );

            //float[] lightAmbient    = { 0.3f, 0.3f, 0.3f, 1.0f };
            //ByteBuffer temp  = ByteBuffer.allocateDirect( 16 );
            //temp.order(  ByteOrder.nativeOrder() );
            //gl.glLightModel(  gl.GL_LIGHT_MODEL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put( lightAmbient ).flip() );

            //set material
            float[] materialColor   = { 1.0f, 1.0f, 1.0f, 1.0f };
            ByteBuffer temp5 = ByteBuffer.allocateDirect( 16 );
            temp5.order( ByteOrder.nativeOrder() );

            //smooth shading
            gl.glShadeModel(  GL.GL_SMOOTH      );

            //init light 1
            initLight1();

            gl.glEnable( GL.GL_COLOR_MATERIAL );
            gl.glMaterialfv( GL.GL_BACK,  GL.GL_AMBIENT_AND_DIFFUSE, (FloatBuffer)temp5.asFloatBuffer().put( materialColor ).flip() );
        }

        @Override
        protected void disableLights()
        {
            //disable lights
            gl.glDisable( GL.GL_LIGHT1          );
            gl.glDisable( GL.GL_LIGHTING        );
            gl.glDisable( GL.GL_COLOR_MATERIAL  );
        }

        @Override
        protected void initLight1()
        {
            //has to be invoked each tick ! y       x       z
            float[] lightPosition   = {     1.0f,   1.0f,   1.0f,   1.0f };
/*
            if ( !lightDebugPointSet )
            {
                lightDebugPointSet = true;
                FX.launchDebugPoint( new LibVertex( lightPosition[ 1 ], lightPosition[ 0 ], lightPosition[ 2 ] ), LibColors.EYellow );
            }
*/
            float[] lightAmbient    = { 0.3f, 0.3f, 0.3f, 1.0f };
            float[] lightDiffuse    = { 1.0f, 1.0f, 1.0f, 1.0f };
            float[] lightSpecular   = { 1.0f, 1.0f, 1.0f, 1.0f };

            ByteBuffer temp  = ByteBuffer.allocateDirect( 16 );
            ByteBuffer temp2 = ByteBuffer.allocateDirect( 16 );
            ByteBuffer temp3 = ByteBuffer.allocateDirect( 16 );
            ByteBuffer temp4 = ByteBuffer.allocateDirect( 16 );

            temp.order(  ByteOrder.nativeOrder() );
            temp2.order( ByteOrder.nativeOrder() );
            temp3.order( ByteOrder.nativeOrder() );
            temp4.order( ByteOrder.nativeOrder() );

            //set up light 1
            gl.glLightfv(  GL.GL_LIGHT1, GL.GL_POSITION, (FloatBuffer)temp3.asFloatBuffer().put( lightPosition ).flip() );
            gl.glLightfv(  GL.GL_LIGHT1, GL.GL_AMBIENT,  (FloatBuffer)temp.asFloatBuffer().put(  lightAmbient  ).flip() );
            gl.glLightfv(  GL.GL_LIGHT1, GL.GL_DIFFUSE,  (FloatBuffer)temp2.asFloatBuffer().put( lightDiffuse  ).flip() );
            gl.glLightfv(  GL.GL_LIGHT1, GL.GL_SPECULAR, (FloatBuffer)temp4.asFloatBuffer().put( lightSpecular ).flip() );
        }

        @Override
        protected int getSrcPixelFormat( SrcPixelFormat spf )
        {
            switch ( spf )
            {
                case ERGB:      return GL.GL_RGB;
                case ERGBA:     return GL.GL_RGBA;
            }

            return 0;
        }
    }
