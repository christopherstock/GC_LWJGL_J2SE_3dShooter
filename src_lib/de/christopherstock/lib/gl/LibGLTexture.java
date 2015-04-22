/*  $Id: LibGLTexture.java 1238 2013-01-02 13:39:56Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  java.io.Serializable;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public class LibGLTexture implements Serializable
    {
        private     static  final   long                serialVersionUID    = -9176705157906845259L;

        public static enum Translucency
        {
            EGlass,
            EOpaque,
            EHasMask,
            EHasMaskBulletHole,
            ;
        }

        private     static          int                 freeID              = 0;

        private                     int                 id                  = 0;
        private                     Translucency        translucency        = null;
        private                     LibMaterial         material            = null;
        private                     Integer             maskId              = null;

        public LibGLTexture( int aId, Translucency aTranslucency, LibMaterial aMaterial, Integer aMaskId )
        {
            id              = aId;
            translucency    = aTranslucency;
            material        = aMaterial;
            maskId          = aMaskId;
        }

        public final int getId()
        {
            return id;
        }

        public final LibMaterial getMaterial()
        {
            return material;
        }

        public final Translucency getTranslucency()
        {
            return translucency;
        }

        public final int getMaskId()
        {
            return maskId.intValue();
        }

        public static final int getNextFreeID()
        {
            return freeID++;
        }
    }
