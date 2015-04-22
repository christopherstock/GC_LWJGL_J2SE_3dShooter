/*  $Id: LWJGLView.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.lwjgl;

    import  java.awt.*;
    import  java.nio.*;
    import  org.lwjgl.*;
    import  org.lwjgl.opengl.*;
    import  org.lwjgl.opengl.DisplayMode;
    import  org.lwjgl.util.glu.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.ui.*;
import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author         Christopher Stock
    *   @version        0.2
    **************************************************************************************/
    public class LWJGLView extends GLView
    {
        public LWJGLView( GLImage[] aTextureImages )
        {
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
                            ShooterSettings.FORM_WIDTH  == dm.getWidth()
                        &&  ShooterSettings.FORM_HEIGHT == dm.getHeight()
                        &&  currentBpp  == dm.getBitsPerPixel()
                    )
                    {
                        Debug.lwjgl.out( "picked display mode [" + dm.getWidth() + "][" + dm.getHeight() + "][" + dm.getBitsPerPixel() + "]" );
                        displayMode = dm;
                        break;
                    }
                }

                // if can't find a mode, notify the user the give up
                if ( displayMode == null )
                {
                    Debug.bugfix.err( "Display mode not available!" );
                    return;
                }

                // configure and create the LWJGL display
                Display.setDisplayMode( displayMode );
                Display.setFullscreen(  false       );

                //set native canvas as parent displayable
                Display.setParent( (Canvas)GLPanel.singleton.getNativePanel() );

                //create the display
                Display.create();

                //request focus
                GLPanel.singleton.getNativePanel().requestFocus();
            }
            catch ( LWJGLException e)
            {
                e.printStackTrace();
            }

            Debug.bugfix.info( "invoked init-method of LWJGLView" );

            //assign the panel's dimensions and parse its offsets
            GLPanel.singleton.width  = Display.getParent().getWidth();
            GLPanel.singleton.height = Display.getParent().getHeight();

            Debug.bugfix.out( "Assigned display dimensions [" + GLPanel.singleton.width + "]x[" + GLPanel.singleton.height + "]" );

            //run through some based OpenGL capability settings

            // effect ?
            // GL11.glEnable(GL11.GL_CULL_FACE);

            //prepare
            GL11.glClearColor(    0.0f, 0.0f, 0.0f, 0.0f                                );  //clear the bg color to black
            GL11.glClearDepth(    DEPTH_BUFFER_SIZE                                     );  //set depth-buffer's size
            GL11.glShadeModel(    GL11.GL_SMOOTH                                        );  //smooth Shading ( GL_FLAT: flat shading )
            GL11.glEnable(        GL11.GL_DEPTH_TEST                                    );  //enable depth-sorting [ jams the scene :-( ]
            GL11.glDepthFunc(     GL11.GL_LEQUAL                                        );  //less or equal depth-testing! GL.GL_LESS caused problems in combination with blending!
            GL11.glHint(          GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST   );  //really nice perspective-calculations
            GL11.glDisable(       GL11.GL_LIGHTING                                      );  //disable lighting
            GL11.glEnable(        GL11.GL_TEXTURE_2D                                    );  //enable textures
            GL11.glMatrixMode(    GL11.GL_PROJECTION                                    );  //switch to projection-matrix-mode
            GL11.glLoadIdentity();

            //set perspective
            GLU.gluPerspective( VIEW_ANGLE, ( (float)ShooterSettings.FORM_WIDTH / (float)ShooterSettings.FORM_HEIGHT ), VIEW_MIN, VIEW_MAX );

            //enter matrix mode modelview :)
            GL11.glMatrixMode( GL11.GL_MODELVIEW );

            //init all textures
            initTextures();

            //parse hud's offsets
            Offset.parseOffsets();

            //gl fully inited
            GL3D.glPanelInitialized = true;
            Debug.bugfix.info( "glPanel fully initialized" );
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
        public final void drawFace( LibVertex[] verticesToDraw, LibVertex faceNormal, Texture texture, float[] col )
        {
            //plain color //TODO replace by enum!
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
                    GL11.glVertex3f(   currentVertex.x, currentVertex.z, currentVertex.y   );
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
                        GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA  );
                        GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE                  );

                        //texture
                        GL11.glEnable(      GL11.GL_TEXTURE_2D                              );      //enable texture-mapping
                        GL11.glBindTexture( GL11.GL_TEXTURE_2D, texture.ordinal()           );      //bind face's texture

                        //set face color
