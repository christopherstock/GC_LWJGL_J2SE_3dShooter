/*  $Id: Ammo.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   The ammunition.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class Ammo
    {
        public static enum AmmoType
        {
            /** for the shotgun and auto-shotgun    */  EShotgunShells(     LibHoleSize.EHuge, 20   ),
            /** for the flamer                      */  EFlamerGas(         LibHoleSize.E51mm, 5    ),
            /** for the grenade launcher            */  EGrenadeRolls(      LibHoleSize.EHuge, 12   ),
            /** for the browning 9mm                */  EBullet9mm(         LibHoleSize.E9mm , 200  ),
            /** for the assault rifle 5.1mm         */  ERifle51mm(         LibHoleSize.ETiny, 400  ),
            /** for the magnum 3.57mm               */  EMagnumBullet357(   LibHoleSize.EHuge, 200  ),
            /** Pipebombs                           */  EPipeBombs(         LibHoleSize.E51mm, 10   ),
            ;

            protected       LibHoleSize             iBulletHoleSize         = null;
            protected       int                     iMaxAmmo                = 0;

            private AmmoType( LibHoleSize aBulletHoleSize, int aMaxAmmo )
            {
                iBulletHoleSize = aBulletHoleSize;
                iMaxAmmo        = aMaxAmmo;
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
            if ( oldAmmo > at.iMaxAmmo ) oldAmmo = at.iMaxAmmo;
            ammo.put( at, new Integer( oldAmmo ) );
        }
    }
