/*  $Id: ShooterDebug.java 1134 2012-03-12 19:51:02Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   The debug system consisting of switchable debug groups.
    *
    *   @author     Christopher Stock
    *   @version    0.3.10
    **************************************************************************************/
    public enum ShooterDebug implements LibDebug
    {
        mouse(              false   ),      //logs mouse movement
        sounds(             false   ),      //logs sound matters
        playerAction(       false   ),      //logs player actions
        bot(                false   ),      //logs bot behaviour
        init(               false   ),      //logs initialization
        wallDestroy(        false   ),      //logs destroyed walls
        floorChange(        false   ),      //logs z change behaviour
        levels(             false   ),      //logs player actions
        glimage(            false   ),      //logs player actions
        gl(                 false   ),      //logs player's collisions
        d3ds(               false   ),      //logs 3ds max parser
        bullethole(         false   ),      //logs extra-info about bullet-holes
        shotAndHit(         false   ),      //logs shots and hitpoints ( lags performance! )
        playerCylinder(     false   ),      //logs player's cylinder

        bugfix(             true    ),      //prior debug group
        major(              true    ),      //prior debug group
        error(              true    ),      //prior debug group

        ;

        /************************************************************************************
        *   Saying YES is definetely equivalent to the boolean <code>true</code>.
        ************************************************************************************/
        public      static  final   boolean         YES                                 = true;
        /************************************************************************************
        *   Saying NO is definetely equivalent to the boolean <code>false</code>.
        ************************************************************************************/
        public      static  final   boolean         NO                                  = false;

        public      static  final   boolean         DEBUG_MODE                          = YES;
        public      static  final   boolean         DEBUG_DRAW_PLAYER_CIRCLE            = YES;
        public      static  final   boolean         DEBUG_DRAW_ITEM_CIRCLE              = YES;
        public      static  final   boolean         DEBUG_DRAW_BOT_CIRCLES              = NO;
        public      static  final   boolean         DEBUG_SHOW_FPS                      = YES;

        private                     boolean         debugEnabled                        = false;

        private ShooterDebug( boolean aDebugEnabled )
        {
            debugEnabled    = aDebugEnabled;
        }

        @Override
        public final void out( Object obj )
        {
            if ( DEBUG_MODE && debugEnabled   ) System.out.println( "[" + LibStringFormat.getSingleton().formatDateTime() + "] " + obj );
        }

        @Override
        public final void err( Object obj )
        {
            if ( DEBUG_MODE                   ) System.err.println( "[" + LibStringFormat.getSingleton().formatDateTime() + "] " + obj );
        }

        @Override
        public final void trace( Throwable obj )
        {
            if ( DEBUG_MODE && debugEnabled   ) obj.printStackTrace();
        }

        @Override
        public final void mem()
        {
            if ( DEBUG_MODE && debugEnabled   )
            {
                Runtime r = Runtime.getRuntime();
                out( "free:   [" + LibStringFormat.getSingleton().formatNumber( r.freeMemory()  ) + "]" );
                out( " total: [" + LibStringFormat.getSingleton().formatNumber( r.totalMemory() ) + "]" );
                out( "  max:  [" + LibStringFormat.getSingleton().formatNumber( r.maxMemory()   ) + "]" );
            }
        }
    }
