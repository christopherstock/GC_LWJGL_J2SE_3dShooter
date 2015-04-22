/*  $Id: ShooterLevelCurrent.java 1264 2013-01-06 16:55:45Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.level;

    import  java.util.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.level.setup.*;

    /**************************************************************************************
    *   Stores all information for the current active level set.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public abstract class LevelCurrent
    {
        protected   static          LevelConfigMain          currentLevelConfig              = null;
        public      static          WallCollection[]                currentGlobalMeshData           = null;
        protected   static          WallCollection[][]              currentSectionMeshData          = null;
        protected   static          LevelConfigSection[]     currentSectionConfigData        = null;

        public      static          LevelSetup               currentLevelMain                = null;
        protected   static          Level[]                  currentSections                 = null;
        protected   static          Level                    currentSection                  = null;

        public static final void init( LevelSetup levelToStart )
        {
            //create new instances and store them
            currentLevelConfig          = levelToStart.createNewLevelConfig();
            currentGlobalMeshData       = levelToStart.createNewGlobalMeshData();
            currentSectionMeshData      = levelToStart.createNewSectionMeshData();
            currentSectionConfigData    = levelToStart.createNewSectionConfigData();
        }

        public static final WallCollection[] getLevelWalls( int newSectionIndex )
        {
            //pick all walls from global data and current section
            Vector<WallCollection> levelWalls = new Vector<WallCollection>();
            levelWalls.addAll( Arrays.asList( currentGlobalMeshData ) );
            levelWalls.addAll( Arrays.asList( currentSectionMeshData[ newSectionIndex ] ) );
            return levelWalls.toArray( new WallCollection[] {} );
        }
    }
