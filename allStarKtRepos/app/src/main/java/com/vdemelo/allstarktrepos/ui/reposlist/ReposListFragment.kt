package com.vdemelo.allstarktrepos.ui.reposlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.vdemelo.allstarktrepos.R
import com.vdemelo.allstarktrepos.databinding.FragmentReposListBinding
import com.vdemelo.allstarktrepos.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReposListFragment : Fragment() {

    private lateinit var binding: FragmentReposListBinding
    private val mainViewModel by viewModel<MainViewModel>()
    private val reposListViewModel by viewModel<ReposListViewModel>()
    private val recyclerView: RecyclerView get() = binding.recyclerViewRepos
//    private val reposRecyclerAdapter: ReposAdapter = ReposAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentReposListBinding.inflate(layoutInflater)

        initUI()
        initObservers()
        reposListViewModel.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repos_list, container, false)
    }

    private fun initUI() {
        setupRecyclerView()
    }

    private fun initObservers() {
//        mainViewModel.repos.observe(this, ::onObserveIssues)
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
//            this.adapter = reposRecyclerAdapter
            this.isVerticalScrollBarEnabled = true
        }
    }

//    private fun onObserveIssues(issues: List<Repo>) {
//        Timber.d(issues.toString())
//
//        recyclerView.adapter = reposRecyclerAdapter.apply {
//            this.repos.clear()
//            this.repos.addAll(issues)
//            this.notifyDataSetChanged()
//        }
//    }

}