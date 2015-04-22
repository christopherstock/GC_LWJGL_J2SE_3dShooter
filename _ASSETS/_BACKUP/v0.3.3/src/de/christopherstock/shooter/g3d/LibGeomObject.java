/*  $Id: LibGeomObject.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   Represents a cylinder.
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public interface LibGeomObject
    {
        public abstract LibVertex getAnchor();

        public abstract boolean checkCollision( Cylinder c );

        public abstract void translate( float tX, float tY, float tZ, LibTransformationMode transformationMode );

        public abstract void setNewAnchor( LibVertex newAnchor, boolean performTranslationOnFaces, LibTransformationMode transformationMode );
    }
