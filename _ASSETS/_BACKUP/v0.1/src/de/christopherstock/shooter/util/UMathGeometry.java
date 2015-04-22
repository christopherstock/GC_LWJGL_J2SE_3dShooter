/*  $Id: Face.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.util;

    import  java.awt.geom.*;
    import  de.christopherstock.shooter.*;

    /**************************************************************************************
    *   A collection of static geometry utility methods.
    *
    *   @version    1.0
    *   @author     stock
    **************************************************************************************/
    public final class UMathGeometry
    {
        /**************************************************************************************
        *   Find the intersection of this circle with the line defined by point p1 & p2.
        *
        *   @param      line        The line to check collision with the specified circle.
        *   @param      circle      The circle to check collision with the specified line.
        *   @return                 The NEARER point of intersection seen from Line2D.Float.getP1().
        *                           <code>null</code> if no intersection occured.
        **************************************************************************************/
        public static Point2D.Float findLineCircleIntersection( Line2D.Float line, Ellipse2D.Float circle ) //Point p1, Point p2 )
        {
            Point2D.Float p1 = (Point2D.Float)line.getP1();
            Point2D.Float p2 = (Point2D.Float)line.getP2();

            float radius  = (float)circle.getWidth() / 2;
            float circleX = (float)circle.getX() + radius;
            float circleY = (float)circle.getY() + radius;

            //substract line by circle values
            p1.x -= circleX;    p1.y -= circleY;
            p2.x -= circleX;    p2.y -= circleY;

            float dx = p2.x - p1.x;
            float dy = p2.y  - p1.y;
            float dr = (float)Math.sqrt( dx * dx + dy * dy );
            float D  = p1.x * p2.y - p2.x * p1.y;
            float discriminant = radius * radius * dr * dr - D * D;
            if ( discriminant < 0.0f )
            {
                //No intersection;
                return null;
            }
            else if ( discriminant == 0.0f )
            {
                //Tangant line there is only ONE intersection
            }
            else if ( discriminant > 0.0f )
            {
                //The line intersects at TWO points
            }

            //Compute intersection for ONE point (to compute intersection at OTHER point change + to a -)
            float x1 = (float)( ( D  * dy + Math.signum( dy ) * dx * Math.sqrt( discriminant ) ) / ( dr * dr ) );
            float y1 = (float)( ( -D * dx + Math.abs(    dy )      * Math.sqrt( discriminant ) ) / ( dr * dr ) );

            float x2 = (float)( ( D  * dy - Math.signum( dy ) * dx * Math.sqrt( discriminant ) ) / ( dr * dr ) );
            float y2 = (float)( ( -D * dx - Math.abs(    dy )      * Math.sqrt( discriminant ) ) / ( dr * dr ) );

            //translate back
            x1 += circleX;
            y1 += circleY;

            x2 += circleX;
            y2 += circleY;

            //return nearer point to line-point 1
            Point2D.Float ret =
            (
                        line.getP1().distance( new Point2D.Float( x1, y1 ) )
                    <   line.getP1().distance( new Point2D.Float( x2, y2 ) )
                ?   new Point2D.Float( x1, y1 )
                :   new Point2D.Float( x2, y2 )
            );

            return ret;

        }

        /**************************************************************************************
        *   Checks if two line-segments intersect and returns the point of intersection.
        *
        *   @param  line1   Line-segment 1.
        *   @param  line2   Line-segment 2.
        *   @return         The point of intersection.
        *                   <code>null</code> if the lines to not intersect.
        **************************************************************************************/
        public static Point2D.Float findLineSegmentIntersection( Line2D.Float line1, Line2D.Float line2 )
        {
            double[]    intersectionCoords  = new double[ 2 ];
            int         res                 = findLineSegmentIntersection
            (
                line1.getX1(), line1.getY1(),
                line1.getX2(), line1.getY2(),
                line2.getX1(), line2.getY1(),
                line2.getX2(), line2.getY2(),
                intersectionCoords
            );

            //may fail
            if ( res < 1 )
            {
                Debug.fatal( "FATAL!! bug in external class Geometry! no intersection point found!" );
                return null;
            }

            return new Point2D.Float( (float)intersectionCoords[ 0 ], (float)intersectionCoords[ 1 ] );

        }

        /**************************************************************************************
        *   Check if two double precision numbers are "equal", i.e. close enough
        *   to a given limit.
        *
        *   @param  a           First number to check
        *   @param  b           Second number to check
        *   @param  limit       The definition of "equal".
        *   @return             True if the twho numbers are "equal", false otherwise
        **************************************************************************************/
        private static boolean equals (double a, double b, double limit)
        {
            return Math.abs (a - b) < limit;
        }

        /**************************************************************************************
        *   Check if two double precision numbers are "equal", i.e. close enough
        *   to a prespecified limit.
        *
        *   @param  a   First number to check
        *   @param  b   Second number to check
        *   @return     True if the twho numbers are "equal", false otherwise
        **************************************************************************************/
        private static boolean equals (double a, double b)
        {
            return equals (a, b, 1.0e-5);
        }

        /**************************************************************************************
        *   Return smallest of four numbers.
        *
        *   @param  a   First number to find smallest among.
        *   @param  b   Second number to find smallest among.
        *   @param  c   Third number to find smallest among.
        *   @param  d   Fourth number to find smallest among.
        *   @return     Smallest of a, b, c and d.
        **************************************************************************************/
        private static double min (double a, double b, double c, double d)
        {
            return Math.min (Math.min (a, b), Math.min (c, d));
        }

        /**************************************************************************************
        *   Return largest of four numbers.
        *
        *   @param  a   First number to find largest among.
        *   @param  b   Second number to find largest among.
        *   @param  c   Third number to find largest among.
        *   @param  d   Fourth number to find largest among.
        *   @return     Largest of a, b, c and d.
        **************************************************************************************/
        private static double max (double a, double b, double c, double d)
        {
            return Math.max (Math.max (a, b), Math.max (c, d));
        }

        /*********************************************************************************
        *   Compute the length of the line from (x0,y0) to (x1,y1).
        *
        *   @param      x0  First  line end point x.
        *   @param      y0  First  line end point y.
        *   @param      x1  Second line end point x.
        *   @param      y1  Second line end point y.
        *   @return         Length of line from (x0,y0) to (x1,y1).
        ***********************************************************************************/
        private static double length (double x0, double y0, double x1, double y1)
        {
            double dx = x1 - x0;
            double dy = y1 - y0;

            return Math.sqrt (dx*dx + dy*dy);
        }

        /**************************************************************************************
        *   Compute the intersection between two line segments, or two lines
        *   of infinite length.
        *
        *   @param  x0              X coordinate first end point first line segment.
        *   @param  y0              Y coordinate first end point first line segment.
        *   @param  x1              X coordinate second end point first line segment.
        *   @param  y1              Y coordinate second end point first line segment.
        *   @param  x2              X coordinate first end point second line segment.
        *   @param  y2              Y coordinate first end point second line segment.
        *   @param  x3              X coordinate second end point second line segment.
        *   @param  y3              Y coordinate second end point second line segment.
        *   @param  intersection    Preallocated by caller to double[2]
        *   @return -1              if lines are parallel (x,y unset),
        *           -2              if lines are parallel and overlapping (x, y center)
        *           0               if intesrection outside segments (x,y set)
        *          +1               if segments intersect (x,y set)
        **************************************************************************************/
        private static int findLineSegmentIntersection
        (
            double x0, double y0,
            double x1, double y1,
            double x2, double y2,
            double x3, double y3,
            double[] intersection
        )
        {
            // TO DO: Make limit depend on input domain
            final double LIMIT        = 1e-5;
            final double INFINITY = 1e10;

            double x, y;

            //
            // Convert the lines to the form y = ax + b
            //

            // Slope of the two lines
            double a0 = UMathGeometry.equals (x0, x1, LIMIT) ?
                                    INFINITY : (y0 - y1) / (x0 - x1);
            double a1 = UMathGeometry.equals (x2, x3, LIMIT) ?
                                    INFINITY : (y2 - y3) / (x2 - x3);

            double b0 = y0 - a0 * x0;
            double b1 = y2 - a1 * x2;

            // Check if lines are parallel
            if ( UMathGeometry.equals ( a0, a1 ) )
            {
                if (!UMathGeometry.equals (b0, b1))
                    return -1; // Parallell non-overlapping

                if (UMathGeometry.equals (x0, x1)) {
                    if (Math.min (y0, y1) < Math.max (y2, y3) ||
                            Math.max (y0, y1) > Math.min (y2, y3)) {
                        double twoMiddle = y0 + y1 + y2 + y3 -
                                                             UMathGeometry.min (y0, y1, y2, y3) -
                                                             UMathGeometry.max (y0, y1, y2, y3);
                        y = (twoMiddle) / 2.0;
                        x = (y - b0) / a0;
                    }
                    else return -1;    // Parallell non-overlapping
                }
                else {
                    if (Math.min (x0, x1) < Math.max (x2, x3) ||
                            Math.max (x0, x1) > Math.min (x2, x3)) {
                        double twoMiddle = x0 + x1 + x2 + x3 -
                                                             UMathGeometry.min (x0, x1, x2, x3) -
                                                             UMathGeometry.max (x0, x1, x2, x3);
                        x = (twoMiddle) / 2.0;
                        y = a0 * x + b0;
                    }
                    else return -1;
                }

                intersection[0] = x;
                intersection[1] = y;
                return -2;
            }

            // Find correct intersection point
            if (UMathGeometry.equals (a0, INFINITY)) {
                x = x0;
                y = a1 * x + b1;
            }
            else if (UMathGeometry.equals (a1, INFINITY)) {
                x = x2;
                y = a0 * x + b0;
            }
            else {
                x = - (b0 - b1) / (a0 - a1);
                y = a0 * x + b0;
            }

            intersection[0] = x;
            intersection[1] = y;

            // Then check if intersection is within line segments
            double distanceFrom1;

            if (UMathGeometry.equals (x0, x1)) {
                if (y0 < y1)
                    distanceFrom1 = y < y0 ? UMathGeometry.length (x, y, x0, y0) :
                                                    y > y1 ? UMathGeometry.length (x, y, x1, y1) : 0.0;
                else
                    distanceFrom1 = y < y1 ? UMathGeometry.length (x, y, x1, y1) :
                                                    y > y0 ? UMathGeometry.length (x, y, x0, y0) : 0.0;
            }
            else {
                if (x0 < x1)
                    distanceFrom1 = x < x0 ? UMathGeometry.length (x, y, x0, y0) :
                                                    x > x1 ? UMathGeometry.length (x, y, x1, y1) : 0.0;
                else
                    distanceFrom1 = x < x1 ? UMathGeometry.length (x, y, x1, y1) :
                                                    x > x0 ? UMathGeometry.length (x, y, x0, y0) : 0.0;
            }

            double distanceFrom2;

            if ( UMathGeometry.equals ( x2, x3 ) )
            {
                if (y2 < y3)
                    distanceFrom2 = y < y2 ? UMathGeometry.length (x, y, x2, y2) :
                                                    y > y3 ? UMathGeometry.length (x, y, x3, y3) : 0.0;
                else
                    distanceFrom2 = y < y3 ? UMathGeometry.length (x, y, x3, y3) :
                                                    y > y2 ? UMathGeometry.length (x, y, x2, y2) : 0.0;
            }
            else
            {
                if (x2 < x3)
                    distanceFrom2 = x < x2 ? UMathGeometry.length (x, y, x2, y2) :
                                                    x > x3 ? UMathGeometry.length (x, y, x3, y3) : 0.0;
                else
                    distanceFrom2 = x < x3 ? UMathGeometry.length (x, y, x3, y3) :
                                                    x > x2 ? UMathGeometry.length (x, y, x2, y2) : 0.0;
            }

            return
            (
                    UMathGeometry.equals (distanceFrom1, 0.0) && UMathGeometry.equals (distanceFrom2, 0.0)
                ?   1
                :   0
            );
        }

        /**************************************************************************************
        *   Checks if a line-segment intersects the given circle.
        *
        *   @param  line    The line-segment to check for intersection with circle.
        *   @param  circle  The circle. CAUTION! only the circle's width is used as it's radius!
        *   @return         <code>true</code> if the line intersects the circle in one ( tangent )
        *                   or two ( secant ) points. Otherwise <code>false</code>.
        *   @deprecated     can be replaced by native api.
        **************************************************************************************/
        @Deprecated
        public static final boolean isLineIntersectingCircle( Line2D.Float line, Ellipse2D.Float circle )
        {
            //check if the closest distance from the circle's center is lower as it's radius
            return
            (
                line.ptSegDist( new Point2D.Double( circle.getCenterX(), circle.getCenterY() ) ) <= circle.getWidth() / 2
            );
        }
    }
