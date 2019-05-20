package org.buffer.android.multiactionswipehelper

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.buffer.android.multiactionswipehelperhelper.R

interface SwipeAction {
    
    /**
     * Position of the action, from the starting point of the drag
     * Can be first or second (0 or 1)
     * Use zero if there's only one action on swipe
     */
    @ActionPosition
    val actionPosition: Int
    
    /**
     * Direction that should trigger this swipe
     */
    @Direction
    val swipeDirection: Int
    
    /**
     * Gets the label to be shown on swipe
     * Different values can be returned depending on
     * the [position] in the list, and whether the drag
     * distance [isUnderThreshold]
     */
    @StringRes
    fun getLabelRes(position: Int, isUnderThreshold: Boolean) : Int
    
    /**
     * Gets the label's text size dimension
     * Different values can be returned depending on
     * the [position] in the list, and whether the drag
     * distance [isUnderThreshold]
     */
    @DimenRes
    fun getLabelSizeRes(position: Int, isUnderThreshold: Boolean) : Int = R.dimen.text_body
    
    /**
     * Gets the background color to be shown on swipe
     * Different values can be returned depending on
     * the [position] in the list, and whether the drag
     * distance [isUnderThreshold]
     */
    @ColorRes
    fun getBackgroundColorRes(position: Int, isUnderThreshold: Boolean) : Int
    
    /**
     * Gets the label color to be shown on swipe
     * Different values can be returned depending on
     * the [position] in the list, and whether the drag
     * distance [isUnderThreshold]
     */
    @ColorRes
    fun getLabelColorRes(position: Int, isUnderThreshold: Boolean) : Int
    
    /**
     * Gets the drawable to be shown on swipe
     * Different values can be returned depending on
     * the [position] in the list, and whether the drag
     * distance [isUnderThreshold]
     */
    @DrawableRes
    fun getIconRes(position: Int, isUnderThreshold: Boolean) : Int
    
}
