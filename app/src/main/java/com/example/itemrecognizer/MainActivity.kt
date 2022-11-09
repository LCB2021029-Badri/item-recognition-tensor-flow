package com.example.itemrecognizer

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.example.itemrecognizer.databinding.ActivityMainBinding
import com.example.itemrecognizer.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    //for converting the image the user selects to bitmap
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting the image button to intent
        binding.btnSelect.setOnClickListener(View.OnClickListener {
            var intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 69)
        })

        //coding functionality for predict
        binding.btnPredict.setOnClickListener(View.OnClickListener {
            //resizing the bitmap to the tflite data set image dimensions
            var resized: Bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

            //copy-paste the code set in ".tflite" file here------------------copy pasted-----------
            val model = MobilenetV110224Quant.newInstance(this)
            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            //create byte buffer image form the acquired bitmap-------manual added 1----------------
            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer
            //--------------------------------------------------------manual added 1----------------
            inputFeature0.loadBuffer(byteBuffer)
            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer// use this result
            //setting output image----------------------------manual added 2------------------------
            binding.textView1.setText(outputFeature0.floatArray[96].toString()) // random index for dummy value
            //------------------------------------------------manual added 2------------------------
            // Releases model resources if no longer used.
            model.close()
            //---------------------------------------------------------------copy pasted------------

        })

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