/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.util.*;

import de.christopherstock.lib.g3d.*;
import  de.christopherstock.shooter.game.collision.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.2
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
