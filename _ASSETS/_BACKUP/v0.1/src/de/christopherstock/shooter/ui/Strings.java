/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui;

    import  java.awt.*;
    import  java.util.*;
    import  java.util.regex.*;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.gl.*;

    /**************************************************************************************
    *   The Heads Up Display.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public class Strings
    {
        /*********************************************************************************
        *   These characters will break lines.
        *********************************************************************************/
        private     static  final   char[]              LINEBREAK_CHARS             =
        {
            '\n', ' ', ',', '?', '-', ':', ';', '/', '.', '!'
        };

        /*********************************************************************************************
        *   Draws a string.
        *
        *   @param  g       The Graphics-Object to draw onto.
        *   @param  col     The color for the String to be drawn.
        *   @param  str     The String to be drawn.
        *   @param  fnt     The font the String is to be drawn in.
        *   @param  x       The String's x-location.
        *   @param  y       The String's y-location.
        *   @param  anchor  The desired anchor for the String.
        *********************************************************************************************/
        public static final void drawString( Graphics2D g, Color col, Font fnt, Anchor anchor, String str, int x, int y )
        {
            drawString( g, col, null, null, fnt, anchor, str, x, y );
        }

        public static final void drawString( Graphics2D g, Color colFg, Color colShadow, Color colOutline, Font fnt, Anchor anchor, String str, int x, int y )
        {
            int         stringWidth     = getStringWidth(  g, str, fnt );
            int         stringHeight    = getStringHeight( g, fnt );

            //assign the anchor
            if ( ( anchor.val & Anchor.EAnchorHCenter.val   ) != 0) x -= stringWidth  / 2;
            if ( ( anchor.val & Anchor.EAnchorHRight.val    ) != 0) x -= stringWidth;
            if ( ( anchor.val & Anchor.EAnchorVCenter.val   ) != 0) y += stringHeight / 2;
            if ( ( anchor.val & Anchor.EAnchorVTop.val      ) != 0) y += stringHeight;

            //set font before drawing
            g.setFont(      fnt );

            //draw shadow
            if ( colShadow != null )
            {
                g.setPaint(   colShadow );
                g.drawString( str, x + 1, y - g.getFontMetrics().getDescent() + 1 );
            }

            //draw outline
            if ( colOutline != null )
            {
                g.setPaint(   colOutline );
                g.drawString( str, x,     y - g.getFontMetrics().getDescent() - 1 );
                g.drawString( str, x - 1, y - g.getFontMetrics().getDescent() - 1 );
                g.drawString( str, x - 1, y - g.getFontMetrics().getDescent()     );
                g.drawString( str, x - 1, y - g.getFontMetrics().getDescent() + 1 );
                g.drawString( str, x + 1, y - g.getFontMetrics().getDescent() - 1 );
                g.drawString( str, x + 1, y - g.getFontMetrics().getDescent()      );
                g.drawString( str, x + 1, y - g.getFontMetrics().getDescent() + 1 );
                g.drawString( str, x,     y - g.getFontMetrics().getDescent() + 1 );
            }

            //draw fg
            g.setPaint(     colFg );
            g.drawString( str, x, y - g.getFontMetrics().getDescent() );

        }

        public static final int getStringWidth( Graphics2D g, String str, Font fnt )
        {
            g.setFont( fnt );
            FontMetrics fm = g.getFontMetrics();
            return fm.charsWidth( str.toCharArray(), 0, str.length() );
            //return (int)fnt.getStringBounds( str, g.getFontRenderContext() ).getWidth();
        }

        public static final int getStringHeight( Graphics2D g, Font fnt )
        {
            g.setFont( fnt );
            FontMetrics fm = g.getFontMetrics();
            return fm.getHeight();
            //return (int)fnt.getStringBounds( str, g.getFontRenderContext() ).getHeight();
        }

        public static final String[] splitByWhitespaces( String heuhaufen )
        {
            Vector<String>  chunks      = null;
            Pattern         pat         = null;

            pat     = Pattern.compile( "[\\s]", Pattern.CASE_INSENSITIVE|Pattern.DOTALL );
            chunks  = new Vector<String>( Arrays.asList( pat.split( heuhaufen ) ) );

            //prune empty elements
            for ( int j = chunks.size() - 1; j >= 0; --j )
            {
                if ( chunks.elementAt( j ).length() == 0 ) chunks.removeElementAt( j );
            }

            //return string array
            return chunks.toArray( new String[] {} );

        }

        private static final int getNearestLinebreakIndex( String aString, int startIndex )
        {
            //start a new end-index-search
            int endIndex = -1;

            //skip leading whitespaces
            while( startIndex < aString.length() && aString.charAt( startIndex ) == ' ' ) ++startIndex;

            //browse all linebreak-chars
            for ( int i = 0; i < LINEBREAK_CHARS.length; ++i )
            {
                //check if this linebreak-char was found
                int foundIndex = aString.substring( startIndex ).indexOf( LINEBREAK_CHARS[ i ] );
                if ( foundIndex == -1 )
                {
                }
                else if ( endIndex == -1 || startIndex + foundIndex < endIndex )
                {
                    endIndex = startIndex + foundIndex;
                }
            }

            return endIndex;

        }

        public static final String[] getViaRegEx( String haystack, String pattern )
        {
            Pattern         pat         = null;
            Matcher         mat         = null;
            Vector<String>  ret         = new Vector<String>();

            pat     = Pattern.compile( pattern, Pattern.CASE_INSENSITIVE|Pattern.DOTALL );
            mat     = pat.matcher( haystack );

            while ( mat.find() )
            {
                ret.addElement( mat.group() );
            }

            //return all strings or null if no string was found
            String[] sa = ret.toArray( new String[] {} );
            return ( sa.length == 0 ? null : sa );

        }

        public static final String[][] getViaRegExGrouped( String haystack, int numberOfCaptureGroups, String pattern )
        {
            Pattern             pat         = null;
            Matcher             mat         = null;
            Vector<String[]>    ret         = new Vector<String[]>();

            pat     = Pattern.compile( pattern, Pattern.CASE_INSENSITIVE|Pattern.DOTALL );
            mat     = pat.matcher( haystack );

            while ( mat.find() )
            {
                String[] sa = new String[ numberOfCaptureGroups ];
                for ( int i = 0; i < numberOfCaptureGroups; ++i )
                {
                    sa[ i ] = mat.group( i + 1 );
                }
                ret.addElement( sa );
            }

            //return all strings or null if no string was found
            String[][] saa = ret.toArray( new String[][] {} );
            return ( saa.length == 0 ? null : saa );

        }

        public static final String[] breakLinesOptimized( Graphics2D g2d, String aString, Font aFont, int aTargetWidth )
        {
            //avoid exceptions
            if ( aString == null || aString.length() == 0 ) return new String[] {};

            //lines to return
            Vector<String> ret = new Vector<String>();

            int startIndex          = 0;
            int tooHighEndIndex     = -1;

            //browse till breaked
            out:
            while ( true )
            {
                //objective is to find the correct end index .. ;)
                int correctEndIndex     = -1;

                //get too high end index ( nearest linebreak )
                tooHighEndIndex = getNearestLinebreakIndex( aString, startIndex );

                //check if NO separator was found
                if ( tooHighEndIndex == -1 )
                {
                    tooHighEndIndex = aString.length();
                }
                else
                {
                    ++tooHighEndIndex;
                }

                //find the correct width for the string to take
                meassureWidth:
                while ( true )
                {
                    //check if the current meassured string extends the width
                    if ( getStringWidth( g2d, aString.substring( startIndex, tooHighEndIndex ).trim(), aFont ) > aTargetWidth )
                    {
                        //check if a smaller piece was found before
                        if ( correctEndIndex == -1 )                                    //                        if ( cutEndIndex == -1 || cutStartIndex == cutEndIndex /* && separatorFound */ )
                        {
                            //try next meassuring with 1 char substracted
                            --tooHighEndIndex;
                            continue meassureWidth;
                        }

                        //correct end index already specified .. pick it and assign new start
                        ret.addElement( aString.substring( startIndex, correctEndIndex ).trim() );
                        startIndex = correctEndIndex;
                    }
                    //is this the last chunk?
                    else if ( tooHighEndIndex == aString.length() )
                    {
                        //pick the rest!
                        ret.addElement( aString.substring( startIndex, tooHighEndIndex ).trim() );
                        break out;
                    }
                    //check if this linebreak char is a manual linebreak
                    else if ( tooHighEndIndex < aString.length() && aString.substring( tooHighEndIndex - 1, tooHighEndIndex ).indexOf( '\n' ) != -1 )
                    {
                        //cut the string and change the startIndex
                        ret.addElement( aString.substring( startIndex, tooHighEndIndex ).trim() );

                        startIndex = tooHighEndIndex;
                    }
                    //this is a correct meassured string that fits the screen
                    else
                    {
                        correctEndIndex = tooHighEndIndex;

                        //get the next possible linebreak from this position ( correct new too high )
                        tooHighEndIndex = getNearestLinebreakIndex( aString, correctEndIndex );
                        if ( tooHighEndIndex == -1 )
                        {
                            tooHighEndIndex = aString.length();
                        }
                        else
                        {
                            ++tooHighEndIndex;  //skip this lb-char
                        }

                        //check the next distanced linebreak
                        continue meassureWidth;

                    }
                    break meassureWidth;
                }
            }

            String[] retArr = new String[ ret.size() ];
            for ( int i = 0; i < ret.size(); ++i )
            {
                retArr[ i ] = ret.elementAt( i );
            }

            return retArr;

        }

        public static final void debugLineBreaks()
        {
            String[] lines = Strings.breakLinesOptimized( (Graphics2D)GLPanel.glPanel.getGraphics(), "123456789-abcdefgh\nijklmnopqrstuvwxyzAnnodinio comes up      with a.gamethatwicgdfgfggfgfgdfdggerrtetetkedandwhacked\n\n\n\ntesttest2test3test4students can play between php and pear sessions\n 123456789abc\n o \n \ndefghijklmnopqrstuvwxyz Mauris porttitor sodales congue. Integer venenatis sem vel libero vehicula hendrerit. Ut in tincidunt felis. Aenean nec mattis magna. Suspendisse enim nisl, euismod quis suscipit vel, porttitor egestas elit. Suspendisse facilisis scelerisque mi, ut pharetra felis porta eget. Proin ultrices molestie ornare. Morbi hendrerit orci eu mauris blandit ut dapibus lacus pulvinar. Proin tempor auctor nisl, quis blandit lacus mollis vel. Curabitur mollis diam quis massa commodo vestibulum. Maecenas in nunc vitae sem vestibulum commodo. In congue elit non sem ornare congue. Aliquam ac purus nibh, id imperdiet neque. Aenean consectetur dictum dictum. Curabitur gravida posuere sodales. Nam augue odio, convallis quis volutpat id, accumsan aliquet turpis. Nullam imperdiet dignissim dui, sed fermentum libero convallis in. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla tempus fringilla lorem, non euismod libero lobortis at. Suspendisse aliquet, nunc ut porta ornare, risus leo placerat leo, eget varius magna dui ac massa. ", Fonts.EDebug, 106 );
            for ( int i = 0; i < lines.length; ++i )
            {
                Debug.info( "LINEBREAK TEST: [" + lines[ i ] + "]" );
            }
        }
    }
