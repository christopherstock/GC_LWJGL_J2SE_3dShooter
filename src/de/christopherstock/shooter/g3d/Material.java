/*  $Id: ShooterMaterial.java 1280 2014-10-08 17:14:17Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.io.hid.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.ShooterSettings.Colors;
    import  de.christopherstock.shooter.base.ShooterTexture.BulletHoleTex;
    import  de.christopherstock.shooter.io.sound.*;

    /**************************************************************************************
    *   Different materials that can be assigned for texture surfaces.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public enum Material implements LibMaterial
    {
        EBrownBricks(       false,      Colors.SLIVER_COLOR_BROWN_BRICKS,   SoundFg.EWallSolid1     ),
        EConcrete(          false,      Colors.SLIVER_COLOR_WALL,           SoundFg.EWallSolid1     ),
        EElectricDevice(    false,      Colors.SLIVER_COLOR_GLASS,          SoundFg.EWallElectric1  ),
        EGlass(             true ,      Colors.SLIVER_COLOR_GLASS,          SoundFg.EWallGlass1     ),
        EHumanFlesh(        false,      Colors.SLIVER_COLOR_BLOOD,          SoundFg.EWallFlesh1     ),
        EPlastic1(          false,      Colors.SLIVER_COLOR_GLASS,          SoundFg.EWallSolid1     ),
        ERedBricks(         false,      Colors.SLIVER_COLOR_RED_BRICKS,     SoundFg.EWallSolid1     ),
        ESteel1(            false,      Colors.SLIVER_COLOR_STEEL,          SoundFg.EWallSolid1     ),
        ESteel2(            false,      Colors.SLIVER_COLOR_STEEL,          SoundFg.EWallSolid1     ),
        EUndefined(         false,      Colors.SLIVER_COLOR_WALL,           SoundFg.EWallSolid1     ),
        EWood(              false,      Colors.SLIVER_COLOR_WALL,           SoundFg.EWallWood1      ),
        ;

        public          boolean         iPenetrable             = false;
        public          LibColors[]     iSliverColors           = null;
        public          SoundFg    iBulletImpactSound      = null;

        private Material( boolean aPenetrable, LibColors[] aSliverColors, SoundFg aBulletSound )
        {
            iPenetrable     = aPenetrable;
            iSliverColors   = aSliverColors;
            iBulletImpactSound    = aBulletSound;
        }

        /**************************************************************************************
        *   Don't move to the constructor - Circle reference in {@link BulletHoleTex}.
        **************************************************************************************/
        @Override
        public LibTexture getBulletHoleTexture()
        {
            switch ( this )
            {
                case EElectricDevice:   return BulletHoleTex.EBulletHoleGlass1;
                case EBrownBricks:      return BulletHoleTex.EBulletHoleBrownBricks1;
                case EConcrete:         return BulletHoleTex.EBulletHoleConcrete1;
                case EGlass:            return BulletHoleTex.EBulletHoleGlass1;
                case EHumanFlesh:       return BulletHoleTex.EBulletHoleSteel1;
                case EPlastic1:         return BulletHoleTex.EBulletHolePlastic1;
                case ERedBricks:        return BulletHoleTex.EBulletHoleConcrete1;
                case ESteel1:           return BulletHoleTex.EBulletHoleSteel1;
                case ESteel2:           return BulletHoleTex.EBulletHoleSteel2;
                case EUndefined:        return BulletHoleTex.EBulletHoleSteel1;
                case EWood:             return BulletHoleTex.EBulletHoleWood1;
            }

            return null;
        }

        @Override
        public LibColors[] getSliverColors()
        {
            return iSliverColors;
        }

        @Override
        public boolean isPenetrable()
        {
            return iPenetrable;
        }

        @Override
        public LibSound getBulletImpactSound()
        {
            return iBulletImpactSound;
        }
    }
