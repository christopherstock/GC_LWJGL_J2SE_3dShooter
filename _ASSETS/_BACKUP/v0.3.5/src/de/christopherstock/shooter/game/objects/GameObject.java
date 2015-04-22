/*  $Id: GameObject.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
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
    *   @version    0.3.5
    **************************************************************************************/
    public interface GameObject
    {
        public enum WallAction
        {
            ENone,
            EDoorSliding,
            EDoorTurning,
            ESprite,
            ;
        }

        public enum WallDestroyable
        {
            ENo,
            EYes,
            ;
        }

        public enum WallClimbable
        {
            ENo,
            EYes,
            ;
        }

        public static enum HitPointCarrier
        {
            EWall,
            EPlayer,
            EBot,
        }

        public static enum WallCollidable
        {
            EYes,
            ENo,
            ;
        }

        public abstract LibVertex getAnchor();

        public abstract float getCarriersFaceAngle();

        public abstract Vector<HitPoint> launchShot( Shot shot );

        public HitPointCarrier getHitPointCarrier();
    }
