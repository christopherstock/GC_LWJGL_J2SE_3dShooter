/*  $Id: ShooterGameLevel.java 725 2011-05-12 20:12:58Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  java.util.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.base.ShooterItem.*;
    import  de.christopherstock.shooter.base.ShooterLevels.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   Represents the level-system. Just a few values to init all levels.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
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
        public                      LibColors                   iBgColor                = null;

        /**************************************************************************************
        *   This level's player-instance being controlled by the user.
        **************************************************************************************/
        private                     Player                      iPlayer                 = null;

        public static enum InvisibleZeroLayerZ
        {
            ENo,
            EYes,
            ;
        }

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

            //create and add all bots
            for ( ShooterBotFactory b : iConfig.iStartupBots )
            {
                //init / reset bot
                addBot( b.createBot() );
            }
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

                        new HUDMessage( "Changing to level [" + ( ShooterGameLevel.levelToChangeTo.ordinal() + 1 ) + "][" + ShooterGameLevel.levelToChangeTo.iDesc + "]" ).show();

                        //change current level
                        currentLevel = new ShooterGameLevel();
                        currentLevel.init();
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
            if ( iConfig.iBg != null ) iConfig.iBg.drawOrtho( cam.rot.x, cam.rot.z );
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

        public final LibColors getBackgroundColor()
        {
            return iConfig.iBgCol;
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

        public final boolean hasInvisibleZLayer()
        {
            return ( iConfig.iHasInvisibleZLayer == InvisibleZeroLayerZ.EYes );
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
