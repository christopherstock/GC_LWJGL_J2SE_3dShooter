/*  $Id: FXSliver.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.ShooterSettings.FxSettings;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class FXSliver extends FX
    {
        public static enum ParticleQuantity
        {
            ETiny,
            ELow,
            EMedium,
            EHigh,
            EMassive,
            ;
        }

        public      static  final   LibColors[]         SLIVER_COLOR_WALL           = new LibColors[]
        {
            LibColors.EExplosion1,  LibColors.EExplosion2,  LibColors.EExplosion3,
            LibColors.EExplosion4,  LibColors.EExplosion5,  LibColors.EExplosion6,
            LibColors.EExplosion7,  LibColors.EExplosion8,  LibColors.EExplosion9,
            LibColors.EExplosion10, LibColors.EExplosion11, LibColors.EExplosion12,
        };

        public      static  final   LibColors[]         SLIVER_COLOR_RED_BRICKS     = new LibColors[]
        {
            LibColors.ESliverBricks1,  LibColors.ESliverBricks2,  LibColors.ESliverBricks3,
            LibColors.ESliverBricks4,  LibColors.ESliverBricks5,
        };

        public      static  final   LibColors[]         SLIVER_COLOR_BLOOD          = new LibColors[]
        {
            LibColors.ESliverBlood1,  LibColors.ESliverBlood2,  LibColors.ESliverBlood3,
        };

        public      static  final   LibColors[]         SLIVER_COLOR_GLASS          = new LibColors[]
        {
            LibColors.ESliverGlass1,  LibColors.ESliverGlass2,  LibColors.ESliverGlass3,
        };



        public                      LibColors[]         iSliverColors               = null;
        public                      int                 iParticlesToLaunch          = 0;

        public FXSliver( LibVertex aAnchor, LibColors[] aSliverColors, ParticleQuantity particleQuantity )
        {
            super( aAnchor );
            iSliverColors = aSliverColors;

            switch ( particleQuantity )
            {
                case ETiny:
                {
                    iParticlesToLaunch = LibMath.getRandom( 1, 3 );
                    break;
                }

                case ELow:
                {
                    iParticlesToLaunch = LibMath.getRandom( 3, 6 );
                    break;
                }

                case EMedium:
                {
                    iParticlesToLaunch = LibMath.getRandom( 6, 12 );
                    break;
                }

                case EHigh:
                {
                    iParticlesToLaunch = LibMath.getRandom( 10, 20 );
                    break;
                }

                case EMassive:
                {
                    iParticlesToLaunch = LibMath.getRandom( 15, 30 );
                    break;
                }
            }
        }

        public final void launch( float angle )
        {
            for ( int i = 0; i < iParticlesToLaunch; ++i )
            {
                float radius   = 0.01f * LibMath.getRandom( 1, 30 );
                float angleMod = ( i * FxSettings.SLIVER_ANGLE_MOD * 2 / iParticlesToLaunch ) - FxSettings.SLIVER_ANGLE_MOD;

                FX.start
                (
                    new FXPoint
                    (
                        FXType.ESliver,
                        iSliverColors[ LibMath.getRandom( 0, iSliverColors.length - 1 ) ],
                        angle + angleMod,
                        iAnchor.x + radius * LibMath.sinDeg( angle ),
                        iAnchor.y + radius * LibMath.cosDeg( angle ),
                        iAnchor.z,
                        iSize,
                        0
                    )
                );
            }
        }
    }
