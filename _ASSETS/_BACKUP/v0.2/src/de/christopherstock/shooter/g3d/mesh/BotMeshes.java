/*  $Id: Mesh.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  de.christopherstock.shooter.io.d3ds.*;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     Christopher Stock
    *   @version    0.2
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
                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1head             ) ),
                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1torso            ) ),

                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1RightUpperArm    ) ),
                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1LeftUpperArm     ) ),

                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1RightLowerArm    ) ),
                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1LeftLowerArm     ) ),

                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1RightHand        ) ),
                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1LeftHand         ) ),

                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1RightUpperLeg    ) ),
                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1LeftUpperLeg     ) ),

                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1RightLowerLeg    ) ),
                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1LeftLowerLeg     ) ),

                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1RightFoot        ) ),
                new Mesh( D3dsImporter.getFaces( D3dsImporter.D3dsFiles.EMeshWoman1LeftFoot         ) ),
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
