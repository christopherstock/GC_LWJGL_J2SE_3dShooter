/*  $Id: CloseCombat.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    /**************************************************************************************
    *   A close-combat Wearpon.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public final class CloseCombat implements WearponKind
    {
        @Override
        public boolean fire( Artefact w )
        {
            return true;
        }

        @Override
        public void reload( boolean hudAnimRequired )
        {
            //empty implementation never being called
        }

        @Override
        public boolean isReloadable()
        {
            return false;
        }
    }
