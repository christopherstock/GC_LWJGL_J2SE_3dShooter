/*  $Id: Collision.java 431 2011-03-19 17:48:24Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.g3d;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
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
