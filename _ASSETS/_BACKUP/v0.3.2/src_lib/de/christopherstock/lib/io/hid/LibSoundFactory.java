/*  $Id: LibIO.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io.hid;

    import  java.io.*;
    import  javax.sound.sampled.*;
    import  de.christopherstock.lib.*;

    /**************************************************************************************
    *   A one shot sound player.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class LibSoundFactory
    {
        protected               LibDebug        iDebug          = null;
        protected               byte[]          iBytes          = null;
        protected               DataLine.Info   iInfo           = null;
        protected               AudioFormat     iAudioFormat    = null;

        public LibSoundFactory( InputStream aInputStream, LibDebug aDebug )
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
            }
        }

        public final LibSound getInstancedClip( float volume, float balance )
        {
            LibSound sound = new LibSound( iDebug, this, volume, balance );
            return sound;
        }
    }
