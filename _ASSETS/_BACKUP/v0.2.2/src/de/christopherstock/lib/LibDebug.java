/*  $Id: LibDebug.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib;

    /**************************************************************************************
    *   Use final instances of this class to declare different log groups.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class LibDebug
    {
        /**************************************************************************************
        *   The debug log levels in descending priority.
        **************************************************************************************/
        public static enum LogLevel
        {
            ELevelFatal,
            ELevelError,
            ELevelWarning,
            ELevelNote,
            ELevelInfo,
            ELevelDebug,
        };

        private                     LogLevel        logLevel            = null;
        private                     boolean         debugEnabled        = false;

        public LibDebug( boolean aDebugEnabled, LogLevel aLogLevel )
        {
            debugEnabled    = aDebugEnabled;
            logLevel        = aLogLevel;
        }

        public final void out( Object obj )
        {
            if ( debugEnabled && logLevel.ordinal() >= LogLevel.ELevelDebug.ordinal() ) System.out.println( obj );
        }

        public final void info( Object obj )
        {
            if ( debugEnabled && logLevel.ordinal() >= LogLevel.ELevelInfo.ordinal() ) System.out.println( obj );
        }

        public final void note( Object obj )
        {
            if ( debugEnabled && logLevel.ordinal() >= LogLevel.ELevelNote.ordinal() ) System.out.println( obj );
        }

        public final void warn( Object obj )
        {
            if ( debugEnabled && logLevel.ordinal() >= LogLevel.ELevelWarning.ordinal() ) System.out.println( obj );
        }

        public final void err( Object obj )
        {
            if ( debugEnabled && logLevel.ordinal() >= LogLevel.ELevelError.ordinal() ) System.out.println( obj );
        }

        public final void fatal( Object obj )
        {
            if ( debugEnabled && logLevel.ordinal() >= LogLevel.ELevelFatal.ordinal() ) System.out.println( obj );
        }
    }