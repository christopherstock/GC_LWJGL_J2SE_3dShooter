/*  $Id: Debug.java 221 2011-01-25 00:00:17Z jenetic.bytemare $
 *  =======================================================================================
 */
    package de.christopherstock.shooter;

    import  de.christopherstock.lib.*;

    /**************************************************************************************
    *   The debug system consisting of switchable debug groups.
    *
    *   @author     Christopher Stock
    *   @version    0.3
    **************************************************************************************/
    public enum ShooterDebugSystem implements LibDebug
    {
        mouse(              false   ),      //logs mouse movement
        levels(             false   ),      //logs player actions
        glimage(            false   ),      //logs player actions
        lwjgl(              false   ),      //logs lwjgl engine
        avatarImages(       false   ),      //logs debugging avatar images
        floorChange(        false   ),      //logs gravity matters
        playerAction(       false   ),      //logs player actions
        gl(                 false   ),      //logs player's collisions
        faceQueue(          false   ),      //logs face queue system
        d3ds(               false   ),      //logs 3ds max parser
        keys(               false   ),      //logs lwjgl engine
        bot(                false   ),      //logs bot behaviour
        breaklines(         false   ),      //logs the optimized linebreak-algorithm
        bullethole(         false   ),      //logs extra-info about bullet-holes
        fps(                false   ),      //logs frames per second ( dispensable )
        freetriangle(       false   ),      //logs extra info for all triangle-states
        hitpoint(           false   ),      //logs wall hitpoints from shots
        item(               false   ),      //logs information on items
        shot(               false   ),      //logs player's shot
        wallCollision(      false   ),      //logs player's collisions

        bugfix(             true    ),      //paramount debug group
        error(              true    ),      //paramount debug group

        ;

        private                     boolean         debugEnabled        = false;

        private ShooterDebugSystem( boolean aDebugEnabled )
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
