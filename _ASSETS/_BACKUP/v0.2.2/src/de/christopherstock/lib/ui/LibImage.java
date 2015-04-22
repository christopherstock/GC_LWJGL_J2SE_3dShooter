/*  $Id: LibImage.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.ui;

    import  java.awt.*;
    import  java.awt.geom.*;
    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  de.christopherstock.lib.*;

    /**************************************************************************************
    *   The Image-Loader.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class LibImage
    {
        public static final BufferedImage[] readAllBufferedImages( int num, String path, String ext, boolean supportAlphaChannel, LibDebug debug )
        {
            BufferedImage[] bufImages = new BufferedImage[ num ];
            try
            {
                for ( int i = 0; i < bufImages.length; ++i )
                {
                    //load the buffered image
                    BufferedImage buf = ImageIO.read( LibImage.class.getResourceAsStream( path + i + ext ) );

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
                debug.err( "Error loading image []" );
            }

            return bufImages;
        }

        /**************************************************************************************
        *   Flips all columns of this BufferedImage
        *   so its byte-array is returned horizontal mirrored.
        *
        *   @param  original    The bitmap's bytes - length must be a multiple of 4.
        **************************************************************************************/
        public static final byte[] flipBytesHorzARGB( byte[] original )
        {
            byte[] flipped = new byte[ original.length ];

            int newIndex = original.length - 4;

            for ( int i = 0; i < original.length; i += 4 )
            {
                flipped[ newIndex + 0 ] = original[ i + 0 ];
                flipped[ newIndex + 1 ] = original[ i + 1 ];
                flipped[ newIndex + 2 ] = original[ i + 2 ];
                flipped[ newIndex + 3 ] = original[ i + 3 ];

                newIndex -= 4;
            }

            return flipped;
        }

        public static final BufferedImage flipVert( BufferedImage bi )
        {
            AffineTransform tx = java.awt.geom.AffineTransform.getScaleInstance( 1, -1 );
            tx.translate( 0, -bi.getHeight( null ) );
            return new AffineTransformOp( tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR ).filter( bi, null );
        }

        /**************************************************************************************
        *   Flips all columns of this BufferedImage
        *   so its byte-array is returned horizontal mirrored.
        *
        *   @param  original    The bitmap's bytes - length must be a multiple of 4.
        *   @param  rowLength   The number of pixels (ARGB values) per row
        *   @deprecated         Replaced by {@link #flipVert(BufferedImage)}
        **************************************************************************************/
        @Deprecated
        public static final byte[] flipBytesVertARGB( byte[] original, int rowLength )
        {
            int                     bytesPerRow = rowLength * 4;
            ByteArrayOutputStream   baos        = new ByteArrayOutputStream();

            for ( int i = original.length - bytesPerRow; i >= 0; i -= bytesPerRow )
            {
                for ( int j = 0; j < bytesPerRow; ++j )
                {
                    baos.write( original[ i + j ] );
                }
            }

            return baos.toByteArray();
        }

        public static final byte[] insertAlphaByte( byte[] rgbBytes )
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            for ( int i = 0; i < rgbBytes.length; i += 3 )
            {
                //set opaque alpha value
                baos.write( rgbBytes[ i + 0 ] );
                baos.write( rgbBytes[ i + 1 ] );
                baos.write( rgbBytes[ i + 2 ] );

                baos.write( 0xff );
            }

            //Debug.bugfix.out( "fixed to [" + baos.size() + "] bytes" );

            return baos.toByteArray();
        }
    }
