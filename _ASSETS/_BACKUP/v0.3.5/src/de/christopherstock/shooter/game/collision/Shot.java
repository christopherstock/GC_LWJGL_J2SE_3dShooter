/*  $Id: Shot.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
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
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.fx.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   All calculations for one shot.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
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

        public                      Point3d             iSrcPoint3d                 = null;
        public                      Point3d             iEndPoint3d                 = null;

        public                      Point2D.Float       iSrcPointHorz               = null;
        public                      Point2D.Float       iSrcPointVert               = null;

        public                      Line2D.Float        iLineShotHorz               = null;
        public                      Line2D.Float        iLineShotVert               = null;

        public                      float               iRotZ                       = 0.0f;
        private                     float               iRotX                       = 0.0f;
        private                     float               iShotRange                  = 0.0f;

        public                      Point2D.Float       iEndPointHorz               = null;
        public                      Point2D.Float       iEndPointVert               = null;

        public                      LibHoleSize         iBulletHoleSize             = null;
        public                      LibDebug            iDebug                      = null;

        public Shot( ShotType aType, ShotOrigin aOrigin, float irregularityHorz, float irregularityVert, float startX, float startY, float startZ, float aRotZ, float aRotX, float aShotRange, LibHoleSize aBulletHoleSize, LibDebug aDebug )
        {
            iType           = aType;
            iOrigin         = aOrigin;
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
            iSrcPointHorz = new Point2D.Float( (float)iSrcPoint3d.x, (float)iSrcPoint3d.y );
            iEndPointHorz   = new Point2D.Float
            (
                iSrcPointHorz.x - LibMath.sinDeg( iRotZ ) * iShotRange,
                iSrcPointHorz.y - LibMath.cosDeg( iRotZ ) * iShotRange
            );
            iLineShotHorz   = new Line2D.Float( iSrcPointHorz, iEndPointHorz );
            iDebug.out( "SHOT LINE HORZ [" + iSrcPoint3d.x + "," + iSrcPoint3d.y + "] to [" + iEndPointHorz.x + "," + iEndPointHorz.y + "]" );

            //calculate end point and shot-line for the vertical axis
            iSrcPointVert = new Point2D.Float( 0.0f, (float)iSrcPoint3d.z );
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
            for ( WallCollection wallCollection : ShooterGameLevel.current().getWallCollections() )
            {
                hitPoints.addAll( wallCollection.launchShot( this ) );
            }

            //launch the shot on all bots' collision unit ( if not shot by a bot! )
            if ( iOrigin != ShotOrigin.EEnemies )
            {
                for ( Bot bot : ShooterGameLevel.current().getBots() )
                {
                    hitPoints.addAll( bot.launchShot( this ) );
                }
            }

            //launch the shot on the player ( if not shot by the player )
            if ( iOrigin != ShotOrigin.EPlayer )
            {
                hitPoints.addAll( ShooterGameLevel.currentPlayer().launchShot( this ) );
            }

            //get nearest hitpoint
            HitPoint nearestHitPoint = HitPoint.getNearestHitPoint( hitPoints );

            //perform an operation if
            if
            (
                    //something has been hit
                    nearestHitPoint != null

                    //the game object is able to be hit
                &&  nearestHitPoint.iCarrier != null && nearestHitPoint.iCarrier.getHitPointCarrier() != null

                    //sharp ammo has been used
                &&  iType == ShotType.ESharpAmmo
            )
            {
                //ShooterDebug.bugfix.out( " faceangle of hit face: [" + nearestHitPoint.horzFaceAngle + "]" );

                //check the hitPoint's receiver
                switch ( nearestHitPoint.iCarrier.getHitPointCarrier() )
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
                        Wall w = (Wall)nearestHitPoint.iCarrier;
                        w.hurt( 20 );

                        break;
                    }

                    case EPlayer:
                    {
                        //player loses health
                        ShooterGameLevel.currentPlayer().hurt( LibMath.getRandom( 2, 10 ) );
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
                        Bot b = (Bot)nearestHitPoint.iCarrier;
                        b.hurt( 20 );

                        //draw Sliver
                        nearestHitPoint.drawWallSliver();

                        break;
                    }
                }

                //show debugs
                iDebug.out( "hit point: [" + nearestHitPoint.iVertex.x + "," + nearestHitPoint.iVertex.y + ", " + nearestHitPoint.iVertex.z + "]" );
                iDebug.out( "shotAngle [" + nearestHitPoint.iHorzShotAngle + "] SliverAngle [" + nearestHitPoint.iHorzSliverAngle + "] invertedShotAngle [" + nearestHitPoint.iHorzInvertedShotAngle + "] faceAngle [" + nearestHitPoint.iHorzFaceAngle + "]" );
            }

            iDebug.out( "=====================================================================\n" );

            return nearestHitPoint;
        }

        public final void drawShotLine()
        {
            //if ( true ) return;

            //draw shot line with own calculations
            for ( float distance = 0.0f; distance < iShotRange; distance += 0.15f )
            {
                FX.launchStaticPoint
                (
                    new LibVertex
                    (
                        (float)( iSrcPoint3d.x - LibMath.sinDeg( iRotZ ) * distance ),
                        (float)( iSrcPoint3d.y - LibMath.cosDeg( iRotZ ) * distance ),
                        (float)( iSrcPoint3d.z - LibMath.sinDeg( iRotX ) * distance )
                    ),
                    LibColors.EShotLine,
                    1,
                    iBulletHoleSize.size / 8 //DebugSettings.DEBUG_POINT_SIZE
                );
            }
        }




/*
        private final void drawShotLine()
        {
            //inoperative :/

            LibVertex v1 = new LibVertex
            (
                iSrcPoint.x - LibMath.sinDeg( iRotZ ) * 0.0f,
                iSrcPoint.y - LibMath.cosDeg( iRotZ ) * 0.0f,
                iSrcPoint.z - LibMath.sinDeg( iRotX ) * 0.0f
            );

            LibVertex v2 = new LibVertex
            (
                iSrcPoint.x - LibMath.sinDeg( iRotZ ) * iShotRange,
                iSrcPoint.y - LibMath.cosDeg( iRotZ ) * iShotRange,
                iSrcPoint.z - LibMath.sinDeg( iRotX ) * iShotRange
            );

            LibVertex v3 = v1.copy();
            LibVertex v4 = v2.copy();

            v3.x += DebugSettings.DEBUG_POINT_SIZE;
            v4.x += DebugSettings.DEBUG_POINT_SIZE;
            v3.y += DebugSettings.DEBUG_POINT_SIZE;
            v4.y += DebugSettings.DEBUG_POINT_SIZE;
            v3.z += DebugSettings.DEBUG_POINT_SIZE;
            v4.z += DebugSettings.DEBUG_POINT_SIZE;

            //draw this shot
            new FaceQuad( v1, v1, v3, v2, v4, LibColors.EShotLine ).draw();
        }
*/
    }
