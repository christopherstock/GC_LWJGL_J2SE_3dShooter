/*  $Id: ShooterDebug.java 794 2011-05-27 22:46:10Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.lib.*;
    import  de.christopherstock.lib.ui.*;

    /**************************************************************************************
    *   The debug system consisting of switchable debug groups.
    *
    *   @author     Christopher Stock
    *   @version    0.3.5
    **************************************************************************************/
    public enum ShooterDebug implements LibDebug
    {
        mouse(              true    ),      //logs mouse movement


        sounds(             false   ),      //logs sound matters
        floorChange(        false   ),      //logs z change behaviour
        bot(                false   ),      //logs bot behaviour
        init(               false   ),      //logs initialization
        levels(             false   ),      //logs player actions
        glimage(            false   ),      //logs player actions
        playerAction(       false   ),      //logs player actions
        gl(                 false   ),      //logs player's collisions
        d3ds(               false   ),      //logs 3ds max parser
        bullethole(         false   ),      //logs extra-info about bullet-holes
        hitpoint(           false   ),      //logs wall hitpoints from shots
        shot(               false   ),      //logs player's shot
        playerCylinder(     false   ),      //logs player's cylinder
        mainThreadDelay(    false   ),      //logs main thread intentional delay

        bugfix(             true    ),      //paramount debug group
        major(              true    ),      //paramount debug group
        error(              true    ),      //paramount debug group

        ;

        private                     boolean         debugEnabled        = false;

        private ShooterDebug( boolean aDebugEnabled )
        {
            debugEnabled    = aDebugEnabled;
        }

        @Override
        public final void out( Object obj )
        {
            if ( ShooterSettings.DebugSettings.DEBUG_MODE && debugEnabled   ) System.out.println( obj );
        }

        @Override
        public final void err( Object obj )
        {
            if ( ShooterSettings.DebugSettings.DEBUG_MODE                   ) System.err.println( obj );
        }

        @Override
        public final void trace( Throwable obj )
        {
            if ( ShooterSettings.DebugSettings.DEBUG_MODE && debugEnabled   ) obj.printStackTrace();
        }

        @Override
        public final void mem()
        {
            if ( ShooterSettings.DebugSettings.DEBUG_MODE && debugEnabled   )
            {
                Runtime r = Runtime.getRuntime();
                System.out.println( "free:  [" + LibStringFormat.getSingleton().formatNumber( r.freeMemory()  ) + "]" );
                System.out.println( " total: [" + LibStringFormat.getSingleton().formatNumber( r.totalMemory() ) + "]" );
                System.out.println( "  max:   [" + LibStringFormat.getSingleton().formatNumber( r.maxMemory()   ) + "]" );
            }
        }
    }
/*
        public static final void drawLine( Vertex v1, Vertex v2 )
        {
            Debug.shot.out( " >> DRAW LINE: ["+v1.x+"]["+v1.y+"]["+v1.z+"] ["+v2.x+"]["+v2.y+"]["+v2.z+"]" );

            float distX = v1.x - v2.x;
            float distY = v1.y - v2.y;
            float distZ = v1.z - v2.z;

            Debug.shot.out( " >> DIST X: " + distX );
            Debug.shot.out( " >> DIST Y: " + distY );
            Debug.shot.out( " >> DIST Z: " + distZ );

            //draw shot line with own calculations
            for ( float distance = 0.0f; distance < 10.0f; distance += 0.01f )
            {
                new DebugPoint
                (
                    Colors.EPink,
                    v1.x - distX * distance / 10.0f,
                    v1.y - distY * distance / 10.0f,
                    v1.z - distZ * distance / 10.0f,
                    DebugPoint.ELifetimeForever
                );
            }
        }
*/
