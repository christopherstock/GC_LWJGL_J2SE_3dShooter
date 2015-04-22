/*  $Id: FXExplosion.java 191 2010-12-13 20:24:11Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class FXExplosion extends FX
    {
        public      static  final   int[]       EXPLOSION_COLORS                = new int[]
        {
            0x785216,   0x62471c,   0x8b7757,
            0xb09468,   0x7a6d58,   0x845124,
            0x9d7e62,   0x786e65,   0x737373,
            0x888787,   0x343434,   0x000000,
        };

        public FXExplosion( LibVertex aAnchor, FXSize aSize, FXTime aTime )
        {
            super( aAnchor );
            size   = aSize;
            time   = aTime;
        }

        @Override
        public final void launch()
        {
            int numParticlesPerWave     = 0;
            int numWaves                = 0;
            switch ( size )
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

            switch ( time )
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

                    new FXPoint
                    (
                        FXType.EExplosion,
                        EXPLOSION_COLORS[ LibMath.getRandom( 0, EXPLOSION_COLORS.length - 1 ) ],
                        angle,
                        anchor.x + radius * LibMath.sinDeg( angle ),
                        anchor.y + radius * LibMath.cosDeg( angle ),
                        anchor.z,
                        size,
                        wave
                    ).start();
                }
            }
        }
    }
