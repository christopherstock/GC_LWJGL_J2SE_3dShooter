/*  $Id: GameEvent.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    /**************************************************************************************
    *   An event being invoked - not only by picking up items but on level start etc.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public interface GameEvent
    {
        public abstract void perform( Bot bot );
    }
