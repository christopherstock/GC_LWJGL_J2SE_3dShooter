/*  $Id: LibDebug.java 911 2012-01-29 18:51:55Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib;

    /*********************************************************************************
    *   This class performs a threaded http-connection.
    *********************************************************************************/
    public interface LibDebug
    {
        /*********************************************************************************
        *   An output being logged if this debug group is enabled.
        *********************************************************************************/
        public abstract void out( Object msg );

        /*********************************************************************************
        *   An output being logged UNCONDITIONAL.
        *********************************************************************************/
        public abstract void err( Object msg );

        /*********************************************************************************
        *   A stacktrace being logged if this debug group is enabled.
        *********************************************************************************/
        public abstract void trace( Throwable msg );

        /*********************************************************************************
        *   Displays memory info.
        *********************************************************************************/
        public abstract void mem();
    }
