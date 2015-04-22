/*  $Id: HUDMessage.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.util.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;
    import  de.christopherstock.shooter.ShooterSettings.OffsetsOrtho;
    import  de.christopherstock.shooter.game.*;

    /**************************************************************************************
    *   The HUD messages system. HUD messages pop up to give the user a short notice
    *   about an event. E.g. picking up items.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public final class HUDMessage
    {
        private     static  final   float                   OPACITY_HUD_MSG         = 1.0f;

        private     static  final   int                     TICKS_POP_UP            = 2;
        private     static  final   int                     TICKS_STILL             = 50;
        private     static  final   int                     TICKS_POP_DOWN          = 10;

        private     static  final   int                     ANIM_STATE_POP_UP       = 0;
        private     static  final   int                     ANIM_STATE_STILL        = 1;
        private     static  final   int                     ANIM_STATE_POP_DOWN     = 2;
        private     static  final   int                     ANIM_STATE_DISABLED     = 3;

        private     static          Vector<HUDMessage>      messageQueue            = new Vector<HUDMessage>();

        private                     int                     iAnim                   = 0;
        private                     int                     iAnimState              = ANIM_STATE_POP_UP;
        private                     LibGLImage              iTextImg                = null;
        private                     String                  iText                   = null;

        public HUDMessage( String aText )
        {
            iAnimState  = ANIM_STATE_POP_UP;
            iAnim       = TICKS_POP_UP - 1;
            iText       = aText;
        }

        public final void show()
        {
            //init img and add this message to the queue
            iTextImg       = LibGLImage.getFromString
            (
                iText,
                Fonts.EAvatarMessage,
                ShooterSettings.Colors.EHudMsgFg.colARGB,
                null,
                ShooterSettings.Colors.EHudMsgOutline.colARGB,
                ShooterDebug.glimage
            );
            messageQueue.add( this );
        }

        public static final void animateAll()
        {
            //browse reversed for easy pruning
            for ( int j = messageQueue.size() - 1; j >= 0; --j )
            {
                if ( messageQueue.elementAt( j ).iAnimState == ANIM_STATE_DISABLED )
                {
                    messageQueue.removeElementAt( j );
                }
                else
                {
                    messageQueue.elementAt( j ).animate();
                }
            }
        }

        private void animate()
        {
            //next animation-tick!
            if ( iAnim > -1 ) --iAnim;

            if ( iAnim == -1 )
            {
                //check next state
                switch ( iAnimState )
                {
                    case ANIM_STATE_POP_UP:
                    {
                        iAnim        = TICKS_STILL - 1;
                        iAnimState   = ANIM_STATE_STILL;
                        break;
                    }

                    case ANIM_STATE_STILL:
                    {
                        iAnim        = TICKS_POP_DOWN - 1;
                        iAnimState   = ANIM_STATE_POP_DOWN;
                        break;
                    }

                    case ANIM_STATE_POP_DOWN:
                    {
                        iAnimState   = ANIM_STATE_DISABLED;
                        break;
                    }
                }
            }
        }

        public static final void drawAllMessages()
        {
            //only if messages are available
            if ( messageQueue.size() > 0 )
            {
                int drawY = OffsetsOrtho.EBorderHudY + ( Level.currentPlayer().showAmmoInHUD() ? 2 * messageQueue.elementAt( 0 ).iTextImg.height : 0 );

                for ( int i = messageQueue.size() - 1; i >= 0; --i )
                {
                    messageQueue.elementAt( i ).draw( drawY );
                    drawY += messageQueue.elementAt( i ).iTextImg.height;
                }
            }
        }

        private final void draw( int drawY )
        {
            //only draw if an avatar-animation is active
            if ( iAnim > -1 )
            {
                //get panel's current alpha
                float     alphaFg = 0;
                switch ( iAnimState )
                {
                    case ANIM_STATE_DISABLED:
                    {
                        return;
                    }
                    case ANIM_STATE_POP_UP:
                    {
                        alphaFg     = OPACITY_HUD_MSG - OPACITY_HUD_MSG * iAnim / TICKS_POP_UP;
                        break;
                    }
                    case ANIM_STATE_STILL:
                    {
                        alphaFg     = OPACITY_HUD_MSG;
                        break;
                    }
                    case ANIM_STATE_POP_DOWN:
                    {
                        alphaFg     = OPACITY_HUD_MSG * iAnim / TICKS_POP_DOWN;
                        break;
                    }
                }

                //draw text
                int x = LibGL3D.panel.width  - OffsetsOrtho.EBorderHudX - iTextImg.width;
                LibGL3D.view.drawOrthoBitmapBytes( iTextImg, x, drawY, alphaFg );
            }
        }
    }
