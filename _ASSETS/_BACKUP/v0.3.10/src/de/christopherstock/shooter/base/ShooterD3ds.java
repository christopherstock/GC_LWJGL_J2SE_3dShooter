/*  $Id: ShooterD3ds.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =================================================================================
 */
    package de.christopherstock.shooter.base;

    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.io.d3ds.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.face.*;

    /********************************************************************************
    *   All available Discreet 3D studio max resource files. The ordinal index
    *   of the enum constant is the filename of the ascii scene export file (.ase).
    *   Remember to set the UVW mapping for all ase files being exported
    *   otherwise the texture will not be displayed correctly.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    ********************************************************************************/
    public class ShooterD3ds
    {
        public static interface D3dsFile
        {
            public abstract void initFile( LibD3dsImporter aD3dsFile );
            public abstract LibD3dsImporter getFile();
        }


        public static enum Menu implements D3dsFile
        {
            ELogoIpco,
            ;

            public                      LibD3dsImporter            iD3dsfile                    = null;

            @Override
            public final void initFile( LibD3dsImporter aD3dsfile )
            {
                iD3dsfile = aD3dsfile;
            }

            @Override
            public final LibD3dsImporter getFile()
            {
                return iD3dsfile;
            }
        }

        public static enum Others implements D3dsFile
        {
            EChairOffice1,
            ECrate1,
            EDoor1,
            EDeskOffice1,
            EFloor1x1,
            EFloor2x2,
            EFloor3x3,
            EFloor3x12,
            EFloor5x5,
            EFloor6x6,
            EFloor20x20,
            EFloor100x100,
            EKeyboard1,
            ELevel1PlayersOffice,
            EPoster1,
            EPoster2,
            ERingBook1,
            EScreen1,
            EShelves1,
            ESodaMachine1,
            ESofa1,
            EStairs3x3,
            ESprite1,
            ESprite2,
            ETranquilizerDart,
            EWall1Solid,
            EWall2Solid,
            EWall1WindowSocket,
            EWall1WindowGlass,
            EWall2WindowSocket,
            EWall2WindowGlass,
            EWall2Door,
            EWhiteboard1,
/*
            EHouse10,
            ETestTube,
            ETestFloor,
            ETestSphere,
            ECactus1,
            ECactus2,
            ECar1,
            ECeilingTiles3x10,
            EContainer1,
            EContainer2,
            ECrop1,
            ECross,
            EFence1,
            EGlassDoor1,
            EGlassChestDoor1,
            EHouse1,
            ETree1,
            EForest1,
            EFloorMarble1x1,
            EFloorMarble1x10,
            EFloorMarble3x10,
            ELevelBounds,
            ELevelOffice1,
            ELevelDesert1,
            ELevelDispatch1,
            ELevelParkingLot1,
            ELevelQatar1,
            EExecutiveOffice1,
            EExecutiveOffice1Door1,
            EExecutiveOffice2,
            EExecutiveOffice3,
            ELevelBase1,
            ETable1,
            ETable2,
            ETestFace,
            EPlant1,
            EPalm1,
            EPowerPole,
            ESign1,
            ESign2,
            ERoad1,
            ERock1,
*/
            ;

            public                      LibD3dsImporter            iD3dsfile                    = null;

            @Override
            public final void initFile( LibD3dsImporter aD3dsfile )
            {
                iD3dsfile = aD3dsfile;
            }

            @Override
            public final LibD3dsImporter getFile()
            {
                return iD3dsfile;
            }
        }

        public static enum Bots implements D3dsFile
        {
            EFace,
            EHat,
            EGlasses,
            EHeadFemale1,
            EHeadFemale2,
            EHeadFemale3,
            EHeadMale1,
            ELeftUpperArmMale,
            ERightUpperArmMale,
            ELeftUpperArmFemale,
            ERightUpperArmFemale,
            ERightUpperLegFemale,
            ELeftUpperLegFemale,
            ERightLowerLegFemale,
            ELeftLowerLegFemale,
            ELeftLowerArmFemale,
            ELeftLowerArmMale,
            ERightLowerArmFemale,
            ERightLowerArmMale,
            ERightUpperLegMale,
            ELeftUpperLegMale,
            ELeftLowerLegMale,
            ERightLowerLegMale,
            ETorsoMale1,
            ETorsoFemale1,
            ENeck,
            ERightHand,
            ELeftHand,
            ERightFoot,
            ELeftFoot,
            ;

            public                      LibD3dsImporter            iD3dsfile                    = null;

            @Override
            public final void initFile( LibD3dsImporter aD3dsfile )
            {
                iD3dsfile = aD3dsfile;
            }

            @Override
            public final  LibD3dsImporter getFile()
            {
                return iD3dsfile;
            }
        }

        public static enum Items implements D3dsFile
        {
            EAmmoBullet44mm,
            EAmmoBullet51mm,
            EAmmoBullet792mm,
            EAmmoBulletMagnum,
            EAmmoShotgunShell,
            EAmmoBullet9mm,
            EPistol1,
            EKnife,
            EShotgun1,
            ECrackers,
            EApple,
            ;

            public                      LibD3dsImporter            iD3dsfile                    = null;

            @Override
            public final void initFile( LibD3dsImporter aD3dsfile )
            {
                iD3dsfile = aD3dsfile;
            }

            @Override
            public final LibD3dsImporter getFile()
            {
                return iD3dsfile;
            }
        }

        /**************************************************************************************
        *   Returns a COPY of an imported 3dsmax mesh.
        **************************************************************************************/
        public static final FaceTriangle[] getFaces( D3dsFile file )
        {
            //copy original faces
            LibMaxTriangle[] originalFaces = file.getFile().getFaces();
            FaceTriangle[]   copiedFaces   = new FaceTriangle[ originalFaces.length ];
            for ( int i = 0; i < copiedFaces.length; ++i )
            {
                copiedFaces[ i ] = new FaceTriangle( originalFaces[ i ] );
            }
            return copiedFaces;
        }

        /**************************************************************************************
        *   Init all 3dsmax-files.
        **************************************************************************************/
        public static final void init( LibDebug aDebug )
        {
            for ( D3dsFile file : Bots.values() )
            {
                file.initFile( new LibD3dsImporter( ShooterSettings.Path.E3dsMaxBot.iUrl + file.toString() + LibExtension.ase.getSpecifier(), aDebug ) );
            }

            for ( D3dsFile file : Others.values() )
            {
                file.initFile( new LibD3dsImporter( ShooterSettings.Path.E3dsMaxOther.iUrl + file.toString() + LibExtension.ase.getSpecifier(), aDebug ) );
            }

            for ( D3dsFile file : Items.values() )
            {
                file.initFile( new LibD3dsImporter( ShooterSettings.Path.E3dsMaxItem.iUrl + file.toString() + LibExtension.ase.getSpecifier(), aDebug ) );
            }

            for ( D3dsFile file : Menu.values() )
            {
                file.initFile( new LibD3dsImporter( ShooterSettings.Path.E3dsMaxMenu.iUrl + file.toString() + LibExtension.ase.getSpecifier(), aDebug ) );
            }


        }
    }
