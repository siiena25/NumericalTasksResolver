package com.github.varenytsiamykhailo.numericaltaskssolver.data.repository

import com.github.varenytsiamykhailo.knml.systemsolvingmethods.JacobiMethod
import com.github.varenytsiamykhailo.knml.systemsolvingmethods.SeidelMethod
import com.github.varenytsiamykhailo.knml.util.*
import com.github.varenytsiamykhailo.knml.util.results.VectorResultWithStatus
import com.jjoe64.graphview.series.DataPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random


class MatrixConditionNumberCalcRepository {
    suspend fun getPointsOfGenerateGraphicDiffAndMu(): Flow<List<Any>> {
        return flow {
            emit(
                generatePointsForGraphicDiffAndMu()
            )
        }
    }

    suspend fun getPointsForJacobiAndSeidelConvergence(n: Int): Flow<Pair<MutableList<DataPoint>, MutableList<DataPoint>>> {
        return flow {
            emit(
                generatePointsFOrJacobiAndSeidelConvergence(n)
            )
        }
    }

    private fun generatePointsFOrJacobiAndSeidelConvergence(count: Int): Pair<MutableList<DataPoint>, MutableList<DataPoint>> {
        val jacobiIterations: MutableList<Int> = mutableListOf()
        val seidelIterations: MutableList<Int> = mutableListOf()

        for (N in 3 until count) {
            val A: Array<Array<Double>> = getMatrixWithRandomElementsAndDiagonalDominance(N, 1, 10, 1000).getElems()
            val B: Array<Double> = getVectorWithRandomElements(N, 1, 10).getElems()

            val resultByJacobiMethod: VectorResultWithStatus = JacobiMethod().solveSystemByJacobiMethod(
                A,
                B,
                eps = 1e-16,
                formSolution = true
            )

            val resultBySeidelMethod: VectorResultWithStatus = SeidelMethod().solveSystemBySeidelMethod(
                A,
                B,
                eps = 1e-16,
                formSolution = true
            )

            jacobiIterations.add(resultByJacobiMethod.solutionObject?.iterations!!)
            seidelIterations.add(resultBySeidelMethod.solutionObject?.iterations!!)
            println("LOG:: N=$N jacobi=${resultByJacobiMethod.solutionObject?.iterations!!} seidel=${resultBySeidelMethod.solutionObject?.iterations!!}")
        }

        val pointsJacobi: MutableList<DataPoint> = mutableListOf()
        for (i in 3 until jacobiIterations.size) {
            pointsJacobi.add(DataPoint(i.toDouble(), jacobiIterations[i].toDouble()))
        }

        val pointsSeidel: MutableList<DataPoint> = mutableListOf()
        for (i in 3 until seidelIterations.size) {
            pointsSeidel.add(DataPoint(i.toDouble(), seidelIterations[i].toDouble()))
        }

        println("pointsJacobi=$pointsJacobi")
        println("pointsSeidel=$pointsSeidel")
        return Pair(pointsJacobi, pointsSeidel)
    }

    suspend fun getPointsOfGenerateGraphicWithStepDiff(): Flow<List<Any>> {
        return flow {
            emit(
                generatePointsForGraphicWithStepDiff()
            )
        }
    }

