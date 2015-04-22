/*  $Id: Lib.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib;

    /**************************************************************************************
    *   Use final instances of this class to declare different log groups.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public abstract class Lib
    {
        public          static  final   int             MILLIS_PER_SECOND           = 1000;

        public          static  final   int             MILLIS_PER_MINUTE           = 60 * MILLIS_PER_SECOND;

        public          static  final   int             MILLIS_PER_HOUR             = 60 * MILLIS_PER_MINUTE;
    }
