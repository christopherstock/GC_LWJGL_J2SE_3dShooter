/*  $Id: Path.java 181 2010-11-13 13:31:37Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.ShooterSettings.*;

    /**************************************************************************************
    *   Valid mesh combinations
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public enum ShooterBotTemplate
    {
        EPilotMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            Bots.EGlasses,              ShooterTexture.EGlass1,                 ShooterTexture.EChrome1,
            Bots.EHat,                  ShooterTexture.EClothBlue1,
            Bots.EHeadMale1,            ShooterTexture.EHairBlack,
            Bots.EFace,                 ShooterTexture.EFaceMale1EyesOpen,      ShooterTexture.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    ShooterTexture.EClothBlueStar1,
            Bots.ERightLowerArmMale,    ShooterTexture.EClothBlueYellowStripes1,
            Bots.ETorsoMale1,           ShooterTexture.EClothBlueSuitFront1,    ShooterTexture.EClothBlueSuitTrousers1,
            Bots.ENeck,                 ShooterTexture.ESkin3,
            Bots.ELeftUpperArmMale,     ShooterTexture.EClothBlueStar1,
            Bots.ELeftLowerArmMale,     ShooterTexture.EClothBlueYellowStripes1,
            Bots.ERightHand,            ShooterTexture.EHand1,
            Bots.ELeftHand,             ShooterTexture.EHand1,
            Bots.ERightUpperLegMale,    ShooterTexture.EClothBlue1,
            Bots.ELeftUpperLegMale,     ShooterTexture.EClothBlue1,
            Bots.ERightLowerLegMale,    ShooterTexture.EClothBlueYellowStripes1,
            Bots.ELeftLowerLegMale,     ShooterTexture.EClothBlueYellowStripes1,
            Bots.ERightFoot,            ShooterTexture.EShoeBrown1,
            Bots.ELeftFoot,             ShooterTexture.EShoeBrown1
        ),

        EPilotFemale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                       null,
            null,                       null,
            Bots.EHeadFemale2,          ShooterTexture.EHairBlonde,
            Bots.EFace,                 ShooterTexture.EFaceFemale1EyesOpen,        ShooterTexture.EFaceFemale1EyesShut,
            Bots.ERightUpperArmFemale,  ShooterTexture.EClothBlueStar1,
            Bots.ERightLowerArmFemale,  ShooterTexture.EClothBlueYellowStripes1,
            Bots.ETorsoFemale1,         ShooterTexture.EClothBlueSuitFront1,        ShooterTexture.EClothBlueSuitTrousers1,
            Bots.ENeck,                 ShooterTexture.ESkin1,
            Bots.ELeftUpperArmFemale,   ShooterTexture.EClothBlueStar1,
            Bots.ELeftLowerArmFemale,   ShooterTexture.EClothBlueYellowStripes1,
            Bots.ERightHand,            ShooterTexture.EHand1,
            Bots.ELeftHand,             ShooterTexture.EHand1,
            Bots.ERightUpperLegFemale,  ShooterTexture.EClothBlue1,
            Bots.ELeftUpperLegFemale,   ShooterTexture.EClothBlue1,
            Bots.ERightLowerLegFemale,  ShooterTexture.EClothBlueYellowStripes1,
            Bots.ELeftLowerLegFemale,   ShooterTexture.EClothBlueYellowStripes1,
            Bots.ERightFoot,            ShooterTexture.EShoeBlack1,
            Bots.ELeftFoot,             ShooterTexture.EShoeBlack1
        ),

        ESpecialForcesMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,

            null,                       null,                                   null,
            Bots.EHat,                  ShooterTexture.EClothCamouflageBlue,
            Bots.EHeadMale1,            ShooterTexture.EHairBlonde,
            Bots.EFace,                 ShooterTexture.EFaceMale1EyesOpen,      ShooterTexture.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    ShooterTexture.EClothCamouflageBlue,
            Bots.ERightLowerArmMale,    ShooterTexture.EClothCamouflageBlue,
            Bots.ETorsoMale1,           ShooterTexture.EClothCamouflageBlue,    ShooterTexture.EClothCamouflageBlue,
            Bots.ENeck,                 ShooterTexture.ESkin3,
            Bots.ELeftUpperArmMale,     ShooterTexture.EClothCamouflageBlue,
            Bots.ELeftLowerArmMale,     ShooterTexture.EClothCamouflageBlue,
            Bots.ERightHand,            ShooterTexture.EHand1,
            Bots.ELeftHand,             ShooterTexture.EHand1,
            Bots.ERightUpperLegMale,    ShooterTexture.EClothCamouflageBlue,
            Bots.ELeftUpperLegMale,     ShooterTexture.EClothCamouflageBlue,
            Bots.ERightLowerLegMale,    ShooterTexture.EClothCamouflageBlue,
            Bots.ELeftLowerLegMale,     ShooterTexture.EClothCamouflageBlue,
            Bots.ERightFoot,            ShooterTexture.EShoeBlack1,
            Bots.ELeftFoot,             ShooterTexture.EShoeBlack1
        ),

        ESecurityMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                   null,
            null,                       null,
            Bots.EHeadMale1,            ShooterTexture.EHairBlonde,
            Bots.EFace,                 ShooterTexture.EFaceMale1EyesOpen,      ShooterTexture.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    ShooterTexture.EClothSecurityLogo,
            Bots.ERightLowerArmMale,    ShooterTexture.ESkin3,
            Bots.ETorsoMale1,           ShooterTexture.ETorsoSecurity,          ShooterTexture.EClothSecurity,
            Bots.ENeck,                 ShooterTexture.ESkin3,
            Bots.ELeftUpperArmMale,     ShooterTexture.EClothSecurityLogo,
            Bots.ELeftLowerArmMale,     ShooterTexture.ESkin3,
            Bots.ERightHand,            ShooterTexture.EClothSecurity,
            Bots.ELeftHand,             ShooterTexture.EClothSecurity,
            Bots.ERightUpperLegMale,    ShooterTexture.EClothSecurity,
            Bots.ELeftUpperLegMale,     ShooterTexture.EClothSecurity,
            Bots.ERightLowerLegMale,    ShooterTexture.EClothSecurity,
            Bots.ELeftLowerLegMale,     ShooterTexture.EClothSecurity,
            Bots.ERightFoot,            ShooterTexture.EShoeBlack1,
            Bots.ELeftFoot,             ShooterTexture.EShoeBlack1
        ),

        ESecurityFemale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                   null,
            null,                       null,
            Bots.EHeadFemale1,          ShooterTexture.EHairBlack,
            Bots.EFace,                 ShooterTexture.EFaceFemale3EyesOpen,    ShooterTexture.EFaceFemale3EyesShut,
            Bots.ERightUpperArmFemale,  ShooterTexture.EClothSecurityLogo,
            Bots.ERightLowerArmFemale,  ShooterTexture.ESkin1,
            Bots.ETorsoFemale1,         ShooterTexture.ETorsoSecurity,          ShooterTexture.EClothSecurity,
            Bots.ENeck,                 ShooterTexture.ESkin1,
            Bots.ELeftUpperArmFemale,   ShooterTexture.EClothSecurityLogo,
            Bots.ELeftLowerArmFemale,   ShooterTexture.ESkin1,
            Bots.ERightHand,            ShooterTexture.EClothSecurity,
            Bots.ELeftHand,             ShooterTexture.EClothSecurity,
            Bots.ERightUpperLegFemale,  ShooterTexture.EClothSecurity,
            Bots.ELeftUpperLegFemale,   ShooterTexture.EClothSecurity,
            Bots.ERightLowerLegFemale,  ShooterTexture.ESkin1,
            Bots.ELeftLowerLegFemale,   ShooterTexture.ESkin1,
            Bots.ERightFoot,            ShooterTexture.EShoeBlack1,
            Bots.ELeftFoot,             ShooterTexture.EShoeBlack1
        ),

        ;

        public              float                   iRadius             = 0.0f;
        public              float                   iHeight             = 0.0f;

        public              Bots                    iHat                = null;
        public              Bots                    iGlasses            = null;
        public              Bots                    iHead               = null;
        public              Bots                    iFace               = null;
        public              Bots                    iRightUpperArm      = null;
        public              Bots                    iRightLowerArm      = null;
        public              Bots                    iTorso              = null;
        public              Bots                    iNeck               = null;
        public              Bots                    iLeftUpperArm       = null;
        public              Bots                    iLeftLowerArm       = null;
        public              Bots                    iRightHand          = null;
        public              Bots                    iLeftHand           = null;
        public              Bots                    iRightUpperLeg      = null;
        public              Bots                    iLeftUpperLeg       = null;
        public              Bots                    iRightLowerLeg      = null;
        public              Bots                    iLeftLowerLeg       = null;
        public              Bots                    iRightFoot          = null;
        public              Bots                    iLeftFoot           = null;

        public              ShooterTexture          iTexGlassesGlass    = null;
        public              ShooterTexture          iTexGlassesHolder   = null;
        public              ShooterTexture          iTexHat             = null;
        public              ShooterTexture          iTexFaceEyesOpen    = null;
        public              ShooterTexture          iTexFaceEyesShut    = null;
        public              ShooterTexture          iTexHair            = null;
        public              ShooterTexture          iTexRightUpperArm   = null;
        public              ShooterTexture          iTexRightLowerArm   = null;
        public              ShooterTexture          iTexTorsoUpper      = null;
        public              ShooterTexture          iTexTorsoLower      = null;
        public              ShooterTexture          iTexNeck            = null;
        public              ShooterTexture          iTexLeftUpperArm    = null;
        public              ShooterTexture          iTexLeftLowerArm    = null;
        public              ShooterTexture          iTexRightHand       = null;
        public              ShooterTexture          iTexLeftHand        = null;
        public              ShooterTexture          iTexRightUpperLeg   = null;
        public              ShooterTexture          iTexLeftUpperLeg    = null;
        public              ShooterTexture          iTexRightLowerLeg   = null;
        public              ShooterTexture          iTexLeftLowerLeg    = null;
        public              ShooterTexture          iTexRightFoot       = null;
        public              ShooterTexture          iTexLeftFoot        = null;



        private ShooterBotTemplate
        (
            float   aRadius,
            float   aHeight,

            Bots    aGlasses,           ShooterTexture aTexGlassesGlass,            ShooterTexture aTexGlassesHolder,
            Bots    aHat,               ShooterTexture aTexHat,
            Bots    aHead,              ShooterTexture aTexHair,
            Bots    aFace,              ShooterTexture aTexFaceEyesOpen,            ShooterTexture aTexFaceEyesShut,
            Bots    aRightUpperArm,     ShooterTexture aTexRightUpperArm,
            Bots    aRightLowerArm,     ShooterTexture aTexRightLowerArm,
            Bots    aTorso,             ShooterTexture aTexTorsoUpper,              ShooterTexture aTexTorsoLower,
            Bots    aNeck,              ShooterTexture aTexNeck,
            Bots    aLeftUpperArm,      ShooterTexture aTexLeftUpperArm,
            Bots    aLeftLowerArm,      ShooterTexture aTexLeftLowerArm,
            Bots    aRightHand,         ShooterTexture aTexRightHand,
            Bots    aLeftHand,          ShooterTexture aTexLeftHand,
            Bots    aRightUpperLeg,     ShooterTexture aTexRightUpperLeg,
            Bots    aLeftUpperLeg,      ShooterTexture aTexLeftUpperLeg,
            Bots    aRightLowerLeg,     ShooterTexture aTexRightLowerLeg,
            Bots    aLeftLowerLeg,      ShooterTexture aTexLeftLowerLeg,
            Bots    aRightFoot,         ShooterTexture aTexRightFoot,
            Bots    aLeftFoot,          ShooterTexture aTexLeftFoot
        )
        {
            iRadius = aRadius;
            iHeight = aHeight;

            iGlasses        = aGlasses;
            iHat            = aHat;
            iHead           = aHead;
            iFace           = aFace;
            iRightUpperArm  = aRightUpperArm;
            iRightLowerArm  = aRightLowerArm;
            iTorso          = aTorso;
            iNeck           = aNeck;
            iLeftUpperArm   = aLeftUpperArm;
            iLeftLowerArm   = aLeftLowerArm;
            iRightHand      = aRightHand;
            iLeftHand       = aLeftHand;
            iRightUpperLeg  = aRightUpperLeg;
            iLeftUpperLeg   = aLeftUpperLeg;
            iRightLowerLeg  = aRightLowerLeg;
            iLeftLowerLeg   = aLeftLowerLeg;
            iRightFoot      = aRightFoot;
            iLeftFoot       = aLeftFoot;

            iTexGlassesGlass    = aTexGlassesGlass  ;
            iTexGlassesHolder   = aTexGlassesHolder ;
            iTexHat             = aTexHat           ;
            iTexFaceEyesOpen    = aTexFaceEyesOpen  ;
            iTexFaceEyesShut    = aTexFaceEyesShut  ;

            iTexHair            = aTexHair          ;
            iTexRightUpperArm   = aTexRightUpperArm ;
            iTexRightLowerArm   = aTexRightLowerArm ;
            iTexTorsoUpper      = aTexTorsoUpper    ;
            iTexTorsoLower      = aTexTorsoLower    ;
            iTexNeck            = aTexNeck          ;
            iTexLeftUpperArm    = aTexLeftUpperArm  ;
            iTexLeftLowerArm    = aTexLeftLowerArm  ;
            iTexRightHand       = aTexRightHand     ;
            iTexLeftHand        = aTexLeftHand      ;
            iTexRightUpperLeg   = aTexRightUpperLeg ;
            iTexLeftUpperLeg    = aTexLeftUpperLeg  ;
            iTexRightLowerLeg   = aTexRightLowerLeg ;
            iTexLeftLowerLeg    = aTexLeftLowerLeg  ;
            iTexRightFoot       = aTexRightFoot     ;
            iTexLeftFoot        = aTexLeftFoot      ;
        }
    }
