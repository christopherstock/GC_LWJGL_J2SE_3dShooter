/*  $Id: GameObject.java 262 2011-02-10 00:44:00Z jenetic.bytemare $
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
    *   @version    0.3
    **************************************************************************************/
    public interface GameObject
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

        public abstract LibVertex getAnchor();

        public abstract float getCarriersFaceAngle();

        public abstract Vector<HitPoint> launchShot( Shot shot );

        public HitPointCarrier getHitPointCarrier();
    }
