/*  $Id: Mesh.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import java.util.*;

import de.christopherstock.shooter.g3d.*;
import de.christopherstock.shooter.game.collision.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public abstract class GameObject
    {
        public enum WallAction
        {
            ENone,
            EDoorSliding,
            EDoorTurning,
            ;
        }

        public static enum HitPointCarrier
        {
            EWall,
            EPlayer,
            EBot,
        }

        private         HitPointCarrier     carrier         = null;

        public GameObject( GameObject.HitPointCarrier aCarrier )
        {
            carrier = aCarrier;
        }

        public abstract Vertex getAnchor();

        public abstract float getCarriersFaceAngle();

        public abstract Vector<HitPoint> launchShot( Shot shot );

        public GameObject.HitPointCarrier getHitPointCarrier()
        {
            return carrier;
        }
    }
