/*  $Id: Artefact.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.awt.*;
    import  java.awt.image.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.game.wearpon.Ammo.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public enum Artefact
    {
        //                  behaviour,                                  magazine    horz        vert        bulletsPerShot  bpsmod  usesound                reloadSound             shotRange,  afterShotDelayMS,   useNeedsKeyRelease, shotsTillKeyReleaseRequired
        EHands(             new CloseCombat(),                                                                                                                                      2.0f,       1000,               false,              1,  new FXOffset( 225, 200 ) ),
        EKnife(             new CloseCombat(),                                                                                                                                      2.0f,       800,                false,              1,  new FXOffset( 225, 200 ) ),
        EWaltherPPK(        new FireArm( AmmoType.EBullet44mm,          7,          3,          3,          1,              0,      Sound.EBulletShot1,     Sound.EReload1    ),    10.0f,      250,                true,               1,  new FXOffset( 263, 200 ) ),
        EPistol(            new FireArm( AmmoType.EBullet9mm,           8,          10,         10,         1,              0,      Sound.EShot1,           Sound.EReload1    ),    25.0f,      150,                true,               1,  new FXOffset( 225, 200 ) ),
        EMagnum357(         new FireArm( AmmoType.EMagnumBullet357,     6,          5,          5,          1,              0,      Sound.EShot1,           Sound.EReload2    ),    30.0f,      500,                false,              1,  new FXOffset( 225, 200 ) ),
        EHuntingRifle(      new FireArm( AmmoType.EBullet51mm,          50,         35,         35,         1,              0,      Sound.EShot1,           Sound.EReload2    ),    45.0f,      100,                true,               3,  new FXOffset( 225, 200 ) ),
        ESpaz12(            new FireArm( AmmoType.EShotgunShells,       2,          100,        100,        10,             3,      Sound.EShotgunShot1,    Sound.EReload2    ),    15.0f,      500,                false,              1,  new FXOffset( 225, 200 ) ),
        EAutomaticShotgun(  new FireArm( AmmoType.EShotgunShells,       5,          60,         60,         15,             2,      Sound.EShotgunShot1,    Sound.EReload2    ),    15.0f,      400,                false,              1,  new FXOffset( 325, 100 ) ),
        ERCP180(            new FireArm( AmmoType.EBullet792mm,         180,        10,         10,         1,              0,      Sound.EShot1,           Sound.EReload2    ),    50.0f,      50,                 false,              1,  new FXOffset( 315, 90 ) ),
        ESniperRifle(       new FireArm( AmmoType.EBullet792mm,         8,          0,          0,          1,              0,      Sound.EBulletShot1,     Sound.EReload2    ),    100.0f,     150,                false,              1,  new FXOffset( 225, 200 ) ),

        EEnvelope(          new Gadget(),                                                                                                                                           2.0f,       1000,               false,              1,  new FXOffset( 225, 200 ) ),
        EMacAir(            new Gadget(),                                                                                                                                           2.0f,       1000,               false,              1,  new FXOffset( 225, 200 ) ),
        EAirMailLetter(     new Gadget(),                                                                                                                                           2.0f,       1000,               false,              1,  new FXOffset( 225, 200 ) ),
        EBottleVolvic(      new Gadget(),                                                                                                                                           2.0f,       1000,               false,              1,  new FXOffset( 225, 200 ) ),
        EMobilePhoneSEW890i(new Gadget(),                                                                                                                                           2.0f,       1000,               false,              1,  new FXOffset( 225, 200 ) ),
        EChips(             new Gadget(),                                                                                                                                           2.0f,       1000,               false,              1,  new FXOffset( 225, 200 ) ),

        ;

        /**************************************************************************************
        *   The kind of wearpon represented by the strategy pattern.
        **************************************************************************************/
        public                      WearponKind         iWearponBehaviour               = null;

        private                     LibGLImage          iWearponImage                   = null;
        private                     LibGLImage[]        iFXImages                       = null;

        private                     long                iDelayAfterUse                  = 0;
        private                     long                iCurrentDelayAfterUse           = 0;
        private                     boolean             iDrawFireFXtick                 = false;

        private                     boolean             iUseNeedsKeyRelease             = false;
        private                     float               iRange                          = 0.0f;
        public                      int                 iShotsTillKeyReleaseRequired    = 0;
        public                      int                 iCurrentShotsWithoutKeyRelease  = 0;
        private                     FXOffset            iFXOffset                       = null;

        public static final class FXOffset
        {
            public int x = 0;
            public int y = 0;

            public FXOffset( int aX, int aY )
            {
                x = aX;
                y = aY;
            }
        }

        private Artefact( WearponKind aWearponKind, float aRange, int aDelayAfterUse, boolean aUseNeedsKeyRelease, int aShotsTillKeyReleaseRequired, FXOffset aFXOffset )
        {
            iWearponBehaviour               = aWearponKind;
            iDelayAfterUse                  = aDelayAfterUse;
            iUseNeedsKeyRelease             = aUseNeedsKeyRelease;
            iRange                          = aRange;
            iShotsTillKeyReleaseRequired    = aShotsTillKeyReleaseRequired;
            iFXOffset                       = aFXOffset;
        }

        public static final void loadImages()
        {
            for ( Artefact wearpon : values() )
            {
                wearpon.loadImage();
            }
        }

        public final void loadImage()
        {
            BufferedImage bufferedImage   = LibImage.load( ShooterSettings.Path.EArtefact.iUrl + this.toString() + LibExtension.png.getSpecifier(), ShooterDebug.glimage, false );
            iWearponImage = new LibGLImage( bufferedImage,   ImageUsage.EOrtho, ShooterDebug.glimage, true );
            Vector<LibGLImage> fxImages = new Vector<LibGLImage>();
            while ( true )
            {
                String ext = ( fxImages.size() > 0 ? String.valueOf( fxImages.size() + 1 ) : "" );
                String url = ShooterSettings.Path.EArtefactMuzzleFlash.iUrl + this.toString() + ext + LibExtension.png.getSpecifier();

                //break if file does not exist
                if ( Artefact.class.getResourceAsStream( url ) == null )
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

        public final void drawOrtho()
        {
            int     modX    = (int)(  20 * ShooterGameLevel.currentPlayer().getWalkingAngleCarriedModifierX() );
            int     modY    = -(int)( 10 * ShooterGameLevel.currentPlayer().getWalkingAngleCarriedModifierY() );

            //hide/show animation?
            if ( HUD.getSingleton().iAnimationPlayerRightHand > 0 )
            {
                switch ( HUD.getSingleton().iAnimationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }
                    case EAnimationHide:
                    {
                        modY -= iWearponImage.height - HUD.getSingleton().iAnimationPlayerRightHand * iWearponImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                    case EAnimationShow:
                    {
                        modY -= HUD.getSingleton().iAnimationPlayerRightHand * iWearponImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                }
            }

            //draw fire fx behind wearpon
            if ( iDrawFireFXtick )
            {
                //no random translation!
                int         randomX     = 0; //LibMath.getRandom( -5, 5 );
                int         randomY     = 0; //LibMath.getRandom( -5, 5 );
                int         randomIndex = ( iFXImages.length > 1 ? LibMath.getRandom( 0, iFXImages.length - 1 ) : 0 );
                LibGLImage  fxImage     = iFXImages[ randomIndex ];

                LibGL3D.view.drawOrthoBitmapBytes
                (
                    fxImage,
                    randomX + modX + LibGL3D.panel.width - iFXOffset.x,
                    randomY + modY + iFXOffset.y
                );

                iDrawFireFXtick = false;
            }

            //draw ortho on gl
            LibGL3D.view.drawOrthoBitmapBytes
            (
                iWearponImage,
                modX + LibGL3D.panel.width - iWearponImage.width,
                modY - iWearponImage.height / 8
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
            //try to fire wearpon
            if ( iWearponBehaviour.fire( this ) )
            {
                //draw fire fx
                iDrawFireFXtick = true;

                //set delay after this shot
                iCurrentDelayAfterUse = System.currentTimeMillis() + iDelayAfterUse;

                //increase number of shots since last key release
                ++iCurrentShotsWithoutKeyRelease;
            }
        }

        public final void orderReload()
        {
            //if no animation is active
            if ( HUD.getSingleton().iAnimationPlayerRightHand == 0 )
            {
                //if the wearpon has ammo
                if ( ShooterGameLevel.currentPlayer().getWearpon().isReloadable() )
                {
                    iWearponBehaviour.reload( true );
                }
            }
        }

        public final void reload( boolean hudAnimRequired )
        {
            iWearponBehaviour.reload( hudAnimRequired );
        }

        public final void handleWearpon()
        {
            if ( iCurrentDelayAfterUse > System.currentTimeMillis() )
            {
                //delay after wearpon use
            }
            else if ( HUD.getSingleton().iAnimationPlayerRightHand > 0 )
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
                    orderReload();
                }
                //shoot
                else if ( ShooterGameLevel.currentPlayer().getLaunchShot() )
                {
                    fire();
                }
            }
        }

        public final float getShotRange()
        {
            return iRange;
        }

        public final LibHoleSize getBulletHoleSize()
        {
            return ( (FireArm)( iWearponBehaviour ) ).getBulletHoleSize();
        }

        public final boolean isReloadable()
        {
            return iWearponBehaviour.isReloadable();
        }
    }
