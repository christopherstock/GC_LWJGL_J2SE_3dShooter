/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl;

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
        public final GLTexture getBulletHoleTexture()
        {
            switch ( this )
            {
                case EUndefined:    {   return GLTexture.EBulletHoleSteel;          }
                case EConcrete:     {   return GLTexture.EBulletHoleConcrete;       }
                case EWood:         {   return GLTexture.EBulletHoleWood;           }
                case ESteel:        {   return GLTexture.EBulletHoleSteel;          }
                case EGlass:        {   return GLTexture.EBulletHoleGlass;          }
                default:            {   return null;                                }
            }
        }
    }
