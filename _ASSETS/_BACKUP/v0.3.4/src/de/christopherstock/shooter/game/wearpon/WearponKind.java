/*  $Id: WearponKind.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
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
