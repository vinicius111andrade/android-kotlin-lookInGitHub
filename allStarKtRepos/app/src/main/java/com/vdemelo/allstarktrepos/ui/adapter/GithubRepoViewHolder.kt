package com.vdemelo.allstarktrepos.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.databinding.ItemListGithubRepoBinding

/**
 * Created by Vinicius Andrade on 10/25/2021.
 */
class GithubRepoViewHolder(
    var binding: ItemListGithubRepoBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        internal fun inflateViewBinding(parent: ViewGroup, viewType: Int): ItemListGithubRepoBinding {
            return ItemListGithubRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
    }

    private var githubRepo: GithubRepo? = null

    init {
        this.binding.root.tag = this
        this.initUI()
    }

    private fun initUI() {
        binding.repoCard.setOnClickListener {
            githubRepo?.run {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(html_url))
                binding.repoCard.context.startActivity(intent)
            }
        }
    }

    fun bind(githubRepo: GithubRepo) {
        this@GithubRepoViewHolder.githubRepo = githubRepo
        binding.githubRepo = githubRepo
    }

}