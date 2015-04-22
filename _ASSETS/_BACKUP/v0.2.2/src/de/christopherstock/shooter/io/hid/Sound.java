/*  $Id: Sound.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    import  java.io.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.shooter.*;
    import  javazoom.jl.decoder.*;
    import  javazoom.jl.player.*;

    /**************************************************************************************
    *   The sound system.
    *
    *   TODO    make a proper SoundManager class !!
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Sound
    {
        public  static  final   int             ESoundBullet1       = 0;
        public  static  final   int             ESoundIndices       = 1;

        public  static          Sound[]         sounds              = null;
        public  static          int             soundToStartNext    = 0;
        public  static          int             currentBgSound      = -1;

        public                  byte[]          bytes               = null;
        public                  Player          playa               = null;

        public Sound( int index )
        {
            //store all bytes
            bytes = LibIO.readStreamBuffered( Sound.class.getResourceAsStream( Path.ESounds.url + index + ".mp3" ) );
        }

        public static final void init()
        {
            sounds = new Sound[ ESoundIndices ];
            for ( int i = 0; i < sounds.length; ++i )
            {
                sounds[ i ] = new Sound( i );
            }
        }

        public final void play()
        {
            try
            {
                playa = new Player( new ByteArrayInputStream( bytes ) );
                playa.play();
            }
            catch ( JavaLayerException jle )
            {
                Debug.bugfix.err( "Exception on playing mp3 [" + soundToStartNext + "]" );
            }
        }

        public final void stop()
        {
            playa.close();
        }

        @SuppressWarnings( "unused" )
        public static final void playSoundBg( int index )
        {
            if ( ShooterSettings.DISABLE_SOUND_BG ) return;

            //do not play current bg sound
            if ( index == currentBgSound ) return;

            //stop current
            if ( currentBgSound != -1 ) sounds[ currentBgSound ].stop();
            currentBgSound = index;
            new Thread()
            {
                @Override
                public void run()
                {
                    if ( currentBgSound != -1 ) sounds[ currentBgSound ].play();
                }
            }.start();
        }

        @SuppressWarnings( "unused" )
        public static final void playSoundFx( int index )
        {
            if ( ShooterSettings.DISABLE_SOUND_FX ) return;

            soundToStartNext = index;

            new Thread()
            {
                @Override
                public void run()
                {
                    sounds[ soundToStartNext ].play();
                }
            }.start();
        }
    }
