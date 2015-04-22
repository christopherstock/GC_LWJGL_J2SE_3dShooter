/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui;

    import  java.awt.*;
    import  java.awt.image.*;
    import  java.util.*;

import de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.*;


    /**************************************************************************************
    *   The Avatar.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public final class AvatarMessage
    {
        private     static  final   int                     EWoman                  = 0;
        private     static  final   int                     EAvatarImagesIndices    = 1;

        private     static  final   int                     OPACITY_PANEL_BG        = 192;
        private     static  final   int                     OPACITY_AVATAR_IMG      = 255;

        private     static  final   int                     ANIM_TICKS_POP_UP       = 5;
        private     static  final   int                     ANIM_TICKS_STILL        = 30;
        private     static  final   int                     ANIM_TICKS_POP_DOWN     = 5;

        private     static  final   int                     ANIM_STATE_DISABLED     = 0;
        private     static  final   int                     ANIM_STATE_POP_UP       = 1;
        private     static  final   int                     ANIM_STATE_STILL        = 2;
        private     static  final   int                     ANIM_STATE_POP_DOWN     = 3;

        private     static          Vector<AvatarMessage>   messageQueue            = new Vector<AvatarMessage>();
        private     static          BufferedImage[]         images                  = null;

        private     static          int                     anim                    = 0;
        private     static          int                     animState               = ANIM_STATE_DISABLED;

        private                     String[]                textLines               = null;

        private AvatarMessage( String text )
        {
            this.textLines = Strings.breakLinesOptimized( (Graphics2D)GLPanel.glPanel.getGraphics(), text, Fonts.EAvatarMessage, GLPanel.PANEL_WIDTH - Offset.EAvatarImageX - images[ 0 ].getWidth() - Offset.EAvatarTextX - Offset.EBorderHudX );
        }

        public static final void loadImages()
        {
            images = Images.readAllBufferedImages( EAvatarImagesIndices, Path.EAvatars.url, ".jpg", true );
        }

        public static final void showMessage( String msg )
        {
            //add a message to the queue
            messageQueue.add( new AvatarMessage( msg ) );
        }

        public static final void animate()
        {
            //next animation-tick!
            if ( anim > -1 ) --anim;

            if ( anim == -1 )
            {
                //check next state
                switch ( animState )
                {
                    case ANIM_STATE_DISABLED:
                    {
                        //start next message if available
                        if ( messageQueue.size() > 0 )
                        {
                            //start the msg-animation
                            anim        = ANIM_TICKS_POP_UP - 1;
                            animState   = ANIM_STATE_POP_UP;
                        }
                        break;
                    }

                    case ANIM_STATE_POP_UP:
                    {
                        anim        = ANIM_TICKS_STILL - 1;
                        animState   = ANIM_STATE_STILL;
                        break;
                    }

                    case ANIM_STATE_STILL:
                    {
                        anim        = ANIM_TICKS_POP_DOWN - 1;
                        animState   = ANIM_STATE_POP_DOWN;
                        break;
                    }

                    case ANIM_STATE_POP_DOWN:
                    {
                        animState   = ANIM_STATE_DISABLED;

                        //pop 1st message
                        messageQueue.removeElementAt( 0 );

                        break;
                    }
                }
            }
        }

        public static final void drawMessage( Graphics2D g )
        {
            //only draw if an avatar-animation is active
            if ( anim > -1 && messageQueue.size() > 0 )
            {
                //get panel's current alpha
                int     alphaBg = 0;
                int     alphaFg = 0;
                switch ( animState )
                {
                    case ANIM_STATE_POP_UP:
                    {
                        alphaBg     = OPACITY_PANEL_BG   - OPACITY_PANEL_BG   * anim / ANIM_TICKS_POP_UP;
                        alphaFg     = OPACITY_AVATAR_IMG - OPACITY_AVATAR_IMG * anim / ANIM_TICKS_POP_UP;
                        break;
                    }
                    case ANIM_STATE_STILL:
                    {
                        alphaBg     = OPACITY_PANEL_BG;
                        alphaFg     = OPACITY_AVATAR_IMG;
                        break;
                    }
                    case ANIM_STATE_POP_DOWN:
                    {
                        alphaBg     = OPACITY_PANEL_BG   * anim / ANIM_TICKS_POP_DOWN;
                        alphaFg     = OPACITY_AVATAR_IMG * anim / ANIM_TICKS_POP_DOWN;
                        break;
                    }
                }

                float alphaFg2F = ( alphaFg / 255.0f );

                //draw panel
                GLPanel.releaseClip( g );
                Color originalPanelBgCol = new Color( Colors.EAvatarMessagePanelBg.rgb );
                Color panelBgCol         = new Color( originalPanelBgCol.getRed(), originalPanelBgCol.getGreen(), originalPanelBgCol.getBlue(), alphaBg );
                g.setPaint( panelBgCol );
                g.fillRect( 0, 0, GLPanel.PANEL_WIDTH, Offset.EAvatarBgPanelHeight );

                //draw text
                int blockHeight = messageQueue.elementAt( 0 ).textLines.length * Strings.getStringHeight( g, Fonts.EAvatarMessage );
                int x = Offset.EAvatarImageX + images[ EWoman ].getWidth() + Offset.EAvatarTextX;
                int y = ( Offset.EAvatarBgPanelHeight - blockHeight ) / 2;
                Color originalTextCol = new Color( Colors.EAvatarMessageText.rgb );
                Color textCol = new Color( originalTextCol.getRed(), originalTextCol.getGreen(), originalTextCol.getBlue(), alphaFg );
                Color originalTextOutlineCol = new Color( Colors.EAvatarMessageTextOutline.rgb );
                Color textOutlineCol = new Color( originalTextOutlineCol.getRed(), originalTextOutlineCol.getGreen(), originalTextOutlineCol.getBlue(), alphaFg );
                for ( String line : messageQueue.elementAt( 0 ).textLines )
                {
                    Strings.drawString( g, textCol, null, textOutlineCol, Fonts.EAvatarMessage, Anchor.EAnchorLeftTop, line, x, y );
                    y += Strings.getStringHeight( g, Fonts.EAvatarMessage );
                }

                //draw avatar alpha-scaled
                float[]     scales = { 1f, 1f, 1f, alphaFg2F };
                float[]     offsets = new float[ 4 ];
                RescaleOp   rop = new RescaleOp( scales, offsets, null );
                y = ( Offset.EAvatarBgPanelHeight - images[ EWoman ].getHeight() ) / 2;
                g.drawImage( images[ EWoman ], rop, Offset.EAvatarImageX, y );

            }
        }
    }
