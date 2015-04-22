/*  $Id: ItemKind.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import de.christopherstock.shooter.*;
    import de.christopherstock.shooter.base.*;
    import de.christopherstock.shooter.base.ShooterD3ds.*;
    import de.christopherstock.shooter.game.objects.ItemToPickUp.*;

    /**************************************************************************************
    *   All different item kinds.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public enum ItemKind
    {
        EAmmoBullet9mm(     ItemType.ECircle, ShooterSettings.ItemSettings.AMMO_RADIUS,    Items.EAmmoBullet9mm,       ShooterStrings.HUDMessages.PICKED_UP_BULLETS_9MM   , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet9mm,         }, true   ),
        EAmmoShotgunShell(  ItemType.ECircle, ShooterSettings.ItemSettings.AMMO_RADIUS,    Items.EAmmoShotgunShell,    ShooterStrings.HUDMessages.PICKED_UP_SHOTGUN_SHELLS, ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainAmmo20ShotgunShells,     }, true   ),
        EAmmoBullet44mm(    ItemType.ECircle, ShooterSettings.ItemSettings.AMMO_RADIUS,    Items.EAmmoBullet44mm,      ShooterStrings.HUDMessages.PICKED_UP_BULLETS_44MM  , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet44mm,        }, true   ),
        EAmmoBullet51mm(    ItemType.ECircle, ShooterSettings.ItemSettings.AMMO_RADIUS,    Items.EAmmoBullet51mm,      ShooterStrings.HUDMessages.PICKED_UP_BULLETS_51MM  , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet51mm,        }, true   ),
        EAmmoBullet792mm(   ItemType.ECircle, ShooterSettings.ItemSettings.AMMO_RADIUS,    Items.EAmmoBullet792mm,     ShooterStrings.HUDMessages.PICKED_UP_BULLETS_792MM , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainAmmo20Bullet792mm,       }, true   ),
        EAmmoBulletMagnum(  ItemType.ECircle, ShooterSettings.ItemSettings.AMMO_RADIUS,    Items.EAmmoBulletMagnum,    ShooterStrings.HUDMessages.PICKED_UP_MAGNUM_BULLETS, ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainAmmo18MagnumBullet,      }, true   ),

        EWearponPistol9mm(  ItemType.ECircle, ShooterSettings.ItemSettings.WEARPON_RADIUS, Items.EPistol1,             ShooterStrings.HUDMessages.PICKED_UP_PISTOL_9MM    , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainWearponWaltherPPK,       }, true   ),
        EWearponPistol2(    ItemType.ECircle, ShooterSettings.ItemSettings.WEARPON_RADIUS, Items.EPistol1,             ShooterStrings.HUDMessages.PICKED_UP_PISTOL_2      , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainWearponMagnum357,        }, true   ),
        EWearponKnife(      ItemType.ECircle, ShooterSettings.ItemSettings.WEARPON_RADIUS, Items.EKnife,               ShooterStrings.HUDMessages.PICKED_UP_KNIFE         , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainWearponKnife,                   }, true   ),
        EItemBottle1(       ItemType.ECircle, ShooterSettings.ItemSettings.ITEM_RADIUS,    Items.EPistol1,             ShooterStrings.HUDMessages.PICKED_UP_BOTTLE        , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainGadgetBottle1,           }, true   ),

        EItemCrackers(      ItemType.ECircle, ShooterSettings.ItemSettings.ITEM_RADIUS,    Items.ECrackers,            ShooterStrings.HUDMessages.PICKED_UP_CRACKERS      , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainGadgetCrackers,                }, true   ),

        EWearponShotgun(    ItemType.ECircle, ShooterSettings.ItemSettings.WEARPON_RADIUS, Items.EShotgun1,            ShooterStrings.HUDMessages.PICKED_UP_SHOTGUN       , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainWearponShotgun,          }, true   ),
        EItemHandset1(      ItemType.ECircle, ShooterSettings.ItemSettings.ITEM_RADIUS,    Items.EPistol1,             ShooterStrings.HUDMessages.PICKED_UP_HANDSET       , ShooterSound.EPickUpItem1, new ItemEvent[] { ItemEvent.EGainGadgetHandset1,          }, true   ),

        EGameEventLevel1AcclaimPlayer(  ItemType.ECircle, ShooterSettings.ItemSettings.EVENT_RADIUS,           null,   null, null, new GameEvent[] { BotEvent.ELevel1AcclaimPlayer,     }, false   ),
        EGameEventLevel1ExplainAction(  ItemType.ECircle, ShooterSettings.ItemSettings.EVENT_RADIUS,           null,   null, null, new GameEvent[] { BotEvent.ELevel1ExplainAction,     }, false   ),
        EGameEventLevel1ChangeToNextSection( ItemType.ECircle, ShooterSettings.ItemSettings.LEVEL_CHANGE_RADIUS,    null,   null, null, new GameEvent[] { BotEvent.ELevel1ChangeToNextLevel,    }, false   ),
        EGameEventLevel1ChangeToPreviousLevel( ItemType.ECircle, ShooterSettings.ItemSettings.LEVEL_CHANGE_RADIUS,    null,   null, null, new GameEvent[] { BotEvent.ELevel1ChangeToPreviousLevel,    }, false   ),

        ;

        protected           ItemType        iType                   = null;

        protected           float           iRadius                 = 0.0f;
        protected           D3dsFile        iMeshFile               = null;
        protected           String          iHudMessage             = null;
        protected           ShooterSound    iPickupSound            = null;
        protected           GameEvent[]     iItemEvents             = null;
        protected           boolean         iSingleEvent            = false;

        private ItemKind( ItemType aType, float aRadius, D3dsFile aMeshFile, String aHudMessage, ShooterSound aPickupSound, GameEvent[] aItemEvents, boolean aSingleEvent )
        {
            iType                   = aType;
            iRadius                 = aRadius;
            iMeshFile               = aMeshFile;
            iHudMessage             = aHudMessage;
            iPickupSound            = aPickupSound;
            iItemEvents             = aItemEvents;
            iSingleEvent            = aSingleEvent;
        }
    }
