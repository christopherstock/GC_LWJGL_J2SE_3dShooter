/*  $Id: Ammo.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.awt.image.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.fx.FXSliver.ParticleQuantity;

    /**************************************************************************************
    *   The ammunition.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class Ammo
    {
        public static enum AmmoType
        {
            /** for the 4.4mm                       */  EBullet44mm(        LibHoleSize.E44mm, 200,  ParticleQuantity.ELow     ),
            /** for the assault rifle 5.1mm         */  EBullet51mm(        LibHoleSize.E51mm, 400,  ParticleQuantity.EMedium  ),
            /** for the assault rifle 7.92mm        */  EBullet792mm(       LibHoleSize.E79mm, 400,  ParticleQuantity.EHigh    ),
            /** for the 4.4mm                       */  EBullet9mm(         LibHoleSize.E9mm,  200,  ParticleQuantity.EMassive ),
            /** for the shotgun and auto-shotgun    */  EShotgunShells(     LibHoleSize.EHuge, 20,   ParticleQuantity.EMassive ),
            /** for the magnum 3.57mm               */  EMagnumBullet357(   LibHoleSize.EHuge, 200,  ParticleQuantity.EMassive ),

            /** for the flamer                      */  EFlamerGas(         LibHoleSize.E51mm, 5,    ParticleQuantity.ELow     ),
            /** for the grenade launcher            */  EGrenadeRolls(      LibHoleSize.EHuge, 12,   ParticleQuantity.EHigh    ),
            /** Pipebombs                           */  EPipeBombs(         LibHoleSize.E51mm, 10,   ParticleQuantity.EMassive ),
            ;

            protected       LibHoleSize             iBulletHoleSize         = null;
            protected       int                     iMaxAmmo                = 0;
            protected       ParticleQuantity        iSliverParticleQuantity = null;
            protected       LibGLImage              iAmmoImage              = null;

            private AmmoType( LibHoleSize aBulletHoleSize, int aMaxAmmo, ParticleQuantity aSliverParticleQuantity )
            {
                iBulletHoleSize         = aBulletHoleSize;
                iMaxAmmo                = aMaxAmmo;
                iSliverParticleQuantity = aSliverParticleQuantity;
            }

            public final void loadImage()
            {
                BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.EShells.iUrl + this.toString() + LibExtension.png.getSpecifier(), ShooterDebug.glimage, false );
                iAmmoImage = new LibGLImage( bufferedImage, ImageUsage.EOrtho, ShooterDebug.glimage, true );
            }

            public final LibGLImage getImage()
            {
                return iAmmoImage;
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

        public static final void loadImages()
        {
            for ( AmmoType ammo : AmmoType.values() )
            {
                ammo.loadImage();
            }
        }
    }
