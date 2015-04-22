/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;
    import  java.awt.image.*;
    import  javax.imageio.*;
    import  javax.media.opengl.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.util.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public enum Wearpon
    {
        //                                              shotRange,  modDepth,   modAngle,   magazineSize    shots,  modShots,       afterShotDelay,     shotNeedsKeyRelease,
        ECrowbar(           null,                       2.0f,       0,          0,          0,              1,      0,              5,                  false       ),
        EPistol(            Ammo.bullet9mm,             25.0f,      10,         10,         8,              1,      0,              3,                  true        ),
        EAssaultRifle(      Ammo.rifle51mm,             45.0f,      35,         35,         9999,           1,      0,              0,                  false       ),
        EScattergun(        Ammo.scattergunShells,      15.0f,      50,         50,         2,              10,     3,              5,                  false       ),
        EWaltherPPK(        Ammo.bullet9mm,             10.0f,      3,          3,          7,              1,      0,              1,                  true        ),
        ERCP180(            Ammo.rifle51mm,             50.0f,      10,         10,         2500,           5,      3,              1,                  false       ),
//        EAutomaticShotgun(  Ammo.shotgunShells,         15.0f,      50,         50,         2,              10,     3,              5,                  false       ),
        ;

        private         static          BufferedImage[] wearponImages               = null;

        private                         Ammo            ammoToUse                   = null;
        private                         int             wearponIrregularityDepth    = 0;
        private                         int             wearponIrregularityAngle    = 0;
        private                         int             currentAfterShotDelay       = 0;
        private                         int             afterShotDelay              = 0;
        private                         int             magazineSize                = 0;
        private                         int             magazineAmmo                = 0;
        private                         int             currentShotCount            = 0;
        private                         int             currentShotCountRandomMod   = 0;
        private                         float           shotRange                   = 0.0f;
        private                         boolean         shotNeedsKeyRelease         = false;

        private Wearpon( Ammo ammoToUse, float shotRange, int wearponIrregularityDepth, int wearponIrregularityAngle, int magazineSize, int currentShotCount, int currentShotCountRandomMod, int afterShotDelay, boolean shotNeedsKeyRelease )
        {
            this.ammoToUse                  = ammoToUse;
            this.wearponIrregularityDepth   = wearponIrregularityDepth;
            this.wearponIrregularityAngle   = wearponIrregularityAngle;
            this.magazineSize               = magazineSize;
            this.afterShotDelay             = afterShotDelay;
            this.shotNeedsKeyRelease        = shotNeedsKeyRelease;
            this.currentShotCount           = currentShotCount;
            this.currentShotCountRandomMod  = currentShotCountRandomMod;
            this.magazineAmmo               = 0;
            this.shotRange                  = shotRange;
        }

        public final float getCurrentIrregularityDepth()
        {
            //return modifier-z for the current wearpon
            return ( UMath.getRandom( -wearponIrregularityDepth, wearponIrregularityDepth ) * 0.01f );
        }

        public final float getCurrentIrregularityAngle()
        {
            //return modifier-z for the current wearpon
            return ( UMath.getRandom( -wearponIrregularityAngle, wearponIrregularityAngle ) * 0.01f );
        }

        public final int getCurrentMagazineSize()
        {
            //return modifier-z for the current wearpon
            return magazineSize;
        }

        public final int getCurrentShotCount()
        {
            return currentShotCount + UMath.getRandom( -currentShotCountRandomMod, currentShotCountRandomMod );
        }

        public final boolean getCurrentShotNeedsKeyRelease()
        {
            return shotNeedsKeyRelease;
        }

        public final void draw2D( Graphics2D g )
        {
            int             modX    = (int)(  20 * Player.user.getWalkingAngle2Modifier() );
            int             modY    = -(int)( 10 * Player.user.getWalkingAngle3Modifier() );
            BufferedImage   img     = wearponImages[ ordinal() ];

            //hide/show animation?
            if ( HUD.animation > 0 )
            {
                switch ( HUD.animationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }
                    case EAnimationHide:
                    {
                        modY += img.getHeight() - HUD.animation * img.getHeight() / Shooter.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                    case EAnimationShow:
                    {
                        modY += HUD.animation * img.getHeight() / Shooter.TICKS_WEARPON_HIDE_SHOW;
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

        public final void drawAmmo( Graphics2D g )
        {
            //draw ammo if the wearpon uses ammo
            if ( ammoToUse != null )
            {
                GLPanel.releaseClip( g );
                Strings.drawString( g, Colors.EAmmoFg.col, null, Colors.EAmmoOutline.col, Fonts.EAmmo, Anchor.EAnchorRightBottom, magazineAmmo + " | " + ( true ? "" : magazineSize + " | "  ) + ammoToUse.ammo, GLPanel.PANEL_WIDTH  - Offset.EBorderHudX, GLPanel.PANEL_HEIGHT - Offset.EBorderHudY );
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
                if ( ammoToUse == null )
                {
                    Debug.info( "Perform close-combat attack" );
                }
                else
                {
                    if ( magazineAmmo == 0 )
                    {
                        return;
                    }

                    //reduce magazine-ammo by 1
                    --magazineAmmo;

                    //launch sound-fx
                    Sound.playSoundFx( Sound.ESoundBullet1 );

                    //launch number of shots this gun fires
                    for ( int i = 0; i < currentShotCount; ++i )
                    {
                        Shot shot = Player.user.getShot();

                        //clear all existent debug points and the shot-line
                        DebugPoint.deleteAll();
                        shot.drawShotLine();

                        //launch the shot on the current level
                        shot.launch();
                    }
                }

                //set delay after this shot
                currentAfterShotDelay = afterShotDelay;

            }
        }

        public final void orderReload()
        {
            //if no animation is active
            if ( HUD.animation == 0 )
            {
                //if the wearpon has ammo
                if ( ammoToUse.ammo > 0 )
                {
                    //if the wearpon is not fully loaded
                    if ( magazineSize != magazineAmmo )
                    {
                        HUD.startHudAnimation( HUD.Animation.EAnimationHide, HUD.ChangeAction.EActionReload );
                    }
                }
            }
        }

        public final void reload()
        {
            //put unused ammo back onto the stack!
            ammoToUse.ammo  += magazineAmmo;

            int ammoToReload = ( ammoToUse.ammo >= magazineSize ? magazineSize : ammoToUse.ammo );
            ammoToUse.ammo -= ammoToReload;
            magazineAmmo = ammoToReload;

            HUD.startHudAnimation( HUD.Animation.EAnimationShow, null );

        }

        public final void handleWearpon()
        {
            if ( currentAfterShotDelay > 0 )
            {
                --currentAfterShotDelay;
            }
            else if ( HUD.animation > 0 )
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
                    if ( ammoToUse != null ) orderReload();
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
            magazineAmmo = 99999;
        }

        public final float getShotRange()
        {
            return shotRange;
        }
    }
