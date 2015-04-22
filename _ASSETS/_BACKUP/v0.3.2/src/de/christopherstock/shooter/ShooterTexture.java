/*  $Id: Texture.java 482 2011-03-24 20:10:25Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  java.awt.image.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLTexture.Translucency;
    import  de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public enum ShooterTexture
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

        ECarpet1,

        ECeiling1,

        EChrome1,

        EClothBlue1,
        EClothBlueYellowStripes1,
        EClothBlueStar1,
        EClothBlueSuitFront1,
        EClothBlueSuitTrousers1,

        EClothSecurity,
        EClothSecurityLogo,

        EClothCamouflageBlue,




        EShirtRed1,

        EConcrete1,

        EFaceFemale1EyesOpen,
        EFaceFemale1EyesShut,

        EFaceFemale2EyesOpen,
        EFaceFemale2EyesShut,

        EFaceFemale3EyesOpen,
        EFaceFemale3EyesShut,

        EFaceMale1EyesOpen,
        EFaceMale1EyesShut,




        EGlass1(                Translucency.EGlass,    LibGLMaterial.EGlass                               ),
        EGrass1,
        EGrass2,
        EGrass3,

        EHairBlonde,
        EHairBlack,
        EHairRed,

        EHand1,

        EItemHandset1,

        EJeansBlue1,


        EKeyboard1,

        ELeaf1,

        ELeather1,
        ELeather2,

        EMarble1,

        ECeramic1,

        EOfficeFloor1,

        EPlastic1,

        EScreen1,

        ESkin1,
        ESkin2,
        ESkin3,

        EShoeWhite1,
        EShoeBlack1,
        EShoeBrown1,
        EShoeBrownDark1,

        ESoil1,

        ETrousersBlack1,

        ETorsoSecurity,




        ERug1,
        ESand1,

        EStones1,
        ETest,

        EWallpaper1,
        EWallpaper2,

        EWearponShotgun,

        EWood1(                 Translucency.EOpaque,   LibGLMaterial.EWood                                ),
        EWood2(                 Translucency.EOpaque,   LibGLMaterial.EWood                                ),
        EWood3(                 Translucency.EOpaque,   LibGLMaterial.EWood                                ),
        ;

        public                      LibGLTexture        iTexture            = null;
        public                      LibGLImage          iTextureImage       = null;

        private ShooterTexture()
        {
            this( Translucency.EOpaque );
        }

        private ShooterTexture( Translucency translucency )
        {
            this( translucency, LibGLMaterial.EUndefined );
        }

        private ShooterTexture( Translucency aTranslucency, LibGLMaterial aMaterial )
        {
            this( aTranslucency, aMaterial, null );
        }

        private ShooterTexture( Translucency aTranslucency, LibGLMaterial aMaterial, ShooterTexture aMask )
        {
            iTexture = new LibGLTexture
            (
                ordinal(),
                aTranslucency,
                aMaterial,
                ( aMask == null ? null : new Integer( aMask.ordinal() ) )
            );
        }

        public static final ShooterTexture getByName( String name )
        {
            if ( name == null ) return null;

            for ( ShooterTexture tex : values() )
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
            for ( ShooterTexture texture : values() )
            {
                texture.loadImage();
            }
        }

        public final void loadImage()
        {
            //load all textures
            BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.ETextures.iUrl + this.toString() + LibExtension.jpg.getSpecifier(), ShooterDebug.glimage, false );
            iTextureImage = new LibGLImage( bufferedImage, ImageUsage.ETexture, ShooterDebug.glimage, true );
        }

        public static final ShooterTexture getBulletHoleTextureForMaterial( LibGLMaterial material )
        {
            switch ( material )
            {
                case EUndefined:    {   return ShooterTexture.EBulletHoleSteel1;           }
                case EConcrete:     {   return ShooterTexture.EBulletHoleConcrete1;        }
                case EWood:         {   return ShooterTexture.EBulletHoleWood1;            }
                case ESteel:        {   return ShooterTexture.EBulletHoleSteel1;           }
                case EGlass:        {   return ShooterTexture.EBulletHoleGlass1;           }
            }

            return null;
        }

        public static final LibGLImage[] getAllTextureImages()
        {
            Enum<ShooterTexture>[] allValues = values();

            LibGLImage[] ret = new LibGLImage[ allValues.length ];

            for ( int i = 0; i < ret.length; ++i )
            {
                ret[ i ] = values()[ i ].iTextureImage;
            }

            return ret;
        }
    }
