/*  $Id: Wearpon.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;
    import  java.awt.image.*;

    import de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.Ammo.AmmoType;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public enum Wearpon
    {
        //                  ammo,                           shotRange,  modDepth,   modAngle,   magazineSize    shots,  modShots,       afterShotDelay,     shotNeedsKeyRelease,
        EHands(             null,                           2.0f,       0,          0,          0,              1,      0,              5,                  false       ),
        ECrowbar(           null,                           2.0f,       0,          0,          0,              1,      0,              5,                  false       ),
        EPistol(            AmmoType.EBullet9mm,            25.0f,      10,         10,         8,              1,      0,              3,                  true        ),
        EAssaultRifle(      AmmoType.ERifle51mm,            45.0f,      35,         35,         9999,           1,      0,              0,                  false       ),
        EScattergun(        AmmoType.EShotgunShells,        15.0f,      50,         50,         2,              10,     3,              5,                  false       ),
        EWaltherPPK(        AmmoType.EBullet9mm,            10.0f,      3,          3,          7,              1,      0,              1,                  true        ),
        ERCP180(            AmmoType.ERifle51mm,            50.0f,      10,         10,         2500,           5,      3,              1,                  false       ),
//      EAutomaticShotgun(  AmmoType.shotgunShells,         15.0f,      50,         50,         2,              10,     3,              5,                  false       ),
//      ERevolver357(       AmmoTyoe.EBullet357,            30.0f,                              6,              1,
        ;

        public                      LibGLImage          wearponImage                = null;
        private                     AmmoType            iAmmoType                   = null;
        private                     int                 iWearponIrregularityDepth   = 0;
        private                     int                 iWearponIrregularityAngle   = 0;
        private                     int                 currentAfterShotDelay       = 0;
        private                     int                 iAfterShotDelay             = 0;
        private                     int                 iMagazineSize               = 0;
        private                     int                 iMagazineAmmo               = 0;
        private                     int                 iCurrentShotCount           = 0;
        private                     int                 iCurrentShotCountRandomMod  = 0;
        private                     float               iShotRange                  = 0.0f;
        private                     boolean             iShotNeedsKeyRelease        = false;

        private Wearpon( AmmoType aAmmoType, float aShotRange, int aWearponIrregularityDepth, int aWearponIrregularityAngle, int aMagazineSize, int aCurrentShotCount, int aCurrentShotCountRandomMod, int aAfterShotDelay, boolean aShotNeedsKeyRelease )
        {
            iAmmoType                   = aAmmoType;
            iWearponIrregularityDepth   = aWearponIrregularityDepth;
            iWearponIrregularityAngle   = aWearponIrregularityAngle;
            iMagazineSize               = aMagazineSize;
            iAfterShotDelay             = aAfterShotDelay;
            iShotNeedsKeyRelease        = aShotNeedsKeyRelease;
            iCurrentShotCount           = aCurrentShotCount;
            iCurrentShotCountRandomMod  = aCurrentShotCountRandomMod;
            iMagazineAmmo               = 0;
            iShotRange                  = aShotRange;
        }

        public static final void loadImages()
        {
            for ( Wearpon wearpon : values() )
            {
                wearpon.loadImage();
            }
        }

        public final void loadImage()
        {
            BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.EWearpons.iUrl + this.toString() + LibExtension.png.getSpecifier(), ShooterDebug.glimage, false );
            wearponImage = new LibGLImage( bufferedImage, ImageUsage.EOrtho, ShooterDebug.glimage, true );
        }

        public final float getCurrentIrregularityDepth()
        {
            //return modifier-z for the current wearpon
            return ( LibMath.getRandom( -iWearponIrregularityDepth, iWearponIrregularityDepth ) * 0.01f );
        }

        public final float getCurrentIrregularityAngle()
        {
            //return modifier-z for the current wearpon
            return ( LibMath.getRandom( -iWearponIrregularityAngle, iWearponIrregularityAngle ) * 0.01f );
        }

        public final int getMagazineSize()
        {
            return iMagazineSize;
        }

        public final int getMagazineAmmo()
        {
            return iMagazineAmmo;
        }

        public final int getCurrentShotCount()
        {
            return iCurrentShotCount + LibMath.getRandom( -iCurrentShotCountRandomMod, iCurrentShotCountRandomMod );
        }

        public final boolean getCurrentShotNeedsKeyRelease()
        {
            return iShotNeedsKeyRelease;
        }

        public final void drawOrtho()
        {
            int     modX    = (int)(  20 * Level.currentPlayer().getWalkingAngleCarriedModifierX() );
            int     modY    = -(int)( 10 * Level.currentPlayer().getWalkingAngleCarriedModifierY() );

            //hide/show animation?
            if ( HUD.singleton.iAnimationPlayerRightHand > 0 )
            {
                switch ( HUD.singleton.iAnimationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }
                    case EAnimationHide:
                    {
                        modY -= wearponImage.height - HUD.singleton.iAnimationPlayerRightHand * wearponImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                    case EAnimationShow:
                    {
                        modY -= HUD.singleton.iAnimationPlayerRightHand * wearponImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                }
            }

            //draw ortho on gl
            LibGL3D.view.drawOrthoBitmapBytes
            (
                wearponImage,
                modX + LibGL3D.panel.width - wearponImage.width,
                modY - wearponImage.height / 8
            );
        }

        /**************************************************************************************
        *   Draws a 3D model of the current holding wearpon in front of the player's eye.
        **************************************************************************************/
