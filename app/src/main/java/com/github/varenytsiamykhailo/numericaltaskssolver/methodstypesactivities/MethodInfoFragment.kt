package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.varenytsiamykhailo.numericaltaskssolver.R
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentMethodInfoBinding


class MethodInfoFragment : Fragment() {
    private lateinit var binding: FragmentMethodInfoBinding

    private val args: MethodInfoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMethodInfoBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {
        // Hyperlinks support
        //binding.descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        //binding.descriptionTextView.setLinkTextColor(Color.BLUE)

        val resultString: String = when (args.methodName) {
            "gaussSimpleMethod" -> {
                getString(R.string.gauss_classic_method_description)
            }
            "thomasMethod" -> {
                getString(R.string.thomas_method_description)
            }
            "jacobiMethod" -> {
                getString(R.string.jacobi_method_description)
            }
            "seidelMethod" -> {
                getString(R.string.seidel_method_description)
            }
            "rectangleMethod" -> {
                getString(R.string.rectangle_method_description)
            }
            "trapezoidMethod" -> {
                getString(R.string.trapezoid_method_description)
            }
            "simpsonMethod" -> {
                getString(R.string.simpson_method_description)
            }
            "svennMethod" -> {
                getString(R.string.svenn_method_description)
            }
            "goldenSectionMethod" -> {
                getString(R.string.golden_section_method_description)
            }
            "fibonacciMethod" -> {
                getString(R.string.fibonacci_method_description)
            }
            else -> {
                "Incorrect method name chosen."
            }
        }


        binding.descriptionTextView.text = resultString

        binding.textScaleSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textScaleTextView.text = "Scale: $progress"
                val mult: Float = 1F + progress.toFloat() / 100
                binding.descriptionTextView.setLineSpacing(0F, mult)
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