/*  $Id: ShooterBotTemplateBase.java 733 2011-05-13 23:16:18Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  de.christopherstock.shooter.ShooterSettings.*;
    import  de.christopherstock.shooter.base.ShooterTexture.*;
    import  de.christopherstock.shooter.game.objects.Bot.*;

    public enum ShooterBotPatternBase
    {
        ENaked
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,
            null,
            /* Tex aTexRightUpperArm    */  null,
            /* Tex aTexRightLowerArm    */  null,
            /* Tex aTexTorsoUpper       */  null,
            /* Tex aTexNeck             */  null,
            /* Tex aTexLeftUpperArm     */  null,
            /* Tex aTexLeftLowerArm     */  null,
            /* Tex aTexRightUpperLeg    */  null,
            /* Tex aTexLeftUpperLeg     */  null,
            /* Tex aTexRightLowerLeg    */  null,
            /* Tex aTexLeftLowerLeg     */  null,
            /* Tex aTexRightHand        */  null,
            /* Tex aTexLeftHand         */  null,
            /* Tex aTexRightFoot        */  null,
            /* Tex aTexLeftFoot         */  null
        ),

        EOfficeEmployee1
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,
            null,
            /* Tex aTexRightUpperArm    */  BotTex.EClothChemise1,
            /* Tex aTexRightLowerArm    */  BotTex.EClothChemise1,
            /* Tex aTexTorsoUpper       */  BotTex.ETorsoUpperChemise1,
            /* Tex aTexNeck             */  null,
            /* Tex aTexLeftUpperArm     */  BotTex.EClothChemise1,
            /* Tex aTexLeftLowerArm     */  BotTex.EClothChemise1,
            /* Tex aTexRightUpperLeg    */  BotTex.EClothBlack,
            /* Tex aTexLeftUpperLeg     */  BotTex.EClothBlack,
            /* Tex aTexRightLowerLeg    */  BotTex.EClothBlack,
            /* Tex aTexLeftLowerLeg     */  BotTex.EClothBlack,
            /* Tex aTexRightHand        */  null,
            /* Tex aTexLeftHand         */  null,
            /* Tex aTexRightFoot        */  BotTex.EShoeBrown1,
            /* Tex aTexLeftFoot         */  BotTex.EShoeBrown1
        ),

        EOfficeEmployee2
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,
            null,
            /* Tex aTexRightUpperArm    */  BotTex.EClothChemise2,
            /* Tex aTexRightLowerArm    */  BotTex.EClothChemise2,
            /* Tex aTexTorsoUpper       */  BotTex.ETorsoUpperChemise2,
            /* Tex aTexNeck             */  null,
            /* Tex aTexLeftUpperArm     */  BotTex.EClothChemise2,
            /* Tex aTexLeftLowerArm     */  BotTex.EClothChemise2,
            /* Tex aTexRightUpperLeg    */  BotTex.EClothBlack,
            /* Tex aTexLeftUpperLeg     */  BotTex.EClothBlack,
            /* Tex aTexRightLowerLeg    */  BotTex.EClothBlack,
            /* Tex aTexLeftLowerLeg     */  BotTex.EClothBlack,
            /* Tex aTexRightHand        */  null,
            /* Tex aTexLeftHand         */  null,
            /* Tex aTexRightFoot        */  BotTex.EShoeBrown1,
            /* Tex aTexLeftFoot         */  BotTex.EShoeBrown1
        ),

        EPilot1
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,
            null,
            /* Tex aTexRightUpperArm    */  BotTex.EClothBlueStar1,
            /* Tex aTexRightLowerArm    */  BotTex.EClothBlueYellowStripes1,
            /* Tex aTexTorsoUpper       */  BotTex.ETorsoUpperBlueSuite,
            /* Tex aTexNeck             */  null,
            /* Tex aTexLeftUpperArm     */  BotTex.EClothBlueStar1,
            /* Tex aTexLeftLowerArm     */  BotTex.EClothBlueYellowStripes1,
            /* Tex aTexRightUpperLeg    */  BotTex.EClothBlue1,
            /* Tex aTexLeftUpperLeg     */  BotTex.EClothBlue1,
            /* Tex aTexRightLowerLeg    */  BotTex.EClothBlueYellowStripes1,
            /* Tex aTexLeftLowerLeg     */  BotTex.EClothBlueYellowStripes1,
            /* Tex aTexRightHand        */  null,
            /* Tex aTexLeftHand         */  null,
            /* Tex aTexRightFoot        */  BotTex.EShoeBrown1,
            /* Tex aTexLeftFoot         */  BotTex.EShoeBrown1
        ),

        ESpecialForces
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,
            null,
            /* Tex aTexRightUpperArm    */  BotTex.EClothCamouflageBlue,
            /* Tex aTexRightLowerArm    */  BotTex.EClothCamouflageBlue,
            /* Tex aTexTorsoUpper       */  BotTex.EClothCamouflageBlue,
            /* Tex aTexNeck             */  null,
            /* Tex aTexLeftUpperArm     */  BotTex.EClothCamouflageBlue,
            /* Tex aTexLeftLowerArm     */  BotTex.EClothCamouflageBlue,
            /* Tex aTexRightUpperLeg    */  BotTex.EClothCamouflageBlue,
            /* Tex aTexLeftUpperLeg     */  BotTex.EClothCamouflageBlue,
            /* Tex aTexRightLowerLeg    */  BotTex.EClothCamouflageBlue,
            /* Tex aTexLeftLowerLeg     */  BotTex.EClothCamouflageBlue,
            /* Tex aTexRightHand        */  null,
            /* Tex aTexLeftHand         */  null,
            /* Tex aTexRightFoot        */  BotTex.EShoeBlack1,
            /* Tex aTexLeftFoot         */  BotTex.EShoeBlack1
        ),

        ESecurityHeavy
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,
            null,
            /* Tex aTexRightUpperArm    */  BotTex.EClothSecurityBadge,
            /* Tex aTexRightLowerArm    */  null,
            /* Tex aTexTorsoUpper       */  BotTex.ETorsoUpperSecurity,
            /* Tex aTexNeck             */  null,
            /* Tex aTexLeftUpperArm     */  BotTex.EClothSecurityBadge,
            /* Tex aTexLeftLowerArm     */  null,
            /* Tex aTexRightUpperLeg    */  BotTex.EClothSecurity,
            /* Tex aTexLeftUpperLeg     */  BotTex.EClothSecurity,
            /* Tex aTexRightLowerLeg    */  BotTex.EClothSecurity,
            /* Tex aTexLeftLowerLeg     */  BotTex.EClothSecurity,
            /* Tex aTexRightHand        */  BotTex.EClothSecurity,
            /* Tex aTexLeftHand         */  BotTex.EClothSecurity,
            /* Tex aTexRightFoot        */  BotTex.EShoeBlack1,
            /* Tex aTexLeftFoot         */  BotTex.EShoeBlack1
        ),

        ESecurityLight
        (
            BotSettings.RADIUS,
            BotSettings.HEIGHT,
            null,
            null,
            /* Tex aTexRightUpperArm    */  BotTex.EClothSecurityBadge,
            /* Tex aTexRightLowerArm    */  null,
            /* Tex aTexTorsoUpper       */  BotTex.ETorsoUpperSecurity,
            /* Tex aTexNeck             */  null,
            /* Tex aTexLeftUpperArm     */  BotTex.EClothSecurityBadge,
            /* Tex aTexLeftLowerArm     */  null,
            /* Tex aTexRightUpperLeg    */  BotTex.EClothSecurity,
            /* Tex aTexLeftUpperLeg     */  BotTex.EClothSecurity,
            /* Tex aTexRightLowerLeg    */  null,
            /* Tex aTexLeftLowerLeg     */  null,
            /* Tex aTexRightHand        */  BotTex.EClothSecurity,
            /* Tex aTexLeftHand         */  BotTex.EClothSecurity,
            /* Tex aTexRightFoot        */  BotTex.EShoeBlack1,
            /* Tex aTexLeftFoot         */  BotTex.EShoeBlack1
        ),

        ;

        public              float                   iRadius             = 0.0f;
        public              float                   iHeight             = 0.0f;

        public              BotBodyType             iBodyType           = null;
        public              BotSkinType             iSkinType           = null;

        public              Tex                     iTexRightUpperArm   = null;
        public              Tex                     iTexRightLowerArm   = null;
        public              Tex                     iTexTorso           = null;
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

        private ShooterBotPatternBase
        (
            float       aRadius,
            float       aHeight,
            BotBodyType aBodyType,
            BotSkinType aSkinType,
            Tex         aTexRightUpperArm,
            Tex         aTexRightLowerArm,
            Tex         aTexTorso,
            Tex         aTexNeck,
            Tex         aTexLeftUpperArm,
            Tex         aTexLeftLowerArm,
            Tex         aTexRightUpperLeg,
            Tex         aTexLeftUpperLeg,
            Tex         aTexRightLowerLeg,
            Tex         aTexLeftLowerLeg,
            Tex         aTexRightHand,
            Tex         aTexLeftHand,
            Tex         aTexRightFoot,
            Tex         aTexLeftFoot
        )
        {
            iRadius             = aRadius;
            iHeight             = aHeight;

            iSkinType           = aSkinType;
            iBodyType           = aBodyType;

            iTexRightUpperArm   = aTexRightUpperArm ;
            iTexRightLowerArm   = aTexRightLowerArm ;
            iTexTorso           = aTexTorso         ;
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
