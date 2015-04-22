/*  $Id: FXPoint.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.LibGLView.Align3D;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.ShooterSettings.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.game.fx.FX.FXSize;
    import  de.christopherstock.shooter.game.fx.FX.FXType;

    /**************************************************************************************
    *   One particle point of any effect.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    class FXPoint
    {
        private                     LibVertex           iPoint                          = null;
        public                      float               iPointSize                      = 0.0f;
        private                     int                 iDelayTicksBefore               = 0;
        private                     LibColors           iColor                          = null;

        public                      FXType              iType                           = null;
        public                      Align3D             iAlign3D                        = null;
        private                     Align3D             iRotationAlign                  = null;
        private                     float               iStartAngle                     = 0.0f;
        private                     float               iSpeedZ                         = 0.0f;
        private                     float               iSpeedModified                  = 0.0f;
        private                     float               iCurrentSpeed                   = 0.0f;
        public                      int                 iLifetime                       = 0;
        private                     int                 iCurrentTick                    = 0;
        private                     float               iRotation                       = 0.0f;

        protected FXPoint( FXType aType, LibColors aColor, float aStartAngle, float aX, float aY, float aZ, FXSize size, int aDelayTicks )
        {
            iType               = aType;
            iPoint              = new LibVertex( aX, aY, aZ, 0.0f, 1.0f );
            iColor              = aColor;
            iStartAngle         = aStartAngle;
            iCurrentTick        = 0;
            iDelayTicksBefore   = aDelayTicks;
            iAlign3D            = Align3D.values()[ LibMath.getRandom( 0, Align3D.values().length - 1 )  ];
            iRotationAlign      = Align3D.values()[ LibMath.getRandom( 0, Align3D.values().length - 1 )  ];

            switch ( aType )
            {
                case EDebug:
                {
                    iPointSize      = DebugSettings.DEBUG_POINT_SIZE;
                    iLifetime       = FxSettings.LIVETIME_DEBUG;
                    break;
                }

                case ESliver:
                {
                    iCurrentSpeed    = 0.0025f * LibMath.getRandom( 2, 14 );
                    iSpeedModified   = 0.0f;
                    iSpeedZ          = 0.00025f; // (* LibMath.getRandom( 15, 20 ) );
                    iLifetime        = FxSettings.LIVETIME_SLIVER;
                    iPointSize       = 0.002f * LibMath.getRandom( 5, 10 );
                    break;
                }

                case EExplosion:
                {
                    switch ( size )
                    {
                        case ESmall:
                        {
                            iCurrentSpeed    = 0.001f   * LibMath.getRandom( 1,  3  );
                            iSpeedModified   = 0.00001f * LibMath.getRandom( 5,  45 );
                            iSpeedZ          = 0.000005f * LibMath.getRandom( 15, 70 );
                            iLifetime        = FxSettings.LIVETIME_EXPLOSION;
                            iPointSize       = 0.001f * LibMath.getRandom( 5, 15 );
                            break;
                        }
                        case EMedium:
                        {
                            iCurrentSpeed    = 0.003f   * LibMath.getRandom( 1,  3  );
                            iSpeedModified   = 0.00001f * LibMath.getRandom( 5,  45 );
                            iSpeedZ          = 0.00001f * LibMath.getRandom( 15, 70 );
                            iLifetime        = FxSettings.LIVETIME_EXPLOSION;
                            iPointSize       = 0.001f * LibMath.getRandom( 10, 20 );
                            break;
                        }
                        case ELarge:
                        {
                            iCurrentSpeed    = 0.008f   * LibMath.getRandom( 1,  3  );
                            iSpeedModified   = 0.00001f * LibMath.getRandom( 5,  45 );
                            iSpeedZ          = 0.00002f * LibMath.getRandom( 15, 70 );
                            iLifetime        = FxSettings.LIVETIME_EXPLOSION;
                            iPointSize       = 0.001f * LibMath.getRandom( 15, 25 );
                            break;
                        }
                    }
                    break;
                }
            }
        }

        protected final void animate()
        {
            if ( iDelayTicksBefore > 0)
            {
                --iDelayTicksBefore;
            }
            else
            {
                switch( iType )
                {
                    case EDebug:
                    {
                        iCurrentTick  += 1;

                        break;
                    }

                    case ESliver:
                    {
                        if ( iPoint.z <= 0.0f )
                        {
                            iPoint.z = 0.0f;
                        }
                        else
                        {
                            float zbase = iCurrentTick; // - FxSettings.LIVETIME_SLIVER / 10 );
                            iPoint.x -= iCurrentSpeed * LibMath.sinDeg( iStartAngle );
                            iPoint.y -= iCurrentSpeed * LibMath.cosDeg( iStartAngle );
                            iPoint.z -= zbase * zbase * iSpeedZ;

                            //clip z on the floor!

                            iRotation     += 5.0f;
                            if ( iRotation >= 360.0f ) iRotation = 0.0f;

                            //sound on 1st floor reach
                            if ( iPoint.z <= 0.0f )
                            {
                                //Sound toPlay = Sound.values()[ LibMath.getRandom( Sound.ERubble1.ordinal(), Sound.ERubble3.ordinal() ) ];
                                //toPlay.playDistancedFx( new Point2D.Float( iPoint.x, iPoint.y ) );
                            }
                        }

                        //increase tick-counter, current speed and rotation
                        iCurrentTick  += 1;
                        iCurrentSpeed += iSpeedModified;

                        break;

                    }

                    case EExplosion:
                    {
                        if ( iCurrentTick < FxSettings.LIVETIME_EXPLOSION / 5 )
                        {
                            float x = FxSettings.LIVETIME_EXPLOSION / 5 - iCurrentTick;
                            iPoint.x += iCurrentSpeed * LibMath.sinDeg( iStartAngle );
                            iPoint.y += iCurrentSpeed * LibMath.cosDeg( iStartAngle );
                            iPoint.z += x * x * iSpeedZ;

                            iRotation     += 10.0f;
                            if ( iRotation >= 360.0f ) iRotation = 0.0f;
                        }
                        else
                        {
                            if ( iPoint.z <= 0.0f )
                            {
                                iPoint.z = 0.0f;
                            }
                            else
                            {
                                float x = iCurrentTick - FxSettings.LIVETIME_EXPLOSION / 5;
                                iPoint.x += iCurrentSpeed * LibMath.sinDeg( iStartAngle );
                                iPoint.y += iCurrentSpeed * LibMath.cosDeg( iStartAngle );
                                iPoint.z -= x * x * iSpeedZ;

                                //clip z on the floor!

                                iRotation     += 5.0f;
                                if ( iRotation >= 360.0f ) iRotation = 0.0f;
                            }
                        }

                        //increase tick-counter, current speed and rotation
                        iCurrentTick  += 1;
                        iCurrentSpeed += iSpeedModified;

                        break;

                    }
                }
            }
        }

        public final void draw( Align3D align3D )
        {
            FaceQuad face = null;

            switch ( align3D )
            {
                case AXIS_Z:
                {
                    face = new FaceQuad
                    (
                        new LibVertex( iPoint.x, iPoint.y, iPoint.z ),
                        new LibVertex( iPoint.x - iPointSize, iPoint.y - iPointSize, iPoint.z ),
                        new LibVertex( iPoint.x - iPointSize, iPoint.y + iPointSize, iPoint.z ),
                        new LibVertex( iPoint.x + iPointSize, iPoint.y + iPointSize, iPoint.z ),
                        new LibVertex( iPoint.x + iPointSize, iPoint.y - iPointSize, iPoint.z ),
                        iColor
                    );
                    break;
                }

                case AXIS_X:
                {
                    face = new FaceQuad
                    (
                        new LibVertex( iPoint.x, iPoint.y, iPoint.z ),
                        new LibVertex( iPoint.x, iPoint.y - iPointSize, iPoint.z - iPointSize ),
                        new LibVertex( iPoint.x, iPoint.y + iPointSize, iPoint.z - iPointSize ),
                        new LibVertex( iPoint.x, iPoint.y + iPointSize, iPoint.z + iPointSize ),
                        new LibVertex( iPoint.x, iPoint.y - iPointSize, iPoint.z + iPointSize ),
                        iColor
                    );
                    break;
                }

                case AXIS_Y:
                default:
                {
                    face = new FaceQuad
                    (
                        new LibVertex( iPoint.x, iPoint.y, iPoint.z ),
                        new LibVertex( iPoint.x - iPointSize, iPoint.y, iPoint.z - iPointSize ),
                        new LibVertex( iPoint.x + iPointSize, iPoint.y, iPoint.z - iPointSize ),
                        new LibVertex( iPoint.x + iPointSize, iPoint.y, iPoint.z + iPointSize ),
                        new LibVertex( iPoint.x - iPointSize, iPoint.y, iPoint.z + iPointSize ),
                        iColor
                    );
                    break;
                }
            }

            switch ( iRotationAlign )
            {
                case AXIS_X:
                {
                    face.translateAndRotateXYZ( new LibMatrix( iRotation, 0.0f, 0.0f ), 0.0f, 0.0f, 0.0f, LibTransformationMode.EOriginalsToOriginals, null );
                    break;
                }

                case AXIS_Y:
                {
                    face.translateAndRotateXYZ( new LibMatrix( 0.0f, iRotation, 0.0f ), 0.0f, 0.0f, 0.0f, LibTransformationMode.EOriginalsToOriginals, null );
                    break;
                }

                case AXIS_Z:
                {
                    face.translateAndRotateXYZ( new LibMatrix( 0.0f, 0.0f, iRotation ), 0.0f, 0.0f, 0.0f, LibTransformationMode.EOriginalsToOriginals, null );
                    break;
                }
            }

            //draw face
            face.draw();
        }

        protected boolean isLifetimeOver()
        {
            return ( iCurrentTick >= iLifetime );
        }

        protected boolean isDelayedBefore()
        {
            return ( iDelayTicksBefore > 0 );
        }

        protected FXType getType()
        {
            return iType;
        }
    }