/*
        public final void draw3D( GL gl )
        {
            //float modX = Player.getWalkingAngle2Modifier()  / 20;
            //float modY = -Player.getWalkingAngle3Modifier() / 40;

            gl.glLoadIdentity();                        //new identity please
            gl.glNormal3f( 0.0f, 0.0f, 0.0f );          //normalize

            gl.glEnable(  GL.GL_BLEND       );                                  //enable Blending
            gl.glBlendFunc( GL.GL_ONE, GL.GL_ZERO );                             //blend Screen Color With Zero (Black)

            //draw the 3d-shotgun
          //Meshes.shotgun.draw();

            gl.glDisable(   GL.GL_BLEND       );                                //disnable Blending

        }
*/
        public static final void drawCrosshair( Graphics g )
        {
            int centerX = LibGL3D.panel.width  / 2;
            int centerY = LibGL3D.panel.height / 2;

            centerY += 42;

            //HUD.releaseClip( g );
            g.setColor( Color.BLACK );
            g.drawLine( centerX - 5, centerY, centerX - 10, centerY );
            g.drawLine( centerX + 5, centerY, centerX + 10, centerY );
            g.drawLine( centerX, centerY - 5, centerX, centerY - 10 );
            g.drawLine( centerX, centerY + 5, centerX, centerY + 10 );
        }

        public final void fire()
        {
            if ( currentAfterShotDelay == 0 )
            {
                if ( iAmmoType == null )
                {
                    ShooterDebug.major.out( "Perform close-combat attack" );
                }
                else
                {
                    //check if wearpon has enough ammo
                    if ( iMagazineAmmo == 0 )
                    {
                        return;
                    }

                    //reduce magazine-ammo by 1
                    --iMagazineAmmo;

                    //launch sound-fx
                    Sound.ESoundBullet1.playGlobalFx();

                    //launch number of shots this gun fires
                    for ( int i = 0; i < iCurrentShotCount; ++i )
                    {
                        Shot shot = Level.currentPlayer().getShot();

                        //clear all existent debug points and the shot-line
                        LibGLDebugPoint.deleteAll();

                        //launch the shot on the current level - draw a shot line 1st
                        shot.drawShotLine();
                        shot.launch();

                    }
                }

                //set delay after this shot
                currentAfterShotDelay = iAfterShotDelay;
            }
        }

        public final void orderReload()
        {
            //if no animation is active
            if ( HUD.singleton.iAnimationPlayerRightHand == 0 )
            {
                //if the wearpon has ammo
                if ( Level.currentPlayer().getAmmo().getAmmo( iAmmoType ) > 0 )
                {
                    //if the wearpon is not fully loaded
                    if ( iMagazineSize != iMagazineAmmo )
                    {
                        HUD.singleton.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionReload );
                    }
                }
            }
        }

        public final void reload()
        {
            //put unused ammo back onto the stack!
            Level.currentPlayer().getAmmo().addAmmo( iAmmoType, iMagazineAmmo );

            //check ammo stock
            int ammo = Level.currentPlayer().getAmmo().getAmmo( iAmmoType );
            int ammoToReload = ( ammo >= iMagazineSize ? iMagazineSize : ammo );

            //load ammoToReload into the magazine and substract it from the ammo stock
            iMagazineAmmo = ammoToReload;
            Level.currentPlayer().getAmmo().substractAmmo( iAmmoType, ammoToReload );

            HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
        }

        public final void handleWearpon()
        {
            if ( currentAfterShotDelay > 0 )
            {
                --currentAfterShotDelay;
            }
            else if ( HUD.singleton.iAnimationPlayerRightHand > 0 )
            {
                //no actions while being animated (hide/show)!
            }
            else
            {
                //reload?
                if ( Keys.reload.iLaunchAction )
                {
                    //reload if the wearpon uses ammo
                    Keys.reload.iLaunchAction = false;
                    if ( iAmmoType != null ) orderReload();
                }
                //shoot
                else if ( ShooterMainThread.launchShot )
                {
                    fire();
                }
            }
        }

        public final float getShotRange()
        {
            return iShotRange;
        }

        public final LibHoleSize getBulletHoleSize()
        {
            return iAmmoType.iBulletHoleSize;
        }

        public final AmmoType getAmmoType()
        {
            return iAmmoType;
        }
    }
