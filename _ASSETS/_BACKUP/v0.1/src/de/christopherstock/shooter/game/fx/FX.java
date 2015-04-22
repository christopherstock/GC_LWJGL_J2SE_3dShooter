/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

    /**************************************************************************************
    *   The effect system.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public abstract class FX
    {
        protected   static  final   int                 MAX_FX                      = 0;

        protected                   float               x                           = 0;
        protected                   float               y                           = 0;
        protected                   float               z                           = 0;

        protected                   FXSize              size                        = null;
        protected                   FXTime              time                        = null;

        public abstract void launch();

        public static final void launchTestExplosion()
        {
            FX fx1 = new FXExplosion( 0.0f, 0.0f, 0.0f, FXSize.ESmall,  FXTime.EShort  );
            FX fx2 = new FXExplosion( 2.0f, 2.0f, 0.0f, FXSize.EMedium, FXTime.EMedium );
            FX fx3 = new FXExplosion( 4.0f, 4.0f, 0.0f, FXSize.ELarge,  FXTime.ELong   );

            fx1.launch();
            fx2.launch();
            fx3.launch();

        }
    }
