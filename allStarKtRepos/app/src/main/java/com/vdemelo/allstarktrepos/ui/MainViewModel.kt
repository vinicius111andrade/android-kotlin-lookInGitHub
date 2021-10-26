package com.vdemelo.allstarktrepos.ui

import androidx.lifecycle.*
import com.vdemelo.allstarktrepos.data.model.SearchResult
import com.vdemelo.allstarktrepos.data.repository.StarredRepository
import com.vdemelo.allstarktrepos.utils.Constants.DEFAULT_QUERY
import com.vdemelo.allstarktrepos.utils.Constants.LAST_SEARCH_QUERY
import com.vdemelo.allstarktrepos.utils.Constants.VISIBLE_THRESHOLD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: StarredRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    /**
     * Either searching or scrolling
     */
    val state: LiveData<UiState>

    /**
     * Action the repository should take based on the state.
     */
    val action: (UiAction) -> Unit

    init {
        val queryLiveData = MutableLiveData(savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY)

        state = queryLiveData
            .distinctUntilChanged()
            .switchMap { queryString ->
                liveData {
                    val uiState: LiveData<UiState> = repository.getSearchResultStream(queryString)
                        .map {
                            UiState(
                                query = queryString,
                                searchResult = it
                            )
                        }
                        .asLiveData(Dispatchers.Main)
                    emitSource(uiState)
                }
            }

        action = { uiAction ->
            when (uiAction) {
                is UiAction.Search -> queryLiveData.postValue(uiAction.query)
                is UiAction.Scroll -> if (uiAction.shouldFetchMore) {
                    val immutableQuery = queryLiveData.value
                    if (immutableQuery != null) {
                        viewModelScope.launch {
                            repository.requestMore(immutableQuery)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value?.query
        super.onCleared()
    }

}

private val UiAction.Scroll.shouldFetchMore
    get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount

sealed class UiAction {

    data class Search(
        val query: String
    ) : UiAction()

    data class Scroll(
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int,
        val totalItemCount: Int
    ) : UiAction()

}

data class UiState(
    val query: String,
    val searchResult: SearchResult
)