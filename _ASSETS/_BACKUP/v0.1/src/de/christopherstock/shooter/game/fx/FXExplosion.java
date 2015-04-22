/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  de.christopherstock.shooter.util.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     stock
    *   @version    1.0
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

        public FXExplosion( float x, float y, float z, FXSize size, FXTime time )
        {
            this.x      = x;
            this.y      = y;
            this.z      = z;

            this.size   = size;
            this.time   = time;

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
                    numParticlesPerWave = 24 + UMath.getRandom( -5, 5 );
                    break;
                }

                case EMedium:
                {
                    numParticlesPerWave = 48 + UMath.getRandom( -10, 10 );
                    break;
                }

                case ELarge:
                {
                    numParticlesPerWave = 128 + UMath.getRandom( -15, 15 );
                    break;
                }
            }

            switch ( time )
            {
                case EShort:
                {
                    numWaves = 5 + UMath.getRandom( -2, 2 );
                    break;
                }

                case EMedium:
                {
                    numWaves = 10 + UMath.getRandom( -2, 4 );
                    break;
                }

                case ELong:
                {
                    numWaves = 15 + UMath.getRandom( -2, 6 );
                    break;
                }
            }

            int angleSteps = 360 / numParticlesPerWave;

            for ( int wave  = 0; wave < numWaves; ++wave )
            {
                for ( int angle = 0; angle < 360; angle += angleSteps )
                {
                    float radius = 0.01f * UMath.getRandom( 1, 16 );

                    new FXPoint
                    (
                        FXType.EExplosion,
                        EXPLOSION_COLORS[ UMath.getRandom( 0, EXPLOSION_COLORS.length - 1 ) ],
                        angle,
                        x + radius * UMath.sinDeg( angle ),
                        y + radius * UMath.cosDeg( angle ),
                        z,
                        size,
                        wave
                    );
                }
            }
        }
    }
