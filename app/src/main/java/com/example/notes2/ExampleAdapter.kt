package com.example.notes2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import kotlinx.android.synthetic.main.example_item.view.*

class ExampleAdapter(
    private val exampleList: MutableList<ShoppingItem>,
    private val listener: MainActivity
) :

    Adapter<ExampleAdapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item,
        parent, false)

        return ExampleViewHolder(itemView)
    }

    override fun getItemCount() = exampleList.size

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]
        //val context = holder.itemView.context

       // holder.imageView.setImageResource(currentItem.imageResources)
        holder.textView1.text = currentItem.name

        val posNow = exampleList.get(holder.absoluteAdapterPosition)
        Log.i("adapter log", posNow.toString())

        holder.itemView.deleteButton1.setOnClickListener {
            listener.itemRemove(posNow)
        }

    }


    inner class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        //val imageView: ImageView = itemView.image_view
        val textView1: TextView = itemView.text_view_1

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position:Int = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(exampleList[position])
            }
        }
    }

    interface onItemClickListener{
        fun onItemClick(position: ShoppingItem)
        fun itemRemove(position: ShoppingItem)
    }
}