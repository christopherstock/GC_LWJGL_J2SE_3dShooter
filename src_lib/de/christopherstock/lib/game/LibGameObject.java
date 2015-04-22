/*  $Id: LibGameObject.java 1240 2013-01-02 14:43:47Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.lib.game;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public abstract interface LibGameObject
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
        public abstract void launchAction( LibCylinder aCylinder, Object gadget, float faceAngle );
    }
