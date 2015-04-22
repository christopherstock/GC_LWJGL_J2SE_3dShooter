/*  $Id: BotMeshes.java 258 2011-02-10 00:11:05Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public class BotMeshes extends MeshCollection
    {
      //public                      Bot             parentBot               = null;

        public                      boolean         debugHeadTurnsLeft      = false;
        public                      float           debugHeadTurn           = 0.0f;

        /**************************************************************************************
        *   Constructs a new mesh with the specified properties.
        *
        *   @param  aFaces              All faces this mesh shall consist of.
        *   @param  anchor              The meshes' anchor point.
        *   @param  aCollisionEnabled   Specifies if the triangle-faces of this mesh shall throw collisions on firing.
        *   @param  aSpecialObject      This mesh may have functionality.
        **************************************************************************************/
        public BotMeshes()
        {
            meshes = new Mesh[]
            {
                D3dsFiles.getFaces( D3dsFiles.EWoman1head             ),
                D3dsFiles.getFaces( D3dsFiles.EWoman1torso            ),

                D3dsFiles.getFaces( D3dsFiles.EWoman1RightUpperArm    ),
                D3dsFiles.getFaces( D3dsFiles.EWoman1LeftUpperArm     ),

                D3dsFiles.getFaces( D3dsFiles.EWoman1RightLowerArm    ),
                D3dsFiles.getFaces( D3dsFiles.EWoman1LeftLowerArm     ),

                D3dsFiles.getFaces( D3dsFiles.EWoman1RightHand        ),
                D3dsFiles.getFaces( D3dsFiles.EWoman1LeftHand         ),

                D3dsFiles.getFaces( D3dsFiles.EWoman1RightUpperLeg    ),
                D3dsFiles.getFaces( D3dsFiles.EWoman1LeftUpperLeg     ),

                D3dsFiles.getFaces( D3dsFiles.EWoman1RightLowerLeg    ),
                D3dsFiles.getFaces( D3dsFiles.EWoman1LeftLowerLeg     ),

                D3dsFiles.getFaces( D3dsFiles.EWoman1RightFoot        ),
                D3dsFiles.getFaces( D3dsFiles.EWoman1LeftFoot         ),
            };
        }

        @Override
        public void translateAndRotateXYZ( float tX, float tY, float tZ, float rotX, float rotY, float rotZ )
        {
            //browse all meshes
            for ( int i = 0; i < meshes.length; ++i )
            {
                if ( i == 0 )
                {
                    meshes[ i ].translateAndRotateXYZ( tX, tY, tZ, rotX, rotY, rotZ + debugHeadTurn );

                    // test head turn

                    if ( debugHeadTurnsLeft )
                    {
                        debugHeadTurn -= 5;

                        if ( debugHeadTurn < -30 ) debugHeadTurnsLeft = false;
                    }
                    else
                    {
                        debugHeadTurn += 5;

                        if ( debugHeadTurn >  30 ) debugHeadTurnsLeft = true;
                    }
                }
                else
                {
                    meshes[ i ].translateAndRotateXYZ( tX, tY, tZ, rotX, rotY, rotZ );
                }
            }
        }
    }
