/*  $Id: CloseCombat.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    /**************************************************************************************
    *   A close-combat Wearpon.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public final class CloseCombat implements WearponKind
    {
        @Override
        public boolean fire( Wearpon w )
        {
            return true;
        }

        @Override
        public void reload( Wearpon w )
        {
            //empty implementation never being called
        }

        @Override
        public boolean isReloadable()
        {
            return false;
        }
    }
