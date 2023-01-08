package com.github.varenytsiamykhailo.numericaltaskssolver.view_models

import androidx.lifecycle.ViewModel
import com.github.varenytsiamykhailo.numericaltaskssolver.data.repository.MatrixConditionNumberCalcRepository
import com.github.varenytsiamykhailo.numericaltaskssolver.data.states.DataState
import kotlinx.coroutines.flow.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class MatrixConditionNumberCalcViewModel constructor(private val repository: MatrixConditionNumberCalcRepository) : ViewModel() {
    private val state: MutableSharedFlow<DataState> = MutableStateFlow(DataState.Default)
    private val _state: SharedFlow<DataState> = state.asSharedFlow()

    fun getPointsListOfGenerateGraphicDiffAndMu() {
        viewModelScope.launch {
            repository.getPointsOfGenerateGraphicDiffAndMu()
                .catch { state.emit(DataState.Error) }
                .collect { state.emit(DataState.PointsOfGenerateGraphicDiffAndMuSuccess(it)) }
        }
    }

    fun getPointsForJacobiAndSeidelConvergence(n: Int) {
        viewModelScope.launch {
            repository.getPointsForJacobiAndSeidelConvergence(n)
                .catch { state.emit(DataState.Error) }
                .collect { state.emit(DataState.PointsOfJacobiAndSeidelConvergence(it)) }
        }
    }

    fun getPointsListOfGenerateGraphicWithStepDiff() {
        viewModelScope.launch {
            repository.getPointsOfGenerateGraphicWithStepDiff()
                .catch { state.emit(DataState.Error) }
                .collect { state.emit(DataState.PointsOfGenerateGraphicWithStepDiffSuccess(it)) }
        }
    }

    fun getState() = _state
}