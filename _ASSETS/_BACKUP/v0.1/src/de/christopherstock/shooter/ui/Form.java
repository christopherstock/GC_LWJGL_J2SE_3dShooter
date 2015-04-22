/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui;

    import  java.awt.*;
    import  java.awt.event.*;
    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  javax.swing.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.*;

    /**************************************************************************************
     * The Form holds the gl-canvas and the preloader-view.
     *
     * @author stock
     * @version 1.0
     **************************************************************************************/
    public class Form extends JFrame implements WindowListener
    {
        public      static  final   int             FORM_WIDTH              = 640;
        public      static  final   int             FORM_HEIGHT             = 480;

        public      static          Form            shooterForm             = null;
        public      static          int             x                       = 100;
        public      static          int             y                       = 100;
        public      static          BufferedImage   iconImage               = null;
        public      static          BufferedImage   bgImage                 = null;

        public Form()
        {
            //get screen environment
            Point centerPoint   = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();

            setIconImage(               iconImage                   );
            setTitle(                   Shooter.FORM_TITLE          );
            setDefaultCloseOperation(   EXIT_ON_CLOSE               );
            setLocation(                (int)centerPoint.getX() - FORM_WIDTH / 2, (int)centerPoint.getY() - FORM_HEIGHT / 2 );
            setSize(                    FORM_WIDTH, FORM_HEIGHT     );
            setResizable(               false                       );
            addWindowListener(          this                        );
            getContentPane().add(       GLPanel.glPanel             );
            setVisible(                 true                        );
            try
            {
                //setting the form always on top may result in a SecurityException on some platforms ( jnlp! )
                setAlwaysOnTop(         true                        );
            }
            catch ( SecurityException se ) {}
        }

        public static void init()
        {
            //load screen images
            try
            {
                iconImage   = ImageIO.read( Form.class.getResourceAsStream( Path.EScreen.url + "icon.png" ) );
                bgImage     = ImageIO.read( Form.class.getResourceAsStream( Path.EScreen.url + "bg.png"   ) );
            }
            catch ( IOException ioe )
            {
                Debug.err("ERROR! Screen-Graphics could not be loaded!");
            } // endcatch

            //instantiate form
            shooterForm = new Form();

        }

        @Override
        public void paint( Graphics g )
        {
            //draw loading screen if still loading
            if ( !Shooter.glPanelInitialized )
            {
                //release clip and draw the bg-image
                g.setClip( 0, 0, FORM_WIDTH, FORM_HEIGHT );
                g.drawImage( bgImage, 0, 0, null );
            }

            //call super-paint
            super.paint( g );
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
                Debug.err( "Setting host-operating system lookAndFeel failed!\n" + e.toString() );
            }
        }

        public void windowClosing( WindowEvent arg0 )
        {
            Debug.info( "main form has been closed properly." );
            Shooter.destroyed = true;
        }

        public void windowOpened(       WindowEvent arg0 )
        {
        }

        public void windowClosed(       WindowEvent arg0 )
        {
        }

        public void windowIconified(    WindowEvent arg0 )
        {
        }

        public void windowDeiconified(  WindowEvent arg0 )
        {
        }

        public void windowActivated(    WindowEvent arg0 )
        {
        }

        public void windowDeactivated(  WindowEvent arg0 )
        {
        }
    }
