/*  $Id: FaceTriangle.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d.face;

    import  java.awt.geom.*;
    import  java.util.*;
    import  javax.vecmath.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.game.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.io.d3ds.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.base.ShooterTexture.*;
import  de.christopherstock.shooter.g3d.*;
import de.christopherstock.shooter.g3d.wall.*;

    /**************************************************************************************
    *   Represents a triangle face.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public class FaceTriangle extends Face
    {
        public                  LibGameObject       iParentGameObject           = null;

        //highest point z and lowest point z
        public                  float               iLowestZ                    = 0.0f;
        public                  float               iHighestZ                   = 0.0f;

        public                  float               iLowestX                    = 0.0f;
        public                  float               iHighestX                   = 0.0f;
        public                  float               iLowestY                    = 0.0f;
        public                  float               iHighestY                   = 0.0f;

        public                  Line2D.Float        iCollisionLineHorz1         = null;
        public                  Line2D.Float        iCollisionLineHorz2         = null;
        public                  Line2D.Float        iCollisionLineHorz3         = null;

        public                  boolean             continueDestroyAnim         = true;

        public                  float               iDamageMultiplier           = 0.0f;

        /**************************************************************************************
        *   Copy constructor.
        **************************************************************************************/
        public FaceTriangle( LibMaxTriangle maxTriangle )
        {
            this
            (
                maxTriangle.iAnchor.copy(),
                ( ShooterTexture.getByName( maxTriangle.iTextureName ) == null ? null : ShooterTexture.getByName( maxTriangle.iTextureName ).getTexture() ),
                maxTriangle.iCol,
                maxTriangle.iA.copy(),
                maxTriangle.iB.copy(),
                maxTriangle.iC.copy(),
                ( maxTriangle.iFaceNormal == null ? null : maxTriangle.iFaceNormal.copy() )
            );
        }

        private FaceTriangle( LibVertex ank, LibGLTexture texture, LibColors col, LibVertex a, LibVertex b, LibVertex c, LibVertex aFaceNormal )
        {
            super( ank, texture, col, aFaceNormal );

            //set vertices
            setOriginalVertices( new LibVertex[] { a, b, c, } );

            //init all values
            updateCollisionValues();
        }

        public final void assignParentGameObject( LibGameObject aParentGameObject )
        {
            //connect to parent mesh
            iParentGameObject = aParentGameObject;
        }

        @Override
        protected final void setCollisionValues()
        {
            //set collission values
            float x1 = iTransformedVertices[ 0 ].x;
            float x2 = iTransformedVertices[ 1 ].x;
            float x3 = iTransformedVertices[ 2 ].x;

            float y1 = iTransformedVertices[ 0 ].y;
            float y2 = iTransformedVertices[ 1 ].y;
            float y3 = iTransformedVertices[ 2 ].y;

            float z1 = iTransformedVertices[ 0 ].z;
            float z2 = iTransformedVertices[ 1 ].z;
            float z3 = iTransformedVertices[ 2 ].z;

            //get minimum and maximum X Y and Z
            iLowestX  = (float)LibMath.min( x1, x2, x3 );
            iLowestY  = (float)LibMath.min( y1, y2, y3 );
            iLowestZ  = (float)LibMath.min( z1, z2, z3 );
            iHighestX = (float)LibMath.max( x1, x2, x3 );
            iHighestY = (float)LibMath.max( y1, y2, y3 );
            iHighestZ = (float)LibMath.max( z1, z2, z3 );

            //get all horizontal vertices in 2d-system
            Point2D.Float   ph1              = new Point2D.Float( x1, y1 );
            Point2D.Float   ph2              = new Point2D.Float( x2, y2 );
            Point2D.Float   ph3              = new Point2D.Float( x3, y3 );

            //calculate angle horz - why is the simplest min-max distance operative now?
            float   angleHorz       = LibMath.getAngleCorrect( new Point2D.Float( iLowestX, iLowestY ), new Point2D.Float( iHighestX, iHighestY ) );

            //angle vert is 0°
            float   angleVert       = 0.0f;

            //set angle vert to 90° if all z-points equal
            if ( z1 == z2 && z1 == z3 && z2 == z3 )
            {
                angleVert = 90.0f;
            }

           //angleVert           = LibMath.getAngleCorrect( new Point2D.Float( 0.0f, iLowestZ ), new Point2D.Float( 1.0f, iHighestZ ) );

            //assign angles and collision lines
            setFaceAngleHorz( angleHorz );
            setFaceAngleVert( angleVert );

            //assign XY collision lines between the three vertices
            iCollisionLineHorz1 = new Line2D.Float( ph1.x, ph1.y, ph2.x, ph2.y );
            iCollisionLineHorz2 = new Line2D.Float( ph1.x, ph1.y, ph3.x, ph3.y );
            iCollisionLineHorz3 = new Line2D.Float( ph2.x, ph2.y, ph3.x, ph3.y );
        }

        /***********************************************************************************
        *   Kai's algo.
        *
        *   @param  shot    The shot to be fired on this mesh.
        *   @return         The nearest hit-point with lots of additional information
        *                   or <code>null</code> if no hit-point was found.
        ***********************************************************************************/
        public LibHitPoint launchShotKai( LibShot shot )
        {
            //only visible faces can be hit
            switch ( iDrawMethod )
            {
                case EInvisible:
                {
                    break;
                }

                case EAlwaysDraw:
                case EHideIfTooDistant:
                {
                    LibVertex[] vertices = getVerticesToDraw();

                    Point3d p1 = new Point3d( vertices[ 0 ].x, vertices[ 0 ].y, vertices[ 0 ].z );
                    Point3d p2 = new Point3d( vertices[ 1 ].x, vertices[ 1 ].y, vertices[ 1 ].z );
                    Point3d p3 = new Point3d( vertices[ 2 ].x, vertices[ 2 ].y, vertices[ 2 ].z );

                    //get line-face-intersection
                    Point3d intersectionPoint = LibMathGeometry.getLineFaceIntersectionD( shot.iSrcPoint3d, shot.iEndPoint3d, p1, p2, p3 );

                    //check collision
                    if ( intersectionPoint == null )
                    {
                        return null;
                    }

                    Point2D.Float intersectionPointHorz = new Point2D.Float( (float)intersectionPoint.x, (float)intersectionPoint.y );

                    //get horizontal values
                    float           exactDistanceHorz     = (float)shot.iSrcPointHorz.distance( intersectionPointHorz  );                //get exact distance
                    float           shotAngleHorz         = LibMath.getAngleCorrect( shot.iSrcPointHorz, intersectionPointHorz );       //get angle between player and hit-point
                    float           invertedShotAngleHorz = 360.0f - ( shotAngleHorz - 180.0f  );                                       //get opposite direction of shot
                    float           sliverAngleHorz       = shotAngleHorz - iFaceAngleHorz * 2;                                         //get Sliver angle

                    //ShooterDebug.shotAndHit.out( "exactDistanceHorz:     [" + exactDistanceHorz + "]" );
                    //ShooterDebug.shotAndHit.out( "shotAngleHorz:         [" + shotAngleHorz + "]" );
                    //ShooterDebug.shotAndHit.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz + "]" );
                    //ShooterDebug.shotAndHit.out( "SliverAngleHorz:       [" + sliverAngleHorz + "]" );

                  //Point2D.Float   intersectionPointVert   = new Point2D.Float( 0, (float)intersectionPoint.z );

                    //get vertical values ( unused with new collision algo )
                  //float exactDistanceVert = (float)shot.iSrcPointVert.distance( intersectionPointVert );       //get exact distance

                    LibGLTexture  faceTexture   = getTexture();
                    ShooterMaterial faceMaterial  = ( faceTexture == null ? WallTex.EConcrete1.getTexture().getMaterial() : faceTexture.getMaterial() );

                    //ShooterDebug.bugfix.out( "DISTANCE [" + intersectionPoint.distance( shot.iSrcPoint3d ) + "] HIT ! shot ["+shot.iSrcPoint3d+"] to ["+shot.iEndPoint3d+"] FACE is ["+face1+"]["+face2+"]["+face3+"]" );
                    //FX.launchDebugPoint( new LibVertex( (float)intersectionPoint.x, (float)intersectionPoint.y, (float)intersectionPoint.z ), LibColors.EPink, 150, 0.03f );

                    if ( iLowestZ == iHighestZ )
                    {
                        sliverAngleHorz = LibMath.normalizeAngle( 360.0f - shotAngleHorz );
                    }

                    //return hit point
                    return new LibHitPoint
                    (
                        shot,
                        iParentGameObject,
                        faceMaterial.getBulletHoleTexture().getTexture(),
                        getTexture(),
                        faceMaterial.iSliverColors,
                        new LibVertex( (float)intersectionPoint.x, (float)intersectionPoint.y, (float)intersectionPoint.z ),
                        exactDistanceHorz,
                        shotAngleHorz,
                        invertedShotAngleHorz,
                        sliverAngleHorz,
                        iFaceAngleHorz,
                        iFaceAngleVert,
                        iDamageMultiplier
                    );
                }
            }

            return null;
        }

        /***********************************************************************************
        *   Using new raycast algo. ( incomplete? )
        *
        *   @param  shot    The shot to be fired on this mesh.
        *   @return         The nearest hit-point with lots of additional information
        *                   or <code>null</code> if no hit-point was found.
        ***********************************************************************************/
        public LibHitPoint launchShotNew( LibShot shot )
        {
            //only visible faces can be hit
            switch ( iDrawMethod )
            {
                case EInvisible:
                {
                    break;
                }

                case EAlwaysDraw:
                case EHideIfTooDistant:
                {
                    LibVertex[] vertices = getVerticesToDraw();

                    Point3d p1 = new Point3d( vertices[ 0 ].x, vertices[ 0 ].y, vertices[ 0 ].z );
                    Point3d p2 = new Point3d( vertices[ 1 ].x, vertices[ 1 ].y, vertices[ 1 ].z );
                    Point3d p3 = new Point3d( vertices[ 2 ].x, vertices[ 2 ].y, vertices[ 2 ].z );

                    //get line-face-intersection
                    Point3d intersectionPoint = LibMathGeometry.getLineFaceIntersectionD( shot.iSrcPoint3d, shot.iEndPoint3d, p1, p2, p3 );

                    //check collision
                    if ( intersectionPoint == null )
                    {
                        return null;
                    }

                    Point2D.Float intersectionPointHorz = new Point2D.Float( (float)intersectionPoint.x, (float)intersectionPoint.y );

                    //get horizontal values
                    float           exactDistanceHorz     = (float)shot.iSrcPointHorz.distance( intersectionPointHorz  );                //get exact distance
                    float           shotAngleHorz         = LibMath.getAngleCorrect( shot.iSrcPointHorz, intersectionPointHorz );       //get angle between player and hit-point
                    float           invertedShotAngleHorz = 360.0f - ( shotAngleHorz - 180.0f  );                                       //get opposite direction of shot
                    float           sliverAngleHorz       = shotAngleHorz - iFaceAngleHorz * 2;                                         //get Sliver angle

                    //ShooterDebug.shotAndHit.out( "exactDistanceHorz:     [" + exactDistanceHorz + "]" );
                    //ShooterDebug.shotAndHit.out( "shotAngleHorz:         [" + shotAngleHorz + "]" );
                    //ShooterDebug.shotAndHit.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz + "]" );
                    //ShooterDebug.shotAndHit.out( "SliverAngleHorz:       [" + sliverAngleHorz + "]" );

                  //Point2D.Float   intersectionPointVert   = new Point2D.Float( 0, (float)intersectionPoint.z );

                    //get vertical values ( unused with new collision algo )
                  //float exactDistanceVert = (float)shot.iSrcPointVert.distance( intersectionPointVert );       //get exact distance

                    LibGLTexture  faceTexture   = getTexture();
                    ShooterMaterial faceMaterial  = ( faceTexture == null ? WallTex.EConcrete1.getTexture().getMaterial() : faceTexture.getMaterial() );

                    //ShooterDebug.bugfix.out( "DISTANCE [" + intersectionPoint.distance( shot.iSrcPoint3d ) + "] HIT ! shot ["+shot.iSrcPoint3d+"] to ["+shot.iEndPoint3d+"] FACE is ["+face1+"]["+face2+"]["+face3+"]" );
                    //FX.launchDebugPoint( new LibVertex( (float)intersectionPoint.x, (float)intersectionPoint.y, (float)intersectionPoint.z ), LibColors.EPink, 150, 0.03f );

                    if ( iLowestZ == iHighestZ )
                    {
                        sliverAngleHorz = LibMath.normalizeAngle( 360.0f - shotAngleHorz );
                    }

                    //return hit point
                    return new LibHitPoint
                    (
                        shot,
                        iParentGameObject,
                        faceMaterial.getBulletHoleTexture().getTexture(),
                        getTexture(),
                        faceMaterial.iSliverColors,
                        new LibVertex( (float)intersectionPoint.x, (float)intersectionPoint.y, (float)intersectionPoint.z ),
                        exactDistanceHorz,
                        shotAngleHorz,
                        invertedShotAngleHorz,
                        sliverAngleHorz,
                        iFaceAngleHorz,
                        iFaceAngleVert,
                        iDamageMultiplier
                    );
                }
            }

            return null;
        }

        /***********************************************************************************
        *   Only suitable for faces that have collision lines specified!
        *
        *   @param  shot    The shot to be fired on this mesh.
        *   @return         The nearest hit-point with lots of additional information
        *                   or <code>null</code> if no hit-point was found.
        ***********************************************************************************/
        //@Deprecated
        public LibHitPoint launchShotOld( LibShot shot )
        {
            //only visible faces can be hit
            switch ( iDrawMethod )
            {
                case EInvisible:
                {
                    break;
                }

                case EAlwaysDraw:
                case EHideIfTooDistant:
                {
                    //horizontal collission lines have to be present
                    if ( iCollisionLineHorz1 == null || iCollisionLineHorz2 == null || iCollisionLineHorz3 == null ) return null;

                    Point2D.Float intersectionPointHorz = null;

                    //we could check if face's z are equal - but the new shot algo is better! :-)

                    //check horizontal collision
                    if ( shot.iLineShotHorz.intersectsLine( iCollisionLineHorz1  ) )
                    {
                        //get intersection point horz
                        //ShooterDebug.shotAndHit.out( "==============\nHORZ FACE HIT!" );
                        intersectionPointHorz = LibMathGeometry.findLineSegmentIntersection( shot.iLineShotHorz, iCollisionLineHorz1, ShooterDebug.error );
                        if ( intersectionPointHorz == null )
                        {
                            ShooterDebug.error.err( "Intersection Point not calculated due to buggy external API." );
                            return null;
                        }
                    }
                    else if ( shot.iLineShotHorz.intersectsLine( iCollisionLineHorz2  ) )
                    {
                        //get intersection point horz
                        //ShooterDebug.shotAndHit.out( "==============\nHORZ FACE HIT!" );
                        intersectionPointHorz = LibMathGeometry.findLineSegmentIntersection( shot.iLineShotHorz, iCollisionLineHorz2, ShooterDebug.error );
                        if ( intersectionPointHorz == null )
                        {
                            ShooterDebug.error.err( "Intersection Point not calculated due to buggy external API." );
                            return null;
                        }
                    }
                    else if ( shot.iLineShotHorz.intersectsLine( iCollisionLineHorz3 ) )
                    {
                        //get intersection point horz
                        //ShooterDebug.shotAndHit.out( "==============\nHORZ FACE HIT!" );
                        intersectionPointHorz = LibMathGeometry.findLineSegmentIntersection( shot.iLineShotHorz, iCollisionLineHorz3, ShooterDebug.error );
                        if ( intersectionPointHorz == null )
                        {
                            ShooterDebug.error.err( "Intersection Point not calculated due to buggy external API." );
                            return null;
                        }
                    }
                    else
                    {
                        return null;
                    }

                    //get horizontal values
                    float           exactDistanceHorz     = (float)shot.iSrcPointHorz.distance( intersectionPointHorz );                //get exact distance
                    float           shotAngleHorz         = LibMath.getAngleCorrect( shot.iSrcPointHorz, intersectionPointHorz );       //get angle between player and hit-point
                    float           invertedShotAngleHorz = 360.0f - ( shotAngleHorz - 180.0f  );                                       //get opposite direction of shot
                    float           sliverAngleHorz       = shotAngleHorz - iFaceAngleHorz * 2;                                         //get Sliver angle
/*
                    ShooterDebug.shotAndHit.out( "exactDistanceHorz:     [" + exactDistanceHorz + "]" );
                    ShooterDebug.shotAndHit.out( "shotAngleHorz:         [" + shotAngleHorz + "]" );
                    ShooterDebug.shotAndHit.out( "invertedShotAngleHorz: [" + invertedShotAngleHorz + "]" );
                    ShooterDebug.shotAndHit.out( "SliverAngleHorz:       [" + sliverAngleHorz + "]" );
*/
                    //calculate face's vertical collision line ( if we assume that this is a straight vertical face! )
                    Line2D.Float collisionLineVert = new Line2D.Float( new Point2D.Float( exactDistanceHorz, iLowestZ ), new Point2D.Float( exactDistanceHorz, iHighestZ ) );
                    //ShooterDebug.shotAndHit.out( "face's collision line vert is: [" + collisionLineVert + "]" );

                    if ( !shot.iLineShotVert.intersectsLine( collisionLineVert ) )
                    {
                        //ShooterDebug.shotAndHit.out( "VERTICAL FACE MISSED!" );
                        return null;
                    }

                    //get then intersection point for the vertical axis
                    //ShooterDebug.shotAndHit.out( "VERTICAL FACE HIT!" );
                    Point2D.Float   intersectionPointVert   = LibMathGeometry.findLineSegmentIntersection( shot.iLineShotVert, collisionLineVert, ShooterDebug.error );
                    float           z                       = intersectionPointVert.y;
                    //ShooterDebug.shotAndHit.out( ">> INTERSECTION POINT VERT: " + intersectionPointVert );

                    //get vertical values
                  //float exactDistanceVert = (float)shot.iSrcPointVert.distance( intersectionPointVert );       //get exact distance

                  //ShooterDebug.shotAndHit.out( ">> EXACT DISTANCE VERT: " + exactDistanceVert );
//                  ShooterDebug.shot.out( ">> SHOT-ANGLE-VERT: " + shotAngleVert );
//                  ShooterDebug.shot.out( ">> SLIVER-ANGLE-VERT: " + SliverAngleVert );

                    LibGLTexture  faceTexture   = getTexture();
                    ShooterMaterial faceMaterial  = ( faceTexture == null ? WallTex.EConcrete1.getTexture().getMaterial() : faceTexture.getMaterial() );

                    if ( iLowestZ == iHighestZ )
                    {
                        sliverAngleHorz = shotAngleHorz;
                    }

                    //return hit point
                    return new LibHitPoint
                    (
                        shot,
                        iParentGameObject,
                        faceMaterial.getBulletHoleTexture().getTexture(),
                        getTexture(),
                        faceMaterial.iSliverColors,
                        new LibVertex( intersectionPointHorz.x, intersectionPointHorz.y, z ),
                        exactDistanceHorz,
                        shotAngleHorz,
                        invertedShotAngleHorz,
                        sliverAngleHorz,
                        iFaceAngleHorz,
                        iFaceAngleVert,
                        iDamageMultiplier
                    );
                }
            }

            return null;
        }

        public final FaceTriangle copy()
        {
            LibVertex ankCopy  = getAnchor().copy();
            LibVertex aCopy    = new LibVertex( iOriginalVertices[ 0 ] );
            LibVertex bCopy    = new LibVertex( iOriginalVertices[ 1 ] );
            LibVertex cCopy    = new LibVertex( iOriginalVertices[ 2 ] );
            LibVertex normCopy = ( iNormal == null ? null : iNormal.copy() );
            return new FaceTriangle
            (
                ankCopy,
                getTexture(),
                getColor(),
                aCopy,
                bCopy,
                cCopy,
                normCopy
            );
        }

        @Override
        public boolean checkCollisionHorz( Cylinder cylinder )
        {
            //this method is never called?

            //cylinders will not collide on floors
            //if ( face.iLowestZ == face.iHighestZ ) return false;

            return checkCollisionHorz( cylinder, true, false );
        }

        public boolean checkCollisionHorz( Cylinder cylinder, boolean useBottomToleranceZ, boolean invertBottomTolerance   )
        {
            //cylinders will not collide on floors
            //if ( face.iLowestZ == face.iHighestZ ) return false;
            return cylinder.checkCollisionHorzLines( this, useBottomToleranceZ, invertBottomTolerance   );
        }

        @Override
        public Vector<Float> checkCollisionVert( Cylinder cylinder )
        {
            Vector<Float> v = new Vector<Float>();

            //check horz intersection
            if ( cylinder.getCircle().intersects( new Rectangle2D.Float( iLowestX, iLowestY, iHighestX - iLowestX, iHighestY - iLowestY ) ) )
            {
                //do not check heights intersection !
                // if ( cylinder.heightsIntersect( iLowestZ, iHighestZ, false ) )
                {
                    //ShooterDebug.bugfix.out("INTERSECT - return float with z [" + iHighestZ + "]");
                    v.add( new Float( iHighestZ ) );
                }
            }

            return v;
        }

        public void setDamageMultiplier( float aDamageMultiplier )
        {
            iDamageMultiplier = aDamageMultiplier;
        }
/*
        public final Float checkCollision( Point2D.Float point )
        {
            //create the collision rectangle of this triangle face
            Rectangle2D.Float faceBaseHorz = new Rectangle2D.Float( minX, minY, maxX - minX, maxY - minY );

            //check if player is in rect
            if ( faceBaseHorz.contains( point ) )    //if ( Player.user.cylinder.checkHorzCenterCollision( x1, y1, x2, y2 ) )    //old solution
            {
                //add this z point to the values
                return new Float( iHighestZ );
            }

            return null;
        }
*/
    }