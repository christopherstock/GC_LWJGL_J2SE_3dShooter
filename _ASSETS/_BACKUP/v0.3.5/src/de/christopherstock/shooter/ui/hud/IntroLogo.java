/*  $Id: IntroLogo.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui.hud;

    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.Lib.Scalation;
    import  de.christopherstock.lib.Lib.ViewSet;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.shooter.base.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.g3d.face.LibFace.DrawMethod;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.objects.GameObject.WallAction;
    import  de.christopherstock.shooter.game.objects.GameObject.WallClimbable;
    import  de.christopherstock.shooter.game.objects.GameObject.WallCollidable;
    import  de.christopherstock.shooter.game.objects.GameObject.WallDestroyable;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class IntroLogo
    {
        private         static  final   float                   SPEED_ROTATION                  = 2.5f;

        private         static          IntroLogo               singleton                       = null;

        private                         Wall                    logo                            = null;
        private                         float                   z                               = 0.0f;

        private IntroLogo()
        {
            logo  = new Wall(   Menu.ELogoIpco,             new LibVertex( 0.0f,        12.5f,   0.0f    ),  0.0f, Scalation.ENone,  WallCollidable.EYes,  WallAction.ENone, WallDestroyable.ENo, WallClimbable.EYes, DrawMethod.EAlwaysDraw         );
        }

        public final void draw3D()
        {
        }

        public static final IntroLogo getSingleton()
        {
            if ( singleton == null )
            {
                singleton = new IntroLogo();
            }

            return singleton;
        }

        public final void drawIntroLogo()
        {
            //set global camera
            LibGL3D.view.setCamera( new ViewSet( 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 180.0f ) );

            //enable light
            LibGL3D.view.setLightsOn();

            //draw logo
            logo.draw();

            //disable lights
            LibGL3D.view.setLightsOff();
        }

        public final void animate()
        {
            logo.translateAndRotateXYZ( 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, z, null, LibTransformationMode.EOriginalsToTransformed );
            z += SPEED_ROTATION;
        }
    }
