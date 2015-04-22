/*  $Id: GLMaterial.java 192 2010-12-13 22:25:43Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

import de.christopherstock.shooter.game.*;

    /**************************************************************************************
    *   The Material is important for bullet holes.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum GLMaterial
    {
        //temporal state
        EUndefined,
        EConcrete,
        EWood,
        ESteel,
        EGlass,
        ;
/*
        //impossible since tight initializing!
        protected           Texture                 bulletHoleTexture           = null;

        private Material( Texture aBulletHoleTexture )
        {
            bulletHoleTexture = aBulletHoleTexture;
        }
*/
        public final Texture getBulletHoleTexture()
        {
            switch ( this )
            {
                case EUndefined:    {   return Texture.EBulletHoleSteel1;          }
                case EConcrete:     {   return Texture.EBulletHoleConcrete1;       }
                case EWood:         {   return Texture.EBulletHoleWood1;           }
                case ESteel:        {   return Texture.EBulletHoleSteel1;          }
                case EGlass:        {   return Texture.EBulletHoleGlass1;          }
                default:            {   return null;                                }
            }
        }
    }
