/*  $Id: WallCollection.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.mesh;

    import  java.util.*;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.shooter.g3d.*;

    /**************************************************************************************
    *   Represents a mesh-collection.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class WallCollection extends MeshCollection
    {
        public WallCollection( Wall[] aWalls )
        {
            this( null, aWalls );
        }

        public WallCollection( Wall aAnchorWall, Wall[] aWalls )
        {
            iMeshes       = aWalls;

            //translate all walls by anchorWall if specified
            if ( aAnchorWall != null )
            {
                Vector<Mesh> newWalls = new Vector<Mesh>();
                newWalls.add( aAnchorWall );
                LibVertex roomAnk    = aAnchorWall.getAnchor();
                for ( Mesh wall : iMeshes )
                {
                    //fixed double translation :p
                    LibVertex newWallAnk = new LibVertex( 0.0f, 0.0f, 0.0f ); //wall.getAnchor().copy();

                    //translate and rotate anchor by room offset and set as new anchor
                    newWallAnk.translate( roomAnk );
                    newWallAnk.rotateXYZ( 0.0f, 0.0f, aAnchorWall.iStartupRotZ, aAnchorWall.getAnchor() );

                    //set anchor and perform translation on originals
                    wall.setNewAnchor( newWallAnk, true, LibTransformationMode.EOriginalsToOriginals );

                    //turn wall by new anchor for room rotation
                    wall.translateAndRotateXYZ
                    (
                        0.0f,
                        0.0f,
                        0.0f,
                        0.0f,
                        0.0f,
                        aAnchorWall.iStartupRotZ,
                        newWallAnk,
                        LibTransformationMode.EOriginalsToOriginals
                    );

                    //ShooterDebug.bugfix.out("new ank: ["+newAnk+"]");
                    newWalls.add( wall );
                }
                iMeshes = newWalls.toArray( iMeshes );
                //ShooterDebug.bugfix.out("new wc: ["+walls.length+"]");
            }
        }

        public final void launchAction( Cylinder cylinder )
        {
            //launch action on all meshes
            for ( Mesh wall : iMeshes )
            {
                ( (Wall)wall ).launchAction( cylinder );
            }
        }

        public final void animate()
        {
            //animate all meshes
            for ( Mesh wall : iMeshes )
            {
                ( (Wall)wall ).animate();
            }
        }
    }
