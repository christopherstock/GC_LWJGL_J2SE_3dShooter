/*  $Id: Fps.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.Fonts;
    import  de.christopherstock.shooter.ShooterSettings.OffsetsOrtho;

    /**************************************************************************************
    *   The Frames Per Second display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class Fps
    {
        private     static          long            startMeassuringMillis       = 0;
        private     static          int             framesDrawn                 = 0;
        private     static          LibGLImage      currentFps                  = null;

        public static final void finishedDrawing()
        {
            //init fps-counter
            if ( startMeassuringMillis == 0 )
            {
                framesDrawn             = 0;
                startMeassuringMillis   = System.currentTimeMillis();
            }
            else
            {
                //increase number of drawn frames
                ++framesDrawn;

                //check if 1 sec is over
                if ( System.currentTimeMillis() - startMeassuringMillis >= Lib.MILLIS_PER_SECOND )
                {
                    currentFps = LibGLImage.getFromString
                    (
                        framesDrawn + " fps",
                        Fonts.EFps,
                        ShooterSettings.Colors.EFpsFg.colABGR,
                        null,
                        ShooterSettings.Colors.EFpsOutline.colABGR,
                        ShooterDebug.glimage
                    );
                    framesDrawn             = 0;
                    startMeassuringMillis   = System.currentTimeMillis();
                }
            }
        }

        public static final void draw()
        {
            if ( currentFps != null )
            {
                LibGL3D.view.drawOrthoBitmapBytes( currentFps, LibGL3D.panel.width - OffsetsOrtho.EBorderHudX - currentFps.width, LibGL3D.panel.height - OffsetsOrtho.EBorderHudY - currentFps.height );
            }
        }
    }
