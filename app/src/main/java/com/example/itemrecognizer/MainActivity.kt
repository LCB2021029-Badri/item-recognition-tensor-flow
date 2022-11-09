package com.example.itemrecognizer

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.itemrecognizer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    //for converting the image the user selects to bitmap
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting the image button to intent
        binding.btnSelect.setOnClickListener {
            var intent:Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent,69)
        }

    }

    //once the user selects the image change the imageView1 to selected image and to store the image (in bitmap format)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //setting the imageview
        binding.imageView1.setImageURI(data?.data)
        //storing the acquired image to bitmap to use it for predection
        var uri : Uri? = data?.data
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
    }

}