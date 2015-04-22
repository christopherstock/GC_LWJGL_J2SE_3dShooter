/*  $Id: WallEnergy.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.wall;

    import  de.christopherstock.lib.fx.LibFX.*;
    import  de.christopherstock.shooter.base.*;

    /**************************************************************************************
    *   Represents a mesh with all faces collision enabled.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class WallEnergy
    {
        public static enum WallHealth
        {
            EUnbreakale(        0   ),
            EGlass(             15  ),
            EWoodBrittle(       10  ),
            EWoodNormal(        15  ),
            EElectricalDevice(  15  ),
            ESolidGlass(        20  ),
            ESolidWood(         25  ),
            EVendingMachine(    25  ),
            ECrate(             30  ),
            EFurniture(         50  ),
            ;

            private     int     iHealth     = 0;

            private WallHealth( int aHealth )
            {
                iHealth = aHealth;
            }

            public int getHealth()
            {
                return iHealth;
            }
        }

        protected                   int                 iHealthStart                    = 0;
        protected                   boolean             iDestroyed                      = false;
        protected                   int                 iHealthCurrent                  = 0;
        protected                   float               iDyingTransZ                    = 0.0f;
        protected                   int                 iCurrentDyingTick               = 0;
        protected                   float               iKillAngleHorz                  = 0.0f;
        protected                   FXSize              iExplosionSize                  = null;
        protected                   ShooterSound        iExplosionSound                 = null;

        protected WallEnergy( WallEnergy.WallHealth aWallHealth, FXSize aExplosionSize, ShooterSound aExplosionSound )
        {
            iHealthStart        = aWallHealth.getHealth();
            iHealthCurrent      = aWallHealth.getHealth();
            iExplosionSize      = aExplosionSize;
            iExplosionSound     = aExplosionSound;
        }
    }
