/*  $Id: WearponKind.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.awt.geom.Point2D;
    import  de.christopherstock.lib.Lib;
    import  de.christopherstock.lib.fx.LibFX.FXSize;
    import  de.christopherstock.lib.g3d.LibHoleSize;
    import  de.christopherstock.lib.game.LibShot.ShotSpender;

    /**************************************************************************************
    *   The Wearpons the game makes use of.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public abstract class WearponKind
    {
        public          ArtefactType        iParentKind         = null;

        protected void setParent( ArtefactType aParent )
        {
            iParentKind = aParent;
        }

        public abstract LibHoleSize getBulletHoleSize();
        public abstract FXSize getSliverParticleSize();
        public abstract Lib.ParticleQuantity getSliverParticleQuantity();

        /**************************************************************************************
         *   @return <code>true</code> if the wearpon actually fired.
         *           <code>false</code> if the wearpon has not been fired ( if there is no ammo etc. ).
         **************************************************************************************/
         protected abstract boolean use( Artefact artefact, ShotSpender ss, Point2D.Float shooterXY );
         protected abstract int getDamage();
    }
