/*  $Id: LibFX.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.fx;

    import  de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   The effect system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public abstract class LibFX
    {
        public static enum FXSize
        {
            ESmall,
            EMedium,
            ELarge,
        }

        public static enum FXTime
        {
            EShort,
            EMedium,
            ELong,
        }

        public static enum FXType
        {
            EStaticDebugPoint,
            EExplosion,
            ESliver,
        }

        public static enum FXGravity
        {
            ELow,
            ENormal,
            EHigh,
        }

        @SuppressWarnings( "unused" )
        private     static  final   int                 MAX_FX                      = 0;

        protected                   LibVertex           iAnchor                     = null;
        protected                   FXSize              iSize                       = null;
        protected                   FXTime              iTime                       = null;
        protected                   int                 iLifetime                   = 0;

        public LibFX( LibVertex aAnchor, int aLifetime )
        {
            iAnchor = aAnchor;
            iLifetime = aLifetime;
        }
    }
