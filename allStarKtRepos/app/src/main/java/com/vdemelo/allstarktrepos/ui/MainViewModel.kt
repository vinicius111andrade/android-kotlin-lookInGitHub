package com.vdemelo.allstarktrepos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.repository.GithubRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val DEFAULT_QUERY = "kotlin"

class MainViewModel(
    private val repository: GithubRepository,
): ViewModel() {

    /**
     * A stream of UI states, either searching or scrolling
     */
    val state: StateFlow<UiState>

    val pagingDataFlow: Flow<PagingData<GithubRepo>>

    /**
     * Action the repository should take based on the state.
     */
    val action: (UiAction) -> Unit

//    override fun onCleared() {
//        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
//        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
//        super.onCleared()
//    }

    init {

        val initialQuery: String = DEFAULT_QUERY
        val lastQueryScrolled: String = DEFAULT_QUERY
        //TODO - quando o activity morre eu to perdendo a query que está ativa,
        // preciso salvar o valor de alguma forma, ou então resetar a caixa de texto tbm,
        // a caixa de texto tá salvando, mas a viewmodel não. Posso pegar a query da caixa
        // de texto, resolve.
        val actionStateFlow = MutableSharedFlow<UiAction>()

        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }

        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }

        pagingDataFlow = searches
            .flatMapLatest { searchGithub(queryString = it.query) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        action = { uiAction ->
            viewModelScope.launch { actionStateFlow.emit(uiAction) }
        }
    }

    private fun searchGithub(queryString: String): Flow<PagingData<GithubRepo>> =
        repository.getSearchResultStream(queryString)

}

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)
