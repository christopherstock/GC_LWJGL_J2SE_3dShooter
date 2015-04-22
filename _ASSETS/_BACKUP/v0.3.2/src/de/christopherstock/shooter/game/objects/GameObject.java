/*  $Id: GameObject.java 542 2011-04-13 14:47:36Z jenetic.bytemare $
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
    *   @version    0.3.2
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
