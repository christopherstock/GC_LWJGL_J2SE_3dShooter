/*  $Id: Mesh.java 300 2011-02-22 00:29:11Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.util.*;
    import  de.christopherstock.lib.Lib.Offset;
    import  de.christopherstock.lib.Lib.Rotation;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class BotMesh extends Mesh
    {
        public                      Rotation            pitch                   = new Rotation();
        public                      float               iLimbSpeed              = 0.0f;
        public                      Vector<Rotation>    targetPitch             = new Vector<Rotation>();

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  aFaces              All faces this mesh shall consist of.
        *   @param  aAnchor             The meshes' anchor point.
        **************************************************************************************/
        public BotMesh( FaceTriangle[] aFaces, LibVertex aAnchor, float aInitRotZ, float aInitScale, GameObject aParentGameObject )
        {
            super( aFaces, aAnchor, aInitRotZ, aInitScale, aParentGameObject, LibTransformationMode.EOriginalsToTransformed );
        }

        public final LibVertex transformOwn( Offset trans )
        {
            //rotate
            LibVertex   limbAnk = getAnchor().copy();

                        limbAnk.x += trans.x;
                        limbAnk.y += trans.y;
                        limbAnk.z += trans.z;

            //translate and turn around x, y and z axis sequentially
            translateAndRotateXYZ(  limbAnk.x,  limbAnk.y,  limbAnk.z,  pitch.x,    0.0f,       0.0f,       limbAnk,    LibTransformationMode.EOriginalsToTransformed      );
            translateAndRotateXYZ(  0.0f,       0.0f,       0.0f,       0.0f,       pitch.y,    0.0f,       limbAnk,    LibTransformationMode.ETransformedToTransformed    );
            translateAndRotateXYZ(  0.0f,       0.0f,       0.0f,       0.0f,       0.0f,       pitch.z,    limbAnk,    LibTransformationMode.ETransformedToTransformed    );

            return limbAnk;
        }

        public final LibVertex transformAroundOtherLimb( Offset trans, BotMesh otherLimb, BotMesh ownLimb, LibVertex otherAnk )
        {
            //roate anchor for lower right arm and right hand
            LibVertex   ownAnk = otherAnk.copy();
                        ownAnk.x += trans.x;
                        ownAnk.y += trans.y;
                        ownAnk.z += trans.z;

            //pitch around all axis sequentially
            ownAnk.rotateXYZ( otherLimb.pitch.x,   0.0f,                       0.0f,                   otherAnk    );
            ownAnk.rotateXYZ( 0.0f,                     otherLimb.pitch.y,     0.0f,                   otherAnk    );
            ownAnk.rotateXYZ( 0.0f,                     0.0f,                       otherLimb.pitch.z, otherAnk    );

            //translate and pitch around all axis sequentially
            translateAndRotateXYZ(  ownAnk.x,   ownAnk.y,   ownAnk.z,   ownLimb.pitch.x,   0.0f,                   0.0f,                   ownAnk, LibTransformationMode.EOriginalsToTransformed      );
            translateAndRotateXYZ(  0.0f,       0.0f,       0.0f,       0.0f,                   ownLimb.pitch.y,   0.0f,                   ownAnk, LibTransformationMode.ETransformedToTransformed    );
            translateAndRotateXYZ(  0.0f,       0.0f,       0.0f,       0.0f,                   0.0f,                   ownLimb.pitch.z,   ownAnk, LibTransformationMode.ETransformedToTransformed    );

            return ownAnk;
        }

        public final void setTargetPitchs( Rotation[] targetPitchs )
        {
            targetPitch = new Vector<Rotation>( Arrays.asList( targetPitchs ) );
        }

        public final boolean reachToTargetPitch( int currentTargetPitch )
        {
            //check if the target pitch is already reached
            boolean skipReach = false;
            boolean reached   = false;
            if ( pitch.sameAsRounded( targetPitch.elementAt( currentTargetPitch ) ) )
            {
                if ( targetPitch.size() == 1 )
                {
                    skipReach = true;
                }
                else
                {
                    reached = true;
                }
            }

            if ( !skipReach )
            {
                pitch.reachToAbsolute( targetPitch.elementAt( currentTargetPitch ), targetPitch.elementAt( currentTargetPitch ).iSpeed );
            }

            return reached;
        }
    }
