/*  $Id: Texture.java 194 2010-12-13 22:37:04Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.util.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.GLImage.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum Texture
    {
        EBulletHoleGlass1mask,

        EBulletHoleConcrete1,
        EBulletHoleSteel1,
        EBulletHoleWood1,
        EBulletHoleGlass1(      Translucency.EHasMask,  GLMaterial.EUndefined,  EBulletHoleGlass1mask   ),
        EBricks1(               Translucency.EOpaque,   GLMaterial.ESteel                               ),
        EBricks2(               Translucency.EOpaque,   GLMaterial.EConcrete                            ),
        ECar1,
        ECar2,
        ECar3,
        EChrome1,
        EConcrete1,
        EGlass1(                Translucency.EGlass,    GLMaterial.EGlass                               ),
        EGrass1,
        EGrass2,
        EGrass3,

        EKeyboard1,

        ELeaf1,
        ECeramic1,

        EPlastic1,

        EScreen1,

        ESoil1,

        EBoxes1,


        ELeather1,
        EWallpaper1,
        ERug1,
        ESand1,
        EStones1,
        ETest,
        EWood1(                 Translucency.EOpaque,   GLMaterial.EWood                                ),
        EWood2(                 Translucency.EOpaque,   GLMaterial.EWood                                ),
        EWood3(                 Translucency.EOpaque,   GLMaterial.EWood                                ),
        ;

        public static enum Translucency
        {
            EGlass,
            EOpaque,
            EHasMask,
            ;
        }

        public          static      GLImage[]       textureImages       = null;

        private                     Translucency    translucency        = null;
        private                     GLMaterial      material            = null;
        private                     Texture         mask                = null;

        private Texture()
        {
            this( Translucency.EOpaque );
        }

        private Texture( Translucency translucency )
        {
            this( translucency, GLMaterial.EUndefined );
        }

        private Texture( Translucency aTranslucency, GLMaterial aMaterial )
        {
            this ( aTranslucency, aMaterial, null );
        }

        private Texture( Translucency aTranslucency, GLMaterial aMaterial, Texture aMask )
        {
            translucency    = aTranslucency;
            material        = aMaterial;
            mask            = aMask;
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

        public static final GLImage[] loadTextureImages()
        {
            return GLImage.loadAllEnumNames( new Vector<Object>( Arrays.asList( values() ) ), Path.ETextures.url, ".png", ImageUsage.ETexture );
        }

        public final GLMaterial getMaterial()
        {
            return material;
        }

        public final Translucency getTranslucency()
        {
            return translucency;
        }

        public final Texture getMask()
        {
            return mask;
        }
    }
