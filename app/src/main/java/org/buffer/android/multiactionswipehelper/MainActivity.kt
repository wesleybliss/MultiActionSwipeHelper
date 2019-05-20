package org.buffer.android.multiactionswipehelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), SwipeActionListener {
    
    private val listItems by lazy { findViewById<RecyclerView>(R.id.listItems) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val fakeItems = mutableListOf<String>()
        
        repeat(20) { fakeItems.add("Item #${it + 1}") }
        
        listItems.apply { 
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = MultiActionSwipeHelperDemoAdapter(fakeItems)
        }

        val swipeActions = listOf(
            object : SwipeAction {
                override val actionPosition = 0
                override val swipeDirection = SwipePositionItemTouchHelper.RIGHT
                override fun getLabelRes(position: Int, isUnderThreshold: Boolean) : Int =
                    if (isUnderThreshold) R.string.keep_going
                    else R.string.huzzah
                override fun getLabelColorRes(position: Int, isUnderThreshold: Boolean) : Int =
                    if (isUnderThreshold) R.color.grey
                    else R.color.white
                override fun getBackgroundColorRes(position: Int, isUnderThreshold: Boolean) : Int =
                    if (isUnderThreshold) android.R.color.holo_green_dark
                    else R.color.money
                override fun getIconRes(position: Int, isUnderThreshold: Boolean) : Int =
                    if (isUnderThreshold) R.drawable.ic_check_circle_outline
                    else R.drawable.ic_check_circle
            }
        )
        
        val swipeHandler = SwipeToPerformActionCallback(
            swipeListener = this,
            textPadding = 20,
            conversationActions = swipeActions,
            allowedDirections = SwipePositionItemTouchHelper.RIGHT,
            returnAfterSwipe = true,
            isUnderFlingThreshold = ::isUnderFlingThreshold)
        
        SwipePositionItemTouchHelper(swipeHandler).attachToRecyclerView(listItems)
        
    }
    
    private fun isUnderFlingThreshold(dX: Float, parentWidth: Int) =
        Math.abs(dX) < (parentWidth / 3)
    
    override fun onActionPerformed(itemPosition: Int, action: SwipeAction?) {
        
        Log.d("MainActivity", "swiped at $itemPosition - action was $action")
        
    }
    
}
