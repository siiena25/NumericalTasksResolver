package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.systemsolvingmethods

import android.annotation.SuppressLint
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
import com.github.varenytsiamykhailo.knml.util.Matrix
import com.github.varenytsiamykhailo.knml.util.Vector
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentInputSystemDataBinding


class InputSystemDataFragment : Fragment() {
    private lateinit var binding: FragmentInputSystemDataBinding

    private val args: InputSystemDataFragmentArgs by navArgs()

    private lateinit var editTextsOfGridLayoutOfMatrixA: Array<Array<EditText>>

    private lateinit var editTextsOfGridLayoutOfVectorB: Array<EditText>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputSystemDataBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        binding.numOfRowsEditText.hint = "$MIN_NUM_OF_ROWS - $MAX_NUM_OF_ROWS"
        binding.numOfColumnsEditText.hint = "$MIN_NUM_OF_COLS - $MAX_NUM_OF_COLS"

        val methodName: String = args.methodName
        binding.chosenMethodTextView.text = "Chosen method: " + when (methodName) {
            "gaussSimpleMethod" -> {
                "Gauss simple method"
            }
            "thomasMethod" -> {
                "Thomas method"
            }
            "jacobiMethod" -> {
                "Jacobi method"
            }
            "seidelMethod" -> {
                "Seidel method"
            }
            else -> {
                "incorrect method type chosen"
            }
        }

