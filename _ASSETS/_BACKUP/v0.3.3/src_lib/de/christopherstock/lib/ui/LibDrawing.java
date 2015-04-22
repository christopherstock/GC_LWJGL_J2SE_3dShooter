
    package de.christopherstock.lib.ui;

    import  java.awt.*;

    /********************************************************************//**
    *   Offers independent drawing operations.
    ************************************************************************/
    public class LibDrawing
    {
        /************************************************************************//**
        *   All nine available anchors for drawables.
        ****************************************************************************/
        public static enum LibAnchor
        {
            ELeftTop,
            ELeftMiddle,
            ELeftBottom,
            ECenterTop,
            ECenterMiddle,
            ECenterBottom,
            ERightTop,
            ERightMiddle,
            ERightBottom,
            ;
        }

        public static enum TriangleDirection
        {
            ELeft,
            ERight,
            ;
        }

        /************************************************************************//**
        *   Paints a filled rect in the specified color and dimension.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  x           destination x to draw to.
        *   @param  y           destination y to draw to.
        *   @param  width       destination rect's width.
        *   @param  height      destination rect's height.
        *   @param  color       The color to fill the rect with.
        ****************************************************************************/
        public static void fillRect( Graphics2D gc, int x, int y, int width, int height, Color color )
        {
            gc.setPaint( color );
            gc.fillRect( x, y, x + width, y + height );
        }

        /************************************************************************//**
        *   Fills a rect with a specified color.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  x           destination x to draw to.
        *   @param  y           destination y to draw to.
        *   @param  width       destination rect's width.
        *   @param  height      destination rect's height.
        *   @param  color       The color to fill the rect with.
        ****************************************************************************/
/*
        public static void fillTriangle( Graphics gc, Paint p, Path2D pa, TriangleDirection triangleDirection, int x, int y, int width, int height, int color )
        {
            switch ( triangleDirection )
            {
                case ELeft:
                {
                    pa.reset();

                    pa.moveTo( x, y );
                    pa.lineTo( x + width, y - height / 2 );

                    pa.lineTo( x + width / 2, y );

                    pa.lineTo( x + width, y + height / 2 );
                    pa.lineTo( x, y );
                    break;
                }

                case ERight:
                {
                    pa.reset();

                    pa.moveTo( x, y );
                    pa.lineTo( x - width, y - height / 2 );

                    pa.lineTo( x - width / 2, y );

                    pa.lineTo( x - width, y + height / 2 );
                    pa.lineTo( x, y );
                    break;
                }
            }

            //p.setStyle(     Style.FILL  );
            //p.setColor(     color       );
            //p.setAntiAlias( true        );

            //gc.drawPath( pa, p );
        }
*/
        /************************************************************************//**
        *   Fills a rect with a specified color.
        *
        *   @param      gc          The Context to draw onto.
        *   @param      x           destination x to draw to.
        *   @param      y           destination y to draw to.
        *   @param      width       destination rect's width.
        *   @param      height      destination rect's height.
        *   @param      colorBorder The color to draw the rect's border with.
        *   @param      colorFill   The color to fill the rect with.
        ****************************************************************************/
/*
        public static void fillBorderedRect( Canvas gc, Paint p, int x, int y, int width, int height, int colorBorder, int colorFill )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     colorBorder );
            p.setAntiAlias( false       );

            gc.drawRect(    x, y, x + width, y + height, p );

            p.setColor(     colorFill   );
            gc.drawRect(    x + 1, y + 1, x + width - 1, y + height - 1, p );
        }
*/
/*
        public static void drawRect( Canvas gc, Paint p, int x, int y, int width, int height, int colorBorder )
        {
            p.setStyle(         Style.STROKE    );
            p.setColor(         colorBorder     );
            p.setAntiAlias(     false           );

            gc.drawRect( x, y, x + width - 1, y + height - 1, p );
        }
*/
        /************************************************************************//**
        *   Fills a rect with a vertical color-gradient.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  x           destination x to draw to.
        *   @param  y           destination y to draw to.
        *   @param  width       destination rect's width.
        *   @param  height      destination rect's height.
        *   @param  colorTop    The color for the top row of the gradient.
        *   @param  colorBottom The color for the bottom row of the gradient.
        ****************************************************************************/
        //public static void fillGradientRect( Canvas gc, int x, int y, int width, int height, GradientDrawable gd )
        {
/*
            gd.setBounds( x, y, x + width, y + height );
            gd.setSize( width, height );
            gd.draw( gc );
*/
        }
/*
        public static final void fillRoundRect( Canvas gc, Paint p, int x, int y, int width, int height, float radius, int col )
        {
            fillRoundRect( gc, p, x, y, width, height, radius, col, null );
        }
*/
/*
        public static final void fillRoundRect( Canvas gc, Paint p, int x, int y, int width, int height, float radius, int col, Integer alpha )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     col         );
            p.setAntiAlias( true        );

            //set alpha if desired
            if ( alpha != null )
            {
                p.setAlpha( alpha.intValue() );
            }

            //draw rounded rect
            gc.drawRoundRect( new RectF( x, y, x + width, y + height ), radius, radius, p );
            p.setAlpha( 255 );
        }
*/
/*
        public static final void fillGradientRoundRect( Canvas gc, int x, int y, int width, int height, float radius, GradientDrawable gd )
        {
            gd.setBounds( x, y, x + width, y + height );
            gd.setCornerRadius( radius );
            gd.setSize( width, height );
            gd.draw( gc );
        }

        public static final void fillCircle( Paint p, Canvas gc, int x, int y, int width, int height, int col )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     col         );
            p.setAntiAlias( true        );

            //draw rounded rect
            gc.drawArc( new RectF( x, y, x + width, y + height ), 0, 360, true, p );
        }
*/
        /************************************************************************//**
        *   Draws a line.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  x1          line's source point x.
        *   @param  y1          line's source point y.
        *   @param  x2          line's destinatuion point x.
        *   @param  y2          line's destinatuion point y.
        *   @param  col         line's color.
        ****************************************************************************/
/*
        public static void drawLine( Canvas gc, Paint p, int x1, int y1, int x2, int y2, int col )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     col         );
            p.setAntiAlias( false       );

            gc.drawLine( x1, y1, x2, y2, p );
        }

        public static void drawBitmap( Canvas gc, BitmapDrawable img, int x, int y, LibAnchor ank )
        {
            drawBitmap( gc, img, x, y, null, ank );
        }
*/
        /********************************************************************//**
        *   Draws a bitmap.
        *
        *   @param      gc      The context to draw onto.
        *   @param      img     The img to draw.
        *   @param      x       The destination x.
        *   @param      y       The destination y.
        *   @param      ank     The anchor-point.
        ************************************************************************/
/*
        public static void drawBitmap( Canvas gc, BitmapDrawable img, int x, int y, Integer alpha, LibAnchor ank )
        {
            //only draw if this drawable is not null and if it's bitmap is not recycled !
            if ( img != null && img.getBitmap() != null && !img.getBitmap().isRecycled() )
            {
                //translate x-point according to anchor
                if ( ank == LibAnchor.ELeftTop    || ank == LibAnchor.ELeftMiddle   || ank == LibAnchor.ELeftBottom   )  x -= 0;
                if ( ank == LibAnchor.ECenterTop  || ank == LibAnchor.ECenterMiddle || ank == LibAnchor.ECenterBottom )  x -= img.getBitmap().getWidth() / 2;
                if ( ank == LibAnchor.ERightTop   || ank == LibAnchor.ERightMiddle  || ank == LibAnchor.ERightBottom  )  x -= img.getBitmap().getWidth();
                //translate y-point according to anchor
                if ( ank == LibAnchor.ELeftTop    || ank == LibAnchor.ECenterTop    || ank == LibAnchor.ERightTop     )  y -= 0;
                if ( ank == LibAnchor.ELeftMiddle || ank == LibAnchor.ECenterMiddle || ank == LibAnchor.ERightMiddle  )  y -= img.getBitmap().getHeight() / 2;
                if ( ank == LibAnchor.ELeftBottom || ank == LibAnchor.ECenterBottom || ank == LibAnchor.ERightBottom  )  y -= img.getBitmap().getHeight();

                //draw the image
                img.setBounds( x, y, x + img.getBitmap().getWidth(), y + img.getBitmap().getHeight() );
                if ( alpha != null ) img.setAlpha( alpha.intValue() );
                img.draw( gc );
            }
        }

        public static void drawString( Canvas gc, Paint p, String str, LibFont fnt, int col, int x, int y, LibAnchor ank )
        {
            int drawX        = x;
            int drawY        = y;
            int stringWidth  = fnt.getStringWidth( str  );
            int stringHeight = fnt.getHeight();

            //translate x-point according to anchor
            if ( ank == LibAnchor.ELeftTop    || ank == LibAnchor.ELeftMiddle   || ank == LibAnchor.ELeftBottom   )  drawX -= 0;
            if ( ank == LibAnchor.ECenterTop  || ank == LibAnchor.ECenterMiddle || ank == LibAnchor.ECenterBottom )  drawX -= stringWidth / 2;
            if ( ank == LibAnchor.ERightTop   || ank == LibAnchor.ERightMiddle  || ank == LibAnchor.ERightBottom  )  drawX -= stringWidth;

            //translate y-point according to anchor
            if ( ank == LibAnchor.ELeftTop    || ank == LibAnchor.ECenterTop    || ank == LibAnchor.ERightTop     )  drawY += fnt.getAscend();
            if ( ank == LibAnchor.ELeftMiddle || ank == LibAnchor.ECenterMiddle || ank == LibAnchor.ERightMiddle  )  drawY = drawY - fnt.getDescend() + stringHeight / 2;
            if ( ank == LibAnchor.ELeftBottom || ank == LibAnchor.ECenterBottom || ank == LibAnchor.ERightBottom  )  drawY -= fnt.getDescend();

            //set color, font, antialiasing and draw ( symbian draws all strings anchored LEFT|BOTTOM at the baseline ! )
            p.setStyle(     Style.FILL              );
            p.setColor(     col                     );
            p.setAntiAlias( true                    );

            p.setTypeface(  fnt.getTypeface()       );
            p.setTextSize(  fnt.getTextSize()       );

            gc.drawText( str, drawX, drawY, p );
*/

/*
            //draw anchor-crosshair if desired
            if ( AppsDebug.DEBUG_DRAW_STRING_ANCHOR_CROSSHAIR )
            {
                //save current canvas
                int canvasSave = gc.save();

                //release clipping
                gc.clipRect( Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Region.Op.REPLACE );

                p.setColor( ColorDebug.EStringAnchorCrosshair );
                gc.drawLine( x - 2, y,     x + 3, y,     p );
                gc.drawLine( x,     y - 2, x,     y + 3, p );

                //restore saved canvas
                gc.restoreToCount( canvasSave );
            }
*/
/*
        }

        public static void drawShadedString( Canvas gc, Paint p, String str, LibFont fnt, int col, int colShadow, int x, int y, LibDrawing.LibAnchor ank )
        {
            drawString( gc, p, str, fnt, colShadow, x + 1, y + 1, ank );
            drawString( gc, p, str, fnt, col,       x,     y,     ank );
        }

        public static final void drawBoxedShadedString( Canvas gc, Paint p, int x, int y, int width, int height, int colBg, String string, LibFont font, int colFontFg, int colFontBg )
        {
            LibDrawing.fillRect( gc, p, x, y, width, height, colBg );
            LibDrawing.drawShadedString( gc, p, string, font, colFontFg, colFontBg, x + width / 2, y + height / 2, LibAnchor.ECenterMiddle );
        }

        public static final Path createStarPath( int outerStarRadius, int innerStarRadius )
        {
            Path pa = new Path();
            pa.setFillType( FillType.EVEN_ODD );
            for ( int angle = -90; angle < 270; angle += 72 )
            {
                Point outerPoint = LibMath.createRotatedPoint( angle, outerStarRadius );
                if ( angle == -90 )
                {
                    pa.setLastPoint( outerPoint.x, outerPoint.y );
                }
                else
                {
                    pa.lineTo( outerPoint.x, outerPoint.y );
                }
                Point innerPoint = LibMath.createRotatedPoint( angle + 36, innerStarRadius );
                pa.lineTo( innerPoint.x, innerPoint.y );
            }
            return pa;
        }
*/
    }
