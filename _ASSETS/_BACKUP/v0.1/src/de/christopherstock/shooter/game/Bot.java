/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.geom.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.collision.unit.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.gl.mesh.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.util.*;

    /**************************************************************************************
    *   The superclass of all non-player-characters.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class Bot implements HitPointCarrier
    {
        public enum BotType
        {
            ETypeEnemy,
            ETypeFriend,
            ;
        }

        private enum BotState
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

        /** graphical representation. */
        private                     Mesh                mesh                    = null;

        /** current facing angle. */
        public                      float               facingAngle             = 0.0f;

        private                     BotType             type                    = null;
        private                     BotState            state                   = null;
        private                     boolean             alive                   = true;
        private                     Point2D.Float[]     wayPoints               = null;
        private                     int                 currentWayPointIndex    = 0;

        /** the collision unit */
        private                     Cylinder            cylinder                = null;

        public Bot( BotType type, float x, float y, float z, float radius, float height, Mesh mesh, Point2D.Float[] wayPoints )
        {
            this.type       = type;
            this.state      = BotState.EStateWalkTowardsPlayer;
            this.mesh       = mesh;
            this.cylinder   = new Cylinder( HitPoint.Carrier.EBot, new Vertex( x, y, z ), radius, height );
            this.wayPoints  = wayPoints;

            //TODO init state ..
            if ( type == BotType.ETypeEnemy ) this.state = BotState.EStateWalkWayPoints;

        }

        protected static final void animateAll()
        {
            //Debug.bot.out( "animating ALL bots .."+botQueue.size()+"" );
            for ( Bot bot : Level.bots )
            {
                if ( bot.alive )
                {
                    bot.animate();
                }
                else
                {
                    Level.bots.removeElement( bot );
                }
            }
        }

        protected static final void drawAll()
        {
            //Debug.bot.out( "drawing ALL bots .."+botQueue.size()+"" );
            for ( Bot bot : Level.bots )
            {
                bot.draw();
            }
        }

        protected static final void checkCollisionsAll( Cylinder c )
        {
            for ( Bot bot : Level.bots )
            {
                //check bot-collision with player
                if ( bot.cylinder.checkCollision( c ) )
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
                if ( bot.cylinder.checkCollision( cylinder ) )
                {
                    Debug.bot.out( "own bot touched" );
                    return true;

                    //sleep if being touched
                    //bot.state = BotState.EStateSleeping;
                }
            }

            return false;

        }

        private final void animate()
        {
            switch ( type )
            {
                case ETypeEnemy:
                case ETypeFriend:
                {
                    Point2D.Float player = Player.user.cylinder.getCenterHorz();
                    Point2D.Float bot    = cylinder.getCenterHorz();

                    //move bot towards the player
                    float anglePlayerToBot = UMath.getAngleCorrect( player, bot );
                    float angleBotToPlayer = anglePlayerToBot - 180.0f;
                    //Debug.bot.out( "angle player to bot is: " + anglePlayerToBot );
                    //Debug.bot.out( "angle bot to player is: " + angleBotToPlayer );

                    float nextPosX = 0.0f;
                    float nextPosY = 0.0f;

                    switch ( state )
                    {
                        case EStateWalkTowardsPlayer:
                        {
                            //bot walks towards the player - turning to him
                            nextPosX = bot.x - UMath.sinDeg( -angleBotToPlayer ) * SPEED_WALKING;
                            nextPosY = bot.y - UMath.cosDeg( -angleBotToPlayer ) * SPEED_WALKING;
                            facingAngle = angleBotToPlayer;
                            break;
                        }

                        case EStateWaiting:
                        {
                            //bot is standing still turning to the player
                            nextPosX = bot.x;
                            nextPosY = bot.y;
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
                            //shouldn't be initialized without wayPoints
                            if ( wayPoints != null )
                            {
                                //check if the current wayPoint has been reached?
                                if ( cylinder.checkCollision( new Ellipse2D.Float( wayPoints[ currentWayPointIndex ].x - WAY_POINT_RADIUS, wayPoints[ currentWayPointIndex ].y - WAY_POINT_RADIUS, 2 * WAY_POINT_RADIUS, 2 * WAY_POINT_RADIUS ) ) )
                                {
                                    //wayPoint has been reached
                                    //System.out.println( "wayPoint reached" );

                                    //aim next wayPoint
                                    ++currentWayPointIndex;

                                    if ( currentWayPointIndex >= wayPoints.length ) currentWayPointIndex = 0;

                                }

                                //move player towards current waypoint
                                Point2D.Float wp = wayPoints[ currentWayPointIndex ];

                                //move bot towards the wayPoint
                                float angleWaypointToBot = UMath.getAngleCorrect( wp, bot );
                                float angleBotToWayPoint = angleWaypointToBot - 180.0f;
                                //Debug.bot.out( "angle player to bot is: " + anglePlayerToBot );
                                //Debug.bot.out( "angle bot to player is: " + angleBotToPlayer );

                                //bot walks towards current waypoint - turning to him
                                nextPosX = bot.x - UMath.sinDeg( -angleBotToWayPoint ) * SPEED_WALKING;
                                nextPosY = bot.y - UMath.cosDeg( -angleBotToWayPoint ) * SPEED_WALKING;
                                facingAngle = angleBotToWayPoint;
                            }

                            //check if this bot sees the player
                            if ( botSeesThePlayer( -angleBotToPlayer ) )
                            {
                                //System.out.println( "player sighted" );

                            }

                            break;
                        }

                    }

                    //check collisions
                    float oldPosX = cylinder.getCenterHorz().x;
                    float oldPosY = cylinder.getCenterHorz().y;

                    //assign new position
                    cylinder.setCenterHorz( nextPosX, nextPosY );

                    //BulletHole.followBot( this );

                    //check collisions
                    if ( checkCollisionsToOtherBots() )
                    {
                        System.out.println( "other bot touched! resetting .." );
                        //undo setting to new position
                        cylinder.setCenterHorz( oldPosX, oldPosY );
                    }
                    else
                    {
                        BulletHole.translateAll( this, nextPosX - oldPosX, nextPosY - oldPosY );
                    }
                    BulletHole.rotateForBot( this, facingAngle );
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
                    mesh.setNewAnchor( cylinder.getAnchor(), false );
                    mesh.translateAndRotateXYZ( cylinder.getAnchor().x, cylinder.getAnchor().y, cylinder.getAnchor().z, 0.0f, 0.0f, facingAngle );

                    //draw bot's mesh
                    mesh.draw();

                    //draw bot's debug-circles
                    drawDebugCircles();

                    break;
                }
            }
        }

        private final void drawDebugCircles()
        {
            if ( Shooter.DEBUG_DRAW_BOT_CIRCLES )
            {
                //Debug.bot.out( "drawing bot .." );
                int VERTICAL_SLICES = 4;
                Vertex ank = cylinder.getAnchor();
                Colors col = null;
                switch ( type )
                {
                    case ETypeFriend:   col = Colors.EBlueLight;    break;
                    case ETypeEnemy:    col = Colors.ERedDark;      break;
                }
                for ( int i = 0; i <= VERTICAL_SLICES; ++i )
                {
                    GLView.drawFace( new FaceEllipseFloor( null, col, ank.x, ank.y, ank.z + ( i * cylinder.getHeight() / VERTICAL_SLICES ), cylinder.getRadius(), cylinder.getRadius() ) );
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
                cylinder.getAnchor().x,
                cylinder.getAnchor().y,
                cylinder.getAnchor().z + ( cylinder.getHeight() / 2 ),
                facingAngle,
                0.0f,   //rotX
                SHOT_RANGE,
                true
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
                cylinder.getAnchor().x,
                cylinder.getAnchor().y,
                cylinder.getAnchor().z + ( cylinder.getHeight() * 3 / 4 ),
                viewAngle,
                0.0f,   //rotX
                SHOT_RANGE,
                false
            );
        }

        public final boolean botSeesThePlayer( float viewAngle )
        {
            //check if this enemy bot sees the player - copy current angle
            Shot     shot     = getViewShot( viewAngle );
            HitPoint hitPoint = shot.launch();

            return ( hitPoint != null && hitPoint.carrier == HitPoint.Carrier.EPlayer );
        }

        public final Vertex getAnchor()
        {
            return cylinder.getAnchor();
        }

        public final float getCarriersFaceAngle()
        {
            return facingAngle;
        }

        public final HitPoint launchShot( Shot shot )
        {
            return cylinder.launchShot( shot );
        }
    }
