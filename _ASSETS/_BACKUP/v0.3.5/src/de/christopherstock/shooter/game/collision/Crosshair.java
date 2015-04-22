/*  $Id: Shot.java 751 2011-05-15 22:26:52Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  javax.vecmath.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.game.fx.*;

    /**************************************************************************************
    *   All calculations for one shot.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class Crosshair
    {
        public                      Point3d             iSrcPoint3d                 = null;
        public                      float               iRotX                       = 0.0f;
        public                      float               iRotZ                       = 0.0f;

        public Crosshair( float startX, float startY, float startZ, float rotZ, float rotX )
        {
            iSrcPoint3d     = new Point3d
            (
                startX,
                startY,
                startZ
            );

            iRotZ = rotZ;
            iRotX = rotX;
        }

        public final void draw()
        {
            //if ( true ) return;

            //draw shot line with own calculations
            for ( float distance = 5.0f; distance < 10.0f; distance += 0.15f )
            {
                FX.launchStaticPoint
                (
                    new LibVertex
                    (
                        (float)( iSrcPoint3d.x - LibMath.sinDeg( iRotZ ) * distance ),
                        (float)( iSrcPoint3d.y - LibMath.cosDeg( iRotZ ) * distance ),
                        (float)( iSrcPoint3d.z - LibMath.sinDeg( iRotX ) * distance )
                    ),
                    LibColors.ECrosshair,
                    0,
                    0.01f //DebugSettings.DEBUG_POINT_SIZE
                );
            }
        }
    }
