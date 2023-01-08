package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities

import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.varenytsiamykhailo.knml.util.LUDecomposition
import com.github.varenytsiamykhailo.knml.util.Matrix
import com.github.varenytsiamykhailo.knml.util.QRDecomposition
import com.github.varenytsiamykhailo.knml.util.Vector
import com.github.varenytsiamykhailo.knml.util.getPretty1DDoubleArrayString
import com.github.varenytsiamykhailo.knml.util.getPretty2DDoubleArrayString
import com.github.varenytsiamykhailo.numericaltaskssolver.R
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentMatrixCalculatorBinding


class MatrixCalculatorFragment : Fragment() {
    private lateinit var binding: FragmentMatrixCalculatorBinding

    private var editTextsOfGridLayoutOfFirstMatrix: Array<Array<EditText>> = arrayOf()

    private var editTextsOfGridLayoutOfSecondVector: Array<EditText> = arrayOf()
    private var editTextsOfGridLayoutOfSecondMatrix: Array<Array<EditText>> = arrayOf()

    private var textViewsOfGridLayoutOfAnswerMatrix: Array<Array<TextView>> = arrayOf()
    private var textViewsOfGridLayoutOfAnswerVector: Array<TextView> = arrayOf()

    private var textViewsOfGridLayoutOfAnswerFirstMatrix: Array<Array<TextView>> = arrayOf()
    private var textViewsOfGridLayoutOfAnswerSecondMatrix: Array<Array<TextView>> = arrayOf()

