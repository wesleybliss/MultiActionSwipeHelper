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
            SwipeAction(
                labelRes = R.string.keep_going,
                actionPosition = 0,
                swipeDirection = SwipePositionItemTouchHelper.RIGHT,
                labelColorRes = R.color.grey,
                backgroundColorRes = android.R.color.holo_green_dark,
                iconRes = R.drawable.ic_check_circle_outline,
                
                activeLabelRes = R.string.huzzah,
                activeLabelColorRes = R.color.white,
                activeBackgroundColorRes = R.color.money,
                activeIconRes = R.drawable.ic_check_circle
            )
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
    
    override fun onActionPerformed(itemPosition: Int, action: ISwipeAction?) {
        
        Log.d("MainActivity", "swiped at $itemPosition - action was $action")
        
    }
    
}
