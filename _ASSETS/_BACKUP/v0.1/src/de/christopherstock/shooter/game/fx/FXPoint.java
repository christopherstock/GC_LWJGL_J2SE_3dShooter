/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  java.util.*;
    import  javax.media.opengl.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.util.*;

    /**************************************************************************************
    *   One particle point of any effect.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class FXPoint
    {
        private     static  final   int                 EXPLOSION_ANIMATION_TIME    = 40;
        private     static  final   int                 LIVETIME_EXPLOSION          = 80;

        private     static          Vector<FXPoint>     fxPoints                    = new Vector<FXPoint>();

        private                     FXType              type                        = null;
        private                     Vertex              point                       = null;
        private                     float[]             color                       = null;
        private                     float               startAngle                  = 0.0f;
        private                     float               speedZ                      = 0.0f;
        private                     long                currentTick                 = 0;
        private                     float               currentSpeed                = 0.0f;
        private                     float               speedModified               = 0.0f;
        private                     float               pointSize                   = 0.0f;
        private                     int                 lifetime                    = 0;
        private                     int                 delayTicks                  = 0;

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

            //append to vector - remove 1st if max. fx reached
            fxPoints.addElement( this );

        }

        public static final void drawFxPoints( GL gl )
        {
            //disable texture-mapping and set the points' color
            GLView.disableTextureMapping();

            //draw all points
            for ( FXPoint fxPoint : fxPoints )
            {
                fxPoint.draw( gl );
            }
        }

        public final void draw( GL gl )
        {
            //do not draw delayed points
            if ( delayTicks == 0)
            {
                //set color
                gl.glColor3fv( color, 0 );

                gl.glBegin( GL.GL_POLYGON );
                gl.glVertex3f( point.x - pointSize, point.z, point.y - pointSize );
                gl.glVertex3f( point.x - pointSize, point.z, point.y + pointSize );
                gl.glVertex3f( point.x + pointSize, point.z, point.y + pointSize );
                gl.glVertex3f( point.x + pointSize, point.z, point.y - pointSize );
                gl.glEnd();

                gl.glBegin( GL.GL_POLYGON );
                gl.glVertex3f( point.x, point.z - pointSize, point.y - pointSize );
                gl.glVertex3f( point.x, point.z - pointSize, point.y + pointSize );
                gl.glVertex3f( point.x, point.z + pointSize, point.y + pointSize );
                gl.glVertex3f( point.x, point.z + pointSize, point.y - pointSize );
                gl.glEnd();

                gl.glBegin( GL.GL_POLYGON );
                gl.glVertex3f( point.x - pointSize, point.z - pointSize, point.y );
                gl.glVertex3f( point.x + pointSize, point.z - pointSize, point.y );
                gl.glVertex3f( point.x + pointSize, point.z + pointSize, point.y );
                gl.glVertex3f( point.x - pointSize, point.z + pointSize, point.y );
                gl.glEnd();
            }
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
    }
