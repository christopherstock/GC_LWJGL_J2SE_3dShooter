/*  $Id: FXExplosion.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  java.awt.geom.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.io.hid.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class FXExplosion extends FX
    {
        public      static  final   LibColors[]         EXPLOSION_COLORS                = new LibColors[]
        {
            LibColors.EExplosion1,  LibColors.EExplosion2,  LibColors.EExplosion3,
            LibColors.EExplosion4,  LibColors.EExplosion5,  LibColors.EExplosion6,
            LibColors.EExplosion7,  LibColors.EExplosion8,  LibColors.EExplosion9,
            LibColors.EExplosion10, LibColors.EExplosion11, LibColors.EExplosion12,
        };

        public FXExplosion( LibVertex aAnchor, FXSize aSize, FXTime aTime )
        {
            super( aAnchor );
            iSize   = aSize;
            iTime   = aTime;
        }

        public final void launch()
        {
            //play explosion sound
            Sound.EExplosion1.playDistancedFx( new Point2D.Float( iAnchor.x, iAnchor.y ) );

            int numParticlesPerWave     = 0;
            int numWaves                = 0;
            switch ( iSize )
            {
                case ESmall:
                {
                    numParticlesPerWave = 24 + LibMath.getRandom( -5, 5 );
                    break;
                }

                case EMedium:
                {
                    numParticlesPerWave = 48 + LibMath.getRandom( -10, 10 );
                    break;
                }

                case ELarge:
                {
                    numParticlesPerWave = 128 + LibMath.getRandom( -15, 15 );
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

                    FX.start
                    (
                        new FXPoint
                        (
                            FXType.EExplosion,
                            EXPLOSION_COLORS[ LibMath.getRandom( 0, EXPLOSION_COLORS.length - 1 ) ],
                            angle,
                            iAnchor.x + radius * LibMath.sinDeg( angle ),
                            iAnchor.y + radius * LibMath.cosDeg( angle ),
                            iAnchor.z,
                            iSize,
                            wave
                        )
                    );
                }
            }
        }
    }
