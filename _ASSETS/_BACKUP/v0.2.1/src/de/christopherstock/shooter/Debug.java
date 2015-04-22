/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;
    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.LibDebug.LogLevel;

    /**************************************************************************************
    *   The debug system consisting of switchable debug groups.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum Debug
    {
        //---   switchable debug groups        ---//
        lwjgl(              Shooter.YES                 ),          //logs lwjgl engine
        mouse(              Shooter.YES                 ),          //logs mouse movement
        playerAction(       Shooter.YES                 ),          //logs player actions
        floorChange(        Shooter.YES                 ),          //logs gravity matters

        d3ds(               Shooter.NO                  ),          //logs 3ds max parser
        keys(               Shooter.NO                  ),          //logs lwjgl engine
        bot(                Shooter.NO                  ),          //logs bot behaviour
        breaklines(         Shooter.NO                  ),          //logs the optimized linebreak-algorithm
        bullethole(         Shooter.NO                  ),          //logs extra-info about bullet-holes
        fps(                Shooter.NO                  ),          //logs frames per second ( dispensable )
        freetriangle(       Shooter.NO                  ),          //logs extra info for all triangle-states
        hitpoint(           Shooter.NO                  ),          //logs wall hitpoints from shots
        item(               Shooter.NO                  ),          //logs information on items
        shot(               Shooter.NO                  ),          //logs player's shot
        wallCollision(      Shooter.NO                  ),          //logs player's collisions

        bugfix(             Boolean.TRUE.booleanValue() ),          //paramount debug group
        ;

        public                      LibDebug    debug               = null;

        Debug( boolean aFlag )
        {
            debug = new LibDebug( aFlag, LogLevel.ELevelDebug );
        }

        public final void out( Object obj )
        {
            debug.out( obj );
        }

        public final void info( Object obj )
        {
            debug.info( obj );
        }

        public final void note( Object obj )
        {
            debug.note( obj );
        }

        public final void warn( Object obj )
        {
            debug.warn( obj );
        }

        public final void err( Object obj )
        {
            debug.err( obj );
        }

        public final void fatal( Object obj )
        {
            debug.fatal( obj );
        }
    }
