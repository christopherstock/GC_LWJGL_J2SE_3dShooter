/*  $Id: ArtefactType.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.awt.image.*;
    import  java.util.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.base.ShooterD3ds.D3dsFile;
    import  de.christopherstock.shooter.base.ShooterD3ds.Items;
    import  de.christopherstock.shooter.base.ShooterD3ds.Others;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public enum ArtefactType
    {
        //cc                wearponKind                                                                                                                                                                                             range,                                  afterShotDelayMS,   useNeedsKeyRelease, shotsTillKeyReleaseRequired     offsetForFireFX                 zoom                        mesh    item                                            crosshair               breaks walls    projectile
        EHands(             new CloseCombat( 10 ),                                                                                                                                                                                  PlayerAttributes.RADIUS_CLOSE_COMBAT,   500,                false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EKnife(             new CloseCombat( 15 ),                                                                                                                                                                                  PlayerAttributes.RADIUS_CLOSE_COMBAT,   500,                false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),

        //fire                           ammoType                       magazine    irrDepth    irrAngle    shotCount       scRandMod   useSound                        reloadSound             bulletShellSound
        EWaltherPPK(        new FireArm( AmmoType.EBullet44mm,          7,          3,          3,          1,              0,          ShooterSound.EBulletShot1,      ShooterSound.EReload1,  ShooterSound.EBulletShell1      ),  75.0f,                                  350,                true,               1,                              new FireFXOffset( 263, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EPistol(            new FireArm( AmmoType.EBullet9mm,           8,          10,         10,         1,              0,          ShooterSound.EShotRifle1,       ShooterSound.EReload1,  ShooterSound.EBulletShell1      ),  75.0f,                                  150,                true,               1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EMagnum357(         new FireArm( AmmoType.EMagnumBullet357,     6,          5,          5,          1,              0,          ShooterSound.EShotRifle1,       ShooterSound.EReload2,  null                            ),  75.0f,                                  500,                false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       Items.EPistol1, ItemKind.EWearponPistol9mm,             CrossHair.EDefault,     true ,          null                        ),
        EHuntingRifle(      new FireArm( AmmoType.EBullet51mm,          50,         35,         35,         1,              0,          ShooterSound.EShotRifle1,       ShooterSound.EReload2,  ShooterSound.EBulletShell1      ),  75.0f,                                  100,                true,               3,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        ESpaz12(            new FireArm( AmmoType.EShotgunShells,       2,          100,        100,        10,             3,          ShooterSound.EShotShotgun1,     ShooterSound.EReload2,  ShooterSound.EBulletShell1      ),  75.0f,                                  500,                false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       Items.EShotgun1, ItemKind.EWearponShotgun,              CrossHair.ECircle ,     false,          null                        ),
        EAutomaticShotgun(  new FireArm( AmmoType.EShotgunShells,       5,          60,         60,         15,             2,          ShooterSound.EShotShotgun1,     ShooterSound.EReload2,  ShooterSound.EBulletShell1      ),  75.0f,                                  400,                false,              1,                              new FireFXOffset( 325, 100 ),   0.0f,                       null,   null,                                           CrossHair.ECircle ,     false,          null                        ),
        ERCP180(            new FireArm( AmmoType.EBullet792mm,         180,        10,         10,         1,              0,          ShooterSound.EShotAssault1,     ShooterSound.EReload2,  ShooterSound.EBulletShell1      ),  75.0f,                                  50,                 false,              1,                              new FireFXOffset( 315, 90  ),   General.MAX_ZOOM / 2,       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        ESniperRifle(       new FireArm( AmmoType.EBullet792mm,         8,          0,          0,          1,              0,          ShooterSound.EBulletShot1,      ShooterSound.EReload2,  ShooterSound.EBulletShell1      ),  250.0f,                                 500,                false,              1,                              new FireFXOffset( 225, 200 ),   General.MAX_ZOOM,           null,   null,                                           CrossHair.EPrecise,     false,          null                        ),
        ETranquilizerGun(   new FireArm( AmmoType.ETranquilizerDarts,   8,          0,          0,          1,              0,          ShooterSound.ETranquilizerShot, ShooterSound.EReload2,  ShooterSound.EBulletShell1      ),  250.0f,                                 500,                false,              1,                              new FireFXOffset( 225, 200 ),   General.MAX_ZOOM * 3 / 4,   null,   null,                                           CrossHair.EPrecise,     false,          Others.ETranquilizerDart    ),

        //
        EKeycard1(          new Gadget( 10, 25, 10 ),                                                                                                                                                                               PlayerAttributes.RADIUS_ACTION,         1000,               false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EEnvelope(          new Gadget( 30, 40, 30 ),                                                                                                                                                                               PlayerAttributes.RADIUS_ACTION,         1000,               false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EMacAir(            new Gadget( 30, 40, 30 ),                                                                                                                                                                               PlayerAttributes.RADIUS_ACTION,         1000,               false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EAirMailLetter(     new Gadget( 30, 40, 30 ),                                                                                                                                                                               PlayerAttributes.RADIUS_ACTION,         1000,               false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EBottleVolvic(      new Gadget( 30, 40, 30 ),                                                                                                                                                                               PlayerAttributes.RADIUS_ACTION,         1000,               false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EMobilePhoneSEW890i(new Gadget( 30, 40, 30 ),                                                                                                                                                                               PlayerAttributes.RADIUS_ACTION,         1000,               false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EChips(             new Gadget( 30, 40, 30 ),                                                                                                                                                                               PlayerAttributes.RADIUS_ACTION,         1000,               false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),
        EAdrenaline(        new Gadget( 10, 40, 10 ),                                                                                                                                                                               PlayerAttributes.RADIUS_ACTION,         1000,               false,              1,                              new FireFXOffset( 225, 200 ),   0.0f,                       null,   null,                                           CrossHair.EDefault,     false,          null                        ),

        ;

        public static enum GiveTakeAnim
        {
            ENone,
            EOffer,
            EHold,
            EDraw,
            ;
        }

        /**************************************************************************************
        *   The kind of wearpon represented by the strategy pattern.
        **************************************************************************************/
        public                      WearponKind         iWearponBehaviour               = null;
        protected                   LibGLImage          iWearponImage                   = null;
        protected                   LibGLImage[]        iFXImages                       = null;
        protected                   long                iDelayAfterUse                  = 0;
        protected                   boolean             iUseNeedsKeyRelease             = false;
        public                      int                 iShotsTillKeyReleaseRequired    = 0;
        protected                   float               iRange                          = 0.0f;
        protected                   FireFXOffset        iFXOffset                       = null;
        protected                   int                 iDamage                         = 0;
        protected                   float               iZoom                           = 0.0f;
        public                      Items               iMesh                           = null;
        public                      ItemKind            iItemTemplate                   = null;
        public                      CrossHair           iCrossHair                      = null;
        public                      boolean             iBreaksWalls                    = false;
        public                      D3dsFile            iProjectile                     = null;

        private ArtefactType( WearponKind aWearponKind, float aRange, int aDelayAfterUse, boolean aUseNeedsKeyRelease, int aShotsTillKeyReleaseRequired, FireFXOffset aFXOffset, float aZoom, Items aMesh, ItemKind aPickUpItemTemplate, CrossHair aCrossHair, boolean aBreaksWalls, D3dsFile aProjectile )
        {
            iWearponBehaviour               = aWearponKind;
            iDelayAfterUse                  = aDelayAfterUse;
            iUseNeedsKeyRelease             = aUseNeedsKeyRelease;
            iRange                          = aRange;
            iShotsTillKeyReleaseRequired    = aShotsTillKeyReleaseRequired;
            iFXOffset                       = aFXOffset;
            iZoom                           = aZoom;
            iMesh                           = aMesh;
            iItemTemplate                   = aPickUpItemTemplate;
            iCrossHair                      = aCrossHair;
            iBreaksWalls                    = aBreaksWalls;
            iProjectile                     = aProjectile;

            iWearponBehaviour.setParent( this );
        }

        public int getDamage()
        {
            return iWearponBehaviour.getDamage();
        }

        public static final void loadImages()
        {
            for ( ArtefactType wearpon : values() )
            {
                wearpon.loadImage();
            }
        }

        public final void loadImage()
        {
            BufferedImage bufferedImage   = LibImage.load( ShooterSettings.Path.EArtefact.iUrl + toString() + LibExtension.png.getSpecifier(), ShooterDebug.glimage, false );
            iWearponImage = new LibGLImage( bufferedImage,   ImageUsage.EOrtho, ShooterDebug.glimage, true );
            Vector<LibGLImage> fxImages = new Vector<LibGLImage>();
            while ( true )
            {
                String ext = ( fxImages.size() > 0 ? String.valueOf( fxImages.size() + 1 ) : "" );
                String url = ShooterSettings.Path.EArtefactMuzzleFlash.iUrl + toString() + ext + LibExtension.png.getSpecifier();

                //break if file does not exist ( allows desired different flashes )
                if ( getClass().getResourceAsStream( url ) == null )
                {
                    break;
                }

                BufferedImage bufferedImageFX = LibImage.load( url, ShooterDebug.glimage, false );
                fxImages.add( new LibGLImage( bufferedImageFX, ImageUsage.EOrtho, ShooterDebug.glimage, true ) );
            }

            iFXImages = fxImages.toArray( new LibGLImage[] {} );
        }

        public final boolean getCurrentShotNeedsKeyRelease()
        {
            return iUseNeedsKeyRelease;
        }

        public final float getShotRange()
        {
            return iRange;
        }

        public final boolean isFireArm()
        {
            return ( iWearponBehaviour instanceof FireArm );
        }

        public final float getZoom()
        {
            return iZoom;
        }

        public final CrossHair getCrossHair()
        {
            return iCrossHair;
        }

        public final boolean getIsWallBreakable()
        {
            return iBreaksWalls;
        }

        public final D3dsFile getProjectile()
        {
            return iProjectile;
        }
    }
