/*  $Id: Cylinder.java 431 2011-03-19 17:48:24Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class Cylinder implements LibGeomObject
    {
        private     static  final   int                 DEBUG_SLICES                = 4;

        public                      LibVertex           anchor                      = null;
        private                     GameObject          parentGameObject            = null;

        /** Current center bottom point. */
        private                     float               radius                      = 0.0f;
        private                     float               height                      = 0.0f;

        /** The target position for the cylinder to move to.
        *   Will be the next pos if no collisions appear.
        **/
        private                     LibVertex           target                      = null;
        private                     LibColors           color                       = null;

        private                     LibDebug            iDebug                      = null;
        private                     boolean             iDebugDrawBotCircles        = false;

        private                     int                 iCollisionCheckingSteps     = 0;

        public Cylinder( GameObject aParentGameObject, LibVertex aAnchor, float aRadius, float aHeight, int aCollisionCheckingSteps, LibDebug aDebug, boolean aDebugDrawBotCircles )
        {
            anchor                  = aAnchor;
            parentGameObject        = aParentGameObject;
            radius                  = aRadius;
            height                  = aHeight;
            iCollisionCheckingSteps = aCollisionCheckingSteps;
            iDebug                  = aDebug;
            iDebugDrawBotCircles    = aDebugDrawBotCircles;
        }

        public final void update( float pX, float pY, float pZ, float newHeight )
        {
            anchor = new LibVertex( pX, pY, pZ );

            //unchanged
            //radius = RADIUS;
            height = newHeight;

        }

        public final void drawDebug()
        {
            if ( iDebugDrawBotCircles )
            {
                //Debug.bot.out( "drawing bot .." );
                for ( int i = 0; i <= DEBUG_SLICES; ++i )
                {
                    new FaceEllipseFloor( null, color, anchor.x, anchor.y, anchor.z + ( i * height / DEBUG_SLICES ), radius, radius ).draw();
                }
            }
        }

        /**************************************************************************************
        *   Check collision with the given cylinder.
        *
        *    @param circle  Circle to check horizontal collision with.
        *    @return        <code>true</code> if collision appears. Otherwise <code>false</code>.
        **************************************************************************************/
        public final boolean checkCollision( Ellipse2D.Float circle )
        {
            //check collision of 2 circles ( easy  task.. )
            Area circleHorz      = new Area( circle );
            Area ownCircleHorz   = new Area( getCircle() );
            circleHorz.intersect( ownCircleHorz );

            //check horizontal intersection
            return ( !circleHorz.isEmpty() );
        }

        public final Point2D.Float getCenterHorz()
        {
            return new Point2D.Float( anchor.x, anchor.y );
        }

        @Override
        public final void setNewAnchor( LibVertex newAnk, boolean b, LibTransformationMode tm )
        {
            anchor.x = newAnk.x;
            anchor.y = newAnk.y;
            anchor.z = newAnk.z;
        }

        @Override
        public final void translate( float x, float y, float z, LibTransformationMode tm )
        {
            anchor.translate( x, y, z );
        }

        public final Vector<HitPoint> launchShot( Shot shot )
        {
            Vector<HitPoint> ret = new Vector<HitPoint>();
            HitPoint hp = launchAShot( shot );
            if ( hp != null ) ret.addElement( hp );
            return ret;
        }

        /**
        *   Workaround - returning a Vector of HitPoints here will result in:
        *
        *   Exception in thread "Thread-3" java.lang.NullPointerException
        *   at java.util.Vector.addAll(Unknown Source)
        */
        private final HitPoint launchAShot( Shot shot )
        {
            //check horizontal intersection
            Point2D.Float   centerHorz              = new Point2D.Float( anchor.x, anchor.y );
            boolean         horizontalIntersection  = shot.iLineShotHorz.ptSegDist( centerHorz ) <= radius;
            if ( horizontalIntersection )
            {
                iDebug.out( "bot horizontal hit !!" );

                //get horizontal intersecttion
                Point2D.Float intersectionPointHorz = LibMathGeometry.findLineCircleIntersection( shot.iLineShotHorz, new Ellipse2D.Float( anchor.x - radius, anchor.y - radius, radius * 2, radius * 2 ) );
                iDebug.out( "interP: ["+intersectionPointHorz+",]" );

                //get angle from horz hitPoint to shot-src-point
                float           angleCenterToHitPointHorz = LibMath.getAngleCorrect( centerHorz, intersectionPointHorz );
                float           angleHitPointHorzToCenter = angleCenterToHitPointHorz - 180.0f;

                float           exactDistanceHorz       = (float)shot.iSrcPointHorz.distance( intersectionPointHorz );
                float           shotAngleHorz           = LibMath.getAngleCorrect( shot.iSrcPointHorz, new Point2D.Float( (float)centerHorz.getX(), (float)centerHorz.getY() ) );        //get angle between player and hit-point
                float           invertedShotAngleHorz   = 360.0f - ( shotAngleHorz - 180.0f  );              //get opposite direction of shot
                float           SliverAngleHorz         = shotAngleHorz /* - faceAngleHorz */ * 2;              //get Sliver angle

                iDebug.out( "exactDistanceHorz:     [" + exactDistanceHorz       + "]" );
                iDebug.out( "shotAngleHorz:         [" + shotAngleHorz           + "]" );
                iDebug.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz   + "]" );
                iDebug.out( "SliverAngleHorz:       [" + SliverAngleHorz         + "]" );

                //calculate face's vertical collision line
                Line2D.Float collisionLineVert = new Line2D.Float( new Point2D.Float( exactDistanceHorz, anchor.z ), new Point2D.Float( exactDistanceHorz, anchor.z + height ) );
                iDebug.out( "bot's collision line vert is: [" + collisionLineVert + "]" );

                if ( !shot.iLineShotVert.intersectsLine( collisionLineVert ) )
                {
                    iDebug.out( "VERTICAL FACE MISSED!" );
                    return null;
                }

                //get intersection point for the vertical axis
                iDebug.out( "VERTICAL FACE HIT!" );
                Point2D.Float   intersectionPointVert   = LibMathGeometry.findLineSegmentIntersection( shot.iLineShotVert, collisionLineVert, iDebug );
                float           z                       = intersectionPointVert.y;
                iDebug.out( ">> INTERSECTION POINT VERT: " + intersectionPointVert );

                //get vertical values
                float exactDistanceVert = (float)shot.iSrcPointVert.distance( intersectionPointVert );       //get exact distance

                iDebug.out( ">> EXACT DISTANCE VERT: " + exactDistanceVert );
//                iDebug.out( ">> SHOT-ANGLE-VERT: " + shotAngleVert );
//                iDebug.out( ">> SLIVER-ANGLE-VERT: " + SliverAngleVert );

                //TODO MERGE 2

                //return hit point
                return new HitPoint
                (
                    parentGameObject,
                    ShooterTexture.getBulletHoleTextureForMaterial( LibGLMaterial.EUndefined ).iTexture,
                    new LibVertex( intersectionPointHorz.x, intersectionPointHorz.y, z ),
                    shot.iBulletHoleSize,
                    shot.iSrcPointHorz,
                    shot.iSrcPointVert,
                    exactDistanceHorz,
                    shotAngleHorz,
                    invertedShotAngleHorz,
                    SliverAngleHorz,
                    angleHitPointHorzToCenter - 90.0f,
                    exactDistanceVert,
                    0.0f                    //faceAngleVert
                );
            }

            //return null
            return null;
        }

        public final Ellipse2D.Float getCircle()
        {
            return new Ellipse2D.Float( anchor.x - radius, anchor.y - radius, radius * 2, radius * 2 );
        }

        public final float getRadius()
        {
            return radius;
        }

        public final float getActionRadius()
        {
            return radius * 2;
        }

        public final LibVertex getTarget()
        {
            return target;
        }

        public final float getHeight()
        {
            return height;
        }

        /************************************************************************************
        *   Checks vertical intersection.
        *
        *   @param  wallZ1  wall's first  z-point. May be lower or higher than the 2nd.
        *   @param  wallZ2  wall's second z-point. May be lower or higher than the 1st.
        *   @return         <code>true</code> if the player's vertical collision line
        *                   intersects the given wall points. Otherwise <code>false</code>.
        ************************************************************************************/
        public final boolean heightsIntersect( float wallZ1, float wallZ2 )
        {
            float lowestWallZ   = Math.min( wallZ1, wallZ2 );
            float highestWallZ  = Math.max( wallZ1, wallZ2 );

            //check if the player's head or toe point lies in between the wall line
            return
            (
                    (   anchor.z            >= lowestWallZ && anchor.z          <= highestWallZ )
                ||  (   anchor.z + height   >= lowestWallZ && anchor.z + height <= highestWallZ )
            );
        }

        public void drawStandingCircle()
        {
            if ( iDebugDrawBotCircles )
            {
                new FaceEllipseFloor( null, LibColors.ERed, anchor.x, anchor.y, anchor.z + 0.001f, radius, radius ).draw();
            }
        }

        public void setDebugPoint()
        {
            //set a debug point!
            LibGLDebugPoint.add( LibColors.EGreen, anchor.x, anchor.y, anchor.z + height / 2, LibGLDebugPoint.ELifetimeLong );
        }

        /**************************************************************************************
        *   Return the nearest available position z for the given point.
        *
        *   @param      floorsToCheck   All floors to check.
        *   @param      maxClimbDistZ   The maximum climbing distance z from the anchor's z-position.
        *   @return                     The nearest floor's z-position or <code>null</code> if non-existent.
        *   @deprecated                 May be useful one day.
        **************************************************************************************/
        @Deprecated
        public final Float getNearestFloor( Vector<Float> floorsToCheck, float maxClimbDistZ )
        {
            if ( floorsToCheck.size() == 0 )
            {
                return null;
            }

            //get the nearest face-hit-point
            float nearestDistance = 0.0f;
            int   nearestIndex    = -1;
            for ( int i = 0; i < floorsToCheck.size(); ++i )
            {
                //assign 1st point - check if the distance is nearer
                float thisDistance = Math.abs( anchor.z - floorsToCheck.elementAt( i ).floatValue() );
                if
                (
                        ( nearestIndex == -1 )
                    ||  ( thisDistance < nearestDistance )
                )
                {
                    //check if the point is under the player or, if lower, not too high to climb up to!
                    if
                    (
                            floorsToCheck.elementAt( i ).floatValue() < anchor.z
                        ||  thisDistance <= maxClimbDistZ
                    )
                    {
                        nearestIndex    = i;
                        nearestDistance = floorsToCheck.elementAt( i ).floatValue();

                    }
                }
            }

            //return the nearest distance
            return ( nearestIndex == -1 ? null : floorsToCheck.elementAt( nearestIndex ) );
        }

        public final Float getHighestFloor( Vector<Float> floorsToCheck, float maxClimbDistZ )
        {
            if ( floorsToCheck.size() == 0 )
            {
                return null;
            }

            //get the highest face-hit-point
            Float highestZ = null;
            for ( int i = 0; i < floorsToCheck.size(); ++i )
            {
                //assign 1st point - check if the distance is nearer
                float thisDistance = Math.abs( anchor.z - floorsToCheck.elementAt( i ).floatValue() );

                //only pick faces that..
                if
                (
                        //..are lower than the player
                        floorsToCheck.elementAt( i ).floatValue() < anchor.z
                        //..are not too high
                    ||  thisDistance <= maxClimbDistZ
                )
                {
                    //assign if higher
                    if ( highestZ == null || floorsToCheck.elementAt( i ).floatValue() > highestZ.floatValue() )
                    {
                        highestZ = floorsToCheck.elementAt( i );
                    }
                }
            }

            //return the nearest distance
            return highestZ;
        }

        public final void setAnchorAsTargetPosition()
        {
            target = new LibVertex( anchor.x, anchor.y, anchor.z );
        }

        public final void moveToTargetPosition( float newHeight, boolean disableWallCollisions, boolean disableBotCollisions )
        {
            //get distances from old to the new position
            float distX     = target.x - anchor.x;
            float distY     = target.y - anchor.y;
            float distZ     = target.z - anchor.z;

            //assign current position as the best position
            float bestPosX  = anchor.x;
            float bestPosY  = anchor.y;
            float bestPosZ  = anchor.z;

            //save original anchor cause it will change in the loop!
            float anchorX   = anchor.x;
            float anchorY   = anchor.y;
            float anchorZ   = anchor.z;

            //nearer the target-point stepwise
            for ( float j = iCollisionCheckingSteps; j > 0; --j )
            {
                //try next step point..
                float distanceToCheckX = distX * j / iCollisionCheckingSteps;
                float distanceToCheckY = distY * j / iCollisionCheckingSteps;
                float distanceToCheckZ = distZ * j / iCollisionCheckingSteps;

                //assign point on the line between current and target point
                target.x = anchorX + distanceToCheckX;
                target.y = anchorY + distanceToCheckY;
                target.z = anchorZ + distanceToCheckZ;

                //update player's collision circle with target location
                update( target.x, target.y, target.z, newHeight );

                //break checking if target-point is collision free
                if
                (
                        ( disableWallCollisions || !Level.current().checkCollisionOnWalls( this ) )
                    &&  ( disableBotCollisions  || !Level.current().checkCollisionOnBots(  this ) )
                )
                {
                    //assign this point as the target-point and break
                    bestPosX = target.x;
                    bestPosY = target.y;
                    bestPosZ = target.z;
                    break;
                }
            }

            //update player's collision circle
            update( bestPosX, bestPosY, bestPosZ, newHeight );
        }

        public final boolean checkCollision( FaceTriangle face, boolean actionRadius )
        {
            //cylinders will not collide on floors
            if ( face.iLowestZ == face.iHighestZ ) return false;

            //check horizontal intersection
            Point2D.Double  playerPosHorz           = new Point2D.Double( getCircle().getCenterX(), getCircle().getCenterY() );
            boolean         horizontalIntersection  = face.iCollisionLineHorz.ptSegDist( playerPosHorz ) <= ( actionRadius ? getActionRadius() : getRadius() );
            if ( horizontalIntersection )
            {
                //check vertical intersection
                boolean verticalIntersection = heightsIntersect( face.iLowestZ, face.iHighestZ );
                if ( verticalIntersection )
                {
                    return true;
                }
            }

            return false;
        }

        /**************************************************************************************
        *   Check collision with the given cylinder.
        *
        *    @param c   Cylinder to check collision with.
        *    @return    <code>true</code> if collision appears. Otherwise <code>false</code>.
        **************************************************************************************/
        @Override
        public final boolean checkCollision( Cylinder c )
        {
            //check horizontal and vertical intersection
            if ( checkCollision( c.getCircle() ) && c.heightsIntersect( anchor.z, anchor.z + height ) )
            {
                return true;
            }

            return false;
        }

        @Override
        public final LibVertex getAnchor()
        {
            return anchor;
        }

        /**************************************************************************************
        *   Check horizontal collision of the face and the player's center point.
        *
        *   @param  x1      rect x1.
        *   @param  y1      rect y1.
        *   @param  x2      rect x2.
        *   @param  y2      rect y2.
        *   @return         <code>true</code> if the cylinder's center point is in the rect.
        *   @deprecated     replaced by j2se api.
        **************************************************************************************/
/*
        @Deprecated
        public final boolean checkHorzCenterCollision( float x1, float y1, float x2, float y2 )
        {
            return
            (
                    ( x1 > x2 ? anchor.x >= x2 && anchor.x <= x1 : anchor.x >= x1 && anchor.x <= x2 )
                &&  ( y1 > y2 ? anchor.y >= y2 && anchor.y <= y1 : anchor.y >= y1 && anchor.y <= y2 )
            );
        }
*/
    }
