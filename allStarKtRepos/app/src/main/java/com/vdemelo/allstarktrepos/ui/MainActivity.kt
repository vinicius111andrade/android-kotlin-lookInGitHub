package com.vdemelo.allstarktrepos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.model.SearchResult
import com.vdemelo.allstarktrepos.databinding.ActivityMainBinding
import com.vdemelo.allstarktrepos.di.Injection
import com.vdemelo.allstarktrepos.ui.adapter.GithubRepoAdapter
import com.vdemelo.allstarktrepos.ui.adapter.GithubRepoViewHolder

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
        binding.apply {

            setupRecyclerView()

            bindState(
                uiState = viewModel.state,
                uiActions = viewModel.action
            )

        }
    }

    private fun ActivityMainBinding.bindState(
        uiState: LiveData<UiState>,
        uiActions: (UiAction) -> Unit
    ) {
        val githubRepoAdapter = GithubRepoAdapter()
        recyclerview.adapter = githubRepoAdapter

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            githubRepoAdapter = githubRepoAdapter,
            uiState = uiState,
            onScrollChanged = uiActions
        )
    }

    private fun ActivityMainBinding.bindSearch(
        uiState: LiveData<UiState>,
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

        uiState
            .map(UiState::query)
            .distinctUntilChanged()
            .observe(this@MainActivity, searchEditText::setText)
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
        uiState: LiveData<UiState>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        setupScrollListener(onScrollChanged)

        uiState
            .map(UiState::searchResult)
            .distinctUntilChanged()
            .observe(this@MainActivity) { result ->
                when (result) {
                    is SearchResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        githubRepoAdapter.submitList(result.data)
                    }
                    is SearchResult.Error -> {
                        Toast.makeText(
                            this@MainActivity,
                            "\uD83D\uDE35 Wooops $result.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun ActivityMainBinding.showEmptyList(show: Boolean) {
        emptyListMessage.isVisible = show
        recyclerview.isVisible = !show
    }

    private fun ActivityMainBinding.setupScrollListener(
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        val layoutManager = recyclerview.layoutManager as LinearLayoutManager
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                onScrollChanged(
                    UiAction.Scroll(
                        visibleItemCount = visibleItemCount,
                        lastVisibleItemPosition = lastVisibleItem,
                        totalItemCount = totalItemCount
                    )
                )
            }
        })
    }

    private fun ActivityMainBinding.setupRecyclerView() {
        recyclerview.apply {

            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }
    }

}