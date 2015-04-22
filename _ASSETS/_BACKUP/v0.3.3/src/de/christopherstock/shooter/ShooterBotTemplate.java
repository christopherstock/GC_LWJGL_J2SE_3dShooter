/*  $Id: ShooterBotTemplate.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.shooter.ShooterTexture.Cloth;
    import  de.christopherstock.shooter.ShooterTexture.Default;
    import  de.christopherstock.shooter.ShooterTexture.Tex;
    import  de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.ShooterSettings.*;

    /**************************************************************************************
    *   Valid mesh combinations
    *
    *   @author     Christopher Stock
    *   @version    0.3.3
    **************************************************************************************/
    public enum ShooterBotTemplate
    {
        EPilotMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            Bots.EGlasses,              Default.EGlass1,                 Default.EChrome1,
            Bots.EHat,                  Cloth.EClothBlue1,
            Bots.EHeadMale1,            Default.EHairBlack,
            Bots.EFace,                 Default.EFaceMale1EyesOpen,      Default.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    Cloth.EClothBlueStar1,
            Bots.ERightLowerArmMale,    Cloth.EClothBlueYellowStripes1,
            Bots.ETorsoMale1,           Cloth.EClothBlueSuitFront1,    Cloth.EClothBlueSuitTrousers1,
            Bots.ENeck,                 Default.ESkin3,
            Bots.ELeftUpperArmMale,     Cloth.EClothBlueStar1,
            Bots.ELeftLowerArmMale,     Cloth.EClothBlueYellowStripes1,
            Bots.ERightHand,            Default.EHand1,
            Bots.ELeftHand,             Default.EHand1,
            Bots.ERightUpperLegMale,    Cloth.EClothBlue1,
            Bots.ELeftUpperLegMale,     Cloth.EClothBlue1,
            Bots.ERightLowerLegMale,    Cloth.EClothBlueYellowStripes1,
            Bots.ELeftLowerLegMale,     Cloth.EClothBlueYellowStripes1,
            Bots.ERightFoot,            Default.EShoeBrown1,
            Bots.ELeftFoot,             Default.EShoeBrown1
        ),

        EPilotFemale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                       null,
            null,                       null,
            Bots.EHeadFemale2,          Default.EHairBlonde,
            Bots.EFace,                 Default.EFaceFemale1EyesOpen,        Default.EFaceFemale1EyesShut,
            Bots.ERightUpperArmFemale,  Cloth.EClothBlueStar1,
            Bots.ERightLowerArmFemale,  Cloth.EClothBlueYellowStripes1,
            Bots.ETorsoFemale1,         Cloth.EClothBlueSuitFront1,         Cloth.EClothBlueSuitTrousers1,
            Bots.ENeck,                 Default.ESkin1,
            Bots.ELeftUpperArmFemale,   Cloth.EClothBlueStar1,
            Bots.ELeftLowerArmFemale,   Cloth.EClothBlueYellowStripes1,
            Bots.ERightHand,            Default.EHand1,
            Bots.ELeftHand,             Default.EHand1,
            Bots.ERightUpperLegFemale,  Cloth.EClothBlue1,
            Bots.ELeftUpperLegFemale,   Cloth.EClothBlue1,
            Bots.ERightLowerLegFemale,  Cloth.EClothBlueYellowStripes1,
            Bots.ELeftLowerLegFemale,   Cloth.EClothBlueYellowStripes1,
            Bots.ERightFoot,            Default.EShoeBlack1,
            Bots.ELeftFoot,             Default.EShoeBlack1
        ),

        ESpecialForcesMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,

            null,                       null,                                   null,
            Bots.EHat,                  Cloth.EClothCamouflageBlue,
            Bots.EHeadMale1,            Default.EHairBlonde,
            Bots.EFace,                 Default.EFaceMale1EyesOpen,      Default.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    Cloth.EClothCamouflageBlue,
            Bots.ERightLowerArmMale,    Cloth.EClothCamouflageBlue,
            Bots.ETorsoMale1,           Cloth.EClothCamouflageBlue,    Cloth.EClothCamouflageBlue,
            Bots.ENeck,                 Default.ESkin3,
            Bots.ELeftUpperArmMale,     Cloth.EClothCamouflageBlue,
            Bots.ELeftLowerArmMale,     Cloth.EClothCamouflageBlue,
            Bots.ERightHand,            Default.EHand1,
            Bots.ELeftHand,             Default.EHand1,
            Bots.ERightUpperLegMale,    Cloth.EClothCamouflageBlue,
            Bots.ELeftUpperLegMale,     Cloth.EClothCamouflageBlue,
            Bots.ERightLowerLegMale,    Cloth.EClothCamouflageBlue,
            Bots.ELeftLowerLegMale,     Cloth.EClothCamouflageBlue,
            Bots.ERightFoot,            Default.EShoeBlack1,
            Bots.ELeftFoot,             Default.EShoeBlack1
        ),

        ESecurityMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                   null,
            null,                       null,
            Bots.EHeadMale1,            Default.EHairBlonde,
            Bots.EFace,                 Default.EFaceMale1EyesOpen,      Default.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    Cloth.EClothSecurityLogo,
            Bots.ERightLowerArmMale,    Default.ESkin3,
            Bots.ETorsoMale1,           Default.ETorsoSecurity,          Cloth.EClothSecurity,
            Bots.ENeck,                 Default.ESkin3,
            Bots.ELeftUpperArmMale,     Cloth.EClothSecurityLogo,
            Bots.ELeftLowerArmMale,     Default.ESkin3,
            Bots.ERightHand,            Cloth.EClothSecurity,
            Bots.ELeftHand,             Cloth.EClothSecurity,
            Bots.ERightUpperLegMale,    Cloth.EClothSecurity,
            Bots.ELeftUpperLegMale,     Cloth.EClothSecurity,
            Bots.ERightLowerLegMale,    Cloth.EClothSecurity,
            Bots.ELeftLowerLegMale,     Cloth.EClothSecurity,
            Bots.ERightFoot,            Default.EShoeBlack1,
            Bots.ELeftFoot,             Default.EShoeBlack1
        ),

        ESecurityFemale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                   null,
            null,                       null,
            Bots.EHeadFemale1,          Default.EHairBlack,
            Bots.EFace,                 Default.EFaceFemale3EyesOpen,    Default.EFaceFemale3EyesShut,
            Bots.ERightUpperArmFemale,  Cloth.EClothSecurityLogo,
            Bots.ERightLowerArmFemale,  Default.ESkin1,
            Bots.ETorsoFemale1,         Default.ETorsoSecurity,          Cloth.EClothSecurity,
            Bots.ENeck,                 Default.ESkin1,
            Bots.ELeftUpperArmFemale,   Cloth.EClothSecurityLogo,
            Bots.ELeftLowerArmFemale,   Default.ESkin1,
            Bots.ERightHand,            Cloth.EClothSecurity,
            Bots.ELeftHand,             Cloth.EClothSecurity,
            Bots.ERightUpperLegFemale,  Cloth.EClothSecurity,
            Bots.ELeftUpperLegFemale,   Cloth.EClothSecurity,
            Bots.ERightLowerLegFemale,  Default.ESkin1,
            Bots.ELeftLowerLegFemale,   Default.ESkin1,
            Bots.ERightFoot,            Default.EShoeBlack1,
            Bots.ELeftFoot,             Default.EShoeBlack1
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

        public              Tex                     iTexGlassesGlass    = null;
        public              Tex                     iTexGlassesHolder   = null;
        public              Tex                     iTexHat             = null;
        public              Tex                     iTexFaceEyesOpen    = null;
        public              Tex                     iTexFaceEyesShut    = null;
        public              Tex                     iTexHair            = null;
        public              Tex                     iTexRightUpperArm   = null;
        public              Tex                     iTexRightLowerArm   = null;
        public              Tex                     iTexTorsoUpper      = null;
        public              Tex                     iTexTorsoLower      = null;
        public              Tex                     iTexNeck            = null;
        public              Tex                     iTexLeftUpperArm    = null;
        public              Tex                     iTexLeftLowerArm    = null;
        public              Tex                     iTexRightHand       = null;
        public              Tex                     iTexLeftHand        = null;
        public              Tex                     iTexRightUpperLeg   = null;
        public              Tex                     iTexLeftUpperLeg    = null;
        public              Tex                     iTexRightLowerLeg   = null;
        public              Tex                     iTexLeftLowerLeg    = null;
        public              Tex                     iTexRightFoot       = null;
        public              Tex                     iTexLeftFoot        = null;



        private ShooterBotTemplate
        (
            float   aRadius,
            float   aHeight,

            Bots    aGlasses,           Tex aTexGlassesGlass,            Tex aTexGlassesHolder,
            Bots    aHat,               Tex aTexHat,
            Bots    aHead,              Tex aTexHair,
            Bots    aFace,              Tex aTexFaceEyesOpen,            Tex aTexFaceEyesShut,
            Bots    aRightUpperArm,     Tex aTexRightUpperArm,
            Bots    aRightLowerArm,     Tex aTexRightLowerArm,
            Bots    aTorso,             Tex aTexTorsoUpper,              Tex aTexTorsoLower,
            Bots    aNeck,              Tex aTexNeck,
            Bots    aLeftUpperArm,      Tex aTexLeftUpperArm,
            Bots    aLeftLowerArm,      Tex aTexLeftLowerArm,
            Bots    aRightHand,         Tex aTexRightHand,
            Bots    aLeftHand,          Tex aTexLeftHand,
            Bots    aRightUpperLeg,     Tex aTexRightUpperLeg,
            Bots    aLeftUpperLeg,      Tex aTexLeftUpperLeg,
            Bots    aRightLowerLeg,     Tex aTexRightLowerLeg,
            Bots    aLeftLowerLeg,      Tex aTexLeftLowerLeg,
            Bots    aRightFoot,         Tex aTexRightFoot,
            Bots    aLeftFoot,          Tex aTexLeftFoot
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
