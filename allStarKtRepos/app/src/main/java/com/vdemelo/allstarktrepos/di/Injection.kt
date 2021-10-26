package com.vdemelo.allstarktrepos.di

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.repository.StarredRepository
import com.vdemelo.allstarktrepos.ui.ViewModelFactory

object Injection {

    private fun provideGithubRepository(): StarredRepository {
        return StarredRepository(GithubApi.create())
    }

    fun provideViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideGithubRepository())
    }

}