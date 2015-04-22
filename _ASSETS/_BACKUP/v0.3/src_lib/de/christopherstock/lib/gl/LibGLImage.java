/*  $Id: LibGLImage.java 203 2011-01-09 21:20:17Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.awt.*;
    import  java.awt.image.*;
    import  java.nio.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.ui.*;

    public class LibGLImage
    {
        public static enum SrcPixelFormat
        {
            ERGB,
            ERGBA,
            ;
        }

        public static enum ImageUsage
        {
            EOrtho,
            ETexture,
            ;
        }

        public              int                 width                       = 0;
        public              int                 height                      = 0;
        public              ByteBuffer          bytes                       = null;
        public              SrcPixelFormat      srcPixelFormat              = null;

        public LibGLImage( BufferedImage aBufferedImage, ImageUsage imageUsage, LibDebug debug, boolean flipAllBytes )
        {
            width   = aBufferedImage.getWidth();
            height  = aBufferedImage.getHeight();

            //detect pixel format
            if ( aBufferedImage.getColorModel().hasAlpha() )
            {
                debug.out( "format is RGB-A" );
                srcPixelFormat = SrcPixelFormat.ERGBA;
            }
            else
            {
                debug.out( "format is RGB" );
                srcPixelFormat = SrcPixelFormat.ERGB;
            }

            //flip buffered image horizontally
            if ( flipAllBytes )
            {
                aBufferedImage = LibImage.flipHorz( aBufferedImage );
            }
            else
            {
                aBufferedImage = LibImage.flipVert( aBufferedImage );
            }

            //read buffer according to usage
            switch ( imageUsage )
            {
                case EOrtho:
                {
                    bytes   = getOrthoByteBuffer( aBufferedImage, debug, flipAllBytes );
                    break;
                }

                case ETexture:
                {
                    bytes   = getTextureByteBuffer( aBufferedImage, debug, flipAllBytes );
                    break;
                }
            }
        }

        private final ByteBuffer getOrthoByteBuffer( BufferedImage aBufferedImage, LibDebug debug, boolean flipAllBytes )
        {
            ByteBuffer  ret         = null;
            byte[]      imgBytes    = LibImage.getBytesFromImg( aBufferedImage, debug );
/*
            //fix images without alpha value!
            if ( imgBytes.length != aBufferedImage.getWidth() * aBufferedImage.getHeight() * 4 )
            {
                debug.out( "fixing RGB bytes to ARGB bytes. img ["+aBufferedImage.getWidth()+" px]["+aBufferedImage.getHeight()+" px]["+imgBytes.length+" bytes] should be ["+(aBufferedImage.getWidth()*aBufferedImage.getHeight()*4)+"]" );
                imgBytes = LibImage.insertAlphaByte( imgBytes );
            }
*/
            //flip all bytes
            if ( flipAllBytes ) imgBytes = LibImage.flipBytesTest( imgBytes );

            ret = ByteBuffer.allocateDirect( imgBytes.length );
            //ret.order( ByteOrder.nativeOrder() );
            ret.put( imgBytes ); //, 0, imgBytes.length );
            ret.flip();

            return ret;
        }

        @SuppressWarnings( "unused" )
        private static final ByteBuffer getTextureByteBuffer( BufferedImage aBufferedImage, LibDebug debug, boolean flipAllBytes )
        {
            byte[] data    = ( (DataBufferByte)aBufferedImage.getRaster().getDataBuffer() ).getData();

            //flip all bytes
            if ( flipAllBytes ) data = LibImage.flipBytesTest( data );

            ByteBuffer buff = ByteBuffer.allocateDirect( data.length );
            buff.order( ByteOrder.nativeOrder() );
            buff.put( data, 0, data.length );
            buff.flip();

            return buff;
        }

        public static final LibGLImage[] convertAll( BufferedImage[] bufferedImages, ImageUsage imageUsage, LibDebug debug )
        {
            LibGLImage[] ret = new LibGLImage[ bufferedImages.length ];

            for ( int i = 0; i < bufferedImages.length; ++i )
            {
                ret[ i ] = new LibGLImage( bufferedImages[ i ], imageUsage, debug, true );
            }

            return ret;
        }

        public static final LibGLImage getFromString( String stringToDisplay, Font font, Color colFg, Color colShadow, Color colOutline, LibDebug debug, Color colBg )
        {
            Graphics2D      g           = LibGL3D.panel.getGraphics();

            int             imgWidth    = LibStrings.getStringWidth(  g, stringToDisplay, font );
            int             imgHeight   = LibStrings.getStringHeight( g, font );

            //create dynamic buffered image and draw context
            BufferedImage   template2 = new BufferedImage( imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB );
            Graphics2D      g2 = (Graphics2D)template2.getGraphics();

            //fill bg if desired
            if ( colBg != null ) LibDrawing.fillRect( g2, 0, 0, imgWidth, imgHeight, colBg );

            //draw outlined string
            LibStrings.drawString( g2, colFg, colShadow, colOutline, font, LibAnchor.EAnchorLeftTop, stringToDisplay, 0, 0 );

            //convert to and return as GLImage
            return new LibGLImage( template2, ImageUsage.EOrtho, debug, false );
        }

        public static final LibGLImage getFullOpaque( Color col, int width, int height, LibDebug debug )
        {
            //create dynamic buffered image and draw context
            BufferedImage   template2 = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
            Graphics2D      g2 = (Graphics2D)template2.getGraphics();

            //draw outlined string
            g2.setColor( col );
            g2.fillRect( 0, 0, width, height );

            //convert to and return as GLImage
            return new LibGLImage( template2, ImageUsage.EOrtho, debug, false );
        }
    }
