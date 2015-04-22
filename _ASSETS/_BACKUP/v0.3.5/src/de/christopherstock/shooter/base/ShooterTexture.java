/*  $Id: ShooterTexture.java 733 2011-05-13 23:16:18Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  java.awt.image.*;
    import  java.util.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLTexture.Translucency;
    import  de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.fx.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class ShooterTexture
    {
        public static final class TexObject
        {
            public                      LibGLTexture        iTexture            = null;
            public                      LibGLImage          iTextureImage       = null;

            protected TexObject( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
            {
                iTexture = new LibGLTexture
                (
                    LibGLTexture.getNextFreeID(),
                    aTranslucency,
                    aMaterial,
                    ( aMask == null ? null : new Integer( aMask.getTexture().getId() ) )
                );
            }

            public final void loadImage( String url )
            {
                //load all textures
                BufferedImage bufferedImage = LibImage.load( url, ShooterDebug.glimage, false );
                iTextureImage = new LibGLImage( bufferedImage, ImageUsage.ETexture, ShooterDebug.glimage, true );
            }
        }

        public static abstract interface Tex
        {
            public abstract LibGLTexture getTexture();
            public abstract LibGLImage getTextureImage();
            public abstract void loadImage( String url );
        }

        /*
         *  Leave this class on 1st position!
         */
        public static enum Mask implements Tex
        {
            EMaskFence1,
            EMaskTree1,
            ;

            private                     TexObject                   iTexObject          = null;

            private Mask()
            {
                this( Translucency.EOpaque );
            }

            private Mask( Translucency translucency )
            {
                this( translucency, LibGLMaterial.EUndefined );
            }

            private Mask( Translucency aTranslucency, LibGLMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private Mask( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
            {
                iTexObject = new TexObject( aTranslucency, aMaterial, aMask );
            }

            @Override
            public final void loadImage( String url )
            {
                iTexObject.loadImage( url );
            }

            @Override
            public LibGLTexture getTexture()
            {
                return iTexObject.iTexture;
            }

            @Override
            public final LibGLImage getTextureImage()
            {
                return iTexObject.iTextureImage;
            }
        }

        public static enum BulletHole implements Tex
        {
            EBulletHoleGlass1mask,
            EBulletHoleConcrete1,
            EBulletHoleSteel1,
            EBulletHoleWood1,
            EBulletHoleGlass1(            Translucency.EHasMask,  LibGLMaterial.EUndefined,  EBulletHoleGlass1mask   ),
            ;

            private                     TexObject                   iTexObject          = null;

            private BulletHole()
            {
                this( Translucency.EOpaque );
            }

            private BulletHole( Translucency translucency )
            {
                this( translucency, LibGLMaterial.EUndefined );
            }

            private BulletHole( Translucency aTranslucency, LibGLMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private BulletHole( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
            {
                iTexObject = new TexObject( aTranslucency, aMaterial, aMask );
            }

            @Override
            public final void loadImage( String url )
            {
                iTexObject.loadImage( url );
            }

            @Override
            public LibGLTexture getTexture()
            {
                return iTexObject.iTexture;
            }

            @Override
            public final LibGLImage getTextureImage()
            {
                return iTexObject.iTextureImage;
            }
        }

        public static enum WallTex implements Tex
        {
            EBoxes1,

            EBricks1(                       Translucency.EOpaque,   LibGLMaterial.ERedBricks                            ),
            EBricks2(                       Translucency.EOpaque,   LibGLMaterial.ERedBricks                            ),

            EBlackMetal1,

            ECactus1,
            ECactus2,

            ECar1,
            ECar2,

            ECarpet1,

            ECeiling1,

            EContainer1,
            EContainer2,

            EChrome1,

            EConcrete1,

            ECrop1,

            EFence1(                        Translucency.EHasMask,    LibGLMaterial.EUndefined,     Mask.EMaskFence1                ),
            ETree1(                         Translucency.EHasMask,    LibGLMaterial.EUndefined,     Mask.EMaskTree1                 ),

            EForest1,

            EGlass1(                        Translucency.EGlass,    LibGLMaterial.EGlass                                            ),

            EGrass1,
            EGrass2,
            EGrass3,

            EJeansBlue1(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                                       ),


            EKeyboard1,


            ELeaf1,

            ELeather1,
            ELeather2,

            EMarble1,
            EMetal1,

            ECeramic1,





            EWallpaper3,





            EPlastic1,
            EPlastic2,

            EScreen1,

            EShirtRed1(                     Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),

            ESign1,
            ESign2,

            ESoil1,

            ERoad1,
            ERoad2,

            ERug1,
            ESand1,

            EStones1,
            ETest,

            EWallpaper1,
            EWallpaper2,

            EWood1(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood2(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood3(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            ;

            private                     TexObject                   iTexObject          = null;

            private WallTex()
            {
                this( Translucency.EOpaque );
            }

            private WallTex( Translucency translucency )
            {
                this( translucency, LibGLMaterial.EUndefined );
            }

            private WallTex( Translucency aTranslucency, LibGLMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private WallTex( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
            {
                iTexObject = new TexObject( aTranslucency, aMaterial, aMask );
            }

            @Override
            public final void loadImage( String url )
            {
                iTexObject.loadImage( url );
            }

            @Override
            public LibGLTexture getTexture()
            {
                return iTexObject.iTexture;
            }

            @Override
            public final LibGLImage getTextureImage()
            {
                return iTexObject.iTextureImage;
            }
        }

        public static enum BotTex implements Tex
        {
            EClothBlue1,
            EClothBlueYellowStripes1,
            EClothBlueStar1,
            EClothBlack,
            EClothChemise1,
            EClothChemise2,
            ETorsoUpperSuiteGrey1,
            EClothCamouflageBlue,
            EClothSecurity,
            EClothSecurityBadge,

            EHairBlonde,
            EHairBlack,
            EHairRed,
            EHairLightBrown,
            EHairAshBlonde,

            EHand1,

            EFaceFemale1RoseEyesOpen,
            EFaceFemale1RoseEyesShut,
            EFaceFemale1BrownEyesOpen,
            EFaceFemale1BlackEyesOpen,
            EFaceFemale1BlackEyesShut,
            EFaceFemale1LightBrownEyesOpen,
            EFaceFemale1LightBrownEyesShut,
            EFaceFemale1BrownEyesShut,
            EFaceFemale2RoseEyesOpen,
            EFaceFemale2RoseEyesShut,
            EFaceFemale2BrownEyesOpen,
            EFaceFemale2BrownEyesShut,
            EFaceFemale2LightBrownEyesOpen,
            EFaceFemale2LightBrownEyesShut,
            EFaceFemale2BlackEyesOpen,
            EFaceFemale2BlackEyesShut,
            EFaceFemale3BlackEyesOpen,
            EFaceFemale3BlackEyesShut,
            EFaceFemale3LightBrownEyesOpen,
            EFaceFemale3LightBrownEyesShut,
            EFaceFemale3BrownEyesOpen,
            EFaceFemale3BrownEyesShut,
            EFaceFemale3RoseEyesOpen,
            EFaceFemale3RoseEyesShut,
            EFaceFemale4YellowEyesOpen,
            EFaceFemale4YellowEyesShut,

            EFaceMale1RoseEyesOpen,
            EFaceMale1RoseEyesShut,
            EFaceMale1BrownEyesOpen,
            EFaceMale1BrownEyesShut,
            EFaceMale1BlackEyesOpen,
            EFaceMale1BlackEyesShut,
            EFaceMale1LightBrownEyesOpen,
            EFaceMale1LightBrownEyesShut,
            EFaceMale2YellowEyesOpen,
            EFaceMale2YellowEyesShut,

            EShoeWhite1,
            EShoeBlack1,
            EShoeBrown1,
            EShoeBrownDark1,

            ESkinRose,
            ESkinLightBrown,
            ESkinBrown,
            ESkinBlack,
            ESkinYellow,

            ETorsoLowerBlackTrousers,
            ETorsoLowerBlueSuite,

            ETorsoUpperSecurity,
            ETorsoUpperBlueSuite,
            ETorsoUpperChemise1,
            ETorsoUpperChemise2,

            ;

            private                     TexObject                   iTexObject          = null;

            private BotTex()
            {
                this( Translucency.EOpaque, LibGLMaterial.EHumanFlesh, null );
            }

            private BotTex( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
            {
                iTexObject = new TexObject( aTranslucency, aMaterial, aMask );
            }

            @Override
            public final void loadImage( String url )
            {
                iTexObject.loadImage( url );
            }

            @Override
            public final LibGLImage getTextureImage()
            {
                return iTexObject.iTextureImage;
            }

            @Override
            public LibGLTexture getTexture()
            {
                return iTexObject.iTexture;
            }
        }

        public static enum ItemTex implements Tex
        {
            EAmmoShotgunShells,
            EAmmoBullet9mm,
            EAmmoBullet51mm,
            EAmmoBullet792mm,
            EAmmoBullet44mm,
            EAmmoMagnumBullet,
            ECrackers,
            EKnife,
            EMauzer,
            EMg34,
            EApple,
            ;

            private                     TexObject                   iTexObject          = null;

            private ItemTex()
            {
                this( Translucency.EOpaque );
            }

            private ItemTex( Translucency translucency )
            {
                this( translucency, LibGLMaterial.EUndefined );
            }

            private ItemTex( Translucency aTranslucency, LibGLMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private ItemTex( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
            {
                iTexObject = new TexObject( aTranslucency, aMaterial, aMask );
            }

            @Override
            public final void loadImage( String url )
            {
                iTexObject.loadImage( url );
            }

            @Override
            public LibGLTexture getTexture()
            {
                return iTexObject.iTexture;
            }

            @Override
            public final LibGLImage getTextureImage()
            {
                return iTexObject.iTextureImage;
            }
        }

        public static final Tex getByName( String name )
        {
            if ( name == null ) return null;

            for ( Mask tex : Mask.values() )
            {
                if ( tex.name().compareToIgnoreCase( name ) == 0 )
                {
                    return tex;
                }
            }
            for ( BulletHole tex : BulletHole.values() )
            {
                if ( tex.name().compareToIgnoreCase( name ) == 0 )
                {
                    return tex;
                }
            }
            for ( WallTex tex : WallTex.values() )
            {
                if ( tex.name().compareToIgnoreCase( name ) == 0 )
                {
                    return tex;
                }
            }
            for ( BotTex tex : BotTex.values() )
            {
                if ( tex.name().compareToIgnoreCase( name ) == 0 )
                {
                    return tex;
                }
            }
            for ( ItemTex tex : ItemTex.values() )
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
            for ( Mask texture : Mask.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesMask.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            for ( BulletHole texture : BulletHole.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesBulletHole.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            for ( WallTex texture : WallTex.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesWall.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            for ( BotTex texture : BotTex.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesBot.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            for ( ItemTex texture : ItemTex.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesItem.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
        }

        public static final Tex getBulletHoleTextureForMaterial( LibGLMaterial material )
        {
            switch ( material )
            {
                case EUndefined:    {   return BulletHole.EBulletHoleSteel1;            }
                case EConcrete:     {   return BulletHole.EBulletHoleConcrete1;         }
                case ERedBricks:    {   return BulletHole.EBulletHoleConcrete1;         }
                case EWood:         {   return BulletHole.EBulletHoleWood1;             }
                case ESteel:        {   return BulletHole.EBulletHoleSteel1;            }
                case EGlass:        {   return BulletHole.EBulletHoleGlass1;            }
                case EHumanFlesh:   {   return BulletHole.EBulletHoleSteel1;            }   // never invoked
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

            for ( Mask m : Mask.values() )
            {
                ret.addElement( m.getTextureImage() );
            }
            for ( BulletHole b : BulletHole.values() )
            {
                ret.addElement( b.getTextureImage() );
            }
            for ( WallTex w : WallTex.values() )
            {
                ret.addElement( w.getTextureImage() );
            }
            for ( BotTex c : BotTex.values() )
            {
                ret.addElement( c.getTextureImage() );
            }
            for ( ItemTex b : ItemTex.values() )
            {
                ret.addElement( b.getTextureImage() );
            }


            return ret.toArray( new LibGLImage[] {} );
        }
    }
