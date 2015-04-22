/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui;

    import  java.awt.*;
    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   The Image-Loader.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class Images
    {
        public static final BufferedImage[] readAllBufferedImages( int num, String path, String ext, boolean supportAlphaChannel )
        {
            BufferedImage[] bufImages = new BufferedImage[ num ];
            try
            {
                for ( int i = 0; i < bufImages.length; ++i )
                {
                    //load the buffered image
                    BufferedImage buf = ImageIO.read( Images.class.getResourceAsStream( path + i + ext ) );

                    if ( supportAlphaChannel )
                    {
                        BufferedImage   bufA    = new BufferedImage( buf.getWidth(), buf.getHeight(), BufferedImage.TYPE_INT_ARGB );
                        Graphics        g       = bufA.getGraphics();
                        g.drawImage( buf, 0, 0, null );

                        //assign alpha-buffered-image
                        bufImages[ i ] = bufA;
                    }
                    else
                    {
                        //direct assignment without alpha
                        bufImages[ i ] = buf;
                    }
                }
            }
            catch( Exception e )
            {
                Debug.err( "Error loading avatar-Images" );
            }

            return bufImages;

        }

        /*****************************************************************************************//**
        *   Reads the given {@link InputStream} buffered and returns all read bytes as a byte-array.
        *
        *   @param  is      The InputStream to read buffered.
        *   @return         All bytes of the given InputStream as a byte-array.
        *********************************************************************************************/
        public static byte[] readStreamBuffered( InputStream is )
        {
            ByteArrayOutputStream   baos        = new ByteArrayOutputStream();
            int                     byteread    = 0;

            try
            {
                //read one byte after another until the EOF-flag is returned
                while ( ( byteread = is.read() ) != -1 )
                {
                    //write this byte, if it could be read, into the output-stream
                    baos.write( byteread );
                }

                is.close();
                baos.close();

                //return the output-stream as a byte-array
                return baos.toByteArray();
            }
            catch ( Exception e )
            {
                try
                {
                    is.close();
                    baos.close();
                }
                catch ( Exception u ) {}
                e.printStackTrace();
                return null;
            }
        }
    }
