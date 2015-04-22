/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public enum Ammo
    {
        /** Ammo for the scattergun                         */  scattergunShells(   10      ),
        /** Ammo for the shotgun and auto-shotgun           */  shotgunShells(      20      ),
        /** Ammo for the flamer                             */  flamerGas(          100     ),
        /** Ammo for the grenade launcher                   */  grenadeRolls(       6       ),
        /** Ammo for the browning 9mm                       */  bullet9mm(          50      ),
        /** Ammo for the assault rifle 5.1mm                */  rifle51mm(          60      ),
        /** Ammo for the magnum 3.57mm                      */  magnumBullet357(    12      ),
        /** Pipebombs                                       */  pipeBombs(          10      ),
        ;

        protected   int                     ammo            = 0;

        private Ammo( int startupAmmo )
        {
            ammo = startupAmmo;
        }
    }
