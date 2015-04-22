/*  $Id: Form.java,v 1.3 2007/09/02 14:19:21 Besitzer Exp $
 *  =======================================================================================
 */
    package de.christopherstock.shooter.ui;

    /**************************************************************************************
    *   The Image-Loader.
    *
    *   @author     stock
    *   @version    1.0
    **************************************************************************************/
    public enum Anchor
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
        *   @param  val     The int-value to use for this anchor.
        **************************************************************************************/
        private Anchor( int val )
        {
            this.val = val;
        }
    }
