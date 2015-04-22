/*  $Id: GLView.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.opengl;

    import  java.awt.image.*;
    import  java.nio.*;
    import  javax.media.opengl.*;
    import  javax.media.opengl.glu.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.opengl.GLPanel.Drawable;
    import  de.christopherstock.shooter.math.*;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class GLView implements GLEventListener
    {
        /**************************************************************************************
        *   Debug point's interface.
        *
        *   @author     Christopher Stock
        *   @version    0.2
        **************************************************************************************/
        public interface DebugPoint3D
        {
            public abstract Vertex  getPoint();
            public abstract float   getPointSize();
            public abstract float[] getPointColor();
        }

        private     static  final   float               DEPTH_BUFFER_SIZE       = 1.0f;

        private     static  final   float               VIEW_ANGLE              = 45.0f;
        private     static  final   float               VIEW_MIN                = 0.1f;
        private     static  final   float               VIEW_MAX                = 100.0f;

        public      static          GLView              singleton               = null;

        private                     GL                  gl                      = null;
        private                     GLU                 glu                     = null;

        private                     Drawable            drawCallback            = null;
        private                     BufferedImage[]     images                  = null;

        public static final void init( Drawable aDrawCallback, BufferedImage[] aTextureImages )
        {
            singleton    = new GLView( aDrawCallback, aTextureImages );
        }

        private GLView( Drawable aDrawCallback, BufferedImage[] aTextureImages )
        {
            drawCallback = aDrawCallback;
            images      = aTextureImages;
        }

        @Override
        public void init( GLAutoDrawable glDrawable )
        {
            Debug.info( "invoked init-method of GLView" );

            //assign the panel's dimensions and parse its offsets
            GLPanel.PANEL_WIDTH  = GLPanel.glPanel.getSize().width;
            GLPanel.PANEL_HEIGHT = GLPanel.glPanel.getSize().height;

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
            glu.gluPerspective( VIEW_ANGLE, (float)Form.FORM_WIDTH / (float)Form.FORM_HEIGHT, VIEW_MIN, VIEW_MAX );

            //enter matrix mode modelview :)
            gl.glMatrixMode( GL.GL_MODELVIEW );

            //init all textures
            initTextures();
            GLPanel.glPanel.requestFocus();

            //set up lighting ( test )
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_AMBIENT,    new float[] { 0.5f, 0.5f, 0.5f, 1.0f }, 0 );
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_DIFFUSE,    new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, 0 );
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_POSITION,   new float[] { 0.0f, 0.0f, 2.0f, 1.0f }, 0 );
            gl.glEnable(  GL.GL_LIGHT1);

            //parse hud's offsets
            Offset.parseOffsets();

            //now
            Shooter.glPanelInitialized = true;
            Debug.info( "glPanel fully initialized" );
        }

        @Override
        public void display( GLAutoDrawable glDrawable )
        {
            //clear the gl and reset modelview
            gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

            //invoke callback drawable
            drawCallback.draw( null );
        }

        @Override
        public void reshape( GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4 )
        {
        }

        @Override
        public void displayChanged( GLAutoDrawable arg0, boolean arg1, boolean     arg2 )
        {
        }

        public final void drawDebugPoint( DebugPoint3D fxPoint )
        {
            Vertex  point      = fxPoint.getPoint();
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

        public void setPlayerCamera()
        {
            Player singleton = Player.singleton;

            gl.glLoadIdentity();                    //new identity please
            gl.glNormal3f(  0.0f, 0.0f, 0.0f );     //normalize

            //modify player's posZ by its height and walking-modifier
            float walkModZ = singleton.cylinder.getAnchor().z + singleton.view.depthEye + singleton.getWalkingAngle1Modifier() * Player.AMP_WALKING_Z;

            gl.glRotatef(       singleton.view.rotY + singleton.view.dieModY,          0.0f, 0.0f, 1.0f        );      //rotate z
            gl.glRotatef(       singleton.view.rotX + singleton.view.dieModX,          1.0f, 0.0f, 0.0f        );      //rotate x
            gl.glRotatef(       360.0f - singleton.view.rotZ - singleton.view.dieModZ, 0.0f, 1.0f, 0.0f        );      //rotate y

            //3rd person camera?
            float modX = 0.0f;
            float modY = 0.0f;
            if ( Shooter.ENABLE_3RD_PERSON_CAMERA )
            {
                modX = UMath.sinDeg( singleton.view.rotZ ) * 2.0f;
                modY = UMath.cosDeg( singleton.view.rotZ ) * 2.0f;
            }

            //check if user is dying
            if ( singleton.dying )
            {
                gl.glTranslatef(    -singleton.cylinder.getAnchor().x, -singleton.cylinder.getAnchor().z - Player.DEPTH_DEATH - ( singleton.view.depthEye - singleton.view.dieModTransZ ), -singleton.cylinder.getAnchor().y   );      //translate
            }
            else
            {
                //gl.glTranslatef(    -user.cylinder.getAnchor().x, -user.cylinder.getAnchor().z - walkModZ - user.dieModTransZ, -user.cylinder.getAnchor().y   );      //translate
                gl.glTranslatef(    -singleton.cylinder.getAnchor().x - modX, -singleton.cylinder.getAnchor().z - walkModZ - singleton.view.dieModTransZ, -singleton.cylinder.getAnchor().y - modY );      //translate
            }
        }

        public void drawSceneBg( int textureIndex )
        {
            //assign texture's position on the bg-face
            float   texX            = ( 360.0f - Player.singleton.view.rotZ ) * 1.0f / 360.0f;
            float   positionY       = -0.95f;

            float   width           = 2.2f;
            float   height          = 2.5f;

            //draw bg-face?

            gl.glLoadIdentity();                        //new identity please
            gl.glNormal3f(  0.0f, 0.0f, 0.0f    );      //normalize

            gl.glEnable(    GL.GL_TEXTURE_2D    );      //enable texture-mapping
            gl.glColor3f(   1.0f, 1.0f, 1.0f    );      //reset colors
            gl.glBindTexture( GL.GL_TEXTURE_2D, textureIndex );

            gl.glDepthMask( false );                    //disable the depth-mask

            gl.glBegin( GL.GL_QUADS );                  //begin drawing
            gl.glTexCoord2f(    texX,           0.0f );   gl.glVertex3f(      -width,     positionY,              -3.75f );
            gl.glTexCoord2f(    texX + 0.2f,    0.0f );   gl.glVertex3f(       width,     positionY,              -3.75f );
            gl.glTexCoord2f(    texX + 0.2f,    1.0f );   gl.glVertex3f(       width,     positionY + height,       -3.75f );
            gl.glTexCoord2f(    texX,           1.0f );   gl.glVertex3f(      -width,     positionY + height,       -3.75f );
            gl.glEnd();                                 //finish drawing

            gl.glDepthMask( true );                     //enable the depth-mask
        }

        public final void drawFace( Face f )
        {
            //plain color or texture?
            if ( f.texture == null )
            {
                //plain
                gl.glDisable(   GL.GL_TEXTURE_2D    );              //disable texture-mapping
              //gl.glEnable(    GL.GL_LIGHTING      );              //lights on
                gl.glColor3fv(  f.color.f3, 0         );            //set the face's color

                //draw all vertices
                gl.glBegin(      GL.GL_POLYGON   );
                for ( Vertex currentVertex : f.transformedVertices )
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
                gl.glBindTexture(   GL.GL_TEXTURE_2D, f.texture.ordinal()   );      //bind face's texture

                //draw all vertices
                gl.glBegin(      GL.GL_POLYGON   );
                for ( Vertex currentVertex : f.transformedVertices )
                {
                    drawTexturedVertex( currentVertex );
                }
                gl.glEnd();
            }
        }

        public final void drawVertex( Vertex v )
        {
            gl.glVertex3f(   v.x, v.z, v.y   );
        }

        public final void drawTexturedVertex( Vertex v )
        {
            gl.glTexCoord2f( v.u, v.v );
            gl.glVertex3f(   v.x, v.z, v.y   );
        }

        public final void initTextures()
        {
            gl.glGenTextures( images.length, IntBuffer.allocate( images.length ) );

            for ( int i = 0; i < images.length; ++i )
            {
                gl.glBindTexture(GL.GL_TEXTURE_2D, i );
                makeRGBTexture( gl, images[ i ] );
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
        private static final void makeRGBTexture( GL gl, BufferedImage img )
        {
            ByteBuffer  buff    = null;
            byte[]      data    = null;

            //rewind data
            data = ( (DataBufferByte)img.getRaster().getDataBuffer() ).getData();
            buff = ByteBuffer.allocateDirect( data.length );
            buff.order( ByteOrder.nativeOrder() );
            buff.put( data, 0, data.length );
            buff.rewind();

            //bind texture to gl
            gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );
            gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR );
            gl.glTexImage2D(    GL.GL_TEXTURE_2D, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buff );
        }
    }
