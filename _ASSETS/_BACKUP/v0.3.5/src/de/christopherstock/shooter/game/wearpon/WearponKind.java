/*  $Id: WearponKind.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public interface WearponKind
    {
        /**************************************************************************************
        *   @return <code>true</code> if the wearpon actually fired.
        *           <code>false</code> if the wearpon has not been fired ( if there is no ammo etc. ).
        **************************************************************************************/
        public abstract boolean fire( Artefact w );

        public abstract void reload( boolean hudAnimRequired );

        public abstract boolean isReloadable();
    }
