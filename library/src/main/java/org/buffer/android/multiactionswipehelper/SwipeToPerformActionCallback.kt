package org.buffer.android.multiactionswipehelper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import org.buffer.android.multiactionswipehelperhelper.R

class SwipeToPerformActionCallback(
    private val swipeListener: SwipeActionListener,
    private val textPadding: Int = 0,
    private var conversationActions: List<SwipeAction>,
    @Direction private val allowedDirections: Int = LEFT or RIGHT,
    var returnAfterSwipe: Boolean = false,
    var isUnderFlingThreshold: (dX: Float, parentWidth: Int) -> Boolean = ::defaultIsUnderFlingThreshold)
    : SwipePositionItemTouchHelper.Callback() {
    
    companion object {

        /**
         * Default method for calculating if the drag distance
         * is over the default threshold, which is 50%
         */
        fun defaultIsUnderFlingThreshold(dX: Float, parentWidth: Int) : Boolean =
            Math.abs(dX) < (parentWidth / 2)
        
    }
    
    override fun getMovementFlags(
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder) =
        makeMovementFlags(0, allowedDirections)
    
    private val background = ColorDrawable()
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    
    private var currentIcon: Drawable? = null
    private var currentLabel: String = ""
    
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) : Boolean = false
    
    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean) {
        
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        
        val dragDirection = if (dX < 0) RIGHT else LEFT
        val parentWidth = recyclerView.width
        
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val itemCenter = (itemView.bottom + itemView.top) / 2f
        val isCanceled = dX == 0f && !isCurrentlyActive
        
        if (isCanceled) {
            
            clearCanvas(
                canvas, 
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat())
            
            super.onChildDraw(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive)
            
            return
            
        }

        val paint = Paint()
        val underThreshold = isUnderFlingThreshold(dX, parentWidth)
        
        if (isCurrentlyActive) {
            val action =
                if (underThreshold)
                    ActionHelper.getFirstActionWithDirection(conversationActions, dragDirection)
                else
                    ActionHelper.getSecondActionWithDirection(conversationActions, dragDirection)
            
            if (action != null) recyclerView.also {
                val position = viewHolder.adapterPosition
                background.color = it.getColorCompat(action.getBackgroundColorRes(position, underThreshold))
                currentIcon = it.getDrawableCompat(action.getIconRes(position, underThreshold))
                paint.color = it.getColorCompat(action.getLabelColorRes(position, underThreshold))
                currentLabel = it.getStringCompat(action.getLabelRes(position, underThreshold))
            }
            
        }
        
        val intrinsicWidth = currentIcon?.intrinsicWidth ?: 0
        val intrinsicHeight = currentIcon?.intrinsicHeight ?: 0
        val currentIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val currentIconBottom = currentIconTop + intrinsicHeight
        val currentIconMargin = (itemHeight - intrinsicHeight) / 2
        
        val currentIconLeft: Int
        val currentIconRight: Int
        val textPositionX: Int
        val textPositionY: Float
        
        paint.textSize = recyclerView.context.resources
                .getDimensionPixelSize(R.dimen.text_large_body).toFloat()
        paint.textAlign = Paint.Align.LEFT
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        
        val textBounds = Rect()
        paint.getTextBounds(currentLabel, 0, currentLabel.length, textBounds)
        
        val textWidth = textBounds.width()
        textPositionY = itemCenter - textBounds.exactCenterY()
        
        if (dragDirection == RIGHT) {
            
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom)
            
            currentIconLeft = itemView.right - currentIconMargin - intrinsicWidth
            currentIconRight = itemView.right - currentIconMargin
            
            currentIcon?.setBounds(
                currentIconLeft,
                currentIconTop,
                currentIconRight,
                currentIconBottom)
            
            textPositionX = (itemView.right - currentIconMargin -
                intrinsicWidth - textPadding - textWidth)
            
        } else {
            
            background.setBounds(
                itemView.left,
                itemView.top, 
                itemView.left + dX.toInt(),
                itemView.bottom)
            
            currentIconLeft = itemView.left + currentIconMargin
            currentIconRight = itemView.left + currentIconMargin + intrinsicWidth
            
            currentIcon?.setBounds(
                currentIconLeft,
                currentIconTop,
                currentIconRight,
                currentIconBottom)
            
            textPositionX = itemView.left + currentIconMargin + intrinsicWidth + textPadding
            
        }
        
        canvas.also {
            background.draw(it)
            currentIcon?.draw(it)
            it.drawText(currentLabel, textPositionX.toFloat(), textPositionY, paint)
        }
        
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        
    }
    
    override fun onSwiped(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        direction: Int,
        horizontalTouchPosition: Float) {
        
        val position =
            if (Math.abs(horizontalTouchPosition) < (viewHolder.itemView.width / 2)) 0 else 1
        
        val dragDirection = if (direction == LEFT) RIGHT else LEFT
        val fallback = if (position == 0) 1 else 0
        val action = ActionHelper.handleAction(
            conversationActions, dragDirection, position, fallback)
        
        swipeListener.onActionPerformed(viewHolder.adapterPosition, action)
        
        // Return the row to it's original position after swipe is complete
        if (returnAfterSwipe)
            recyclerView.adapter?.notifyItemChanged(viewHolder.adapterPosition)
        
    }
    
    private fun clearCanvas(
        canvas: Canvas?,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float) {
        canvas?.drawRect(left, top, right, bottom, clearPaint)
    }
    
}
