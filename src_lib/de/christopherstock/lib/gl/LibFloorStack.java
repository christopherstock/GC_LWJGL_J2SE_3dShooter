/*  $Id: Wall.java 1247 2013-01-02 20:04:58Z jenetic.bytemare@gmail.com $
 *  =======================================================================================
 */
    package de.christopherstock.lib.gl;

    import  de.christopherstock.lib.LibDebug;
    import  de.christopherstock.lib.g3d.LibVertex;
    import  de.christopherstock.lib.game.LibGameObject;

    /**************************************************************************************
    *   Implemented by an Object to determine if it's climbable or not. 
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public interface LibFloorStack
    {
        public abstract Float getHighestFloor( LibGameObject aParentGameObject, LibVertex aAnchor, float aRadius, float aHeight, int aCollisionCheckingSteps, LibDebug aDebug, boolean aDebugDrawBotCircles, float aBottomCollisionToleranceZ, float aMinBottomCollisionToleranceZ, int aEllipseSegments, Object exclude );
    }
