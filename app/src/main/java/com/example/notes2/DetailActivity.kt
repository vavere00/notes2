package com.example.notes2

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.detail_item.*

class DetailActivity : AppCompatActivity() {

//        private val db get() = (application as App).db
    private val db get() = Database.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_item)

        val id = intent.getLongExtra(MainActivity.EXTRA_ID, 0)
        val item = db.shoppingItemDao().getItemById(id)
        if(item != null) {
            Log.i("det item", item.toString())
            Log.i("det extra", MainActivity.EXTRA_ID.toString())

            detTitle.setText(item.name)
            detDetails.setText(item.details)
        }

        saveButton.setOnClickListener {
            if (id < 1){
                val item = ShoppingItem(detTitle.text.toString(),details = detDetails.text.toString(),"")
                item.uid = db.shoppingItemDao().insertAll(item).first()
            }
            else{
            db.shoppingItemDao().update(
                item.copy(
                    name = detTitle.text.toString(),
                    details = detDetails.text.toString(),
                    urlPhoto = ""
                )
            )
            }
            val intent = Intent().putExtra(EXTRA_ID, item.uid)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}