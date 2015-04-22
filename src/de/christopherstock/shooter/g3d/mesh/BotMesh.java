/*  $Id: BotMesh.java 1250 2013-01-02 21:00:46Z jenetic.bytemare@gmail.com $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.util.*;
    import  de.christopherstock.lib.Lib.Invert;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.Lib.Offset;
    import  de.christopherstock.lib.Lib.Rotation;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.g3d.face.*;
    import  de.christopherstock.lib.g3d.face.LibFace.*;
    import  de.christopherstock.lib.game.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public class BotMesh extends Mesh
    {
        private     static  final   long                serialVersionUID        = -592606625644524448L;

        public                      Rotation            iPitch                  = new Rotation();
        public                      float               iLimbSpeed              = 0.0f;
        public                      Vector<Rotation>    iTargetPitch            = new Vector<Rotation>();

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  aFaces              All faces this mesh shall consist of.
        *   @param  aAnchor             The meshes' anchor point.
        **************************************************************************************/
        public BotMesh( LibFaceTriangle[] aFaces, LibVertex aAnchor, float aInitRotZ, float aInitScale, LibGameObject aParentGameObject, float aDamageMultiplier )
        {
            super( aFaces, aAnchor, aInitRotZ, aInitScale, Invert.ENo, aParentGameObject, LibTransformationMode.EOriginalsToTransformed, DrawMethod.EAlwaysDraw );
            for ( LibFaceTriangle ft : aFaces )
            {
                ft.setDamageMultiplier( aDamageMultiplier );
            }
        }

        public final LibVertex translateLimb( Offset trans )
        {
            LibVertex   limbAnk = getAnchor().copy();

            limbAnk.x += trans.x;
            limbAnk.y += trans.y;
            limbAnk.z += trans.z;

            translateAndRotateXYZ(  limbAnk.x,  limbAnk.y,  limbAnk.z,  0.0f,    0.0f,       0.0f,       limbAnk,    LibTransformationMode.EOriginalsToTransformed      );

            return limbAnk;
        }

        public final LibVertex translateAndRotateLimb( Offset trans )
        {
            //rotate
            LibVertex   limbAnk = getAnchor().copy();

                        limbAnk.x += trans.x;
                        limbAnk.y += trans.y;
                        limbAnk.z += trans.z;

            //translate and turn around x, y and z axis sequentially is no more necessary!?
            translateAndRotateXYZ(  limbAnk.x,  limbAnk.y,  limbAnk.z,  iPitch.x,    iPitch.y,       iPitch.z,       limbAnk,    LibTransformationMode.EOriginalsToTransformed      );

            return limbAnk;
        }

        public final void rotateAroundAnchor( LibVertex anchor, Rotation pitch )
        {
            //translate and turn around x, y and z axis sequentially is no more necessary!?
            translateAndRotateXYZ( 0.0f, 0.0f, 0.0f, pitch.x, pitch.y, pitch.z, anchor, LibTransformationMode.ETransformedToTransformed );
        }

        public final void transformOwn( Offset trans, float rotX, float rotY, float rotZ )
        {
            //rotate
            LibVertex   limbAnk = getAnchor().copy();

                        limbAnk.x += trans.x;
                        limbAnk.y += trans.y;
                        limbAnk.z += trans.z;

            //translate and turn around x, y and z axis sequentially is no more necessary!?
            translateAndRotateXYZ(  limbAnk.x,  limbAnk.y,  limbAnk.z,  rotX,    rotY,       rotZ,       limbAnk,    LibTransformationMode.EOriginalsToTransformed      );
        }

        public final void setTargetPitchs( Rotation[] targetPitchs )
        {
            iTargetPitch = new Vector<Rotation>( Arrays.asList( targetPitchs ) );
        }

        public final boolean reachToTargetPitch( int currentTargetPitch )
        {
            //check if the target pitch is already reached
            boolean skipReach = false;
            boolean reached   = false;
            if ( iPitch.equalRounded( iTargetPitch.elementAt( currentTargetPitch ) ) )
            {
                if ( iTargetPitch.size() == 1 )
                {
                    skipReach = true;
                }

                reached = true;
            }

            if ( !skipReach )
            {
                iPitch.reachToAbsolute( iTargetPitch.elementAt( currentTargetPitch ), iTargetPitch.elementAt( currentTargetPitch ).iSpeed );
            }

            return reached;
        }

        /**************************************************************************************
        *   Represents a mesh.
        *
        *   @deprecated     This calculation is wrong!
        *                   The anchor point of the child limb has to be rotated like the parent child!
        **************************************************************************************/
        @Deprecated
        public final LibVertex transformAroundOtherLimb( Offset trans, BotMesh otherLimb, BotMesh ownLimb, LibVertex otherAnk )
        {
            //roate anchor for lower right arm and right hand
            LibVertex   ownAnk = otherAnk.copy();

                        ownAnk.x += trans.x;
                        ownAnk.y += trans.y;
                        ownAnk.z += trans.z;

                        translateAndRotateXYZ(  ownAnk.x,   ownAnk.y,   ownAnk.z,   0.0f,                   0.0f,                   0.0f,                   ownAnk, LibTransformationMode.EOriginalsToTransformed      );

            //pitch around all axis sequentially is necessary!
            ownAnk.rotateXYZ( otherLimb.iPitch.x,       0.0f,                       0.0f,                   otherAnk    );
            ownAnk.rotateXYZ( 0.0f,                     otherLimb.iPitch.y,         0.0f,                   otherAnk    );
            ownAnk.rotateXYZ( 0.0f,                     0.0f,                       otherLimb.iPitch.z,     otherAnk    );

          //ownAnk.rotateXYZ( otherLimb.iPitch.x,   otherLimb.iPitch.y,                       otherLimb.iPitch.z,                   otherAnk    );

            //translate and pitch around all axis sequentially

            translateAndRotateXYZ(  0.0f,       0.0f,       0.0f,       ownLimb.iPitch.x,       0.0f,                   0.0f,                   ownAnk, LibTransformationMode.ETransformedToTransformed    );
            translateAndRotateXYZ(  0.0f,       0.0f,       0.0f,       0.0f,                   ownLimb.iPitch.y,       0.0f,                   ownAnk, LibTransformationMode.ETransformedToTransformed    );
            translateAndRotateXYZ(  0.0f,       0.0f,       0.0f,       0.0f,                   0.0f,                   ownLimb.iPitch.z,       ownAnk, LibTransformationMode.ETransformedToTransformed    );

            return ownAnk;
        }
    }
