/*  $Id: AvatarMessage.java 228 2011-01-26 18:15:12Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  java.awt.*;
    import  java.awt.image.*;
    import  java.util.*;
    import  de.christopherstock.lib.gl.*;
import de.christopherstock.lib.gl.LibGLImage.ImageUsage;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.ShooterSettings.AvatarMessages;
import de.christopherstock.shooter.ShooterSettings.Fonts;
import de.christopherstock.shooter.ShooterSettings.Offset;

    /**************************************************************************************
    *   Represents a pop-up-message with an avatar image and a message.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public final class AvatarMessage
    {
        public static enum AvatarImage
        {
            EWoman,
            EWoman2,
            EMan,
            ;

            public          BufferedImage           img         = null;

            public static final void loadImages()
            {
                for ( AvatarImage avatarImage : values() )
                {
                    avatarImage.loadImage();
                }
            }

            private final void loadImage()
            {
                img = LibImage.load( ShooterSettings.Path.EAvatars.url + this.toString() + LibExtension.png.getSpecifier(), ShooterDebugSystem.glimage, true );
            }
        };

        private static enum AnimState
        {
            EDisabled,
            EPopUp,
            EStill,
            EPopDown,
            ;
        }

        private     static          Vector<AvatarMessage>   messageQueue            = new Vector<AvatarMessage>();

        private     static          int                     anim                    = 0;
        private     static          AnimState               animState               = AnimState.EDisabled;

        private                     LibGLImage              iBar                    = null;
        private                     Font                    iFont                   = null;
        private                     AvatarImage             iImgAvatar              = null;

        private AvatarMessage( AvatarImage aImage, String aText, Font aFont )
        {
            iFont       = aFont;
            iImgAvatar  = aImage;

            //break text in lines
            Graphics2D  g           = LibGL3D.panel.getGraphics();
            String[]    textLines   = LibStrings.breakLinesOptimized( LibGL3D.panel.getGraphics(), aText, iFont, LibGL3D.panel.width - Offset.EAvatarImageX - iImgAvatar.img.getWidth() - Offset.EAvatarTextX - Offset.EBorderHudX );

            //init bar backbuffer
            BufferedImage bar = LibImage.getFullOpaque( ShooterSettings.Colors.EAvatarMessagePanelBg.colABGR, LibGL3D.panel.width, iImgAvatar.img.getHeight() );
            Graphics2D    g2d = (Graphics2D)bar.getGraphics();

            //draw avatar
            g2d.drawImage( iImgAvatar.img, Offset.EAvatarImageX, 0, null );

            //draw text
            int blockHeight = LibStrings.getLinesHeight( g, textLines, iFont, ShooterSettings.HUD.LINE_SPACING_RATIO_EMPTY_LINES );
            int x           = Offset.EAvatarImageX + iImgAvatar.img.getWidth() + Offset.EAvatarTextX;
            int y           = ( Offset.EAvatarBgPanelHeight / 2 ) - ( blockHeight / 2 ); // + ( blockHeight / 2 );
            LibStrings.drawStrings( g2d, textLines, iFont, x, y, ShooterSettings.Colors.EAvatarMessageText.colABGR, null, ShooterSettings.Colors.EAvatarMessageTextOutline.colABGR, ShooterSettings.HUD.LINE_SPACING_RATIO_EMPTY_LINES );

            //create bar
            iBar = new LibGLImage( bar, ImageUsage.EOrtho, ShooterDebugSystem.avatarImages, false );
        }

        public static final void showMessage( AvatarImage img, String msg )
        {
            //add a message to the queue
            messageQueue.add( new AvatarMessage( img, msg, Fonts.EAvatarMessage ) );
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
                    case EDisabled:
                    {
                        //start next message if available
                        if ( messageQueue.size() > 0 )
                        {
                            //start the msg-animation
                            anim        = AvatarMessages.ANIM_TICKS_POP_UP - 1;
                            animState   = AnimState.EPopUp;
                        }
                        break;
                    }

                    case EPopUp:
                    {
                        anim        = AvatarMessages.ANIM_TICKS_STILL - 1;
                        animState   = AnimState.EStill;
                        break;
                    }

                    case EStill:
                    {
                        anim        = AvatarMessages.ANIM_TICKS_POP_DOWN - 1;
                        animState   = AnimState.EPopDown;
                        break;
                    }

                    case EPopDown:
                    {
                        animState   = AnimState.EDisabled;

                        //pop 1st message
                        messageQueue.removeElementAt( 0 );

                        break;
                    }
                }
            }
        }

        public static final void drawMessage()
        {
            //only draw if an avatar-animation is active
            if ( anim > -1 && messageQueue.size() > 0 )
            {
                //draw this msg
                messageQueue.elementAt( 0 ).draw();
            }
        }

        private final void draw()
        {
            //get panel's current alpha
            int alpha = 0;
            switch ( animState )
            {
                case EDisabled:
                {
                    //won't be passed
                    break;
                }

                case EPopUp:
                {
                    alpha     = AvatarMessages.OPACITY_PANEL_BG   - AvatarMessages.OPACITY_PANEL_BG   * anim / AvatarMessages.ANIM_TICKS_POP_UP;
                    break;
                }
                case EStill:
                {
                    alpha     = AvatarMessages.OPACITY_PANEL_BG;
                    break;
                }
                case EPopDown:
                {
                    alpha     = AvatarMessages.OPACITY_PANEL_BG   * anim / AvatarMessages.ANIM_TICKS_POP_DOWN;
                    break;
                }
            }

            //draw bg bar
            LibGL3D.view.drawOrthoBitmapBytes( iBar, 0, LibGL3D.panel.height - iBar.height - Offset.EAvatarImageY, alpha );
        }
    }
