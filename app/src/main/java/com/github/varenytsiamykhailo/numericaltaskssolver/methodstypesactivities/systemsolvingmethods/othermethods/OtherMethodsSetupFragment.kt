package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.systemsolvingmethods.othermethods

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.varenytsiamykhailo.knml.systemsolvingmethods.GaussMethod
import com.github.varenytsiamykhailo.knml.systemsolvingmethods.ThomasMethod
import com.github.varenytsiamykhailo.knml.util.results.VectorResultWithStatus
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentOtherMethodsSetupBinding
import com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.IntegralMethodsFragmentDirections
import com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.systemsolvingmethods.jacobiseidelmethods.JacobiSeidelMethodsSetupFragmentArgs


class OtherMethodsSetupFragment : Fragment() {
    private lateinit var binding: FragmentOtherMethodsSetupBinding

    private val args: OtherMethodsSetupFragmentArgs by navArgs()

    private lateinit var A: Array<Array<Double>>

    private lateinit var B: Array<Double>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        A = args.matrixA.getElems()
        B = args.vectorB.getElems()

        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherMethodsSetupBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {
        var formFullSolution: Boolean = false
        binding.formFullSolutionCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            formFullSolution = isChecked
        }

        binding.solveSystemButton.setOnClickListener {
            when (args.methodName) {
                "gaussSimpleMethod" -> {
                    val result: VectorResultWithStatus =
                        GaussMethod().solveSystemByGaussClassicMethod(
                            A,
                            B,
                            formSolution = formFullSolution
                        )

                    val resultString: String = formResultString(result)
                    startAnswerSolutionActivity(resultString)
                }
                "thomasMethod" -> {
                    val result: VectorResultWithStatus =
                        ThomasMethod().solveSystemByThomasMethod(
                            A,
                            B,
                            formSolution = formFullSolution
                        )

                    val resultString: String = formResultString(result)
                    startAnswerSolutionActivity(resultString)
                }
                else -> {
                    Toast.makeText(
                        requireActivity(),
                        "Incorrect method type chosen.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun formResultString(vectorResultWithStatus: VectorResultWithStatus): String {
        var resultString: String = ""
        resultString += "Is successful: " + vectorResultWithStatus.isSuccessful + "\n"
        if (vectorResultWithStatus.isSuccessful) {
            resultString += "Full solution: \n" + (vectorResultWithStatus.solutionObject?.solutionString
                ?: "not required.") + "\n\n"
            resultString += "Solution result: " + vectorResultWithStatus.vectorResult + "\n"
        } else {
            resultString += vectorResultWithStatus.errorException?.message ?: "Exception with null message"
        }
        return resultString
    }

    private fun startAnswerSolutionActivity(resultString: String, ) {
        val action =
            OtherMethodsSetupFragmentDirections.actionOtherMethodsSetupFragmentToAnswerSolutionFragment(
                resultString
            )
        findNavController().navigate(action)
    }
}