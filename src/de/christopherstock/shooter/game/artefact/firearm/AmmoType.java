/*  $Id: AmmoType.java 1284 2014-10-08 21:57:48Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.artefact.firearm;

    import  java.awt.image.BufferedImage;
    import  de.christopherstock.lib.Lib;
    import  de.christopherstock.lib.fx.LibFX.FXSize;
    import  de.christopherstock.lib.g3d.LibHoleSize;
    import  de.christopherstock.lib.gl.LibGLImage;
    import  de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.io.LibExtension;
    import  de.christopherstock.lib.ui.LibImage;
    import  de.christopherstock.shooter.ShooterDebug;
    import  de.christopherstock.shooter.ShooterSettings;

    /**************************************************************************************
    *   The ammunition.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public enum AmmoType
    {
        /** for the 4.4mm                       */  EBullet44mm(        LibHoleSize.E44mm,     200,     Lib.ParticleQuantity.ELow,     10,  FXSize.ESmall  ),
        /** for the assault rifle 5.1mm         */  EBullet51mm(        LibHoleSize.E51mm,     400,     Lib.ParticleQuantity.EMedium,  12,  FXSize.ESmall  ),
        /** for the assault rifle 7.92mm        */  EBullet792mm(       LibHoleSize.E79mm,     400,     Lib.ParticleQuantity.EHigh,    14,  FXSize.EMedium ),
        /** for the 9mm                         */  EBullet9mm(         LibHoleSize.E9mm,      200,     Lib.ParticleQuantity.EMassive, 16,  FXSize.EMedium ),
        /** for the shotgun and auto-shotgun    */  EShotgunShells(     LibHoleSize.EHuge,     100,     Lib.ParticleQuantity.EMassive, 8,   FXSize.ELarge  ),
        /** for the magnum 3.57mm               */  EMagnumBullet357(   LibHoleSize.EHuge,     200,     Lib.ParticleQuantity.EMassive, 20,  FXSize.ELarge  ),
        /** for the tranquilizer gun            */  ETranquilizerDarts( LibHoleSize.E79mm,     60,      Lib.ParticleQuantity.EMedium,  0,   FXSize.EMedium ),

        /** for the flamer                      */  EFlamerGas(         LibHoleSize.E51mm,     5,       Lib.ParticleQuantity.ELow,     10,  FXSize.ELarge  ),
        /** for the grenade launcher            */  EGrenadeRolls(      LibHoleSize.EHuge,     12,      Lib.ParticleQuantity.EHigh,    30,  FXSize.ELarge  ),
        /** Pipebombs                           */  EPipeBombs(         LibHoleSize.E51mm,     10,      Lib.ParticleQuantity.EMassive, 40,  FXSize.ELarge  ),
        ;

        protected       LibHoleSize             iBulletHoleSize         = null;
        protected       FXSize                  iSliverParticleSize     = null;
        protected       int                     iMaxAmmo                = 0;
        protected       Lib.ParticleQuantity    iSliverParticleQuantity = null;
        protected       LibGLImage              iHUDAmmoImage           = null;
        protected       int                     iDamage                 = 0;

        private AmmoType( LibHoleSize aBulletHoleSize, int aMaxAmmo, Lib.ParticleQuantity aSliverParticleQuantity, int aDamage, FXSize aSliverParticleSize )
        {
            iBulletHoleSize         = aBulletHoleSize;
            iMaxAmmo                = aMaxAmmo;
            iSliverParticleQuantity = aSliverParticleQuantity;
            iDamage                 = aDamage;
            iSliverParticleSize     = aSliverParticleSize;
        }

        protected final void loadImage()
        {
            BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.EShells.iUrl + toString() + LibExtension.png.getSpecifier(), ShooterDebug.glImage, false );
            iHUDAmmoImage = new LibGLImage( bufferedImage, ImageUsage.EOrtho, ShooterDebug.glImage, true );
        }

        protected final LibGLImage getImage()
        {
            return iHUDAmmoImage;
        }

        protected final int getDamage()
        {
            return iDamage;
        }
    }
