/*  $Id: ShooterSound.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  java.awt.geom.*;
    import  java.io.*;
    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.io.hid.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.ShooterSettings.Sounds;

    /**************************************************************************************
    *   The sound system.
    *
    *   @version    0.3.10
    *   @author     Christopher Stock
    **************************************************************************************/
    public enum ShooterSound
    {
        EBulletShot1,
        EBulletShell1,
        ECamera,

        EPickUpItem1,

        EDoorClack1,

        EDoorOpen1,
        EDoorClose1,

        EExplosion1,
        EFemaleGiggle1,
        ELocked1,
        EMute,
        EReload1,
        EReload2,

        EShotRifle1,
        EShotAssault1,
        EShotShotgun1,

        ETranquilizerShot,

        EUnlocked1,

        EWallGlass1,
        EWallSolid1,

        ;

        private     static  final   int                 MAX_SOUND_QUEUE_SIZE    = 100;  //always lags and crashes :(

        private     static          Vector<LibSound>    allSounds               = new Vector<LibSound>();

        private                     LibSoundFactory     factory                 = null;

        public static final void init()
        {
            //load all sounds
            for ( ShooterSound sound : values() )
            {
                sound.loadBytes();
            }
        }

        public static final void onRun()
        {
            //ShooterDebug.bugfix.out("maintain [" + allSounds.size() + "] sounds");
            //ShooterDebug.bugfix.mem();

            for ( int i = allSounds.size() - 1; i >= 0; --i )
            {
                //removed finished sounds
                if ( allSounds.elementAt( i ).hasSoundFinished() )
                {
                    allSounds.removeElementAt( i );
                }
                //countdown delayed sounds
                else if ( allSounds.elementAt( i ).iDelay > 0 )
                {
                    //subsctract delay
                    --allSounds.elementAt( i ).iDelay;

                    //start sound if delay reached 0
                    if ( allSounds.elementAt( i ).iDelay == 0 )
                    {
                        allSounds.elementAt( i ).start();
                    }
                }
                //adjust volume for currently played sound
                else if ( allSounds.elementAt( i ).isDistanced() )
                {
                    float distanceToPlayer = (float)ShooterGameLevel.currentPlayer().getCylinder().getCenterHorz().distance( allSounds.elementAt( i ).getDistantLocation() );
                    float volume = ( distanceToPlayer <= Sounds.SPEECH_PLAYER_DISTANCE_MAX_VOLUME ? LibSound.VOLUME_MAX : ( 1.0f - ( distanceToPlayer / Sounds.SPEECH_PLAYER_DISTANCE_MUTE ) ) );

                    allSounds.elementAt( i ).updateDistancedSound( volume );
                }
            }
        }

        public final String getUri()
        {
            return ShooterSettings.Path.ESounds.iUrl + toString() + LibExtension.wav.getSpecifier();
        }

        private final void loadBytes()
        {
            String uri = getUri();

            try
            {
                ByteArrayInputStream bais = Lib.preStreamJarResource( uri );

                //store all bytes
                factory = new LibSoundFactory( bais, ShooterDebug.sounds );
            }
            catch ( Throwable t )
            {
                ShooterDebug.error.out( "Could not read sound file [" + uri + "]" );
                ShooterDebug.error.trace( t );
                System.exit( 0 );
            }
        }

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
            float distanceToPlayer = (float)ShooterGameLevel.currentPlayer().getCylinder().getCenterHorz().distance( distantLocation );
            float volume = ( distanceToPlayer <= Sounds.SPEECH_PLAYER_DISTANCE_MAX_VOLUME ? LibSound.VOLUME_MAX : ( 1.0f - ( distanceToPlayer / Sounds.SPEECH_PLAYER_DISTANCE_MUTE ) ) );

            //ShooterDebug.bugfix.out( "play distanced fx with volume [" + volume + "]" );

            playFx( volume, LibSound.BALANCE_BOTH, delay, distantLocation );
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
            playFx( LibSound.VOLUME_MAX, LibSound.BALANCE_BOTH, delay, null );
        }

        /**************************************************************************************
        *   Starts a one-shot sound effect that is not referenced.
        **************************************************************************************/
        private final void playFx( float volume, float balance, int delay, Point2D.Float distantLocation )
        {
            boolean disableSoundFx = General.DISABLE_SOUND_FX;
            if ( disableSoundFx ) return;

            //reference sound fx
            LibSound sound = factory.getInstancedClip( volume, balance, delay, distantLocation );

            try
            {
                //start immediately if not delayed
                if ( delay == 0 )
                {
                    sound.start();
                }

                //add to queue
                if ( allSounds.size() < MAX_SOUND_QUEUE_SIZE ) allSounds.addElement( sound );
            }
            catch ( Throwable t )
            {
                ShooterDebug.error.err( "Throwable caught on playing sound fx" );
            }

        }
    }
