/*  $Id: Sound.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.io.hid.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.ShooterSettings.Sounds;
    import  de.christopherstock.shooter.base.*;

    /**************************************************************************************
    *   The sound system.
    *
    *   @version    0.3.5
    *   @author     Christopher Stock
    **************************************************************************************/
    public enum Sound
    {
        EClack1,
        EFemaleGiggle1,

        EBulletShot1,

        EReload1,
        EBulletShell1,
        EDoorClack1,
        EDoorOpen1,
        EDoorClose1,
        EExplosion1,
        ERubble1,
        ERubble2,
        ERubble3,

        ECamera,
        EReload2,
        EShot1,
        EShotgunShot1,

        ;

        private     static          Vector<LibSound>    allSounds           = new Vector<LibSound>();

        private                     LibSoundFactory     factory             = null;

        private Sound()
        {
        }

        public static final void init()
        {
            //load all sounds
            for ( Sound sound : values() )
            {
                sound.loadBytes();
            }
        }

        public static final void maintainAllSounds()
        {
/*
            if ( allSounds.size() > 0 )
            {
                ShooterDebug.bugfix.out("maintain [" + allSounds.size() + "] sounds");
                ShooterDebug.bugfix.mem();
            }
*/
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
            return ShooterSettings.Path.ESounds.iUrl + this.toString() + LibExtension.wav.getSpecifier();
        }

        private final void loadBytes()
        {
            String uri = getUri();

            try
            {
                //store all bytes
                factory = new LibSoundFactory
                (
                    Sound.class.getResourceAsStream( uri ),
                    ShooterDebug.sounds
                );
            }
            catch ( Throwable t )
            {
                ShooterDebug.error.out( "Could not read sound file [" + uri + "]" );
                ShooterDebug.error.trace( t );
                System.exit( 0 );
            }
        }

        /**************************************************************************************
        *   Starts a one-shot sound effect being on a specified distance from the listener.
        **************************************************************************************/
        public final void playDistancedFx( Point2D.Float distantLocation )
        {
            //specify volume from distance
            float distanceToPlayer = (float)ShooterGameLevel.currentPlayer().getCylinder().getCenterHorz().distance( distantLocation );
            float volume = ( distanceToPlayer <= Sounds.SPEECH_PLAYER_DISTANCE_MAX_VOLUME ? LibSound.VOLUME_MAX : ( 1.0f - ( distanceToPlayer / Sounds.SPEECH_PLAYER_DISTANCE_MUTE ) ) );

            playFx( volume, LibSound.BALANCE_BOTH, 0, distantLocation );
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
                allSounds.addElement( sound );
            }
            catch ( Throwable t )
            {
                ShooterDebug.error.err( "Throwable caught on playing sound fx" );
            }

        }
    }
