/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;
    import  java.awt.image.*;
    import  javax.imageio.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.player.*;
    import de.christopherstock.shooter.gl.opengl.*;
    import  de.christopherstock.shooter.io.hid.*;
import de.christopherstock.shooter.math.*;
import  de.christopherstock.shooter.ui.*;
import de.christopherstock.shooter.ui.hud.Ammo.AmmoType;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum Wearpon
    {
        //                  ammo,                           shotRange,  modDepth,   modAngle,   magazineSize    shots,  modShots,       afterShotDelay,     shotNeedsKeyRelease,
        ECrowbar(           null,                           2.0f,       0,          0,          0,              1,      0,              5,                  false       ),
        EPistol(            AmmoType.EBullet9mm,            25.0f,      10,         10,         8,              1,      0,              3,                  true        ),
        EAssaultRifle(      AmmoType.ERifle51mm,            45.0f,      35,         35,         9999,           1,      0,              0,                  false       ),
        EScattergun(        AmmoType.EScattergunShells,     15.0f,      50,         50,         2,              10,     3,              5,                  false       ),
        EWaltherPPK(        AmmoType.EBullet9mm,            10.0f,      3,          3,          7,              1,      0,              1,                  true        ),
        ERCP180(            AmmoType.ERifle51mm,            50.0f,      10,         10,         2500,           5,      3,              1,                  false       ),
//      EAutomaticShotgun(  AmmoType.shotgunShells,         15.0f,      50,         50,         2,              10,     3,              5,                  false       ),
        ;

        private     static  final   boolean             SHOW_MAGAZINE_SIZE          = true;

        private     static          BufferedImage[]     wearponImages               = null;

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

        public final float getCurrentIrregularityDepth()
        {
            //return modifier-z for the current wearpon
            return ( UMath.getRandom( -iWearponIrregularityDepth, iWearponIrregularityDepth ) * 0.01f );
        }

        public final float getCurrentIrregularityAngle()
        {
            //return modifier-z for the current wearpon
            return ( UMath.getRandom( -iWearponIrregularityAngle, iWearponIrregularityAngle ) * 0.01f );
        }

        public final int getCurrentMagazineSize()
        {
            //return modifier-z for the current wearpon
            return iMagazineSize;
        }

        public final int getCurrentShotCount()
        {
            return iCurrentShotCount + UMath.getRandom( -iCurrentShotCountRandomMod, iCurrentShotCountRandomMod );
        }

        public final boolean getCurrentShotNeedsKeyRelease()
        {
            return iShotNeedsKeyRelease;
        }

        public final void draw2D( Graphics2D g )
        {
            int             modX    = (int)(  20 * Player.singleton.getWalkingAngle2Modifier() );
            int             modY    = -(int)( 10 * Player.singleton.getWalkingAngle3Modifier() );
            BufferedImage   img     = wearponImages[ ordinal() ];

            //hide/show animation?
            if ( HUD.singleton.animation > 0 )
            {
                switch ( HUD.singleton.animationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }
                    case EAnimationHide:
                    {
                        modY += img.getHeight() - HUD.singleton.animation * img.getHeight() / Shooter.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                    case EAnimationShow:
                    {
                        modY += HUD.singleton.animation * img.getHeight() / Shooter.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                }
            }

            g.drawImage
            (
                img,
                modX + GLPanel.PANEL_WIDTH  - img.getWidth(),
                modY + img.getHeight() / 8 + GLPanel.PANEL_HEIGHT - img.getHeight(),
                GLPanel.glPanel
            );
        }

        /**************************************************************************************
        *   Draws a 3D model of the current holding wearpon in front of the player's eye.
        *
        *   @param  gl  The gl to draw onto.
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
        public final void drawAmmo( Graphics2D g )
        {
            //draw ammo if the wearpon uses ammo
            if ( iAmmoType != null )
            {
                GLPanel.releaseClip( g );
                Strings.drawString( g, Colors.EAmmoFg.col, null, Colors.EAmmoOutline.col, Fonts.EAmmo, Anchor.EAnchorRightBottom, iMagazineAmmo + " | " + ( SHOW_MAGAZINE_SIZE ? "" : iMagazineSize + " | "  ) + Player.singleton.ammo.getAmmo( iAmmoType ), GLPanel.PANEL_WIDTH  - Offset.EBorderHudX, GLPanel.PANEL_HEIGHT - Offset.EBorderHudY );
            }
        }

        public static final void drawCrosshair( Graphics g )
        {
            int centerX = GLPanel.PANEL_WIDTH  / 2;
            int centerY = GLPanel.PANEL_HEIGHT / 2;

            centerY += 42;

            GLPanel.releaseClip( g );
            g.setColor( Color.BLACK );
            g.drawLine( centerX - 5, centerY, centerX - 10, centerY );
            g.drawLine( centerX + 5, centerY, centerX + 10, centerY );
            g.drawLine( centerX, centerY - 5, centerX, centerY - 10 );
            g.drawLine( centerX, centerY + 5, centerX, centerY + 10 );
        }

        public static final void loadImages()
        {
            wearponImages   = new BufferedImage[ Wearpon.values().length ];
            int i = 0;
            try
            {
                for ( i = 0; i < wearponImages.length; ++i )
                {
                    //Debug.DEBUG_OUT( "loading " + PATH_IMAGES_WEARPONS + i + ".png" );
                    wearponImages[ i ] = ImageIO.read( Wearpon.class.getResourceAsStream( Path.EWearpons.url + i + ".png" ) );
                }
            }
            catch( Exception e )
            {
                Debug.err( "Error loading wearpon-Image [" + i + "]" );
            }
        }

        public final void fire()
        {
            if ( currentAfterShotDelay == 0 )
            {
                if ( iAmmoType == null )
                {
                    Debug.info( "Perform close-combat attack" );
                }
                else
                {
                    if ( iMagazineAmmo == 0 )
                    {
                        return;
                    }

                    //reduce magazine-ammo by 1
                    --iMagazineAmmo;

                    //launch sound-fx
                    Sound.playSoundFx( Sound.ESoundBullet1 );

                    //launch number of shots this gun fires
                    for ( int i = 0; i < iCurrentShotCount; ++i )
                    {
                        Shot shot = Player.singleton.getShot();

                        //clear all existent debug points and the shot-line
                        DebugPoint.deleteAll();
                        shot.drawShotLine();

                        //launch the shot on the current level
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
            if ( HUD.singleton.animation == 0 )
            {
                //if the wearpon has ammo
                if ( Player.singleton.ammo.getAmmo( iAmmoType ) > 0 )
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
            int ammo = Player.singleton.ammo.getAmmo( iAmmoType );

            //put unused ammo back onto the stack!
            ammo  += iMagazineAmmo;

            int ammoToReload = ( ammo >= iMagazineSize ? iMagazineSize : ammo );
            iMagazineAmmo = ammoToReload;

            Player.singleton.ammo.substractAmmo( iAmmoType, ammoToReload );

            HUD.singleton.startHudAnimation( HUD.Animation.EAnimationShow, null );
        }

        public final void handleWearpon()
        {
            if ( currentAfterShotDelay > 0 )
            {
                --currentAfterShotDelay;
            }
            else if ( HUD.singleton.animation > 0 )
            {
                //no actions while being animated (hide/show)!
            }
            else
            {
                //reload?
                if ( MainThread.launchWearponReload )
                {
                    Debug.info( "reload wearpon!" );
                    //reload if the wearpon uses ammo
                    MainThread.launchWearponReload = false;
                    if ( iAmmoType != null ) orderReload();
                }
                //shot?
                else if ( MainThread.launchShot )
                {
                    //check if wearpon has enough ammo
                    fire();
                }
            }
        }

        public final void cheatAmmo()
        {
            iMagazineAmmo = 99999;
        }

        public final float getShotRange()
        {
            return iShotRange;
        }

        public final BulletHole.PointSize getBulletHoleSize()
        {
            return iAmmoType.bulletHoleSize;
        }
    }
