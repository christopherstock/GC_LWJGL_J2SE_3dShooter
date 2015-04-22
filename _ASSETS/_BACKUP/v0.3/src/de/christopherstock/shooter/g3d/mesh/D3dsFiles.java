/*  $Id: D3dsImporter.java 202 2011-01-09 17:41:33Z jenetic.bytemare $
 *  =================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.io.d3ds.*;
    import  de.christopherstock.shooter.g3d.*;

    /********************************************************************************
    *   All available Discreet 3D studio max resource files. The ordinal index
    *   of the enum constant is the filename of the ascii scene export file (.ase).
    *   Remember to set the UVW mapping for all ase files being exported.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    ********************************************************************************/
    public enum D3dsFiles
    {
        ECar1,
        ECar2,
        ECar3,

        EDesk1,

        EExecutiveOffice1GlassDoor,


        EItemPistol1,
        EItemShotgun,

        EItemAmmoShotgunShells,
        EItemAmmoBullet9mm,
        EItemBottle1,


        ELevelDesert1,
        ELevelDispatch1,
        ELevelParkingLot1,

        EExecutiveOffice1,
        EExecutiveOffice1Door1,

        EPlant1,

        EShelves1,

        EWoman1head,
        EWoman1torso,
        EWoman1RightUpperArm,
        EWoman1LeftUpperArm,
        EWoman1RightLowerArm,
        EWoman1LeftLowerArm,
        EWoman1RightHand,
        EWoman1LeftHand,
        EWoman1RightUpperLeg,
        EWoman1LeftUpperLeg,
        EWoman1RightLowerLeg,
        EWoman1LeftLowerLeg,
        EWoman1RightFoot,
        EWoman1LeftFoot,

        ;

        public                      LibD3dsImporter            d3dsfile                    = null;

        /**************************************************************************************
        *   Returns a COPY of an imported 3dsmax mesh.
        **************************************************************************************/
        public static final Mesh getFaces( D3dsFiles file )
        {
            //copy original faces
            LibMaxTriangle[] originalFaces = file.d3dsfile.getFaces();
            FreeTriangle[]   copiedFaces   = new FreeTriangle[ originalFaces.length ];
            for ( int i = 0; i < copiedFaces.length; ++i )
            {
                copiedFaces[ i ] = new FreeTriangle( originalFaces[ i ] );
            }
            return new Mesh( copiedFaces );
        }

        /**************************************************************************************
        *   Init all 3dsmax-files.
        **************************************************************************************/
        public static final void init( LibDebug aDebug, String aPath )
        {
            for ( D3dsFiles file : values() )
            {
                file.d3dsfile = new LibD3dsImporter( aPath + file.toString() + LibExtension.ase.getSpecifier(), aDebug );
            }
        }
    }
