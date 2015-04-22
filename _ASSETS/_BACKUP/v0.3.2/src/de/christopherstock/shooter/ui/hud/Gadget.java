/*  $Id: Gadget.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.image.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.gl.LibGLImage.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;

    /**************************************************************************************
    *   The Gadgets / ( non-wearpons ) the player can hold.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public enum Gadget
    {
        EEnvelope,
        EMacAir,
        EAirMailLetter,
        EBottleVolvic,
        EMobilePhoneSEW890i,
        EChips,
        ;

        public                          LibGLImage      gadgetImage                 = null;
        public                          boolean         ownedByPlayer               = false;

        private Gadget()
        {
        }

        public final void drawOrtho()
        {
            int             modX    = (int)(  20 * Level.currentPlayer().getWalkingAngleCarriedModifierX() );
            int             modY    = -(int)( 10 * Level.currentPlayer().getWalkingAngleCarriedModifierY() );

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
                        modY -= gadgetImage.height - HUD.singleton.iAnimationPlayerRightHand * gadgetImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                    case EAnimationShow:
                    {
                        modY -= HUD.singleton.iAnimationPlayerRightHand * gadgetImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                }
            }

            //draw ortho on gl
            LibGL3D.view.drawOrthoBitmapBytes
            (
                gadgetImage,
                modX + LibGL3D.panel.width - gadgetImage.width,
                modY - gadgetImage.height / 8
            );
        }

        public static final void loadImages()
        {
            for ( Gadget gadget : values() )
            {
                gadget.loadImage();
            }
        }

        private final void loadImage()
        {
            BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.EGadgets.iUrl + this.toString() + LibExtension.png.getSpecifier(), ShooterDebug.glimage, false );
            gadgetImage = new LibGLImage( bufferedImage, ImageUsage.EOrtho, ShooterDebug.glimage, true );
        }

        public final void handleGadget()
        {

        }
    }
