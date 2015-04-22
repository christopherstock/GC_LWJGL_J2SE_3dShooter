/*  $Id: Collision.java 182 2010-11-13 13:33:42Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public interface Collision
    {
        public enum CollisionEnabled
        {
            YES( Shooter.YES ),
            NO(  Shooter.NO  ),
            ;

            public                      boolean             flag                = false;

            private CollisionEnabled( boolean aFlag )
            {
                flag = aFlag;
            }
        }
    }
