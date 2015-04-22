/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.awt.geom.*;
    import  java.util.*;

import de.christopherstock.lib.g3d.*;
import de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.g3d.*;
import de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.player.*;
import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   The superclass of all non-player-characters.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Bot extends GameObject
    {
        /**************************************************************************************
        *   The maximum turning speed in degrees per tick.
        **************************************************************************************/
        public      static      final       float       SPEED_TURNING_MAX       = 10.0f;

        /**************************************************************************************
        *   The minimum turning speed in degrees per tick.
        **************************************************************************************/
        public      static      final       float       SPEED_TURNING_MIN       = 2.5f;

        public enum BotType
        {
            ETypeEnemy,
            ETypeFriend,
            ;
        }

        public enum BotState
        {
            EStateWalkTowardsPlayer,
            EStateWaiting,
            EStateSleeping,
            EStateWalkWayPoints,
            ;
        }

        private     static  final   float               WAY_POINT_RADIUS        = 0.25f;

        private     static  final   float               SHOT_RANGE              = 20.0f;
        private     static  final   float               SPEED_WALKING           = 0.05f;
        public      static  final   float               HEIGHT                  = 1.3f;
        public      static  final   float               RADIUS                  = 0.25f;

        public                      BotMeshes           botMeshes               = null;

        /** current facing angle. */
        private                     float               facingAngle             = 0.0f;

        protected                   BotType             type                    = null;
        protected                   BotState            state                   = null;
        private                     boolean             isAlive                 = true;
        private                     Point2D.Float[]     wayPoints               = null;
        private                     int                 currentWayPointIndex    = 0;

        /** the collision unit for simple collisions */
        protected                   Cylinder            cylinder                = null;

        public Bot( BotType aType, LibVertex aStartPosition, Point2D.Float[] aWayPoints, BotState aState )
        {
            super( GameObject.HitPointCarrier.EBot );
            type        = aType;
            cylinder    = new Cylinder( this, aStartPosition, Bot.RADIUS, Bot.HEIGHT );
            wayPoints   = aWayPoints;
            state       = aState;

            //init mesh-collection
            botMeshes      = new BotMeshes();
            botMeshes.setNewAnchor( aStartPosition, true );
            botMeshes.assignParentOnFaces( this );

            //set bot to 1st waypoint
            if ( aState == Bot.BotState.EStateWalkWayPoints )
            {
                setCenterHorz( wayPoints[ 0 ].x, wayPoints[ 0 ].y );
            }
        }

        protected final boolean checkCollision( Cylinder aCylinder )
        {
            return cylinder.checkCollision( aCylinder );
        }

        protected final boolean checkCollision( Ellipse2D.Float aEllipse )
        {
            return cylinder.checkCollision( aEllipse );
        }

        public final Point2D.Float getCenterHorz()
        {
            return cylinder.getCenterHorz();
        }

        protected final void drawDebugCircles()
        {
            if ( ShooterSettings.DEBUG_DRAW_BOT_CIRCLES )
            {
                //Debug.bot.out( "drawing bot .." );
                int VERTICAL_SLICES = 4;
                LibVertex ank = getAnchor();
                Colors col = null;
                switch ( type )
                {
                    case ETypeFriend:   col = Colors.EBlueLight;    break;
                    case ETypeEnemy:    col = Colors.EBlueDark;      break;
                }
                for ( int i = 0; i <= VERTICAL_SLICES; ++i )
                {
                    new FaceEllipseFloor( null, col, ank.x, ank.y, ank.z + ( i * cylinder.getHeight() / VERTICAL_SLICES ), cylinder.getRadius(), cylinder.getRadius() ).draw();
                }
            }
        }

        protected final float getHeight()
        {
            return cylinder.getHeight();
        }

        protected final void setCenterHorz( float newX, float newY )
        {
            cylinder.setCenterHorz( newX, newY );
        }

        public static final void drawAll()
        {
            //Debug.bot.out( "drawing ALL bots .."+botQueue.size()+"" );
            for ( Bot bot : Level.bots )
            {
                bot.draw();
            }
        }

        public static final void checkCollisionsAll( Cylinder c )
        {
            for ( Bot bot : Level.bots )
            {
                //check bot-collision with player
                if ( bot.checkCollision( c ) )
                {
                    Debug.bot.out( "bot touched" );

                    //sleep if being touched
                    //bot.state = BotState.EStateSleeping;
                }
            }
        }

        protected final boolean checkCollisionsToOtherBots()
        {
            for ( Bot bot : Level.bots )
            {
                //skip own !
                if ( this == bot ) continue;

                //check bot-collision with other bot
                if ( bot.checkCollision( cylinder ) )
                {
                    Debug.bot.out( "own bot touched" );
                    return true;

                    //sleep if being touched
                    //bot.state = BotState.EStateSleeping;
                }
            }

            return false;
        }

        public final void animate()
        {
            switch ( type )
            {
                case ETypeEnemy:
                case ETypeFriend:
                {
                    Point2D.Float player = Player.singleton.cylinder.getCenterHorz();
                    Point2D.Float bot    = getCenterHorz();

                    //move bot towards the player
                    float anglePlayerToBot = LibMath.getAngleCorrect( player, bot );
                    float angleBotToPlayer = anglePlayerToBot - 180.0f;
                    //Debug.bot.out( "angle player to bot is: " + anglePlayerToBot );
                    //Debug.bot.out( "angle bot to player is: " + angleBotToPlayer );

                    float   nextPosX = 0.0f;
                    float   nextPosY = 0.0f;
                    boolean faceAngleChanged = false;

                    switch ( state )
                    {
                        case EStateWalkTowardsPlayer:
                        {
                            //bot walks towards the player - turning to him
                            nextPosX = bot.x - LibMath.sinDeg( -angleBotToPlayer ) * SPEED_WALKING;
                            nextPosY = bot.y - LibMath.cosDeg( -angleBotToPlayer ) * SPEED_WALKING;
                            if ( angleBotToPlayer != facingAngle ) faceAngleChanged = true;
                            facingAngle = angleBotToPlayer;
                            break;
                        }

                        case EStateWaiting:
                        {
                            //bot is standing still turning to the player
                            nextPosX = bot.x;
                            nextPosY = bot.y;
                            if ( angleBotToPlayer != facingAngle ) faceAngleChanged = true;
                            facingAngle = angleBotToPlayer;
                            break;
                        }

                        case EStateSleeping:
                        {
                            //remain on position and do not change angle
                            nextPosX = bot.x;
                            nextPosY = bot.y;
                            break;
                        }

                        case EStateWalkWayPoints:
                        {
                            //shouldn't have been initialized without wayPoints ..
                            if ( wayPoints != null )
                            {
                                //check if the current wayPoint has been reached?
                                if ( checkCollision( new Ellipse2D.Float( wayPoints[ currentWayPointIndex ].x - WAY_POINT_RADIUS, wayPoints[ currentWayPointIndex ].y - WAY_POINT_RADIUS, 2 * WAY_POINT_RADIUS, 2 * WAY_POINT_RADIUS ) ) )
                                {
                                    //wayPoint has been reached
                                    //Debug.info( "wayPoint reached" );

                                    //aim next wayPoint
                                    ++currentWayPointIndex;


                                    //botMeshes.testAnimateLegs();



                                    if ( currentWayPointIndex >= wayPoints.length ) currentWayPointIndex = 0;
                                }

                                //move player towards current waypoint
                                Point2D.Float wp = wayPoints[ currentWayPointIndex ];

                                //move bot towards the wayPoint
                                float angleWaypointToBot = LibMath.getAngleCorrect( wp, bot );
                                float angleBotToWayPoint = LibMath.normalizeAngle( angleWaypointToBot - 180.0f );
                                //Debug.bot.out( "angle player to bot is: " + anglePlayerToBot );
                                //Debug.bot.out( "angle bot to player is: " + angleBotToPlayer );

                                //bot walks towards current waypoint - turning to him
                                nextPosX = bot.x - LibMath.sinDeg( -angleBotToWayPoint ) * SPEED_WALKING;
                                nextPosY = bot.y - LibMath.cosDeg( -angleBotToWayPoint ) * SPEED_WALKING;

                                //get the rotation of the bot
                                float angleDistance   = LibMath.getAngleDistance( facingAngle, angleBotToWayPoint );
                                float turningDistance = Math.abs( angleDistance );

                                if ( turningDistance >= SPEED_TURNING_MIN )
                                {
                                    //clip turning distance
                                    if ( turningDistance > SPEED_TURNING_MAX ) turningDistance = SPEED_TURNING_MAX;

                                    Debug.bot.out( "turning bot, src ["+facingAngle+"] target ["+angleBotToWayPoint+"] distance is [" + angleDistance + "]" );

                                    faceAngleChanged = true;

                                    if ( angleDistance < 0 )
                                    {
                                        facingAngle = LibMath.normalizeAngle( facingAngle - turningDistance );
                                    }
                                    else if ( angleDistance > 0 )
                                    {
                                        facingAngle = LibMath.normalizeAngle( facingAngle + turningDistance );
                                    }
                                }
                                else
                                {
                                    //Debug.info( "do not turn bot ["+angleBotToWayPoint+"] ["+facingAngle+"]" );
                                }
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

                    //assign new position
                    setCenterHorz( nextPosX, nextPosY );

                    //BulletHole.followBot( this );

                    //check collisions
                    if ( checkCollisionsToOtherBots() )
                    {
                        //resetting though touching another bot

                        //undo setting to new position
                        setCenterHorz( oldPosX, oldPosY );
                    }
                    else
                    {
                        //translate to next position
                        BulletHole.translateAll( this, nextPosX - oldPosX, nextPosY - oldPosY );
                    }

                    //rotate if faceAngle changed
                    if ( faceAngleChanged )
                    {
                        BulletHole.rotateForBot( this, facingAngle );
                    }
                    break;
                }
            }
        }

        private final void draw()
        {
            switch ( type )
            {
                case ETypeEnemy:
                case ETypeFriend:
                {
                    //translate and rotate the bot's mesh
                    LibVertex a = getAnchor();
                    botMeshes.setNewAnchor( a, false );
                    botMeshes.translateAndRotateXYZ( a.x, a.y, a.z, 0.0f, 0.0f, facingAngle );


                    //bullet holes??


                    //modified?
//                    botMeshes.testAnimateLegs();

                    //draw bot's mesh
                    botMeshes.draw();

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
                Shot.ShotOrigin.EEnemies,
                false,
                0.0f,   //irregularityAngle
                0.0f,   //irregularityDepth
                getAnchor().x,
                getAnchor().y,
                getAnchor().z + ( getHeight() / 2 ),
                facingAngle,
                0.0f,   //rotX
                SHOT_RANGE,
                BulletHole.PointSize.E9mm   // bot always fires 9mm
            );
        }

        public final Shot getViewShot( float viewAngle )
        {
            return new Shot
            (
                Shot.ShotOrigin.EEnemies,
                true,
                0.0f,   //irregularityAngle
                0.0f,   //irregularityDepth
                getAnchor().x,
                getAnchor().y,
                getAnchor().z + ( getHeight() * 3 / 4 ),
                viewAngle,
                0.0f,   //rotX
                SHOT_RANGE,
                BulletHole.PointSize.ENone
            );
        }

        public final boolean botSeesThePlayer( float viewAngle )
        {
            //check if this enemy bot sees the player - copy current angle
            Shot     shot     = getViewShot( viewAngle );
            HitPoint hitPoint = shot.launch();

            return ( hitPoint != null && hitPoint.gameObject.getHitPointCarrier() == GameObject.HitPointCarrier.EPlayer );
        }

        @Override
        public final LibVertex getAnchor()
        {
            return cylinder.getAnchor();
        }

        @Override
        public final float getCarriersFaceAngle()
        {
            return facingAngle;
        }

        @Override
        public final Vector<HitPoint> launchShot( Shot shot )
        {
            return botMeshes.launchShot( shot );
        }

        public final boolean isAlive()
        {
            return isAlive;
        }
    }
