/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;
    import  java.awt.image.*;
    import  javax.imageio.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.*;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     stock
    *   @version    1.0
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

        private         static          BufferedImage[] gadgetImages                = null;

        public                          boolean         ownedByPlayer               = false;

        private Gadget()
        {
        }

        public final void draw2D( Graphics2D g )
        {
            int             modX    = (int)(  20 * Player.user.getWalkingAngle2Modifier() );
            int             modY    = -(int)( 10 * Player.user.getWalkingAngle3Modifier() );
            BufferedImage   img     = gadgetImages[ ordinal() ];

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

            if ( true ) g.drawImage
            (
                img,
                modX + GLPanel.PANEL_WIDTH  - img.getWidth(),
                modY + img.getHeight() / 8 + GLPanel.PANEL_HEIGHT - img.getHeight(),
                GLPanel.glPanel
            );
        }

        public static final void loadImages()
        {
            gadgetImages = new BufferedImage[ Gadget.values().length ];
            int i = 0;
            try
            {
                for ( i = 0; i < gadgetImages.length; ++i )
                {
                    //Debug.DEBUG_OUT( "loading " + PATH_IMAGES_WEARPONS + i + ".png" );
                    gadgetImages[ i ] = ImageIO.read( Wearpon.class.getResourceAsStream( Path.EGadgets.url + i + ".png" ) );
                }
            }
            catch( Exception e )
            {
                Debug.err( "Error loading gadget-Image [" + i + "]" );
            }
        }

        public final void handleGadget()
        {

        }
    }
