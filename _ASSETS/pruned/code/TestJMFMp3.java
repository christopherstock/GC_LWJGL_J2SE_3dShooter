
    package de.christopherstock.shooter;

    import  java.io.*;
    import  javax.media.*;
    import  javax.media.format.*;

    public class TestJMFMp3
    {
        public      static              Player              player                  = null;

        public static final void main( String[] args )
        {
            Format input1 = new AudioFormat( AudioFormat.MPEGLAYER3 );
            Format input2 = new AudioFormat( AudioFormat.MPEG       );
            Format output = new AudioFormat( AudioFormat.LINEAR     );

            PlugInManager.addPlugIn
            (
                com.sun.media.codec.audio.mp3.JavaDecoder.class.getName(),
                new Format[]{input1, input2},
                new Format[]{output},
                PlugInManager.CODEC
            );

            try
            {
                player = Manager.createPlayer(new MediaLocator(new File("E:\\workspaces\\eclipse\\Shooter\\trunk\\res\\res\\sound\\bg\\EExtraction.mp3").toURI().toURL()));

                System.out.println( "play() 1 " + player );

                player.realize();

                player.addControllerListener
                (
                    new ControllerListener()
                    {
                        @Override
                        public void controllerUpdate(ControllerEvent e)
                        {
                            System.out.println("CU - " + e);

                            if ( e instanceof EndOfMediaEvent )
                            {
                                //playMP3.stop();
                                //playMP3.close();
                            }

                            if ( e instanceof RealizeCompleteEvent )
                            {
                                player.setMediaTime( new Time( 12.0 ) );
                                player.setStopTime(  new Time( 14.0 ) );
                                player.start();
                            }

                            if ( e instanceof StopAtTimeEvent )
                            {
                                player.setMediaTime( new Time( 12.0 ) );
                                player.setStopTime(  new Time( 14.0 ) );
                                player.start();
                            }
                        }
                    }
                );
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
            }
        }
    }
