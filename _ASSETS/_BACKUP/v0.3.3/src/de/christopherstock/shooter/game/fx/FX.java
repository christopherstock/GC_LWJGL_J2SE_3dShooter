/*  $Id: FX.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.LibGLView.Align3D;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.game.fx.FXSliver.ParticleQuantity;

    /**************************************************************************************
    *   The effect system.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public abstract class FX
    {
        public static enum FXSize
        {
            ESmall,
            EMedium,
            ELarge,
        }

        public static enum FXTime
        {
            EShort,
            EMedium,
            ELong,
        }

        public static enum FXType
        {
            EExplosion,
            EDebug,
            ESliver,
        }

        @SuppressWarnings( "unused" )
        private     static  final   int                 MAX_FX                      = 0;

        public      static  final   int                 ELifetimeLong               = 100;
        public      static  final   int                 ELifetimeMedium             = 10;
        public      static  final   int                 ELifetimeShort              = 5;
        public      static  final   int                 ELifetimeForever            = -1;

        private     static          Vector<FXPoint>     fxPoints                    = new Vector<FXPoint>();

        protected                   LibVertex           iAnchor                     = null;
        protected                   FXSize              iSize                       = null;
        protected                   FXTime              iTime                       = null;

        public static final void animateAll()
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
                    if ( fxPoint.iType == FXType.EDebug )
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

        public FX( LibVertex aAnchor )
        {
            iAnchor = aAnchor;
        }

        public static final void launchTestExplosion()
        {
            launchExplosion( new LibVertex( 0.0f, 0.0f, 0.0f ), FXSize.ESmall,  FXTime.EShort  );
            launchExplosion( new LibVertex( 2.0f, 2.0f, 0.0f ), FXSize.EMedium, FXTime.EMedium );
            launchExplosion( new LibVertex( 4.0f, 4.0f, 0.0f ), FXSize.ELarge,  FXTime.ELong   );
        }

        public static final void launchDebugPoint( LibVertex v, LibColors aCol, int l, float s )
        {
            FXPoint fx = new FXPoint( FXType.EDebug, aCol, 0.0f, v.x, v.y, v.z, null, 0 );
            fx.iLifetime  = l;
            fx.iPointSize = s;
            start( fx );
        }

        public static final void clearDebugPoints()
        {
            //browse all points reversed
            for ( int i = fxPoints.size() - 1; i >= 0; --i )
            {
                //prune or animate
                if ( fxPoints.elementAt( i ).getType() == FXType.EDebug )
                {
                    fxPoints.removeElementAt( i );
                }
            }
        }

        public static final void launchExplosion( LibVertex v, FXSize size, FXTime time )
        {
            FXExplosion fx = new FXExplosion( v, size, time );
            fx.launch();
        }

        public static final void launchSliver( LibVertex v, LibColors[] sliverColors, float angle, ParticleQuantity pq )
        {
            FXSliver fx = new FXSliver( v, sliverColors, pq );
            fx.launch( angle );
        }

        public static final void start( FXPoint point )
        {
            //adding the fxpoint to the vector makes it active
            fxPoints.addElement( point );
        }
    }
