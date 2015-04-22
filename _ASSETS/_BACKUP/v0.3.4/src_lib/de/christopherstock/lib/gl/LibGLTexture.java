/*  $Id: LibGLTexture.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    /**************************************************************************************
    *   The Texture-System.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class LibGLTexture
    {
        public static enum Translucency
        {
            EGlass,
            EOpaque,
            EHasMask,
            ;
        }

        private     static          int                 freeID              = 0;

        private                     int                 id                  = 0;
        private                     Translucency        translucency        = null;
        private                     LibGLMaterial       material            = null;
        private                     Integer             maskId              = null;

        public LibGLTexture( int aId, Translucency aTranslucency, LibGLMaterial aMaterial, Integer aMaskId )
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

        public final LibGLMaterial getMaterial()
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
