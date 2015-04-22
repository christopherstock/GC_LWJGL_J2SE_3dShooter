/*  $Id: LibCollision.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.g3d;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public interface LibCollision
    {
        public enum CollisionEnabled
        {
            EYes,
            ENo,
            ;

            public boolean getBoolean()
            {
                return ( this == EYes );
            }
        }
    }
