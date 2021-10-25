package com.vdemelo.allstarktrepos.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // This will prevent state loss
//    private val viewModel: MainViewModel by viewModels {
//        object : AbstractSavedStateViewModelFactory(this, null) {
//            override fun <T : ViewModel?> create(
//                key: String,
//                modelClass: Class<T>,
//                handle: SavedStateHandle
//            ): T {
//                @Suppress("UNCHECKED_CAST")
//                return MainViewModel(handle, PageKeyRepository(GithubApi.create())) as T
//            }
//        }
//    }

//    private val mainViewModel by viewModel<MainViewModel>()
//    private val recyclerView: RecyclerView get() = binding.recyclerViewRepos
//    private val reposRecyclerAdapter: RepoAdapter = RepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initObservers()
    }

    private fun initUI() {
        setupRecyclerView()
    }

    private fun initObservers() {
//        mainViewModel.repos.observe(this, ::onObserveRepos)
    }

    private fun setupRecyclerView() {
//        recyclerView.apply {
//            this.adapter = reposRecyclerAdapter
//            this.isVerticalScrollBarEnabled = true
//        }
    }

//    private fun onObserveRepos(repos: List<Repo>) {
//        Timber.d(repos.toString())
//
//        recyclerView.adapter = reposRecyclerAdapter.apply {
//            this.repos.clear()
//            this.repos.addAll(repos)
//            this.notifyDataSetChanged() // Se der pra tirar eh melhor
//        }
//    }

}