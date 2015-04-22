/*  $Id: GLView.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.jogl;

    import  java.nio.*;
    import  javax.media.opengl.*;
    import  javax.media.opengl.glu.*;

import de.christopherstock.lib.g3d.*;
import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.*;
import de.christopherstock.shooter.gl.GLPanel.GLCallback2D;
import de.christopherstock.shooter.gl.GLPanel.GLCallback3D;
import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class JOGLView extends GLView implements GLEventListener
    {
        private                     GL                  gl                      = null;
        private                     GLU                 glu                     = null;

        public                      GLCallback2D          callback2D              = null;
        public                      GLCallback3D          callback3D              = null;

        public JOGLView( GLCallback2D aCallback2D, GLCallback3D aCallback3D, GLImage[] aTextureImages )
        {
            callback2D      = aCallback2D;
            callback3D      = aCallback3D;
            textureImages   = aTextureImages;
        }

        @Override
        public void init( GLAutoDrawable glDrawable )
        {
            Debug.bugfix.info( "invoked init-method of GLView" );

            //assign the panel's dimensions and parse its offsets
            GLPanel.glPanel.width  = GLPanel.glPanel.getNativePanel().getSize().width;
            GLPanel.glPanel.height = GLPanel.glPanel.getNativePanel().getSize().height;

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
            gl.glLoadIdentity();

            //set perspective
            glu.gluPerspective( VIEW_ANGLE, ( (float)ShooterSettings.FORM_WIDTH / (float)ShooterSettings.FORM_HEIGHT ), VIEW_MIN, VIEW_MAX );

            //enter matrix mode modelview :)
            gl.glMatrixMode( GL.GL_MODELVIEW );

            //init all textures
            initTextures();
            GLPanel.glPanel.getNativePanel().requestFocus();

            //set up lighting ( test )
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_AMBIENT,    new float[] { 0.5f, 0.5f, 0.5f, 1.0f }, 0 );
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_DIFFUSE,    new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, 0 );
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_POSITION,   new float[] { 0.0f, 0.0f, 2.0f, 1.0f }, 0 );
            gl.glEnable(  GL.GL_LIGHT1);

            //parse hud's offsets
            Offset.parseOffsets();

            //gl fully inited
            GL3D.glPanelInitialized = true;
            Debug.bugfix.info( "glPanel fully initialized" );
        }

        @Override
        public void display( GLAutoDrawable glDrawable )
        {
            //invoke callback drawable
            callback3D.draw3D();
            callback2D.draw2D();
        }

        @Override
        public void clearGl()
        {
            //clear the gl and reset modelview
            gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
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
        public final void drawDebugPoint( GLView.DebugPoint3D fxPoint )
        {
            LibVertex  point      = fxPoint.getPoint();
            float   pointSize  = fxPoint.getPointSize();
            float[] pointColor = fxPoint.getPointColor();

            //disable texture-mapping and set the points' color
            gl.glDisable(    GL.GL_TEXTURE_2D    );
            gl.glColor3fv( pointColor, 0 );

            gl.glBegin( GL.GL_POLYGON );
            gl.glVertex3f( point.x - pointSize, point.z, point.y - pointSize );
            gl.glVertex3f( point.x - pointSize, point.z, point.y + pointSize );
            gl.glVertex3f( point.x + pointSize, point.z, point.y + pointSize );
            gl.glVertex3f( point.x + pointSize, point.z, point.y - pointSize );
            gl.glEnd();

            gl.glBegin( GL.GL_POLYGON );
            gl.glVertex3f( point.x, point.z - pointSize, point.y - pointSize );
            gl.glVertex3f( point.x, point.z - pointSize, point.y + pointSize );
            gl.glVertex3f( point.x, point.z + pointSize, point.y + pointSize );
            gl.glVertex3f( point.x, point.z + pointSize, point.y - pointSize );
            gl.glEnd();

            gl.glBegin( GL.GL_POLYGON );
            gl.glVertex3f( point.x - pointSize, point.z - pointSize, point.y );
            gl.glVertex3f( point.x + pointSize, point.z - pointSize, point.y );
            gl.glVertex3f( point.x + pointSize, point.z + pointSize, point.y );
            gl.glVertex3f( point.x - pointSize, point.z + pointSize, point.y );
            gl.glEnd();
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
        public final void drawFace( LibVertex[] verticesToDraw, GLTexture texture, float[] col )
        {
            //plain color or texture?
            if ( texture == null )
            {
                //plain
                gl.glDisable(   GL.GL_TEXTURE_2D    );              //disable texture-mapping
              //gl.glEnable(    GL.GL_LIGHTING      );              //lights on
                gl.glColor3fv(  col, 0              );            //set the face's color

                //draw all vertices
                gl.glBegin(      GL.GL_POLYGON   );
                for ( LibVertex currentVertex : verticesToDraw )
                {
                    drawVertex( currentVertex );
                }
                gl.glEnd();
              //gl.glDisable(   GL.GL_LIGHTING      );          //lights off
            }
            else
            {
                //texture
                gl.glEnable(        GL.GL_TEXTURE_2D                        );      //enable texture-mapping
                gl.glBindTexture(   GL.GL_TEXTURE_2D, texture.ordinal()     );      //bind face's texture

                //draw all vertices
                gl.glBegin(      GL.GL_POLYGON   );
                for ( LibVertex currentVertex : verticesToDraw )
                {
                    drawTexturedVertex( currentVertex );
                }
                gl.glEnd();
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

        public final void initTextures()
        {

            gl.glGenTextures( textureImages.length, IntBuffer.allocate( textureImages.length ) );

            for ( int i = 0; i < textureImages.length; ++i )
            {
                gl.glBindTexture(GL.GL_TEXTURE_2D, i );
                makeRGBTexture( textureImages[ i ] );
                gl.glTexParameteri(   GL.GL_TEXTURE_2D,   GL.GL_TEXTURE_MIN_FILTER,   GL.GL_LINEAR    );
                gl.glTexParameteri(   GL.GL_TEXTURE_2D,   GL.GL_TEXTURE_MAG_FILTER,   GL.GL_LINEAR    );
                gl.glTexEnvf(         GL.GL_TEXTURE_ENV,  GL.GL_TEXTURE_ENV_MODE,     GL.GL_REPLACE   );
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
            gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );
            gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR );
            gl.glTexImage2D(    GL.GL_TEXTURE_2D, 0, GL.GL_RGB, img.width, img.height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.bytes );
        }

        @Override
        public void drawOrthoBitmapBytes( GLImage glImage, int x, int y )
        {
            //prepare rendering 2D
            setOrthoOn();

            //blending prunes transparent pixels
            gl.glEnable(      GL.GL_BLEND           );
            gl.glBlendFunc(   GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA  );

            //be sure to disable texturing - bytes will not be drawn otherwise
            gl.glDisable(        GL.GL_TEXTURE_2D                        );

            //set and draw pixels
            gl.glRasterPos2f( 0, 0 );
            gl.glBitmap( 0, 0, 0, 0, x, y, glImage.bytes );  //workaround to allow negative coordinates
            gl.glDrawPixels( glImage.width, glImage.height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, glImage.bytes );
            gl.glFlush();

            //disable blending
            gl.glDisable(      GL.GL_BLEND           );

            //restore previous perspective and model views
            setOrthoOff();
        }

        @Override
        public void setOrthoOn()
        {
            // prepare to render in 2D
            gl.glDisable( GL.GL_DEPTH_TEST );             // so 2D stuff stays on top of 3D scene
            gl.glMatrixMode( GL.GL_PROJECTION );
            gl.glPushMatrix();                            // preserve perspective view
            gl.glLoadIdentity();                          // clear the perspective matrix
            gl.glOrtho( 0, ShooterSettings.FORM_WIDTH, 0, ShooterSettings.FORM_HEIGHT, -1, 1 );  // turn on 2D
            gl.glMatrixMode( GL.GL_MODELVIEW );
            gl.glPushMatrix();                // Preserve the Modelview Matrix
            gl.glLoadIdentity();              // clear the Modelview Matrix
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
    }
