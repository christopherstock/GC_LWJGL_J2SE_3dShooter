/*  $Id: LibSoundFactory.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io.hid;

    import  java.awt.geom.*;
    import  java.io.*;
    import  javax.sound.sampled.*;
    import  de.christopherstock.lib.*;

    /**************************************************************************************
    *   A factory being initialized with one sound.
    *   It delivers one-shot sound-clips from this sound.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class LibSoundFactory
    {
        protected               LibDebug        iDebug          = null;
        protected               byte[]          iBytes          = null;
        protected               DataLine.Info   iInfo           = null;
        protected               AudioFormat     iAudioFormat    = null;

        public LibSoundFactory( InputStream aInputStream, LibDebug aDebug ) throws Throwable
        {
            iDebug  = aDebug;

            try
            {
                if ( aInputStream == null ) throw new FileNotFoundException( "sound is null!" );

                AudioInputStream    audioInputStream    = AudioSystem.getAudioInputStream( aInputStream );
                                    iAudioFormat        = audioInputStream.getFormat();
                int                 size                = (int) ( iAudioFormat.getFrameSize() * audioInputStream.getFrameLength() );

                iInfo   = new DataLine.Info( Clip.class, iAudioFormat, size );
                iBytes  = new byte[ size ];
                audioInputStream.read( iBytes, 0, size );
            }
            catch ( Throwable t )
            {
                iDebug.trace( t );
                throw t;
            }
        }

        public final LibSound getInstancedClip( float volume, float balance, int delay, Point2D.Float aDistantLocation )
        {
            LibSound sound = new LibSound( iDebug, this, volume, balance, delay, aDistantLocation );
            //if ( aDistantLocation != null ) sound.updateDistancedSound();
            return sound;
        }
    }
