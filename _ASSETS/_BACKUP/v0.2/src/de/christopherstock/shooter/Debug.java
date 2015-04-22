/*  $Id: Colors.java,v 1.1 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    /**************************************************************************************
    *   The debug system consisting of switchable debug groups.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    public enum Debug
    {
        //---   switchable debug groups        ---//
        bot(                Shooter.NO                  ),          //logs bot behaviour
        breaklines(         Shooter.NO                  ),          //logs the optimized linebreak-algorithm
        bullethole(         Shooter.NO                  ),          //logs extra-info about bullet-holes
        d3ds(               Shooter.NO                  ),          //logs 3ds max parser
        floorChange(        Shooter.NO                  ),          //logs gravity matters
        fps(                Shooter.NO                  ),          //logs frames per second ( dispensable )
        freetriangle(       Shooter.NO                  ),          //logs extra info for all triangle-states
        hitpoint(           Shooter.NO                  ),          //logs wall hitpoints from shots
        item(               Shooter.NO                  ),          //logs information on items
        mouse(              Shooter.NO                  ),          //logs mouse movement
        playerAction(       Shooter.YES                 ),          //logs player actions
        shot(               Shooter.NO                  ),          //logs player's shot
        wallCollision(      Shooter.NO                  ),          //logs player's collisions

        bugfix(             Boolean.TRUE.booleanValue() ),          //paramount debug group
        ;

        //---   debug levels        ---//
        private     static  final   int         ELevelFatal         = 0;    //always shown
        private     static  final   int         ELevelError         = 1;
        private     static  final   int         ELevelWarning       = 2;
        private     static  final   int         ELevelNote          = 3;
        private     static  final   int         ELevelInfo          = 4;
        private     static  final   int         ELevelDebug         = 5;

        private     static  final   int         DEBUG_LEVEL         = ELevelDebug;

        private                     boolean     flag                = false;

        Debug( boolean flag )
        {
            this.flag = flag;
        }

        public final void out( Object obj )
        {
            if ( flag && DEBUG_LEVEL >= ELevelDebug ) System.out.println( obj );
        }

        public static final void info( Object obj )
        {
            if ( DEBUG_LEVEL >= ELevelInfo ) System.out.println( obj );
        }

        public static final void note( Object obj )
        {
            if ( DEBUG_LEVEL >= ELevelNote ) System.out.println( obj );
        }

        public static final void warning( Object obj )
        {
            if ( DEBUG_LEVEL >= ELevelWarning ) System.out.println( obj );
        }

        public static final void err( Object obj )
        {
            if ( DEBUG_LEVEL >= ELevelError ) System.err.println( obj );
        }

        public static final void fatal( Object obj )
        {
            if ( DEBUG_LEVEL >= ELevelFatal ) System.err.println( obj );
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
    }
