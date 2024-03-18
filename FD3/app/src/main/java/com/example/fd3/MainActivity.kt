package com.example.fd3

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.TokenWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.vision.label.ImageLabel
import com.google.android.gms.vision.label.ImageLabeler
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.firebase.FirebaseApp
import java.io.IOException
import com.google.firebase.FirebaseOptions
import com.google.mlkit.vision.label.ImageLabelerOptionsBase
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val img: ImageView = findViewById(R.id.imageToLabel)
        //파일 확장자를 포함한 이미지 파일명
        val fileName = "cat.jpg"
        //이미지 파일명으로 부터 비트맵을 불러옴
        val bitmap: Bitmap? = assetsToBitmap(fileName)
        bitmap?.apply{
            img.setImageBitmap(this)
        }

        val txtOutput : TextView = findViewById(R.id.txtOutput)
        val btn: Button = findViewById(R.id.btnTest)
        btn.setOnClickListener {
            val labeler =
                ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromBitmap(bitmap!!, 0)
            var outputText = ""
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        outputText += "$text : $confidence\n"
                        //val index = label.index
                    }
                    txtOutput.text = outputText
                }
                .addOnFailureListener { _ ->
                    // Task failed with an exception
                    // ...
                }

        }
    }
}

fun Context.assetsToBitmap(fileName: String): Bitmap?
{
    return try
    {
        with(assets.open(fileName))
        {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {null}
}