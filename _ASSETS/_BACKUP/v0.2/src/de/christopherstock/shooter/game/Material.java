/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import de.christopherstock.shooter.gl.*;

    /**************************************************************************************
    *   The Material is important for bullet holes.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum Material
    {
        //temporal state
        EUndefined,
        EConcrete,
        EWood,
        ESteel,
        EGlass,
        ;
/*
        //tight initializing :(
        protected           Texture                 bulletHoleTexture           = null;

        private Material( Texture aBulletHoleTexture )
        {
            bulletHoleTexture = aBulletHoleTexture;
        }
*/
        public final Texture getBulletHoleTexture()
        {
            //gnarly :(
            switch ( this )
            {
                case EUndefined:    {   return Texture.EBulletHoleSteel;        }
                case EConcrete:     {   return Texture.EBulletHoleConcrete;     }
                case EWood:         {   return Texture.EBulletHoleWood;         }
                case ESteel:        {   return Texture.EBulletHoleSteel;        }
                case EGlass:        {   return Texture.EBulletHoleConcrete;     }
                default:            {   return null;                            }
            }
        }
    }
