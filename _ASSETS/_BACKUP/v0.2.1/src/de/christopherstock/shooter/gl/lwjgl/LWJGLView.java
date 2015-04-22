/*  $Id: GLView.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.lwjgl;

    import  java.awt.*;
    import  org.lwjgl.*;
    import  org.lwjgl.opengl.*;
    import  org.lwjgl.opengl.DisplayMode;
    import  org.lwjgl.util.glu.*;

import de.christopherstock.lib.g3d.*;
import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.*;
import  de.christopherstock.shooter.ui.*;

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
                Display.setParent( (Canvas)GLPanel.glPanel.getNativePanel() );

                //create the display
                Display.create();

                //request focus
                GLPanel.glPanel.getNativePanel().requestFocus();
            }
            catch ( LWJGLException e)
            {
                e.printStackTrace();
            }

            Debug.bugfix.info( "invoked init-method of LWJGLView" );

            //assign the panel's dimensions and parse its offsets
            GLPanel.glPanel.width  = Display.getParent().getWidth();
            GLPanel.glPanel.height = Display.getParent().getHeight();

            Debug.bugfix.out( "Assigned display dimensions [" + GLPanel.glPanel.width + "]x[" + GLPanel.glPanel.height + "]" );

            //Display.

            // run through some based OpenGL capability settings. Textures
            // enabled, back face culling enabled, depth testing is on,

            // effect ?
            // GL11.glEnable(GL11.GL_CULL_FACE);

            //prepare
            GL11.glClearColor(    0.0f, 0.0f, 0.0f, 0.0f                          );  //clear the bg color to black
            GL11.glShadeModel(    GL11.GL_SMOOTH                                    );  //smooth Shading ( GL_FLAT: flat shading )
            GL11.glClearDepth(    DEPTH_BUFFER_SIZE                               );  //set depth-buffer's size
            GL11.glEnable(        GL11.GL_DEPTH_TEST                                );  //enable depth-sorting [ jams the scene :-( ]
            GL11.glDepthFunc(     GL11.GL_LEQUAL                                    );  //less or equal depth-testing! GL.GL_LESS caused problems in combination with blending!
            GL11.glHint(          GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST );  //really nice perspective-calculations
            GL11.glDisable(       GL11.GL_LIGHTING                                  );  //disable lighting
            GL11.glEnable(        GL11.GL_TEXTURE_2D                                );  //enable textures
            GL11.glMatrixMode(    GL11.GL_PROJECTION                                );  //switch to projection-matrix-mode
            GL11.glLoadIdentity();

            //set perspective
            GLU.gluPerspective( VIEW_ANGLE, ( (float)ShooterSettings.FORM_WIDTH / (float)ShooterSettings.FORM_HEIGHT ), VIEW_MIN, VIEW_MAX );

            //enter matrix mode modelview :)
            GL11.glMatrixMode( GL11.GL_MODELVIEW );

            //init all textures
            initTextures();

            //GLPanel.glPanel.requestFocus();

            // clear GL ?
            GL11.glClear( GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
/*
            //set up lighting ( test )
            GL11.glLightfv( GL11.GL_LIGHT1, GL11.GL_AMBIENT,    new float[] { 0.5f, 0.5f, 0.5f, 1.0f }, 0 );
            GL11.glLightfv( GL11.GL_LIGHT1, GL11.GL_DIFFUSE,    new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, 0 );
            GL11.glLightfv( GL11.GL_LIGHT1, GL11.GL_POSITION,   new float[] { 0.0f, 0.0f, 2.0f, 1.0f }, 0 );
            GL11.glEnable(  GL11.GL_LIGHT1);
*/
            //parse hud's offsets
