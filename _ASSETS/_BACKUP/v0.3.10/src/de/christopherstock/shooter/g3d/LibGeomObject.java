/*  $Id: LibGeomObject.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  java.util.*;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public interface LibGeomObject
    {
        public abstract LibVertex getAnchor();
        public abstract boolean checkCollisionHorz( Cylinder c );
        public abstract Vector<Float> checkCollisionVert( Cylinder c );
        public abstract void translate( float tX, float tY, float tZ, LibTransformationMode transformationMode );
        public abstract void setNewAnchor( LibVertex newAnchor, boolean performTranslationOnFaces, LibTransformationMode transformationMode );
    }
