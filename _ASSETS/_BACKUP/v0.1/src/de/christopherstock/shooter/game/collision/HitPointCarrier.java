/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.collision;

    import  de.christopherstock.shooter.gl.g3d.*;

    /**************************************************************************************
    *   Represents a bullet-hole on a wall or an object.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public interface HitPointCarrier
    {
        public abstract Vertex getAnchor();

        public abstract float getCarriersFaceAngle();
    }
