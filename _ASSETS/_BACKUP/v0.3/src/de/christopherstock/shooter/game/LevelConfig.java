/*  $Id: LevelConfig.java 270 2011-02-11 19:11:49Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  de.christopherstock.shooter.ShooterSettings.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.Item.ItemTemplate;

    public enum LevelConfig
    {
        ELevel1
        (
            new ViewSet( 1.0f, 10.0f, PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 270.0f ),
            BackGround.ENight1,
            new Item[]
            {
                new Item( ItemTemplate.EAmmoBullet9mm,     1.0f, 1.0f,  0.0f + 0.01f ),
                new Item( ItemTemplate.EAmmoShotgunShells, 1.0f, 2.5f,  0.0f + 0.01f ),
                new Item( ItemTemplate.EWearponPistol9mm,  1.0f, 4.0f,  0.0f + 0.01f ),
                new Item( ItemTemplate.EWearponShotgun,    1.0f, 5.5f,  0.0f + 0.01f ),
                new Item( ItemTemplate.EItemBottle1,       2.0f, 4.0f,  0.0f + 0.01f ),
/*
                new Item( ItemPreset.EAmmoBullet9mm,     2.0f, 1.0f,  0.0f + 0.01f ),
                new Item( ItemPreset.EAmmoShotgunShells, 2.0f, 2.5f,  0.0f + 0.01f ),
                new Item( ItemPreset.EWearponPistol9mm,  2.0f, 4.0f,  0.0f + 0.01f ),
                new Item( ItemPreset.EWearponShotgun,    2.0f, 5.5f,  0.0f + 0.01f ),

                new Item( ItemPreset.EAmmoBullet9mm,     3.0f, 1.0f,  0.0f + 0.01f ),
                new Item( ItemPreset.EAmmoShotgunShells, 3.0f, 2.5f,  0.0f + 0.01f ),
                new Item( ItemPreset.EWearponPistol9mm,  3.0f, 4.0f,  0.0f + 0.01f ),
                new Item( ItemPreset.EWearponShotgun,    3.0f, 5.5f,  0.0f + 0.01f ),

                new Item( ItemPreset.EAmmoBullet9mm,     4.0f, 1.0f,  0.0f + 0.01f ),
                new Item( ItemPreset.EAmmoShotgunShells, 4.0f, 2.5f,  0.0f + 0.01f ),
                new Item( ItemPreset.EWearponPistol9mm,  4.0f, 4.0f,  0.0f + 0.01f ),
                new Item( ItemPreset.EWearponShotgun,    4.0f, 5.5f,  0.0f + 0.01f ),
*/
            }
        ),
        ELevel2( new ViewSet( 2.0f, 2.0f,  PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 270.0f ), BackGround.EMeadow,   null ),
        ELevel3( new ViewSet( 5.0f, 5.0f,  PlayerAttributes.DEPTH_TOE, 0.0f, 0.0f, 180.0f ), BackGround.EDispatch, null ),
        ;

        protected               ViewSet             iStartPosition      = null;
        protected               BackGround          iBg                 = null;
        protected               Item[]              iItems              = null;

        private LevelConfig( ViewSet aStartPosition, BackGround aBg, Item[] aItems )
        {
            iStartPosition  = aStartPosition;
            iBg             = aBg;
            iItems          = aItems;
        }
    }
