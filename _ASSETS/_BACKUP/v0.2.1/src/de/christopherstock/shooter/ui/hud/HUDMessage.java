/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;
    import  java.util.*;

import de.christopherstock.shooter.gl.*;
import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The Avatar.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public final class HUDMessage
    {
        private     static  final   int                     OPACITY_HUD_MSG         = 255;

        private     static  final   int                     TICKS_POP_UP       = 2;
        private     static  final   int                     TICKS_STILL        = 50;
        private     static  final   int                     TICKS_POP_DOWN     = 10;

        private     static  final   int                     ANIM_STATE_POP_UP       = 0;
        private     static  final   int                     ANIM_STATE_STILL        = 1;
        private     static  final   int                     ANIM_STATE_POP_DOWN     = 2;
        private     static  final   int                     ANIM_STATE_DISABLED     = 3;

        private     static          Vector<HUDMessage>      messageQueue            = new Vector<HUDMessage>();

        private                     int                     anim                    = 0;
        private                     int                     animState               = ANIM_STATE_POP_UP;

        private                     String                  text                    = null;

        public HUDMessage( String aText )
        {
            text       = aText;
            animState  = ANIM_STATE_POP_UP;
            anim       = TICKS_POP_UP - 1;
        }

        public static final void showMessage( String msg )
        {
            //add a message to the queue
            messageQueue.add( new HUDMessage( msg ) );
        }

        public static final void animateAll()
        {
            //browse reversed for easy pruning
            for ( int j = messageQueue.size() - 1; j >= 0; --j )
            {
                if ( messageQueue.elementAt( j ).animState == ANIM_STATE_DISABLED )
                {
                    messageQueue.removeElementAt( j );
                }
                else
                {
                    messageQueue.elementAt( j ).animate();
                }
            }
        }

        public void animate()
        {
            //next animation-tick!
            if ( anim > -1 ) --anim;

            if ( anim == -1 )
            {
                //check next state
                switch ( animState )
                {
                    case ANIM_STATE_POP_UP:
                    {
                        anim        = TICKS_STILL - 1;
                        animState   = ANIM_STATE_STILL;
                        break;
                    }

                    case ANIM_STATE_STILL:
                    {
                        anim        = TICKS_POP_DOWN - 1;
                        animState   = ANIM_STATE_POP_DOWN;
                        break;
                    }

                    case ANIM_STATE_POP_DOWN:
                    {
                        animState   = ANIM_STATE_DISABLED;
                        break;
                    }
                }
            }
        }

        public static final void drawAllMessages( Graphics2D g )
        {
            int index = messageQueue.size() - 1;
            for ( HUDMessage message : messageQueue )
            {
                message.draw( g, index-- );
            }
        }

        public final void draw( Graphics2D g, int index )
        {
            //only draw if an avatar-animation is active
            if ( anim > -1 )
            {
                //get panel's current alpha
                int     alphaFg = 0;
                switch ( animState )
                {
                    case ANIM_STATE_DISABLED:
                    {
                        return;
                    }
                    case ANIM_STATE_POP_UP:
                    {
                        alphaFg     = OPACITY_HUD_MSG - OPACITY_HUD_MSG * anim / TICKS_POP_UP;
                        break;
                    }
                    case ANIM_STATE_STILL:
                    {
                        alphaFg     = OPACITY_HUD_MSG;
                        break;
                    }
                    case ANIM_STATE_POP_DOWN:
                    {
                        alphaFg     = OPACITY_HUD_MSG * anim / TICKS_POP_DOWN;
                        break;
                    }
                }

                //draw text
                int x = GLPanel.glPanel.width  - Offset.EBorderHudX ;
                int y = GLPanel.glPanel.height - Offset.EBorderHudY - Offset.EHudMsgY - index * 15;
                Color originalTextCol   = new Color( Colors.EHudMsgFg.rgb     );
                Color originalShadowCol = new Color( Colors.EHudMsgOutline.rgb );
                Color textCol   = new Color( originalTextCol.getRed(),   originalTextCol.getGreen(),   originalTextCol.getBlue(),   alphaFg );
                Color outlineCol = new Color( originalShadowCol.getRed(), originalShadowCol.getGreen(), originalShadowCol.getBlue(), alphaFg );

                Strings.drawString( g, textCol, null, outlineCol, Fonts.EAvatarMessage, Anchor.EAnchorRightBottom, text, x, y );

            }
        }
    }