        binding.numOfRowsEditText.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    controlMinMaxValuesOfEditText(
                        view as EditText,
                        MIN_NUM_OF_ROWS,
                        MAX_NUM_OF_ROWS
                    )
                    //buildGridLayoutOfMatrixA(numOfRows, numOfCols)
                    //editText.clearFocus()
                }
            }

        binding.numOfColumnsEditText.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    controlMinMaxValuesOfEditText(
                        view as EditText,
                        MIN_NUM_OF_COLS,
                        MAX_NUM_OF_COLS
                    )
                    //buildGridLayoutOfMatrixA(numOfRows, numOfCols)
                    //view.clearFocus()
                }
            }


        binding.refreshButton.setOnClickListener {
            controlMinMaxValuesOfEditText(
                binding.numOfRowsEditText,
                MIN_NUM_OF_ROWS,
                MAX_NUM_OF_ROWS
            )
            controlMinMaxValuesOfEditText(
                binding.numOfColumnsEditText,
                MIN_NUM_OF_COLS,
                MAX_NUM_OF_COLS
            )

            val numOfRows: Int = Integer.parseInt(binding.numOfRowsEditText.text.toString())
            val numOfCols: Int = Integer.parseInt(binding.numOfColumnsEditText.text.toString())

            buildGridLayoutsOfMatrixAVectorB(numOfRows, numOfCols)
        }

        binding.solveSystemButton.setOnClickListener {

            val n: Int = editTextsOfGridLayoutOfMatrixA.size
            val m: Int = editTextsOfGridLayoutOfMatrixA[0].size

            val A: Array<Array<Double>> = Array(n) { Array(m) { 0.0 } }

            for (i in 0 until n) {
                for (j in 0 until m) {
                    try {
                        A[i][j] = editTextsOfGridLayoutOfMatrixA[i][j].text.toString().toDouble()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(
                            requireActivity(),
                            "Incorrect value at A[$i][$j]. Expected double/float type.",
                            Toast.LENGTH_LONG
                        ).show()

                        return@setOnClickListener
                    }
                }
            }

            val B: Array<Double> = Array(n) { 0.0 }

            for (i in 0 until n) {
                try {
                    B[i] = editTextsOfGridLayoutOfVectorB[i].text.toString().toDouble()
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        requireActivity(),
                        "Incorrect value at B[$i]. Expected double/float type.",
                        Toast.LENGTH_LONG
                    ).show()

                    return@setOnClickListener
                }
            }


            when (args.methodName) {
                "gaussSimpleMethod" -> {
                    val action =
                        InputSystemDataFragmentDirections.actionInputSystemDataFragmentToOtherMethodsSetupFragment(
                            "gaussSimpleMethod", Matrix(A), Vector(B)
                        )
                    findNavController().navigate(action)
                }
                "thomasMethod" -> {
                    val action =
                        InputSystemDataFragmentDirections.actionInputSystemDataFragmentToOtherMethodsSetupFragment(
                            "thomasMethod", Matrix(A), Vector(B)
                        )
                    findNavController().navigate(action)
                }
                "jacobiMethod" -> {
                    val action =
                        InputSystemDataFragmentDirections.actionInputSystemDataFragmentToJacobiSeidelMethodsSetupFragment(
                            "jacobiMethod", Matrix(A), Vector(B)
                        )
                    findNavController().navigate(action)
                }
                "seidelMethod" -> {
                    val action =
                        InputSystemDataFragmentDirections.actionInputSystemDataFragmentToJacobiSeidelMethodsSetupFragment(
                            "seidelMethod", Matrix(A), Vector(B)
                        )
                    findNavController().navigate(action)
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
        val A: Array<Array<Double>>
        val B: Array<Double>
        val methodName: String = args.methodName
        if (methodName == "thomasMethod") {
            A = DEFAULT_MATRIX_A_FOR_THOMAS_METHOD
            B = DEFAULT_VECTOR_B_FOR_THOMAS_METHOD
        } else {
            A = DEFAULT_MATRIX_A
            B = DEFAULT_VECTOR_B
        }

        val numOfRows: Int = A.size
        val numOfCols: Int = A[0].size

        binding.numOfRowsEditText.setText(numOfRows.toString())
        binding.numOfColumnsEditText.setText(numOfCols.toString())

        buildGridLayoutsOfMatrixAVectorB(numOfRows, numOfCols)

        for (i in 0 until numOfRows) {
            for (j in 0 until numOfCols) {
                editTextsOfGridLayoutOfMatrixA[i][j].setText(A[i][j].toString())
            }
        }

        for (i in 0 until numOfRows) {
            editTextsOfGridLayoutOfVectorB[i].setText(B[i].toString())
        }
    }

    private fun controlMinMaxValuesOfEditText(editText: EditText, minValue: Int, maxValue: Int) {
        val str: String = editText.text.toString()
        if (str.isEmpty()) {
            editText.setText(minValue.toString())
            //editText.setSelection(editText.length())//placing cursor at the end of the text
        } else {
            val n = Integer.parseInt(str)
            if (n < minValue) {
                editText.setText(minValue.toString())
            } else if (n > maxValue) {
                editText.setText(maxValue.toString())
            }
        }
    }

    private fun buildGridLayoutsOfMatrixAVectorB(numOfRows: Int, numOfCols: Int) {
        buildGridLayoutOfMatrixA(numOfRows, numOfCols)
        buildGridLayoutOfVectorB(numOfRows)
    }

    private fun buildGridLayoutOfMatrixA(numOfRows: Int, numOfCols: Int) {
        val gridLayoutOfMatrixA = binding.gridLayoutOfMatrixA

        gridLayoutOfMatrixA.removeAllViews()

        gridLayoutOfMatrixA.rowCount = numOfRows
        gridLayoutOfMatrixA.columnCount = numOfCols

        editTextsOfGridLayoutOfMatrixA = Array(numOfRows) {
            Array(numOfCols) {
                EditText(gridLayoutOfMatrixA.context)
            }
        }

        for (i in 0 until numOfRows) {
            for (j in 0 until numOfCols) {
                //editTexts[i][j] = EditText(gridLayoutOfMatrixA.context)
                gridLayoutOfMatrixA.addView(editTextsOfGridLayoutOfMatrixA[i][j])
                setupEditText(editTextsOfGridLayoutOfMatrixA[i][j], i, j)
            }
        }
    }

    private fun buildGridLayoutOfVectorB(numOfRows: Int) {
        val gridLayoutOfVectorB = binding.gridLayoutOfVectorB

        gridLayoutOfVectorB.removeAllViews()

        gridLayoutOfVectorB.rowCount = numOfRows
        gridLayoutOfVectorB.columnCount = 1

        editTextsOfGridLayoutOfVectorB = Array(numOfRows) {
            EditText(gridLayoutOfVectorB.context)
        }

        for (i in 0 until numOfRows) {
            //editTexts[i] = EditText(gridLayoutOfVectorB.context)
            gridLayoutOfVectorB.addView(editTextsOfGridLayoutOfVectorB[i])
            setupEditText(editTextsOfGridLayoutOfVectorB[i], i, 0)
        }
    }

    // Putting the edit text according to row and column index
    private fun setupEditText(editText: EditText, row: Int, column: Int) {
        val param = GridLayout.LayoutParams()
        param.width = 160
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
        private const val MIN_NUM_OF_ROWS: Int = 1
        private const val MAX_NUM_OF_ROWS: Int = 30
        private const val MIN_NUM_OF_COLS: Int = 1
        private const val MAX_NUM_OF_COLS: Int = 30
        private val DEFAULT_MATRIX_A: Array<Array<Double>> = arrayOf(
            arrayOf(115.0, -20.0, -75.0),
            arrayOf(15.0, -50.0, -5.0),
            arrayOf(6.0, 2.0, 20.0)
        )
        private val DEFAULT_VECTOR_B: Array<Double> = arrayOf(20.0, -40.0, 28.0)
        private val DEFAULT_MATRIX_A_FOR_THOMAS_METHOD: Array<Array<Double>> = arrayOf(
            arrayOf(4.0, 1.0, 0.0, 0.0),
            arrayOf(1.0, 4.0, 1.0, 0.0),
            arrayOf(0.0, 1.0, 4.0, 1.0),
            arrayOf(0.0, 0.0, 1.0, 4.0)
        )
        private val DEFAULT_VECTOR_B_FOR_THOMAS_METHOD: Array<Double> = arrayOf(5.0, 6.0, 6.0, 5.0)
    }
}