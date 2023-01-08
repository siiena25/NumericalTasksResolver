package com.github.varenytsiamykhailo.numericaltaskssolver.data.states

import com.jjoe64.graphview.series.DataPoint


sealed class DataState {
    data class PointsOfGenerateGraphicDiffAndMuSuccess(val points: List<Any>) : DataState()
    data class PointsOfJacobiAndSeidelConvergence(val pointsPair: Pair<MutableList<DataPoint>, MutableList<DataPoint>>) : DataState()
    data class PointsOfGenerateGraphicWithStepDiffSuccess(val points: List<Any>) : DataState()
    object Error : DataState()
    object Default : DataState()
}