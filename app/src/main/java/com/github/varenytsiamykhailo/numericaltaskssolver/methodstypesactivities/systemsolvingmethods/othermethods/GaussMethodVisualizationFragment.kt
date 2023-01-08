package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities.systemsolvingmethods.othermethods

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.varenytsiamykhailo.knml.systemsolvingmethods.GaussMethod
import com.github.varenytsiamykhailo.knml.util.Matrix
import com.github.varenytsiamykhailo.knml.util.Vector
import com.github.varenytsiamykhailo.knml.util.results.VectorResultWithStatus
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentGaussMethodVisualizationBinding
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.sqrt
import kotlin.random.Random


class GaussMethodVisualizationFragment : Fragment() {
    private lateinit var binding: FragmentGaussMethodVisualizationBinding

    var minY = Double.MAX_VALUE
    var maxY = Double.MIN_VALUE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGaussMethodVisualizationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun setupClickListeners() {
        binding.apply {
            removeAllGraphicsBtn.setOnClickListener {
                graph.removeAllSeries()
                minY = Double.MAX_VALUE
                maxY = Double.MIN_VALUE
            }

            classicGaussMethodBtn.setOnClickListener { generateClassicGaussMethodGraphic(GaussMethod.Classic, false) }
            gaussMethodWithCompletePivotingBtn.setOnClickListener { generateClassicGaussMethodGraphic(
                GaussMethod.CompletePivoting, false) }
            gaussMethodWithPivotingByRowBtn.setOnClickListener { generateClassicGaussMethodGraphic(
                GaussMethod.PartialPivotingByRow, false) }
            gaussMethodWithPivotingByColumnBtn.setOnClickListener { generateClassicGaussMethodGraphic(
                GaussMethod.PartialPivotingByColumn, false) }

            classicGaussMethodBtnDiagonal.setOnClickListener { generateClassicGaussMethodGraphic(
                GaussMethod.Classic, true) }
            gaussMethodWithCompletePivotingBtnDiagonal.setOnClickListener { generateClassicGaussMethodGraphic(
                GaussMethod.CompletePivoting, true) }
            gaussMethodWithPivotingByRowBtnDiagonal.setOnClickListener { generateClassicGaussMethodGraphic(
                GaussMethod.PartialPivotingByRow, true) }
            gaussMethodWithPivotingByColumnBtnDiagonal.setOnClickListener { generateClassicGaussMethodGraphic(
                GaussMethod.PartialPivotingByColumn, true) }
        }
    }

    enum class GaussMethod {
        Classic,
        PartialPivotingByColumn,
        PartialPivotingByRow,
        CompletePivoting
    }

    private fun generateClassicGaussMethodGraphic(gaussMethod: GaussMethod, isDiagonal: Boolean) {
        val graph = binding.graph
        val points = mutableListOf<DataPoint>()

        for (i in 3 until 100 step 10) {
            val matrix = generateRandomMatrix(i, isDiagonal)
            val vectorX = generateRandomVector(i)
            val vectorB = matrix.multiply(vectorX)
            val vectorX2 = when (gaussMethod) {
                GaussMethod.Classic -> GaussMethod().solveSystemByGaussClassicMethod(matrix, vectorB, false)
                GaussMethod.PartialPivotingByColumn -> GaussMethod().solveSystemByGaussMethodWithPivoting(matrix, vectorB, false, com.github.varenytsiamykhailo.knml.systemsolvingmethods.GaussMethod.PivotingStrategy.PartialByColumn)
                GaussMethod.PartialPivotingByRow -> GaussMethod().solveSystemByGaussMethodWithPivoting(matrix, vectorB, false, com.github.varenytsiamykhailo.knml.systemsolvingmethods.GaussMethod.PivotingStrategy.PartialByRow)
                GaussMethod.CompletePivoting -> GaussMethod().solveSystemByGaussMethodWithPivoting(matrix, vectorB, false, com.github.varenytsiamykhailo.knml.systemsolvingmethods.GaussMethod.PivotingStrategy.Complete)
            }
            val diff = getDiffOfVectors(vectorX, vectorX2)
            if (diff < minY) minY = diff
            if (diff > maxY) maxY = diff
            points.add(DataPoint(i.toDouble(), diff))
        }
        val series = LineGraphSeries(points.toTypedArray())
        graph.addSeries(series)
        series.color =
            when (gaussMethod) {
                GaussMethod.Classic -> Color.CYAN
                GaussMethod.PartialPivotingByColumn -> Color.GRAY
                GaussMethod.PartialPivotingByRow -> Color.BLUE
                GaussMethod.CompletePivoting -> Color.RED
            }

        with(graph) {
            viewport.isYAxisBoundsManual = true
            viewport.isScalable = true

            viewport.setScalableY(true)
            viewport.setMinY(minY)
            viewport.setMaxY(maxY)

            viewport.isXAxisBoundsManual = true
            viewport.setMinX(10.0)
            viewport.setMaxX(100.0)
        }
    }

    private fun getDiffOfVectors(vectorX: Vector, vectorX2: VectorResultWithStatus): Double {
        val diffVectorArray: MutableList<Double> = mutableListOf()
        if (vectorX2.vectorResult != null) {
            for (i in 0 until vectorX.getN()) {
                diffVectorArray.add((vectorX.getElem(i) - vectorX2.vectorResult!!.getElem(i)))
            }
        }
        val diffVector = Vector(diffVectorArray.toTypedArray())

        var sqrSumOfVectorValues = 0.0
        for (i in 0 until diffVector.getN()) {
            sqrSumOfVectorValues += diffVector.getElem(i) * diffVector.getElem(i)
        }

        return sqrt(sqrSumOfVectorValues)
    }

    companion object {
        val m = 0
        val n = 10

        private fun random(from: Int, to: Int) : Int {
            return java.util.Random().nextInt(to - from) + from
        }

        private fun generateRandomMatrix(i: Int, isDiagonal: Boolean): Matrix {
            val matrixArray: MutableList<Array<Double>> = mutableListOf()

            for (j in 0 until i) {
                val randomArray: MutableList<Double> = mutableListOf()
                for (k in 0 until i) {
                    if (isDiagonal && j == k) {
                        randomArray.add(random(m, n).toDouble() * 100)
                    } else {
                        randomArray.add(random(m, n).toDouble())
                    }
                }
                matrixArray.add(randomArray.toTypedArray())
            }

            return Matrix(matrixArray.toTypedArray())
        }

        private fun generateRandomVector(i: Int): Vector {
            val vectorArray: MutableList<Double> = mutableListOf()
            for (i in 0 until i) {
                vectorArray.add(Random.nextDouble(0.9, 1.1))
            }
            return Vector(vectorArray.toTypedArray())
        }
    }
}