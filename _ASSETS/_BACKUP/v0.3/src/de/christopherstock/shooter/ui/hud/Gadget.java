/*  $Id: Gadget.java 249 2011-02-02 18:13:34Z jenetic.bytemare $
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
    *   @version    0.3
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
            if ( HUD.singleton.animationPlayerRightHand > 0 )
            {
                switch ( HUD.singleton.animationState )
                {
                    case EAnimationNone:
                    {
                        break;
                    }
                    case EAnimationHide:
                    {
                        modY -= gadgetImage.height - HUD.singleton.animationPlayerRightHand * gadgetImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                    case EAnimationShow:
                    {
                        modY -= HUD.singleton.animationPlayerRightHand * gadgetImage.height / ShooterSettings.Performance.TICKS_WEARPON_HIDE_SHOW;
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
            BufferedImage bufferedImage = LibImage.load( ShooterSettings.Path.EGadgets.url + this.toString() + LibExtension.png.getSpecifier(), ShooterDebugSystem.glimage, false );
            gadgetImage = new LibGLImage( bufferedImage, ImageUsage.EOrtho, ShooterDebugSystem.glimage, true );
        }

        public final void handleGadget()
        {

        }
    }
