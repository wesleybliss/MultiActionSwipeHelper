package org.buffer.android.multiactionswipehelper

import androidx.annotation.ColorRes

interface ISwipeAction {
    
    val labelRes: Int
    
    @ActionPosition
    val actionPosition: Int
    
    @Direction
    val swipeDirection: Int
    
    val backgroundColorRes: Int
    
    val labelColorRes: Int
    
    val iconRes: Int
    
    //
    
    val activeLabelRes: Int
    
    val activeBackgroundColorRes: Int
    
    val activeLabelColorRes: Int
    
    val activeIconRes: Int
    
}
