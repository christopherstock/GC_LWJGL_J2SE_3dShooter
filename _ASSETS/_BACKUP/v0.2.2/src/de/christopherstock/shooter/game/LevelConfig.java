/*  $Id: LevelConfig.java 190 2010-12-13 20:12:09Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  de.christopherstock.shooter.ShooterSettings.*;
    import  de.christopherstock.shooter.g3d.*;

    public enum LevelConfig
    {
        ELevel1( new ViewSet( 1.0f, 10.0f, PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 270.0f ), BackGround.EDesert   ),
        ELevel2( new ViewSet( 2.0f, 2.0f,  PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 270.0f ), BackGround.EMeadow   ),
        ELevel3( new ViewSet( 5.0f, 5.0f,  PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 180.0f ), BackGround.EDispatch ),
        ;

        protected               ViewSet             iStartPosition      = null;
        protected               BackGround          iBg                 = null;

        private LevelConfig( ViewSet aStartPosition, BackGround aBg )
        {
            iStartPosition  = aStartPosition;
            iBg             = aBg;
        }
    }
