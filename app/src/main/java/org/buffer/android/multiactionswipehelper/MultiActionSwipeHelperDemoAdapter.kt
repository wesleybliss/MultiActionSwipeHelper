package org.buffer.android.multiactionswipehelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MultiActionSwipeHelperDemoAdapter(val items: List<String>)
    : RecyclerView.Adapter<MultiActionSwipeHelperDemoAdapter.VH>() {
    
    class VH(view: View) : RecyclerView.ViewHolder(view) {
        
        fun bind(item: String, @ColorInt rowColor: Int) {
            
            itemView.setBackgroundColor(rowColor)
            itemView.findViewById<TextView>(R.id.textItem).text = item
            
        }
        
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : VH =
        VH(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_item, parent, false))
    
    override fun getItemCount() : Int = items.size
    
    override fun onBindViewHolder(holder: VH, position: Int) {
        
        val rowColorRes =
            if ((position % 2) == 0) android.R.color.darker_gray
            else android.R.color.background_light
        
        val rowColor = ContextCompat
            .getColor(holder.itemView.context, rowColorRes)
        
        holder.bind(items[position], rowColor)
        
    }
    
}
