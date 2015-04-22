/*  $Id: Item.java 487 2011-03-26 17:51:03Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.ui.*;
    import  de.christopherstock.shooter.*;
    import de.christopherstock.shooter.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.ShooterSettings.DebugSettings;
    import  de.christopherstock.shooter.g3d.face.*;
    import  de.christopherstock.shooter.g3d.mesh.*;
    import  de.christopherstock.shooter.game.*;
    import  de.christopherstock.shooter.game.collision.*;
    import  de.christopherstock.shooter.io.hid.*;
    import  de.christopherstock.shooter.ui.hud.*;
    import  de.christopherstock.shooter.ui.hud.Ammo.AmmoType;

    /**************************************************************************************
    *   An item being able to be picked up by the player.
    *
    *   @author     Christopher Stock
    *   @version    0.3.2
    **************************************************************************************/
    public class Item implements GameObject
    {
        public static enum ItemTemplate
        {
            EAmmoBullet9mm(     ItemType.ECircle, ShooterSettings.Items.AMMO_RADIUS,    Others.EItemAmmoBullet9mm,       new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_BULLETS_9MM    ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet9mm,       }   ),
            EAmmoShotgunShells( ItemType.ECircle, ShooterSettings.Items.AMMO_RADIUS,    Others.EItemAmmoShotgunShells,   new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_SHOTGUN_SHELLS ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainAmmo20ShotgunShells,   }   ),
            EWearponPistol9mm(  ItemType.ECircle, ShooterSettings.Items.WEARPON_RADIUS, Others.EItemPistol1,             new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_PISTOL_9MM     ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainWearponPistol9mm,      }   ),
            EItemBottle1(       ItemType.ECircle, ShooterSettings.Items.GADGET_RADIUS,  Others.EItemPistol1,             new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_BOTTLE         ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainGadgetBottle1,         }   ),

            EWearponShotgun(    ItemType.ECircle, ShooterSettings.Items.WEARPON_RADIUS, Others.EItemPistol1,             new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_SHOTGUN        ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainWearponShotgun,        }   ),
            EItemHandset1(      ItemType.ECircle, ShooterSettings.Items.GADGET_RADIUS,  Others.EItemPistol1,             new HUDMessage( ShooterStrings.HUDMessages.PICKED_UP_HANDSET        ), Sound.EClack1, new ItemEvent[] { ItemEvent.EGainGadgetHandset1,        }   ),

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
            EGainWearponShotgun,
            EGainWearponPistol9mm,
            EGainGadgetBottle1,
            EGainGadgetHandset1,
            ;

            public void perform()
            {
                switch ( this )
                {
                    case EGainAmmo20Bullet9mm:
                    {
                        Level.currentPlayer().getAmmo().addAmmo( AmmoType.EBullet9mm, 20 );
                        break;
                    }

                    case EGainAmmo20ShotgunShells:
                    {
                        Level.currentPlayer().getAmmo().addAmmo( AmmoType.EShotgunShells, 20 );
                        break;
                    }

                    case EGainWearponPistol9mm:
                    {
                        Level.currentPlayer().deliverWearpon( Wearpon.EPistol );
                        break;
                    }

                    case EGainWearponShotgun:
                    {
                        Level.currentPlayer().deliverWearpon( Wearpon.EScattergun );
                        break;
                    }

                    case EGainGadgetBottle1:
                    {
                        Level.currentPlayer().deliverGadget( Gadget.EBottleVolvic );
                        break;
                    }

                    case EGainGadgetHandset1:
                    {
                        Level.currentPlayer().deliverGadget( Gadget.EMobilePhoneSEW890i );
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

        public Item( ItemTemplate aTemplate, float x, float y, float z )
        {
            iTemplate = aTemplate;
            iAnchor   = new LibVertex( x, y, z, 0.0f, 0.0f );
        }

        public final void draw()
        {
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
            Area player = new Area( Level.currentPlayer().getCylinder().getCircle() );
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
            iMesh = new Mesh( ShooterD3dsFiles.getFaces( iTemplate.iMeshFile ), iAnchor, 0.0f, 1.0f, this, LibTransformationMode.EOriginalsToTransformed );
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
