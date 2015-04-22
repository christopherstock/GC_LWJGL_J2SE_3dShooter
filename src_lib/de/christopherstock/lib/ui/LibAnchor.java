/*  $Id: LibAnchor.java 1224 2012-12-31 12:32:24Z jenetic.bytemare@googlemail.com $
 *  =======================================================================================
 */
    package de.christopherstock.lib.ui;

    /**************************************************************************************
    *   The Image-Loader.
    *
    *   @author     Christopher Stock
    *   @version    0.3.11
    **************************************************************************************/
    public enum LibAnchor
    {
        /**     Anchor for horizontal left.     */  EAnchorHLeft(           0x01    ),
        /**     Anchor for horizontal center.   */  EAnchorHCenter(         0x02    ),
        /**     Anchor for horizontal right.    */  EAnchorHRight(          0x04    ),
        /**     Anchor for vertical top.        */  EAnchorVTop(            0x08    ),
        /**     Anchor for vertical center.     */  EAnchorVCenter(         0x10    ),
        /**     Anchor for vertical bottom.     */  EAnchorVBottom(         0x20    ),

        /**     Anchor for left top.            */  EAnchorLeftTop(         EAnchorHLeft.val   | EAnchorVTop.val    ),
        /**     Anchor for left middle.         */  EAnchorLeftMiddle(      EAnchorHLeft.val   | EAnchorVCenter.val ),
        /**     Anchor for left bottom.         */  EAnchorLeftBottom(      EAnchorHLeft.val   | EAnchorVBottom.val ),
        /**     Anchor for center top.          */  EAnchorCenterTop(       EAnchorHCenter.val | EAnchorVTop.val    ),
        /**     Anchor for center middle.       */  EAnchorCenterMiddle(    EAnchorHCenter.val | EAnchorVCenter.val ),
        /**     Anchor for center bottom.       */  EAnchorCenterBottom(    EAnchorHCenter.val | EAnchorVBottom.val ),
        /**     Anchor for right left.          */  EAnchorRightTop(        EAnchorHRight.val  | EAnchorVTop.val    ),
        /**     Anchor for right middle.        */  EAnchorRightMiddle(     EAnchorHRight.val  | EAnchorVCenter.val ),
        /**     Anchor for right bottom.        */  EAnchorRightBottom(     EAnchorHRight.val  | EAnchorVBottom.val ),
        ;

        /**************************************************************************************
        *   The underlying int-value this anchor consists of.
        **************************************************************************************/
        public          int     val         = 0;

        /**************************************************************************************
        *   Defines one anchor.
        *
        *   @param  aVal     The int-value to use for this anchor.
        **************************************************************************************/
        private LibAnchor( int aVal )
        {
            val = aVal;
        }
    }
