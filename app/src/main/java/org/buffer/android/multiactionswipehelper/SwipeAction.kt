package org.buffer.android.multiactionswipehelper

interface SwipeAction {

    val identifier: Int

    val actionPosition: Int

    val swipeDirection: Int

    val backgroundColor: Int

    val labelColor: Int

    val icon: Int

}