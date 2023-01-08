package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.integralsolvingmethods

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.varenytsiamykhailo.knml.integralmethods.RectangleMethod
import com.github.varenytsiamykhailo.knml.integralmethods.SimpsonMethod
import com.github.varenytsiamykhailo.knml.integralmethods.TrapezoidMethod
import com.github.varenytsiamykhailo.knml.util.results.DoubleResultWithStatus
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentInputIntegralDataBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder


class InputIntegralDataFragment : Fragment() {
    private lateinit var binding: FragmentInputIntegralDataBinding

    private val args: InputIntegralDataFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputIntegralDataBinding.inflate(
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
            "rectangleMethod" -> {
                "rectangle method"
            }
            "trapezoidMethod" -> {
                "trapezoid method"
            }
            "simpsonMethod" -> {
                "simpson method"
            }
            else -> {
                "incorrect method type chosen"
            }
        }

        binding.epsEditText.setText(DEFAULT_EPS)

        var useMachineEps = false

        binding.useMachineEpsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                useMachineEps = true
                binding.epsEditText.setText("0")
            } else {
                useMachineEps = false
                binding.epsEditText.setText(DEFAULT_EPS)
            }
        }

        var formFullSolution = false
        binding.formFullSolutionCheckBox.setOnCheckedChangeListener { _, isChecked ->
            formFullSolution = isChecked
        }

        binding.solveIntegralButton.setOnClickListener {

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


            val intervalStart: Double = try {
                binding.intervalStartEditText.text.toString().toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    requireActivity(),
                    "Incorrect interval start value. Expected double/float type.",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            val intervalEnd: Double = try {
                binding.intervalEndEditText.text.toString().toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    requireActivity(),
                    "Incorrect interval end value. Expected double/float type.",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            // With machine eps methods dont work
            var eps: Double? = 0.0001
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


            when (args.methodName) {
                "rectangleMethod" -> {
                    val result: DoubleResultWithStatus =
                        RectangleMethod().solveIntegralByRectangleMethod(
                            intervalStart,
                            intervalEnd,
                            eps!!,
                            formFullSolution,
                            function
                        )

                    val resultString: String = formResultString(result)
                    startAnswerSolutionActivity(resultString)
                }
                "trapezoidMethod" -> {
                    val result: DoubleResultWithStatus =
                        TrapezoidMethod().solveIntegralByTrapezoidMethod(
                            intervalStart,
                            intervalEnd,
                            eps!!,
                            formFullSolution,
                            function
                        )

                    val resultString: String = formResultString(result)
                    startAnswerSolutionActivity(resultString)
                }
                "simpsonMethod" -> {
                    val result: DoubleResultWithStatus =
                        SimpsonMethod().solveIntegralBySimpsonMethod(
                            intervalStart,
                            intervalEnd,
                            eps!!,
                            formFullSolution,
                            function
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

        setupDefaultValuesInGrids()
    }

    private fun setupDefaultValuesInGrids() {
        binding.intervalStartEditText.setText(DEFAULT_INTERVAL_START)
        binding.intervalEndEditText.setText(DEFAULT_INTERVAL_END)
    }

    private fun formResultString(doubleResultWithStatus: DoubleResultWithStatus): String {
        var resultString = ""
        resultString += "Is successful: " + doubleResultWithStatus.isSuccessful + "\n"
        if (doubleResultWithStatus.isSuccessful) {
            resultString += "Full solution: \n" + (doubleResultWithStatus.solutionObject?.solutionString
                ?: "not required.") + "\n\n"
            resultString += "Solution result: " + doubleResultWithStatus.doubleResult + "\n"
        } else {
            resultString += doubleResultWithStatus.errorException?.message
                ?: "Exception with null message"
        }
        return resultString
    }

    private fun startAnswerSolutionActivity(resultString: String) {
        val action =
            InputIntegralDataFragmentDirections.actionInputIntegralDataFragmentToAnswerSolutionFragment(
                resultString
            )
        findNavController().navigate(action)
    }

    companion object {
        private const val DEFAULT_EPS: String = "0.0001"
        private const val DEFAULT_INTERVAL_START: String = "1.0"
        private const val DEFAULT_INTERVAL_END: String = "5.0"
    }
}