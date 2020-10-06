package com.example.notes2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), ExampleAdapter.onItemClickListener {
    private val exampleList = generateDummyList(10)
    private val adapter = ExampleAdapter(exampleList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        //recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.setHasFixedSize(true)

        buttonAdd.setOnClickListener {
            startActivity(Intent(this, detailAct::class.java))
        }

    }

    fun itemInsert(view: View){
        val index:Int = Random.nextInt(8)

        val nextItem = ExampleItem(
            R.drawable.ic_baseline_ship,
            "New itme, pos $index"
        )
        exampleList.add(index, nextItem)
        adapter.notifyItemInserted(index)
    }


    override fun itemRemove(item: ExampleItem){
        val index: Int = item.toString().toInt()
        Log.i("", index.toString())
        Log.i("",exampleList.toString())
        //exampleList.removeAt(index)
        //adapter.notifyItemRemoved(index)
        //
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = exampleList[position]
        clickedItem.text1 = "Item clicked"
        adapter.notifyItemChanged(position)
    }

    private fun generateDummyList(size: Int): ArrayList<ExampleItem> {
        val list = ArrayList<ExampleItem>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.ic_android_1
                1 -> R.drawable.ic_baseline_bike
                else -> R.drawable.ic_baseline_ship
            }
            val item = ExampleItem(drawable, "Item $i")
            list += item
        }
        return list
    }




}

