/*  $Id: ShooterGameLevel.java 1261 2013-01-05 00:51:42Z jenetic.bytemare@gmail.com $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.level;

    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.io.sound.*;
    import  de.christopherstock.shooter.level.setup.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   Specifies all tasks for changing a level.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public class LevelChange
    {
        private         static          LevelSetup           levelMainToChangeTo             = null;
        private         static          int                         levelSectionIndexToChangeTo     = -1;
        private         static          boolean                     resetOnLevelChange              = false;
        private         static          long                        levelChangeBlocker              = 0;

        public static final void orderLevelChange( LevelSetup newLevelMain, int newLevelSectionIndex, boolean reset )
        {
            levelMainToChangeTo         = newLevelMain;
            levelSectionIndexToChangeTo = newLevelSectionIndex;
            resetOnLevelChange          = reset;
        }

        public static final void checkChangeToSection()
        {
            if ( levelSectionIndexToChangeTo != -1 )
            {
                if ( levelChangeBlocker <= System.currentTimeMillis() )
                {
                    //change to if possible
                    ShooterDebug.level.out( "change level to [" + levelSectionIndexToChangeTo + "]" );

                    //reset new level if desired
                    if ( resetOnLevelChange )
                    {
                        //init all level data and game levels
                        LevelCurrent.init( levelMainToChangeTo );
                        Level.init();

                        //disable all hud fx
                        HUDFx.disableAllFx();

                        //start according bg music
                        SoundBg.startSound( LevelCurrent.currentLevelConfig.iBgSound );
                    }

                    //show HUD message
                    HUDMessageManager.getSingleton().showMessage( "Changing to level section [" + LevelCurrent.currentSectionConfigData[ levelSectionIndexToChangeTo ].iDescSection + "]" );

                    //change current level - do NOT change to constructor! level is referenced in init() !
                    LevelCurrent.currentSection = LevelCurrent.currentSections[ levelSectionIndexToChangeTo ];

                    //call this AFTER initing level data !!
                    LevelCurrent.currentSection.assignWalls();

                    //suppress quick level change
                    levelChangeBlocker = System.currentTimeMillis() + 100;

                    //set no active level change
                    levelSectionIndexToChangeTo = -1;
                }
            }
        }
    }
