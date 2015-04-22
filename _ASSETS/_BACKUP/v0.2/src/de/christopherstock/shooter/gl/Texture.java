/*  $Id: Texture.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  java.awt.geom.*;
    import  java.awt.image.*;
    import  javax.imageio.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum Texture
    {
        ETest,
        EStones1,
        EBulletHoleConcrete,
        EOfficeWalls1(  Material.EConcrete      ),
        EBgDesert,
        EMeadowLight1,
        EWood1(         Material.EWood          ),
        EBricks1,
        ESand1,
        EBricksGrey1(   Material.EConcrete      ),
        EMeadowDark1,
        EBulletHoleSteel,
        EBulletHoleWood,

        ;

        private         Material            material            = null;

        private Texture()
        {
            this( Material.EUndefined );
        }

        private Texture( Material aMaterial )
        {
            material = aMaterial;
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

        public static final BufferedImage[] loadTextureImages()
        {
            BufferedImage[] textureImages = new BufferedImage[ values().length ];

            for ( int i = 0; i < values().length; ++i )
            {
                textureImages[ i ] = Texture.readTextureImage( Path.ETextures.url + i + ".png" );
            }

            return textureImages;
        }

        public static final BufferedImage readTextureImage( String res )
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

        public final Material getMaterial()
        {
            return material;
        }
    }
