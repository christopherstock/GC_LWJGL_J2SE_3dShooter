/*  $Id: Shot.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  java.awt.geom.*;
    import  java.util.*;
    import  javax.vecmath.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.ShooterSettings.DebugSettings;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   All calculations for one shot.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public class Shot
    {
        public static enum ShotType
        {
            /** a non-striking view */
            EViewOnly,
            /** a striking projectile */
            ESharpAmmo,
        }

        public static enum ShotOrigin
        {
            /** the one and only player */
            EPlayer,
            /** all friendly bots */
            EFriends,
            /** all hostile bots */
            EEnemies,
        }

        public                      ShotType            iType                       = null;
        public                      ShotOrigin          iOrigin                     = null;
        private                     LibVertex           iSrcPoint                   = null;
        public                      float               iRotZ                       = 0.0f;
        private                     float               iRotX                       = 0.0f;
        private                     float               iShotRange                  = 0.0f;

        //calculated values
        public                      Point2D.Float       iEndPointHorz               = null;
        public                      Point2D.Float       iEndPointVert               = null;

        public                      Line2D.Float        iLineShotHorz               = null;
        public                      Line2D.Float        iLineShotVert               = null;

        public                      Point2D.Float       iSrcPointHorz               = null;
        public                      Point2D.Float       iSrcPointVert               = null;

        //private                     HitPoint            iHitPoint                   = null;

        public                      LibHoleSize         iBulletHoleSize             = null;
        public                      LibDebug            iDebug                      = null;

        public                      Point3d             iSrcPoint3d                 = null;
        public                      Point3d             iEndPoint3d                 = null;

        public Shot( ShotType aType, ShotOrigin aOrigin, float irregularityHorz, float irregularityVert, float startX, float startY, float startZ, float aRotZ, float aRotX, float aShotRange, LibHoleSize aBulletHoleSize, LibDebug aDebug )
        {
            iType           = aType;
            iOrigin         = aOrigin;
            iSrcPoint       = new LibVertex( startX, startY, startZ );
            iRotZ           = aRotZ;
            iRotX           = aRotX;
            iShotRange      = aShotRange;
            iBulletHoleSize = aBulletHoleSize;
            iDebug          = aDebug;

            iDebug.out( "=======================================" );

            //let the wearpon affect the horz- and vert-fire-angles
            iRotZ           += irregularityHorz;
            iRotX           += irregularityVert;

            iSrcPoint3d     = new Point3d
            (
                startX,
                startY,
                startZ
            );
            iEndPoint3d     = new Point3d
            (
                iSrcPoint3d.x - LibMath.sinDeg( iRotZ ) * iShotRange,
                iSrcPoint3d.y - LibMath.cosDeg( iRotZ ) * iShotRange,
                iSrcPoint3d.z - LibMath.sinDeg( iRotX ) * iShotRange
            );

            //calculate end point and shot-line for the horizontal axis
            iSrcPointHorz = new Point2D.Float( iSrcPoint.x, iSrcPoint.y );
            iEndPointHorz   = new Point2D.Float
            (
                iSrcPointHorz.x - LibMath.sinDeg( iRotZ ) * iShotRange,
                iSrcPointHorz.y - LibMath.cosDeg( iRotZ ) * iShotRange
            );
            iLineShotHorz   = new Line2D.Float( iSrcPointHorz, iEndPointHorz );
            iDebug.out( "SHOT LINE HORZ [" + iSrcPoint.x + "," + iSrcPoint.y + "] to [" + iEndPointHorz.x + "," + iEndPointHorz.y + "]" );

            //calculate end point and shot-line for the vertical axis
            iSrcPointVert = new Point2D.Float( 0.0f, iSrcPoint.z );
            iEndPointVert   = new Point2D.Float
            (
                iSrcPointVert.x + LibMath.cosDeg( iRotX ) * iShotRange,
                iSrcPointVert.y - LibMath.sinDeg( iRotX ) * iShotRange
            );
            iLineShotVert   = new Line2D.Float( iSrcPointVert, iEndPointVert );
            iDebug.out( "SHOT LINE VERT " + iSrcPointVert + iEndPointVert );

            //Debug.drawLine( new Vertex( 2.0f, 1.0f, 2.0f, -1.0f, -1.0f ), new Vertex( 0.0f, 0.0f, 0.0f, -1.0f, -1.0f ) );
            //Debug.drawLine( srcPointVert.x, srcPointVert.y, endPointVert.x, endPointVert.y );
            //Debug.drawLine( new Vertex( srcPoint.x, srcPoint.y, srcPointVert.y, -1.0f, -1.0f ), new Vertex( endPointVert.x, 0.0f, 0.0f, -1.0f, -1.0f ) );
        }

        public HitPoint launch()
        {
            //collect all hit points
            Vector<HitPoint> hitPoints = new Vector<HitPoint>();

            //launch the shot on all walls
            for ( WallCollection wallCollection : GameLevel.current().getWallCollections() )
            {
                hitPoints.addAll( wallCollection.launchShot( this ) );
            }

            //launch the shot on all bots' collision unit ( if not shot by a bot! )
            if ( iOrigin != ShotOrigin.EEnemies )
            {
                for ( Bot bot : GameLevel.current().getBots() )
                {
                    hitPoints.addAll( bot.launchShot( this ) );
                }
            }

            //launch the shot on the player ( if not shot by the player )
            if ( iOrigin != ShotOrigin.EPlayer )
            {
                hitPoints.addAll( GameLevel.currentPlayer().launchShot( this ) );
            }

            //get nearest hitpoint
            HitPoint nearestHitPoint = HitPoint.getNearestHitPoint( hitPoints );

            //perform an operation if
            if
            (
                    //something has been hit
                    nearestHitPoint != null

                    //the game object is able to be hit
                &&  nearestHitPoint.carrier != null && nearestHitPoint.carrier.getHitPointCarrier() != null

                    //sharp ammo has been used
                &&  iType == ShotType.ESharpAmmo
            )
            {
                //ShooterDebug.bugfix.out( " faceangle of hit face: [" + nearestHitPoint.horzFaceAngle + "]" );

                //check the hitPoint's receiver
                switch ( nearestHitPoint.carrier.getHitPointCarrier() )
                {
                    case EWall:
                    {
                        //append bullet hole
                        if ( iBulletHoleSize != LibHoleSize.ENone )
                        {
                            //draw bullet hole
                            BulletHole.addBulletHole( nearestHitPoint );
                        }

                        //draw Sliver
                        nearestHitPoint.drawWallSliver();

                        //hurt wall
                        Wall w = (Wall)nearestHitPoint.carrier;
                        w.hurt( 20 );

                        break;
                    }

                    case EPlayer:
                    {
                        //player loses health
                        GameLevel.currentPlayer().hurt( LibMath.getRandom( 2, 10 ) );
                        break;
                    }

                    case EBot:
                    {
                        //append bullet hole
                        if ( iBulletHoleSize != LibHoleSize.ENone )
                        {
                            //do NOT create bullet holes on bots!
                            //BulletHole.addBulletHole( nearestHitPoint );

                        }

                        //hurt bot
                        Bot b = (Bot)nearestHitPoint.carrier;
                        b.hurt( 20 );

                        //draw Sliver
                        nearestHitPoint.drawWallSliver();

                        break;
                    }
                }

                //show debugs
                iDebug.out( "hit point: [" + nearestHitPoint.vertex.x + "," + nearestHitPoint.vertex.y + ", " + nearestHitPoint.vertex.z + "]" );
                iDebug.out( "shotAngle [" + nearestHitPoint.horzShotAngle + "] SliverAngle [" + nearestHitPoint.horzSliverAngle + "] invertedShotAngle [" + nearestHitPoint.horzInvertedShotAngle + "] faceAngle [" + nearestHitPoint.horzFaceAngle + "]" );
            }

            iDebug.out( "=====================================================================\n" );

            return nearestHitPoint;
        }

        public final void drawShotLine()
        {
            //draw shot line with own calculations
            for ( float distance = 0.0f; distance < iShotRange; distance += 0.01f )
            {
                FX.launchDebugPoint
                (
                    new LibVertex
                    (
                        iSrcPoint.x - LibMath.sinDeg( iRotZ ) * distance,
                        iSrcPoint.y - LibMath.cosDeg( iRotZ ) * distance,
                        iSrcPoint.z - LibMath.sinDeg( iRotX ) * distance
                    ),
                    LibColors.EYellow,
                    50,
                    DebugSettings.DEBUG_POINT_SIZE
                );
            }
        }
    }
