/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  java.util.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.fx.FX.FXSize;
    import  de.christopherstock.shooter.game.fx.FX.FXType;
import de.christopherstock.shooter.gl.opengl.*;
import de.christopherstock.shooter.gl.opengl.GLView.DebugPoint3D;
import  de.christopherstock.shooter.math.*;

    /**************************************************************************************
    *   One particle point of any effect.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class FXPoint implements DebugPoint3D
    {
        private     static  final   int                 EXPLOSION_ANIMATION_TIME    = 40;
        private     static  final   int                 LIVETIME_EXPLOSION          = 80;

        public      static          Vector<FXPoint>     fxPoints                    = new Vector<FXPoint>();

        public                      Vertex              point                       = null;
        public                      float               pointSize                   = 0.0f;
        public                      int                 delayTicks                  = 0;
        public                      float[]             color                       = null;

        private                     FXType              type                        = null;
        private                     float               startAngle                  = 0.0f;
        private                     float               speedZ                      = 0.0f;
        private                     long                currentTick                 = 0;
        private                     float               currentSpeed                = 0.0f;
        private                     float               speedModified               = 0.0f;
        private                     int                 lifetime                    = 0;

        public FXPoint( FXType pointType, int col, float startAng, float x, float y, float z, FXSize size, int delay )
        {
            type            = pointType;
            point           = new Vertex( x, y, z, 0.0f, 1.0f );
            color           = UMath.col2f3( col );
            startAngle      = startAng;
            currentTick     = 0;
            delayTicks      = delay;

            switch ( pointType )
            {
                case EExplosion:
                {
                    switch ( size )
                    {
                        case ESmall:
                        {
                            currentSpeed    = 0.001f   * UMath.getRandom( 1,  3  );
                            speedModified   = 0.00001f * UMath.getRandom( 5,  45 );
                            speedZ          = 0.000005f * UMath.getRandom( 15, 70 );
                            lifetime        = LIVETIME_EXPLOSION;
                            pointSize       = 0.001f * UMath.getRandom( 5, 15 );
                            break;
                        }
                        case EMedium:
                        {
                            currentSpeed    = 0.003f   * UMath.getRandom( 1,  3  );
                            speedModified   = 0.00001f * UMath.getRandom( 5,  45 );
                            speedZ          = 0.00001f * UMath.getRandom( 15, 70 );
                            lifetime        = LIVETIME_EXPLOSION;
                            pointSize       = 0.001f * UMath.getRandom( 10, 20 );
                            break;
                        }
                        case ELarge:
                        {
                            currentSpeed    = 0.008f   * UMath.getRandom( 1,  3  );
                            speedModified   = 0.00001f * UMath.getRandom( 5,  45 );
                            speedZ          = 0.00002f * UMath.getRandom( 15, 70 );
                            lifetime        = LIVETIME_EXPLOSION;
                            pointSize       = 0.001f * UMath.getRandom( 15, 25 );
                            break;
                        }
                    }
                    break;
                }
            }
        }

        public final void start()
        {
            //adding the fxpoint to the vector makes it active
            fxPoints.addElement( this );
        }

        public static final void handleAnimations()
        {
            //browse all points
            for ( FXPoint fxPoint : fxPoints )
            {
                fxPoint.animate();
            }

            //browse all points reversed for pruning
            for ( int i = fxPoints.size() - 1; i >= 0; --i )
            {
                if ( fxPoints.elementAt( i ).currentTick >= fxPoints.elementAt( i ).lifetime )
                {
                    fxPoints.removeElementAt( i );
                }
            }
        }

        public final void animate()
        {
            if ( delayTicks > 0)
            {
                --delayTicks;
            }
            else
            {
                switch( type )
                {
                    case EExplosion:
                    {
                        if ( currentTick < EXPLOSION_ANIMATION_TIME / 2 )
                        {
                            float x = EXPLOSION_ANIMATION_TIME / 2 - currentTick;
                            point.x += currentSpeed * UMath.sinDeg( startAngle );
                            point.y += currentSpeed * UMath.cosDeg( startAngle );
                            point.z += x * x * speedZ;
                        }
                        else if ( currentTick <= EXPLOSION_ANIMATION_TIME )
                        {
                            float x = currentTick - EXPLOSION_ANIMATION_TIME / 2;
                            point.x += currentSpeed * UMath.sinDeg( startAngle );
                            point.y += currentSpeed * UMath.cosDeg( startAngle );
                            point.z -= x * x * speedZ;
                        }
                        else if ( currentTick > EXPLOSION_ANIMATION_TIME )
                        {
                            //no changes
                        }

                        //increase tick-counter and current speed
                        currentTick += 1;
                        currentSpeed += speedModified;

                        break;

                    }
                }
            }
        }

        @Override
        public Vertex getPoint()
        {
            return point;
        }

        @Override
        public float getPointSize()
        {
            return pointSize;
        }

        @Override
        public float[] getPointColor()
        {
            return color;
        }

        public static final void drawFXPoints()
        {
            //draw all points
            for ( FXPoint fxPoint : fxPoints )
            {
                //do not draw delayed points
                if ( fxPoint.delayTicks == 0)
                {
                    GLView.singleton.drawDebugPoint( fxPoint );
                }
            }
        }
    }