    private var currentSelectedOperation = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatrixCalculatorBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {
        binding.numOfRowsEditText.hint = "$MIN_NUM_OF_ROWS - $MAX_NUM_OF_ROWS"
        binding.numOfColumnsEditText.hint = "$MIN_NUM_OF_COLS - $MAX_NUM_OF_COLS"
        binding.numOfRowsEditTextSecond.hint = "$MIN_NUM_OF_ROWS - $MAX_NUM_OF_ROWS"
        binding.numOfColumnsEditTextSecond.hint = "$MIN_NUM_OF_COLS - $MAX_NUM_OF_COLS"

        setupRefreshButtonFirstElem()
        setupRefreshButtonSecondElem()

        val numOfRowsEditTextChangeListener = View.OnFocusChangeListener { view, hasFocus ->
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
        val numOfColsEditTextChangeListener = View.OnFocusChangeListener { view, hasFocus ->
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

        binding.numOfRowsEditText.onFocusChangeListener = numOfRowsEditTextChangeListener
        binding.numOfRowsEditTextSecond.onFocusChangeListener = numOfRowsEditTextChangeListener
        binding.numOfColumnsEditText.onFocusChangeListener = numOfColsEditTextChangeListener
        binding.numOfColumnsEditTextSecond.onFocusChangeListener = numOfColsEditTextChangeListener

        setupDefaultValuesInGrids()

        binding.nextButton.setOnClickListener {
            binding.answerMatrixLl.isVisible = true

            val n: Int = editTextsOfGridLayoutOfFirstMatrix.size
            val m: Int = editTextsOfGridLayoutOfFirstMatrix[0].size

            val firstMatrix: Array<Array<Double>> = Array(n) { Array(m) { 0.0 } }
            val secondMatrix: Array<Array<Double>> = Array(n) { Array(m) { 0.0 } }
            val secondVector: Array<Double> = Array(n) { 0.0 }
            var secondNumber = 1.0

            for (i in 0 until n) {
                for (j in 0 until m) {
                    try {
                        firstMatrix[i][j] =
                            editTextsOfGridLayoutOfFirstMatrix[i][j].text.toString().toDouble()
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

            if (binding.secondElemMatrix.isVisible) {
                for (i in 0 until n) {
                    for (j in 0 until m) {
                        try {
                            secondMatrix[i][j] =
                                editTextsOfGridLayoutOfSecondMatrix[i][j].text.toString().toDouble()
                        } catch (e: NumberFormatException) {
                            Toast.makeText(
                                requireActivity(),
                                "Incorrect value at B[$i][$j]. Expected double/float type.",
                                Toast.LENGTH_LONG
                            ).show()

                            return@setOnClickListener
                        }
                    }
                }
            } else if (binding.secondElemVector.isVisible) {
                for (i in 0 until n) {
                    try {
                        secondVector[i] =
                            editTextsOfGridLayoutOfSecondVector[i].text.toString().toDouble()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(
                            requireActivity(),
                            "Incorrect value at B[$i]. Expected double/float type.",
                            Toast.LENGTH_LONG
                        ).show()

                        return@setOnClickListener
                    }
                }
            } else if (binding.secondElemNumber.isVisible) {
                secondNumber = binding.tvSecondNumber.text.toString().toDouble()
            }


            when (currentSelectedOperation) {
                MATRIX_OP_ADD -> {
                    val answerMatrix = Matrix(firstMatrix).add(Matrix(secondMatrix))
                    buildGridLayoutOfAnswerMatrix(answerMatrix.getElems())
                }
                MATRIX_OP_SUB -> {
                    val answerMatrix = Matrix(firstMatrix).sub(Matrix(secondMatrix))
                    buildGridLayoutOfAnswerMatrix(answerMatrix.getElems())
                }
                MATRIX_OP_MUL_NUMBER -> {
                    val answerMatrix = Matrix(firstMatrix).multiply(secondNumber)
                    buildGridLayoutOfAnswerMatrix(answerMatrix.getElems())
                }
                MATRIX_OP_MUL_VECTOR -> {
                    val answerVector = Matrix(firstMatrix).multiply(Vector(secondVector))
                    buildGridLayoutOfAnswerVector(answerVector.getElems())
                }
                MATRIX_OP_MUL_MATRIX -> {
                    val answerMatrix = Matrix(firstMatrix).multiply(Matrix(secondMatrix))
                    buildGridLayoutOfAnswerMatrix(answerMatrix.getElems())
                }
                MATRIX_OP_NORM -> {
                    val answerNumber = Matrix(firstMatrix).norm()
                    buildAnswerNumber(answerNumber)
                }
                MATRIX_OP_TRANSPOSE -> {
                    val answerMatrix = Matrix(firstMatrix).transpose()
                    buildGridLayoutOfAnswerMatrix(answerMatrix.getElems())
                }
                MATRIX_OP_ADJOINT -> {
                    val answerMatrix = Matrix(firstMatrix).adjoint()
                    buildGridLayoutOfAnswerMatrix(answerMatrix.getElems())
                }
                MATRIX_OP_INVERTIBLE -> {
                    val answerMatrix = Matrix(firstMatrix).invertible()
                    buildGridLayoutOfAnswerMatrix(answerMatrix!!.getElems())
                }
                MATRIX_OP_LU_DECOMPOSITION -> {
                    val luDecomposition = LUDecomposition(Matrix(firstMatrix))
                    buildGridLayoutOfAnswerLUDecomposition(luDecomposition)
                }
                MATRIX_OP_QR_DECOMPOSITION -> {
                    val qrDecomposition = QRDecomposition(Matrix(firstMatrix))
                    buildGridLayoutOfAnswerQrDecomposition(qrDecomposition)
                }
                MATRIX_OP_DETERMINANT -> {
                    val answerNumber = Matrix(firstMatrix).determinantWithGauss()
                    buildAnswerNumber(answerNumber)
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

        binding.apply {
            selectOperationSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        itemSelected: View, selectedItemPosition: Int, selectedId: Long
                    ) {
                        binding.hint.isVisible = false
                        binding.firstMatrixElem.isVisible = true
                        binding.nextButton.isVisible = true

                        currentSelectedOperation = selectedItemPosition

                        when (selectedItemPosition) {
                            MATRIX_OP_ADD,
                            MATRIX_OP_SUB,
                            MATRIX_OP_MUL_MATRIX -> {
                                secondElemLl.isVisible = true
                                secondElemMatrix.isVisible = true
                                secondElemVector.isVisible = false
                                secondElemNumber.isVisible = false
                            }
                            MATRIX_OP_MUL_NUMBER -> {
                                secondElemLl.isVisible = true
                                secondElemMatrix.isVisible = false
                                secondElemVector.isVisible = false
                                secondElemNumber.isVisible = true
                            }
                            MATRIX_OP_MUL_VECTOR -> {
                                secondElemLl.isVisible = true
                                secondElemMatrix.isVisible = false
                                secondElemVector.isVisible = true
                                secondElemNumber.isVisible = false
                            }
                            else -> {
                                secondElemLl.isVisible = false
                                secondElemMatrix.isVisible = false
                                secondElemVector.isVisible = false
                                secondElemNumber.isVisible = false
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        binding.apply {
                            firstMatrixElem.isVisible = false
                            binding.secondElemLl.isVisible = false
                            nextButton.isVisible = false
                            binding.hint.isVisible = true
                        }
                    }
                }
        }
    }

    private fun setupRefreshButtonFirstElem() {
        val refreshButtonClickListener = View.OnClickListener {
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

            buildGridLayoutsOfFirstElem(numOfRows, numOfCols)
        }
        binding.refreshButtonFirst.setOnClickListener(refreshButtonClickListener)
    }

    private fun setupRefreshButtonSecondElem() {
        val refreshButtonClickListener = View.OnClickListener {
            controlMinMaxValuesOfEditText(
                binding.numOfRowsEditTextSecond,
                MIN_NUM_OF_ROWS,
                MAX_NUM_OF_ROWS
            )
            controlMinMaxValuesOfEditText(
                binding.numOfColumnsEditTextSecond,
                MIN_NUM_OF_COLS,
                MAX_NUM_OF_COLS
            )

            val numOfRows: Int = Integer.parseInt(binding.numOfRowsEditTextSecond.text.toString())
            val numOfCols: Int =
                Integer.parseInt(binding.numOfColumnsEditTextSecond.text.toString())

            buildGridLayoutsOfSecondElem(numOfRows, numOfCols)
        }
        binding.apply {
            refreshButtonSecond.setOnClickListener(refreshButtonClickListener)
            refreshButtonSecondVector.setOnClickListener(refreshButtonClickListener)
        }
    }

    private fun setupDefaultValuesInGrids() {
        val A: Array<Array<Double>> = DEFAULT_MATRIX_A
        val B: Array<Double> = DEFAULT_VECTOR_B

        val numOfRows: Int = A.size
        val numOfCols: Int = A[0].size

        binding.numOfRowsEditText.setText(numOfRows.toString())
        binding.numOfColumnsEditText.setText(numOfCols.toString())

        binding.numOfRowsEditTextSecond.setText(numOfRows.toString())
        binding.numOfColumnsEditTextSecond.setText(numOfCols.toString())

        binding.numOfElemsEditTextSecond.setText(numOfRows.toString())

        buildGridLayoutsOfFirstElem(numOfRows, numOfCols)
        buildGridLayoutsOfSecondElem(numOfRows, numOfCols)

        for (i in 0 until numOfRows) {
            for (j in 0 until numOfCols) {
                editTextsOfGridLayoutOfFirstMatrix[i][j].setText(A[i][j].toString())
                editTextsOfGridLayoutOfSecondMatrix[i][j].setText(A[i][j].toString())
            }
        }

        for (i in 0 until numOfRows) {
            editTextsOfGridLayoutOfSecondVector[i].setText(B[i].toString())
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

    private fun buildGridLayoutsOfFirstElem(numOfRows: Int, numOfCols: Int) {
        buildGridLayoutOfFirstMatrix(numOfRows, numOfCols)
    }

    private fun buildGridLayoutsOfSecondElem(numOfRows: Int, numOfCols: Int) {
        buildGridLayoutOfSecondVector(numOfRows)
        buildGridLayoutOfSecondMatrix(numOfRows, numOfCols)
    }

    private fun buildGridLayoutOfFirstMatrix(numOfRows: Int, numOfCols: Int) {
        val gridLayoutOfFirstMatrix = binding.gridLayoutOfFirstMatrix

        gridLayoutOfFirstMatrix.removeAllViews()

        gridLayoutOfFirstMatrix.rowCount = numOfRows
        gridLayoutOfFirstMatrix.columnCount = numOfCols

        editTextsOfGridLayoutOfFirstMatrix = Array(numOfRows) {
            Array(numOfCols) {
                EditText(gridLayoutOfFirstMatrix.context)
            }
        }

        for (i in 0 until numOfRows) {
            for (j in 0 until numOfCols) {
                //editTexts[i][j] = EditText(gridLayoutOfMatrixA.context)
                gridLayoutOfFirstMatrix.addView(editTextsOfGridLayoutOfFirstMatrix[i][j])
                setupEditText(editTextsOfGridLayoutOfFirstMatrix[i][j], i, j)
            }
        }
    }

    private fun buildGridLayoutOfSecondMatrix(numOfRows: Int, numOfCols: Int) {
        val gridLayoutOfSecondMatrix = binding.gridLayoutOfSecondMatrix

        gridLayoutOfSecondMatrix.removeAllViews()

        gridLayoutOfSecondMatrix.rowCount = numOfRows
        gridLayoutOfSecondMatrix.columnCount = numOfCols

        println("Setup start")
        editTextsOfGridLayoutOfSecondMatrix = Array(numOfRows) {
            Array(numOfCols) {
                EditText(gridLayoutOfSecondMatrix.context)
            }
        }
        println("Setup: $editTextsOfGridLayoutOfSecondMatrix")

        for (i in 0 until numOfRows) {
            for (j in 0 until numOfCols) {
                gridLayoutOfSecondMatrix.addView(editTextsOfGridLayoutOfSecondMatrix[i][j])
                setupEditText(editTextsOfGridLayoutOfSecondMatrix[i][j], i, j)
            }
        }
    }

    private fun buildGridLayoutOfAnswerMatrix(elems: Array<Array<Double>>) {
        val numOfRows = elems.size
        val numOfCols = elems[0].size

        val gridLayoutOfAnswerMatrix = binding.gridLayoutOfAnswerMatrix

        gridLayoutOfAnswerMatrix.removeAllViews()

        gridLayoutOfAnswerMatrix.rowCount = numOfRows
        gridLayoutOfAnswerMatrix.columnCount = numOfCols

        textViewsOfGridLayoutOfAnswerMatrix = Array(numOfRows) {
            Array(numOfCols) {
                TextView(gridLayoutOfAnswerMatrix.context)
            }
        }

        for (i in 0 until numOfRows) {
            for (j in 0 until numOfCols) {
                gridLayoutOfAnswerMatrix.addView(textViewsOfGridLayoutOfAnswerMatrix[i][j])
                setupTextView(textViewsOfGridLayoutOfAnswerMatrix[i][j], i, j)

                textViewsOfGridLayoutOfAnswerMatrix[i][j].text = cropNumber(elems[i][j].toString())
            }
        }

        binding.answerMatrixLl.isVisible = true
        binding.answerVectorLl.isVisible = false
        binding.answerNumberLl.isVisible = false
        binding.answerLuDecLl.isVisible = false
        binding.answerQrDecLl.isVisible = false
    }

    private fun buildGridLayoutOfAnswerVector(elems: Array<Double>) {
        val numOfRows = elems.size

        val gridLayoutOfAnswerVector = binding.gridLayoutOfAnswerVector

        gridLayoutOfAnswerVector.removeAllViews()

        gridLayoutOfAnswerVector.rowCount = numOfRows
        gridLayoutOfAnswerVector.columnCount = 1

        textViewsOfGridLayoutOfAnswerVector = Array(numOfRows) {
            TextView(gridLayoutOfAnswerVector.context)
        }

        for (i in 0 until numOfRows) {
            gridLayoutOfAnswerVector.addView(textViewsOfGridLayoutOfAnswerVector[i])
            setupTextView(textViewsOfGridLayoutOfAnswerVector[i], i, 0)

            textViewsOfGridLayoutOfAnswerVector[i].text = cropNumber(elems[i].toString())
        }

        binding.answerMatrixLl.isVisible = false
        binding.answerVectorLl.isVisible = true
        binding.answerNumberLl.isVisible = false
        binding.answerLuDecLl.isVisible = false
        binding.answerQrDecLl.isVisible = false
    }

    private fun buildAnswerNumber(elem: Double) {
        binding.answerNumberTv.text = elem.toString()

        binding.answerMatrixLl.isVisible = false
        binding.answerVectorLl.isVisible = false
        binding.answerNumberLl.isVisible = true
        binding.answerLuDecLl.isVisible = false
        binding.answerQrDecLl.isVisible = false
    }

    private fun buildGridLayoutOfAnswerLUDecomposition(luDecomposition: LUDecomposition) {
        val numOfRowsL = luDecomposition.lowerMatrix.getElems().size
        val numOfColsL = luDecomposition.lowerMatrix.getElems()[0].size

        val gridLayoutOfAnswerLowerMatrix = binding.gridLayoutOfAnswerLowerMatrix

        gridLayoutOfAnswerLowerMatrix.removeAllViews()

        gridLayoutOfAnswerLowerMatrix.rowCount = numOfRowsL
        gridLayoutOfAnswerLowerMatrix.columnCount = numOfColsL

        textViewsOfGridLayoutOfAnswerFirstMatrix = Array(numOfRowsL) {
            Array(numOfColsL) {
                TextView(gridLayoutOfAnswerLowerMatrix.context)
            }
        }

        for (i in 0 until numOfRowsL) {
            for (j in 0 until numOfColsL) {
                gridLayoutOfAnswerLowerMatrix.addView(textViewsOfGridLayoutOfAnswerFirstMatrix[i][j])
                setupTextView(textViewsOfGridLayoutOfAnswerFirstMatrix[i][j], i, j)

                textViewsOfGridLayoutOfAnswerFirstMatrix[i][j].text = cropNumber(luDecomposition.lowerMatrix.getElems()[i][j].toString())
            }
        }

        val numOfRowsU = luDecomposition.upperMatrix.getElems().size
        val numOfColsU = luDecomposition.upperMatrix.getElems()[0].size

        val gridLayoutOfAnswerUpperMatrix = binding.gridLayoutOfAnswerUpperMatrix

        gridLayoutOfAnswerUpperMatrix.removeAllViews()

        gridLayoutOfAnswerUpperMatrix.rowCount = numOfRowsU
        gridLayoutOfAnswerUpperMatrix.columnCount = numOfColsU

        textViewsOfGridLayoutOfAnswerSecondMatrix = Array(numOfRowsU) {
            Array(numOfColsU) {
                TextView(gridLayoutOfAnswerUpperMatrix.context)
            }
        }

        for (i in 0 until numOfRowsU) {
            for (j in 0 until numOfColsU) {
                gridLayoutOfAnswerUpperMatrix.addView(textViewsOfGridLayoutOfAnswerSecondMatrix[i][j])
                setupTextView(textViewsOfGridLayoutOfAnswerSecondMatrix[i][j], i, j)

                textViewsOfGridLayoutOfAnswerSecondMatrix[i][j].text = cropNumber(luDecomposition.upperMatrix.getElems()[i][j].toString())
            }
        }

        binding.answerMatrixLl.isVisible = false
        binding.answerVectorLl.isVisible = false
        binding.answerNumberLl.isVisible = false
        binding.answerLuDecLl.isVisible = true
        binding.answerQrDecLl.isVisible = false
    }

    private fun cropNumber(s: String): CharSequence {
        return if (s.contains(".") && s.length > 6) {
            s.substring(0, 6)
        } else {
            s
        }
    }

    private fun buildGridLayoutOfAnswerQrDecomposition(qrDecomposition: QRDecomposition) {
        val numOfRowsQ = qrDecomposition.Q.getElems().size
        val numOfColsQ = qrDecomposition.R.getElems()[0].size

        val gridLayoutOfAnswerMatrixQ = binding.gridLayoutOfAnswerMatrixQ

        gridLayoutOfAnswerMatrixQ.removeAllViews()

        gridLayoutOfAnswerMatrixQ.rowCount = numOfRowsQ
        gridLayoutOfAnswerMatrixQ.columnCount = numOfColsQ

        textViewsOfGridLayoutOfAnswerFirstMatrix = Array(numOfRowsQ) {
            Array(numOfColsQ) {
                TextView(gridLayoutOfAnswerMatrixQ.context)
            }
        }

        for (i in 0 until numOfRowsQ) {
            for (j in 0 until numOfColsQ) {
                gridLayoutOfAnswerMatrixQ.addView(textViewsOfGridLayoutOfAnswerFirstMatrix[i][j])
                setupTextView(textViewsOfGridLayoutOfAnswerFirstMatrix[i][j], i, j)

                textViewsOfGridLayoutOfAnswerFirstMatrix[i][j].text = cropNumber(qrDecomposition.Q.getElems()[i][j].toString())
            }
        }

        val numOfRowsR = qrDecomposition.R.getElems().size
        val numOfColsR = qrDecomposition.R.getElems()[0].size

        val gridLayoutOfAnswerMatrixR = binding.gridLayoutOfAnswerMatrixR

        gridLayoutOfAnswerMatrixR.removeAllViews()

        gridLayoutOfAnswerMatrixR.rowCount = numOfRowsR
        gridLayoutOfAnswerMatrixR.columnCount = numOfColsR

        textViewsOfGridLayoutOfAnswerSecondMatrix = Array(numOfRowsR) {
            Array(numOfColsR) {
                TextView(gridLayoutOfAnswerMatrixR.context)
            }
        }

        for (i in 0 until numOfRowsR) {
            for (j in 0 until numOfColsR) {
                gridLayoutOfAnswerMatrixR.addView(textViewsOfGridLayoutOfAnswerSecondMatrix[i][j])
                setupTextView(textViewsOfGridLayoutOfAnswerSecondMatrix[i][j], i, j)

                textViewsOfGridLayoutOfAnswerSecondMatrix[i][j].text = cropNumber(qrDecomposition.R.getElems()[i][j].toString())
            }
        }

        binding.answerMatrixLl.isVisible = false
        binding.answerVectorLl.isVisible = false
        binding.answerNumberLl.isVisible = false
        binding.answerLuDecLl.isVisible = false
        binding.answerQrDecLl.isVisible = true
    }

    private fun buildGridLayoutOfSecondVector(numOfRows: Int) {
        val gridLayoutOfSecondVector = binding.gridLayoutOfSecondVector

        gridLayoutOfSecondVector.removeAllViews()

        gridLayoutOfSecondVector.rowCount = numOfRows
        gridLayoutOfSecondVector.columnCount = 1

        editTextsOfGridLayoutOfSecondVector = Array(numOfRows) {
            EditText(gridLayoutOfSecondVector.context)
        }

        for (i in 0 until numOfRows) {
            //editTexts[i] = EditText(gridLayoutOfVectorB.context)
            gridLayoutOfSecondVector.addView(editTextsOfGridLayoutOfSecondVector[i])
            setupEditText(editTextsOfGridLayoutOfSecondVector[i], i, 0)
        }
    }

    // Putting the edit text according to row and column index
    private fun setupEditText(editText: EditText, row: Int, column: Int) {
        val param = GridLayout.LayoutParams()
        param.apply {
            width = 160
            height = 120
            setGravity(Gravity.CENTER)
            rowSpec = GridLayout.spec(row)
            columnSpec = GridLayout.spec(column)
        }

        editText.apply {
            layoutParams = param
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            inputType = (InputType.TYPE_CLASS_NUMBER
                    or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    or InputType.TYPE_NUMBER_FLAG_SIGNED)
        }
    }

    private fun setupTextView(textView: TextView, row: Int, column: Int) {
        val param = GridLayout.LayoutParams()
        param.apply {
            width = 160
            height = 120
            setGravity(Gravity.CENTER)
            rowSpec = GridLayout.spec(row)
            columnSpec = GridLayout.spec(column)
        }

        textView.apply {
            layoutParams = param
            setTextColor(ResourcesCompat.getColor(resources, R.color.green, null))
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            inputType = (InputType.TYPE_CLASS_NUMBER
                    or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    or InputType.TYPE_NUMBER_FLAG_SIGNED)
        }
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

        private const val MATRIX_OP_ADD = 0
        private const val MATRIX_OP_SUB = 1
        private const val MATRIX_OP_MUL_NUMBER = 2
        private const val MATRIX_OP_MUL_VECTOR = 3
        private const val MATRIX_OP_MUL_MATRIX = 4
        private const val MATRIX_OP_NORM = 5
        private const val MATRIX_OP_TRANSPOSE = 6
        private const val MATRIX_OP_ADJOINT = 7
        private const val MATRIX_OP_INVERTIBLE = 8
        private const val MATRIX_OP_LU_DECOMPOSITION = 9
        private const val MATRIX_OP_QR_DECOMPOSITION = 10
        private const val MATRIX_OP_DETERMINANT = 11
    }
}