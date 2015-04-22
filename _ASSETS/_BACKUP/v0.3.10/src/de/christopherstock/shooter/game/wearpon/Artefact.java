/*  $Id: Artefact.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.awt.geom.Point2D;
    import  de.christopherstock.lib.Lib.LibAnimation;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.game.LibShot.ShotSpender;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.objects.ItemToPickUp.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.HUD.*;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class Artefact
    {
        public              ArtefactType            iArtefactType                       = null;

        public              int                     iCurrentShotsWithoutKeyRelease      = 0;
        public              long                    iCurrentDelayAfterUse               = 0;
        private             boolean                 iDrawFireFXtick                     = false;
        public              int                     iMagazineAmmo                       = 0;

        public Artefact( ArtefactType aArtefactType )
        {
            iArtefactType           = aArtefactType;
        }

        public final void fire( ShotSpender ss, Point2D.Float shooterXY )
        {
            //check if delay after use is still active
            if ( iCurrentDelayAfterUse > System.currentTimeMillis() )
            {
                //delay after wearpon use
            }
            //try to fire wearpon
            else if ( iArtefactType.iWearponBehaviour.use( this, ss, shooterXY ) )
            {
                //draw fire fx
                iDrawFireFXtick = true;

                //set delay after this shot
                iCurrentDelayAfterUse = System.currentTimeMillis() + iArtefactType.iDelayAfterUse;

                //increase number of shots since last key release
                ++iCurrentShotsWithoutKeyRelease;
            }
        }

        public final void handleArtefact( ShotSpender ss, boolean doFire, AmmoSet ammoSet )
        {
            //check artefact actions
            if ( iCurrentDelayAfterUse > System.currentTimeMillis() )
            {
                //delay after wearpon use
            }
            else
            {
                //reload?
                if ( Keys.reload.iLaunchAction || MouseInput.mouseHoldReload )
                {
                    //reload if the wearpon uses ammo
                    Keys.reload.iLaunchAction   = false;
                    MouseInput.mouseHoldReload  = false;

                    //if the wearpon has ammo
                    if ( iArtefactType.isFireArm() )
                    {
                        reload( ammoSet, true, true, null );
                    }
                }
                //shoot
                else if ( doFire )
                {
                    fire( ss, null );
                }
            }

            //animate start / give anim
            if ( iArtefactType.iWearponBehaviour instanceof Gadget )
            {
                ( (Gadget)iArtefactType.iWearponBehaviour ).handleGadget();
            }
        }
        public final void drawOrtho()
        {
            //this is player's gun
            Player p = ShooterGameLevel.currentPlayer();

            int     modX    = ( 20 + (int)(  20 * p.getWalkingAngleCarriedModifierX() ) );      //clip on right border
            int     modY    = ( -(int)( 10 * p.getWalkingAngleCarriedModifierY() ) );

            //hide/show animation?
            if ( Shooter.mainThread.iHUD.animationActive() )
            {
                switch ( Shooter.mainThread.iHUD.iAnimationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }
                    case EAnimationHide:
                    {
                        modY -= iArtefactType.iWearponImage.height - Shooter.mainThread.iHUD.getAnimationRightHand() * iArtefactType.iWearponImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                    case EAnimationShow:
                    {
                        modY -= Shooter.mainThread.iHUD.getAnimationRightHand() * iArtefactType.iWearponImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                }
            }

            //give/take animation?
            int[] modGiveTake = new int[] { 0, 0, };
            if ( iArtefactType.iWearponBehaviour instanceof Gadget )
            {
                modGiveTake = ( (Gadget)iArtefactType.iWearponBehaviour ).getGiveTakeDrawMod();
            }

            //zoom animation?
            if ( p.iZoom != 0.0f )
            {
                modX += iArtefactType.iWearponImage.width  * 0.75f * p.iScaleFactor * 2;
                modY -= iArtefactType.iWearponImage.height * 0.5f  * p.iScaleFactor / 2;
            }

            //draw fire fx behind artefact
            if ( iDrawFireFXtick && iArtefactType.iWearponBehaviour instanceof FireArm )
            {
                //no random translation!
                int         randomX     = 0; //LibMath.getRandom( -5, 5 );
                int         randomY     = 0; //LibMath.getRandom( -5, 5 );

                //only draw if fx image could be read
                if ( iArtefactType.iFXImages.length > 1 )
                {
                    int         randomIndex = LibMath.getRandom( 0, iArtefactType.iFXImages.length - 1 );
                    LibGLImage  fxImage     = iArtefactType.iFXImages[ randomIndex ];

                    LibGL3D.view.drawOrthoBitmapBytes
                    (
                        fxImage,
                        randomX + modX + modGiveTake[ 0 ] + LibGL3D.panel.width - iArtefactType.iFXOffset.x,
                        randomY + modY + modGiveTake[ 1 ] + iArtefactType.iFXOffset.y,
                        1.0f,
                        1.0f + p.iScaleFactor * 1.5f,
                        1.0f + p.iScaleFactor * 1.5f,
                        true
                    );
                }

                iDrawFireFXtick = false;
            }

            //draw artefact
            if ( !Shooter.mainThread.iHUD.iHideWearpon )
            {
                LibGL3D.view.drawOrthoBitmapBytes
                (
                    iArtefactType.iWearponImage,
                    modX + modGiveTake[ 0 ] + LibGL3D.panel.width - iArtefactType.iWearponImage.width,
                    modY + modGiveTake[ 1 ] - iArtefactType.iWearponImage.height / 8,
                    1.0f,
                    1.0f + p.iScaleFactor * 1.5f,
                    1.0f + p.iScaleFactor * 1.5f,
                    true
                );
            }
        }

        public final String getCurrentAmmoStringMagazineAmmo()
        {
            return String.valueOf( iMagazineAmmo );
        }

        public final String getCurrentAmmoStringTotalAmmo( AmmoSet ammoSet )
        {
            return String.valueOf( ammoSet.getAmmo( ( (FireArm)iArtefactType.iWearponBehaviour ).iAmmoType ) );
        }

        public void reload( AmmoSet ammoSet, boolean reloadAnimationRequired, boolean playSound, Point2D.Float reloaderXY )
        {
            //if ammo to reload is available
            if ( ammoSet.getAmmo( ( (FireArm)iArtefactType.iWearponBehaviour ).iAmmoType ) > 0 )
            {
                //if the wearpon is not fully loaded
                if ( ( (FireArm)iArtefactType.iWearponBehaviour ).iMagazineSize != iMagazineAmmo )
                {
                    //start HUD-animation 'hide' only if required
                    if ( reloadAnimationRequired )
                    {
                        //start reload animation
                        Shooter.mainThread.iHUD.startHandAnimation( LibAnimation.EAnimationHide, ChangeAction.EActionReload );
                    }
                    else
                    {
                        //perform reload
                        performReload( ammoSet, playSound, reloaderXY, false );
                    }
                }
            }
        }

        public final void performReload( AmmoSet ammoSet, boolean playSound, Point2D.Float reloaderXY, boolean freeAmmo )
        {
            FireArm fireArm = ( (FireArm)iArtefactType.iWearponBehaviour );

            //put unused ammo back onto the stack!
            ammoSet.addAmmo( fireArm.iAmmoType, iMagazineAmmo );

            //check ammo stock
            int ammo = ammoSet.getAmmo( fireArm.iAmmoType );
            int ammoToReload = ( ammo >= fireArm.iMagazineSize ? fireArm.iMagazineSize : ammo );

            if ( freeAmmo ) ammoToReload = fireArm.iMagazineSize;

            //load ammoToReload into the magazine and substract it from the ammo stock
            iMagazineAmmo = ammoToReload;
            ammoSet.substractAmmo( fireArm.iAmmoType, ammoToReload );

            //play reload sound
            if ( playSound )
            {
                if ( reloaderXY == null )
                {
                    fireArm.getReloadSound().playGlobalFx();
                }
                else
                {
                    fireArm.getReloadSound().playDistancedFx( reloaderXY );
                }
            }
        }

        public final ItemToPickUp getPickUpItem( LibVertex ank )
        {
            if ( iArtefactType.iItemTemplate == null ) return null;
            return new ItemToPickUp( iArtefactType.iItemTemplate, this, ank.x, ank.y, ank.z, LibMath.getRandom( 0, 360 ), Rotating.ENo );
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
    }
