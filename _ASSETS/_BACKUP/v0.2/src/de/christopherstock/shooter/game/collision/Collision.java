/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
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
