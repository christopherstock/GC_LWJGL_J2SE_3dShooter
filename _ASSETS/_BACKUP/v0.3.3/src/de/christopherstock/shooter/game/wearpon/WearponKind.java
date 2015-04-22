/*  $Id: WearponKind.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public interface WearponKind
    {
        /**************************************************************************************
        *   @return <code>true</code> if the wearpon actually fired.
        *           <code>false</code> if the wearpon has not been fired ( if there is no ammo etc. ).
        **************************************************************************************/
        public abstract boolean fire( Wearpon w );

        public abstract void reload( Wearpon w );

        public abstract boolean isReloadable();
    }