//            division by zero !
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
            GL11.glVertex3f( point.x - pointSize, point.z, point.y - pointSize );
            GL11.glVertex3f( point.x - pointSize, point.z, point.y + pointSize );
            GL11.glVertex3f( point.x + pointSize, point.z, point.y + pointSize );
            GL11.glVertex3f( point.x + pointSize, point.z, point.y - pointSize );
            GL11.glEnd();

            GL11.glBegin( GL11.GL_POLYGON );
            GL11.glVertex3f( point.x, point.z - pointSize, point.y - pointSize );
            GL11.glVertex3f( point.x, point.z - pointSize, point.y + pointSize );
            GL11.glVertex3f( point.x, point.z + pointSize, point.y + pointSize );
            GL11.glVertex3f( point.x, point.z + pointSize, point.y - pointSize );
            GL11.glEnd();

            GL11.glBegin( GL11.GL_POLYGON );
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
        public final void drawFace( LibVertex[] verticesToDraw, GLTexture texture, float[] col )
        {
            //plain color or texture?
            if ( texture == null )
            {
                //plain
                GL11.glDisable(   GL11.GL_TEXTURE_2D    );          //disable texture-mapping
              //gl.glEnable(    GL.GL_LIGHTING      );              //lights on
                GL11.glColor3f( col[ 0 ], col[ 1 ], col[ 2 ] );     //set col

                //draw all vertices
                GL11.glBegin(      GL11.GL_POLYGON   );
                for ( LibVertex currentVertex : verticesToDraw )
                {
                    GL11.glVertex3f(   currentVertex.x, currentVertex.z, currentVertex.y   );
                }
                GL11.glEnd();
              //gl.glDisable(   GL.GL_LIGHTING      );          //lights off
            }

            //playing with glass / blending
            else if ( texture == GLTexture.EGlass1 )
            {
                // Full Brightness, 50% Alpha ( NEW )
                // Blending Function For Translucency Based On Source Alpha Value ( NEW )
                GL11.glEnable(     GL11.GL_BLEND           );
                GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA  );
                GL11.glColor4f( 0.5f, 0.5f, 0.5f, 0.25f );
                GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE );

                //texture
                GL11.glEnable(        GL11.GL_TEXTURE_2D                        );      //enable texture-mapping
                GL11.glBindTexture(   GL11.GL_TEXTURE_2D, GLTexture.EGlass1.ordinal()   );      //bind face's texture

                //draw all vertices
                GL11.glBegin(      GL11.GL_POLYGON   );
                for ( LibVertex currentVertex : verticesToDraw )
                {
                    drawTexturedVertex( currentVertex );
                }
                GL11.glEnd();

                //disable blending
                GL11.glDisable(      GL11.GL_BLEND           );
            }

            //draw textured face
            else
            {
                //texture
                GL11.glEnable(        GL11.GL_TEXTURE_2D                        );      //enable texture-mapping
                GL11.glBindTexture(   GL11.GL_TEXTURE_2D, texture.ordinal()     );      //bind face's texture

                //draw all vertices
                GL11.glBegin(      GL11.GL_POLYGON   );
                for ( LibVertex currentVertex : verticesToDraw )
                {
                    drawTexturedVertex( currentVertex );
                }
                GL11.glEnd();
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
                GL11.glTexParameteri(   GL11.GL_TEXTURE_2D,   GL11.GL_TEXTURE_MIN_FILTER,   GL11.GL_LINEAR    );
                GL11.glTexParameteri(   GL11.GL_TEXTURE_2D,   GL11.GL_TEXTURE_MAG_FILTER,   GL11.GL_LINEAR    );
                GL11.glTexEnvf(         GL11.GL_TEXTURE_ENV,  GL11.GL_TEXTURE_ENV_MODE,     GL11.GL_REPLACE   );
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
            GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR );
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
        public void drawOrthoBitmapBytes( GLImage glImage, int x, int y )
        {
            //prepare rendering 2D
            setOrthoOn();

            //blending prunes transparent pixels
            GL11.glEnable(      GL11.GL_BLEND           );
            GL11.glBlendFunc(   GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA  );

            //be sure to disable texturing - bytes will not be drawn otherwise
            GL11.glDisable(        GL11.GL_TEXTURE_2D                        );

            //set and draw pixels
            GL11.glRasterPos2f( 0, 0 );
            GL11.glBitmap( 0, 0, 0, 0, x, y, glImage.bytes );  //workaround to allow negative coordinates
            GL11.glDrawPixels( glImage.width, glImage.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, glImage.bytes );
            GL11.glFlush();

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
    }
