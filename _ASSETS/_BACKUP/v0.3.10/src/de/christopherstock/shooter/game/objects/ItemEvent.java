/*  $Id: ItemEvent.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game.objects;

    import  de.christopherstock.shooter.base.*;
    import  de.christopherstock.shooter.game.wearpon.*;
    import  de.christopherstock.shooter.ui.hud.HUDMessageManager;

    /**************************************************************************************
    *   An event being invoked - not only by picking up items but on level start etc.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public enum ItemEvent implements GameEvent
    {
        EGainAmmo20Bullet9mm,
        EGainAmmo20ShotgunShells,
        EGainAmmo20Bullet51mm,
        EGainAmmo20Bullet44mm,
        EGainAmmo20Bullet792mm,
        EGainAmmo120Bullet792mm,
        EGainAmmo18MagnumBullet,
        EGainAmmo20TranquilizerDarts,

        EGainWearponShotgun,
        EGainWearponWaltherPPK,
        EGainWearponMagnum357,
        EGainWearponKnife,

        EGainGadgetBottle1,
        EGainGadgetHandset1,
        EGainGadgetCrackers,
        EGainGadgetKeycard,

        EGiveCrackers,
        EGiveKeycard,

        ;

        @Override
        public void perform( Bot bot )
        {
            switch ( this )
            {
                case EGainAmmo20Bullet44mm:
                {
                    ShooterGameLevel.currentPlayer().getAmmoSet().addAmmo( AmmoType.EBullet44mm, 20 );
                    break;
                }

                case EGainAmmo18MagnumBullet:
                {
                    ShooterGameLevel.currentPlayer().getAmmoSet().addAmmo( AmmoType.EMagnumBullet357, 18 );
                    break;
                }

                case EGainAmmo20Bullet9mm:
                {
                    ShooterGameLevel.currentPlayer().getAmmoSet().addAmmo( AmmoType.EBullet9mm, 20 );
                    break;
                }

                case EGainAmmo20Bullet51mm:
                {
                    ShooterGameLevel.currentPlayer().getAmmoSet().addAmmo( AmmoType.EBullet51mm, 20 );
                    break;
                }

                case EGainAmmo20Bullet792mm:
                {
                    ShooterGameLevel.currentPlayer().getAmmoSet().addAmmo( AmmoType.EBullet792mm, 20 );
                    break;
                }

                case EGainAmmo120Bullet792mm:
                {
                    ShooterGameLevel.currentPlayer().getAmmoSet().addAmmo( AmmoType.EBullet792mm, 120 );
                    break;
                }

                case EGainAmmo20ShotgunShells:
                {
                    ShooterGameLevel.currentPlayer().getAmmoSet().addAmmo( AmmoType.EShotgunShells, 20 );
                    break;
                }

                case EGainAmmo20TranquilizerDarts:
                {
                    ShooterGameLevel.currentPlayer().getAmmoSet().addAmmo( AmmoType.ETranquilizerDarts, 20 );
                    break;
                }

                case EGainWearponKnife:
                {
                    ShooterGameLevel.currentPlayer().iArtefactSet.deliverArtefact( new Artefact( ArtefactType.EKnife ) );
                    break;
                }

                case EGainWearponWaltherPPK:
                {
                    ShooterGameLevel.currentPlayer().iArtefactSet.deliverArtefact( new Artefact( ArtefactType.EWaltherPPK ) );
                    break;
                }

                case EGainWearponMagnum357:
                {
                    ShooterGameLevel.currentPlayer().iArtefactSet.deliverArtefact( new Artefact( ArtefactType.EMagnum357 ) );
                    break;
                }

                case EGainWearponShotgun:
                {
                    ShooterGameLevel.currentPlayer().iArtefactSet.deliverArtefact( new Artefact( ArtefactType.ESpaz12 ) );
                    break;
                }

                case EGainGadgetBottle1:
                {
                    ShooterGameLevel.currentPlayer().iArtefactSet.deliverArtefact( new Artefact( ArtefactType.EBottleVolvic ) );
                    break;
                }

                case EGainGadgetHandset1:
                {
                    ShooterGameLevel.currentPlayer().iArtefactSet.deliverArtefact( new Artefact( ArtefactType.EMobilePhoneSEW890i ) );
                    break;
                }

                case EGainGadgetCrackers:
                {
                    ShooterGameLevel.currentPlayer().iArtefactSet.deliverArtefact( new Artefact( ArtefactType.EChips ) );
                    break;
                }

                case EGainGadgetKeycard:
                {
                    ShooterGameLevel.currentPlayer().iArtefactSet.deliverArtefact( new Artefact( ArtefactType.EKeycard1 ) );
                    break;
                }

                case EGiveCrackers:
                {
                    EGainGadgetCrackers.perform( null );
                    HUDMessageManager.getSingleton().showMessage( ShooterStrings.HUDMessages.TAKE_CRACKERS );
                    break;
                }

                case EGiveKeycard:
                {
                    EGainGadgetKeycard.perform( null );
                    HUDMessageManager.getSingleton().showMessage( ShooterStrings.HUDMessages.TAKE_KEYCARD );
                    break;
                }
            }
        }
    }
