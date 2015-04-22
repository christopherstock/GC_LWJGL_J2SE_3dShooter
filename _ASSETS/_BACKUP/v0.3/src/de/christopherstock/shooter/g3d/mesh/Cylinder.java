/*  $Id: Cylinder.java 261 2011-02-10 00:39:04Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.DebugSettings;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class Cylinder extends GeomObject
    {
        private     static  final   int                 DEBUG_SLICES                = 4;

        private                     GameObject          parentGameObject            = null;

        /** Current center bottom point. */
        private                     float               radius                      = 0.0f;
        private                     float               height                      = 0.0f;

        /** The target position for the cylinder to move to.
        *   Will be the next pos if no collisions appear.
        **/
        private                     LibVertex           target                      = null;
        private                     LibColors           color                       = null;

        public Cylinder( GameObject aParentGameObject, LibVertex aAnchor, float aRadius, float aHeight )
        {
            anchor              = aAnchor;
            parentGameObject    = aParentGameObject;
            radius              = aRadius;
            height              = aHeight;
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
            if ( DebugSettings.DEBUG_DRAW_BOT_CIRCLES )
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

        public final void setCenterHorz( float x, float y )
        {
            anchor.x = x;
            anchor.y = y;
        }

        @Override
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
            boolean         horizontalIntersection  = shot.lineShotHorz.ptSegDist( centerHorz ) <= radius;
            if ( horizontalIntersection )
            {
                ShooterDebugSystem.bot.out( "bot horizontal hit !!" );

                //get horizontal intersecttion
                Point2D.Float intersectionPoint = LibMathGeometry.findLineCircleIntersection( shot.lineShotHorz, new Ellipse2D.Float( anchor.x - radius, anchor.y - radius, radius * 2, radius * 2 ) );
                ShooterDebugSystem.bot.out( "interP: ["+intersectionPoint+",]" );

                //get angle from horz hitPoint to shot-src-point
                float           angleCenterToHitPointHorz = LibMath.getAngleCorrect( centerHorz, intersectionPoint );
                float           angleHitPointHorzToCenter = angleCenterToHitPointHorz - 180.0f;

                float           exactDistanceHorz       = (float)shot.srcPointHorz.distance( intersectionPoint );
                float           shotAngleHorz           = LibMath.getAngleCorrect( shot.srcPointHorz, new Point2D.Float( (float)centerHorz.getX(), (float)centerHorz.getY() ) );        //get angle between player and hit-point
                float           invertedShotAngleHorz   = 360.0f - ( shotAngleHorz - 180.0f  );              //get opposite direction of shot
                float           SliverAngleHorz         = shotAngleHorz /* - faceAngleHorz */ * 2;              //get Sliver angle

                ShooterDebugSystem.bot.out( "exactDistanceHorz:     [" + exactDistanceHorz       + "]" );
                ShooterDebugSystem.bot.out( "shotAngleHorz:         [" + shotAngleHorz           + "]" );
                ShooterDebugSystem.bot.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz   + "]" );
                ShooterDebugSystem.bot.out( "SliverAngleHorz:       [" + SliverAngleHorz         + "]" );

                //calculate face's vertical collision line
                Line2D.Float collisionLineVert = new Line2D.Float( new Point2D.Float( exactDistanceHorz, anchor.z ), new Point2D.Float( exactDistanceHorz, anchor.z + height ) );
                ShooterDebugSystem.bot.out( "bot's collision line vert is: [" + collisionLineVert + "]" );

                if ( !shot.lineShotVert.intersectsLine( collisionLineVert ) )
                {
                    ShooterDebugSystem.bot.out( "VERTICAL FACE MISSED!" );
                    return null;
                }

                //get intersection point for the vertical axis
                ShooterDebugSystem.bot.out( "VERTICAL FACE HIT!" );
                Point2D.Float   intersectionPointVert   = LibMathGeometry.findLineSegmentIntersection( shot.lineShotVert, collisionLineVert, ShooterDebugSystem.error );
                float           z                       = intersectionPointVert.y;
                ShooterDebugSystem.bot.out( ">> INTERSECTION POINT VERT: " + intersectionPointVert );

                //get vertical values
                float exactDistanceVert = (float)shot.srcPointVert.distance( intersectionPointVert );       //get exact distance
                float shotAngleVert         = LibMath.getAngleCorrect( shot.srcPointVert, new Point2D.Float( exactDistanceVert, z ) );    //get angle between player and hit-point
                float invertedShotAngleVert = 360.0f - ( shotAngleVert - 180.0f  );                             //get opposite direction of shot
                float SliverAngleVert       = 90.0f + 90.0f - shotAngleVert ;                                //get Sliver angle
                ShooterDebugSystem.bot.out( ">> EXACT DISTANCE VERT: " + exactDistanceVert );
                ShooterDebugSystem.bot.out( ">> SHOT-ANGLE-VERT: " + shotAngleVert );
                ShooterDebugSystem.bot.out( ">> SLIVER-ANGLE-VERT: " + SliverAngleVert );

                //return hit point
                return new HitPoint
                (
                    parentGameObject,
                    shot,
                    null,
                    new LibVertex( intersectionPoint.x, intersectionPoint.y, z ),

                    exactDistanceHorz,
                    shotAngleHorz,
                    invertedShotAngleHorz,
                    SliverAngleHorz,
                    angleHitPointHorzToCenter - 90.0f,

                    exactDistanceVert,
                    shotAngleVert,
                    invertedShotAngleVert,
                    SliverAngleVert,
                    0.0f                    //faceAngleVert
                );
            }

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
            if ( DebugSettings.DEBUG_SHOW_PLAYER_CIRCLE )
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

        public final void moveToTargetPosition( float newHeight )
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
            for ( float j = ShooterSettings.Performance.COLLISION_CHECKING_STEPS; j > 0; --j )
            {
                //try next step point..
                float distanceToCheckX = distX * j / ShooterSettings.Performance.COLLISION_CHECKING_STEPS;
                float distanceToCheckY = distY * j / ShooterSettings.Performance.COLLISION_CHECKING_STEPS;
                float distanceToCheckZ = distZ * j / ShooterSettings.Performance.COLLISION_CHECKING_STEPS;

                //assign point on the line between current and target point
                target.x = anchorX + distanceToCheckX;
                target.y = anchorY + distanceToCheckY;
                target.z = anchorZ + distanceToCheckZ;

                //update player's collision circle with target location
                update( target.x, target.y, target.z, newHeight );

                //break checking if target-point is collision free
                if ( !Level.current().checkCollisionOnWalls( this ) )
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

        public final boolean checkCollision( FreeTriangle face, boolean actionRadius )
        {
            //cylinders will not collide on floors
            if ( face.lowestZ == face.highestZ ) return false;

            //check horizontal intersection
            Point2D.Double  playerPosHorz           = new Point2D.Double( getCircle().getCenterX(), getCircle().getCenterY() );
            boolean         horizontalIntersection  = face.collisionLineHorz.ptSegDist( playerPosHorz ) <= ( actionRadius ? getActionRadius() : getRadius() );
            if ( horizontalIntersection )
            {
                //check vertical intersection
                boolean verticalIntersection = heightsIntersect( face.lowestZ, face.highestZ );
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
