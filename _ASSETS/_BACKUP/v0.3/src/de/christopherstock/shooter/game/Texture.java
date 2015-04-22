/*  $Id: Texture.java 253 2011-02-07 18:57:13Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.image.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLTexture.Translucency;
    import  de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public enum Texture
    {
        EBulletHoleGlass1mask,

        EBulletHoleConcrete1,
        EBulletHoleSteel1,
        EBulletHoleWood1,
        EBulletHoleGlass1(      Translucency.EHasMask,  LibGLMaterial.EUndefined,  EBulletHoleGlass1mask   ),

        EAmmoShotgunShells,
        EAmmoBullet9mm,

        EBoxes1,

        EBricks1(               Translucency.EOpaque,   LibGLMaterial.ESteel                               ),
        EBricks2(               Translucency.EOpaque,   LibGLMaterial.EConcrete                            ),

        EBlackMetal1,

        ECar1,
        ECar2,
        ECar3,
        EChrome1,
        EConcrete1,
        EGlass1(                Translucency.EGlass,    LibGLMaterial.EGlass                               ),
        EGrass1,
        EGrass2,
        EGrass3,

        EKeyboard1,

        ELeaf1,
        ECeramic1,

        EPlastic1,

        EScreen1,

        ESoil1,



        ELeather1,
        EWallpaper1,
        ERug1,
        ESand1,


        EWearponShotgun,

        EStones1,
        ETest,
        EWood1(                 Translucency.EOpaque,   LibGLMaterial.EWood                                ),
        EWood2(                 Translucency.EOpaque,   LibGLMaterial.EWood                                ),
        EWood3(                 Translucency.EOpaque,   LibGLMaterial.EWood                                ),
        ;

        public                      LibGLTexture        iTexture            = null;
        public                      LibGLImage          iTextureImage       = null;

        private Texture()
        {
            this( Translucency.EOpaque );
        }

        private Texture( Translucency translucency )
        {
            this( translucency, LibGLMaterial.EUndefined );
        }

        private Texture( Translucency aTranslucency, LibGLMaterial aMaterial )
        {
            this( aTranslucency, aMaterial, null );
        }

        private Texture( Translucency aTranslucency, LibGLMaterial aMaterial, Texture aMask )
        {
            iTexture = new LibGLTexture
            (
                ordinal(),
                aTranslucency,
                aMaterial,
                ( aMask == null ? null : new Integer( aMask.ordinal() ) )
            );
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

        public static final void loadImages()
        {
            for ( Texture texture : values() )
            {
                texture.loadImage();
            }
        }

        public final void loadImage()
        {
            //load all textures
            BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.ETextures.url + this.toString() + LibExtension.png.getSpecifier(), ShooterDebugSystem.glimage, false );
            iTextureImage = new LibGLImage( bufferedImage, ImageUsage.ETexture, ShooterDebugSystem.glimage, true );
        }

        public static final Texture getBulletHoleTextureForMaterial( LibGLMaterial material )
        {
            switch ( material )
            {
                case EUndefined:    {   return Texture.EBulletHoleSteel1;           }
                case EConcrete:     {   return Texture.EBulletHoleConcrete1;        }
                case EWood:         {   return Texture.EBulletHoleWood1;            }
                case ESteel:        {   return Texture.EBulletHoleSteel1;           }
                case EGlass:        {   return Texture.EBulletHoleGlass1;           }
            }

            return null;
        }

        public static final LibGLImage[] getAllTextureImages()
        {
            LibGLImage[] ret = new LibGLImage[ values().length ];

            for ( int i = 0; i < ret.length; ++i )
            {
                ret[ i ] = values()[ i ].iTextureImage;
            }

            return ret;
        }
    }
