/*  $Id: LibSound.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io.hid;

    import  java.awt.geom.*;
    import  javax.sound.sampled.*;
    import  javax.sound.sampled.LineEvent.*;
    import  de.christopherstock.lib.*;

    /**************************************************************************************
    *   A one shot threaded sound.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class LibSound extends Thread implements LineListener
    {
        public      static  final       float               VOLUME_MAX              = 1.0f;
        public      static  final       float               VOLUME_MUTE             = 0.0f;
        public      static  final       float               BALANCE_ONLY_LEFT       = -1.0f;
        public      static  final       float               BALANCE_ONLY_RIGHT      = 1.0f;
        public      static  final       float               BALANCE_BOTH            = 0.0f;

        private                         Clip                iClip                   = null;
        private                         LibDebug            iDebug                  = null;
        private                         LibSoundFactory     iFactory                = null;
        private                         FloatControl        iVolumeControl          = null;
        private                         FloatControl        iBalanceControl         = null;

        private                         float               iVolume                 = 0.0f;
        private                         float               iBalance                = 0.0f;
        public                          int                 iDelay                  = 0;

        private                         boolean             iSoundHasFinished       = false;

        private                         Point2D.Float       iDistantLocation        = null;

        public LibSound( LibDebug aDebug, LibSoundFactory aFactory, float aVolume, float aBalance, int aDelay, Point2D.Float aDistantLocation )
        {
            iDebug   = aDebug;
            iFactory = aFactory;

            iVolume  = aVolume;
            iBalance = aBalance;
            iDelay   = aDelay;

            iDistantLocation = aDistantLocation;

            clipVolume();
            clipBalance();
        }

        @Override
        public void run()
        {
            try
            {
                iClip = (Clip) AudioSystem.getLine( iFactory.iInfo );
                iClip.open( iFactory.iAudioFormat, iFactory.iBytes, 0, iFactory.iBytes.length );

                //volume control
                iVolumeControl = (FloatControl)iClip.getControl( FloatControl.Type.MASTER_GAIN );
                setVolume( iVolume );

                //ignore balance for now!
/*
                try
                {
                    //balance control, -1.0f to 1.0f, 0.0f is both speakers 50%
                    iBalanceControl = (FloatControl)iClip.getControl( FloatControl.Type.BALANCE );
                    setBalance( iBalance );
                }
                catch ( Exception e )
                {
                    iDebug.err( "Unsupported control type 'balance' for this wav" );
                }
*/
                //start clip from the beginning ( obsolete )
                iClip.setMicrosecondPosition( 0 );
                iClip.addLineListener( this );

                //play the sound
                iClip.start();
            }
            catch ( Throwable t )
            {
                iDebug.err( "Exception on playing java player" );
                iDebug.trace( t );
            }
        }

        public void setVolume( float aVolume )
        {
            iVolume = aVolume;
            clipVolume();

            if ( iVolumeControl != null )
            {
                float   dB = (float)( Math.log( iVolume ) / Math.log( 10.0f ) * 20.0f );
                iVolumeControl.setValue( dB );
            }
        }

        public void setBalance( float aBalance )
        {
            iBalance = aBalance;
            clipBalance();

            if ( iBalanceControl != null )
            {
                iBalanceControl.setValue( iBalance );
            }
        }

        public void clipVolume()
        {
            if ( iVolume < VOLUME_MUTE ) iVolume = VOLUME_MUTE;
            if ( iVolume > VOLUME_MAX  ) iVolume = VOLUME_MAX;
        }

        public void clipBalance()
        {
            if ( iBalance < BALANCE_ONLY_LEFT  ) iBalance = BALANCE_ONLY_LEFT;
            if ( iBalance > BALANCE_ONLY_RIGHT ) iBalance = BALANCE_ONLY_RIGHT;
        }

        public boolean hasSoundFinished()
        {
            return iSoundHasFinished;
        }

        @Override
        public void update( LineEvent le )
        {
            LineEvent.Type t = le.getType();

            if ( t == Type.STOP || t == Type.CLOSE )
            {
                //iDebug.err( "sound stops!" );
                iSoundHasFinished = true;
            }
        }

        public boolean isDistanced()
        {
            return ( iDistantLocation != null );
        }

        public void updateDistancedSound( float volume )
        {
            //clip volume
            if ( volume < LibSound.VOLUME_MUTE ) volume = LibSound.VOLUME_MUTE;
            if ( volume > LibSound.VOLUME_MAX  ) volume = LibSound.VOLUME_MAX;

            setVolume( volume );
            //iDebug.out( "update volume to:  volume [" + volume + "]"  );
        }

        public final Point2D.Float getDistantLocation()
        {
            return iDistantLocation;
        }
    }
