/*  $Id: Ammo.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.util.*;
    import  de.christopherstock.shooter.game.collision.*;

    /**************************************************************************************
    *   The ammunition.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Ammo
    {
        public static enum AmmoType
        {
            /** for the scattergun                  */  EScattergunShells(  BulletHole.PointSize.E51mm  ),
            /** for the shotgun and auto-shotgun    */  EShotgunShells(     BulletHole.PointSize.EHuge  ),
            /** for the flamer                      */  EFlamerGas(         BulletHole.PointSize.E51mm  ),
            /** for the grenade launcher            */  EGrenadeRolls(      BulletHole.PointSize.EHuge  ),
            /** for the browning 9mm                */  EBullet9mm(         BulletHole.PointSize.E9mm   ),
            /** for the assault rifle 5.1mm         */  ERifle51mm(         BulletHole.PointSize.E51mm  ),
            /** for the magnum 3.57mm               */  EMagnumBullet357(   BulletHole.PointSize.EHuge  ),
            /** Pipebombs                           */  EPipeBombs(         BulletHole.PointSize.E51mm  ),
            ;

            protected       BulletHole.PointSize    bulletHoleSize          = null;

            private AmmoType( BulletHole.PointSize aBulletHoleSize )
            {
                bulletHoleSize  = aBulletHoleSize;
            }
        }

        public                  Hashtable<AmmoType,Integer>     ammo                    = null;

        public Ammo()
        {
            ammo    = new Hashtable<AmmoType,Integer>();

            for ( AmmoType at : AmmoType.values() )
            {
                ammo.put( at, new Integer( 0 ) );
            }
        }

        public int getAmmo( AmmoType at )
        {
            return ammo.get( at ).intValue();
        }

        public void substractAmmo( AmmoType at, int substraction )
        {
            int oldAmmo = ammo.get( at ).intValue();
            oldAmmo -= substraction;
            ammo.put( at, new Integer( oldAmmo ) );
        }

        public void addAmmo( AmmoType at, int addition )
        {
            int oldAmmo = ammo.get( at ).intValue();
            oldAmmo += addition;
            ammo.put( at, new Integer( oldAmmo ) );
        }
    }
