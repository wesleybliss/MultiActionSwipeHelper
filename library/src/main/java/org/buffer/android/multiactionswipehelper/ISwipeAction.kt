package org.buffer.android.multiactionswipehelper

interface ISwipeAction {

    val identifier: Int

    val actionPosition: Int

    val swipeDirection: Int

    val backgroundColor: Int

    val labelColor: Int

    val icon: Int

}
