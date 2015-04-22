/*  $Id: ShooterMaterial.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.wall;

    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.ShooterSettings.Colors;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.base.ShooterTexture.BulletHole;

    /**************************************************************************************
    *   Different materials that can be assigned for texture surfaces.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public enum ShooterMaterial
    {
        EBrownBricks(   false,    Colors.SLIVER_COLOR_BROWN_BRICKS,    ShooterSound.EWallSolid1   ),
        EConcrete(      false,    Colors.SLIVER_COLOR_WALL        ,    ShooterSound.EWallSolid1   ),
        EGlass(         true ,    Colors.SLIVER_COLOR_GLASS       ,    ShooterSound.EWallGlass1   ),
        EHumanFlesh(    false,    Colors.SLIVER_COLOR_BLOOD       ,    ShooterSound.EWallSolid1   ),
        EPlastic1(      false,    Colors.SLIVER_COLOR_GLASS       ,    ShooterSound.EWallSolid1   ),
        ERedBricks(     false,    Colors.SLIVER_COLOR_RED_BRICKS  ,    ShooterSound.EWallSolid1   ),
        ESteel1(        false,    Colors.SLIVER_COLOR_STEEL       ,    ShooterSound.EWallSolid1   ),
        ESteel2(        false,    Colors.SLIVER_COLOR_STEEL       ,    ShooterSound.EWallSolid1   ),
        EUndefined(     false,    Colors.SLIVER_COLOR_WALL        ,    ShooterSound.EWallSolid1   ),
        EWood(          false,    Colors.SLIVER_COLOR_WALL        ,    ShooterSound.EWallSolid1   ),
        ;

        public          boolean         iPenetrable     = false;
        public          LibColors[]     iSliverColors   = null;
        public          ShooterSound    iBulletSound    = null;

        private ShooterMaterial( boolean aPenetrable, LibColors[] aSliverColors, ShooterSound aBulletSound )
        {
            iPenetrable     = aPenetrable;
            iSliverColors   = aSliverColors;
            iBulletSound    = aBulletSound;
        }

        /**************************************************************************************
        *   Don't move to the constructor - Circle reference in {@link BulletHole}.
        **************************************************************************************/
        public BulletHole getBulletHoleTexture()
        {
            switch ( this )
            {
                case EBrownBricks:  return BulletHole.EBulletHoleBrownBricks1;
                case EConcrete:     return BulletHole.EBulletHoleConcrete1;
                case EGlass:        return BulletHole.EBulletHoleGlass1;
                case EHumanFlesh:   return BulletHole.EBulletHoleSteel1;
                case EPlastic1:     return BulletHole.EBulletHolePlastic1;
                case ERedBricks:    return BulletHole.EBulletHoleConcrete1;
                case ESteel1:       return BulletHole.EBulletHoleSteel1;
                case ESteel2:       return BulletHole.EBulletHoleSteel2;
                case EUndefined:    return BulletHole.EBulletHoleSteel1;
                case EWood:         return BulletHole.EBulletHoleWood1;
            }

            return null;
        }
    }
