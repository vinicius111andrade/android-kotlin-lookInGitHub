package com.vdemelo.allstarktrepos.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.model.SearchResult
import com.vdemelo.allstarktrepos.databinding.ActivityMainBinding
import com.vdemelo.allstarktrepos.di.Injection
import com.vdemelo.allstarktrepos.ui.adapter.GithubRepoAdapter
import com.vdemelo.allstarktrepos.ui.adapter.LoadStateAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(owner = this)
        ).get(MainViewModel::class.java)

        initUI()
    }

    private fun initUI() {
        binding.bindState(
                uiState = viewModel.state,
                pagingData = viewModel.pagingDataFlow,
                uiActions = viewModel.action
            )
    }

    private fun ActivityMainBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<GithubRepo>>,
        uiActions: (UiAction) -> Unit
    ) {
        val githubRepoAdapter = GithubRepoAdapter()
        recyclerview.adapter = githubRepoAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter { githubRepoAdapter.retry() },
            footer = LoadStateAdapter { githubRepoAdapter.retry() }
        )

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            githubRepoAdapter = githubRepoAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun ActivityMainBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(searchEditText::setText)
        }
    }

    private fun ActivityMainBinding.updateRepoListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        searchEditText.text.trim().let {
            if (it.isNotEmpty()) {
                recyclerview.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun ActivityMainBinding.bindList(
        githubRepoAdapter: GithubRepoAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<GithubRepo>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {

        recyclerview.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })
        val notLoading = githubRepoAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for RemoteMediator changes.
            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where Remote REFRESH completes i.e., NotLoading.
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(githubRepoAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recyclerview.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            githubRepoAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && githubRepoAdapter.itemCount == 0
                // show empty list
                emptyListMessage.isVisible = isListEmpty
                // Only show the list if refresh succeeds.
                recyclerview.isVisible = !isListEmpty
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        this@MainActivity,
                        "\uD83D\uDE35 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}