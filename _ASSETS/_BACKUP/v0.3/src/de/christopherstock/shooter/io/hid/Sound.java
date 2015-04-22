/*  $Id: Sound.java 265 2011-02-10 19:48:54Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.io.hid;

    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   The sound system.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public enum Sound
    {
        ESoundBullet1,
        EClack1,
        ;

        public  static          Sound           soundToStartNext    = null;
        public  static          Sound           currentBgSound      = null;

        public                  LibJlPlayer     playa               = null;

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
            //store all bytes
            playa = new LibJlPlayer
            (
                LibIO.readStreamBuffered
                (
                    Sound.class.getResourceAsStream( ShooterSettings.Path.ESounds.url + this.toString() + LibExtension.mp3.getSpecifier() )
                ),
                ShooterDebugSystem.error
            );
        }

        public final void playBg()
        {
            boolean disableSoundBg = ShooterSettings.DISABLE_SOUND_BG;
            if ( disableSoundBg ) return;

            //do not play current bg sound
            if ( this == currentBgSound ) return;

            //stop current
            if ( currentBgSound != null ) currentBgSound.playa.stop();
            currentBgSound = this;
            playa.play();
        }

        public final void playFx()
        {
            boolean disableSoundFx = ShooterSettings.DISABLE_SOUND_FX;
            if ( disableSoundFx ) return;
            playa.play();
        }
    }