    fun fillMatrix(n: Int, a: Double, b: Double): Matrix {
        val m = Matrix(n, n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                m.setElem(i, j, Random.nextDouble(-a, b))
            }
        }
        return m
    }

    fun calcDiff(deltaF: Vector, deltaA: Matrix, x: Vector, n: Int): MutableList<Any> {
        val a = fillMatrix(n, 0.0, 10.0)
        val f = a.multiply(x)

        val normaA = a.norm()
        val normaDeltaF = deltaF.norm()
        val normaF =f.norm()
        val normaDeltaA = deltaA.norm()

        val arrayMatrix: Array<DoubleArray> = a.getElems().map { it.toDoubleArray() }.toTypedArray().copyOf()
        val inverseMatrix = helpfulFunctions.inverse(arrayMatrix)

        val aInv = Matrix(a.getN(), a.getM())
        for (i in 0 until a.getN()) {
            for (j in 0 until a.getM()) {
                aInv.setElem(i, j, inverseMatrix!!.getElem(i, j))
            }
        }

        val mu = aInv.norm() * normaA
        println("mu: " + arrayMatrix + " " + aInv.norm() + " " + normaA)

        return mutableListOf(
            mu,
            mu * ((normaDeltaF / normaF) + (normaDeltaA / normaA)) * 100,
            normaDeltaA,
            normaA,
            normaDeltaF,
            normaF
        )
    }

    fun generatePointsForGraphicDiffAndMu(): List<Any> {
        val n = 100
        val deltaF = Vector(n)
        val deltaA = Matrix(n, n)
        for (i in 0 until n) {
            deltaF.setElem(i, 0.1)
            for (j in 0 until n) {
                deltaA.setElem(i, j, 0.1)
            }
        }

        //linspace from 0 to 100 with count of points = 100
        val x = Vector(100)
        for (i in 0 until 100) {
            x.setElem(i, i.toDouble())
        }

        val xAxis = mutableListOf<Double>()
        val yAxis = mutableListOf<Double>()
        val normaDeltaAVec = mutableListOf<Double>()
        val normaAVec = mutableListOf<Double>()
        val normaDeltaFVec = mutableListOf<Double>()
        val normaFVec = mutableListOf<Double>()

        for (i in 0 until 100) {
            val calcDiffList = calcDiff(deltaF, deltaA, x, n)
            val x_i = calcDiffList[0] as Double
            val y_i = calcDiffList[1] as Double
            val normaDeltaA = calcDiffList[2] as Double
            val normaA = calcDiffList[3] as Double
            val normaDeltaF = calcDiffList[4] as Double
            val normaF = calcDiffList[5] as Double

            xAxis.add(x_i)
            yAxis.add(y_i)
            normaDeltaAVec.add(normaDeltaA)
            normaAVec.add(normaA)
            normaDeltaFVec.add(normaDeltaF)
            normaFVec.add(normaF)
        }

        return getPointsForGraphicDiffAndMu(xAxis, yAxis)
    }

    private fun getPointsForGraphicDiffAndMu(xAxis: MutableList<Double>, yAxis: MutableList<Double>): List<Any> {
        val points = mutableListOf<DataPoint>()

        var minX = Double.MAX_VALUE
        var maxX = Double.MIN_VALUE

        var minY = Double.MAX_VALUE
        var maxY = Double.MIN_VALUE
        for (i in 0 until xAxis.size) {
            if (minX > xAxis[i]) minX = xAxis[i]
            if (maxX < xAxis[i]) maxX = xAxis[i]

            if (minY > yAxis[i] / 100) minY = yAxis[i] / 100
            if (maxY < yAxis[i] / 100) maxY = yAxis[i]/ 100

            points.add(DataPoint(xAxis[i], yAxis[i] / 100))
            println(points[i])
        }

        return listOf(points, minX, maxX, minY, maxY)
    }

    private fun getPointsForGraphicWithStepDiff(xAxis: MutableList<Double>, yAxis: MutableList<Double>): List<Any> {
        val points = mutableListOf<DataPoint>()

        var minX = 0.0
        var maxX = 100000.0

        var minY = 0.0
        var maxY = 100000.0
        for (i in 0 until xAxis.size) {
            if (minX > xAxis[i]) minX = xAxis[i]
            if (maxX < xAxis[i]) maxX = xAxis[i]

            if (minY > yAxis[i] / 100) minY = yAxis[i] / 100
            if (maxY < yAxis[i] / 100) maxY = yAxis[i]/ 100

            points.add(DataPoint(xAxis[i], yAxis[i] / 100))
            println(points[i])
        }

        return listOf(points, minX, maxX, minY, maxY)
    }

    fun generatePointsForGraphicWithStepDiff(): List<Any> {
        val n = 100
        val x = Vector(n)
        for (i in 0 until n) {
            x.setElem(i, 1.0)
        }
        val xAxis = mutableListOf<Double>()
        val yAxis = mutableListOf<Double>()

        for (i in 0 until 100) {
            val deltaF = Vector(n)
            val deltaA = Matrix(n, n)
            for (j in 0 until n) {
                deltaF.setElem(j, 0.1 + (i * 0.001))
                for (k in 0 until n) {
                    deltaA.setElem(j, k, 0.1 + (i * 0.001))
                }
            }
            val calcDiffList = calcDiff(deltaF, deltaA, x, n)
            val x_i = calcDiffList[0] as Double
            val y_i = calcDiffList[1] as Double
            xAxis.add(x_i)
            yAxis.add(y_i)
        }

        return getPointsForGraphicWithStepDiff(xAxis, yAxis)
    }

    internal object helpfulFunctions {
        const val N = 4

        // Function to get cofactor of A[p][q] in temp[][]. n is current
        // dimension of A[][]
        fun getCofactor(A: Array<DoubleArray>, temp: Array<DoubleArray>, p: Int, q: Int, n: Int) {
            var i = 0
            var j = 0

            // Looping for each element of the matrix
            for (row in 0 until n) {
                for (col in 0 until n) {
                    // Copying into temporary matrix only those element
                    // which are not in given row and column
                    if (row != p && col != q) {
                        temp[i][j++] = A[row][col]

                        // Row is filled, so increase row index and
                        // reset col index
                        if (j == n - 1) {
                            j = 0
                            i++
                        }
                    }
                }
            }
        }

        fun determinant(A: Array<DoubleArray>, n: Int): Double {
            var D = 0.0 // Initialize result

            // Base case : if matrix contains single element
            if (n == 1) return A[0][0]
            val temp = Array(N) {
                DoubleArray(
                    N
                )
            } // To store cofactors
            var sign = 1 // To store sign multiplier

            // Iterate for each element of first row
            for (f in 0 until n) {
                // Getting Cofactor of A[0][f]
                getCofactor(A, temp, 0, f, n)
                D += sign * A[0][f] * determinant(temp, n - 1)

                // terms are to be added with alternate sign
                sign = -sign
            }
            return D
        }

        // Function to get adjoint of A[N][N] in adj[N][N].
        fun adjoint(A: Array<DoubleArray>, adj: Array<DoubleArray>) {
            if (N == 1) {
                adj[0][0] = 1.0
                return
            }

            // temp is used to store cofactors of A[][]
            var sign = 1
            val temp = Array(N) {
                DoubleArray(
                    N
                )
            }
            for (i in 0 until N) {
                for (j in 0 until N) {
                    // Get cofactor of A[i][j]
                    getCofactor(A, temp, i, j, N)

                    // sign of adj[j][i] positive if sum of row
                    // and column indexes is even.
                    sign = if ((i + j) % 2 == 0) 1 else -1

                    // Interchanging rows and columns to get the
                    // transpose of the cofactor matrix
                    adj[j][i] = sign * determinant(temp, N - 1)
                }
            }
        }

        // Function to calculate and store inverse, returns false if
        // matrix is singular
        fun inverse(A: Array<DoubleArray>): Matrix? {
            val matrix = Matrix(A.size, A.size)

            // Find determinant of A[][]
            val det = determinant(A, N)
            if (det == 0.0) {
                print("Singular matrix, can't find its inverse")
                return null
            }

            // Find adjoint
            val adj = Array(N) {
                DoubleArray(
                    N
                )
            }
            adjoint(A, adj)

            // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
            for (i in 0 until N) for (j in 0 until N) matrix.setElem(i, j, adj[i][j] / det)
            return matrix
        }
    }
}