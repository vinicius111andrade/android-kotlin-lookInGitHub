package com.vdemelo.allstarktrepos.di

import com.vdemelo.allstarktrepos.data.repository.GithubRepository
import com.vdemelo.allstarktrepos.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    single { GithubRepository(get())}
}
