/*  $Id: Mesh.java,v 1.1 2007/09/02 14:19:19 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.gl.g3d;

    import  de.christopherstock.shooter.game.collision.Collision.*;
import de.christopherstock.shooter.gl.mesh.*;
    import  de.christopherstock.shooter.io.d3ds.*;

import  static  de.christopherstock.shooter.io.d3ds.D3dsImporter.D3dsFiles;

    /**************************************************************************************
    *   Represents a mesh.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class Meshes
    {
        /**************************************************************************************
        *   The 1st level: desert.
        **************************************************************************************/
        public  static          Mesh                levelDesert1                    = null;
        public  static          Mesh                enemy1                          = null;
        public  static          Mesh                executiveOffice1                = null;
        public  static          Mesh                executiveOffice1Door1           = null;
        public  static          Mesh                executiveOffice1Door2           = null;

        public static final void init()
        {
            levelDesert1 = new Mesh
            (
                D3dsImporter.getFaces( D3dsFiles.EMeshLevelDesert1 ),
                new Vertex( 0.0f, 0.0f, 0.0f ),
                CollisionEnabled.YES,
                Mesh.MeshAction.ENone
            );

            enemy1 = new Mesh
            (
                D3dsImporter.getFaces( D3dsFiles.EMeshEnemy1 ),
                new Vertex( 0.0f, 0.0f, 0.0f ), CollisionEnabled.NO,
                Mesh.MeshAction.ENone
            );


            executiveOffice1 = new Mesh
            (
                D3dsImporter.getFaces( D3dsFiles.EMeshExecutiveOffice1 ),
                new Vertex( 14.0f, 8.0f, 0.01f ), CollisionEnabled.YES,
                Mesh.MeshAction.ENone
            );

            executiveOffice1Door1 = new Mesh
            (
                D3dsImporter.getFaces( D3dsFiles.EMeshExecutiveOffice1Door1 ),
                new Vertex( 7.95f, 9.225f, 0.01f ),
                CollisionEnabled.YES,
                Mesh.MeshAction.EDoorSliding
            );

            executiveOffice1Door2 = new Mesh
            (
                D3dsImporter.getFaces( D3dsFiles.EMeshExecutiveOffice1Door1 ),
                new Vertex( 5.95f, 5.225f, 0.01f ),
                CollisionEnabled.YES,
                Mesh.MeshAction.EDoorTurning
            );
        }
/*
        public  static  final   Mesh                testBoxImported                 = new Mesh
        (
            D3dsImporter.getFaces( D3dsImporter.EMeshTestBox1 ),
            0.0f,
            0.0f,
            0.0f
        );

        public  static  final   Mesh                teapotImported                  = new Mesh
        (
            D3dsImporter.getFaces( D3dsImporter.EMeshTestTeapot ),
            2.0f,
            2.0f,
            0.0f
        );
*/

/*
        public  static  final   Mesh                shotgun                       = new Mesh
        (
            D3dsImporter.getFaces( D3dsImporter.EMeshTestShotgun ),
            0.0f,
            0.0f,
            0.0f
        );


        public  static  final   Mesh                testTorusKnot                   = new Mesh
        (
            D3dsImporter.getFaces( D3dsImporter.EMeshTestTorusKnot ),
            0.0f,
            0.0f,
            0.0f
        );
*/
/*
        public  static  final   Mesh                multifaceImported               = new Mesh
        (
            D3dsImporter.getFaces( D3dsImporter.EMeshTestMultifaced ),
            0.0f,
            0.0f,
            0.0f
        );
*/
/*
        public  static  final   Mesh                testWalls                       = new Mesh
        (
            new Face[]
            {
                new FaceQuadWall(    Texture.ETest, Colors.EDarkGrey,   0.0f, 1.0f, 0.0f,       -0.5f, 2.0f, 1.0f,      Tiling.ETilingDefaultX, Tiling.ETilingDefaultY, true ,false ),
                new FaceQuadWall(    Texture.ETest, Colors.EDarkGrey,   0.0f, 1.0f, 0.0f,       1.0f, -1.0f, 1.0f,      Tiling.ETilingDefaultX, Tiling.ETilingDefaultY, true ,false ),
                new FaceQuadWall(    Texture.ETest, Colors.EDarkGrey,   1.0f, 0.0f, 0.0f,       1.0f, 0.0f, 1.0f,       Tiling.ETilingDefaultX, Tiling.ETilingDefaultY, true ,false ),
            },
            2.0f,
            1.0f,
            0.0f
        );
*/
    }
