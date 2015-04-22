/*  $Id: LibGLForm.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.awt.*;
    import  java.awt.event.*;
    import  java.awt.image.*;
    import  javax.swing.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.gl.LibGLFrame.GLCallbackForm;

    /**************************************************************************************
    *   The Form holds the gl-canvas and the preloader-view.
    *
    *   @author         Christopher Stock
    *   @version        0.3.10
    **************************************************************************************/
    public class LibGLForm implements WindowListener, FocusListener
    {
        public                      GLCallbackForm  iForm                       = null;
        public                      JFrame          nativeForm                  = null;
        private                     BufferedImage   iIconImage                  = null;
        public                      BufferedImage   iBgImage                    = null;

        public LibGLForm( GLCallbackForm aForm, String aTitle, Component contentPane, int width, int height, BufferedImage aIconImage, BufferedImage aBgImage )
        {
            iForm       = aForm;
            iIconImage  = aIconImage;
            iBgImage    = aBgImage;

            //instanciate JFrame
            nativeForm = new LibGLFrame( iBgImage );

            //get screen environment
            Point centerPoint   = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

            nativeForm.setIconImage(                iIconImage                      );
            nativeForm.setTitle(                    aTitle                          );
            nativeForm.setDefaultCloseOperation(    JFrame.EXIT_ON_CLOSE            );
            nativeForm.setLocation(                 (int)centerPoint.getX() - width / 2, (int)centerPoint.getY() - height / 2 );
            nativeForm.setSize(                     width, height                   );
            nativeForm.setResizable(                false                           );
            nativeForm.setUndecorated(              true                            );

            //add listener
            nativeForm.addWindowListener(           this                            );
            nativeForm.addFocusListener(            this                            );

            //set canvas as content pane
            nativeForm.getContentPane().add(        contentPane                     );

            //show form
            nativeForm.setVisible(                  true                            );

            //stick in foreground ( may raise a SucurityException )
            try
            {
                nativeForm.setAlwaysOnTop(          true                            );
            }
            catch ( SecurityException se )
            {
                //ignore exception
            }
        }

        @Override
        public void windowClosing( WindowEvent arg0 )
        {
            iForm.onFormDestroyed();
        }

        @Override
        public void windowOpened(       WindowEvent arg0 )
        {
            //no operations
        }

        @Override
        public void windowClosed(       WindowEvent arg0 )
        {
            //no operations
        }

        @Override
        public void windowIconified(    WindowEvent arg0 )
        {
            //no operations
        }

        @Override
        public void windowDeiconified(  WindowEvent arg0 )
        {
            //no operations
        }

        @Override
        public void windowActivated(    WindowEvent arg0 )
        {
            //no operations
        }

        @Override
        public void windowDeactivated(  WindowEvent arg0 )
        {
            //no operations
        }

        @Override
        public void focusLost( FocusEvent fe )
        {
            //no operations
        }

        @Override
        public void focusGained( FocusEvent fe )
        {
            //no operations
        }

        /********************************************************************************
        *   Sets lookAndFeel of the host operating system.
        *********************************************************************************/
        public static final void setLookAndFeel( LibDebug debug )
        {
            try
            {
                UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            }
            catch( Exception e )
            {
                debug.err( "Setting host-operating system lookAndFeel failed!\n" + e.toString() );
            }
        }
    }
