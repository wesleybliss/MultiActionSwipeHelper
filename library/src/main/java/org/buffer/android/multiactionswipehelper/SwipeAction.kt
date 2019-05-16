@file:Suppress("unused")

package org.buffer.android.multiactionswipehelper

data class SwipeAction(
    override val identifier: Int,
    override val actionPosition: Int,
    override val swipeDirection: Int,
    override val backgroundColor: Int,
    override val labelColor: Int,
    override val icon: Int
) : ISwipeAction
