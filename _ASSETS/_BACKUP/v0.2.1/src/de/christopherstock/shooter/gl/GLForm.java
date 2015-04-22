/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  java.awt.*;
    import  java.awt.event.*;
    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  javax.swing.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
     * The Form holds the gl-canvas and the preloader-view.
     *
     * @author      Christopher Stock
     * @version     0.2
     **************************************************************************************/
    public class GLForm implements WindowListener
    {
        public      static          GLForm          singleton               = null;

        private                     JFrame          nativeForm              = null;

        //to static
        private                     BufferedImage   iconImage               = null;
        public                      BufferedImage   bgImage                 = null;

        public GLForm( Component joglPanel )
        {
            //load form utils
            try
            {
                iconImage   = ImageIO.read( GLForm.class.getResourceAsStream( Path.EScreen.url + "icon.png" ) );
                bgImage     = ImageIO.read( GLForm.class.getResourceAsStream( Path.EScreen.url + "bg.png"   ) );
            }
            catch ( IOException ioe )
            {
                Debug.bugfix.err("ERROR! Screen-Graphics could not be loaded!");
            }

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
                    g2d.setClip( 0, 0, ShooterSettings.FORM_WIDTH, ShooterSettings.FORM_HEIGHT );
                    g2d.drawImage( bgImage, 0, 0, null );

                    //call super-paint here in order to draw gl stuff!
                    super.paint( g );
                }
            };

            //get screen environment
            Point centerPoint   = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

            nativeForm.setIconImage(               iconImage                    );
            nativeForm.setTitle(                   ShooterSettings.FORM_TITLE           );
            nativeForm.setDefaultCloseOperation(   JFrame.EXIT_ON_CLOSE         );
            nativeForm.setLocation(                (int)centerPoint.getX() - ShooterSettings.FORM_WIDTH / 2, (int)centerPoint.getY() - ShooterSettings.FORM_HEIGHT / 2 );
            nativeForm.setSize(                    ShooterSettings.FORM_WIDTH, ShooterSettings.FORM_HEIGHT      );
            nativeForm.setResizable(               false                        );

            //add listener
            nativeForm.addWindowListener(          this                         );

            //set canvas as content pane
            nativeForm.getContentPane().add(       joglPanel                  );

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
        public static final void setLookAndFeel()
        {
            try
            {
                UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            }
            catch( Exception e )
            {
                Debug.bugfix.err( "Setting host-operating system lookAndFeel failed!\n" + e.toString() );
            }
        }


        @Override
        public void windowClosing( WindowEvent arg0 )
        {
            Debug.bugfix.info( "main form has been closed properly." );
            Shooter.destroyed = true;
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
