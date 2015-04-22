/*  $Id: Collision.java 214 2011-01-21 17:43:07Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   The debug system.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public interface Collision
    {
        public enum CollisionEnabled
        {
            YES( ShooterSettings.YES ),
            NO(  ShooterSettings.NO  ),
            ;

            public                      boolean             flag                = false;

            private CollisionEnabled( boolean aFlag )
            {
                flag = aFlag;
            }
        }
    }
