/*  $Id: GeomObject.java 191 2010-12-13 20:24:11Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.game.collision.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public abstract class GeomObject
    {
        protected           LibVertex              anchor                          = null;

        public final LibVertex getAnchor()
        {
            return anchor;
        }

        public abstract Vector<HitPoint> launchShot( Shot shot );

        public abstract boolean checkCollision( Cylinder c );


    }
