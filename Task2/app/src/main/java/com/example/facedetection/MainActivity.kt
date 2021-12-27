package com.example.facedetection

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.facedetection.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding?=null
    private val binding get() = _binding!!
    private val REQUESTIMAGECAPTURE = 124
    private lateinit var image: InputImage
    lateinit var detector: FaceDetector

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        binding.cameraButton.setOnClickListener {
            val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityIfNeeded(intent,REQUESTIMAGECAPTURE)

            }catch (e: ActivityNotFoundException){
                Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUESTIMAGECAPTURE && resultCode == RESULT_OK ){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            detectFace(imageBitmap)
        }
    }
    private fun detectFace(bitmap: Bitmap?) {
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        try {
            InputImage.fromBitmap(bitmap!!,0).also { image = it }
            detector = FaceDetection.getClient(highAccuracyOpts)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        detector.process(image)
            .addOnSuccessListener { firebaseVisionFaces ->
                var resultText: String? = ""
                var i = 1
                for (face in firebaseVisionFaces) {
                    resultText = resultText+"""Detected Faces: $i""".trimIndent() +
                            ("\nSmile: "
                                    + (face?.smilingProbability?.times(100)) + "%") +
                            ("\nleft eye open: "
                                    + (face?.leftEyeOpenProbability?.times(100)) + "%") +
                            ("\nright eye open: "
                                    + (face?.rightEyeOpenProbability?.times(100)) + "%")+"\n"
                    i++
                }
                if (firebaseVisionFaces.size == 0) {
                    Toast
                        .makeText(
                            this@MainActivity,
                            "NO FACE DETECT",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                } else {
                    val bundle = Bundle()
                    bundle.putString(
                        LCOFaceDetection.RESULT_TEXT,
                        resultText
                    )
                    val resultDialog: DialogFragment = ResultDialog()
                    resultDialog.arguments = bundle
                    resultDialog.isCancelable = true
                    resultDialog.show(
                        supportFragmentManager,
                        LCOFaceDetection.RESULT_DIALOG
                    )
                }
            }
            .addOnFailureListener {
                Toast
                    .makeText(
                        this@MainActivity,
                        it.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
    }
}