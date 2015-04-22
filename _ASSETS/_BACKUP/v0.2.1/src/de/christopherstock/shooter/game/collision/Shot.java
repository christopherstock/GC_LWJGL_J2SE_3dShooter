/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  java.awt.geom.*;

import de.christopherstock.lib.g3d.*;
import de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.*;
import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.player.*;
import  de.christopherstock.shooter.ui.*;

    /**************************************************************************************
    *   All calculations for one shot.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public class Shot
    {
        public enum ShotOrigin
        {
            /** the one and only player */
            EPlayer,
            /** all friendly bots */
            EFriends,
            /** all hostile bots */
            EEnemies,
        }

        //input values
        public                      ShotOrigin          origin                      = null;
        private                     LibVertex              srcPoint                    = null;
        private                     float               rotZ                        = 0.0f;
        private                     float               rotX                        = 0.0f;
        private                     float               shotRange                   = 0.0f;

        //calculated values
        private                     Point2D.Float       endPointHorz                = null;
        private                     Point2D.Float       endPointVert                = null;

        public                      Line2D.Float        lineShotHorz                = null;
        public                      Line2D.Float        lineShotVert                = null;

        public                      Point2D.Float       srcPointHorz                = null;
        public                      Point2D.Float       srcPointVert                = null;

        private                     HitPoint            hitPoint                    = null;
        private                     boolean             viewOnly                    = false;

        protected                   BulletHole.PointSize bulletHoleSize             = null;

        public Shot( ShotOrigin aOrigin, boolean aViewOnly, float irregularityAngle, float irregularityDepth, float startX, float startY, float startZ, float aRotZ, float aRotX, float aShotRange, BulletHole.PointSize aBulletHoleSize )
        {
            Debug.shot.out( "=======================================" );

            //assign
            viewOnly        = aViewOnly;
            origin          = aOrigin;
            srcPoint        = new LibVertex( startX, startY, startZ );
            rotZ            = aRotZ;
            rotX            = aRotX;
            shotRange       = aShotRange;
            bulletHoleSize  = aBulletHoleSize;

            //let the wearpon affect the horz- and vert-fire-angles
            rotZ           += irregularityAngle;
            rotX           += irregularityDepth;

            //calculate end point and shot-line for the horizontal axis
            srcPointHorz = new Point2D.Float( srcPoint.x, srcPoint.y );
            endPointHorz   = new Point2D.Float
            (
                srcPointHorz.x - LibMath.sinDeg( rotZ ) * shotRange,
                srcPointHorz.y - LibMath.cosDeg( rotZ ) * shotRange
            );
            lineShotHorz   = new Line2D.Float( srcPointHorz, endPointHorz );
            Debug.shot.out( "SHOT LINE HORZ [" + srcPoint.x + "," + srcPoint.y + "] to [" + endPointHorz.x + "," + endPointHorz.y + "]" );

            //calculate end point and shot-line for the vertical axis
            srcPointVert = new Point2D.Float( 0.0f, srcPoint.z );
            endPointVert   = new Point2D.Float
            (
                srcPointVert.x + LibMath.cosDeg( rotX ) * shotRange,
                srcPointVert.y - LibMath.sinDeg( rotX ) * shotRange
            );
            lineShotVert   = new Line2D.Float( srcPointVert, endPointVert );
            Debug.shot.out( "SHOT LINE VERT " + srcPointVert + endPointVert );

            //Debug.drawLine( new Vertex( 2.0f, 1.0f, 2.0f, -1.0f, -1.0f ), new Vertex( 0.0f, 0.0f, 0.0f, -1.0f, -1.0f ) );
            //Debug.drawLine( srcPointVert.x, srcPointVert.y, endPointVert.x, endPointVert.y );
            //Debug.drawLine( new Vertex( srcPoint.x, srcPoint.y, srcPointVert.y, -1.0f, -1.0f ), new Vertex( endPointVert.x, 0.0f, 0.0f, -1.0f, -1.0f ) );
        }

        public HitPoint launch()
        {
            //launching the shot on the level returns the nearest hitpoint ( if any )
            hitPoint = Level.launchShot( this );

            //perform an operation if
            if
            (
                    //something has been hit
                    hitPoint != null

                    //the hit item is able to carry a hit point
                &&  hitPoint.gameObject != null && hitPoint.gameObject.getHitPointCarrier() != null

                    //sharp ammo has been used
                &&  !viewOnly
            )
            {
                //check the hitPoint's body
                switch ( hitPoint.gameObject.getHitPointCarrier() )
                {
                    case EWall:
                    {
                        //append bullet hole
                        if ( bulletHoleSize != BulletHole.PointSize.ENone )
                        {
                            BulletHole.addBulletHole( hitPoint );

                            //draw Sliver
                            hitPoint.drawWallSliver();
                        }
                        break;
                    }

                    case EPlayer:
                    {
                        //player loses health
                        Player.singleton.hurt( LibMath.getRandom( 2, 10 ) );
                        break;
                    }

                    case EBot:
                    {
                        //append bullet hole
                        if ( bulletHoleSize != BulletHole.PointSize.ENone )
                        {
                            BulletHole.addBulletHole( hitPoint );

                            //draw Sliver
                            hitPoint.drawWallSliver();
                        }
                        break;
                    }
                }

                //show debugs
                hitPoint.debugOut();

            }

            Debug.shot.out( "=====================================================================\n" );

            return hitPoint;

        }

        public final void drawShotLine()
        {
            //draw shot line with own calculations
            for ( float distance = 0.0f; distance < Player.singleton.getWearpon().getShotRange(); distance += 0.01f )
            {
                DebugPoint.add
                (
                    Colors.EYellow,
                    srcPoint.x - LibMath.sinDeg( rotZ ) * distance,
                    srcPoint.y - LibMath.cosDeg( rotZ ) * distance,
                    srcPoint.z - LibMath.sinDeg( rotX ) * distance,
                    DebugPoint.ELifetimeMedium
                );
            }
        }
    }
