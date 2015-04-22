/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.fx;

import de.christopherstock.lib.g3d.*;

    /**************************************************************************************
    *   The effect system.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public abstract class FX
    {
        public static enum FXSize
        {
            ESmall,
            EMedium,
            ELarge,
        }

        public static enum FXTime
        {
            EShort,
            EMedium,
            ELong,
        }

        public static enum FXType
        {
            EExplosion,
        }

        protected   static  final   int                 MAX_FX                      = 0;

        protected                   LibVertex              anchor                      = null;

        protected                   FXSize              size                        = null;
        protected                   FXTime              time                        = null;

        public abstract void launch();

        public FX( LibVertex aAnchor )
        {
            anchor = aAnchor;
        }

        public static final void launchTestExplosion()
        {
            FX fx1 = new FXExplosion( new LibVertex( 0.0f, 0.0f, 0.0f ), FXSize.ESmall,  FXTime.EShort  );
            FX fx2 = new FXExplosion( new LibVertex( 2.0f, 2.0f, 0.0f ), FXSize.EMedium, FXTime.EMedium );
            FX fx3 = new FXExplosion( new LibVertex( 4.0f, 4.0f, 0.0f ), FXSize.ELarge,  FXTime.ELong   );

            fx1.launch();
            fx2.launch();
            fx3.launch();

        }
    }
