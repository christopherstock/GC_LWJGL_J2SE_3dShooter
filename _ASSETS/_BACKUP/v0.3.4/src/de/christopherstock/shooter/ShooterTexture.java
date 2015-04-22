/*  $Id: ShooterTexture.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
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
    *   @version    0.3.4
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
         *  Leave this class on 1st position! make class Mask!
         */

        public static enum BulletHole implements Tex
        {
            EBulletHoleGlass1mask,
            EBulletHoleConcrete1,
            EFence1mask,
            ETree1mask,
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

        public static enum Default implements Tex
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

            EFence1(                        Translucency.EHasMask,    LibGLMaterial.EUndefined,     BulletHole.EFence1mask          ),
            ETree1(                         Translucency.EHasMask,    LibGLMaterial.EUndefined,     BulletHole.ETree1mask           ),

            EForest1,

            EGlass1(                        Translucency.EGlass,    LibGLMaterial.EGlass                                            ),

            EGrass1,
            EGrass2,
            EGrass3,

            EItemHandset1,

            EJeansBlue1(                    Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                                       ),


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

            EShirtRed1(                     Translucency.EOpaque,   LibGLMaterial.EHumanFlesh                            ),

            ESign1,
            ESign2,

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

            EWood1(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood2(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            EWood3(                         Translucency.EOpaque,   LibGLMaterial.EWood                                ),
            ;

            private                     TexObject                   iTexObject          = null;

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

        public static enum Wall implements Tex
        {
            ;

            private                     TexObject                   iTexObject          = null;

            private Wall()
            {
                this( Translucency.EOpaque );
            }

            private Wall( Translucency translucency )
            {
                this( translucency, LibGLMaterial.EUndefined );
            }

            private Wall( Translucency aTranslucency, LibGLMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
            }

            private Wall( Translucency aTranslucency, LibGLMaterial aMaterial, Tex aMask )
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
            EBlue1(),
            EBlueYellowStripes1(),
            EBlueStar1(),
            EBlueSuitFront1(),
            EBlueSuitTrousers1(),
            ESecurity(),
            ESecurityLogo(),
            ECamouflageBlue(),

            ESkin1(),
            ESkin2(),
            ESkin3(),

            EFaceFemale1EyesOpen(),
            EFaceFemale1EyesShut(),
            EFaceFemale2EyesOpen(),
            EFaceFemale2EyesShut(),
            EFaceFemale3EyesOpen(),
            EFaceFemale3EyesShut(),
            EFaceMale1EyesOpen(),
            EFaceMale1EyesShut(),

            EHairBlonde(),
            EHairBlack(),
            EHairRed(),

            EHand1(),

            EShoeWhite1(),
            EShoeBlack1(),
            EShoeBrown1(),
            EShoeBrownDark1(),
            ;

            private                     TexObject                   iTexObject          = null;

            private BotTex()
            {
                this( Translucency.EOpaque, LibGLMaterial.EHumanFlesh );
            }

            private BotTex( Translucency aTranslucency, LibGLMaterial aMaterial )
            {
                this( aTranslucency, aMaterial, null );
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

            for ( Default tex : Default.values() )
            {
                if ( tex.name().compareToIgnoreCase( name ) == 0 )
                {
                    return tex;
                }
            }
            for ( Wall tex : Wall.values() )
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
            for ( BulletHole tex : BulletHole.values() )
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
            for ( BulletHole texture : BulletHole.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesBulletHole.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            for ( Default texture : Default.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesDefault.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            for ( BotTex texture : BotTex.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesBot.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            for ( ItemTex texture : ItemTex.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesItem.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
            }
            for ( Wall texture : Wall.values() )
            {
                texture.loadImage( ShooterSettings.Path.ETexturesWall.iUrl + texture.toString() + LibExtension.jpg.getSpecifier() );
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

            for ( BulletHole b : BulletHole.values() )
            {
                ret.addElement( b.getTextureImage() );
            }
            for ( Default d : Default.values() )
            {
                ret.addElement( d.getTextureImage() );
            }
            for ( BotTex c : BotTex.values() )
            {
                ret.addElement( c.getTextureImage() );
            }            for ( Wall w : Wall.values() )
            {
                ret.addElement( w.getTextureImage() );
            }
            for ( ItemTex b : ItemTex.values() )
            {
                ret.addElement( b.getTextureImage() );
            }
            for ( Wall w : Wall.values() )
            {
                ret.addElement( w.getTextureImage() );
            }

            return ret.toArray( new LibGLImage[] {} );
        }
    }
