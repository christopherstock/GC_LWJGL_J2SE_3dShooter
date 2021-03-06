/*  $Id: ShooterTexture.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
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
import de.christopherstock.shooter.g3d.wall.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class ShooterTexture
    {
        public static final class TexObject
        {
            public                      LibGLTexture        iTexture            = null;
            public                      LibGLImage          iTextureImage       = null;

            protected TexObject( Translucency aTranslucency, ShooterMaterial aMaterial, Tex aMask )
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
            EMaskPlant1,
            EMaskPlant2,
            EMaskTree1,
            EMaskSliver1,
            ;

            private                     TexObject                   iTexObject          = null;

            private Mask()
            {
                this( Translucency.EOpaque );
            }

            private Mask( Translucency translucency )
            {
                this( translucency, ShooterMaterial.EUndefined );
            }

            private Mask( Translucency aTranslucency, ShooterMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private Mask( Translucency aTranslucency, ShooterMaterial aMaterial, Tex aMask )
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
            EBulletHoleBrownBricks1,
            EBulletHoleGlass1mask,
            EBulletHoleGlass1(            Translucency.EHasMaskBulletHole,  ShooterMaterial.EUndefined,  EBulletHoleGlass1mask   ),
            EBulletHoleConcrete1,
            EBulletHolePlastic1,
            EBulletHoleSteel1,
            EBulletHoleSteel2,
            EBulletHoleWood1,
            ;

            private                     TexObject                   iTexObject          = null;

            private BulletHole()
            {
                this( Translucency.EOpaque );
            }

            private BulletHole( Translucency translucency )
            {
                this( translucency, ShooterMaterial.EUndefined );
            }

            private BulletHole( Translucency aTranslucency, ShooterMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private BulletHole( Translucency aTranslucency, ShooterMaterial aMaterial, Tex aMask )
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
            EBlackMetal1(                   ShooterMaterial.ESteel1                                                               ),
            EBricks1(                       ShooterMaterial.EBrownBricks                                                          ),
            EBricks2(                       ShooterMaterial.ERedBricks                                                            ),
            ECarpet1,
            EChrome1(                       ShooterMaterial.ESteel1                                                               ),
            EChrome2(                       ShooterMaterial.ESteel2                                                               ),
            ECeiling1,
            EClothDarkRed,
            EConcrete1,
            ECrate1,
            EGlass1(                        Translucency.EGlass,    ShooterMaterial.EGlass                                        ),
            EGrass1,
            EKeyboard1(                     ShooterMaterial.EPlastic1                                                             ),
            ELeather1,
            EMarble1,
            EMarble2,
            EPlant1(                        Translucency.EHasMask,  ShooterMaterial.EUndefined,   Mask.EMaskPlant1                ),
            EPlant2(                        Translucency.EHasMask,  ShooterMaterial.EUndefined,   Mask.EMaskPlant2                ),
            EPlastic1(                      ShooterMaterial.EPlastic1                                                             ),
            EPoster1(                       ShooterMaterial.EPlastic1                                                             ),
            EPoster2(                       ShooterMaterial.EPlastic1                                                             ),
            ERingBook1,
            EScreen1(                       ShooterMaterial.EGlass                                        ),
            EScreen2(                       ShooterMaterial.EGlass                                        ),
            ESliver1(                       Translucency.EHasMask,  ShooterMaterial.EUndefined,   Mask.EMaskSliver1               ),
            ESodaMachine1(                  ShooterMaterial.EGlass                                        ),
            ESodaMachine2(                  ShooterMaterial.EGlass                                        ),
            ESodaMachine3(                  ShooterMaterial.EGlass                                        ),
            ETest,
            ETest2,
            ETest3,
            ETree1(                         Translucency.EHasMask,  ShooterMaterial.EUndefined,   Mask.EMaskTree1                 ),
            EWallpaper4,
            EWhiteboard1(                   ShooterMaterial.EPlastic1                                                             ),
            EWood1(                         ShooterMaterial.EWood                                                                 ),
/*
            ECactus1,
            ECactus2,
            ECar1,
            ECar2,
            ECarpet1,
            ECeiling1,
            ECeramic1,
            EContainer1,
            EContainer2,
            ECrop1,
            EFence1(                        Translucency.EHasMask,    LibGLMaterial.EUndefined,     Mask.EMaskFence1                ),
            EForest1,
            EGrass1,
            EGrass2,
            EGrass3,
            EHouse1,
            EHouse2,
            EHouse3,
            EHouse4,
            EHouse5,
            EHouse6,
            EHouse7,
            EHouse8,
            EHouse9,
            EJeansBlue1(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                                       ),
            EKeyboard1,
            ELeaf1,
            ELeather1,
            ELeather2,
            EMarble1,
            EMetal1,
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
            EWater1,
            EWallpaper1(                    Translucency.EOpaque,   LibGLMaterial.EConcrete                            ),
            EWallpaper2(                    Translucency.EOpaque,   LibGLMaterial.EConcrete                            ),
            EWallpaper3,
            EWood1(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood2(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood3(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood4(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
*/
            ;

            private                     TexObject                   iTexObject          = null;

            private WallTex()
            {
                this( Translucency.EOpaque, ShooterMaterial.EUndefined, null );
            }

            private WallTex( ShooterMaterial aMaterial )
            {
                this( Translucency.EOpaque, aMaterial, null );
            }

            private WallTex( Translucency aTranslucency, ShooterMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private WallTex( Translucency aTranslucency, ShooterMaterial aMaterial, Tex aMask )
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
            EClothBlue,
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

            EFaceMale2RoseEyesOpen,
            EFaceMale2RoseEyesShut,
            EFaceMale2BrownEyesOpen,
            EFaceMale2BrownEyesShut,
            EFaceMale2BlackEyesOpen,
            EFaceMale2BlackEyesShut,
            EFaceMale2LightBrownEyesOpen,
            EFaceMale2LightBrownEyesShut,

            EFaceMale2YellowEyesOpen,
            EFaceMale2YellowEyesShut,

            EFaceMale3LightBrownEyesOpen,

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
            ETorsoUpperChemise3,
            ETorsoUpperChemise4,
            ;

            private                     TexObject                   iTexObject          = null;

            private BotTex()
            {
                this( Translucency.EOpaque, ShooterMaterial.EHumanFlesh, null );
            }

            private BotTex( Translucency aTranslucency, ShooterMaterial aMaterial, Tex aMask )
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
            EAmmoShotgunShell,
            EAmmoBullet9mm,
            EAmmoBullet51mm,
            EAmmoBullet792mm,
            EAmmoBullet44mm,
            EAmmoBulletMagnum,
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
                this( translucency, ShooterMaterial.EUndefined );
            }

            private ItemTex( Translucency aTranslucency, ShooterMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private ItemTex( Translucency aTranslucency, ShooterMaterial aMaterial, Tex aMask )
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
            ShooterDebug.init.out( "initUi 5.0" );
            for ( Mask texture : Mask.values() )
            {
                ShooterDebug.init.out( "> load [" + ShooterSettings.Path.ETexturesMask.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() + "]" );
                texture.loadImage( ShooterSettings.Path.ETexturesMask.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            ShooterDebug.init.out( "initUi 5.2" );
            for ( BulletHole texture : BulletHole.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesBulletHole.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            ShooterDebug.init.out( "initUi 5.3" );
            for ( WallTex texture : WallTex.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesWall.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            ShooterDebug.init.out( "initUi 5.4" );
            for ( BotTex texture : BotTex.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesBot.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            ShooterDebug.init.out( "initUi 5.5" );
            for ( ItemTex texture : ItemTex.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesItem.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
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
