/*  $Id: Texture.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  java.awt.geom.*;
    import  java.awt.image.*;
    import  java.nio.*;
    import  javax.imageio.*;
    import  javax.media.opengl.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public enum Texture
    {
        ETest,
        EStones1,
        EBullet1,
        EOfficeWalls1,

        EBgDesert,
        EMeadowLight1,

        EWood1,
        EBricks1,

        ESand1,
        EBricksGrey1,

        EMeadowDark1,

        ;

        protected static final void init( GL gl )
        {
            gl.glGenTextures( values().length, IntBuffer.allocate( values().length ) );

            for ( int i = 0; i < values().length; ++i )
            {
                BufferedImage img = Texture.readPNGImage( Path.ETextures.url + values()[ i ].ordinal() + ".png" );
                gl.glBindTexture(GL.GL_TEXTURE_2D, i );
                makeRGBTexture( gl, img );
                gl.glTexParameteri(   GL.GL_TEXTURE_2D,   GL.GL_TEXTURE_MIN_FILTER,   GL.GL_LINEAR    );
                gl.glTexParameteri(   GL.GL_TEXTURE_2D,   GL.GL_TEXTURE_MAG_FILTER,   GL.GL_LINEAR    );
                gl.glTexEnvf(         GL.GL_TEXTURE_ENV,  GL.GL_TEXTURE_ENV_MODE,     GL.GL_REPLACE   );
            }
        }

        public static final Texture getByName( String name )
        {
            if ( name == null ) return null;

            for ( Texture tex : values() )
            {
                if ( tex.name().compareToIgnoreCase( name ) == 0 )
                {
                    return tex;
                }
            }

            return null;

        }

        private static final BufferedImage readPNGImage( String res )
        {
            try
            {
                BufferedImage img = ImageIO.read( Texture.class.getResourceAsStream( res ) );
                AffineTransform tx = java.awt.geom.AffineTransform.getScaleInstance( 1, -1 );
                tx.translate( 0, -img.getHeight( null ) );
                img = new AffineTransformOp( tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR ).filter( img, null );

                return img;
            }
            catch ( Exception e )
            {
                Debug.err( "ERROR! Couldn't read Texture-Image " + res );
                System.exit( 0 );
                return null;
            }
        }

        private static final void makeRGBTexture( GL gl, BufferedImage img )
        {
            ByteBuffer  buff    = null;
            byte[]      data    = null;

            //rewind - see http://www.experts-exchange.com/Programming/Languages/Java/Q_22397090.html?sfQueryTermInfo=1+jogl
            data = ( (DataBufferByte)img.getRaster().getDataBuffer() ).getData();
            buff = ByteBuffer.allocateDirect( data.length );
            buff.order( ByteOrder.nativeOrder() );
            buff.put( data, 0, data.length );
            buff.rewind();

            //bind texture to gl
            //Debug.DEBUG_OUT("texturing img");
            gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
            gl.glTexImage2D( GL.GL_TEXTURE_2D, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buff );

        }
    }
