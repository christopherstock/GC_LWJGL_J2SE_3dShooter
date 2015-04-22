/*  $Id: Gadget.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.GLImage.ImageUsage;

    /**************************************************************************************
    *   The Gadgets / ( non-wearpons ) the player can hold.
    *
    *   @author     Christopher Stock
    *   @version    0.2
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

        public          static          GLImage[]       gadgetImages                = null;

        public                          boolean         ownedByPlayer               = false;

        private Gadget()
        {
        }

        public final void drawOrtho()
        {
            int             modX    = (int)(  20 * Level.currentPlayer().getWalkingAngleCarriedModifierX() );
            int             modY    = -(int)( 10 * Level.currentPlayer().getWalkingAngleCarriedModifierY() );
            GLImage   img     = gadgetImages[ ordinal() ];

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
                        modY -= img.height - HUD.singleton.animationPlayerRightHand * img.height / ShooterSettings.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                    case EAnimationShow:
                    {
                        modY -= HUD.singleton.animationPlayerRightHand * img.height / ShooterSettings.TICKS_WEARPON_HIDE_SHOW;
                        break;
                    }
                }
            }

            //draw ortho on gl
            GLView.singleton.drawOrthoBitmapBytes
            (
                img,
                modX + GLPanel.singleton.width - img.width,
                modY - img.height / 8
            );
/*
            //draw on Graphics2D
            g.drawImage
            (
                img,
                modX + GLPanel.glPanel.width  - img.getWidth(),
                modY + img.getHeight() / 8 + GLPanel.glPanel.height - img.getHeight(),
                GLPanel.glPanel.getNativePanel()
            );
*/
        }

        public static final void loadImages()
        {
            gadgetImages = GLImage.loadAll( Gadget.values().length, Path.EGadgets.url, ".png", ImageUsage.EOrtho );
        }

        public final void handleGadget()
        {

        }
    }
