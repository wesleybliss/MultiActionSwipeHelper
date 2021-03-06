package org.buffer.android.multiactionswipehelper

import android.graphics.Canvas
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchUIUtil
import android.view.View
import org.buffer.android.multiactionswipehelperhelper.R

internal class ItemTouchUIUtilImpl {

    internal class Lollipop : Honeycomb() {
        override fun onDraw(c: Canvas, recyclerView: androidx.recyclerview.widget.RecyclerView, view: View,
                            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            if (isCurrentlyActive) {
                var originalElevation: Any? = view.getTag(R.id.item_touch_helper_previous_elevation)
                if (originalElevation == null) {
                    originalElevation = ViewCompat.getElevation(view)
                    val newElevation = 1f + findMaxElevation(recyclerView, view)
                    ViewCompat.setElevation(view, newElevation)
                    view.setTag(R.id.item_touch_helper_previous_elevation, originalElevation)
                }
            }
            super.onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive)
        }

        private fun findMaxElevation(recyclerView: androidx.recyclerview.widget.RecyclerView, itemView: View): Float {
            val childCount = recyclerView.childCount
            var max = 0f
            for (i in 0 until childCount) {
                val child = recyclerView.getChildAt(i)
                if (child === itemView) {
                    continue
                }
                val elevation = ViewCompat.getElevation(child)
                if (elevation > max) {
                    max = elevation
                }
            }
            return max
        }

        override fun clearView(view: View) {
            val tag = view.getTag(R.id.item_touch_helper_previous_elevation)
            if (tag != null && tag is Float) {
                ViewCompat.setElevation(view, tag)
            }
            view.setTag(R.id.item_touch_helper_previous_elevation, null)
            super.clearView(view)
        }
    }

    internal open class Honeycomb : ItemTouchUIUtil {

        override fun clearView(view: View) {
            view.translationX = 0f
            view.translationY = 0f
        }

        override fun onSelected(view: View) {

        }

        override fun onDraw(c: Canvas, recyclerView: androidx.recyclerview.widget.RecyclerView, view: View,
                            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            view.translationX = dX
            view.translationY = dY
        }

        override fun onDrawOver(c: Canvas, recyclerView: androidx.recyclerview.widget.RecyclerView, view: View, dX: Float,
                                dY: Float, actionState: Int, isCurrentlyActive: Boolean) { }
    }

    internal class Gingerbread : ItemTouchUIUtil {

        private fun draw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, view: View,
                         dX: Float, dY: Float) {
            c.save()
            c.translate(dX, dY)
            parent.drawChild(c, view, 0)
            c.restore()
        }

        override fun clearView(view: View) {
            view.visibility = View.VISIBLE
        }

        override fun onSelected(view: View) {
            view.visibility = View.INVISIBLE
        }

        override fun onDraw(c: Canvas, recyclerView: androidx.recyclerview.widget.RecyclerView, view: View,
                            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            if (actionState != ItemTouchHelper.ACTION_STATE_DRAG) {
                draw(c, recyclerView, view, dX, dY)
            }
        }

        override fun onDrawOver(c: Canvas, recyclerView: androidx.recyclerview.widget.RecyclerView,
                                view: View, dX: Float, dY: Float,
                                actionState: Int, isCurrentlyActive: Boolean) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                draw(c, recyclerView, view, dX, dY)
            }
        }
    }
}
