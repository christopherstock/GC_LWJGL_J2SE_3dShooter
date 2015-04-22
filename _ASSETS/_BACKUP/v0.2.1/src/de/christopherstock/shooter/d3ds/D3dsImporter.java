/*  $Id: Loader3dsmax.cs,v 1.5 2006/11/17 06:46:15 Christopher Stock Exp $
 *  =================================================================================
 */
    package de.christopherstock.shooter.d3ds;

    import  java.io.*;
    import  java.util.*;

import de.christopherstock.lib.g3d.*;
import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.gl.*;
import  de.christopherstock.shooter.ui.*;

    /********************************************************************************
    *   A 3d studio max ascii scene export file importer using regular expressions.
    *   Switched from enum to ordinary class in order to deliver instances of the meshes.
    *s
    *   @author     Christopher Stock
    *   @version    0.2
    ********************************************************************************/
    public class D3dsImporter
    {
        /********************************************************************************
        *   All available Discreet 3D studio max resource files. The ordinal index
        *   of the enum constant is the filename of the ascii scene export file (.ase).
        *
        *   @author     Christopher Stock
        *   @version    0.2
        ********************************************************************************/
        public enum D3dsFiles
        {
            EMeshLevelDesert1,
            EMeshEnemy1,
            EMeshExecutiveOffice1,
            EMeshExecutiveOffice1Door1,

            EMeshWoman1,
            EMeshWoman1head,
            EMeshWoman1torso,
            EMeshWoman1RightUpperArm,
            EMeshWoman1LeftUpperArm,
            EMeshWoman1RightLowerArm,
            EMeshWoman1LeftLowerArm,
            EMeshWoman1RightHand,
            EMeshWoman1LeftHand,
            EMeshWoman1RightUpperLeg,
            EMeshWoman1LeftUpperLeg,
            EMeshWoman1RightLowerLeg,
            EMeshWoman1LeftLowerLeg,
            EMeshWoman1RightFoot,
            EMeshWoman1LeftFoot,

            EMeshExecutiveOffice1GlassDoor,

            ETest,

            ETest2,
        }

        public  static  final   int                 POINTS_SCALATION            = 10;

        private static          D3dsImporter[]      originalFileData            = null;

        private                 FreeTriangle[]      faces                       = null;
        private                 MaxMaterial[]       materials3ds                = null;

        /**************************************************************************************
        *   references this class so all enum constants are being initiated
        *   and all 3dsmax-files will be parsed.
        **************************************************************************************/
        public static final void init()
        {
            originalFileData = new D3dsImporter[ D3dsFiles.values().length ];
            for ( int i = 0; i < D3dsFiles.values().length; ++i )
            {
                originalFileData[ i ] = new D3dsImporter( D3dsFiles.values()[ i ] );
            }
        }

        private D3dsImporter( D3dsFiles file )
        {
            Debug.d3ds.out( "=======================================" );
            Debug.d3ds.out( "Parsing 3dsmax-file [" + file.ordinal() + "]" );

            //open the AsciiSceneImport-file and the output-file
            try
            {
                //read the file
                String      fileSrc = readAse( file.ordinal() );

                //pick and parse materials
                String      chunkMaterialList   = Strings.getViaRegEx( fileSrc, "\\*MATERIAL_LIST \\{.+?\\n\\}" )[ 0 ];
                parseMaterials( chunkMaterialList );

                //pick and parse meshes
                String[]    chunksGeomObjects   = Strings.getViaRegEx( fileSrc, "\\*GEOMOBJECT \\{.+?\\n\\}"    );
                parseMeshes( chunksGeomObjects );
            }
            catch( Exception e )
            {
                Debug.bugfix.err( "i/o error on loading d3ds resource [" + file.ordinal() + "]" );
                e.printStackTrace();
                return;
            }
        }

        /**************************************************************************************
        *   Returns a COPY of an imported 3dsmax mesh.
        **************************************************************************************/
        public static final FreeTriangle[] getFaces( D3dsFiles file )
        {
            FreeTriangle[] copiedFaces = new FreeTriangle[ originalFileData[ file.ordinal() ].faces.length ];
            for ( int i = 0; i < copiedFaces.length; ++i )
            {
                copiedFaces[ i ] = originalFileData[ file.ordinal() ].faces[ i ].copy();
            }
            return copiedFaces;
        }

        private static final String readAse( int index )
        {
            BufferedReader inStream = new BufferedReader( new InputStreamReader( D3dsImporter.class.getResourceAsStream( Path.E3dsMax.url + index + ".ase" ) ) );

            try
            {
                StringBuffer sb = new StringBuffer();
                while ( true )
                {
                    String l = inStream.readLine();
                    if ( l == null ) break;
                    sb.append( l + "\n" );
                }
                return sb.toString();
            }
            catch ( IOException ioe )
            {
                Debug.bugfix.err( "ERROR loading 3ds max file [" + index + "]" );
                return null;
            }
        }

        private final void parseMaterials( String src )
        {
            //check if materials are defined
            String[] chunksMaterials = Strings.getViaRegEx( src, "\\t\\*MATERIAL \\d+ \\{.+?\\n\\t\\}" );
            if ( chunksMaterials != null )
            {
                //browse all material-chunks and assign all materials
                materials3ds = new MaxMaterial[ chunksMaterials.length ];
                for ( int i = 0; i < chunksMaterials.length; ++i )
                {
                    //get material name
                    String[][]  materialNameAa  = Strings.getViaRegExGrouped( chunksMaterials[ i ], 1, "\\*MATERIAL_NAME \"([^\"]+)\"" );
                    String      materialName    = materialNameAa[ 0 ][ 0 ];
                    Colors      materialColor   = Colors.getByName( materialName );

                    //get material offsets ( optional )
                    String[][] materialOffsetUaa = Strings.getViaRegExGrouped( chunksMaterials[ i ], 1, "\\*UVW_U_OFFSET ([\\-\\d\\.]+)" );
                    float      materialOffsetU   = ( materialOffsetUaa == null ? 0.0f : Float.parseFloat( materialOffsetUaa[ 0 ][ 0 ] ) );
                    String[][] materialOffsetVaa = Strings.getViaRegExGrouped( chunksMaterials[ i ], 1, "\\*UVW_V_OFFSET ([\\-\\d\\.]+)" );
                    float      materialOffsetV   = ( materialOffsetVaa == null ? 0.0f : Float.parseFloat( materialOffsetVaa[ 0 ][ 0 ] ) );

                    //get material offsets ( optional )
                    String[][] materialTilingUaa = Strings.getViaRegExGrouped( chunksMaterials[ i ], 1, "\\*UVW_U_TILING ([\\-\\d\\.]+)" );
                    float      materialTilingU   = ( materialTilingUaa == null ? 1.0f : Float.parseFloat( materialTilingUaa[ 0 ][ 0 ] ) );
                    String[][] materialTilingVaa = Strings.getViaRegExGrouped( chunksMaterials[ i ], 1, "\\*UVW_V_TILING ([\\-\\d\\.]+)" );
                    float      materialTilingV   = ( materialTilingVaa == null ? 1.0f : Float.parseFloat( materialTilingVaa[ 0 ][ 0 ] ) );

                    //assign material
                    materials3ds[ i ] = new MaxMaterial( materialName, materialColor, materialOffsetU, materialOffsetV, materialTilingU, materialTilingV  );
                    Debug.d3ds.out( "Material [" + i + "]: [" + materials3ds[ i ].name + "][" + materials3ds[ i ].offsetU + "][" + materials3ds[ i ].offsetV + "][" + materials3ds[ i ].tilingU + "][" + materials3ds[ i ].tilingV + "]" );
                }
            }
        }

        private final void parseMeshes( String[] meshesSrc )
        {
            Debug.d3ds.out( "meshes to parse: ["+meshesSrc.length+"]" );

            Vector<Face>        allFaces                = new Vector<Face>();
            MaxVertex[]         vertices3ds             = null;
            MaxFace[]           faces3ds                = null;
            MaxTextureVertex[]  textureVertices3ds      = null;

            //browse all meshes
            int cur = 0;
            for ( String meshSrc : meshesSrc )
            {
                //BufferedReader inStream = new BufferedReader( new StringReader( chunksGeomObjects[ i ] ) );

                //next texture!
                ++cur;
                Debug.d3ds.out( "\nImporting mesh # [" + cur + "]" );

                //get number of vertices
                String[][]  numVerticesAA   = Strings.getViaRegExGrouped( meshSrc, 1, "\\*MESH_NUMVERTEX (\\d+)" );
                int         numVertices     = Integer.parseInt( numVerticesAA[ 0 ][ 0 ] );
                            vertices3ds     = new MaxVertex[ numVertices ];
                Debug.d3ds.out( "number of vertices: [" + numVertices + "]" );

                //get number of faces
                String[][]  numFacesAA      = Strings.getViaRegExGrouped( meshSrc, 1, "\\*MESH_NUMFACES (\\d+)" );
                int         numFaces        = Integer.parseInt( numFacesAA[ 0 ][ 0 ] );
                            faces3ds        = new MaxFace[ numFaces ];
                Debug.d3ds.out( "number of faces: [" + numFaces + "]" );

                //get number of texture-vertices
                String[][]  numTVerticesAA      = Strings.getViaRegExGrouped( meshSrc, 1, "\\*MESH_NUMTVERTEX (\\d+)" );
                int         numTVertices        = Integer.parseInt( numTVerticesAA[ 0 ][ 0 ] );
                            textureVertices3ds  = new MaxTextureVertex[ numTVertices ];
                Debug.d3ds.out( "number of texture-vertices: [" + numTVertices + "]" );

                //read all vertices
                String[][]  verticesAA      = Strings.getViaRegExGrouped( meshSrc, 3, "\\*MESH_VERTEX\\s+\\d+\\s+([\\d\\.\\-]+)\\s+([\\d\\.\\-]+)\\s+([\\d\\.\\-]+)\\n" );
                Debug.d3ds.out( "parsing [" + verticesAA.length + "] vertices.." );
                //assign them
                for ( int i = 0; i < verticesAA.length; ++i )
                {
                    //assign all vertices
                    vertices3ds[ i ] = new MaxVertex
                    (
                        Float.parseFloat( verticesAA[ i ][ 0 ] ) / POINTS_SCALATION,
                        Float.parseFloat( verticesAA[ i ][ 1 ] ) / POINTS_SCALATION,
                        Float.parseFloat( verticesAA[ i ][ 2 ] ) / POINTS_SCALATION
                    );
                    //Debug.d3dsRegEx.out( "[" + verticesAA[ i ][ 0 ] + "][" + verticesAA[ 0 ][ 1 ] + "][" + verticesAA[ 0 ][ 2 ] + "]" );
                }

                //read all faces
                String[][]  facesAA         = Strings.getViaRegExGrouped( meshSrc, 3, "\\*MESH_FACE\\s+\\d+\\:\\s+A\\:\\s+([\\d\\.\\-]+)\\s+B\\:\\s+([\\d\\.\\-]+)\\s+C\\:\\s+([\\d\\.\\-]+)" );
                Debug.d3ds.out( "parsing [" + facesAA.length + "] faces.." );
                //assign them
                for ( int i = 0; i < facesAA.length; ++i )
                {
                    //assign all faces
                    faces3ds[ i ] = new MaxFace
                    (
                        vertices3ds[ Integer.parseInt( facesAA[ i ][ 0 ] ) ],
                        vertices3ds[ Integer.parseInt( facesAA[ i ][ 1 ] ) ],
                        vertices3ds[ Integer.parseInt( facesAA[ i ][ 2 ] ) ]
                    );
                }

                //read all texture-vertices
                String[][]  textureVerticesAA      = Strings.getViaRegExGrouped( meshSrc, 3, "\\*MESH_TVERT\\s+\\d+\\s+([\\d\\.\\-]+)\\s+([\\d\\.\\-]+)\\s+([\\d\\.\\-]+)\\n" );
                if ( textureVerticesAA != null )
                {
                    Debug.d3ds.out( "parsing [" + textureVerticesAA.length + "] texture-vertices.." );
                    //assign them
                    for ( int i = 0; i < textureVerticesAA.length; ++i )
                    {
                        //assign all vertices
                        textureVertices3ds[ i ] = new MaxTextureVertex
                        (
                            Float.parseFloat( textureVerticesAA[ i ][ 0 ] ),
                            Float.parseFloat( textureVerticesAA[ i ][ 1 ] )
                        );
                        //Debug.d3dsRegEx.out( "[" + textureVerticesAA[ i ][ 0 ] + "][" + textureVerticesAA[ i ][ 1 ] + "][" + textureVerticesAA[ i ][ 2 ] + "]" );
                    }
                }

                //read all texture-faces and assign them
                String[][]  textureFacesAA         = Strings.getViaRegExGrouped( meshSrc, 3, "\\*MESH_TFACE\\s+\\d+\\s+([\\d\\.\\-]+)\\s+([\\d\\.\\-]+)\\s+([\\d\\.\\-]+)\\n" );
                if ( textureFacesAA != null )
                {
                    Debug.d3ds.out( "parsing [" + textureFacesAA.length + "] texture-faces.." );
                    for ( int currentTextureFace = 0; currentTextureFace < faces3ds.length; ++currentTextureFace )
                    {
                        //assign all texture-faces
                        faces3ds[ currentTextureFace ].vertex1.u = textureVertices3ds[ Integer.parseInt( textureFacesAA[ currentTextureFace ][ 0 ] ) ].u;
                        faces3ds[ currentTextureFace ].vertex1.v = textureVertices3ds[ Integer.parseInt( textureFacesAA[ currentTextureFace ][ 0 ] ) ].v;
                        faces3ds[ currentTextureFace ].vertex2.u = textureVertices3ds[ Integer.parseInt( textureFacesAA[ currentTextureFace ][ 1 ] ) ].u;
                        faces3ds[ currentTextureFace ].vertex2.v = textureVertices3ds[ Integer.parseInt( textureFacesAA[ currentTextureFace ][ 1 ] ) ].v;
                        faces3ds[ currentTextureFace ].vertex3.u = textureVertices3ds[ Integer.parseInt( textureFacesAA[ currentTextureFace ][ 2 ] ) ].u;
                        faces3ds[ currentTextureFace ].vertex3.v = textureVertices3ds[ Integer.parseInt( textureFacesAA[ currentTextureFace ][ 2 ] ) ].v;
                    }
                }

                //set material used if an error occurs on mapping textures ..
                MaxMaterial material = new MaxMaterial( null, Colors.ERed, 0.0f, 0.0f, 1.0f, 1.0f );

                //get number of texture-vertices
                String[][]  materialRefAA       = Strings.getViaRegExGrouped( meshSrc, 1, "\\*MATERIAL_REF (\\d+)" );
                if ( materialRefAA != null )
                {
                    int         materialRef         = Integer.parseInt( materialRefAA[ 0 ][ 0 ] );
                    material = materials3ds[ materialRef ];
                    Debug.d3ds.out( "material ref is: [" + materialRef + "]" );
                }
                else
                {
                    Debug.d3ds.out( "This mesh has no material." );
                }

                //get current texture
                GLTexture tex = GLTexture.getByName( material.name );
                Debug.d3ds.out( "picked: " + tex );
                Debug.d3ds.out( "picked: " + material.name );

                //add all faces to the vector
                for( MaxFace face : faces3ds )
                {
                    //Debug.d3dsRegEx.out( "CREATE FACE!" + material.name );
                    try
                    {
                        //create free-triangle and add it to the faces stack
                        FreeTriangle ft = new FreeTriangle
                        (
                            new LibVertex( 0.0f, 0.0f, 0.0f ),
                            ( material.name == null || tex == null ? null : tex ),
                            material.color,
                            new LibVertex ( face.vertex1.y, face.vertex1.x, face.vertex1.z, ( face.vertex1.u + material.offsetU ) * material.tilingU, ( face.vertex1.v + material.offsetV ) * material.tilingV ),
                            new LibVertex ( face.vertex2.y, face.vertex2.x, face.vertex2.z, ( face.vertex2.u + material.offsetU ) * material.tilingU, ( face.vertex2.v + material.offsetV ) * material.tilingV ),
                            new LibVertex ( face.vertex3.y, face.vertex3.x, face.vertex3.z, ( face.vertex3.u + material.offsetU ) * material.tilingU, ( face.vertex3.v + material.offsetV ) * material.tilingV )
                        );
                        allFaces.add( ft );
                    }
                    catch ( Exception ioe )
                    {
                        Debug.bugfix.err( "I/O Exception on writing parsed ASE-File!" );
                        ioe.printStackTrace();
                    }
                }
            }

            //convert all faces from vector to array
            faces = allFaces.toArray( new FreeTriangle[] {} );

            //done
            Debug.d3ds.out( "done" );
        }
    }
