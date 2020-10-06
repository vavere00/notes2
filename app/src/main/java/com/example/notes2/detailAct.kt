package com.example.notes2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.detail_item.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class detailAct  : AppCompatActivity() {
    private val db get() = Database.getInstance(this)
    private var exampleList = mutableListOf<ShoppingItem>()

    private val adapter = ExampleAdapter(exampleList, MainActivity())

    private val items = mutableListOf<ShoppingItem>()

    val REQUEST_IMAGE_CAPTURE = 1

    lateinit var currentPhotoPath: String

    val mainactivity : MainActivity = MainActivity()

    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.i("path1", currentPhotoPath.toString())
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
        Log.i("path2", currentPhotoPath.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_item)

        val imageView: ImageView = findViewById(R.id.imageView)
        val butCamera: FloatingActionButton = findViewById(R.id.floatingActionCamera)

        saveButton.setOnClickListener { appendItem() }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        }

        fun dispatchTakePictureIntent() {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        //...
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.notes2.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }

        butCamera.setOnClickListener{
             val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            Log.i("", "Line 87")
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            Log.i("", "Line 100")

//            fun galleryAddPic() {
//                Log.i("", "Line 106")
//                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
//                    val f = File(currentPhotoPath)
//                    mediaScanIntent.data = Uri.fromFile(f)
//                    sendBroadcast(mediaScanIntent)
//                    Log.i("", "Line 111")
//                }
//            }
        }
    }



    private fun appendItem() {
        val item = ShoppingItem(detTitle.text.toString(), detDetails.text.toString(), "")
        item.uid = db.shoppingItemDao().insertAll(item).first()
        items.add(item)

        items.sortBy { it.name }
        detTitle.text.clear()
        detDetails.text.clear()
        //adapter.notifyDataSetChanged()
        mainactivity.notifychange()
    }

}

//
//val f = File(currentPhotoPath)
//MediaScannerConnection.scanFile(context, arrayOf(file.toString()),arrayOf(file.getName()), null)