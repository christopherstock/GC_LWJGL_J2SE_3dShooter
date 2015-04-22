/*  $Id: LWJGLView.java 208 2011-01-21 17:04:29Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl.lwjgl;

    import  java.awt.*;
    import  java.nio.*;
    import  org.lwjgl.*;
    import  org.lwjgl.opengl.*;
    import  org.lwjgl.opengl.DisplayMode;
    import  org.lwjgl.util.glu.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author         Christopher Stock
    *   @version        0.3.2
    **************************************************************************************/
    public class LibLWJGLView extends LibGLView
    {
        public LibLWJGLView( GLCallbackInit aCallbackGLinit, LibGLPanel panel, LibGLImage[] aTextureImages, LibDebug aDebug, int aFormWidth, int aFormHeight, boolean aEnableLights )
        {
            super( aCallbackGLinit, aDebug, aEnableLights );

            textureImages          = aTextureImages;

            try
            {
                // find out what the current bits per pixel of the desktop is
                int currentBpp = Display.getDisplayMode().getBitsPerPixel();

                // find a display mode at 800x600
                DisplayMode[] dms           = Display.getAvailableDisplayModes();
                DisplayMode   displayMode   = null;

                for ( DisplayMode dm : dms )
                {
                    //check if this display mode fits
                    if
                    (
                            aFormWidth  == dm.getWidth()
                        &&  aFormHeight == dm.getHeight()
                        &&  currentBpp  == dm.getBitsPerPixel()
                    )
                    {
                        iDebug.out( "picked display mode [" + dm.getWidth() + "][" + dm.getHeight() + "][" + dm.getBitsPerPixel() + "]" );
                        displayMode = dm;
                        break;
                    }
                }

                // if can't find a mode, notify the user the give up
                if ( displayMode == null )
                {
                    iDebug.err( "Display mode not available!" );
                    return;
                }

                // configure and create the LWJGL display
                Display.setDisplayMode( displayMode );
                Display.setFullscreen(  false       );

                //set native canvas as parent displayable
                Display.setParent( (Canvas)panel.getNativePanel() );

                //create the display
                Display.create();

                //request focus
                panel.getNativePanel().requestFocus();
            }
            catch ( LWJGLException e)
            {
                e.printStackTrace();
            }

            iDebug.out( "invoked init-method of LWJGLView" );

            //assign the panel's dimensions and parse its offsets
            panel.width  = Display.getParent().getWidth();
            panel.height = Display.getParent().getHeight();

            iDebug.out( "assigned panel dimensions [" + LibGL3D.panel.width + "]x[" + LibGL3D.panel.height + "]" );

            //run through some based OpenGL capability settings

            // effect ?
            // GL11.glEnable(GL11.GL_CULL_FACE);

            //prepare
            GL11.glClearColor(    0.0f, 0.0f, 0.0f, 0.0f                                );  //clear the bg color to black
            GL11.glShadeModel(    GL11.GL_SMOOTH                                        );  //smooth Shading ( GL_FLAT: flat shading )
            GL11.glClearDepth(    DEPTH_BUFFER_SIZE                                     );  //set depth-buffer's size
            GL11.glEnable(        GL11.GL_DEPTH_TEST                                    );  //enable depth-sorting [ jams the scene :-( ]
            GL11.glDepthFunc(     GL11.GL_LEQUAL                                        );  //less or equal depth-testing! GL.GL_LESS caused problems in combination with blending!
            GL11.glHint(          GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST   );  //really nice perspective-calculations
            GL11.glDisable(       GL11.GL_LIGHTING                                      );  //disable lighting
            GL11.glEnable(        GL11.GL_TEXTURE_2D                                    );  //enable textures
            GL11.glMatrixMode(    GL11.GL_PROJECTION                                    );  //switch to projection-matrix-mode
            //GL11.glLoadIdentity();

            //set perspective
            GLU.gluPerspective( VIEW_ANGLE, ( (float)LibGL3D.panel.width / (float)LibGL3D.panel.height ), VIEW_MIN, VIEW_MAX );

            //enter matrix mode modelview :)
            GL11.glMatrixMode( GL11.GL_MODELVIEW );

            //init all textures
            initTextures();
            LibGL3D.panel.getNativePanel().requestFocus();

            //complete
            iCallbackGLinit.onCompletedViewInits();

            //gl fully inited
            LibGL3D.glPanelInitialized = true;
        }

        @Override
        public final void drawDebugPoint( DebugPoint3D fxPoint )
        {
            LibVertex  point      = fxPoint.getPoint();
            float   pointSize  = fxPoint.getPointSize();
            float[] pointColor = fxPoint.getPointColor();

            //disable texture-mapping and set the points' color
            GL11.glDisable(    GL11.GL_TEXTURE_2D    );
            GL11.glColor3f( pointColor[ 0 ], pointColor[ 1 ], pointColor[ 2 ] );

            GL11.glBegin( GL11.GL_POLYGON );
            GL11.glNormal3f( 1.0f, 0.0f, 0.0f );
            GL11.glVertex3f( point.x - pointSize, point.z, point.y - pointSize );
            GL11.glVertex3f( point.x - pointSize, point.z, point.y + pointSize );
            GL11.glVertex3f( point.x + pointSize, point.z, point.y + pointSize );
            GL11.glVertex3f( point.x + pointSize, point.z, point.y - pointSize );
            GL11.glEnd();

            GL11.glBegin( GL11.GL_POLYGON );
            GL11.glNormal3f( 1.0f, 0.0f, 0.0f );
            GL11.glVertex3f( point.x, point.z - pointSize, point.y - pointSize );
            GL11.glVertex3f( point.x, point.z - pointSize, point.y + pointSize );
            GL11.glVertex3f( point.x, point.z + pointSize, point.y + pointSize );
            GL11.glVertex3f( point.x, point.z + pointSize, point.y - pointSize );
            GL11.glEnd();

            GL11.glBegin( GL11.GL_POLYGON );
            GL11.glNormal3f( 1.0f, 0.0f, 0.0f );
            GL11.glVertex3f( point.x - pointSize, point.z - pointSize, point.y );
            GL11.glVertex3f( point.x + pointSize, point.z - pointSize, point.y );
            GL11.glVertex3f( point.x + pointSize, point.z + pointSize, point.y );
            GL11.glVertex3f( point.x - pointSize, point.z + pointSize, point.y );
            GL11.glEnd();
        }

        @Override
        public void setPlayerCamera( float posX, float posY, float posZ, float rotX, float rotY, float rotZ )
        {
            GL11.glLoadIdentity();                                              //new identity please
            GL11.glNormal3f(    0.0f,           0.0f,   0.0f            );      //normalize

            GL11.glRotatef(     rotY,           0.0f,   0.0f,   1.0f    );      //rotate z (!)
            GL11.glRotatef(     rotX,           1.0f,   0.0f,   0.0f    );      //rotate x
            GL11.glRotatef(     360.0f - rotZ,  0.0f,   1.0f,   0.0f    );      //rotate y (!)

            GL11.glTranslatef(  posX,           posZ,   posY            );      //translate x z y
        }

        @Override
        public final void drawFace( LibVertex[] verticesToDraw, LibVertex faceNormal, LibGLTexture texture, float[] col )
        {
            //draw plain color if texture is missing
            if ( texture == null )
            {
                //draw plain without texture
                GL11.glDisable(   GL11.GL_TEXTURE_2D    );

                //set face color
                GL11.glColor3f( col[ 0 ], col[ 1 ], col[ 2 ] );

                //draw all vertices
                GL11.glBegin(      GL11.GL_POLYGON   );

                //set face normal
                if ( faceNormal != null ) GL11.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                //draw all vertices
                for ( LibVertex currentVertex : verticesToDraw )
                {
                    drawVertex( currentVertex );
                }
                GL11.glEnd();
            }

            //texture
            else
            {
                switch ( texture.getTranslucency() )
                {
                    case EGlass:
                    {
                        GL11.glEnable(      GL11.GL_BLEND                                   );
                      //GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA  );
                        GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE                  );

                        //texture
                        GL11.glEnable(      GL11.GL_TEXTURE_2D                              );      //enable texture-mapping
                        GL11.glBindTexture( GL11.GL_TEXTURE_2D, texture.getId()             );      //bind face's texture

                        //set glass color
                        GL11.glColor4f(     0.5f, 0.5f, 0.5f, 0.5f                          );

                        //draw all vertices
                        GL11.glBegin(       GL11.GL_POLYGON                                 );

                        //set face normal
                        if ( faceNormal != null ) GL11.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                        //draw all vertices
                        for ( LibVertex currentVertex : verticesToDraw )
                        {
                            drawTexturedVertex( currentVertex );
                        }
                        GL11.glEnd();

                        //disable blending
                        GL11.glDisable(     GL11.GL_BLEND                                   );
                        break;
                    }

                    case EHasMask:
                    {
                        //enable texture-mapping
                        GL11.glEnable(      GL11.GL_TEXTURE_2D                              );

                        //set face color
                        GL11.glColor3f( col[ 0 ], col[ 1 ], col[ 2 ] );

                        //blend for mask
                        GL11.glEnable(      GL11.GL_BLEND                                   );
                        GL11.glBlendFunc( GL11.GL_DST_COLOR, GL11.GL_ZERO );

//                      GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA  );
//                      GL11.glColor4f(     0.5f, 0.5f, 0.5f, 0.25f                         );

                        //draw mask
                        GL11.glBindTexture( GL11.GL_TEXTURE_2D, texture.getMaskId()         );
                        GL11.glBegin(       GL11.GL_POLYGON                                 );

                        //set face normal
                        if ( faceNormal != null ) GL11.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                        //draw all vertices
                        for ( LibVertex currentVertex : verticesToDraw )
                        {
                            drawTexturedVertex( currentVertex );
                        }
                        GL11.glEnd();

                        //blend for texture
                        GL11.glBlendFunc( GL11.GL_ONE, GL11.GL_ONE );

                        //draw texture
                        GL11.glBindTexture( GL11.GL_TEXTURE_2D, texture.getId()             );
                        GL11.glBegin(       GL11.GL_POLYGON                                 );

                        //set face normal
                        if ( faceNormal != null ) GL11.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                        //draw all vertices
                        for ( LibVertex currentVertex : verticesToDraw )
                        {
                            drawTexturedVertex( currentVertex );
                        }
                        GL11.glEnd();

                        //disable blending
                        GL11.glDisable(         GL11.GL_BLEND                                   );

                        break;
                    }

                    case EOpaque:
                    {
                        //texture
                        GL11.glEnable(          GL11.GL_TEXTURE_2D                        );      //enable texture-mapping
                        GL11.glBindTexture(     GL11.GL_TEXTURE_2D, texture.getId()       );      //bind face's texture

                        //set face color
                        GL11.glColor3f( col[ 0 ], col[ 1 ], col[ 2 ] );

                        //draw all vertices
                        GL11.glBegin(         GL11.GL_POLYGON   );

                        //set face normal
                        if ( faceNormal != null ) GL11.glNormal3f( faceNormal.x, faceNormal.y, faceNormal.z );

                        //draw all vertices
                        for ( LibVertex currentVertex : verticesToDraw )
                        {
                            drawTexturedVertex( currentVertex );
                        }
                        GL11.glEnd();

                        break;
                    }
                }
            }
        }

        public final void drawVertex( LibVertex v )
        {
            GL11.glVertex3f( v.x, v.z, v.y   );
        }

        public final void drawTexturedVertex( LibVertex v )
        {
            GL11.glTexCoord2f( v.u, v.v );
            GL11.glVertex3f(   v.x, v.z, v.y   );
        }

        public final void initTextures()
        {
            GL11.glGenTextures();

            for ( int i = 0; i < textureImages.length; ++i )
            {
                GL11.glBindTexture( GL11.GL_TEXTURE_2D, i );
                makeRGBTexture( textureImages[ i ] );

                //this line disabled the lights on textures ! do NOT uncomment it !!
                //GL11.glTexEnvf(         GL11.GL_TEXTURE_ENV,  GL11.GL_TEXTURE_ENV_MODE,     GL11.GL_REPLACE   );
            }
        }

        /**************************************************************************************
        *   information about the data rewinding can be found at
        *   http://www.experts-exchange.com/Programming/Languages/Java/Q_22397090.html?sfQueryTermInfo=1+jogl
        **************************************************************************************/
        private final void makeRGBTexture( LibGLImage img )
        {
            //bind texture to gl
            GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR ); // GL_NEAREST is also possible ..
            GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR );
            GL11.glTexImage2D(    GL11.GL_TEXTURE_2D, 0, getSrcPixelFormat( img.srcPixelFormat ), img.width, img.height, 0, getSrcPixelFormat( img.srcPixelFormat ), GL11.GL_UNSIGNED_BYTE, img.bytes );
        }

        @Override
        public void clearGl()
        {
            //clear the gl
            GL11.glClear( GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
        }

        @Override
        public void flushGL()
        {
            //force all drawing
            GL11.glFlush();
        }

        @Override
        public void drawOrthoBitmapBytes( LibGLImage glImage, int x, int y, float alpha )
        {
            //prepare rendering 2D
            setOrthoOn();

            //be sure to disable texturing - bytes will not be drawn otherwise
            GL11.glDisable( GL11.GL_TEXTURE_2D );

            //blending allows transparent pixels
            GL11.glEnable(  GL11.GL_BLEND );

            //must have :( use higher quality alpha pixel pruning if full alpha
            if ( alpha == 1.0f )
            {
                //blend transparent alpha values
                GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
            }
            else
            {
                //disable full alpha pixels - this is the solution we have been looking for a long time :)
                GL11.glEnable( GL11.GL_ALPHA_TEST );
                GL11.glAlphaFunc( GL11.GL_GREATER, 0 );

                //blend images translucent [ why can we mix GL 1.4 and GL 1.1 here and it works ?? ]
                GL11.glBlendFunc(   GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA );
                GL14.glBlendColor( 1.0f, 1.0f, 1.0f, alpha );
            }

            //set and draw pixels - this is a workaround to allow negative coordinates
            GL11.glRasterPos2f( 0, 0 );
            GL11.glBitmap( 0, 0, 0, 0, x, y, glImage.bytes );  //
            GL11.glDrawPixels( glImage.width, glImage.height, getSrcPixelFormat( glImage.srcPixelFormat ), GL11.GL_UNSIGNED_BYTE, glImage.bytes );

            //disable blending
            GL11.glDisable( GL11.GL_BLEND );

            //restore previous perspective and model views
            setOrthoOff();
        }

        @Override
        public void setOrthoOn()
        {
            // prepare to render in 2D
            GL11.glDisable( GL11.GL_DEPTH_TEST );                                       // so 2D stuff stays on top of 3D scene
            GL11.glMatrixMode( GL11.GL_PROJECTION );
            GL11.glPushMatrix();                                                        // preserve perspective view
            GL11.glLoadIdentity();                                                      // clear the perspective matrix
            GL11.glOrtho( 0, LibGL3D.panel.width, 0, LibGL3D.panel.height, -1, 1 );     // turn on 2D
            GL11.glMatrixMode( GL11.GL_MODELVIEW );
            GL11.glPushMatrix();                                                        // Preserve the Modelview Matrix
            GL11.glLoadIdentity();                                                      // clear the Modelview Matrix
        }

        @Override
        public void setOrthoOff()
        {
            // restore the original positions and views
            GL11.glMatrixMode( GL11.GL_PROJECTION );
            GL11.glPopMatrix();
            GL11.glMatrixMode( GL11.GL_MODELVIEW );
            GL11.glPopMatrix();
            GL11.glEnable( GL11.GL_DEPTH_TEST );            // turn Depth Testing back on
        }

        @Override
        protected void enableLights()
        {
            GL11.glEnable( GL11.GL_LIGHTING    );
            GL11.glEnable( GL11.GL_LIGHT1   );

            //float[] lightAmbient    = { 0.3f, 0.3f, 0.3f, 1.0f };
            //ByteBuffer temp  = ByteBuffer.allocateDirect( 16 );
            //temp.order(  ByteOrder.nativeOrder() );
            //GL11.glLightModel(  GL11.GL_LIGHT_MODEL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put( lightAmbient ).flip() );

            //set material
            float[] materialColor   = { 1.0f, 1.0f, 1.0f, 1.0f };
            ByteBuffer temp5 = ByteBuffer.allocateDirect( 16 );
            temp5.order( ByteOrder.nativeOrder() );

            //smooth shading
            GL11.glShadeModel(  GL11.GL_SMOOTH      );

            //init light 1
            initLight1();

            GL11.glEnable( GL11.GL_COLOR_MATERIAL );
            GL11.glMaterial( GL11.GL_BACK,  GL11.GL_AMBIENT_AND_DIFFUSE, (FloatBuffer)temp5.asFloatBuffer().put( materialColor ).flip() );
        }

        @Override
        protected void disableLights()
        {
            //disable lights
            GL11.glDisable( GL11.GL_LIGHT1          );
            GL11.glDisable( GL11.GL_LIGHTING        );
            GL11.glDisable( GL11.GL_COLOR_MATERIAL  );
        }

        @Override
        protected void initLight1()
        {
            //has to be invoked each tick ! y       x       z
            float[] lightPosition   = {     1.0f,   1.0f,   1.0f,   1.0f };

            if ( !lightDebugPointSet )
            {
                lightDebugPointSet = true;
                LibGLDebugPoint.add( LibColors.EYellow, lightPosition[ 1 ], lightPosition[ 0 ], lightPosition[ 2 ], 100000 );
            }

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
            GL11.glLight(  GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer)temp3.asFloatBuffer().put( lightPosition ).flip() );
            GL11.glLight(  GL11.GL_LIGHT1, GL11.GL_AMBIENT,  (FloatBuffer)temp.asFloatBuffer().put(  lightAmbient  ).flip() );
            GL11.glLight(  GL11.GL_LIGHT1, GL11.GL_DIFFUSE,  (FloatBuffer)temp2.asFloatBuffer().put( lightDiffuse  ).flip() );
            GL11.glLight(  GL11.GL_LIGHT1, GL11.GL_SPECULAR, (FloatBuffer)temp4.asFloatBuffer().put( lightSpecular ).flip() );
        }

        @Override
        protected int getSrcPixelFormat( SrcPixelFormat spf )
        {
            switch ( spf )
            {
                case ERGB:      return GL11.GL_RGB;
                case ERGBA:     return GL11.GL_RGBA;
            }

            return 0;
        }
/*
        public static void drawTestSprite()
        {
            //GLView.singleton.setOrthoOn();

            LibGLImage glImage = Wearpon.wearponImages[ Wearpon.EAssaultRifle.ordinal() ];
            int x = 0;
            int y = 0;

            //Debug.bugfix.out( "w: " + glImage.width );
            //Debug.bugfix.out( "h: " + glImage.height );
            //Debug.bugfix.out( "b: " + glImage.bytes.capacity() );

            GL11.glRasterPos2f( 0, 0 );
            GL11.glDisable(        GL11.GL_TEXTURE_2D                        );

            GL11.glDisable( GL11.GL_DEPTH_TEST );           // so 2D stuff stays on top of 3D scene
            GL11.glMatrixMode( GL11.GL_PROJECTION );
            GL11.glMatrixMode( GL11.GL_MODELVIEW );

            GL11.glBitmap( 0, 0, 0, 0, x, y, glImage.bytes );  //workaround to allow negative coordinates
            GL11.glDrawPixels( glImage.width, glImage.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, glImage.bytes );

            //GLView.singleton.setOrthoOff();

            GL11.glEnable( GL11.GL_DEPTH_TEST );           // so 2D stuff stays on top of 3D scene
        }
*/
    }
