/*  $Id: Level.java,v 1.3 2007/09/02 14:19:20 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.game;

    import  java.awt.geom.*;
    import  java.util.*;
    import  de.christopherstock.shooter.*;
import de.christopherstock.shooter.g3d.*;
    import  de.christopherstock.shooter.game.player.*;
    import de.christopherstock.shooter.gl.opengl.*;
    import  de.christopherstock.shooter.ui.*;
import  de.christopherstock.shooter.io.hid.*;

    /**************************************************************************************
    *   The Level.
    *
    *   @author     Christopher Stock
    *   @version    0.2
    **************************************************************************************/
    class Item
    {
        enum ItemType
        {
            ETypeCircle,
            ;
        }

        private     static  final   Colors              DEBUG_COLOR     = Colors.EGreen;
        private     static          Vector<Item>        itemQueue       = new Vector<Item>();

        private                     ItemType            type            = null;
        private                     float               radius          = 0.0f;
        private                     Vertex              anchor          = null;
        private                     boolean             collected       = false;

        private Item( ItemType aType, float x, float y, float z, float aRadius )
        {
            type   = aType;
            anchor = new Vertex( x, y, z, 0.0f, 0.0f );
            radius = aRadius;
        }

        protected static final void addItem( float x, float y, float z, float radius )
        {
            itemQueue.add( new Item( ItemType.ETypeCircle, x, y, z, radius ) );
            Debug.item.out( "ADDING ITEM ["+itemQueue.size()+"]" );
        }

        protected static final void drawAll()
        {
            //Debug.item.out( "drawing ALL items .."+itemQueue.size()+"" );
            for ( Item item : itemQueue )
            {
                item.draw();
            }
        }

        protected static final void checkCollisions()
        {
            for ( int j = itemQueue.size() - 1; j >= 0; --j )
            {
                if ( itemQueue.elementAt( j ).collected )
                {
                    itemQueue.removeElementAt( j );
                }
                else
                {
                    itemQueue.elementAt( j ).checkCollision();
                }
            }
        }

        private final void draw()
        {
            switch ( type )
            {
                case ETypeCircle:
                {
                    if ( Shooter.DEBUG_DRAW_ITEM_CIRCLE )
                    {
                        //Debug.item.out( "drawing item .." );
                        GLView.singleton.drawFace( new FaceEllipseFloor( null, DEBUG_COLOR, anchor.x, anchor.y, anchor.z, radius, radius ) );
                    }
                    break;
                }
            }
        }

        private final void checkCollision()
        {
            //check if not inited
            /*
            if ( Player.user.getCollisionCircle() == null ) return;
            Debug.note( "check .." );
            */

            //check collision of 2 circles ( easy  task.. )
            Area player = new Area( Player.singleton.cylinder.getCircle() );
            Ellipse2D.Float itemCircle = new Ellipse2D.Float( anchor.x - radius, anchor.y - radius, 2 * radius, 2 * radius );
            Area item   = new Area( itemCircle );
            player.intersect( item );
            if ( !player.isEmpty() )
            {
                Sound.playSoundFx( Sound.ESoundBullet1 );
                Debug.info( "item collected" );
                collected = true;
            }
            else
            {
                /*
                            Debug.note( "not collected!" );
                            Debug.note( Player.user.getCollisionCircle().getCenterX() + "==" + Player.user.getCollisionCircle().getCenterY() );
                            Debug.note( Player.user.getCollisionCircle().getWidth() + "--" + Player.user.getCollisionCircle().getHeight() );
                            Debug.note( itemCircle.getCenterX() + "==" + itemCircle.getCenterY() );
                            Debug.note( itemCircle.getWidth() + "--" + itemCircle.getHeight() );
                */
            }
        }
    }
