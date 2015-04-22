/*  $Id: ViewSet.java 187 2010-11-26 18:27:44Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    /**************************************************************************************
    *   Represents a camera adjustment in 3d space.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class ViewSet
    {
        public          float                       posX               = 0;
        public          float                       posY               = 0;
        public          float                       posZ               = 0;

        public          float                       rotX               = 0;
        public          float                       rotY               = 0;
        public          float                       rotZ               = 0;

        public ViewSet( float aPosX, float aPosY, float aPosZ, float aRotX, float aRotY, float aRotZ )
        {
            posX = aPosX;
            posY = aPosY;
            posZ = aPosZ;

            rotX = aRotX;
            rotY = aRotY;
            rotZ = aRotZ;
        }
    }
