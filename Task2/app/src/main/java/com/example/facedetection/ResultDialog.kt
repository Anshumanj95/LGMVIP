package com.example.facedetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.facedetection.databinding.FragmentResultdialogBinding

class ResultDialog : DialogFragment() {
    private var _binding: FragmentResultdialogBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentResultdialogBinding.inflate(inflater,container,false)
        val resultText: String

        val bundle = arguments
        resultText = bundle!!.getString(
            LCOFaceDetection.RESULT_TEXT
        ).toString()
        binding.result.text = resultText
        binding.ok.setOnClickListener { dismiss() }
        return binding.root
    }
}