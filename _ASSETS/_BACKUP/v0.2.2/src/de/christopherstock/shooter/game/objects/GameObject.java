/*  $Id: GameObject.java 189 2010-12-13 20:02:19Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.game.collision.*;

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

        public abstract LibVertex getAnchor();

        public abstract float getCarriersFaceAngle();

        public abstract Vector<HitPoint> launchShot( Shot shot );

        public GameObject.HitPointCarrier getHitPointCarrier()
        {
            return carrier;
        }
    }
