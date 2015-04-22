/*  $Id: CloseCombat.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.wearpon;

    import  java.awt.geom.Point2D;
    import  de.christopherstock.lib.Lib.*;
    import  de.christopherstock.lib.fx.LibFX.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.game.*;
    import  de.christopherstock.lib.game.LibShot.ShotSpender;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.game.fx.*;

    /**************************************************************************************
    *   A close-combat Wearpon.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public final class CloseCombat extends WearponKind
    {
        private         int             iDamage         = 0;
        private         ShooterSound    iUseSound       = null;

        public CloseCombat( int aDamage )
        {
            iDamage = aDamage;
        }

        @Override
        public boolean use( Artefact w, ShotSpender ss, Point2D.Float shooterXY )
        {
            //ShooterDebug.bugfix.out( "LAUNCH CC - damage is [" + iDamage + "]" );

            //launch use-sound-fx
            if ( iUseSound != null )
            {
                if ( shooterXY == null )
                {
                    iUseSound.playGlobalFx();
                }
                else
                {
                    iUseSound.playDistancedFx( shooterXY );
                }
            }

            //clear all debug fx points before firing!
            LibFXManager.clearDebugPoints();

            //launch horizontal strayed shots
            for ( float f = -25.0f; f <= 25.0f; f += 5.0f )
            {
                //draw the shot line and launch the shot
                LibShot s = ss.getShot( f );
                //s.drawShotLine( FxSettings.LIFETIME_DEBUG );
                if ( ShooterGameLevel.current().launchShot( s ).length > 0 )
                {
                    break;
                }
            }

            return true;
        }

        @Override
        public int getDamage()
        {
            return iDamage;
        }

        @Override
        public final ParticleQuantity getSliverParticleQuantity()
        {
            return ParticleQuantity.EMedium;
        }

        @Override
        public final FXSize getSliverParticleSize()
        {
            return FXSize.EMedium;
        }

        @Override
        public final LibHoleSize getBulletHoleSize()
        {
            return LibHoleSize.E9mm;
        }
    }
