/*  $Id: LibGameObject.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.lib.game;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.g3d.Cylinder;
    import  de.christopherstock.shooter.game.wearpon.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public interface LibGameObject
    {
        public static enum HitPointCarrier
        {
            EWall,
            EPlayer,
            EBot,
        }

        public abstract LibVertex getAnchor();
        public abstract HitPointCarrier getHitPointCarrier();
        public abstract float getCarriersFaceAngle();
        public abstract Vector<LibHitPoint> launchShot( LibShot shot );
        public abstract void launchAction( Cylinder aCylinder, Gadget gadget, float faceAngle );
    }
