/*  $Id: GeomObject.java 431 2011-03-19 17:48:24Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public interface LibGeomObject
    {
        public abstract LibVertex getAnchor();

        public abstract boolean checkCollision( Cylinder c );

        public abstract void translate( float tX, float tY, float tZ, LibTransformationMode transformationMode );

        public abstract void setNewAnchor( LibVertex newAnchor, boolean performTranslationOnFaces, LibTransformationMode transformationMode );
    }
