/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.collision.*;
import de.christopherstock.shooter.game.collision.unit.*;
    import  de.christopherstock.shooter.game.player.*;
import  de.christopherstock.shooter.gl.g3d.*;
import de.christopherstock.shooter.gl.mesh.*;

    /**************************************************************************************
    *   Represents the level-system. Just a few values to init all levels.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public enum Level
    {
        ELevel1
        (
            1.0f, 9.0f, Player.DEPTH_TOE,
            0.0f, 0.0f, 270.0f
        );

        //values for the current level
        private     static          Level                       currentLevel            = null;
        private     static          MeshCollection[]            meshCollections         = null;
        protected   static          Vector<Bot>                 bots                    = new Vector<Bot>();

        private                     float                       startPosX               = 0;
        private                     float                       startPosY               = 0;
        private                     float                       startPosZ               = 0;
        private                     float                       startRotX               = 0;
        private                     float                       startRotY               = 0;
        private                     float                       startRotZ               = 0;

        private Level( float startPosX, float startPosY, float startPosZ, float startRotX, float startRotY, float startRotZ )
        {
            this.startPosX          = startPosX;
            this.startPosY          = startPosY;
            this.startPosZ          = startPosZ;
            this.startRotX          = startRotX;
            this.startRotY          = startRotY;
            this.startRotZ          = startRotZ;
        }

        protected static final void addBot( Bot.BotType botType, float x, float y, float z, float radius, float height, Mesh mesh, Point2D.Float[] wayPoints )
        {
            bots.add( new Bot( botType, x, y, z, radius, height, mesh, wayPoints ) );
            Debug.bot.out( "ADDING BOT ["+Level.bots.size()+"]" );
        }

        /**************************************************************************************
        *   Sets up a new level.
        *
        *   @param  newLevel    The ID of the desired level.
        **************************************************************************************/
        public static final void setupNewLevel( Level newLevel )
        {
            //switch initCurrentLevel

            //assign ID of the current level and all level-specific values
            currentLevel    = newLevel;
            meshCollections = MeshCollection.LEVEL_MESH_COLLECTION_DATA[ newLevel.ordinal() ];
            Player.initUser
            (
                currentLevel.startPosX,
                currentLevel.startPosY,
                currentLevel.startPosZ,
                currentLevel.startRotX,
                currentLevel.startRotY,
                currentLevel.startRotZ
            );

            //spawn an item and a bot
            Item.addItem( 0.0f,  0.0f,  0.0f + 0.01f, 0.1f );
            Item.addItem( 14.0f, 8.0f,  0.0f + 0.01f, 0.1f );



            final Point2D.Float[] wayPointsEnemy1 = new Point2D.Float[]
            {
                new Point2D.Float( 0,  0        ),
                new Point2D.Float( 5.0f,  0        ),
                new Point2D.Float( 5.0f,  2.5f        ),
                new Point2D.Float( 5.0f,  4.5f        ),
                /*
                new Point2D.Float( 0,  5        ),
                new Point2D.Float( 2.5f,  5     ),
                new Point2D.Float( 5,  5        ),
                new Point2D.Float( 5,  2.5f     ),
                new Point2D.Float( 5,  0        ),
                new Point2D.Float( 2.5f,  0     ),
                */
            };

//            addBot( Bot.BotType.ETypeFriend,  14.0f,    14.0f,  0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.enemy1, null );
//            addBot( Bot.BotType.ETypeFriend,  24.0f,    24.0f,  0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.enemy1, null );


            addBot( Bot.BotType.ETypeEnemy,   0.0f,     0.0f,   0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.enemy1, wayPointsEnemy1 );



/*
            addBot(   24.0f,  24.0f,   0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.object1 );
            addBot(   34.0f,  34.0f,   0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.object1 );
            addBot(   14.0f,  14.0f,   0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.object1 );
            addBot(   34.0f,  24.0f,   0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.object1 );
*/


            //addBot(   10.0f, 10.0f,  0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.object1 );



        }

        /**************************************************************************************
        *   Draws the level onto the screen.
        **************************************************************************************/
        public static final void draw()
        {
            //draw all MeshCollections

            for ( MeshCollection meshCollection : meshCollections )
            {
                meshCollection.draw();
            }

            //draw all items and bots
            Item.drawAll();
            Bot.drawAll();

        }

        public static final HitPoint launchShot( Shot shot )
        {
            //collect all hit points
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //launch the shot on all mesh-collections
            for ( MeshCollection meshCollection : meshCollections )
            {
                //add all hitPoints that appear in all 'walls'
                hitPoints.addAll( meshCollection.launchShot( shot ) );
            }

            //launch the shot on all bots' collision unit ( if not shot by a bot! )
            if ( shot.origin != Shot.ShotOrigin.EEnemies )
            {
                for ( Bot bot : bots )
                {
                    HitPoint hitPoint = bot.launchShot( shot );
                    if ( hitPoint != null )
                    {
                        //add hitPoint after connecting it to this bot
                        hitPoint.connectToCarrier( bot );
                        hitPoints.addElement( hitPoint );
                    }
                }
            }

            //launch the shot on the player ( if not shot by the player )
            if ( shot.origin != Shot.ShotOrigin.EPlayer )
            {
                HitPoint hitPoint = Player.user.cylinder.launchShot( shot );
                if ( hitPoint != null )
                {
                    //add hitPoint after connecting it to this bot
                    hitPoints.addElement( hitPoint );
                }
            }

            //return nearest hitpoint
            return HitPoint.getNearestHitPoint( hitPoints );

        }

        /**************************************************************************************
        *   Return the nearest floor.
        *
        *   @param      point   The point to get the accoring z-position of any face the point lies on.
        *   @return             The nearest floor's z-position or <code>null</code> if non-existent.
        *   @deprecated         May be useful one day.
        **************************************************************************************/
        public static final Float getNearestFloor( Point2D.Float point )
        {
            //collect all hit points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( MeshCollection meshCollection : meshCollections )
            {
                //launch the shot on all mesh-collections
                hitPointsZ.addAll( meshCollection.launchFloorCheck( point ) );
            }

            //return nearest floor
            return Player.user.cylinder.getNearestFloor( hitPointsZ, Player.MAX_CLIMBING_UP_Z );

        }

        public static final Float getHighestFloor( Point2D.Float point )
        {
            //collect all hit points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( MeshCollection meshCollection : meshCollections )
            {
                //launch the shot on all mesh-collections
                hitPointsZ.addAll( meshCollection.launchFloorCheck( point ) );
            }

            //return nearest floor
            return Player.user.cylinder.getHighestFloor( hitPointsZ, Player.MAX_CLIMBING_UP_Z );

        }

        public static final boolean checkCollisionFree( Cylinder cylinder )
        {
            //collisions may be disabled
            if ( Shooter.DISABLE_COLLISIONS ) return true;

            //nextPosX
            for ( MeshCollection meshCollection : meshCollections )
            {
                //launch the shot on all mesh-collections
                if ( meshCollection.checkCollisionFree( cylinder ) == false ) return false;
            }

            return true;

        }

        public static final void launchAction( Cylinder cylinder )
        {
            //browse all mesh-collections
            for ( MeshCollection meshCollection : meshCollections )
            {
                //launch the shot on this mesh-collection
                meshCollection.launchAction( cylinder );
            }
        }

        public static final void animate()
        {
            //browse all mesh-collections
            for ( MeshCollection meshCollection : meshCollections )
            {
                //animate all mesh-collections
                meshCollection.animate();
            }
        }
    }
