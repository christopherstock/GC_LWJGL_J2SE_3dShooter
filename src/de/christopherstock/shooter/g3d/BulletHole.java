/*  $Id: BulletHole.java 1284 2014-10-08 21:57:48Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.g3d;

    import  java.awt.geom.*;
    import  java.util.Vector;
    import  de.christopherstock.lib.Lib.Invert;
    import  de.christopherstock.lib.Lib.LibTransformationMode;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.g3d.face.*;
    import  de.christopherstock.lib.g3d.face.LibFace.*;
    import  de.christopherstock.lib.game.*;
    import  de.christopherstock.lib.io.d3ds.LibD3dsFile;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSettings.BulletHoles;
    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.g3d.wall.Wall;
    import  de.christopherstock.shooter.game.objects.*;

    /**************************************************************************************
    *   Represents a bullet-hole on a wall or an object.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public class BulletHole
    {
        private     static          Vector<BulletHole>      bulletHoles                                     = new Vector<BulletHole>();

        private                     LibHitPoint             iHitPoint                                       = null;
        private                     Mesh                    iProjectile                                     = null;
        private                     LibFaceEllipseWall      iFace                                           = null;
        private                     LibD3dsFile             iProjectileTemplate                             = null;
        private                     LibVertex               iOriginalPosition                               = null;
        private                     LibVertex               iPosition                                       = null;
        private                     float                   iCarriersLastFaceAngle                          = 0.0f;

        public BulletHole( LibHitPoint aHitPoint, LibD3dsFile aProjectile )
        {
            iHitPoint           = aHitPoint;
            iProjectileTemplate = aProjectile;

            if ( iProjectileTemplate != null )
            {
                float rotZ = iHitPoint.iHorzShotAngle + 90.0f;

                //only set once .. :/
                iProjectile = new Mesh( ShooterD3ds.getFaces( iProjectileTemplate ), iHitPoint.iVertex, 0.0f, 1.0f, Invert.ENo, iHitPoint.iCarrier, LibTransformationMode.EOriginalsToOriginals, DrawMethod.EAlwaysDraw );
                iProjectile.translateAndRotateXYZ
                (
                    0.0f,
                    0.0f,
                    0.0f,
                     LibMath.sinDeg( rotZ ) * iHitPoint.iShot.iRotX,
                    -LibMath.cosDeg( rotZ ) * iHitPoint.iShot.iRotX,    //lucky punch
                    LibMath.normalizeAngle( rotZ ),
                    iProjectile.iAnchor,
                    LibTransformationMode.EOriginalsToTransformed
                );

                //ShooterDebug.bugfix.out( "> " +  iHitPoint.iShot.iRotX );
            }

           //ShooterDebug.bugfix.out( "Vert face angle set to [" + iVertFaceAngle + "]" );

            ShooterDebug.bulletHole.out( "new bullet hole: face-angle h [" + iHitPoint.iHorzFaceAngle + "] v [" + iHitPoint.iVertFaceAngle + "]" );

            //get suitable distance to avoid overlapping bullet-holes
            float distanceHorzFromFace  = getSuitableDistanceFromHorzFace();
            float distanceVertFromFace  = getSuitableDistanceFromVertFace();
            float distX                 = -distanceHorzFromFace * -LibMath.cosDeg( aHitPoint.iHorzFaceAngle );
            float distY                 =  distanceHorzFromFace *  LibMath.sinDeg( aHitPoint.iHorzFaceAngle );
            float distZ                 = -distanceVertFromFace *  LibMath.sinDeg( aHitPoint.iVertFaceAngle );

            //get the two holes that hit the wall from both sides
            Point2D.Float pointHorz1 = new Point2D.Float( aHitPoint.iVertex.x + distX, aHitPoint.iVertex.y + distY );
            Point2D.Float pointHorz2 = new Point2D.Float( aHitPoint.iVertex.x - distX, aHitPoint.iVertex.y - distY );
            ShooterDebug.bulletHole.out( " bullet-hole: [" + pointHorz1.x + "][" + pointHorz1.y + "] [" + pointHorz2.x + "][" + pointHorz2.y + "]" );

            Point2D.Float pointVert1 = new Point2D.Float( aHitPoint.iShot.iSrcPointVert.x, aHitPoint.iVertex.z - distZ );
            Point2D.Float pointVert2 = new Point2D.Float( aHitPoint.iShot.iSrcPointVert.x, aHitPoint.iVertex.z + distZ );

            //select the nearer point
            float distanceHorz1 = (float)aHitPoint.iShot.iSrcPointHorz.distance( pointHorz1 );
            float distanceHorz2 = (float)aHitPoint.iShot.iSrcPointHorz.distance( pointHorz2 );


            Point2D.Float nearerHolePointHorz = ( distanceHorz1 < distanceHorz2 ? pointHorz1 : pointHorz2 );
            //Point2D.Float nearerHolePointHorz = ( distanceHorz1 < distanceHorz2 ? pointHorz2 : pointHorz1 );

            float distanceVert1 = (float)aHitPoint.iShot.iSrcPointVert.distance( pointVert1 );
            float distanceVert2 = (float)aHitPoint.iShot.iSrcPointVert.distance( pointVert2 );
/*
            if ( distanceVert1 < distanceVert2 )
            {
                ShooterDebug.bugfix.out( "higher is nearer" );
            }
            else
            {
                ShooterDebug.bugfix.out( "lower is nearer" );
            }
*/
            Point2D.Float nearerHolePointVert = ( distanceVert1 < distanceVert2 ? pointVert1 : pointVert2 );

            //ShooterDebug.bugfix.out( "dis v: "  + distZ );

            //assign the position of the Bullet Hole
            iOriginalPosition  = new LibVertex( nearerHolePointHorz.x, nearerHolePointHorz.y, nearerHolePointVert.y );
            iPosition          = new LibVertex( nearerHolePointHorz.x, nearerHolePointHorz.y, nearerHolePointVert.y );

            //check distance to Carrier if any
            if ( aHitPoint.iCarrier != null )
            {
                iCarriersLastFaceAngle = aHitPoint.iCarrier.getCarriersFaceAngle();
            }

            //setup the face
            updateFace( true );
        }

        private final void updateFace( boolean newRandomTexRot )
        {
            iFace = new LibFaceEllipseWall
            (
                ShooterDebug.face,
                iHitPoint.iBulletHoleTexture,
                iHitPoint.iHorzFaceAngle,
                iHitPoint.iVertFaceAngle,
                iPosition.x,
                iPosition.y,
                iPosition.z,
                iHitPoint.iShot.iBulletHoleSize.size,
                iHitPoint.iShot.iBulletHoleSize.size,
                ( newRandomTexRot ?  LibMath.getRandom( 0, 360 ) : iFace.iTextureRotation ),
                ShooterSettings.Performance.ELLIPSE_SEGMENTS
            );
        }

        public static final void drawAll()
        {
            //draw all points
            for ( BulletHole bulletHole : BulletHole.bulletHoles )
            {
                if ( bulletHole.iProjectileTemplate != null )
                {
                    //projectile
                    if ( bulletHole.iProjectile != null )
                    {
                        bulletHole.iProjectile.draw();
                    }
                }
                else
                {
                    //bullet hole
                    bulletHole.iFace.draw();
                }
            }
        }

        public static final void translateAll( LibGameObject bhc, float transX, float transY, float transZ )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : BulletHole.bulletHoles )
            {
                //check if this bullet hole belongs to the specified bot
                if ( bulletHole.iHitPoint.iCarrier == bhc )
                {
                    //translate it!
                    bulletHole.iPosition.x           += transX;
                    bulletHole.iPosition.y           += transY;
                    bulletHole.iPosition.z           += transZ;
                    bulletHole.iOriginalPosition.x   += transX;
                    bulletHole.iOriginalPosition.y   += transY;
                    bulletHole.iOriginalPosition.z   += transZ;
                    bulletHole.updateFace( false );

                    //translate projectile
                    if ( bulletHole.iProjectile != null )
                    {
                        bulletHole.iProjectile.translate( transX, transY, transZ, LibTransformationMode.ETransformedToTransformed );
                    }
                }
            }
        }

        //only z-rotation are allowed for bullet-holes!
        public static final void rotateForBot( Bot bhc, float rotZ )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : BulletHole.bulletHoles )
            {
                //check if this bullet hole belongs to the specified bot
                if ( bulletHole.iHitPoint.iCarrier == bhc )
                {
                    //reverse last translation
                    LibMatrix transformationMatrix = new LibMatrix( 0.0f, 0.0f, rotZ - bulletHole.iCarriersLastFaceAngle );
                    LibVertex translatedHitPoint = transformationMatrix.transformVertexF( bulletHole.iPosition, bhc.getAnchor() );

                    //assign new face angle
                    bulletHole.iHitPoint.iHorzFaceAngle += ( rotZ - bulletHole.iCarriersLastFaceAngle );

                    //asssign new hit-point and update face
                    bulletHole.iPosition = translatedHitPoint;
                    bulletHole.updateFace( false );

                    //assign new rotations
                    bulletHole.iCarriersLastFaceAngle = rotZ;
                }
            }
        }

        public static final void rotateForWall( Wall bhc, float rotX, float rotY, float rotZ )
        {
            //browse all bullet-holes
            for ( BulletHole bulletHole : BulletHole.bulletHoles )
            {
                //check if this bullet hole belongs to the specified mersh
                if ( bulletHole.iHitPoint.iCarrier == bhc )
                {
                    //rotate bulletHole for mesh
                    bulletHole.rotateAroundVertex( bhc.getAnchor(), rotX, rotY, rotZ );

                    //rotate projectile
                    if ( bulletHole.iProjectile != null )
                    {
                        bulletHole.iProjectile.translateAndRotateXYZ( 0.0f, 0.0f, 0.0f, rotX, rotY, rotZ, bhc.getAnchor(), LibTransformationMode.ETransformedToTransformed );
                    }
                }
            }
        }

        public static final void removeForWall( Wall bhc )
        {
            //browse all bullet-holes reversed
            for ( int i = BulletHole.bulletHoles.size() - 1; i >= 0; --i )
            {
                //check if this bullet hole belongs to the specified mersh
                if ( BulletHole.bulletHoles.elementAt( i ).iHitPoint.iCarrier == bhc )
                {
                    //remove this bullet hole
                    BulletHole.bulletHoles.removeElementAt( i );
                }
            }
        }

        public static final void darkenAll( Wall bhc, float opacity )
        {
            //browse all bullet-holes reversed
            for ( int i = BulletHole.bulletHoles.size() - 1; i >= 0; --i )
            {
                //check if this bullet hole belongs to the specified mersh
                if ( BulletHole.bulletHoles.elementAt( i ).iHitPoint.iCarrier == bhc )
                {
                    //darken bulletHole
                    BulletHole.bulletHoles.elementAt( i ).darken( opacity );
                }
            }
        }

        private final void darken( float opacity )
        {
            iFace.darken( opacity );
            //if ( iProjectile != null ) iProjectile.darkenAllFaces( opacity, useRandomSubstract, useRandomAdd, maxSubstract, maxAdd );
        }

        private final float getSuitableDistanceFromHorzFace()
        {
            float dis = BulletHoles.MIN_DISTANCE_FROM_FACE;

            //check distance to all other bullet-holes
            for ( BulletHole bulletHole : BulletHole.bulletHoles )
            {
                //should be distance3d :/
                if ( new Point2D.Float( bulletHole.iHitPoint.iVertex.x, bulletHole.iHitPoint.iVertex.y ).distance( new Point2D.Float( iHitPoint.iVertex.x, iHitPoint.iVertex.y ) ) < BulletHoles.MIN_POINT_DISTANCE_FOR_SAME_LAYER_MULTIPLIER * iHitPoint.iShot.iBulletHoleSize.size )
                {
                    dis += BulletHoles.STEP_DISTANCE_FROM_FACE;
                }
            }

            return dis;
        }

        private final float getSuitableDistanceFromVertFace()
        {
            float dis = BulletHoles.MIN_DISTANCE_FROM_FACE;

            //check distance to all other bullet-holes
            for ( BulletHole bulletHole : BulletHole.bulletHoles )
            {
                //should be distance3d :/
                if ( Math.abs( bulletHole.iHitPoint.iVertex.z - iHitPoint.iVertex.z ) < BulletHoles.MIN_POINT_DISTANCE_FOR_SAME_LAYER_MULTIPLIER * iHitPoint.iShot.iBulletHoleSize.size )
                {
                    dis += BulletHoles.STEP_DISTANCE_FROM_FACE;
                }
            }

            return dis;
        }

        private final void rotateAroundVertex( LibVertex vertex, float rotX, float rotY, float rotZ )
        {
            //increase saved rot z
            ShooterDebug.bulletHole.out( " turn for [" + rotZ + "]" );

            //transform
            LibMatrix transformationMatrix = new LibMatrix( rotX, rotY, rotZ );
            LibVertex translatedHitPoint   = transformationMatrix.transformVertexF( iPosition, vertex );

            //increase face angle for rotZ
            iHitPoint.iHorzFaceAngle += rotZ;
            ShooterDebug.bulletHole.out( " setting bullet hole angle to [" + iHitPoint.iHorzFaceAngle + "]" );

            //asssign new hit-point and update face
            iPosition = translatedHitPoint;

            //update the bullet hole
            updateFace( false );
/*
            //projectile
            if ( iProjectile != null )
            {
                iProjectile = null;
            }
*/
        }

        public static final void clearBulletHoles()
        {
            BulletHole.bulletHoles.removeAllElements();
        }

        public static final void addBulletHole( LibHitPoint hitPoint, LibD3dsFile aProjectile )
        {
            //add to bullet-hole-stack, prune stack if overflowing
            BulletHole.bulletHoles.add( new BulletHole( hitPoint, aProjectile ) );
            if ( BulletHole.bulletHoles.size() > ShooterSettings.Performance.MAX_NUMBER_BULLET_HOLES ) BulletHole.bulletHoles.removeElementAt( 0 );

            ShooterDebug.bulletHole.out( " new bullet hole count: [" + BulletHole.bulletHoles.size() + "]" );
        }
    }
