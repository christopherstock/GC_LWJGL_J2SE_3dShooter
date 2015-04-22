/*  $Id: Bot.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterD3dsFiles.Bots;
    import  de.christopherstock.shooter.ShooterSettings.BotSettings;
    import  de.christopherstock.shooter.ShooterSettings.DebugSettings;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.Shot.*;
    import  de.christopherstock.shooter.io.hid.*;

    /**************************************************************************************
    *   The superclass of all non-player-characters.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class Bot implements GameObject
    {
        public enum BotType
        {
            ETypeEnemy,
            ETypeFriend,
            ;
        }

        public enum BotJob
        {
            ELeadPlayerToLastWaypoint,
            EWalkWaypoints,
            EWatchPlayer,
            ESleep,
            ;
        }

        public enum BotState
        {
            EWalkTowardsPlayer,
            EWatchPlayer,
            ESleep,
            EWalkToNextWayPoint,
            ;
        }

        public                      BotMeshes           iBotMeshes              = null;

        /** current facing angle. */
        public                      float               iFacingAngle            = 0.0f;
        public                      boolean             iFaceAngleChanged       = false;

        protected                   BotType             iType                   = null;
        protected                   BotJob              iJob                    = null;
        protected                   BotState            iState                  = null;
        private                     boolean             iIsAlive                = true;
        private                     Point2D.Float[]     iWayPoints              = null;
        private                     int                 iCurrentWayPointIndex   = 0;

        /** the collision unit for simple collisions */
        protected                   Cylinder            iCylinder               = null;

        private                     long                iNextEyeChange          = 0;
        private                     boolean             iEyesOpen               = true;

        public Bot( ShooterBotTemplate aTemplate, BotType aType, LibVertex aStartPosition, Point2D.Float[] aWayPoints, BotJob aJob, Bots itemLeft, Bots itemRight )
        {
            iType           = aType;
            iCylinder       = new Cylinder( this, aStartPosition, aTemplate.iRadius, aTemplate.iHeight, 0, ShooterDebug.bot, DebugSettings.DEBUG_DRAW_BOT_CIRCLES );
            iWayPoints      = aWayPoints;
            iJob            = aJob;

            //init mesh-collection
            iBotMeshes      = new BotMeshes( aTemplate, aStartPosition, this, itemLeft, itemRight );

            //set next eye blink
            iEyesOpen       = true;
            setNextEyeChange();
        }

        private final void setNextEyeChange()
        {
            iNextEyeChange =
            (
                    System.currentTimeMillis()
                +   (
                            iEyesOpen
                        ?   LibMath.getRandom( BotSettings.EYE_BLINK_INTERVAL_MIN, BotSettings.EYE_BLINK_INTERVAL_MAX )
                        :   BotSettings.EYE_CLOSED_INTERVAL
                    )
            );
        }

        public final boolean checkCollision( Cylinder aCylinder )
        {
            return iCylinder.checkCollision( aCylinder );
        }

        protected final boolean checkCollision( Ellipse2D.Float aEllipse )
        {
            return iCylinder.checkCollision( aEllipse );
        }

        public final Point2D.Float getCenterHorz()
        {
            return iCylinder.getCenterHorz();
        }

        protected final void drawDebugCircles()
        {
            if ( DebugSettings.DEBUG_DRAW_BOT_CIRCLES )
            {
                //Debug.bot.out( "drawing bot .." );
                int VERTICAL_SLICES = 4;
                LibVertex ank = getAnchor();
                LibColors col = null;
                switch ( iType )
                {
                    case ETypeFriend:   col = LibColors.EBlueLight;    break;
                    case ETypeEnemy:    col = LibColors.EBlueDark;      break;
                }
                for ( int i = 0; i <= VERTICAL_SLICES; ++i )
                {
                    new FaceEllipseFloor( null, col, ank.x, ank.y, ank.z + ( i * iCylinder.getHeight() / VERTICAL_SLICES ), iCylinder.getRadius(), iCylinder.getRadius() ).draw();
                }
            }
        }

        protected final float getHeight()
        {
            return iCylinder.getHeight();
        }

        protected final void setCenterHorz( float newX, float newY )
        {
            iCylinder.setNewAnchor( new LibVertex( newX, newY, iCylinder.getAnchor().z ), false, null );
        }

        public final Cylinder getCylinder()
        {
            return iCylinder;
        }

        protected final boolean checkCollisionsToOtherBots()
        {
            for ( Bot bot : Level.current().iBots )
            {
                //skip own !
                if ( this == bot ) continue;

                //check bot-collision with other bot
                if ( bot.checkCollision( iCylinder ) )
                {
                    ShooterDebug.bot.out( "own bot touched" );
                    return true;

                    //sleep if being touched
                    //bot.state = BotState.EStateSleeping;
                }
            }

            return false;
        }

        protected final boolean checkCollisionsToPlayer()
        {
            //check bot-collision with player
            if ( checkCollision( Level.currentPlayer().getCylinder().getCircle() ) )
            {
                ShooterDebug.bot.out( "player touched" );
                return true;

                //sleep if being touched
                //bot.state = BotState.EStateSleeping;
            }

            return false;
        }

        public final void animate()
        {
            switch ( iType )
            {
                case ETypeEnemy:
                case ETypeFriend:
                {
                    Point2D.Float player = Level.currentPlayer().getCylinder().getCenterHorz();
                    Point2D.Float bot    = getCenterHorz();

                    //move bot towards the player
                    float anglePlayerToBot = LibMath.getAngleCorrect( player, bot );
                    float angleBotToPlayer = anglePlayerToBot - 180.0f;
                    float distanceToPlayer = (float)player.distance( bot );

                    //Debug.bot.out( "angle player to bot is: " + anglePlayerToBot );
                    //Debug.bot.out( "angle bot to player is: " + angleBotToPlayer );

                    float   nextPosX            = 0.0f;
                    float   nextPosY            = 0.0f;
                            iFaceAngleChanged   = false;

                    //check the bot's job
                    switch ( iJob )
                    {
                        case ELeadPlayerToLastWaypoint:
                        {
                            //check distance from player to next waypoint and from bot to next waypoint
                            float   distancePlayerToNextWaypoint = (float)player.distance( iWayPoints[ iCurrentWayPointIndex ] );
                            float   distanceBotToNextWaypoint    = (float)bot.distance(    iWayPoints[ iCurrentWayPointIndex ] );
                            boolean playerOutOfBotReach          = ( distanceToPlayer > ShooterSettings.BotSettings.MAX_LEADING_DISTANCE_TO_PLAYER );

                            //wait for player if he is out of reach and farer from the next waypoint than the bot
                            if ( playerOutOfBotReach && distancePlayerToNextWaypoint > distanceBotToNextWaypoint )
                            {
                                //wait for player
                                iState = BotState.EWatchPlayer;
                            }
                            else
                            {
                                //walk to next waypoint
                                iState = BotState.EWalkToNextWayPoint;
                            }
                            break;
                        }

                        case EWalkWaypoints:
                        {
                            iState = BotState.EWalkToNextWayPoint;
                            break;
                        }

                        case EWatchPlayer:
                        {
                            iState = BotState.EWatchPlayer;
                            break;
                        }

                        case ESleep:
                        {
                            iState = BotState.ESleep;
                            break;
                        }
                    }

                    //perform action according to current state
                    switch ( iState )
                    {
                        case EWatchPlayer:
                        {
                            //bot is standing still turning to the player
                            nextPosX = bot.x;
                            nextPosY = bot.y;

                            if ( angleBotToPlayer != iFacingAngle )
                            {
                                rotateBotTo( angleBotToPlayer );
                            }
                            break;
                        }

                        case EWalkTowardsPlayer:
                        {
                            //bot walks towards the player - turning to him
                            nextPosX = bot.x - LibMath.sinDeg( -angleBotToPlayer ) * BotSettings.SPEED_WALKING;
                            nextPosY = bot.y - LibMath.cosDeg( -angleBotToPlayer ) * BotSettings.SPEED_WALKING;
                            if ( angleBotToPlayer != iFacingAngle ) iFaceAngleChanged = true;
                            iFacingAngle = angleBotToPlayer;
                            break;
                        }

                        case ESleep:
                        {
                            //remain on position and do not change angle
                            nextPosX = bot.x;
                            nextPosY = bot.y;
                            break;
                        }

                        case EWalkToNextWayPoint:
                        {
                            //shouldn't have been initialized without wayPoints ..
                            if ( iWayPoints != null )
                            {
                                //check if the current wayPoint has been reached?
                                if ( checkCollision( new Ellipse2D.Float( iWayPoints[ iCurrentWayPointIndex ].x - BotSettings.WAY_POINT_RADIUS, iWayPoints[ iCurrentWayPointIndex ].y - BotSettings.WAY_POINT_RADIUS, 2 * BotSettings.WAY_POINT_RADIUS, 2 * BotSettings.WAY_POINT_RADIUS ) ) )
                                {
                                    //wayPoint has been reached
                                    //Debug.info( "wayPoint reached" );

                                    //target next wayPoint
                                    ++iCurrentWayPointIndex;

                                    //begin with 1st waypoint
                                    if ( iCurrentWayPointIndex >= iWayPoints.length ) iCurrentWayPointIndex = 0;
                                }

                                //move player towards current waypoint
                                Point2D.Float currentWayPoint = iWayPoints[ iCurrentWayPointIndex ];

                                //move bot towards the wayPoint
                                float angleWaypointToBot = LibMath.getAngleCorrect( currentWayPoint, bot );
                                float angleBotToWayPoint = LibMath.normalizeAngle( angleWaypointToBot - 180.0f );
                                //Debug.bot.out( "angle player to bot is: " + anglePlayerToBot );
                                //Debug.bot.out( "angle bot to player is: " + angleBotToPlayer );

                                //bot walks towards current waypoint - turning to him
                                nextPosX = bot.x - LibMath.sinDeg( -angleBotToWayPoint ) * BotSettings.SPEED_WALKING;
                                nextPosY = bot.y - LibMath.cosDeg( -angleBotToWayPoint ) * BotSettings.SPEED_WALKING;

                                //order rotation to
                                rotateBotTo( angleBotToWayPoint );
                            }
/*
                            //check if this bot sees the player
                            if ( botSeesThePlayer( -angleBotToPlayer ) )
                            {
                                System.out.println( "player sighted" );
                            }
*/
                            break;
                        }
                    }

                    //check collisions
                    float oldPosX = getCenterHorz().x;
                    float oldPosY = getCenterHorz().y;

                    //set to new position
                    setCenterHorz( nextPosX, nextPosY );

                    //check collisions to other bots
                    if ( checkCollisionsToOtherBots() )
                    {
                        //set back to old position
                        setCenterHorz( oldPosX, oldPosY );
                    }
                    //check collisions to player
                    else if ( checkCollisionsToPlayer() )
                    {
                        //undo setting to new position
                        setCenterHorz( oldPosX, oldPosY );
                    }
                    else
                    {
                        //leave the new position

                        //translate all bullet holes to new position
                        BulletHole.translateAll( this, nextPosX - oldPosX, nextPosY - oldPosY );
                    }

                    //rotate if faceAngle changed
                    if ( iFaceAngleChanged )
                    {
                        BulletHole.rotateForBot( this, iFacingAngle );
                    }
                    break;
                }
            }

            //perform transformations here

            //let eyes blink
            if ( System.currentTimeMillis() >= iNextEyeChange )
            {
                iEyesOpen = !iEyesOpen;
                if ( iEyesOpen )
                {
                    iBotMeshes.iFace.changeTexture( iBotMeshes.iTemplate.iTexFaceEyesShut.iTexture, iBotMeshes.iTemplate.iTexFaceEyesOpen.iTexture );
                }
                else
                {
                    iBotMeshes.iFace.changeTexture( iBotMeshes.iTemplate.iTexFaceEyesOpen.iTexture, iBotMeshes.iTemplate.iTexFaceEyesShut.iTexture );
                }
                setNextEyeChange();
            }

            //translate and rotate the bot's mesh
            LibVertex botAnchor = getAnchor();

            //set anchors for all meshes
            iBotMeshes.setNewAnchor( botAnchor, false, null );

            //transform bot's limbs
            iBotMeshes.transformLimbs( botAnchor, iFacingAngle );
        }

        public final void draw()
        {
            switch ( iType )
            {
                case ETypeEnemy:
                case ETypeFriend:
                {
                    //turn bullet holes?? :(

                    //draw bot's mesh
                    iBotMeshes.draw();

                    //draw bot's debug-circles
                    drawDebugCircles();

                    break;
                }
            }
        }

        public final Shot getShot()
        {
            return new Shot
            (
                ShotType.ESharpAmmo,
                Shot.ShotOrigin.EEnemies,
                0.0f,   //irregularityAngle
                0.0f,   //irregularityDepth
                getAnchor().x,
                getAnchor().y,
                getAnchor().z + ( getHeight() / 2 ),
                iFacingAngle,
                0.0f,   //rotX
                BotSettings.SHOT_RANGE,
                LibHoleSize.E9mm,   // bot always fires 9mm
                ShooterDebug.shot
            );
        }

        public final Shot getViewShot( float viewAngle )
        {
            return new Shot
            (
                ShotType.EViewOnly,
                Shot.ShotOrigin.EEnemies,
                0.0f,   //irregularityAngle
                0.0f,   //irregularityDepth
                getAnchor().x,
                getAnchor().y,
                getAnchor().z + ( getHeight() * 3 / 4 ),
                viewAngle,
                0.0f,   //rotX
                BotSettings.VIEW_RANGE,
                LibHoleSize.ENone,
                ShooterDebug.shot
            );
        }

        public final boolean botSeesThePlayer( float viewAngle )
        {
            //check if this enemy bot sees the player - copy current angle
            Shot     shot     = getViewShot( viewAngle );
            HitPoint hitPoint = shot.launch();

            return ( hitPoint != null && hitPoint.carrier.getHitPointCarrier() == GameObject.HitPointCarrier.EPlayer );
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iCylinder.getAnchor();
        }

        @Override
        public final float getCarriersFaceAngle()
        {
            return iFacingAngle;
        }

        @Override
        public final Vector<HitPoint> launchShot( Shot shot )
        {
            return iBotMeshes.launchShot( shot );
        }

        public final boolean isAlive()
        {
            return iIsAlive;
        }

        @Override
        public HitPointCarrier getHitPointCarrier()
        {
            return HitPointCarrier.EBot;
        }

        private void rotateBotTo( float targetAngle )
        {
            //get the rotation of the bot
            float angleDistance   = LibMath.getAngleDistanceRelative( iFacingAngle, targetAngle );
            float turningDistance = Math.abs( angleDistance );

            if ( turningDistance >= BotSettings.SPEED_TURNING_MIN )
            {
                //clip turning distance
                if ( turningDistance > BotSettings.SPEED_TURNING_MAX ) turningDistance = BotSettings.SPEED_TURNING_MAX;

                ShooterDebug.bot.out( "turning bot, src ["+iFacingAngle+"] target ["+targetAngle+"] distance is [" + angleDistance + "]" );

                iFaceAngleChanged = true;

                if ( angleDistance < 0 )
                {
                    iFacingAngle = LibMath.normalizeAngle( iFacingAngle - turningDistance );
                }
                else if ( angleDistance > 0 )
                {
                    iFacingAngle = LibMath.normalizeAngle( iFacingAngle + turningDistance );
                }
            }
        }

        public void say( Sound sound )
        {
            float distanceToPlayer = (float)getCenterHorz().distance( Level.currentPlayer().getCylinder().getCenterHorz() );

            sound.playFx( distanceToPlayer );
        }
    }
