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

    private val db get() = Database.getInstance(this)
    private var exampleList = mutableListOf<ShoppingItem>()

    //private val exampleList = generateDummyList(10)


    private val adapter = ExampleAdapter(exampleList, this)

    //private lateinit var adapter: ShoppingItemRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exampleList = generateDummyList(10)


        exampleList.addAll(db.shoppingItemDao().getAll())



        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        buttonAdd.setOnClickListener {
            startActivity(Intent(this, detailAct::class.java))
        }

    }

    fun itemInsert(view: View){
        val index:Int = Random.nextInt(8)

        val nextItem = ShoppingItem(
            "test $index",
            "test details",
            "test url"
        )
        exampleList.add(index, nextItem)
        adapter.notifyItemInserted(index)
    }


    fun notifychange(){
        adapter.notifyDataSetChanged()
    }


    override fun itemRemove(item: ShoppingItem){
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
        //clickedItem.name = "Item clicked"
        adapter.notifyItemChanged(position)
    }

    private fun generateDummyList(size: Int): MutableList<ShoppingItem> {
        val list = mutableListOf<ShoppingItem>()
        for (i in 0 until size) {
            val details: String = when (i % 3) {
                0 -> "details test $i"
                1 -> "details test $i"
                else -> "details test $i"
            }
            val item = ShoppingItem("Item $i","","")
            list += item
        }
        return list
    }



}

