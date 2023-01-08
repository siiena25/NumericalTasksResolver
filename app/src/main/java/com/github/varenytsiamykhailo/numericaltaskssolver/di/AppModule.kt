package com.github.varenytsiamykhailo.numericaltaskssolver.di

import com.github.varenytsiamykhailo.numericaltaskssolver.SharedPreferencesStorage
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("storage")) { SharedPreferencesStorage(get()) }
}

/*
val loginModule = module {
    single { LoginServiceImpl() }
    single { LoginRepository(SharedPreferencesStorage(get()), LoginRemoteDataSource(get())) }
    viewModel { LoginViewModel(get()) }
}*/