//                        GL11.glColor3f( col[ 0 ], col[ 1 ], col[ 2 ] );

                        //set glass color
                        GL11.glColor4f(     0.5f, 0.5f, 0.5f, 0.25f                         );

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
                        GL11.glBindTexture( GL11.GL_TEXTURE_2D, texture.getMask().ordinal() );
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
                        GL11.glBindTexture( GL11.GL_TEXTURE_2D, texture.ordinal() );
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

                    case EOpaque:
                    {
                        //texture
                        GL11.glEnable(        GL11.GL_TEXTURE_2D                        );      //enable texture-mapping
                        GL11.glBindTexture(   GL11.GL_TEXTURE_2D, texture.ordinal()     );      //bind face's texture

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

        public final void drawTexturedVertex( LibVertex v )
        {
            GL11.glTexCoord2f( v.u, v.v );
            GL11.glVertex3f(   v.x, v.z, v.y   );
        }

        public final void initTextures()
        {
            GL11.glGenTextures();
            //GL11.glGenTextures( images.length, IntBuffer.allocate( images.length ) );

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
        *   {@link http://www.experts-exchange.com/Programming/Languages/Java/Q_22397090.html?sfQueryTermInfo=1+jogl}
        *
        *   @author     Christopher Stock
        *   @version    0.2
        **************************************************************************************/
        private final void makeRGBTexture( GLImage img )
        {
            //bind texture to gl
            GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR ); // GL_NEAREST is also possible ..
            GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR );
            GL11.glTexImage2D(    GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, img.width, img.height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, img.bytes );
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
        public void drawOrthoBitmapBytes( GLImage glImage, int x, int y )
        {
            //prepare rendering 2D
            setOrthoOn();

            //blending allows transparent pixels
            GL11.glEnable(      GL11.GL_BLEND           );
            GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA  );

            //be sure to disable texturing - bytes will not be drawn otherwise
            GL11.glDisable(        GL11.GL_TEXTURE_2D                        );

            //set and draw pixels
            GL11.glRasterPos2f( 0, 0 );
            GL11.glBitmap( 0, 0, 0, 0, x, y, glImage.bytes );  //workaround to allow negative coordinates
            GL11.glDrawPixels( glImage.width, glImage.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, glImage.bytes );

            //disable blending
            GL11.glDisable(      GL11.GL_BLEND           );

            //restore previous perspective and model views
            setOrthoOff();
        }

        @Override
        public void setOrthoOn()
        {
            // prepare to render in 2D
            GL11.glDisable( GL11.GL_DEPTH_TEST );           // so 2D stuff stays on top of 3D scene
            GL11.glMatrixMode( GL11.GL_PROJECTION );
            GL11.glPushMatrix();                            // preserve perspective view
            GL11.glLoadIdentity();                          // clear the perspective matrix
            GL11.glOrtho( 0, ShooterSettings.FORM_WIDTH, 0, ShooterSettings.FORM_HEIGHT, -1, 1 );  // turn on 2D
            GL11.glMatrixMode( GL11.GL_MODELVIEW );
            GL11.glPushMatrix();                            // Preserve the Modelview Matrix
            GL11.glLoadIdentity();                          // clear the Modelview Matrix
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

        public static void drawTestSprite()
        {
            //GLView.singleton.setOrthoOn();

            GLImage glImage = Wearpon.wearponImages[ Wearpon.EAssaultRifle.ordinal() ];
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
                DebugPoint.add( LibColors.EYellow, lightPosition[ 1 ], lightPosition[ 0 ], lightPosition[ 2 ], 100000 );
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
    }
