/*  $Id: Cylinder.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.game.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.g3d.wall.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class Cylinder implements LibGeomObject
    {
        private     static  final   int                 VERTICAL_DEBUG_SLICES           = 4;

        private                     LibGameObject       iParentGameObject               = null;
        private                     LibVertex           iAnchor                         = null;
        private                     LibDebug            iDebug                          = null;

        /** Current center bottom point. */
        private                     float               iRadius                         = 0.0f;

        /** Cylinder's height. */
        public                      float               iHeight                         = 0.0f;
        private                     boolean             iDebugDrawBotCircles            = false;
        private                     int                 iCollisionCheckingSteps         = 0;
        private                     float               iBottomCollisionToleranceZ      = 0.0f;
        private                     float               iMinBottomCollisionToleranceZ   = 0.0f;

        /** The target position for the cylinder to move to. Will be the next pos if no collisions appear. */
        private                     LibVertex           target                          = null;

        public Cylinder( LibGameObject aParentGameObject, LibVertex aAnchor, float aRadius, float aHeight, int aCollisionCheckingSteps, LibDebug aDebug, boolean aDebugDrawBotCircles, float aBottomCollisionToleranceZ, float aMinBottomCollisionToleranceZ )
        {
            iParentGameObject               = aParentGameObject;
            iAnchor                         = aAnchor;
            iRadius                         = aRadius;
            iHeight                         = aHeight;
            iCollisionCheckingSteps         = aCollisionCheckingSteps;
            iDebug                          = aDebug;
            iDebugDrawBotCircles            = aDebugDrawBotCircles;
            iBottomCollisionToleranceZ      = aBottomCollisionToleranceZ;
            iMinBottomCollisionToleranceZ   = aMinBottomCollisionToleranceZ;
        }

        public final void update( float pX, float pY, float pZ, float newHeight )
        {
            iAnchor = new LibVertex( pX, pY, pZ );

            //unchanged
            //radius = RADIUS;
            iHeight = newHeight;

        }

        public final void drawDebug()
        {
            if ( iDebugDrawBotCircles )
            {
                //Debug.bot.out( "drawing bot .." );
                for ( int i = 0; i <= VERTICAL_DEBUG_SLICES; ++i )
                {
                    new FaceEllipseFloor( null, LibColors.ERed, iAnchor.x, iAnchor.y, iAnchor.z + ( i * iHeight / VERTICAL_DEBUG_SLICES ), iRadius, iRadius ).draw();
                }
            }
        }

        public final Point2D.Float getCenterHorz()
        {
            return new Point2D.Float( iAnchor.x, iAnchor.y );
        }

        @Override
        public final void setNewAnchor( LibVertex newAnk, boolean b, LibTransformationMode tm )
        {
            iAnchor.x = newAnk.x;
            iAnchor.y = newAnk.y;
            iAnchor.z = newAnk.z;
        }

        @Override
        public final void translate( float x, float y, float z, LibTransformationMode tm )
        {
            iAnchor.translate( x, y, z );
        }

        public final Vector<LibHitPoint> launchShot( LibShot shot )
        {
            Vector<LibHitPoint> ret = new Vector<LibHitPoint>();
            LibHitPoint hp = launchAShot( shot );
            if ( hp != null ) ret.addElement( hp );
            return ret;
        }

        /**
        *   Workaround - returning a Vector of HitPoints here will result in:
        *
        *   Exception in thread "Thread-3" java.lang.NullPointerException
        *   at java.util.Vector.addAll(Unknown Source)
        */
        private final LibHitPoint launchAShot( LibShot shot )
        {
            //if ( shot.iOrigin == ShotOrigin.EEnemies ) ShooterDebug.bugfix.out( "launch shot: [" + "on cylinder" + "]" );

            //check horizontal intersection
            Point2D.Float   centerHorz              = new Point2D.Float( iAnchor.x, iAnchor.y );
            boolean         horizontalIntersection  = shot.iLineShotHorz.ptSegDist( centerHorz ) <= iRadius;
            if ( horizontalIntersection )
            {
                //if ( shot.iOrigin == ShotOrigin.EEnemies ) ShooterDebug.bugfix.out( " player hit" );

                iDebug.out( "bot horizontal hit !!" );

                //get horizontal intersecttion
                Point2D.Float intersectionPointHorz = LibMathGeometry.findLineCircleIntersection( shot.iLineShotHorz, getCircle() );
                iDebug.out( "interP: ["+intersectionPointHorz+",]" );

                //get angle from horz hitPoint to shot-src-point
                float           angleCenterToHitPointHorz = LibMath.getAngleCorrect( centerHorz, intersectionPointHorz );
                float           angleHitPointHorzToCenter = angleCenterToHitPointHorz - 180.0f;

                float           exactDistanceHorz       = (float)shot.iSrcPointHorz.distance( intersectionPointHorz );
                float           shotAngleHorz           = LibMath.getAngleCorrect( shot.iSrcPointHorz, new Point2D.Float( (float)centerHorz.getX(), (float)centerHorz.getY() ) );        //get angle between player and hit-point
                float           invertedShotAngleHorz   = 360.0f - ( shotAngleHorz - 180.0f  );              //get opposite direction of shot
                float           sliverAngleHorz         = shotAngleHorz /* - faceAngleHorz */ * 2;              //get Sliver angle

                iDebug.out( "exactDistanceHorz:     [" + exactDistanceHorz       + "]" );
                iDebug.out( "shotAngleHorz:         [" + shotAngleHorz           + "]" );
                iDebug.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz   + "]" );
                iDebug.out( "SliverAngleHorz:       [" + sliverAngleHorz         + "]" );

                //calculate face's vertical collision line
                Line2D.Float collisionLineVert = new Line2D.Float( new Point2D.Float( exactDistanceHorz, iAnchor.z ), new Point2D.Float( exactDistanceHorz, iAnchor.z + iHeight ) );
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

                //return hit point
                return new LibHitPoint
                (
                    shot,
                    iParentGameObject,
                    ShooterMaterial.EUndefined.getBulletHoleTexture().getTexture(),
                    null,
                    ShooterMaterial.EHumanFlesh.iSliverColors,
                    new LibVertex( intersectionPointHorz.x, intersectionPointHorz.y, z ),
                    exactDistanceHorz,
                    shotAngleHorz,
                    invertedShotAngleHorz,
                    sliverAngleHorz,
                    angleHitPointHorzToCenter - 90.0f,
                    0.0f,                    //faceAngleVert
                    1.0f
                );
            }

            //return null
            return null;
        }

        public final Ellipse2D.Float getCircle()
        {
            return new Ellipse2D.Float( iAnchor.x - iRadius, iAnchor.y - iRadius, iRadius * 2, iRadius * 2 );
        }

        public final float getRadius()
        {
            return iRadius;
        }

        public final float setRadius( float newRadius )
        {
            return iRadius = newRadius;
        }

        public final LibVertex getTarget()
        {
            return target;
        }

        public final float getHeight()
        {
            return iHeight;
        }

        /************************************************************************************
        *   Checks vertical intersection.
        *
        *   @param  wallZ1  wall's first  z-point. May be lower or higher than the 2nd.
        *   @param  wallZ2  wall's second z-point. May be lower or higher than the 1st.
        *   @return         <code>true</code> if the player's vertical collision line
        *                   intersects the given wall points. Otherwise <code>false</code>.
        ************************************************************************************/
        public final boolean heightsIntersect( float wallZ1, float wallZ2, boolean useBottomToleranceZ, boolean invertBottomTolerance )
        {
            float lowestWallZ   = Math.min( wallZ1, wallZ2 );
            float highestWallZ  = Math.max( wallZ1, wallZ2 );

            //ShooterDebug.bugfix.out( "heights intersect " + wallZ1 + " " + wallZ2 + " " + iAnchor.z + "  "   );

            float mod = ( invertBottomTolerance ? -1.0f : 1.0f );
            boolean miss =
            (
                    iAnchor.z + ( useBottomToleranceZ ? mod * iBottomCollisionToleranceZ : mod * iMinBottomCollisionToleranceZ ) > highestWallZ
                ||  iAnchor.z + iHeight                                                                              < lowestWallZ
            );
            //ShooterDebug.bugfix.out( "intersect miss " + miss );

            //ShooterDebug.bugfix.out( " check: ["+anchor.z+"]["+lowestWallZ+"]["+highestWallZ+"]["+height+"] miss: ["+miss+"]" );

            //check if the player's head or toe point lies in between the wall line
            return !miss;
        }

        public void drawStandingCircle()
        {
            if ( iDebugDrawBotCircles )
            {
                new FaceEllipseFloor( null, LibColors.ERed, iAnchor.x, iAnchor.y, iAnchor.z + 0.001f, iRadius, iRadius ).draw();
            }
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
                float thisDistance = Math.abs( iAnchor.z - floorsToCheck.elementAt( i ).floatValue() );
                if
                (
                        ( nearestIndex == -1 )
                    ||  ( thisDistance < nearestDistance )
                )
                {
                    //check if the point is under the player or, if lower, not too high to climb up to!
                    if
                    (
                            floorsToCheck.elementAt( i ).floatValue() < iAnchor.z
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

        public final Float getHighestOfCheckedFloor( Vector<Float> floorsToCheck )
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
                float thisDistance = Math.abs( iAnchor.z - floorsToCheck.elementAt( i ).floatValue() );

                //only pick faces that..
                if
                (
                        //..are lower than the player
                        floorsToCheck.elementAt( i ).floatValue() < iAnchor.z
                        //..are not too high
                    ||  thisDistance <= iBottomCollisionToleranceZ
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
            target = new LibVertex( iAnchor.x, iAnchor.y, iAnchor.z );
        }

        public final void moveToTargetPosition( float newHeight, boolean disableWallCollisions, boolean disableBotCollisions )
        {
            //get distances from old to the new position
            float distX     = target.x - iAnchor.x;
            float distY     = target.y - iAnchor.y;
            float distZ     = target.z - iAnchor.z;

            //save original anchor cause it will change in the loop!
            float startX   = iAnchor.x;
            float startY   = iAnchor.y;
            float startZ   = iAnchor.z;

            //check target-point collision stepwise from far to near ( to increase walking speed )
            for ( float j = iCollisionCheckingSteps; j > 0; --j )
            {
                //try next step point..
                float distanceToCheckX = distX * j / iCollisionCheckingSteps;
                float distanceToCheckY = distY * j / iCollisionCheckingSteps;
                float distanceToCheckZ = distZ * j / iCollisionCheckingSteps;

                //try point on the line between current and target point
                target.x = startX + distanceToCheckX;
                target.y = startY + distanceToCheckY;
                target.z = startZ + distanceToCheckZ;
                if ( tryTargetPoint( newHeight, disableWallCollisions, disableBotCollisions ) ) return;

                //try point on x-clipped axis
                target.x = startX + distanceToCheckX;
                target.y = startY;
                target.z = startZ + distanceToCheckZ;
                if ( tryTargetPoint( newHeight, disableWallCollisions, disableBotCollisions ) ) return;

                //try point on y-clipped axis
                target.x = startX;
                target.y = startY + distanceToCheckY;
                target.z = startZ + distanceToCheckZ;
                if ( tryTargetPoint( newHeight, disableWallCollisions, disableBotCollisions ) ) return;
            }

            //update player's collision circle to initial position
            update( startX, startY, startZ, newHeight );
        }

        /**************************************************************************************
         *   Check collision with the given cylinder.
         *
         *    @param circle  Circle to check horizontal collision with.
         *    @return        <code>true</code> if collision appears. Otherwise <code>false</code>.
         **************************************************************************************/
         public final boolean checkCollision( float z )
         {
             //check collision of 2 circles ( easy  task.. )
             return
             (
                     z >= iAnchor.z
                 &&  z < ( iAnchor.z + iHeight )
             );
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

        public final boolean checkCollisionHorzLines( FaceTriangle face, boolean useBottomToleranceZ, boolean invertBottomTolerance  )
        {
            //check horizontal intersection
            Point2D.Double  cylinderPosHorz         = new Point2D.Double( iAnchor.x, iAnchor.y );
            boolean         horizontalIntersection1 = ( face.iCollisionLineHorz1.ptSegDist( cylinderPosHorz ) <= iRadius );
            boolean         horizontalIntersection2 = ( face.iCollisionLineHorz2.ptSegDist( cylinderPosHorz ) <= iRadius );
            boolean         horizontalIntersection3 = ( face.iCollisionLineHorz3.ptSegDist( cylinderPosHorz ) <= iRadius );

            if ( horizontalIntersection1 || horizontalIntersection2 || horizontalIntersection3 )
            {
                //check vertical intersection
                boolean verticalIntersection = heightsIntersect( face.iLowestZ, face.iHighestZ, useBottomToleranceZ, invertBottomTolerance   );
                if ( verticalIntersection )
                {
                    //ShooterDebug.bugfix.out("INTERSECT - return float with z [" + face.iHighestZ + "]");
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
        public final boolean checkCollisionHorz( Cylinder c )
        {
            //check horizontal and vertical intersection
            if ( checkCollision( c.getCircle() ) && c.heightsIntersect( iAnchor.z, iAnchor.z + iHeight, false, false ) )
            {
                //no height information is required here ..
                return true;
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
        public final Vector<Float> checkCollisionVert( Cylinder c )
        {
            Vector<Float> v = new Vector<Float>();

            //check horizontal and vertical intersection
            if ( checkCollision( c.getCircle() ) && c.heightsIntersect( iAnchor.z, iAnchor.z + iHeight, false, false ) )
            {
                //no height information is required here ..
                v.add( new Float( 42 ) );
            }

            return v;
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iAnchor;
        }

        public final boolean tryTargetPoint( float newHeight, boolean disableWallCollisions, boolean disableBotCollisions )
        {
            //update player's collision circle with target location
            update( target.x, target.y, target.z, newHeight );

            //check if target-point is collision free
            boolean collisionFree =
            (
                    ( disableWallCollisions || !ShooterGameLevel.current().checkCollisionOnWalls( this ) )
                &&  ( disableBotCollisions  || !ShooterGameLevel.current().checkCollisionOnBots(  this ) )
            );

            if ( collisionFree )
            {
                //assign this point as the target-point and leave this method!
                update( target.x, target.y, target.z, newHeight );
            }

            return collisionFree;
        }

        public final Cylinder copy()
        {
            return new Cylinder
            (
                iParentGameObject,
                iAnchor.copy(),
                iRadius,
                iHeight,
                iCollisionCheckingSteps,
                iDebug,
                iDebugDrawBotCircles,
                iBottomCollisionToleranceZ,
                iMinBottomCollisionToleranceZ
            );
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
