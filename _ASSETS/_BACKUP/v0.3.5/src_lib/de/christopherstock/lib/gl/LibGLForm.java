/*  $Id: LibGLForm.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.awt.*;
    import  java.awt.event.*;
    import  java.awt.image.*;
    import  javax.swing.*;
    import  de.christopherstock.lib.*;

    /**************************************************************************************
     * The Form holds the gl-canvas and the preloader-view.
     *
     * @author      Christopher Stock
     * @version     0.3.5
     **************************************************************************************/
    public class LibGLForm implements WindowListener
    {
        public static interface GLCallbackForm
        {
            public void onFormDestroyed();
        }


        private                     GLCallbackForm  iForm                       = null;
        private                     JFrame          nativeForm                  = null;
        private                     BufferedImage   iIconImage                  = null;
        public                      BufferedImage   iBgImage                    = null;

        public LibGLForm( GLCallbackForm aForm, String aTitle, Component contentPane, int width, int height, BufferedImage aIconImage, BufferedImage aBgImage )
        {
            iForm       = aForm;
            iIconImage  = aIconImage;
            iBgImage    = aBgImage;

            //instanciate JFrame
            nativeForm = new JFrame()
            {
                @Override
                public void paint( Graphics g )
                {
                    //do NOT call super-paint here! form will not draw otherwise
                    //super.paint( g );

                    //cast to 2d
                    Graphics2D g2d = (Graphics2D)g;

                    //draw 2D
                    g2d.setClip( 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE );
                    g2d.drawImage( iBgImage, 0, 0, null );

                    //call super-paint here in order to draw gl stuff!
                    super.paint( g );
                }
            };

            //get screen environment
            Point centerPoint   = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

            nativeForm.setIconImage(               iIconImage                   );
            nativeForm.setTitle(                   aTitle                       );
            nativeForm.setDefaultCloseOperation(   JFrame.EXIT_ON_CLOSE         );
            nativeForm.setLocation(                (int)centerPoint.getX() - width / 2, (int)centerPoint.getY() - height / 2 );
            nativeForm.setSize(                    width, height );
            nativeForm.setResizable(               false                        );

            //add listener
            nativeForm.addWindowListener(          this                         );

            //set canvas as content pane
            nativeForm.getContentPane().add(       contentPane                    );

            //show form
            nativeForm.setVisible(                 true                         );

            //stick in foreground ( may raise a SucurityException )
            try
            {
                nativeForm.setAlwaysOnTop(         true                         );
            }
            catch ( SecurityException se ) {}
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


        @Override
        public void windowClosing( WindowEvent arg0 )
        {
            iForm.onFormDestroyed();
        }

        @Override
        public void windowOpened(       WindowEvent arg0 )
        {
        }

        @Override
        public void windowClosed(       WindowEvent arg0 )
        {
        }

        @Override
        public void windowIconified(    WindowEvent arg0 )
        {
        }

        @Override
        public void windowDeiconified(  WindowEvent arg0 )
        {
        }

        @Override
        public void windowActivated(    WindowEvent arg0 )
        {
        }

        @Override
        public void windowDeactivated(  WindowEvent arg0 )
        {
        }
    }
