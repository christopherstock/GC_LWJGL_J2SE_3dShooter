/*  $Id: BotFactory.java 739 2011-05-15 02:08:00Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.base;

    import  de.christopherstock.lib.g3d.*;
    import  de.christopherstock.lib.math.*;
    import  de.christopherstock.shooter.base.ShooterD3dsFiles.*;
    import  de.christopherstock.shooter.game.objects.*;
    import de.christopherstock.shooter.game.objects.Bot.*;

    /**************************************************************************************
    *   The superclass of all non-player-characters.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public class ShooterBotFactory
    {
        public static enum Glasses
        {
            ENever,
            EAlways,
            ESometimes,
            EOften,
            ;
        }

        public static enum Hat
        {
            ENever,
            EAlways,
            ESometimes,
            EOften,
            ;
        }

        public static enum BotKind
        {
            EUnitSecurity(          Glasses.ENever,         Hat.ENever,     BotSkinType.SET_NORTH_EUROPEAN, BotBodyType.values() ),
            EUnitPilot(             Glasses.EAlways,        Hat.EAlways,    BotSkinType.SET_SOUTH_EUROPEAN, BotBodyType.values() ),
            EUnitSpecialForces(     Glasses.ESometimes,     Hat.ESometimes, BotSkinType.SET_NORTH_AFRICAN,  BotBodyType.values() ),
            EUnitOfficeEmployee(    Glasses.EOften,         Hat.ENever,     BotSkinType.SET_NORTH_EUROPEAN, BotBodyType.values() ),
            ;

            public          BotSkinType[]       iSkinTypes  = null;
            public          BotBodyType[]       iBodyTypes  = null;
            public          Glasses             iGlasses    = null;
            public          Hat                 iHat        = null;

            private BotKind( Glasses aGlasses, Hat aHat, BotSkinType[] aSkinTypes, BotBodyType[] aBodyTypes )
            {
                iGlasses    = aGlasses;
                iHat        = aHat;
                iSkinTypes  = aSkinTypes;
                iBodyTypes  = aBodyTypes;
            }
        }

        public          BotKind             iKind       = null;
        public          LibVertex           iAnchor     = null;

        public ShooterBotFactory( BotKind aKind, LibVertex aAnchor )
        {
            iKind   = aKind;
            iAnchor = aAnchor;
        }

        public final Bot createBot()
        {
            switch ( iKind )
            {
                case EUnitOfficeEmployee:
                {
                    switch ( LibMath.getRandom( 0, 1 ) )
                    {
                        case 0: return new Bot(  new ShooterBotPattern( ShooterBotPatternBase.EOfficeEmployee1,   iKind   ),  Bot.BotType.ETypeFriend, iAnchor,  null, BotJob.EWatchPlayer,           Bots.EItemPistol,     null                 );
                        case 1: return new Bot(  new ShooterBotPattern( ShooterBotPatternBase.EOfficeEmployee2,   iKind   ),  Bot.BotType.ETypeFriend, iAnchor,  null, BotJob.EWatchPlayer,           Bots.EItemPistol,     null                 );
                    }
                    break;
                }

                case EUnitSecurity:
                {
                    switch ( LibMath.getRandom( 0, 1 ) )
                    {
                        case 0: return new Bot(  new ShooterBotPattern( ShooterBotPatternBase.ESecurityLight,     iKind   ),  Bot.BotType.ETypeFriend, iAnchor,  null, BotJob.ESleep,           Bots.EItemPistol,     null                 );
                        case 1: return new Bot(  new ShooterBotPattern( ShooterBotPatternBase.ESecurityHeavy,     iKind   ),  Bot.BotType.ETypeFriend, iAnchor,  null, BotJob.ESleep,           Bots.EItemPistol,     null                 );
                    }
                    break;
                }

                case EUnitPilot:
                {
                    switch ( LibMath.getRandom( 0, 1 ) )
                    {
                        case 0: return new Bot(  new ShooterBotPattern( ShooterBotPatternBase.EPilot1,            iKind   ),  Bot.BotType.ETypeFriend, iAnchor,  null, BotJob.ESleep,           Bots.EItemPistol,     null                 );
                        case 1: return new Bot(  new ShooterBotPattern( ShooterBotPatternBase.EPilot1,            iKind   ),  Bot.BotType.ETypeFriend, iAnchor,  null, BotJob.ESleep,           Bots.EItemPistol,     null                 );
                    }
                    break;
                }

                case EUnitSpecialForces:
                {
                    switch ( LibMath.getRandom( 0, 1 ) )
                    {
                        case 0: return new Bot(  new ShooterBotPattern( ShooterBotPatternBase.ESpecialForces,     iKind   ),  Bot.BotType.ETypeFriend, iAnchor,  null, BotJob.ESleep,           Bots.EItemPistol,     null                 );
                        case 1: return new Bot(  new ShooterBotPattern( ShooterBotPatternBase.ESpecialForces,     iKind   ),  Bot.BotType.ETypeFriend, iAnchor,  null, BotJob.ESleep,           Bots.EItemPistol,     null                 );
                    }
                    break;
                }
            }

            return null;
        }
    }
