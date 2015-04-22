/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.geom.*;
    import  java.util.*;

import de.christopherstock.lib.g3d.*;
import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
import  de.christopherstock.shooter.game.player.*;

    /**************************************************************************************
    *   Represents the level-system. Just a few values to init all levels.
    *
    *   @author     Christopher Stock
    *   @version    0.2
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
        private     static          WallCollection[]            meshCollections         = null;
        public      static          Vector<Bot>                 bots                    = new Vector<Bot>();

        private                     float                       startPosX               = 0;
        private                     float                       startPosY               = 0;
        private                     float                       startPosZ               = 0;
        private                     float                       startRotX               = 0;
        private                     float                       startRotY               = 0;
        private                     float                       startRotZ               = 0;

        private Level( float aStartPosX, float aStartPosY, float aStartPosZ, float aStartRotX, float aStartRotY, float aStartRotZ )
        {
            startPosX          = aStartPosX;
            startPosY          = aStartPosY;
            startPosZ          = aStartPosZ;
            startRotX          = aStartRotX;
            startRotY          = aStartRotY;
            startRotZ          = aStartRotZ;
        }

        protected static final void addBot( Bot botToAdd )
        {
            bots.add( botToAdd );
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
            meshCollections = WallCollection.LEVEL_MESH_COLLECTION_DATA[ newLevel.ordinal() ];
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
                new Point2D.Float( 0.0f,  0.0f      ),
                new Point2D.Float( 2.5f,  0.0f      ),
                new Point2D.Float( 2.5f,  2.5f      ),
                new Point2D.Float( 0.0f,  2.5f      ),
                new Point2D.Float( 0.0f,  5.0f      ),
                new Point2D.Float( 2.5f,  5.0f      ),
                new Point2D.Float( 2.5f,  2.5f      ),
                new Point2D.Float( 0.0f,  2.5f      ),
            };
/*
            final Point2D.Float[] wayPointsEnemy2 = new Point2D.Float[]
            {
                new Point2D.Float( 10.0f,   10.0f   ),
                new Point2D.Float( 15.0f,   10.0f   ),
                new Point2D.Float( 15.0f,   12.5f   ),
                new Point2D.Float( 15.0f,   14.5f   ),
            };
*/

//            addBot( Bot.BotType.ETypeFriend,  14.0f,    14.0f,  0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.enemy1, null );
//            addBot( Bot.BotType.ETypeFriend,  24.0f,    24.0f,  0.0f + 0.001f, Bot.RADIUS, Bot.HEIGHT, Meshes.enemy1, null );


            addBot( new Bot( Bot.BotType.ETypeEnemy, new LibVertex( 3.5f, 10.5f, 0.0f + 0.001f ),  wayPointsEnemy1,    Bot.BotState.EStateWalkWayPoints   ) );
/*
            addBot( new BotMesh( Bot.BotType.ETypeEnemy, new Vertex( 2.5f, 5.5f,  0.0f + 0.001f ),  D3dsImporter.D3dsFiles.EMeshWoman1,  wayPointsEnemy2,    Bot.BotState.EStateWalkWayPoints   ) );
            addBot( new BotMesh( Bot.BotType.ETypeEnemy, new Vertex( 1.5f, 15.0f, 0.0f + 0.001f ),  D3dsImporter.D3dsFiles.EMeshWoman1,  null,               Bot.BotState.EStateWaiting         ) );
*/

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
            //draw all walls
            for ( WallCollection meshCollection : meshCollections )
            {
                meshCollection.draw();
            }
        }

        public static final HitPoint launchShot( Shot shot )
        {
            //collect all hit points
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //launch the shot on all mesh-collections
            for ( WallCollection meshCollection : meshCollections )
            {
                //add all hitPoints that appear in all 'walls'
                hitPoints.addAll( meshCollection.launchShot( shot ) );
            }

            //launch the shot on all bots' collision unit ( if not shot by a bot! )
            if ( shot.origin != Shot.ShotOrigin.EEnemies )
            {
                for ( Bot bot : bots )
                {
                    Vector<HitPoint> hps = bot.launchShot( shot );
                    hitPoints.addAll( hps );
                }
            }

            //launch the shot on the player ( if not shot by the player )
            if ( shot.origin != Shot.ShotOrigin.EPlayer )
            {
                hitPoints.addAll( Player.singleton.cylinder.launchShot( shot ) );
            }

            //return nearest hitpoint
            return HitPoint.getNearestHitPoint( hitPoints );
        }

        /**************************************************************************************
        *   Returns the nearest floor. May be useful one day. !?
        *
        *   @param      point   The point to get the accoring z-position of any face the point lies on.
        *   @return             The nearest floor's z-position or <code>null</code> if non-existent.
        **************************************************************************************/
/*
        @Deprecated
        public static final Float getNearestFloor( Point2D.Float point )
        {
            //collect all hit points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( WallCollection meshCollection : meshCollections )
            {
                //launch the shot on all mesh-collections
                hitPointsZ.addAll( meshCollection.launchFloorCheck( point ) );
            }

            //return nearest floor
            return Player.user.cylinder.getNearestFloor( hitPointsZ, Player.MAX_CLIMBING_UP_Z );
        }
*/
        public static final Float getHighestFloor( Point2D.Float point )
        {
            //collect all hit points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( WallCollection meshCollection : meshCollections )
            {
                //launch the shot on all mesh-collections
                hitPointsZ.addAll( meshCollection.launchFloorCheck( point ) );
            }

            //return nearest floor
            return Player.singleton.cylinder.getHighestFloor( hitPointsZ, Player.MAX_CLIMBING_UP_Z );

        }

        public static final boolean checkCollision( Cylinder cylinder )
        {
            //collisions may be disabled
            if ( ShooterSettings.DISABLE_COLLISIONS ) return false;

            //nextPosX
            for ( WallCollection meshCollection : meshCollections )
            {
                //launch the shot on all mesh-collections
                if ( meshCollection.checkCollision( cylinder ) ) return true;
            }

            return false;
        }

        public static final void launchAction( Cylinder cylinder )
        {
            //browse all mesh-collections
            for ( WallCollection meshCollection : meshCollections )
            {
                //launch the shot on this mesh-collection
                meshCollection.launchAction( cylinder );
            }
        }

        public static final void animate()
        {
            //browse all mesh-collections
            for ( WallCollection meshCollection : meshCollections )
            {
                //animate all mesh-collections
                meshCollection.animate();
            }
        }

        public static final void animateAllBots()
        {
            //Debug.bot.out( "animating ALL bots .."+botQueue.size()+"" );
            for ( Bot bot : Level.getAllBots() )
            {
                if ( bot.isAlive() )
                {
                    bot.animate();
                }
                else
                {
                    Level.bots.removeElement( bot );
                }
            }
        }

        public static Vector<Bot> getAllBots()
        {
            return bots;
        }
    }
