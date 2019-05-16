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
                R.string.huzzah,
                50,
                SwipePositionItemTouchHelper.RIGHT,
                R.color.money,
                android.R.color.white,
                R.drawable.ic_launcher_foreground)
        )
        
        val swipeHandler = SwipeToPerformActionCallback(
            this, 
            20, 
            swipeActions,
            SwipePositionItemTouchHelper.RIGHT)
        
        SwipePositionItemTouchHelper(swipeHandler).attachToRecyclerView(listItems)
        
    }
    
    override fun onActionPerformed(itemPosition: Int, action: ISwipeAction?) {
        
        Log.d("MainActivity", "swiped at $itemPosition - action was $action")
        
    }
    
}
