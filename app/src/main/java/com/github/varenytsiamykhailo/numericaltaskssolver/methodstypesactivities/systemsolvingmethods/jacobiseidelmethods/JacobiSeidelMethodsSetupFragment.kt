package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.systemsolvingmethods.jacobiseidelmethods

import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.varenytsiamykhailo.knml.systemsolvingmethods.JacobiMethod
import com.github.varenytsiamykhailo.knml.systemsolvingmethods.SeidelMethod
import com.github.varenytsiamykhailo.knml.util.results.VectorResultWithStatus
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentJacobiSeidelMethodsSetupBinding


class JacobiSeidelMethodsSetupFragment : Fragment() {
    private lateinit var binding: FragmentJacobiSeidelMethodsSetupBinding

    private val args: JacobiSeidelMethodsSetupFragmentArgs by navArgs()

    private lateinit var A: Array<Array<Double>>

    private lateinit var B: Array<Double>

    private lateinit var defaultVectorOfInitialApproximation: Array<Double>

    private lateinit var editTextsOfGridLayoutOfVectorInitialApproximation: Array<EditText>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        A = args.matrixA.getElems()
        B = args.vectorB.getElems()
        defaultVectorOfInitialApproximation = Array(A.size) { 1.0 }

        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJacobiSeidelMethodsSetupBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {

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
        binding.formFullSolutionCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            formFullSolution = isChecked
        }

        binding.solveSystemButton.setOnClickListener {
            val n: Int = A.size
            val vectorOfInitialApproximation: Array<Double> = Array(n) { 0.0 }

            for (i in 0 until n) {
                try {
                    vectorOfInitialApproximation[i] =
                        editTextsOfGridLayoutOfVectorInitialApproximation[i].text.toString()
                            .toDouble()
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        requireActivity(),
                        "Incorrect value at I[$i]. Expected double/float type.",
                        Toast.LENGTH_LONG
                    ).show()

                    return@setOnClickListener
                }
            }

            var eps: Double? = null
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
                "jacobiMethod" -> {
                    val result: VectorResultWithStatus =
                        JacobiMethod().solveSystemByJacobiMethod(
                            A,
                            B,
                            initialApproximation = vectorOfInitialApproximation,
                            eps = eps,
                            formSolution = formFullSolution
                        )

                    val resultString: String = formResultString(result)
                    startAnswerSolutionActivity(resultString)
                }
                "seidelMethod" -> {
                    val result: VectorResultWithStatus =
                        SeidelMethod().solveSystemBySeidelMethod(
                            A,
                            B,
                            initialApproximation = vectorOfInitialApproximation,
                            eps = eps,
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

        setupDefaultValuesInGrids()
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

    private fun startAnswerSolutionActivity(resultString: String) {
        val action =
            JacobiSeidelMethodsSetupFragmentDirections.actionJacobiSeidelMethodsSetupFragmentToAnswerSolutionFragment(
                resultString
            )
        findNavController().navigate(action)
    }

    private fun setupDefaultValuesInGrids() {
        val numOfRows: Int = defaultVectorOfInitialApproximation.size

        buildGridLayoutOfVectorB(numOfRows)

        for (i in 0 until numOfRows) {
            editTextsOfGridLayoutOfVectorInitialApproximation[i].setText(
                defaultVectorOfInitialApproximation[i].toString()
            )
        }
    }

    private fun buildGridLayoutOfVectorB(numOfRows: Int) {
        val gridLayoutOfVectorInitialApproximation = binding.gridLayoutOfVectorInitialApproximation

        gridLayoutOfVectorInitialApproximation.removeAllViews()

        gridLayoutOfVectorInitialApproximation.rowCount = numOfRows
        gridLayoutOfVectorInitialApproximation.columnCount = 1

        editTextsOfGridLayoutOfVectorInitialApproximation = Array(numOfRows) {
            EditText(gridLayoutOfVectorInitialApproximation.context)
        }

        for (i in 0 until numOfRows) {
            //editTexts[i] = EditText(gridLayoutOfVectorB.context)
            gridLayoutOfVectorInitialApproximation.addView(
                editTextsOfGridLayoutOfVectorInitialApproximation[i]
            )
            setupEditText(editTextsOfGridLayoutOfVectorInitialApproximation[i], i, 0)
        }
    }

    // Putting the edit text according to row and column index
    private fun setupEditText(editText: EditText, row: Int, column: Int) {
        val param = GridLayout.LayoutParams()
        param.width = 200
        param.height = 120
        param.setGravity(Gravity.CENTER)
        param.rowSpec = GridLayout.spec(row)
        param.columnSpec = GridLayout.spec(column)
        editText.layoutParams = param
        //editText.setText("${row}")
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        editText.inputType =
            (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)
    }

    companion object {
        private const val DEFAULT_EPS: String = "0.0001"
    }
}