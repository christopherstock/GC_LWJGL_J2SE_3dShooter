/*  $Id: Item.java 626 2011-04-22 21:49:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.ShooterSettings.DebugSettings;
    import  de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.g3d.face.LibFace.DrawMethod;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.game.objects.*;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.game.wearpon.Ammo.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;

    /**************************************************************************************
    *   An item being able to be picked up by the player.
    *
    *   @author     Christopher Stock
    *   @version    0.3.4
    **************************************************************************************/
    class ShooterItem implements GameObject
    {
        public static enum Rotating
        {
            EYes,
            ENo,
            ;
        }

        public static enum ItemTemplate
        {
            EAmmoBullet9mm(     ItemType.ECircle, ShooterSettings.Items.AMMO_RADIUS,    Items.EAmmoBullet9mm,       new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_BULLETS_9MM    ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet9mm,         }   ),
            EAmmoShotgunShell(  ItemType.ECircle, ShooterSettings.Items.AMMO_RADIUS,    Items.EAmmoShotgunShells,   new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_SHOTGUN_SHELLS ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainAmmo20ShotgunShells,     }   ),

            EAmmoBullet44mm(    ItemType.ECircle, ShooterSettings.Items.AMMO_RADIUS,    Items.EAmmoBullet44mm,      new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_BULLETS_44MM   ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet44mm,        }   ),
            EAmmoBullet51mm(    ItemType.ECircle, ShooterSettings.Items.AMMO_RADIUS,    Items.EAmmoBullet51mm,      new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_BULLETS_51MM   ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet51mm,        }   ),
            EAmmoBullet792mm(   ItemType.ECircle, ShooterSettings.Items.AMMO_RADIUS,    Items.EAmmoBullet792mm,     new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_BULLETS_792MM  ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet792mm,       }   ),



            EAmmoMagnumBullet(  ItemType.ECircle, ShooterSettings.Items.AMMO_RADIUS,    Items.EAmmoMagnumBullet,    new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_MAGNUM_BULLETS ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainAmmo18MagnumBullet,      }   ),

            EWearponPistol9mm(  ItemType.ECircle, ShooterSettings.Items.WEARPON_RADIUS, Items.EPistol1,             new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_PISTOL_9MM     ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainWearponWaltherPPK,       }   ),
            EWearponPistol2(    ItemType.ECircle, ShooterSettings.Items.WEARPON_RADIUS, Items.EPistol2,             new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_PISTOL_2       ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainWearponMagnum357,        }   ),
            EWearponKnife(      ItemType.ECircle, ShooterSettings.Items.WEARPON_RADIUS, Items.EKnife,               new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_KNIFE          ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainKnife,                   }   ),
            EItemBottle1(       ItemType.ECircle, ShooterSettings.Items.ITEM_RADIUS,    Items.EPistol1,             new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_BOTTLE         ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainGadgetBottle1,           }   ),

            EItemCrackers(      ItemType.ECircle, ShooterSettings.Items.ITEM_RADIUS,    Items.ECrackers,            new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_CRACKERS       ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainCrackers,                }   ),


            EWearponShotgun(    ItemType.ECircle, ShooterSettings.Items.WEARPON_RADIUS, Items.EShotgun1,            new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_SHOTGUN        ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainWearponShotgun,          }   ),
            EItemHandset1(      ItemType.ECircle, ShooterSettings.Items.ITEM_RADIUS,  Items.EPistol1,               new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_HANDSET        ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainGadgetHandset1,          }   ),

            ;

            protected           ItemType        iType               = null;
            protected           float           iRadius             = 0.0f;
            protected           D3dsFile        iMeshFile           = null;
            protected           HUDMessage      iHudMessage         = null;
            protected           Sound           iPickupSound        = null;
            protected           ItemEvent[]     iItemEvents         = null;

            private ItemTemplate( ItemType aType, float aRadius, D3dsFile aMeshFile, HUDMessage aHudMessage, Sound aPickupSound, ItemEvent[] aItemEvents )
            {
                iType        = aType;
                iRadius      = aRadius;
                iMeshFile    = aMeshFile;
                iHudMessage  = aHudMessage;
                iPickupSound = aPickupSound;
                iItemEvents  = aItemEvents;
            }
        }

        public static enum ItemType
        {
            ECircle,
            ;
        }

        public static enum ItemEvent
        {
            EGainAmmo20Bullet9mm,
            EGainAmmo20ShotgunShells,
            EGainAmmo20Bullet51mm,
            EGainAmmo20Bullet44mm,
            EGainAmmo20Bullet792mm,
            EGainAmmo18MagnumBullet,

            EGainWearponShotgun,
            EGainWearponWaltherPPK,
            EGainWearponMagnum357,
            EGainGadgetBottle1,
            EGainGadgetHandset1,
            EGainKnife,
            EGainCrackers,
            ;

            public void perform()
            {
                switch ( this )
                {
                    case EGainAmmo20Bullet44mm:
                    {
                        ShooterGameLevel.currentPlayer().getAmmo().addAmmo( AmmoType.EBullet44mm, 20 );
                        break;
                    }

                    case EGainAmmo18MagnumBullet:
                    {
                        ShooterGameLevel.currentPlayer().getAmmo().addAmmo( AmmoType.EMagnumBullet357, 18 );
                        break;
                    }

                    case EGainAmmo20Bullet9mm:
                    {
                        ShooterGameLevel.currentPlayer().getAmmo().addAmmo( AmmoType.EBullet9mm, 20 );
                        break;
                    }

                    case EGainAmmo20Bullet51mm:
                    {
                        ShooterGameLevel.currentPlayer().getAmmo().addAmmo( AmmoType.EBullet51mm, 20 );
                        break;
                    }

                    case EGainAmmo20Bullet792mm:
                    {
                        ShooterGameLevel.currentPlayer().getAmmo().addAmmo( AmmoType.EBullet792mm, 20 );
                        break;
                    }

                    case EGainAmmo20ShotgunShells:
                    {
                        ShooterGameLevel.currentPlayer().getAmmo().addAmmo( AmmoType.EShotgunShells, 20 );
                        break;
                    }

                    case EGainKnife:
                    {
                        ShooterGameLevel.currentPlayer().deliverWearpon( Artefact.EKnife );
                        break;
                    }

                    case EGainWearponWaltherPPK:
                    {
                        ShooterGameLevel.currentPlayer().deliverWearpon( Artefact.EWaltherPPK );
                        break;
                    }

                    case EGainWearponMagnum357:
                    {
                        ShooterGameLevel.currentPlayer().deliverWearpon( Artefact.EMagnum357 );
                        break;
                    }

                    case EGainWearponShotgun:
                    {
                        ShooterGameLevel.currentPlayer().deliverWearpon( Artefact.ESpaz12 );
                        break;
                    }

                    case EGainGadgetBottle1:
                    {
                        ShooterGameLevel.currentPlayer().deliverWearpon( Artefact.EBottleVolvic );
                        break;
                    }

                    case EGainGadgetHandset1:
                    {
                        ShooterGameLevel.currentPlayer().deliverWearpon( Artefact.EMobilePhoneSEW890i );
                        break;
                    }

                    case EGainCrackers:
                    {
                        ShooterGameLevel.currentPlayer().deliverWearpon( Artefact.EChips );
                        break;
                    }
                }
            }
        }

        private     static  final   LibColors           DEBUG_COLOR         = LibColors.EGreen;

        private                     LibVertex           iAnchor             = null;
        private                     ItemTemplate        iTemplate           = null;
        private                     boolean             iCollected          = false;
        private                     Mesh                iMesh               = null;
        private                     float               iStartRotZ          = 0.0f;
        private                     Rotating            iIsRotating         = null;
        private                     float               aRotationZ          = 0.0f;

        public ShooterItem( ItemTemplate aTemplate, float x, float y, float z, float aRotZ, Rotating aIsRotating )
        {
            iTemplate   = aTemplate;
            iAnchor     = new LibVertex( x, y, z, 0.0f, 0.0f );
            iStartRotZ  = aRotZ;
            iIsRotating = aIsRotating;
        }

        public final void draw()
        {
            //rotate if desired
            if ( iIsRotating == Rotating.EYes )
            {
                iMesh.translateAndRotateXYZ( iAnchor.x, iAnchor.y, iAnchor.z, 0.0f, 0.0f, aRotationZ, null, LibTransformationMode.EOriginalsToTransformed );
                aRotationZ += ShooterSettings.Items.SPEED_ROTATING;
            }

            //draw mesh
            iMesh.draw();
/*
                    iMesh.translateAndRotateXYZ
                    (
                        iMesh.getAnchor().x,
                        iMesh.getAnchor().y,
                        iMesh.getAnchor().z,
                        50.0f,
                        0.0f,
                        0.0f,
                        null
                    );
*/
            //draw debug shape
            switch ( iTemplate.iType )
            {
                case ECircle:
                {
                    if ( DebugSettings.DEBUG_DRAW_ITEM_CIRCLE )
                    {
                        //Debug.item.out( "drawing item .." );
                        new FaceEllipseFloor( null, DEBUG_COLOR, iAnchor.x, iAnchor.y, iAnchor.z, iTemplate.iRadius, iTemplate.iRadius ).draw();
                    }
                    break;
                }
            }
        }

        public final boolean isCollected()
        {
            return iCollected;
        }

        public final void checkCollision()
        {
            //check if not inited
            /*
            if ( Player.user.getCollisionCircle() == null ) return;
            Debug.note( "check .." );
            */

            //check collision of 2 circles ( easy  task.. )
            Area player = new Area( ShooterGameLevel.currentPlayer().getCylinder().getCircle() );
            Ellipse2D.Float itemCircle = new Ellipse2D.Float( iAnchor.x - iTemplate.iRadius, iAnchor.y - iTemplate.iRadius, 2 * iTemplate.iRadius, 2 * iTemplate.iRadius );
            Area item   = new Area( itemCircle );
            player.intersect( item );
            if ( !player.isEmpty() )
            {
                //perform item event
                for ( ItemEvent event : iTemplate.iItemEvents )
                {
                    event.perform();
                }

                //play sound fx
                iTemplate.iPickupSound.playGlobalFx();
                ShooterDebug.major.out( "item collected" );

                //show hud message
                iTemplate.iHudMessage.show();

                //mark as collected
                iCollected = true;
            }
        }

        public final void loadD3ds()
        {
            iMesh = new Mesh( ShooterD3dsFiles.getFaces( iTemplate.iMeshFile ), iAnchor, iStartRotZ, 1.0f, this, LibTransformationMode.EOriginalsToTransformed, DrawMethod.EHideIfTooDistant );
        }

        @Override
        public final LibVertex getAnchor()
        {
            return iAnchor;
        }

        @Override
        public final float getCarriersFaceAngle()
        {
            return 0.0f;
        }

        @Override
        public final HitPointCarrier getHitPointCarrier()
        {
            return null;
        }

        @Override
        public final Vector<HitPoint> launchShot( Shot s )
        {
            return null;
        }
    }
