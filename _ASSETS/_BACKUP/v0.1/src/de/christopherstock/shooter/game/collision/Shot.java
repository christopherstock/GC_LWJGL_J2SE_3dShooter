/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  java.awt.geom.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.player.*;
    import  de.christopherstock.shooter.gl.g3d.*;
    import  de.christopherstock.shooter.ui.*;
    import  de.christopherstock.shooter.util.*;

    /**************************************************************************************
    *   All calculations for one shot.
    *
    *   @author     stock
    *   @version    1.0
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
        private                     Vertex              srcPoint                    = null;
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
        private                     boolean             drawBulletHole              = false;
        private                     boolean             viewOnly                    = false;

        public Shot( ShotOrigin origin, boolean viewOnly, float irregularityAngle, float irregularityDepth, float startX, float startY, float startZ, float rotZ, float rotX, float shotRange, boolean drawBulletHole )
        {
            Debug.shot.out( "=======================================" );

            //assign
            this.viewOnly       = viewOnly;
            this.origin         = origin;
            this.srcPoint       = new Vertex( startX, startY, startZ );
            this.rotZ           = rotZ;
            this.rotX           = rotX;
            this.shotRange      = shotRange;
            this.drawBulletHole = drawBulletHole;

            //let the wearpon affect the horz- and vert-fire-angles
            this.rotZ           += irregularityAngle;
            this.rotX           += irregularityDepth;

            //calculate end point and shot-line for the horizontal axis
            srcPointHorz = new Point2D.Float( srcPoint.x, srcPoint.y );
            this.endPointHorz   = new Point2D.Float
            (
                srcPointHorz.x - UMath.sinDeg( this.rotZ ) * this.shotRange,
                srcPointHorz.y - UMath.cosDeg( this.rotZ ) * this.shotRange
            );
            this.lineShotHorz   = new Line2D.Float( srcPointHorz, endPointHorz );
            Debug.shot.out( "SHOT LINE HORZ [" + srcPoint.x + "," + srcPoint.y + "] to [" + endPointHorz.x + "," + endPointHorz.y + "]" );

            //calculate end point and shot-line for the vertical axis
            srcPointVert = new Point2D.Float( 0.0f, srcPoint.z );
            this.endPointVert   = new Point2D.Float
            (
                srcPointVert.x + UMath.cosDeg( this.rotX ) * this.shotRange,
                srcPointVert.y - UMath.sinDeg( this.rotX ) * this.shotRange
            );
            this.lineShotVert   = new Line2D.Float( srcPointVert, endPointVert );
            Debug.shot.out( "SHOT LINE VERT " + srcPointVert + endPointVert );

            //Debug.drawLine( new Vertex( 2.0f, 1.0f, 2.0f, -1.0f, -1.0f ), new Vertex( 0.0f, 0.0f, 0.0f, -1.0f, -1.0f ) );
            //Debug.drawLine( srcPointVert.x, srcPointVert.y, endPointVert.x, endPointVert.y );
            //Debug.drawLine( new Vertex( srcPoint.x, srcPoint.y, srcPointVert.y, -1.0f, -1.0f ), new Vertex( endPointVert.x, 0.0f, 0.0f, -1.0f, -1.0f ) );

        }

        public HitPoint launch()
        {
            //launching the shot onto the mesh collection returns the nearest hit-point ( if any )
            hitPoint = Level.launchShot( this );

            //check if the shot will have effect
            if ( hitPoint != null )
            {
                //only perform for sharp ammo
                if ( !viewOnly )
                {
                    //check the hitPoint's body
                    switch ( hitPoint.carrier )
                    {
                        case EWall:
                        {
                            //append bullet hole
                            if ( drawBulletHole ) BulletHole.addBulletHole( hitPoint );
                            //draw Sliver
                            if ( drawBulletHole ) hitPoint.drawWallSliver();
                            break;
                        }

                        case EPlayer:
                        {
                            //player loses health
                            Player.user.hurt( UMath.getRandom( 2, 10 ) );
                            break;
                        }

                        case EBot:
                        {
                            //append bullet hole
                            if ( drawBulletHole ) BulletHole.addBulletHole( hitPoint );
                            //draw Sliver
                            if ( drawBulletHole ) hitPoint.drawWallSliver();
                            break;
                        }
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
            for ( float distance = 0.0f; distance < Player.user.getWearpon().getShotRange(); distance += 0.01f )
            {
                DebugPoint.add
                (
                    Colors.EYellow,
                    srcPoint.x - UMath.sinDeg( rotZ ) * distance,
                    srcPoint.y - UMath.cosDeg( rotZ ) * distance,
                    srcPoint.z - UMath.sinDeg( rotX ) * distance,
                    DebugPoint.ELifetimeMedium
                );
            }
        }
    }
