/*  $Id: GameLevel.java 676 2011-04-26 22:33:25Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.ShooterD3dsFiles.Bots;
    import  de.christopherstock.shooter.ShooterItem.ItemEvent;
    import  de.christopherstock.shooter.ShooterLevels.LevelConfig;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.objects.Bot.BotJob;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   Represents the level-system. Just a few values to init all levels.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public class ShooterGameLevel
    {
        //values for the current level
        private     static          ShooterGameLevel            currentLevel            = null;
        private     static          LevelConfig                 levelToChangeTo         = null;
        public      static          long                        levelChangeBlocker      = 0;

        private                     LevelConfig                 iConfig                 = null;
        private                     WallCollection[]            iWallCollections        = null;
        public                      Vector<Bot>                 iBots                   = null;
        public                      Vector<ShooterItem>         iItems                  = null;

        /**************************************************************************************
        *   This level's player-instance being controlled by the user.
        **************************************************************************************/
        private                     Player                      iPlayer                 = null;

        private ShooterGameLevel()
        {
            //keep target level as own config
            iConfig = levelToChangeTo;

            //create player
            iPlayer = new Player
            (
                iConfig.iStartPosition,
                General.DISABLE_GRAVITY
            );
        }

        private final void init()
        {
            iBots    = new Vector<Bot>();
            iItems   = new Vector<ShooterItem>();

            //assign walls and remove all bullet holes
            iWallCollections = ShooterLevels.getLevelWalls( levelToChangeTo );

            //remove all bullet holes
            BulletHole.removeAll();

            //handle startup items and wearpons to the player
            for ( ItemEvent i : iConfig.iStartupItems )
            {
                i.perform();
            }
            for ( Artefact w : iConfig.iStartupWearpons )
            {
                iPlayer.deliverWearpon( w );
                w.reload( false );
            }

            //spawn some items
            if ( iConfig.iItems != null )
            {
                for ( ShooterItem aItem : iConfig.iItems )
                {
                    //load item's d3ds and add it to the stack
                    aItem.loadD3ds();
                    iItems.add( aItem );
                }
            }

            //boolean a = true;
            //if ( a ) return;

            final Point2D.Float[] wayPointsEnemy1 = new Point2D.Float[]
            {
                new Point2D.Float( 56.5f,  60.0f     ),
                new Point2D.Float( 56.5f,  50.0f     ),
            };


//          Bot b0 = new Bot( Bot.BotType.ETypeFriend, new LibVertex( 6.5f, 7.0f,  0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ELeadPlayerToLastWaypoint );
//          Bot b1 = new Bot( Bot.BotType.ETypeFriend, new LibVertex( 8.0f, 10.0f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep );

            Bot b0 = new Bot(  ShooterBotTemplate.ESecurityMale,        Bot.BotType.ETypeFriend, new LibVertex( 57.5f, 56.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.EWalkWaypoints,    Bots.EItemPistol,     null                );
/*
            Bot b1 = new Bot(  ShooterBotTemplate.EPilotFemale,         Bot.BotType.ETypeFriend, new LibVertex( 106.0f, 106.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep,                       null,                 null                );
            Bot b2 = new Bot(  ShooterBotTemplate.ESecurityFemale,      Bot.BotType.ETypeFriend, new LibVertex( 106.5f, 106.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep,                       null,                 null                );
            Bot b3 = new Bot(  ShooterBotTemplate.EPilotMale,           Bot.BotType.ETypeFriend, new LibVertex( 108.0f, 106.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep,                       null,                 Bots.EItemShotgun   );
            Bot b4 = new Bot(  ShooterBotTemplate.ESpecialForcesMale,   Bot.BotType.ETypeFriend, new LibVertex( 105.5f, 106.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep,                       Bots.EItemShotgun,    null                );
*/

/*
            Bot b5 = new Bot(  ShooterBotTemplate.ESecurityMale,        Bot.BotType.ETypeFriend, new LibVertex( 7.5f, 7.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ELeadPlayerToLastWaypoint,    Bots.EItemPistol,     null                );
            Bot b6 = new Bot(  ShooterBotTemplate.EPilotFemale,         Bot.BotType.ETypeFriend, new LibVertex( 6.0f, 7.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep,                       null,                 null                );
            Bot b7 = new Bot(  ShooterBotTemplate.ESecurityFemale,      Bot.BotType.ETypeFriend, new LibVertex( 6.5f, 7.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep,                       null,                 null                );
            Bot b8 = new Bot(  ShooterBotTemplate.EPilotMale,           Bot.BotType.ETypeFriend, new LibVertex( 8.0f, 7.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep,                       null,                 Bots.EItemShotgun   );
            Bot b9 = new Bot(  ShooterBotTemplate.ESpecialForcesMale,   Bot.BotType.ETypeFriend, new LibVertex( 5.5f, 7.5f, 0.0f + 0.001f ),  wayPointsEnemy1, BotJob.ESleep,                       Bots.EItemShotgun,    null                );
*/
            //b1.iFacingAngle = 0.0f;

            addBot( b0 );
/*
            addBot( b1 );

            addBot( b2 );
            addBot( b3 );
            addBot( b4 );
*/
/*
            addBot( b5 );
            addBot( b6 );
            addBot( b7 );
            addBot( b8 );
            addBot( b9 );
*/
        }

        protected final void addBot( Bot botToAdd )
        {
            iBots.add( botToAdd );
            ShooterDebug.bot.out( "adding bot. capacity is now [" + iBots.size() + "]" );
        }

        public static final void checkChangeTo()
        {
            if ( ShooterGameLevel.levelToChangeTo != null )
            {
                if ( levelChangeBlocker <= System.currentTimeMillis() )
                {
                    //change to if possible
                    if ( currentLevel != null && ShooterGameLevel.levelToChangeTo == currentLevel.iConfig )
                    {
                        ShooterDebug.levels.out( "deny change to equal level [" + ShooterGameLevel.levelToChangeTo + "]" );
                    }
                    else
                    {
                        ShooterDebug.levels.out( "change level to [" + ShooterGameLevel.levelToChangeTo + "]" );

                        new HUDMessage( "Push ENTER to get some hints" ).show();

                        //change current level
                        currentLevel = new ShooterGameLevel();
                        currentLevel.init();
/*
                        //give player some guns and ammo
                        currentPlayer().deliverWearpon( Wearpon.EPistol );
                        currentPlayer().deliverWearpon( Wearpon.EMagnum357 );
                        currentPlayer().deliverWearpon( Wearpon.ESpaz12 );
                        currentPlayer().deliverWearpon( Wearpon.EWaltherPPK );
                        currentPlayer().deliverWearpon( Wearpon.ERCP180 );
                        currentPlayer().deliverWearpon( Wearpon.EHuntingRifle );
                        currentPlayer().deliverWearpon( Wearpon.EAutomaticShotgun );
                        currentPlayer().deliverWearpon( Wearpon.ESniperRifle );

                        //currentPlayer().deliverGadget( Gadget.EMobilePhoneSEW890i );
                        //currentPlayer().deliverGadget( Gadget.EBottleVolvic );
                        currentPlayer().getAmmo().addAmmo( AmmoType.EShotgunShells,     99999       );
                        currentPlayer().getAmmo().addAmmo( AmmoType.EBullet9mm,         99999       );
                        currentPlayer().getAmmo().addAmmo( AmmoType.ERifle51mm,         99999       );
                        currentPlayer().getAmmo().addAmmo( AmmoType.ERifle792mm,        99999       );
                        currentPlayer().getAmmo().addAmmo( AmmoType.EMagnumBullet357,   99999       );
*/
                        levelChangeBlocker = System.currentTimeMillis() + 100;
                    }

                    //set no active level change
                    ShooterGameLevel.levelToChangeTo = null;
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

        public final Float getHighestFloor( Cylinder cylinder )
        {
            //collect all hit points
            Vector<Float> hitPointsZ = new Vector<Float>();
            for ( WallCollection meshCollection : iWallCollections )
            {
                //launch the shot on all mesh-collections
                hitPointsZ.addAll( meshCollection.checkCollisionVert( cylinder ) );
            }
            Float ret = cylinder.getHighestOfCheckedFloor( hitPointsZ );

            //return nearest floor
            return ret;
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

        private final void animateWalls()
        {
            //browse all mesh-collections
            for ( WallCollection meshCollection : iWallCollections )
            {
                //animate all mesh-collections
                meshCollection.animate();
            }
        }

        private final void animateBots()
        {
            for ( int i = iBots.size() - 1; i >= 0; --i )
            {
                //animate bot
                iBots.elementAt( i ).animate();

                //prune if disappearing
                if ( iBots.elementAt( i ).isDying() )
                {
                    //decreade disappear timer
                    --iBots.elementAt( i ).iDisappearTimer;
                    if ( iBots.elementAt( i ).iDisappearTimer <= 0 )
                    {
                        iBots.removeElementAt( i );
                    }
                }
            }
        }

        public static ShooterGameLevel current()
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
                //launch the collision on all mesh-collections
                if ( meshCollection.checkCollisionHorz( cylinder ) ) return true;
            }

            return false;
        }

        public final boolean checkCollisionOnBots( Cylinder cylinder )
        {
            //browse all bots
            for ( Bot bot : iBots )
            {
                //launch the cylinder on all mesh-collections
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
            for ( ShooterItem item : iItems )
            {
                item.draw();
            }
        }

        private final void checkItemCollisions()
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

        public final void animate()
        {
            //animate all walls
            animateWalls();

            //animate all bots
            animateBots();

            //check player-item-collisions
            checkItemCollisions();
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
/*
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
            return GameLevel.currentPlayer().getCylinder().getHighestOfCheckedFloor( hitPointsZ, PlayerAttributes.MAX_CLIMBING_UP_Z );
        }
*/
    }
