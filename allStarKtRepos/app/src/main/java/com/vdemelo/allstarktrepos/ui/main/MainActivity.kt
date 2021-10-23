package com.vdemelo.allstarktrepos.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.vdemelo.allstarktrepos.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModel<MainViewModel>()
//    private val recyclerView: RecyclerView get() = binding.recyclerViewRepos
//    private val reposRecyclerAdapter: RepoAdapter = RepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initObservers()
        mainViewModel.refresh()
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