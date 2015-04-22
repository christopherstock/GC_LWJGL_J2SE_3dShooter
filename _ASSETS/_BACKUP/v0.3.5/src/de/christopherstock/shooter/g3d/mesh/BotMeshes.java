/*  $Id: BotMeshes.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.util.*;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.Lib.Offset;
    import  de.christopherstock.lib.Lib.Rotation;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.ShooterSettings.BotSettings.RotationSpeed;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.base.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.base.ShooterTexture.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class BotMeshes extends MeshCollection
    {
        public static enum HeadPosition
        {
            EStill,
            EAccept,
            ;
        }

        public static enum HeadTarget
        {
            EDefault,
            EAcceptDown,
            ;
        }

        public static enum ArmsPosition
        {
            ERestInHip,
            EAimHighRight,
            EAimHighLeft,
            EAimHighBoth,
            EDownBoth,
            EShakeArms,
            ;
        }

        public static enum ArmTarget
        {
            ERestInHip,
            EPointingToCeiling,
            EPointToSide,
            EPickUp,
            EAimHigh,
            EDown,
            EAimLow,
            EEllbowBack,
            ;
        }

        public static enum Arm
        {
            ELeft,
            ERight,
            ;
        }

        public static enum LegsPosition
        {
            EShakeLegs,
            EStandSpreadLegged,
            EKickRight,
            EKickLeft,
            EKickRightHigh,
            EKickLeftHigh,
            ;
        }

        public static enum LegTarget
        {
            EStandingSpreadLegged,
            EKicking,
            EKickingHigh,
            ;
        }

        public static enum Leg
        {
            ELeft,
            ERight,
            ;
        }

        public      static  final   Offset              OFFSET_ABSOLUTE_HEAD                = new Offset(   0.0f,       -0.05f,     1.225f  );
        public      static  final   Offset              OFFSET_ABSOLUTE_LEFT_UPPER_ARM      = new Offset(   -0.075f,    0.0f,       1.1f    );
        public      static  final   Offset              OFFSET_ABSOLUTE_RIGHT_UPPER_ARM     = new Offset(   0.075f,     0.0f,       1.1f    );
        public      static  final   Offset              OFFSET_ABSOLUTE_LEFT_UPPER_LEG      = new Offset(   -0.075f,    -0.05f,     0.68f   );
        public      static  final   Offset              OFFSET_ABSOLUTE_RIGHT_UPPER_LEG     = new Offset(   0.075f,     -0.05f,     0.68f   );
        public      static  final   Offset              OFFSET_RELATIVE_LEFT_LOWER_ARM      = new Offset(   -0.13f,     -0.013f,    -0.15f  );
        public      static  final   Offset              OFFSET_RELATIVE_RIGHT_LOWER_ARM     = new Offset(   0.13f,      0.013f,     -0.15f  );
        public      static  final   Offset              OFFSET_RELATIVE_LEFT_HAND           = new Offset(   0.07f,      -0.04f,     -0.15f  );
        public      static  final   Offset              OFFSET_RELATIVE_RIGHT_HAND          = new Offset(   -0.07f,     -0.04f,     -0.15f  );
        public      static  final   Offset              OFFSET_RELATIVE_LEFT_LOWER_LEG      = new Offset(   -0.06f,     0.0f,       -0.3f   );
        public      static  final   Offset              OFFSET_RELATIVE_RIGHT_LOWER_LEG     = new Offset(   0.06f,      0.0f,       -0.3f   );
        public      static  final   Offset              OFFSET_RELATIVE_LEFT_FOOT           = new Offset(   -0.075f,    0.02f,      -0.3f   );
        public      static  final   Offset              OFFSET_RELATIVE_RIGHT_FOOT          = new Offset(   0.075f,     0.02f,      -0.3f   );

        public                      int                 currentTargetPitchHead              = 0;

        public                      int                 currentTargetPitchLeftArm           = 0;
        public                      int                 currentTargetPitchRightArm          = 0;
        public                      int                 currentTargetPitchLeftLeg           = 0;
        public                      int                 currentTargetPitchRightLeg          = 0;

        public                      ShooterBotPattern  iTemplate                           = null;

        public                      BotMesh             iEquippedItemLeft                   = null;
        public                      BotMesh             iEquippedItemRight                  = null;

        public                      BotMesh             iGlasses                            = null;
        public                      BotMesh             iHat                                = null;
        public                      BotMesh             iHead                               = null;
        public                      BotMesh             iFace                               = null;
        public                      BotMesh             iRightUpperArm                      = null;
        public                      BotMesh             iRightLowerArm                      = null;
        public                      BotMesh             iTorso                              = null;
        public                      BotMesh             iNeck                               = null;
        public                      BotMesh             iLeftUpperArm                       = null;
        public                      BotMesh             iLeftLowerArm                       = null;
        public                      BotMesh             iRightHand                          = null;
        public                      BotMesh             iLeftHand                           = null;
        public                      BotMesh             iRightUpperLeg                      = null;
        public                      BotMesh             iLeftUpperLeg                       = null;
        public                      BotMesh             iRightLowerLeg                      = null;
        public                      BotMesh             iLeftLowerLeg                       = null;
        public                      BotMesh             iRightFoot                          = null;
        public                      BotMesh             iLeftFoot                           = null;

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  aAnchor             The meshes' anchor point.
        **************************************************************************************/
        public BotMeshes( ShooterBotPattern aTemp, LibVertex aAnchor, GameObject aParentGameObject, Bots itemLeft, Bots itemRight )
        {
            //assign template
            iTemplate = aTemp;

            ShooterBotPatternBase aTemplate = aTemp.iBase;

            //assign gun
            if ( itemLeft != null )
            {
                iEquippedItemLeft  = new BotMesh( ShooterD3dsFiles.getFaces( itemLeft                   ), aAnchor, 0.0f, 1.0f, aParentGameObject );
                iEquippedItemLeft.mirrorFaces( true, false, false );
            }

            if ( itemRight != null )
            {
                iEquippedItemRight = new BotMesh( ShooterD3dsFiles.getFaces( itemRight                  ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            }

            if ( aTemp.iHat != null )
            {
                iHat = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iHat ), aAnchor, 0.0f, 1.0f, aParentGameObject );
                iHat.changeTexture(             WallTex.ETest.getTexture(),          aTemp.iTexHat.getTexture()                  );
            }

            if ( aTemp.iGlasses != null )
            {
                iGlasses = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iGlasses ), aAnchor, 0.0f, 1.0f, aParentGameObject );
                iGlasses.changeTexture(             WallTex.ETest.getTexture(),         aTemp.iTexGlassesGlass.getTexture()         );
                iGlasses.changeTexture(             BotTex.EHairBlonde.getTexture(),    aTemp.iTexGlassesHolder.getTexture()        );
            }

            //assign limbs
            iHead           = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iHead               ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iFace           = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iFace               ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iRightUpperArm  = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iRightUpperArm      ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iRightLowerArm  = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iRightLowerArm      ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iTorso          = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iTorso              ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iNeck           = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iNeck               ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iLeftUpperArm   = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iLeftUpperArm       ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iLeftLowerArm   = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iLeftLowerArm       ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iRightHand      = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iRightHand          ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iLeftHand       = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iLeftHand           ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iRightUpperLeg  = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iRightUpperLeg      ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iLeftUpperLeg   = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iLeftUpperLeg       ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iRightLowerLeg  = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iRightLowerLeg      ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iLeftLowerLeg   = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iLeftLowerLeg       ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iRightFoot      = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iRightFoot          ), aAnchor, 0.0f, 1.0f, aParentGameObject );
            iLeftFoot       = new BotMesh( ShooterD3dsFiles.getFaces( aTemp.iLeftFoot           ), aAnchor, 0.0f, 1.0f, aParentGameObject );

            //assign textures for bot meshes
            iRightUpperLeg.changeTexture(   WallTex.ETest.getTexture(),          ( aTemplate.iTexRightUpperLeg == null ? aTemp.iSkin.getTexture() : aTemplate.iTexRightUpperLeg .getTexture()  )         );
            iLeftUpperLeg.changeTexture(    WallTex.ETest.getTexture(),          ( aTemplate.iTexLeftUpperLeg  == null ? aTemp.iSkin.getTexture() : aTemplate.iTexLeftUpperLeg  .getTexture()  )         );
            iRightLowerLeg.changeTexture(   WallTex.ETest.getTexture(),          ( aTemplate.iTexRightLowerLeg == null ? aTemp.iSkin.getTexture() : aTemplate.iTexRightLowerLeg .getTexture()  )         );
            iLeftLowerLeg.changeTexture(    WallTex.ETest.getTexture(),          ( aTemplate.iTexLeftLowerLeg  == null ? aTemp.iSkin.getTexture() : aTemplate.iTexLeftLowerLeg  .getTexture()  )         );
            iLeftFoot.changeTexture(        WallTex.ETest.getTexture(),          ( aTemplate.iTexLeftFoot      == null ? aTemp.iSkin.getTexture() : aTemplate.iTexLeftFoot      .getTexture()  )         );
            iRightFoot.changeTexture(       WallTex.ETest.getTexture(),          ( aTemplate.iTexRightFoot     == null ? aTemp.iSkin.getTexture() : aTemplate.iTexRightFoot     .getTexture()  )         );
            iLeftUpperArm.changeTexture(    WallTex.ETest.getTexture(),          ( aTemplate.iTexLeftUpperArm  == null ? aTemp.iSkin.getTexture() : aTemplate.iTexLeftUpperArm  .getTexture()  )         );
            iRightUpperArm.changeTexture(   WallTex.ETest.getTexture(),          ( aTemplate.iTexRightUpperArm == null ? aTemp.iSkin.getTexture() : aTemplate.iTexRightUpperArm .getTexture()  )         );
            iLeftLowerArm.changeTexture(    WallTex.ETest.getTexture(),          ( aTemplate.iTexLeftLowerArm  == null ? aTemp.iSkin.getTexture() : aTemplate.iTexLeftLowerArm  .getTexture()  )         );
            iRightLowerArm.changeTexture(   WallTex.ETest.getTexture(),          ( aTemplate.iTexRightLowerArm == null ? aTemp.iSkin.getTexture() : aTemplate.iTexRightLowerArm .getTexture()  )         );
            iLeftHand.changeTexture(        WallTex.ETest.getTexture(),          ( aTemplate.iTexLeftHand      == null ? aTemp.iSkin.getTexture() : aTemplate.iTexLeftHand      .getTexture()  )         );
            iRightHand.changeTexture(       WallTex.ETest.getTexture(),          ( aTemplate.iTexRightHand     == null ? aTemp.iSkin.getTexture() : aTemplate.iTexRightHand     .getTexture()  )         );
            iNeck.changeTexture(            WallTex.ETest.getTexture(),          ( aTemplate.iTexNeck          == null ? aTemp.iSkin.getTexture() : aTemplate.iTexNeck          .getTexture()  )         );
            iTorso.changeTexture(           WallTex.ETest.getTexture(),          ( aTemplate.iTexTorso    == null ? aTemp.iSkin.getTexture() : aTemplate.iTexTorso    .getTexture()  )         );

            iHead.changeTexture(            BotTex.EHairBlonde.getTexture(),     aTemp.iTexHair.getTexture()                            );
            iFace.changeTexture(            WallTex.ETest.getTexture(),          aTemp.iTexFaceEyesOpen.getTexture()                    );

            //assign initial arm position
            assignArmsPosition( ArmsPosition.ERestInHip             );
            assignLegsPosition( LegsPosition.EStandSpreadLegged     );
            assignHeadPosition( HeadPosition.EAccept                );

            Vector<Mesh> newMeshes = new Vector<Mesh>();
            newMeshes.add( iHead            );
            newMeshes.add( iFace            );
            newMeshes.add( iRightUpperArm   );
            newMeshes.add( iRightLowerArm   );
            newMeshes.add( iRightHand       );
            newMeshes.add( iLeftUpperArm    );
            newMeshes.add( iLeftLowerArm    );
            newMeshes.add( iLeftHand        );
            newMeshes.add( iTorso           );
            newMeshes.add( iNeck            );
            newMeshes.add( iRightUpperLeg   );
            newMeshes.add( iRightLowerLeg   );
            newMeshes.add( iLeftLowerLeg    );
            newMeshes.add( iLeftUpperLeg    );
            newMeshes.add( iRightFoot       );
            newMeshes.add( iLeftFoot        );

            if ( iGlasses           != null ) newMeshes.add( iGlasses           );
            if ( iHat               != null ) newMeshes.add( iHat               );
            if ( iEquippedItemLeft  != null ) newMeshes.add( iEquippedItemLeft  );
            if ( iEquippedItemRight != null ) newMeshes.add( iEquippedItemRight );

            iMeshes = newMeshes.toArray( new Mesh[] {} );
        }

        public void assignArmsPosition( ArmsPosition newArmsPosition )
        {
            switch ( newArmsPosition )
            {
                case EShakeArms:
                {
                    assignArmTargets( Arm.ELeft,  new ArmTarget[] { ArmTarget.EPointingToCeiling, ArmTarget.EPointToSide, ArmTarget.EPickUp, } );
                    assignArmTargets( Arm.ERight, new ArmTarget[] { ArmTarget.ERestInHip, ArmTarget.EAimLow, ArmTarget.EDown, ArmTarget.EPointToSide, }  );
                    break;
                }

                case ERestInHip:
                {
                    assignArmTargets( Arm.ELeft,  new ArmTarget[] { ArmTarget.ERestInHip, }  );
                    assignArmTargets( Arm.ERight, new ArmTarget[] { ArmTarget.ERestInHip, }  );
                    break;
                }

                case EAimHighRight:
                {
                    assignArmTargets( Arm.ELeft,  new ArmTarget[] { ArmTarget.EDown,      }  );
                    assignArmTargets( Arm.ERight, new ArmTarget[] { ArmTarget.EAimHigh,   }  );
                    break;
                }

                case EAimHighLeft:
                {
                    assignArmTargets( Arm.ELeft,  new ArmTarget[] { ArmTarget.EAimHigh,  }   );
                    assignArmTargets( Arm.ERight, new ArmTarget[] { ArmTarget.EDown,     }   );
                    break;
                }

                case EAimHighBoth:
                {
                    assignArmTargets( Arm.ELeft,  new ArmTarget[] { ArmTarget.EAimHigh,  }   );
                    assignArmTargets( Arm.ERight, new ArmTarget[] { ArmTarget.EAimHigh,  }   );
                    break;
                }

                case EDownBoth:
                {
                    assignArmTargets( Arm.ELeft,  new ArmTarget[] { ArmTarget.EDown,     }   );
                    assignArmTargets( Arm.ERight, new ArmTarget[] { ArmTarget.EDown,     }   );
                    break;
                }
            }
        }

        private void assignArmTargets( Arm arm, ArmTarget[] newArmTargets )
        {
            //reset current arm targets
            currentTargetPitchLeftArm   = 0;
            currentTargetPitchRightArm  = 0;

            Rotation[] upperArmPitch      = new Rotation[ newArmTargets.length ];
            Rotation[] lowerArmPitch      = new Rotation[ newArmTargets.length ];
            Rotation[] handPitch          = new Rotation[ newArmTargets.length ];

            for ( int i = 0; i < newArmTargets.length; ++i )
            {
                upperArmPitch[ i ] = getNewArmPosition( arm, newArmTargets[ i ] )[ 0 ];
                lowerArmPitch[ i ] = getNewArmPosition( arm, newArmTargets[ i ] )[ 1 ];
                handPitch[     i ] = getNewArmPosition( arm, newArmTargets[ i ] )[ 2 ];
            }

            //assign to specified arm
            switch ( arm )
            {
                case ELeft:
                {
                    iLeftUpperArm.setTargetPitchs(   upperArmPitch );
                    iLeftLowerArm.setTargetPitchs(   lowerArmPitch );
                    iLeftHand.setTargetPitchs(       handPitch );

                    if ( iEquippedItemLeft != null ) iEquippedItemLeft.setTargetPitchs(   handPitch );

                    break;
                }

                case ERight:
                {
                    iRightUpperArm.setTargetPitchs(  upperArmPitch );
                    iRightLowerArm.setTargetPitchs(  lowerArmPitch );
                    iRightHand.setTargetPitchs(      handPitch );

                    if ( iEquippedItemRight != null ) iEquippedItemRight.setTargetPitchs(  handPitch );

                    break;
                }
            }
        }

        private Rotation[] getNewArmPosition( Arm arm, ArmTarget newArmPosition )
        {
            Rotation upperArmPitch      = new Rotation();
            Rotation lowerArmPitch      = new Rotation();
            Rotation handPitch          = new Rotation();

            //define roations
            switch ( newArmPosition )
            {
                case ERestInHip:
                {
                    upperArmPitch.set( 0.0f, 0.0f,   0.0f, RotationSpeed.LIMBS  );
                    lowerArmPitch.set( 0.0f, -10.0f, 0.0f, RotationSpeed.LIMBS );
                    handPitch.set(     0.0f, 0.0f,   0.0f, RotationSpeed.LIMBS  );
                    break;
                }

                case EPickUp:
                {
                    upperArmPitch.set(  -50.0f, 0.0f,  0.0f, RotationSpeed.LIMBS );
                    lowerArmPitch.set(  -50.0f, 0.0f,  0.0f, RotationSpeed.LIMBS );
                    handPitch.set(       -55.0f, 35.0f, 0.0f, RotationSpeed.LIMBS );
                    break;
                }

                case EPointToSide:
                {
                    upperArmPitch.set(  0.0f, -50.0f,  0.0f, RotationSpeed.LIMBS );
                    lowerArmPitch.set(  0.0f, -110.0f, 0.0f, RotationSpeed.LIMBS );
                    handPitch.set(       0.0f, -95.0f,  0.0f, RotationSpeed.LIMBS );
                    break;
                }

                case EAimLow:
                {
                    upperArmPitch.set(  -50.0f, 50.0f, 0.0f, RotationSpeed.LIMBS );
                    lowerArmPitch.set(  -60.0f, 0.0f,  0.0f, RotationSpeed.LIMBS );
                    handPitch.set(       -95.0f, 0.0f,  0.0f, RotationSpeed.LIMBS );
                    break;
                }

                case EEllbowBack:
                {
                    upperArmPitch.set(  0.0f,   0.0f,  90.0f, RotationSpeed.LIMBS );
                    lowerArmPitch.set(  0.0f,   40.0f, 90.0f, RotationSpeed.LIMBS );
                    handPitch.set(       -55.0f, 0.0f,  0.0f, RotationSpeed.LIMBS  );
                    break;
                }

                case EAimHigh:
                {
                    upperArmPitch.set(  -90.0f, 0.0f,  0.0f, RotationSpeed.LIMBS  );
                    lowerArmPitch.set(  -90.0f, 0.0f,  0.0f, RotationSpeed.LIMBS  );
                    handPitch.set(      -90.0f, -90.0f,  0.0f, RotationSpeed.LIMBS );
                    break;
                }

                case EDown:
                {
                    upperArmPitch.set(  0.0f, 20.0f,  0.0f, RotationSpeed.LIMBS );
                    lowerArmPitch.set(  0.0f, -25.0f, 0.0f, RotationSpeed.LIMBS );
                    handPitch.set(       0.0f, -10.0f, 0.0f, RotationSpeed.LIMBS );

                    break;
                }

                case EPointingToCeiling:
                {
                    upperArmPitch.set(   0.0f,   -120.0f, 0.0f, RotationSpeed.LIMBS );
                    lowerArmPitch.set(   0.0f,   120.0f,  0.0f, RotationSpeed.LIMBS );
                    handPitch.set(       185.0f, 0.0f,    0.0f, RotationSpeed.LIMBS );
                    break;
                }
            }

            //assign to specified arm
            switch ( arm )
            {
                case ELeft:
                {
                    //flip y axis for left arm!
                    upperArmPitch.set(   upperArmPitch.x, -upperArmPitch.y, -upperArmPitch.z, RotationSpeed.UPPER_ARM );
                    lowerArmPitch.set(   lowerArmPitch.x, -lowerArmPitch.y, -lowerArmPitch.z, RotationSpeed.LOWER_ARM );
                    handPitch.set(       handPitch.x,     -handPitch.y,     -handPitch.z, RotationSpeed.HAND     );
                    break;
                }

                case ERight:
                {
                    upperArmPitch.set(  upperArmPitch.x, upperArmPitch.y, upperArmPitch.z, RotationSpeed.UPPER_ARM );
                    lowerArmPitch.set(  lowerArmPitch.x, lowerArmPitch.y, lowerArmPitch.z, RotationSpeed.LOWER_ARM );
                    handPitch.set(      handPitch.x,     handPitch.y,     handPitch.z, RotationSpeed.HAND     );

                    break;
                }
            }

            return new Rotation[] { upperArmPitch, lowerArmPitch, handPitch, };
        }

        public void assignLegsPosition( LegsPosition newLegsPosition )
        {
            //reset current leg targets
            currentTargetPitchLeftLeg   = 0;
            currentTargetPitchRightLeg  = 0;

            //reset current leg target if the position has changed
            switch ( newLegsPosition )
            {
                case EShakeLegs:
                {
                    assignLegTargets( Leg.ELeft,   new LegTarget[] { LegTarget.EStandingSpreadLegged, LegTarget.EKicking,   }   );
                    assignLegTargets( Leg.ERight,  new LegTarget[] { LegTarget.EStandingSpreadLegged, LegTarget.EKicking,   }   );
                    break;
                }

                case EStandSpreadLegged:
                {
                    assignLegTargets( Leg.ELeft,   new LegTarget[] { LegTarget.EStandingSpreadLegged, }   );
                    assignLegTargets( Leg.ERight,  new LegTarget[] { LegTarget.EStandingSpreadLegged, }   );
                    break;
                }

                case EKickRight:
                {
                    assignLegTargets( Leg.ELeft,   new LegTarget[] { LegTarget.EStandingSpreadLegged,   }   );
                    assignLegTargets( Leg.ERight,  new LegTarget[] { LegTarget.EKicking,                }   );
                    break;
                }

                case EKickLeft:
                {
                    assignLegTargets( Leg.ELeft,   new LegTarget[] { LegTarget.EKicking,                }   );
                    assignLegTargets( Leg.ERight,  new LegTarget[] { LegTarget.EStandingSpreadLegged,   }   );
                    break;
                }

                case EKickRightHigh:
                {
                    assignLegTargets( Leg.ELeft,   new LegTarget[] { LegTarget.EKickingHigh,            }   );
                    assignLegTargets( Leg.ERight,  new LegTarget[] { LegTarget.EStandingSpreadLegged,   }   );
                    break;
                }

                case EKickLeftHigh:
                {
                    assignLegTargets( Leg.ELeft,   new LegTarget[] { LegTarget.EStandingSpreadLegged,   }   );
                    assignLegTargets( Leg.ERight,  new LegTarget[] { LegTarget.EKickingHigh,                }   );
                    break;
                }
            }
        }

        private void assignLegTargets( Leg leg, LegTarget[] newLegTargets )
        {
            //reset current leg targets
            currentTargetPitchLeftLeg   = 0;
            currentTargetPitchRightLeg  = 0;

            Rotation[] upperLegPitch      = new Rotation[ newLegTargets.length ];
            Rotation[] lowerLegPitch      = new Rotation[ newLegTargets.length ];
            Rotation[] footPitch          = new Rotation[ newLegTargets.length ];

            for ( int i = 0; i < newLegTargets.length; ++i )
            {
                upperLegPitch[ i ] = getNewLegPosition( leg, newLegTargets[ i ] )[ 0 ];
                lowerLegPitch[ i ] = getNewLegPosition( leg, newLegTargets[ i ] )[ 1 ];
                footPitch[     i ] = getNewLegPosition( leg, newLegTargets[ i ] )[ 2 ];
            }

            //assign to specified leg
            switch ( leg )
            {
                case ELeft:
                {
                    //flip y axis for left leg!

                    iLeftUpperLeg.setTargetPitchs(   upperLegPitch );
                    iLeftLowerLeg.setTargetPitchs(   lowerLegPitch );
                    iLeftFoot.setTargetPitchs(       footPitch );
                    break;
                }

                case ERight:
                {
                    iRightUpperLeg.setTargetPitchs(  upperLegPitch );
                    iRightLowerLeg.setTargetPitchs(  lowerLegPitch );
                    iRightFoot.setTargetPitchs(      footPitch );

                    break;
                }
            }
        }

        public void assignHeadPosition( HeadPosition newHeadPosition )
        {
            switch ( newHeadPosition )
            {
                case EStill:
                {
                    assignHeadTargets( new HeadTarget[] { HeadTarget.EDefault, } );
                    break;
                }

                case EAccept:
                {
                    assignHeadTargets( new HeadTarget[] { HeadTarget.EDefault, HeadTarget.EAcceptDown, } );
                    break;
                }
            }
        }

        private void assignHeadTargets( HeadTarget[] newHeadTargets )
        {
            //reset current head targets
            currentTargetPitchHead      = 0;

            Rotation[] headPitch          = new Rotation[ newHeadTargets.length ];

            for ( int i = 0; i < newHeadTargets.length; ++i )
            {
                headPitch[     i ] = getNewHeadPosition( newHeadTargets[ i ] );
            }

            iHead.setTargetPitchs(      headPitch );
            iFace.setTargetPitchs(      headPitch );

            if ( iHat     != null ) iHat.setTargetPitchs(       headPitch );
            if ( iGlasses != null ) iGlasses.setTargetPitchs(   headPitch );
        }

        private final Rotation[] getNewLegPosition( Leg leg, LegTarget newLegPosition )
        {
            Rotation upperLegPitch  = new Rotation();
            Rotation lowerLegPitch  = new Rotation();
            Rotation footPitch      = new Rotation();

            //define roations
            switch ( newLegPosition )
            {
                case EStandingSpreadLegged:
                {
                    upperLegPitch.set(  0.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    lowerLegPitch.set(  0.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    footPitch.set(      0.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    break;
                }

                case EKicking:
                {
                    upperLegPitch.set(  -90.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    lowerLegPitch.set(  -90.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    footPitch.set(      -90.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    break;
                }

                case EKickingHigh:
                {
                    upperLegPitch.set(  -135.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    lowerLegPitch.set(  -135.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    footPitch.set(      -135.0f,   0.0f,   0.0f, RotationSpeed.LIMBS );
                    break;
                }
            }

            //assign to specified leg
            switch ( leg )
            {
                case ELeft:
                {
                    //flip y axis for left leg!
                    upperLegPitch.set(   upperLegPitch.x, -upperLegPitch.y, -upperLegPitch.z, RotationSpeed.LIMBS );
                    lowerLegPitch.set(   lowerLegPitch.x, -lowerLegPitch.y, -lowerLegPitch.z, RotationSpeed.LIMBS );
                    footPitch.set(       footPitch.x,     -footPitch.y,     -footPitch.z, RotationSpeed.LIMBS     );
                    break;
                }

                case ERight:
                {
                    upperLegPitch.set(  upperLegPitch.x, upperLegPitch.y, upperLegPitch.z, RotationSpeed.LIMBS );
                    lowerLegPitch.set(  lowerLegPitch.x, lowerLegPitch.y, lowerLegPitch.z, RotationSpeed.LIMBS );
                    footPitch.set(      footPitch.x,     footPitch.y,     footPitch.z, RotationSpeed.LIMBS     );

                    break;
                }
            }

            return new Rotation[] { upperLegPitch, lowerLegPitch, footPitch, };
        }


        private final Rotation getNewHeadPosition( HeadTarget newHeadPosition )
        {
            Rotation headPitch      = new Rotation();

            //define roations
            switch ( newHeadPosition )
            {
                case EDefault:
                {
                    headPitch.set(      0.0f,   0.0f,   0.0f, RotationSpeed.HEAD );
                    break;
                }

                case EAcceptDown:
                {
                    headPitch.set(      30.0f,   0.0f,   0.0f, RotationSpeed.HEAD );
                    break;
                }
            }

            //headPitch.set(   headPitch.x, -headPitch.y, -headPitch.z );

            return headPitch;
        }

        public void transformLimbs( LibVertex botAnchor, float facingAngle, float dropDeadAngle )
        {
            //set target pitches
            setTargetPitches();

            //perform transformations on limbs - this costs lots of performance :(
            transformAllLimbs( botAnchor );

            //turn all faces x around bot's anchor
            if ( dropDeadAngle != 0.0f ) turnAllLimbsX( botAnchor, dropDeadAngle );

            //turn all faces around bot's anchor
            if ( facingAngle != 0.0f ) turnAllLimbsZ( botAnchor, facingAngle );
        }

        private void setTargetPitches()
        {
            //head
            boolean h = iHead.reachToTargetPitch(       currentTargetPitchHead );
                        iFace.reachToTargetPitch(       currentTargetPitchHead );
                        if ( iHat     != null ) iHat.reachToTargetPitch(        currentTargetPitchHead );
                        if ( iGlasses != null ) iGlasses.reachToTargetPitch(    currentTargetPitchHead );

            //check if all limbs of the head reached finish
            if ( h )
            {
                ++currentTargetPitchHead;
                if ( currentTargetPitchHead >= iHead.iTargetPitch.size() ) currentTargetPitchHead = 0;
            }

            //right arm
            boolean ra1 = iRightUpperArm.reachToTargetPitch( currentTargetPitchRightArm );
            boolean ra2 = iRightLowerArm.reachToTargetPitch( currentTargetPitchRightArm );
            boolean ra3 = iRightHand.reachToTargetPitch(     currentTargetPitchRightArm );
                          if ( iEquippedItemRight != null ) iEquippedItemRight.reachToTargetPitch(  currentTargetPitchRightArm );
            //check if all limbs of the left arm reached finish
            if ( ra1 & ra2 & ra3 )
            {
                ++currentTargetPitchRightArm;
                if ( currentTargetPitchRightArm >= iRightUpperArm.iTargetPitch.size() ) currentTargetPitchRightArm = 0;
            }

            //left arm
            boolean la1 = iLeftUpperArm.reachToTargetPitch( currentTargetPitchLeftArm );
            boolean la2 = iLeftLowerArm.reachToTargetPitch( currentTargetPitchLeftArm );
            boolean la3 = iLeftHand.reachToTargetPitch(     currentTargetPitchLeftArm );
                          if ( iEquippedItemLeft != null ) iEquippedItemLeft.reachToTargetPitch(  currentTargetPitchLeftArm );
            //check if all limbs of the left arm reached finish
            if ( la1 & la2 & la3 )
            {
                ++currentTargetPitchLeftArm;
                if ( currentTargetPitchLeftArm >= iLeftUpperArm.iTargetPitch.size() ) currentTargetPitchLeftArm = 0;
            }

            //right leg
            boolean rl1 = iRightUpperLeg.reachToTargetPitch( currentTargetPitchRightLeg );
            boolean rl2 = iRightLowerLeg.reachToTargetPitch( currentTargetPitchRightLeg );
            boolean rl3 = iRightFoot.reachToTargetPitch(     currentTargetPitchRightLeg );
            //check if all limbs of the right leg reached finish
            if ( rl1 & rl2 & rl3 )
            {
                ++currentTargetPitchRightLeg;
                if ( currentTargetPitchRightLeg >= iRightUpperLeg.iTargetPitch.size() ) currentTargetPitchRightLeg = 0;
            }

            //left leg
            boolean ll1 = iLeftUpperLeg.reachToTargetPitch( currentTargetPitchLeftLeg );
            boolean ll2 = iLeftLowerLeg.reachToTargetPitch( currentTargetPitchLeftLeg );
            boolean ll3 = iLeftFoot.reachToTargetPitch(     currentTargetPitchLeftLeg );
            //check if all limbs of the left leg reached finish
            if ( ll1 & ll2 & ll3 )
            {
                ++currentTargetPitchLeftLeg;
                if ( currentTargetPitchLeftLeg >= iLeftUpperLeg.iTargetPitch.size() ) currentTargetPitchLeftLeg = 0;
            }
        }

        private void transformAllLimbs( LibVertex botAnchor )
        {
            //transform head
                                                iHead.transformOwn(                           BotMeshes.OFFSET_ABSOLUTE_HEAD                                                                          );
                                                iFace.transformOwn(                           BotMeshes.OFFSET_ABSOLUTE_HEAD                                                                          );
            if ( iHat     != null )             iHat.transformOwn(                            BotMeshes.OFFSET_ABSOLUTE_HEAD                                                                          );
            if ( iGlasses != null )             iGlasses.transformOwn(                        BotMeshes.OFFSET_ABSOLUTE_HEAD                                                                          );

            //transform right arm
            LibVertex rightUpperArmAnk  =       iRightUpperArm.transformOwn(                  BotMeshes.OFFSET_ABSOLUTE_RIGHT_UPPER_ARM                                                               );
            LibVertex rightLowerArmAnk  =       iRightLowerArm.transformAroundOtherLimb(      BotMeshes.OFFSET_RELATIVE_RIGHT_LOWER_ARM,  iRightUpperArm, iRightLowerArm, rightUpperArmAnk            );
                                                iRightHand.transformAroundOtherLimb(          BotMeshes.OFFSET_RELATIVE_RIGHT_HAND,       iRightLowerArm, iRightHand,     rightLowerArmAnk            );
            if ( iEquippedItemRight != null )   iEquippedItemRight.transformAroundOtherLimb(  BotMeshes.OFFSET_RELATIVE_RIGHT_HAND,       iRightLowerArm, iRightHand,     rightLowerArmAnk            );

            //transform left arm
            LibVertex leftUpperArmAnk   =       iLeftUpperArm.transformOwn(                   BotMeshes.OFFSET_ABSOLUTE_LEFT_UPPER_ARM                                                                );
            LibVertex leftLowerArmAnk   =       iLeftLowerArm.transformAroundOtherLimb(       BotMeshes.OFFSET_RELATIVE_LEFT_LOWER_ARM,   iLeftUpperArm,  iLeftLowerArm,  leftUpperArmAnk             );
                                                iLeftHand.transformAroundOtherLimb(           BotMeshes.OFFSET_RELATIVE_LEFT_HAND,        iLeftLowerArm,  iLeftHand,      leftLowerArmAnk             );
            if ( iEquippedItemLeft != null )    iEquippedItemLeft.transformAroundOtherLimb(   BotMeshes.OFFSET_RELATIVE_LEFT_HAND,        iLeftLowerArm,  iLeftHand,      leftLowerArmAnk             );

            //transform right leg
            LibVertex rightUpperLegAnk  =       iRightUpperLeg.transformOwn(                  BotMeshes.OFFSET_ABSOLUTE_RIGHT_UPPER_LEG                                                               );
            LibVertex rightLowerLegAnk  =       iRightLowerLeg.transformAroundOtherLimb(      BotMeshes.OFFSET_RELATIVE_RIGHT_LOWER_LEG,  iRightUpperLeg, iRightLowerLeg, rightUpperLegAnk            );
                                                iRightFoot.transformAroundOtherLimb(          BotMeshes.OFFSET_RELATIVE_RIGHT_FOOT,       iRightLowerLeg, iRightFoot,     rightLowerLegAnk            );

            //transform left leg
            LibVertex leftUpperLegAnk   =       iLeftUpperLeg.transformOwn(                   BotMeshes.OFFSET_ABSOLUTE_LEFT_UPPER_LEG                                                                );
            LibVertex leftLowerLegAnk   =       iLeftLowerLeg.transformAroundOtherLimb(       BotMeshes.OFFSET_RELATIVE_LEFT_LOWER_LEG,   iLeftUpperLeg,  iLeftLowerLeg,  leftUpperLegAnk             );
                                                iLeftFoot.transformAroundOtherLimb(           BotMeshes.OFFSET_RELATIVE_LEFT_FOOT,        iLeftLowerLeg,  iLeftFoot,      leftLowerLegAnk             );

            //torso and neck are not transformed - just translate them
            iTorso.translate( botAnchor.x, botAnchor.y, botAnchor.z, LibTransformationMode.EOriginalsToTransformed );
            iNeck.translate(  botAnchor.x, botAnchor.y, botAnchor.z, LibTransformationMode.EOriginalsToTransformed );
        }

        public void turnAllLimbsZ( LibVertex botAnchor, float facingAngle )
        {
            for ( Mesh mesh : iMeshes )
            {
                mesh.translateAndRotateXYZ( 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, facingAngle, botAnchor, LibTransformationMode.ETransformedToTransformed );
            }
        }

        public void turnAllLimbsX( LibVertex botAnchor, float angle )
        {
            for ( Mesh mesh : iMeshes )
            {
                mesh.translateAndRotateXYZ( 0.0f, 0.0f, 0.0f, angle, 0.0f, 0.0f, botAnchor, LibTransformationMode.ETransformedToTransformed );
            }
        }
    }
