/*  $Id: Wearpon.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.awt.*;
    import  java.awt.image.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.wearpon.Ammo.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public enum Wearpon
    {
        //                  behaviour,                                  magazine    horz        vert        bulletsPerShot  bpsmod  usesound                reloadSound             shotRange,  afterShotDelayMS,   useNeedsKeyRelease, shotsTillKeyReleaseRequired
        EHands(             new CloseCombat(),                                                                                                                                      2.0f,       1000,               false,              1 ),
        ECrowbar(           new CloseCombat(),                                                                                                                                      2.0f,       800,                false,              1 ),
        EWaltherPPK(        new FireArm( AmmoType.ERifle792mm,          7,          3,          3,          1,              0,      Sound.EBulletShot1,     Sound.EReload1    ),    10.0f,      250,                true,               1 ),
        EPistol(            new FireArm( AmmoType.EBullet9mm,           8,          10,         10,         1,              0,      Sound.EShot1,           Sound.EReload1    ),    25.0f,      150,                true,               1 ),
        EMagnum357(         new FireArm( AmmoType.EMagnumBullet357,     6,          5,          5,          1,              0,      Sound.EShot1,           Sound.EReload2    ),    30.0f,      500,                false,              1 ),
        EHuntingRifle(      new FireArm( AmmoType.ERifle51mm,           50,         35,         35,         1,              0,      Sound.EShot1,           Sound.EReload2    ),    45.0f,      100,                true,               3 ),
        ESpaz12(            new FireArm( AmmoType.EShotgunShells,       2,          100,        100,        10,             3,      Sound.EShotgunShot1,    Sound.EReload2    ),    15.0f,      500,                false,              1 ),
        EAutomaticShotgun(  new FireArm( AmmoType.EShotgunShells,       5,          60,         60,         15,             2,      Sound.EShotgunShot1,    Sound.EReload2    ),    15.0f,      400,                false,              1 ),
        ERCP180(            new FireArm( AmmoType.ERifle792mm,          180,        10,         10,         1,              0,      Sound.EShot1,           Sound.EReload2    ),    50.0f,      50,                 false,              1 ),
        ;

        /**************************************************************************************
        *   The kind of wearpon represented by the strategy pattern.
        **************************************************************************************/
        public                      WearponKind         iWearponBehaviour               = null;

        private                     LibGLImage          iWearponImage                   = null;

        private                     long                iDelayAfterUse                  = 0;
        private                     long                iCurrentDelayAfterUse           = 0;

        private                     boolean             iUseNeedsKeyRelease             = false;
        private                     float               iRange                          = 0.0f;
        public                      int                 iShotsTillKeyReleaseRequired    = 0;
        public                      int                 iCurrentShotsWithoutKeyRelease  = 0;

        private Wearpon( WearponKind aWearponKind, float aRange, int aDelayAfterUse, boolean aUseNeedsKeyRelease, int aShotsTillKeyReleaseRequired )
        {
            iWearponBehaviour               = aWearponKind;
            iDelayAfterUse                  = aDelayAfterUse;
            iUseNeedsKeyRelease             = aUseNeedsKeyRelease;
            iRange                          = aRange;
            iShotsTillKeyReleaseRequired    = aShotsTillKeyReleaseRequired;
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
            iWearponImage = new LibGLImage( bufferedImage, ImageUsage.EOrtho, ShooterDebug.glimage, true );
        }

        public final boolean getCurrentShotNeedsKeyRelease()
        {
            return iUseNeedsKeyRelease;
        }

        public final void drawOrtho()
        {
            int     modX    = (int)(  20 * GameLevel.currentPlayer().getWalkingAngleCarriedModifierX() );
            int     modY    = -(int)( 10 * GameLevel.currentPlayer().getWalkingAngleCarriedModifierY() );

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
                if ( GameLevel.currentPlayer().getWearpon().isReloadable() )
                {
                    iWearponBehaviour.reload( this );
                }
            }
        }

        public final void reload()
        {
            iWearponBehaviour.reload( this );
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
                else if ( ShooterMainThread.launchShot )
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
