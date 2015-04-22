/*  $Id: ShooterTexture.java 633 2011-04-23 23:33:09Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  java.awt.image.*;
    import  java.util.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLTexture.Translucency;
    import  de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.game.fx.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class ShooterTexture
    {
        public static abstract interface Tex
        {
            public abstract LibGLTexture getTexture();
        }

        public static enum Default implements Tex
        {
            EBulletHoleGlass1mask,
            EBulletHoleConcrete1,
            EBulletHoleSteel1,
            EBulletHoleWood1,
            EBulletHoleGlass1(              Translucency.EHasMask,  LibGLMaterial.EUndefined,  EBulletHoleGlass1mask   ),

            EAmmoShotgunShells,
            EAmmoBullet9mm,

            EBoxes1,

            EBricks1(                       Translucency.EOpaque,   LibGLMaterial.ERedBricks                            ),
            EBricks2(                       Translucency.EOpaque,   LibGLMaterial.ERedBricks                            ),

            EBlackMetal1,

            ECar1,
            ECar2,
            ECar3,

            ECarpet1,

            ECeiling1,

            EContainer1,
            EContainer2,

            EChrome1,

            EShirtRed1(                     Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),

            EConcrete1,

            EFaceFemale1EyesOpen(           Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EFaceFemale1EyesShut(           Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EFaceFemale2EyesOpen(           Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EFaceFemale2EyesShut(           Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EFaceFemale3EyesOpen(           Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EFaceFemale3EyesShut(           Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EFaceMale1EyesOpen(             Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EFaceMale1EyesShut(             Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),

            EGlass1(                        Translucency.EGlass,    LibGLMaterial.EGlass                               ),

            EGrass1,
            EGrass2,
            EGrass3,

            EHairBlonde(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EHairBlack(                     Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EHairRed(                       Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),

            EHand1(                         Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),

            EItemHandset1,

            EJeansBlue1(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),


            EKeyboard1,

            ELeaf1,

            ELeather1,
            ELeather2,

            EMarble1,
            EMetal1,

            ECeramic1,

            EOfficeFloor1,

            EPlastic1,
            EPlastic2,

            EScreen1,
            ESign1,

            ESkin1(                         Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            ESkin2(                         Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            ESkin3(                         Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EShoeWhite1(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EShoeBlack1(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EShoeBrown1(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EShoeBrownDark1(                Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),

            ESoil1,

            ETrousersBlack1(                Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            ETorsoSecurity(                 Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),



            ERoad1,
            ERoad2,


            ERug1,
            ESand1,

            EStones1,
            ETest,

            EWallpaper1,
            EWallpaper2,

            EWearponShotgun,

            EWood1(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood2(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood3(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            ;

            public                      LibGLTexture        iTexture            = null;
            public                      LibGLImage          iTextureImage       = null;

            private Default()
            {
                this( Translucency.EOpaque );
            }

            private Default( Translucency translucency )
            {
                this( translucency, LibGLMaterial.EUndefined );
            }

            private Default( Translucency aTranslucency, LibGLMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private Default( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
            {
                iTexture = new LibGLTexture
                (
                    LibGLTexture.getNextFreeID(),
                    aTranslucency,
                    aMaterial,
                    ( aMask == null ? null : new Integer( aMask.getTexture().getId() ) )
                );
            }

            public final void loadImage()
            {
                //load all textures
                BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.ETexturesDefault.iUrl + this.toString() + LibExtension.jpg.getSpecifier(), ShooterDebug.glimage, false );
                iTextureImage = new LibGLImage( bufferedImage, ImageUsage.ETexture, ShooterDebug.glimage, true );
            }

            @Override
            public LibGLTexture getTexture()
            {
                return iTexture;
            }
        }

        public static enum Cloth implements Tex
        {
            EClothBlue1(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EClothBlueYellowStripes1(       Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EClothBlueStar1(                Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EClothBlueSuitFront1(           Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EClothBlueSuitTrousers1(        Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EClothSecurity(                 Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EClothSecurityLogo(             Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            EClothCamouflageBlue(           Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),
            ;

            public                      LibGLTexture        iTexture            = null;
            public                      LibGLImage          iTextureImage       = null;

            private Cloth()
            {
                this( Translucency.EOpaque );
            }

            private Cloth( Translucency translucency )
            {
                this( translucency, LibGLMaterial.EUndefined );
            }

            private Cloth( Translucency aTranslucency, LibGLMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private Cloth( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
            {
                iTexture = new LibGLTexture
                (
                    LibGLTexture.getNextFreeID(),
                    aTranslucency,
                    aMaterial,
                    ( aMask == null ? null : new Integer( aMask.getTexture().getId() ) )
                );
            }

            public final void loadImage()
            {
                //load all textures
                BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.ETexturesCloth.iUrl + this.toString() + LibExtension.jpg.getSpecifier(), ShooterDebug.glimage, false );
                iTextureImage = new LibGLImage( bufferedImage, ImageUsage.ETexture, ShooterDebug.glimage, true );
            }

            @Override
            public LibGLTexture getTexture()
            {
                return iTexture;
            }
        }

        public static final Tex getByName( String name )
        {
            if ( name == null ) return null;

            for ( Default tex : Default.values() )
            {
                if ( tex.name().compareToIgnoreCase( name ) == 0 )
                {
                    return tex;
                }
            }
            for ( Cloth tex : Cloth.values() )
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
            for ( Default texture : Default.values() )
            {
                texture.loadImage();
            }
            for ( Cloth texture : Cloth.values() )
            {
                texture.loadImage();
            }
        }

        public static final Tex getBulletHoleTextureForMaterial( LibGLMaterial material )
        {
            switch ( material )
            {
                case EUndefined:    {   return Default.EBulletHoleSteel1;            }
                case EConcrete:     {   return Default.EBulletHoleConcrete1;         }
                case ERedBricks:    {   return Default.EBulletHoleConcrete1;         }
                case EWood:         {   return Default.EBulletHoleWood1;             }
                case ESteel:        {   return Default.EBulletHoleSteel1;            }
                case EGlass:        {   return Default.EBulletHoleGlass1;            }
                case EHumanFlesh:   {   return Default.EBulletHoleSteel1;            }   // never invoked
            }

            return null;
        }

        public static final LibColors[] getSliverColorsForMaterial( LibGLMaterial material )
        {
            switch ( material )
            {
                case EUndefined:    {   return FXSliver.SLIVER_COLOR_WALL;          }
                case EConcrete:     {   return FXSliver.SLIVER_COLOR_WALL;          }
                case EWood:         {   return FXSliver.SLIVER_COLOR_WALL;          }
                case ESteel:        {   return FXSliver.SLIVER_COLOR_WALL;          }
                case EGlass:        {   return FXSliver.SLIVER_COLOR_GLASS;    }
                case ERedBricks:    {   return FXSliver.SLIVER_COLOR_RED_BRICKS;    }
                case EHumanFlesh:   {   return FXSliver.SLIVER_COLOR_BLOOD;         }
            }

            return null;
        }

        public static final LibGLImage[] getAllTextureImages()
        {
            Vector<LibGLImage> ret = new Vector<LibGLImage>();

            for ( Default d : Default.values() )
            {
                ret.addElement( d.iTextureImage );
            }
            for ( Cloth c : Cloth.values() )
            {
                ret.addElement( c.iTextureImage );
            }

            return ret.toArray( new LibGLImage[] {} );
        }
    }
