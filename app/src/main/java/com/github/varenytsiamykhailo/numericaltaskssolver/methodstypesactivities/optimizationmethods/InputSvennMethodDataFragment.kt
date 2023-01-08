package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.optimizationmethods

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentAnswerSolutionBinding
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentInputSvennMethodDataBinding
import com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.IntegralMethodsFragmentDirections
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception


class InputSvennMethodDataFragment : Fragment() {
    private lateinit var binding: FragmentInputSvennMethodDataBinding

    private val args: InputSvennMethodDataFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputSvennMethodDataBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        val methodName: String = args.methodName
        binding.chosenMethodTextView.text = "Chosen method: " + when (methodName) {
            "svennMethod" -> {
                "Svenn method"
            }
            else -> {
                "incorrect method type chosen"
            }
        }

        binding.epsEditText.setText(DEFAULT_EPS)

        var useMachineEps: Boolean = false

        binding.useMachineEpsCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                useMachineEps = true
                binding.epsEditText.setText("0")
            } else {
                useMachineEps = false
                binding.epsEditText.setText(DEFAULT_EPS)
            }
        }

        var formFullSolution: Boolean = false
        binding.formFullSolutionCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            formFullSolution = isChecked
        }

        binding.findIntervalButton.setOnClickListener {

            val functionString: String = binding.functionEditText.text.toString()
            val functionExpression: Expression = try {
                ExpressionBuilder(functionString).variables("x").build()
            } catch (e: Exception) {
                Toast.makeText(
                    requireActivity(),
                    "Incorrect function." + e.message,
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            val function: (x: Double) -> Double = { x ->
                try {
                    functionExpression.setVariable("x", x).evaluate()
                } catch (e: Throwable) {
                    if (e.message == "Division by zero!") {
                        Double.POSITIVE_INFINITY
                    } else {
                        Double.NaN
                    }
                }
            }


            val startPoint: Double = try {
                binding.startPointEditText.text.toString().toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    requireActivity(),
                    "Incorrect interval start value. Expected double/float type.",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            // With machine eps methods dont work
            var eps: Double = 0.0001
            try {
                if (!useMachineEps) {
                    eps = binding.epsEditText.text.toString().toDouble()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    requireActivity(),
                    "Incorrect eps value. Expected double/float type.",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }


            /* val methodName: String = this.intent.getStringExtra("methodName").toString()
             when (methodName) {
                 "svennMethod" -> {
                     val result: IntervalResultWithStatus =
                         SvennMethod().findIntervalBySvennMin(
                             startPoint,
                             eps,
                             formFullSolution,
                             function
                         )

                     val resultString: String = formResultString(result)
                     startAnswerSolutionActivity(resultString)
                 }
                 else -> {
                     Toast.makeText(
                         this, "Incorrect method type chosen.",
                         Toast.LENGTH_LONG
                     ).show()
                 }
             }*/
        }

        setupDefaultValuesInGrids()
    }

    private fun setupDefaultValuesInGrids() {
        binding.startPointEditText.setText(DEFAULT_START_POINT)
    }

    /*private fun formResultString(intervalResultWithStatus: IntervalResultWithStatus): String {
        var resultString: String = ""
        resultString += "Is successful: " + intervalResultWithStatus.isSuccessful + "\n"
        if (intervalResultWithStatus.isSuccessful) {
            resultString += "Full solution: \n" + (intervalResultWithStatus.solutionObject?.solutionString
                ?: "not required.") + "\n\n"
            resultString += "Solution result: " + intervalResultWithStatus.intervalResult + "\n"
        } else {
            resultString += intervalResultWithStatus.errorException?.message
                ?: "Exception with null message"
        }
        return resultString
    }*/

    private fun startAnswerSolutionActivity(resultString: String) {
        val action =
            InputSvennMethodDataFragmentDirections.actionInputSvennMethodDataFragmentToAnswerSolutionFragment(
                resultString
            )
        findNavController().navigate(action)
    }

    companion object {
        private val DEFAULT_EPS: String = "0.0001"
        private val DEFAULT_START_POINT: String = "-10.0"
    }
}