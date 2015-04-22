/*  $Id: Debug.java 181 2010-11-13 13:31:37Z jenetic.bytemare $
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
    public class Debug
    {
        public  static  final       LogLevel    logLevel        = LogLevel.ELevelDebug;

        //---   switchable debug groups        ---//
        public      static  final   LibDebug    lwjgl           = new LibDebug( Shooter.YES, logLevel    );  //logs lwjgl engine
        public      static  final   LibDebug    mouse           = new LibDebug( Shooter.YES, logLevel    );  //logs mouse movement
        public      static  final   LibDebug    floorChange     = new LibDebug( Shooter.YES, logLevel    );  //logs gravity matters
        public      static  final   LibDebug    playerAction    = new LibDebug( Shooter.YES, logLevel    );  //logs player actions

        public      static  final   LibDebug    glimage         = new LibDebug( Shooter.NO,  logLevel    );  //logs player actions
        public      static  final   LibDebug    faceQueue       = new LibDebug( Shooter.NO,  logLevel    );  //logs face queue system
        public      static  final   LibDebug    d3ds            = new LibDebug( Shooter.NO,  logLevel    );  //logs 3ds max parser
        public      static  final   LibDebug    keys            = new LibDebug( Shooter.NO,  logLevel    );  //logs lwjgl engine
        public      static  final   LibDebug    bot             = new LibDebug( Shooter.NO,  logLevel    );  //logs bot behaviour
        public      static  final   LibDebug    breaklines      = new LibDebug( Shooter.NO,  logLevel    );  //logs the optimized linebreak-algorithm
        public      static  final   LibDebug    bullethole      = new LibDebug( Shooter.NO,  logLevel    );  //logs extra-info about bullet-holes
        public      static  final   LibDebug    fps             = new LibDebug( Shooter.NO,  logLevel    );  //logs frames per second ( dispensable )
        public      static  final   LibDebug    freetriangle    = new LibDebug( Shooter.NO,  logLevel    );  //logs extra info for all triangle-states
        public      static  final   LibDebug    hitpoint        = new LibDebug( Shooter.NO,  logLevel    );  //logs wall hitpoints from shots
        public      static  final   LibDebug    item            = new LibDebug( Shooter.NO,  logLevel    );  //logs information on items
        public      static  final   LibDebug    shot            = new LibDebug( Shooter.NO,  logLevel    );  //logs player's shot
        public      static  final   LibDebug    wallCollision   = new LibDebug( Shooter.NO,  logLevel    );  //logs player's collisions

        public      static  final   LibDebug    bugfix          = new LibDebug( Shooter.YES, logLevel    );  //paramount debug group

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
