/*  $Id: GL3D.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  java.awt.*;
    import  java.awt.geom.*;
    import  java.awt.image.*;
    import  java.nio.*;
    import  javax.imageio.*;
    import  de.christopherstock.lib.math.*;
import de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.Debug;
import  de.christopherstock.shooter.ui.*;

    public class GLImage
    {
        public static enum ImageUsage
        {
            EOrtho,
            ETexture,
            ;
        }

        public                  int             width                   = 0;
        public                  int             height                  = 0;
        public                  ByteBuffer      bytes                   = null;

        public GLImage( BufferedImage aBufferedImage, ImageUsage usage )
        {
            width   = aBufferedImage.getWidth();
            height  = aBufferedImage.getHeight();

            switch ( usage )
            {
                case EOrtho:
                {
                    bytes   = getOrthoByteBuffer( aBufferedImage );
                    break;
                }

                case ETexture:
                {
                    bytes   = getTextureByteBuffer( aBufferedImage );
                    break;
                }
            }
        }

        private static final ByteBuffer getOrthoByteBuffer( BufferedImage aBufferedImage )
        {
            byte[]      imgBytes    = null;
            ByteBuffer  ret         = null;

            DataBuffer  db          = aBufferedImage.getData().getDataBuffer();

            if ( db instanceof DataBufferInt )
            {
                int[] ints = ((DataBufferInt)db ).getData();
                //Debug.bugfix.out( "number of ints is: ["+ints.length+"]" );

                imgBytes = LibMath.intArrayToByteArray( ints );
                //Debug.bugfix.out( "number of bytes is: ["+imgBytes.length+"]" );
            }
            else if ( db instanceof DataBufferByte )
            {
                imgBytes = ((DataBufferByte)db ).getData();
                //Debug.bugfix.out( "number of bytes is: ["+imgBytes.length+"]" );
            }

            //fix images without rgb value!
            if ( imgBytes != null && imgBytes.length != aBufferedImage.getWidth() * aBufferedImage.getHeight() * 4 )
            {
                //Debug.bugfix.out( "fixing RGB bytes to ARGB bytes. img ["+aBufferedImage.getWidth()+" px]["+aBufferedImage.getHeight()+" px]["+imgBytes.length+" bytes] should be ["+(aBufferedImage.getWidth()*aBufferedImage.getHeight()*4)+"]" );
                imgBytes = LibImage.insertAlphaByte( imgBytes );
            }

            imgBytes = LibImage.flipBytesVertARGB( imgBytes, aBufferedImage.getWidth() );

            ret = ByteBuffer.allocateDirect( imgBytes.length ).put( imgBytes );
            ret.flip();

            return ret;
        }

        private static final ByteBuffer getTextureByteBuffer( BufferedImage aBufferedImage )
        {
            AffineTransform tx = java.awt.geom.AffineTransform.getScaleInstance( 1, -1 );
            tx.translate( 0, -aBufferedImage.getHeight( null ) );
            aBufferedImage = new AffineTransformOp( tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR ).filter( aBufferedImage, null );
            byte[] data    = ( (DataBufferByte)aBufferedImage.getRaster().getDataBuffer() ).getData();

            ByteBuffer buff = ByteBuffer.allocateDirect( data.length );
            buff.order( ByteOrder.nativeOrder() );
            buff.put( data, 0, data.length );
            buff.rewind();

            return buff;
        }

        public static final GLImage[] loadAll( int amount, String path, String extension, ImageUsage usage )
        {
            GLImage[] glImages = new GLImage[ amount ];

            for ( int i = 0; i < glImages.length; ++i )
            {
                String url = path + i + extension;

                try
                {
                    //load the buffered image
                    BufferedImage buf = ImageIO.read( GLImage.class.getResourceAsStream( url ) );

                    final boolean supportAlphaChannel = false;
                    if ( supportAlphaChannel )
                    {
                        //unused - used for 2d drawing in order to draw with a different alpha value
                        BufferedImage   bufA    = new BufferedImage( buf.getWidth(), buf.getHeight(), BufferedImage.TYPE_INT_ARGB );
                        Graphics        g       = bufA.getGraphics();
                        g.drawImage( buf, 0, 0, null );

                        //assign alpha-buffered-image
                        glImages[ i ] = new GLImage( bufA, usage );
                    }
                    else
                    {
                        //direct assignment without alpha
                        glImages[ i ] = new GLImage( buf, usage );
                    }
                }
                catch( Exception e )
                {
                    Debug.bugfix.err( "ERROR! Couldn't read Texture-Image " + url );
                    e.printStackTrace();
                    System.exit( 0 );
                }
            }

            return glImages;
        }

        public static final GLImage getFromString( String stringToDisplay, Font font, Color colFg, Color colShadow, Color colOutline )
        {
            Graphics2D      g           = (Graphics2D)GLPanel.glPanel.getNativePanel().getGraphics();

            int             imgWidth    = Strings.getStringWidth(  g, stringToDisplay, font );
            int             imgHeight   = Strings.getStringHeight( g, font );

            //create dynamic buffered image and draw context
            BufferedImage   template2 = new BufferedImage( imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB );
            Graphics2D      g2 = (Graphics2D)template2.getGraphics();

            //clear canvas
            //g2.setPaint( new Color( 1.0f, 0.0f, 0.0f, 1.0f ) );
            //g2.fillRect( 0, 0, imgWidth / 2, imgHeight );

            //draw outlined string
            Strings.drawString( g2, colFg, colShadow, colOutline, font, Anchor.EAnchorLeftTop, stringToDisplay, 0, 0 );

            //convert to and return as GLImage
            return new GLImage( template2, ImageUsage.EOrtho );
        }
    }
