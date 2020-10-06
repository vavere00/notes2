package com.example.notes2

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detail_item.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), ExampleAdapter.onItemClickListener {

    private val db get() = Database.getInstance(this)
    private var exampleList = mutableListOf<ShoppingItem>()

    private val adapter = ExampleAdapter(exampleList, this)

    //private lateinit var adapter: ShoppingItemRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //exampleList = generateDummyList(10)
        exampleList.addAll(db.shoppingItemDao().getAll())

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        buttonAdd.setOnClickListener {
            appendItem()
           // exampleList.addAll(db.shoppingItemDao().getAll())
            Log.i("click",exampleList.toString())
//            startActivity(Intent(this, DetailActivity::class.java))
            val intent = Intent(this, DetailActivity::class.java)
                .putExtra(EXTRA_ID, "")

            startActivityForResult(intent, REQUEST_CODE_DETAILS)
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
        Log.i("delete", item.toString())
        db.shoppingItemDao().delete(item)
        //Log.i("delete", dbdelete.toString())
        exampleList.clear()
        exampleList.addAll(db.shoppingItemDao().getAll())
        //val index: Int = item.toString().toInt()
//        exampleList.removeAt(item.uid.toInt())
//        adapter.notifyItemRemoved(item.uid.toInt())
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(item: ShoppingItem) {
        Toast.makeText(this, "Item ${item.uid} clicked", Toast.LENGTH_SHORT).show()
//        val clickedItem = exampleList[position]
//        clickedItem.name = "Item clicked"
        val intent = Intent(this, DetailActivity::class.java)
            .putExtra(EXTRA_ID, item.uid)

        startActivityForResult(intent, REQUEST_CODE_DETAILS)
//        adapter.notifyItemChanged(position)
//        exampleList.clear()
//        exampleList.addAll(db.shoppingItemDao().getAll())
//        adapter.notifyDataSetChanged()
    }

    companion object {

        const val EXTRA_ID = "com.example.notes2.extras.shopping_item_id"
        const val REQUEST_CODE_DETAILS = 1234
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK && data != null) {
        exampleList.clear()
        exampleList.addAll(db.shoppingItemDao().getAll())
        adapter.notifyDataSetChanged()
        }
    }

//     override fun itemClicked(item: ShoppingItem) {
//        val intent = Intent(this, DetailActivity::class.java)
//            .putExtra(EXTRA_ID, item.uid)
//        Log.i("delete item", item.toString())
//        Log.i("delete extra", EXTRA_ID.toString())
////        startActivityForResult(intent, REQUEST_CODE_DETAILS)
//    }




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


    private fun appendItem() {
        val item = ShoppingItem("Item test","test","")
        item.uid = db.shoppingItemDao().insertAll(item).first()
        exampleList.add(item)

        exampleList.sortBy { it.name }
        adapter.notifyDataSetChanged()
    }



}

