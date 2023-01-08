package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.optimizationmethods

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.varenytsiamykhailo.knml.util.results.DoubleResultWithStatus
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentInputOptimizationDataBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder


class InputOptimizationDataFragment : Fragment() {
    private lateinit var binding: FragmentInputOptimizationDataBinding

    private val args: InputOptimizationDataFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputOptimizationDataBinding.inflate(
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
            "goldenSectionMethod" -> {
                "golden section method"
            }
            "fibonacciMethod" -> {
                "Fibonacci method"
            }
            else -> {
                "incorrect method type chosen"
            }
        }

        binding.epsEditText.setText(DEFAULT_EPS)

        var useMachineEps = false

        binding.useMachineEpsCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
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

        binding.findMinimumButton.setOnClickListener {

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


            /*val methodName: String = this.intent.getStringExtra("methodName").toString()
            when (methodName) {
                "goldenSectionMethod" -> {
                    val result: DoubleResultWithStatus =
                        GoldenSectionMethod().findExtremaByGoldenSectionMethod(
                            intervalStart,
                            intervalEnd,
                            eps,
                            formFullSolution,
                            function
                        )

                    val resultString: String = formResultString(result)
                    startAnswerSolutionActivity(resultString)
                }
                "fibonacciMethod" -> {
                    val result: DoubleResultWithStatus =
                        FibonacciMethod().findExtremaByFibonacciMethod(
                            intervalStart,
                            intervalEnd,
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
                }*/
        }
        //setupDefaultValuesInGrids()
    }

    private fun setupDefaultValuesInGrids() {
        //binding.intervalStartEditText.setText(DEFAULT_INTERVAL_START)
        //binding.intervalEndEditText.setText(DEFAULT_INTERVAL_END)
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

    /*private fun startAnswerSolutionActivity(resultString: String) {
        val intent = Intent(
            this,
            AnswerSolutionActivity::class.java
        )
        intent.putExtra("resultString", resultString)
        startActivity(intent)
    }*/

    companion object {
        private const val DEFAULT_EPS: String = "0.0001"
        private const val DEFAULT_INTERVAL_START: String = "-10.0"
        private const val DEFAULT_INTERVAL_END: String = "7.0"
    }
}