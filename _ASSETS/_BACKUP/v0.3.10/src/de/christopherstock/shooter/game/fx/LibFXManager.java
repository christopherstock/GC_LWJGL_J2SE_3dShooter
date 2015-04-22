/*  $Id: LibFXManager.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.fx.*;
    import  de.christopherstock.lib.fx.LibFX.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.LibGLView.Align3D;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   Initializes and starts an effect.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public final class LibFXManager
    {
        private         static          Vector<FXPoint>         fxPoints                    = new Vector<FXPoint>();

        public static final void launchStaticPoint( LibVertex vertex, LibColors col, float size, int lifetime )
        {
            FXPoint fx = new FXPoint( 0.0f, LibFX.FXType.EStaticDebugPoint, col, 0.0f, vertex.x, vertex.y, vertex.z, null, 0, lifetime, FXGravity.ENormal );
//          fx.iLifetime  = lifetime;
            fx.iPointSize = size;
            LibFXManager.start( fx );
        }

        public static final void launchExplosion( LibVertex v, LibFX.FXSize size, LibFX.FXTime time, int lifetime )
        {
            LibFXExplosion fx = new LibFXExplosion( v, size, time, lifetime );
            fx.launch();
        }

        public static final void launchSliver( LibVertex v, LibColors[] sliverColors, float angle, Lib.ParticleQuantity pq, float angleMod, int lifetime, FXSize size, FXGravity gravity )
        {
            LibFXSliver fx = new LibFXSliver( v, sliverColors, pq, angleMod, lifetime, size, gravity );
            fx.launch( angle );
        }

        public static final void onRun()
        {
            //browse all points reversed
            for ( int i = fxPoints.size() - 1; i >= 0; --i )
            {
                //prune or animate
                if ( fxPoints.elementAt( i ).isLifetimeOver() )
                {
                    fxPoints.removeElementAt( i );
                }
                else
                {
                    fxPoints.elementAt( i ).animate();
                }
            }
        }

        public static final void drawAll()
        {
            //draw all points
            for ( FXPoint fxPoint : fxPoints )
            {
                //do not draw delayed points
                if ( !fxPoint.isDelayedBefore() )
                {
                    if ( fxPoint.iType == LibFX.FXType.EStaticDebugPoint )
                    {
                        fxPoint.draw( Align3D.AXIS_X );
                        fxPoint.draw( Align3D.AXIS_Y );
                        fxPoint.draw( Align3D.AXIS_Z );
                    }
                    else
                    {
                        fxPoint.draw( fxPoint.iAlign3D );
                    }
                }
            }
        }

        public static final void clearDebugPoints()
        {
            //browse all points reversed
            for ( int i = fxPoints.size() - 1; i >= 0; --i )
            {
                //prune or animate
                if ( fxPoints.elementAt( i ).getType() == LibFX.FXType.EStaticDebugPoint )
                {
                    fxPoints.removeElementAt( i );
                }
            }
        }

        protected static final void start( FXPoint point )
        {
            //adding the fxpoint to the vector makes it active
            fxPoints.addElement( point );
        }
    }
