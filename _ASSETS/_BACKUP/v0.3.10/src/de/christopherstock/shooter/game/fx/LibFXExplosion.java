/*  $Id: LibFXExplosion.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  de.christopherstock.lib.fx.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.base.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    class LibFXExplosion extends LibFX
    {
        public      static  final   LibColors[]         EXPLOSION_COLORS                = new LibColors[]
        {
            LibColors.EExplosion1,  LibColors.EExplosion2,  LibColors.EExplosion3,
            LibColors.EExplosion4,  LibColors.EExplosion5,  LibColors.EExplosion6,
            LibColors.EExplosion7,  LibColors.EExplosion8,  LibColors.EExplosion9,
            LibColors.EExplosion10, LibColors.EExplosion11, LibColors.EExplosion12,
        };

        protected LibFXExplosion( LibVertex aAnchor, FXSize aSize, FXTime aTime, int lifetime )
        {
            super( aAnchor, lifetime );
            iSize   = aSize;
            iTime   = aTime;
        }

        protected final void launch()
        {
            int numParticlesPerWave     = 0;
            int numWaves                = 0;
            switch ( iSize )
            {
                case ESmall:
                {
                    numParticlesPerWave = 25 + LibMath.getRandom( -5, 10 );
                    break;
                }

                case EMedium:
                {
                    numParticlesPerWave = 50 + LibMath.getRandom( -5, 15 );
                    break;
                }

                case ELarge:
                {
                    numParticlesPerWave = 100 + LibMath.getRandom( -5, 20 );
                    break;
                }
            }

            switch ( iTime )
            {
                case EShort:
                {
                    numWaves = 5 + LibMath.getRandom( -2, 2 );
                    break;
                }

                case EMedium:
                {
                    numWaves = 10 + LibMath.getRandom( -2, 4 );
                    break;
                }

                case ELong:
                {
                    numWaves = 15 + LibMath.getRandom( -2, 6 );
                    break;
                }
            }

            int angleSteps = 360 / numParticlesPerWave;

            for ( int wave  = 0; wave < numWaves; ++wave )
            {
                for ( int angle = 0; angle < 360; angle += angleSteps )
                {
                    float radius = 0.01f * LibMath.getRandom( 1, 16 );

                    LibFXManager.start
                    (
                        new FXPoint
                        (
                            ShooterGameLevel.currentPlayer().getAnchor().z,
                            FXType.EExplosion,
                            EXPLOSION_COLORS[ LibMath.getRandom( 0, EXPLOSION_COLORS.length - 1 ) ],
                            angle,
                            iAnchor.x + radius * LibMath.sinDeg( angle ),
                            iAnchor.y + radius * LibMath.cosDeg( angle ),
                            iAnchor.z,
                            iSize,
                            wave,
                            iLifetime,
                            FXGravity.ENormal
                        )
                    );
                }
            }
        }
    }
