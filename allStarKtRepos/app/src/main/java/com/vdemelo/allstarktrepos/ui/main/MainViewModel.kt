package com.vdemelo.allstarktrepos.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PageKeyRepository
): ViewModel() {

    companion object {
        const val KEY_LANGUAGE = "language"
        const val DEFAULT_LANGUAGE = "kotlin"
    }

    init {
        if (!savedStateHandle.contains(KEY_LANGUAGE)) {
            savedStateHandle.set(KEY_LANGUAGE, DEFAULT_LANGUAGE)
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    val posts = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<GithubRepo>() },
        savedStateHandle.getLiveData<String>(KEY_LANGUAGE)
            .asFlow()
            .flatMapLatest { repository.reposOfGithub(it, 30) }
            // cachedIn() shares the paging state across multiple consumers of posts,
            // e.g. different generations of UI across rotation config change
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    fun shouldShowLanguage(language: String) = savedStateHandle.get<String>(KEY_LANGUAGE) != language

    fun showLanguage(language: String) {
        if (!shouldShowLanguage(language))
            return

        clearListCh.trySend(Unit).isSuccess

        savedStateHandle.set(KEY_LANGUAGE, language)
    }

}