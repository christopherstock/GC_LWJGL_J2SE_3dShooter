/*  $Id: Level.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.ui.hud.Ammo.AmmoType;

    /**************************************************************************************
    *   Represents the level-system. Just a few values to init all levels.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Level
    {
        //values for the current level
        private     static          Level                       currentLevel            = null;

        private                     LevelConfig                 iConfig                 = null;
        private                     WallCollection[]            wallCollections         = null;
        public                      Vector<Bot>                 bots                    = null;

        /**************************************************************************************
        *   This level's player-instance being controlled by the user.
        **************************************************************************************/
        public                      Player                      iPlayer                 = null;

        public      static          LevelConfig                 levelToChangeTo         = null;
        public      static          long                        levelChangeBlocker     = 0;

        private Level( LevelConfig aConfig )
        {
            iConfig = aConfig;
            iPlayer = new Player
            (
                iConfig.iStartPosition,
                ShooterSettings.DISABLE_GRAVITY
            );

            bots    = new Vector<Bot>();

            //assign walls and remove all bullet holes
            wallCollections = WallCollection.getLevelWalls( aConfig );

            //remove all bullet holes
            BulletHole.removeAll();

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

            Bot b = new Bot( Bot.BotType.ETypeEnemy, new LibVertex( 3.5f, 10.5f, 0.0f + 0.001f ),  wayPointsEnemy1,    Bot.BotState.EStateWalkWayPoints   );
            addBot( b );
        }

        protected final void addBot( Bot botToAdd )
        {
            bots.add( botToAdd );
            Debug.bot.out( "adding bot. capacity is now [" + bots.size() + "]" );
        }

        public static final void checkChangeTo()
        {
            if ( Level.levelToChangeTo != null )
            {
                if ( levelChangeBlocker <= System.currentTimeMillis() )
                {
                    System.out.println("mh");

                    //change to if possible
                    if ( currentLevel != null && Level.levelToChangeTo == currentLevel.iConfig )
                    {
                        Debug.bugfix.out( "deny change to equal level [" + Level.levelToChangeTo + "]" );
                    }
                    else
                    {
                        Debug.bugfix.out( "change level to [" + Level.levelToChangeTo + "]" );

                        //change current level
                        currentLevel = new Level( Level.levelToChangeTo );

                        //gain cheat ammo
                        Level.currentPlayer().ammo.addAmmo( AmmoType.ERifle51mm, 99999 );  //gain 99999 ammo

                        levelChangeBlocker = System.currentTimeMillis() + 100;
                    }

                    //button needs to be press it again
                    Level.levelToChangeTo = null;
                }
            }
        }

        /**************************************************************************************
        *   Draws the level onto the screen.
        **************************************************************************************/
        public final void draw()
        {
            //draw all walls
            for ( WallCollection meshCollection : wallCollections )
            {
                meshCollection.draw();
            }
        }

        public final HitPoint launchShot( Shot shot )
        {
            //collect all hit points
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //launch the shot on all mesh-collections
            for ( WallCollection meshCollection : wallCollections )
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
                hitPoints.addAll( Level.currentPlayer().cylinder.launchShot( shot ) );
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
        public final Float getHighestFloor( Point2D.Float point )
        {
            //collect all hit points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( WallCollection meshCollection : wallCollections )
            {
                //launch the shot on all mesh-collections
                hitPointsZ.addAll( meshCollection.launchFloorCheck( point ) );
            }

            //return nearest floor
            return Level.currentPlayer().cylinder.getHighestFloor( hitPointsZ, PlayerAttributes.MAX_CLIMBING_UP_Z );
        }

        public final void launchAction( Cylinder cylinder )
        {
            //browse all mesh-collections
            for ( WallCollection meshCollection : wallCollections )
            {
                //launch the shot on this mesh-collection
                meshCollection.launchAction( cylinder );
            }
        }

        public final void animate()
        {
            //browse all mesh-collections
            for ( WallCollection meshCollection : wallCollections )
            {
                //animate all mesh-collections
                meshCollection.animate();
            }
        }

        public final void animateAllBots()
        {
            //Debug.bot.out( "animating ALL bots .."+botQueue.size()+"" );
            for ( Bot bot : bots )
            {
                if ( bot.isAlive() )
                {
                    bot.animate();
                }
                else
                {
                    bots.removeElement( bot );
                }
            }
        }

        public static Level current()
        {
            return currentLevel;
        }

        public static Player currentPlayer()
        {
            return currentLevel.iPlayer;
        }

        public final void drawAllBots()
        {
            //Debug.bot.out( "drawing ALL bots .."+botQueue.size()+"" );
            for ( Bot bot : bots )
            {
                bot.draw();
            }
        }

        public final boolean checkCollisionOnWalls( Cylinder cylinder )
        {
            //collisions may be disabled
            if ( ShooterSettings.DISABLE_COLLISIONS ) return false;

            //nextPosX
            for ( WallCollection meshCollection : wallCollections )
            {
                //launch the shot on all mesh-collections
                if ( meshCollection.checkCollision( cylinder ) ) return true;
            }

            return false;
        }

        public final void checkCollisionOnBots( Cylinder c )
        {
            for ( Bot bot : bots )
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

        public final void drawBg( ViewSet cam )
        {
            iConfig.iBg.drawOrtho( cam.rotX, cam.rotZ );
        }
    }
