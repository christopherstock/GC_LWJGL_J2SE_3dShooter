/*  $Id: Level.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.ShooterLevels.LevelConfig;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.ShooterSettings.PlayerAttributes;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.objects.Bot.BotJob;
    import  de.christopherstock.shooter.ui.hud.Ammo.AmmoType;
import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   Represents the level-system. Just a few values to init all levels.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class Level
    {
        //values for the current level
        private     static          Level                       currentLevel            = null;
        private     static          LevelConfig                 levelToChangeTo         = null;
        public      static          long                        levelChangeBlocker      = 0;

        private                     LevelConfig                 iConfig                 = null;
        private                     WallCollection[]            iWallCollections        = null;
        public                      Vector<Bot>                 iBots                   = null;
        public                      Vector<Item>                iItems                  = null;

        /**************************************************************************************
        *   This level's player-instance being controlled by the user.
        **************************************************************************************/
        private                     Player                      iPlayer                 = null;

        private Level( LevelConfig aConfig )
        {
            iConfig = aConfig;
            iPlayer = new Player
            (
                iConfig.iStartPosition,
                General.DISABLE_GRAVITY
            );

            iBots    = new Vector<Bot>();
            iItems   = new Vector<Item>();

            //assign walls and remove all bullet holes
            iWallCollections = ShooterLevels.getLevelWalls( aConfig );

            //remove all bullet holes
            BulletHole.removeAll();

            //spawn some items
            if ( iConfig.iItems != null )
            {
                for ( Item aItem : iConfig.iItems )
                {
                    //load item's d3ds and add it to the stack
                    aItem.loadD3ds();
                    iItems.add( aItem );
                }
            }

            final Point2D.Float[] wayPointsEnemy1 = new Point2D.Float[]
            {
                new Point2D.Float( 6.5f,  40.0f     ),
                new Point2D.Float( 6.5f,  0.0f     ),
            };

//          Bot b0 = new Bot( Bot.BotType.ETypeFriend, new LibVertex( 6.5f, 7.0f,  0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ELeadPlayerToLastWaypoint );
//          Bot b1 = new Bot( Bot.BotType.ETypeFriend, new LibVertex( 8.0f, 10.0f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep );

            Bot b0 = new Bot(  ShooterBotTemplate.ESecurityMale,        Bot.BotType.ETypeFriend, new LibVertex( 7.5f, 6.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep, Bots.EItemPistol,     null                );
            Bot b1 = new Bot(  ShooterBotTemplate.EPilotFemale,         Bot.BotType.ETypeFriend, new LibVertex( 6.0f, 6.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep, null,                 null                );
            Bot b2 = new Bot(  ShooterBotTemplate.ESecurityFemale,      Bot.BotType.ETypeFriend, new LibVertex( 6.5f, 6.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep, null,                 null                );
            Bot b3 = new Bot(  ShooterBotTemplate.EPilotMale,           Bot.BotType.ETypeFriend, new LibVertex( 8.0f, 6.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep, null,                 Bots.EItemShotgun   );
            Bot b4 = new Bot(  ShooterBotTemplate.ESpecialForcesMale,   Bot.BotType.ETypeFriend, new LibVertex( 5.5f, 6.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep, Bots.EItemShotgun,    null                );

            //b1.iFacingAngle = 0.0f;

            addBot( b0 );
            addBot( b1 );
            addBot( b2 );
            addBot( b3 );
            addBot( b4 );

            //give player some guns
            iPlayer.deliverWearpon( Wearpon.ERCP180 );
            iPlayer.deliverWearpon( Wearpon.EAssaultRifle );
            iPlayer.deliverGadget( Gadget.EMobilePhoneSEW890i );
            iPlayer.deliverGadget( Gadget.EBottleVolvic );
        }

        protected final void addBot( Bot botToAdd )
        {
            iBots.add( botToAdd );
            ShooterDebug.bot.out( "adding bot. capacity is now [" + iBots.size() + "]" );
        }

        public static final void checkChangeTo()
        {
            if ( Level.levelToChangeTo != null )
            {
                if ( levelChangeBlocker <= System.currentTimeMillis() )
                {
                    //change to if possible
                    if ( currentLevel != null && Level.levelToChangeTo == currentLevel.iConfig )
                    {
                        ShooterDebug.levels.out( "deny change to equal level [" + Level.levelToChangeTo + "]" );
                    }
                    else
                    {
                        ShooterDebug.levels.out( "change level to [" + Level.levelToChangeTo + "]" );

                        new HUDMessage( "Push ENTER to get some hints" ).show();

                        //change current level
                        currentLevel = new Level( Level.levelToChangeTo );

                        //gain cheat ammo
                        Level.currentPlayer().getAmmo().addAmmo( AmmoType.ERifle51mm, 99999 );  //gain 99999 ammo

                        levelChangeBlocker = System.currentTimeMillis() + 100;
                    }

                    //set no active level change
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
            for ( WallCollection meshCollection : iWallCollections )
            {
                meshCollection.draw();
            }
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
                hitPointsZ.addAll( meshCollection.checkCollision( point ) );
            }

            //return nearest floor
            return Player.user.cylinder.getNearestFloor( hitPointsZ, Player.MAX_CLIMBING_UP_Z );
        }
*/
        public final Float getHighestFloor( Point2D.Float point )
        {
            //collect all hit points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( WallCollection meshCollection : iWallCollections )
            {
                //launch the shot on all mesh-collections
                hitPointsZ.addAll( meshCollection.checkCollision( point ) );
            }

            //return nearest floor
            return Level.currentPlayer().getCylinder().getHighestFloor( hitPointsZ, PlayerAttributes.MAX_CLIMBING_UP_Z );
        }

        public final void launchAction( Cylinder cylinder )
        {
            //browse all mesh-collections
            for ( WallCollection meshCollection : iWallCollections )
            {
                //launch the shot on this mesh-collection
                meshCollection.launchAction( cylinder );
            }
        }

        public final void animateWalls()
        {
            //browse all mesh-collections
            for ( WallCollection meshCollection : iWallCollections )
            {
                //animate all mesh-collections
                meshCollection.animate();
            }
        }

        public final void animateBots()
        {
            //Debug.bot.out( "animating ALL bots .."+botQueue.size()+"" );
            for ( Bot bot : iBots )
            {
                if ( bot.isAlive() )
                {
                    bot.animate();
                }
                else
                {
                    iBots.removeElement( bot );
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
            for ( Bot bot : iBots )
            {
                bot.draw();
            }
        }

        public final boolean checkCollisionOnWalls( Cylinder cylinder )
        {
            //browse all mesh collections
            for ( WallCollection meshCollection : iWallCollections )
            {
                //launch the shot on all mesh-collections
                if ( meshCollection.checkCollision( cylinder ) ) return true;
            }

            return false;
        }

        public final boolean checkCollisionOnBots( Cylinder cylinder )
        {
            //browse all bots
            for ( Bot bot : iBots )
            {
                //launch the shot on all mesh-collections
                if ( bot.checkCollision( cylinder ) ) return true;
            }

            return false;
        }

        public final void drawBg( ViewSet cam )
        {
            iConfig.iBg.drawOrtho( cam.rot.x, cam.rot.z );
        }

        public static final void orderLevelChange( LevelConfig config )
        {
            levelToChangeTo = config;
        }

        public final void drawAllItems()
        {
            //Debug.item.out( "drawing ALL items .."+itemQueue.size()+"" );
            for ( Item item : iItems )
            {
                item.draw();
            }
        }

        public final void checkItemCollisions()
        {
            //browse reversed
            for ( int j = iItems.size() - 1; j >= 0; --j )
            {
                if ( iItems.elementAt( j ).isCollected() )
                {
                    //remove collected items
                    iItems.removeElementAt( j );
                }
                else
                {
                    //check collisions on non-collected items
                    iItems.elementAt( j ).checkCollision();
                }
            }
        }

        public final WallCollection[] getWallCollections()
        {
            return iWallCollections;
        }

        public final Vector<Bot> getBots()
        {
            return iBots;
        }
    }
