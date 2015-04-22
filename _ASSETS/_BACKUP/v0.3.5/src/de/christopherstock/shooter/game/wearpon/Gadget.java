/*  $Id: Gadget.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    /**************************************************************************************
    *   A close-combat Wearpon.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public final class Gadget implements WearponKind
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
