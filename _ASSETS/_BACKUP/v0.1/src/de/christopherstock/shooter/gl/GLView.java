/*  $Id: GLView.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  javax.media.opengl.*;
    import  javax.media.opengl.glu.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The GL-View.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class GLView implements GLEventListener
    {
        private     static  final   float           DEPTH_BUFFER_SIZE           = 1.0f;

        private     static  final   float           VIEW_ANGLE                  = 45.0f;
        private     static  final   float           VIEW_MIN                    = 0.1f;
        private     static  final   float           VIEW_MAX                    = 100.0f;

        private     static          GLView          glView                      = null;
        private     static          GL              gl                          = null;
        private     static          GLU             glu                         = null;

        public static final void init()
        {
            glView          = new GLView();
            GLPanel.glPanel = new GLPanel( glView );
        }

        public void init( GLAutoDrawable glDrawable )
        {
            Debug.info("invoked init-method of GLView");

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
            Texture.init( gl );
            GLPanel.glPanel.requestFocus();

            //set up lighting ( test )
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_AMBIENT,    new float[] { 0.5f, 0.5f, 0.5f, 1.0f }, 0 );
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_DIFFUSE,    new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, 0 );
            gl.glLightfv( GL.GL_LIGHT1, GL.GL_POSITION,   new float[] { 0.0f, 0.0f, 2.0f, 1.0f }, 0 );
            gl.glEnable(  GL.GL_LIGHT1);

            //assign the panel's dimensions and parse its offsets
            GLPanel.PANEL_WIDTH  = GLPanel.glPanel.getSize().width;
            GLPanel.PANEL_HEIGHT = GLPanel.glPanel.getSize().height;
            Offset.parseOffsets();

            //now
            Shooter.glPanelInitialized = true;
            Debug.info( "glPanel fully initialized" );
        }

        public void display( GLAutoDrawable glDrawable )
        {
            //clear the gl and reset modelview
            gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

            Player.drawSceneBg( gl );                       //draw background => to Level class
            Player.setPlayerCamera( gl );                   //set the player's camera in new identity!
            Level.draw();                                   //draw the level
            BulletHole.drawAll();                           //draw bullet holes
            FXPoint.drawFxPoints( gl );                     //draw fx points
            DebugPoint.drawAll( gl );                       //draw debug points
            Player.user.cylinder.drawStandingCircle();      //draw circle on players bottom location

          //Player.gadget.draw3D( gl );                     //draw wearpon 3D in new identity! ( last step )
          //Player.wearpon.draw2D( gl );                    //draw wearpon 3D in new identity! ( last step )
          //Player.wearpon.draw3D( gl );                    //draw wearpon 3D in new identity! ( last step )
        }

        public void reshape( GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4 )
        {
        }

        public void displayChanged( GLAutoDrawable arg0, boolean arg1, boolean     arg2 )
        {
        }

        public static final void drawFace( Face face )
        {
            //gl.glOrtho(-1.5, 1.0, -2.0, 0.5, -1.0, 3.5);
            //gl.glOrtho( -1.5, 1.0, -0.5, 0.0, 0.0, 1.0 );
            face.draw( gl );
        }

        public static final void disableTextureMapping()
        {
            GLView.gl.glDisable( GL.GL_TEXTURE_2D );
        }
    }
