/*  $Id: LibGeomObject.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public interface LibGeomObject
    {
        public abstract LibVertex getAnchor();
        public abstract boolean checkCollisionHorz( Cylinder c );
        public abstract Vector<Float> checkCollisionVert( Cylinder c );
        public abstract void translate( float tX, float tY, float tZ, LibTransformationMode transformationMode );
        public abstract void setNewAnchor( LibVertex newAnchor, boolean performTranslationOnFaces, LibTransformationMode transformationMode );
    }
