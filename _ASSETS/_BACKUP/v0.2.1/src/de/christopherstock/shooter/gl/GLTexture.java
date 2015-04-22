/*  $Id: Texture.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.GLImage.*;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum GLTexture
    {
        ETest,
        EStones1,
        EBulletHoleConcrete,
        EOfficeWalls1(      GLMaterial.EConcrete        ),
        EBgDesert,
        EMeadowLight1,
        EWood1(             GLMaterial.EWood            ),
        EBricks1,
        ESand1,
        EBricksGrey1(       GLMaterial.EConcrete        ),
        EMeadowDark1,
        EBulletHoleSteel,
        EBulletHoleWood,
        EGlass1(            GLMaterial.EGlass           ),
        EWood2(             GLMaterial.EWood            ),
        EBulletHoleGlass,
        ELeather1,

        ;

        public          static      GLImage[]       textureImages       = null;

        private                     GLMaterial      material            = null;

        private GLTexture()
        {
            this( GLMaterial.EUndefined );
        }

        private GLTexture( GLMaterial aMaterial )
        {
            material = aMaterial;
        }

        public static final GLTexture getByName( String name )
        {
            if ( name == null ) return null;

            for ( GLTexture tex : values() )
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
            return GLImage.loadAll( values().length, Path.ETextures.url, ".png", ImageUsage.ETexture );
        }

        public final GLMaterial getMaterial()
        {
            return material;
        }
    }
