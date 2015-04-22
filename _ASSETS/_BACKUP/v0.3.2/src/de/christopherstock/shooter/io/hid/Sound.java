/*  $Id: Sound.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.io.hid.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.ShooterSettings.Sounds;

    /**************************************************************************************
    *   The sound system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public enum Sound
    {
        EClack1,
        EFemaleGiggle1,
        ESoundBullet1,
        ;

        private                     LibSoundFactory     player              = null;

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

        private final void loadBytes()
        {
            try
            {
                //store all bytes
                player = new LibSoundFactory
                (
                    Sound.class.getResourceAsStream( ShooterSettings.Path.ESounds.iUrl + this.toString() + LibExtension.wav.getSpecifier() ),
                    ShooterDebug.error
                );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }

        /**************************************************************************************
        *   Starts a one-shot sound effect being on a specified distance from the listener.
        **************************************************************************************/
        public final LibSound playFx( float distanceToPlayer )
        {
            //specify volume from distance
            float volume = ( distanceToPlayer <= Sounds.SPEECH_PLAYER_DISTANCE_MAX_VOLUME ? LibSound.VOLUME_MAX : ( 1.0f - ( distanceToPlayer / Sounds.SPEECH_PLAYER_DISTANCE_MUTE ) ) );

            //clip volume
            if ( volume < LibSound.VOLUME_MUTE ) volume = LibSound.VOLUME_MUTE;
            if ( volume > LibSound.VOLUME_MAX  ) volume = LibSound.VOLUME_MAX;

            ShooterDebug.sounds.out( "distance to player: [" + distanceToPlayer + "] volume [" + volume + "]"  );

            //play sound
            return playFx( volume, LibSound.BALANCE_BOTH );
        }

        /**************************************************************************************
        *   Starts a one-shot sound effect that is not referenced.
        **************************************************************************************/
        public final LibSound playGlobalFx()
        {
            return playFx( LibSound.VOLUME_MAX, LibSound.BALANCE_BOTH );
        }

        /**************************************************************************************
        *   Starts a one-shot sound effect that is not referenced.
        **************************************************************************************/
        private final LibSound playFx( float volume, float balance )
        {
            boolean disableSoundFx = General.DISABLE_SOUND_FX;
            if ( disableSoundFx ) return null;

            //no reference to the sound fx
            LibSound sound = player.getInstancedClip( volume, balance );
            sound.start();
            return sound;
        }
    }
