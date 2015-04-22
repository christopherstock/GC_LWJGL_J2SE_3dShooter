/*  $Id: LibIO.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.io;

    import  java.io.*;
    import  javazoom.jl.player.*;
    import  de.christopherstock.lib.*;

    /**************************************************************************************
    *   A one shot mp3 player.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class LibJlPlayer
    {
        protected               byte[]          iBytes          = null;
        protected               LibDebug        iDebug          = null;
        protected               Player          iPlaya          = null;

        public LibJlPlayer( byte[] aBytes, LibDebug aDebug )
        {
            iBytes  = aBytes;
            iDebug  = aDebug;
        }

        public final void play()
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        setPriority( Thread.MAX_PRIORITY );
                        iPlaya  = new Player( new ByteArrayInputStream( iBytes ) );
                        iPlaya.play();
                    }
                    catch ( Throwable t )
                    {
                        iDebug.err( "Exception on playing jl player" );
                        iDebug.trace( t );
                    }
                }
            }.start();
        }

        public final void stop()
        {
            iPlaya.close();
        }
    }
