/*  $Id: ShooterSound.java 1279 2014-10-07 21:59:11Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.sound;

    import  java.awt.geom.*;
    import  java.io.*;
    import  java.util.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.io.hid.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.ShooterSettings.Sounds;
    import  de.christopherstock.shooter.level.*;

    /**************************************************************************************
    *   The sound system.
    *
    *   @version    0.3.11
    *   @author     Christopher Stock
    **************************************************************************************/
    public enum SoundFg implements LibSound
    {
        EBulletShell1,
        ECamera,

        EDoorClack1,
        EDoorClose1,
        EDoorOpen1,

        EExplosion1,
        EFemaleGiggle1,
        ELocked1,
        EPickUpItem1,
        EPlayerHit1,

        EReload1,
        EReload2,

        EShotAssault1,
        EShotRifle1,
        EShotShotgun1,
        EShotSilenced,

        ETranquilizerShot,
        EUnlocked1,

        EWallElectric1,
        EWallFlesh1,
        EWallGlass1,
        EWallSolid1,
        EWallWood1,

        ;

        private     static  final   int                     MAX_SOUND_QUEUE_SIZE    = 512;

        private     static          Vector<LibSoundClip>    soundQueue              = new Vector<LibSoundClip>();

        private                     LibSoundFactory         factory                 = null;

        public static final void init()
        {
            //load all sounds
            for ( SoundFg sound : values() )
            {
                sound.loadBytes();
            }
        }

        public static final void onRun()
        {
            //ShooterDebug.sound.out( "maintaining [" + soundQueue.size() + "] sounds" );

            //browse queue reversed
            for ( int i = soundQueue.size() - 1; i >= 0; --i )
            {
                //reference queded item
                LibSoundClip queuedItem = soundQueue.elementAt( i );

                //remove finished sounds
                if ( queuedItem.hasSoundFinished() )
                {
                    soundQueue.removeElement( queuedItem );
                }
                //countdown delayed sounds
                else if ( queuedItem.iDelay > 0 )
                {
                    //subsctract delay
                    --queuedItem.iDelay;

                    //start sound if delay reached 0
                    if ( queuedItem.iDelay == 0 )
                    {
                        queuedItem.start();
                    }
                }
                //adjust volume for currently played sound
                else if ( queuedItem.isDistanced() )
                {
                    float distanceToPlayer = (float)Level.currentPlayer().getCylinder().getCenterHorz().distance( queuedItem.getDistantLocation() );
                    float volume = ( distanceToPlayer <= Sounds.SPEECH_PLAYER_DISTANCE_MAX_VOLUME ? LibSoundClip.VOLUME_MAX : ( 1.0f - ( distanceToPlayer / Sounds.SPEECH_PLAYER_DISTANCE_MUTE ) ) );

                    queuedItem.updateDistancedSound( volume );
                }
            }
        }

        private final void loadBytes()
        {
            String uri = ShooterSettings.Path.ESoundsFg.iUrl + toString() + LibExtension.wav.getSpecifier();

            try
            {
                //PRE-stream sound resource from JAR ( won't work otherwise! )
                ByteArrayInputStream bais = LibIO.preStreamJarResource( uri );

                //store all bytes
                factory = new LibSoundFactory( bais, ShooterDebug.sound );

                //release the stream
                bais.close();
            }
            catch ( Throwable t )
            {
                ShooterDebug.error.out( "Could not read sound file [" + uri + "]" );
                ShooterDebug.error.trace( t );
                System.exit( 0 );
            }
        }

        @Override
        public final void playDistancedFx( Point2D.Float distantLocation )
        {
            playDistancedFx( distantLocation, 0 );
        }

        /**************************************************************************************
        *   Starts a one-shot sound effect being on a specified distance from the listener.
        **************************************************************************************/
        public final void playDistancedFx( Point2D.Float distantLocation, int delay )
        {
            //specify volume from distance
            float distanceToPlayer = (float)Level.currentPlayer().getCylinder().getCenterHorz().distance( distantLocation );
            float volume =
            (
                    distanceToPlayer <= Sounds.SPEECH_PLAYER_DISTANCE_MAX_VOLUME
                ?   LibSoundClip.VOLUME_MAX
                :   ( LibSoundClip.VOLUME_MAX - ( distanceToPlayer / Sounds.SPEECH_PLAYER_DISTANCE_MUTE ) )
            );

            //ShooterDebug.bugfix.out( "play distanced fx with volume [" + volume + "]" );

            playFx( volume, LibSoundClip.BALANCE_BOTH, delay, distantLocation );
        }

        public final void playGlobalFx()
        {
            playGlobalFx( 0 );
        }

        /**************************************************************************************
        *   Starts a one-shot sound effect that is not referenced.
        **************************************************************************************/
        public final void playGlobalFx( int delay )
        {
            playFx( LibSoundClip.VOLUME_MAX, LibSoundClip.BALANCE_BOTH, delay, null );
        }

        /**************************************************************************************
        *   Starts a one-shot sound effect that is not referenced.
        **************************************************************************************/
        private final void playFx( float volume, float balance, int delay, Point2D.Float distantLocation )
        {
            //break on disabled system
            boolean disableSoundFx = General.DISABLE_SOUND_FX;
            if ( disableSoundFx ) return;

            //break on low memory
            //if ( LibSoundClip.disabledByMemory ) return;

            //reference sound fx
            LibSoundClip sound = factory.getInstancedClip( volume, balance, delay, distantLocation );

            try
            {
                //start immediately if not delayed
                if ( delay == 0 )
                {
                    sound.start();
                }

                //add to queue
                if ( soundQueue.size() < MAX_SOUND_QUEUE_SIZE ) soundQueue.addElement( sound );
            }
            catch ( Throwable t )
            {
                ShooterDebug.error.err( "Throwable caught on playing sound fx " + t );
            }
        }
    }
