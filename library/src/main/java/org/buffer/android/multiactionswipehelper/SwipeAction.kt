@file:Suppress("unused")

package org.buffer.android.multiactionswipehelper

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SwipeAction(
    
    @StringRes
    override val labelRes: Int,
    
    @ActionPosition
    override val actionPosition: Int,
    
    @Direction
    override val swipeDirection: Int,
    
    @ColorRes
    override val backgroundColorRes: Int,
    
    @ColorRes
    override val labelColorRes: Int,
    
    @DrawableRes
    override val iconRes: Int,
    
    //
    
    @StringRes
    override val activeLabelRes: Int = labelRes,
    
    @ColorRes
    override val activeBackgroundColorRes: Int = backgroundColorRes,
    
    @ColorRes
    override val activeLabelColorRes: Int = labelColorRes,

    @DrawableRes
    override val activeIconRes: Int = iconRes
    
) : ISwipeAction
