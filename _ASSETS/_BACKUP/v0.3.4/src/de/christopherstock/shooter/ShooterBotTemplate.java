/*  $Id: ShooterBotTemplate.java 706 2011-05-04 21:40:34Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.shooter.ShooterTexture.BotTex;
    import  de.christopherstock.shooter.ShooterTexture.Default;
    import  de.christopherstock.shooter.ShooterTexture.Tex;
    import  de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.ShooterSettings.*;

    /**************************************************************************************
    *   Valid mesh combinations
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    public enum ShooterBotTemplate
    {
        EPilotMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            Bots.EGlasses,              Default.EGlass1,                 Default.EChrome1,
            Bots.EHat,                  BotTex.EBlue1,
            Bots.EHeadMale1,            BotTex.EHairBlack,
            Bots.EFace,                 BotTex.EFaceMale1EyesOpen,      BotTex.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    BotTex.EBlueStar1,
            Bots.ERightLowerArmMale,    BotTex.EBlueYellowStripes1,
            Bots.ETorsoMale1,           BotTex.EBlueSuitFront1,    BotTex.EBlueSuitTrousers1,
            Bots.ENeck,                 BotTex.ESkin3,
            Bots.ELeftUpperArmMale,     BotTex.EBlueStar1,
            Bots.ELeftLowerArmMale,     BotTex.EBlueYellowStripes1,
            Bots.ERightHand,            BotTex.EHand1,
            Bots.ELeftHand,             BotTex.EHand1,
            Bots.ERightUpperLegMale,    BotTex.EBlue1,
            Bots.ELeftUpperLegMale,     BotTex.EBlue1,
            Bots.ERightLowerLegMale,    BotTex.EBlueYellowStripes1,
            Bots.ELeftLowerLegMale,     BotTex.EBlueYellowStripes1,
            Bots.ERightFoot,            BotTex.EShoeBrown1,
            Bots.ELeftFoot,             BotTex.EShoeBrown1
        ),

        EPilotFemale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                       null,
            null,                       null,
            Bots.EHeadFemale2,          BotTex.EHairBlonde,
            Bots.EFace,                 BotTex.EFaceFemale1EyesOpen,        BotTex.EFaceFemale1EyesShut,
            Bots.ERightUpperArmFemale,  BotTex.EBlueStar1,
            Bots.ERightLowerArmFemale,  BotTex.EBlueYellowStripes1,
            Bots.ETorsoFemale1,         BotTex.EBlueSuitFront1,         BotTex.EBlueSuitTrousers1,
            Bots.ENeck,                 BotTex.ESkin1,
            Bots.ELeftUpperArmFemale,   BotTex.EBlueStar1,
            Bots.ELeftLowerArmFemale,   BotTex.EBlueYellowStripes1,
            Bots.ERightHand,            BotTex.EHand1,
            Bots.ELeftHand,             BotTex.EHand1,
            Bots.ERightUpperLegFemale,  BotTex.EBlue1,
            Bots.ELeftUpperLegFemale,   BotTex.EBlue1,
            Bots.ERightLowerLegFemale,  BotTex.EBlueYellowStripes1,
            Bots.ELeftLowerLegFemale,   BotTex.EBlueYellowStripes1,
            Bots.ERightFoot,            BotTex.EShoeBlack1,
            Bots.ELeftFoot,             BotTex.EShoeBlack1
        ),

        ESpecialForcesMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,

            null,                       null,                                   null,
            Bots.EHat,                  BotTex.ECamouflageBlue,
            Bots.EHeadMale1,            BotTex.EHairBlonde,
            Bots.EFace,                 BotTex.EFaceMale1EyesOpen,      BotTex.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    BotTex.ECamouflageBlue,
            Bots.ERightLowerArmMale,    BotTex.ECamouflageBlue,
            Bots.ETorsoMale1,           BotTex.ECamouflageBlue,    BotTex.ECamouflageBlue,
            Bots.ENeck,                 BotTex.ESkin3,
            Bots.ELeftUpperArmMale,     BotTex.ECamouflageBlue,
            Bots.ELeftLowerArmMale,     BotTex.ECamouflageBlue,
            Bots.ERightHand,            BotTex.EHand1,
            Bots.ELeftHand,             BotTex.EHand1,
            Bots.ERightUpperLegMale,    BotTex.ECamouflageBlue,
            Bots.ELeftUpperLegMale,     BotTex.ECamouflageBlue,
            Bots.ERightLowerLegMale,    BotTex.ECamouflageBlue,
            Bots.ELeftLowerLegMale,     BotTex.ECamouflageBlue,
            Bots.ERightFoot,            BotTex.EShoeBlack1,
            Bots.ELeftFoot,             BotTex.EShoeBlack1
        ),

        ESecurityMale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                   null,
            null,                       null,
            Bots.EHeadMale1,            BotTex.EHairBlonde,
            Bots.EFace,                 BotTex.EFaceMale1EyesOpen,      BotTex.EFaceMale1EyesShut,
            Bots.ERightUpperArmMale,    BotTex.ESecurityLogo,
            Bots.ERightLowerArmMale,    BotTex.ESkin3,
            Bots.ETorsoMale1,           Default.ETorsoSecurity,          BotTex.ESecurity,
            Bots.ENeck,                 BotTex.ESkin3,
            Bots.ELeftUpperArmMale,     BotTex.ESecurityLogo,
            Bots.ELeftLowerArmMale,     BotTex.ESkin3,
            Bots.ERightHand,            BotTex.ESecurity,
            Bots.ELeftHand,             BotTex.ESecurity,
            Bots.ERightUpperLegMale,    BotTex.ESecurity,
            Bots.ELeftUpperLegMale,     BotTex.ESecurity,
            Bots.ERightLowerLegMale,    BotTex.ESecurity,
            Bots.ELeftLowerLegMale,     BotTex.ESecurity,
            Bots.ERightFoot,            BotTex.EShoeBlack1,
            Bots.ELeftFoot,             BotTex.EShoeBlack1
        ),

        ESecurityFemale
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,                       null,                                   null,
            null,                       null,
            Bots.EHeadFemale1,          BotTex.EHairBlack,
            Bots.EFace,                 BotTex.EFaceFemale3EyesOpen,    BotTex.EFaceFemale3EyesShut,
            Bots.ERightUpperArmFemale,  BotTex.ESecurityLogo,
            Bots.ERightLowerArmFemale,  BotTex.ESkin1,
            Bots.ETorsoFemale1,         Default.ETorsoSecurity,          BotTex.ESecurity,
            Bots.ENeck,                 BotTex.ESkin1,
            Bots.ELeftUpperArmFemale,   BotTex.ESecurityLogo,
            Bots.ELeftLowerArmFemale,   BotTex.ESkin1,
            Bots.ERightHand,            BotTex.ESecurity,
            Bots.ELeftHand,             BotTex.ESecurity,
            Bots.ERightUpperLegFemale,  BotTex.ESecurity,
            Bots.ELeftUpperLegFemale,   BotTex.ESecurity,
            Bots.ERightLowerLegFemale,  BotTex.ESkin1,
            Bots.ELeftLowerLegFemale,   BotTex.ESkin1,
            Bots.ERightFoot,            BotTex.EShoeBlack1,
            Bots.ELeftFoot,             BotTex.EShoeBlack1
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
