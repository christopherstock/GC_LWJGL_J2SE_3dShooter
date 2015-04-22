/*  $Id: AmmoSet.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.util.*;

    /**************************************************************************************
    *   Represents the ammunition for the player, a bot or a device.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class AmmoSet
    {
        public                  Hashtable<AmmoType,Integer>     ammo                    = null;

        public AmmoSet()
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

        public static final void loadImages()
        {
            for ( AmmoType ammo : AmmoType.values() )
            {
                ammo.loadImage();
            }
        }
    }
