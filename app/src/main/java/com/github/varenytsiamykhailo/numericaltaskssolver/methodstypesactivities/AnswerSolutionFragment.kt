package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentAnswerSolutionBinding


class AnswerSolutionFragment : Fragment() {
    private lateinit var binding: FragmentAnswerSolutionBinding

    private val args: AnswerSolutionFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnswerSolutionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {
        val resultString: String = args.resultString
        binding.solutionTextView.text = resultString

        binding.textScaleSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textScaleTextView.text = "Scale: $progress"
                val mult: Float = 1F + progress.toFloat() / 100
                binding.solutionTextView.setLineSpacing(0F, mult)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }
        })
    }
}