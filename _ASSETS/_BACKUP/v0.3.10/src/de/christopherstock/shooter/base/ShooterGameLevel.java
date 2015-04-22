/*  $Id: ShooterGameLevel.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  java.awt.geom.Point2D;
    import  java.util.*;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.fx.LibFX.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.game.*;
    import  de.christopherstock.lib.game.LibShot.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.FxSettings;
    import  de.christopherstock.shooter.ShooterSettings.General;
    import  de.christopherstock.shooter.base.ShooterD3ds.D3dsFile;
    import  de.christopherstock.shooter.base.ShooterLevels.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.g3d.wall.Wall;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.ui.hud.*;
    import  de.christopherstock.shooter.ui.hud.HUD.ChangeAction;

    /**************************************************************************************
    *   Represents the level-system. Just a few values to init all levels.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class ShooterGameLevel
    {
        private     static          ShooterGameLevel[]          levels                  = null;
        private     static          ShooterGameLevel            currentLevel            = null;

        /**************************************************************************************
        *   The global player-instance being controlled by the user.
        **************************************************************************************/
        private     static          Player                      player                 = null;

        /**************************************************************************************
        *   Global bullet holes are a must! And the end of a long-lasting fix.
        **************************************************************************************/
        public      static          Vector<BulletHole>          iBulletHoles            = new Vector<BulletHole>();

        private     static          LevelConfig                 levelToChangeTo         = null;
        private     static          boolean                     resetOnLevelChange      = false;
        public      static          long                        levelChangeBlocker      = 0;

        public                      LevelConfig                 iConfig                 = null;
        private                     Vector<Bot>                 iBots                   = null;
        public                      Vector<ItemToPickUp>        iItems                  = null;
        private                     WallCollection[]            iWallCollections        = null;

        public                      int                         adrenalineTicks         = 0;
        private                     int                         adrenalineDelayLevel    = 0;
        private                     int                         adrenalineDelayPlayer   = 0;

        public static enum InvisibleZeroLayerZ
        {
            ENo,
            EYes,
            ;
        }

        private ShooterGameLevel( LevelConfig aConfig )
        {
            //keep target level as own config
            iConfig = aConfig;
        }

        public static final void init()
        {
            //create player
            player = new Player
            (
                LevelConfig.values()[ 0 ].iStartPosition,
                General.DISABLE_GRAVITY
            );

            //init all game levels
            levels = new ShooterGameLevel[ LevelConfig.values().length ];
            for ( int i = 0; i < LevelConfig.values().length; ++i )
            {
                levels[ i ] = new ShooterGameLevel( LevelConfig.values()[ i ] );
                levels[ i ].initBasics();
            }
        }

        private final void initBasics()
        {
            //assign walls
            iWallCollections = ShooterLevels.getLevelWalls( iConfig );

            //reset level
            reset();
        }

        public void reset()
        {
            //recreate player
            player = new Player
            (
                LevelConfig.values()[ 0 ].iStartPosition,
                General.DISABLE_GRAVITY
            );

            //remove all bullet holes
            clearBulletHoles();

            //handle startup items and wearpons to the player
            for ( ItemEvent i : iConfig.iStartupItems )
            {
                i.perform( null );
            }
            for ( ArtefactType w : iConfig.iStartupWearpons )
            {
                Artefact toDeliver = new Artefact( w );
                player.iArtefactSet.deliverArtefact( toDeliver );

                //reload if firearm
                if ( w.isFireArm() )
                {
                    //ShooterDebug.bugfix.out( "reload initial wearpon" );
                    toDeliver.reload( player.iAmmoSet, false, false, null );
                }
            }

            //change to 1st artefact
            player.orderWearponOrGadget( ChangeAction.EActionNext );

            //spawn specified items
            iItems   = new Vector<ItemToPickUp>();
            if ( iConfig.iItems != null )
            {
                for ( ItemToPickUp aItem : iConfig.iItems )
                {
                    //load item's d3ds and add it to the stack
                    aItem.loadD3ds();
                    iItems.add( aItem );
                }
            }

            //create and add all bots
            iBots    = new Vector<Bot>();
            for ( ShooterBotFactory b : iConfig.iStartupBots )
            {
                //init / reset bot
                Bot botToAdd = b.createBot();
                addBot( botToAdd );
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

                        HUDMessageManager.getSingleton().showMessage( "Changing to level [" + ( ShooterGameLevel.levelToChangeTo.ordinal() + 1 ) + "][" + ShooterGameLevel.levelToChangeTo.iDesc + "]" );

                        //change current level - do NOT change to constructor! level is referenced in init() !
                        currentLevel = levels[ levelToChangeTo.ordinal() ];

                        //reset new level if desired
                        if ( resetOnLevelChange )
                        {
                            currentLevel.reset();
                        }
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

        public final void launchAction( Cylinder cylinder, Gadget gadget, float faceAngle )
        {
            //launch action on all mesh collections
            for ( WallCollection meshCollection : iWallCollections )
            {
                //launch the shot on this mesh-collection
                meshCollection.launchAction( cylinder, gadget, faceAngle );
            }

            //launch action on all bots
            for ( Bot b : iBots )
            {
                b.launchAction( cylinder, gadget, faceAngle );
            }
        }

        public final LibHitPoint[] launchShot( LibShot s )
        {
            //collect all hit points
            Vector<LibHitPoint> allHitPoints = new Vector<LibHitPoint>();

            //launch the shot on all walls
            for ( WallCollection wallCollection : iWallCollections )
            {
                allHitPoints.addAll( wallCollection.launchShot( s ) );
            }

            //launch the shot on all bots' collision unit ( if not shot by a bot! )
            if ( s.iOrigin != ShotOrigin.EEnemies )
            {
                for ( Bot bot : getBots() )
                {
                    allHitPoints.addAll( bot.launchShot( s ) );
                }
            }

            //launch the shot on the player ( if not shot by the player )
            if ( s.iOrigin != ShotOrigin.EPlayer )
            {
                allHitPoints.addAll( player.launchShot( s ) );
            }

            //get all affected hitpoints
            LibHitPoint[] affectedHitPoints = null;

            //check if wall-breaking ammo has been used!
            if ( s.iWallBreakingAmmo )
            {
                //all hitpoints are afftected
                affectedHitPoints = allHitPoints.toArray( new LibHitPoint[] {} );
            }
            else
            {
                //get nearest hitpoints ( considering penetrable walls )
                affectedHitPoints = LibHitPoint.getAffectedHitPoints( allHitPoints );
            }

            //draw nearest hp in 3d
            //if ( nearestHitPoint != null ) LibFXManager.launchStaticPoint( nearestHitPoint.iVertex, LibColors.EWhite, 0.05f, 300 );

            //hurt all game objects only once
            Vector<LibGameObject> hitObjects = new Vector<LibGameObject>();

            //browse all afftected hitpoints
            for ( int i = 0; i < affectedHitPoints.length; ++i )
            {
                LibHitPoint affectedHitPoint = affectedHitPoints[ i ];

                //perform an operation if
                if
                (
                        //the game object is able to be hit
                        affectedHitPoint.iCarrier != null && affectedHitPoint.iCarrier.getHitPointCarrier() != null

                        //sharp ammo has been used
                    &&  s.iType == ShotType.ESharpAmmo
                )
                {
                    //ShooterDebug.bugfix.out( " faceangle of hit face: [" + nearestHitPoint.horzFaceAngle + "]" );

                    //check the hitPoint's receiver
                    switch ( affectedHitPoint.iCarrier.getHitPointCarrier() )
                    {
                        case EWall:
                        {
                            Wall w = (Wall)affectedHitPoint.iCarrier;
                            boolean drawBulletHoleAndPlaySound = true;

                            //check if this is a projectile
                            if ( s.iProjectile != null )
                            {
                                //no bullet hole for last point
                                if ( i == affectedHitPoints.length - 1 )
                                {
                                    drawBulletHoleAndPlaySound  = false;

                                    //append projectile for last point
                                    addBulletHole( affectedHitPoint, s.iProjectile );
                                }
                            }

                            //append bullet hole
                            if ( drawBulletHoleAndPlaySound && s.iBulletHoleSize != LibHoleSize.ENone )
                            {
                                //draw bullet hole
                                addBulletHole( affectedHitPoint, null );
                            }

                            //only once per wall
                            if ( !hitObjects.contains( w ) )
                            {
                                hitObjects.add( w );

                                //hurt wall ( not for projectiles
                                if ( s.iProjectile == null )
                                {
                                    w.hurt( s.iDamage, affectedHitPoint.iHorzInvertedShotAngle );
                                }

                                //draw sliver
                                affectedHitPoint.launchWallSliver( s.iParticleQuantity, s.iSliverAngleMod, FxSettings.LIFETIME_SLIVER, s.iSliverSize, FXGravity.ENormal );

                                //play wall sound ( not for target wall ! )
                                if ( affectedHitPoint.iWallTexture != null && drawBulletHoleAndPlaySound )
                                {
                                    affectedHitPoint.iWallTexture.getMaterial().iBulletSound.playDistancedFx( new Point2D.Float( affectedHitPoint.iVertex.x, affectedHitPoint.iVertex.y ) );
                                }
                            }
                            break;
                        }

                        case EPlayer:
                        {
                            //only once
                            if ( !hitObjects.contains( player ) )
                            {
                                hitObjects.add( player );

                                //player loses health
                                player.hurt( LibMath.getRandom( 1, 1 ) );

                                //draw sliver
                                affectedHitPoint.launchWallSliver( s.iParticleQuantity, s.iSliverAngleMod, FxSettings.LIFETIME_SLIVER, s.iSliverSize, FXGravity.ENormal );
                            }
                            break;
                        }

                        case EBot:
                        {
                            //only once per bot
                            Bot b       = (Bot)affectedHitPoint.iCarrier;
                            if ( !hitObjects.contains( b ) )
                            {
                                hitObjects.add( b );

                                if ( s.iProjectile == null )
                                {
                                    //hurt bot
                                    int damage  = ( affectedHitPoint.iDamageMultiplier == -1 ? b.getHealth() : (int)( s.iDamage * affectedHitPoint.iDamageMultiplier ) );
                                    ShooterDebug.bot.out( "damage is " + damage );
                                    b.hurt( damage );

                                    //draw blood
                                    affectedHitPoint.launchWallSliver( s.iParticleQuantity, s.iSliverAngleMod, FxSettings.LIFETIME_BLOOD, FXSize.ELarge, FXGravity.ELow );
                                }
                                else
                                {
                                    //player falls asleep
                                    b.fallAsleep();
                                }

                                //play hit sound?
                                //ShooterTexture.getBulletSoundForMaterial( nearestHitPoint.iTexture.getMaterial() ).playDistancedFx( new Point2D.Float( nearestHitPoint.iVertex.x, nearestHitPoint.iVertex.y ) );
                            }
                            break;
                        }
                    }
                }

                //show debugs
              //s.iDebug.out( "hit point: [" + nearestHitPoint.iVertex.x + "," + nearestHitPoint.iVertex.y + ", " + nearestHitPoint.iVertex.z + "]" );
              //s.iDebug.out( "shotAngle [" + nearestHitPoint.iHorzShotAngle + "] SliverAngle [" + nearestHitPoint.iHorzSliverAngle + "] invertedShotAngle [" + nearestHitPoint.iHorzInvertedShotAngle + "] faceAngle [" + nearestHitPoint.iHorzFaceAngle + "]" );
            }

            //s.iDebug.out( "=====================================================================\n" );

            return affectedHitPoints;
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
                if ( iBots.elementAt( i ).isDead() )
                {
                    //decrease disappear timer
                    --iBots.elementAt( i ).iDisappearTimer;

                    if ( iBots.elementAt( i ).iDisappearTimer <= General.FADE_OUT_FACES_TOTAL_TICKS )
                    {
                        iBots.elementAt( i ).fadeOutAllFaces();
                    }

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
            return player;
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

        public static final void orderLevelChange( LevelConfig config, boolean reset )
        {
            levelToChangeTo    = config;
            resetOnLevelChange = reset;
        }

        public final void drawAllItems()
        {
            //Debug.item.out( "drawing ALL items .."+itemQueue.size()+"" );
            for ( ItemToPickUp item : iItems )
            {
                item.draw();
            }
        }

        private final void animateItems()
        {
            //browse reversed
            for ( int j = iItems.size() - 1; j >= 0; --j )
            {
                //check if item is collected
                if ( iItems.elementAt( j ).shallBeRemoved() )
                {
                    //remove collected items
                    iItems.removeElementAt( j );
                }
                else
                {
                    //check collisions on non-collected items
                    iItems.elementAt( j ).animate();
                }
            }
        }

        public final Vector<Bot> getBots()
        {
            return iBots;
        }

        public final LibColors getBackgroundColor()
        {
            return iConfig.iBgCol;
        }

        public final void onRun()
        {
            boolean runPlayer = false;
            boolean runLevel  = false;

            //check player and level animation
            if ( adrenalineTicks-- > 0 )
            {
                //check player animation
                if ( adrenalineDelayPlayer > 0 )
                {
                    --adrenalineDelayPlayer;
                }
                else
                {
                    //animate level and restart delay
                    adrenalineDelayPlayer = 2;

                    //animate player
                    runPlayer = true;
                }

                //check level animation
                if ( adrenalineDelayLevel > 0 )
                {
                    --adrenalineDelayLevel;
                }
                else
                {
                    //animate level and restart delay
                    adrenalineDelayLevel = 10;

                    //animate level
                    runLevel = true;
                }
            }
            else
            {
                //animate player and level
                runPlayer = true;
                runLevel = true;
            }

            //run player
            if ( runPlayer )
            {
                player.onRun();
            }

            //run level
            if ( runLevel )
            {
                //animate all walls
                animateWalls();

                //animate all bots
                animateBots();

                //check if player picked up an item
                animateItems();

                //animate particle systems and HUD
                LibFXManager.onRun();
                Shooter.mainThread.iHUD.onRun();
            }
        }

        public final boolean hasInvisibleZLayer()
        {
            return ( iConfig.iHasInvisibleZLayer == InvisibleZeroLayerZ.EYes );
        }

        public final void startAdrenaline()
        {
            adrenalineTicks = ShooterSettings.General.TICKS_ADRENALINE;
            HUDFx.startAdrenalineFx();
        }

        public final Bot getBotByID( int id )
        {
            for ( Bot bot : iBots )
            {
                if ( bot.iID == id )
                {
                    return bot;
                }
            }

            return null;
        }

        public final void addBulletHole( LibHitPoint hitPoint, D3dsFile aProjectile )
        {
            //add to bullet-hole-stack, prune stack if overflowing
            iBulletHoles.add( new BulletHole( hitPoint, aProjectile ) );
            if ( iBulletHoles.size() > ShooterSettings.Performance.MAX_NUMBER_BULLET_HOLES ) iBulletHoles.removeElementAt( 0 );
        }

        public final void clearBulletHoles()
        {
            iBulletHoles.removeAllElements();
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
