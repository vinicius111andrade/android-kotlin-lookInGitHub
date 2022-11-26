package com.vdemelo.allstarktrepos.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.databinding.ItemListGithubRepoBinding
import com.vdemelo.allstarktrepos.utils.loadImage

class GithubRepoViewHolder(
    var binding: ItemListGithubRepoBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        internal fun inflateViewBinding(parent: ViewGroup): ItemListGithubRepoBinding {
            return ItemListGithubRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
    }

    private var githubRepo: GithubRepo? = null
    private val context = binding.tvForkCount.context

    init {
        this.binding.root.tag = this
        this.initUI()
    }

    private fun initUI() {
        binding.repoCard.setOnClickListener {
            githubRepo?.run {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(html_url))
                context.startActivity(intent)
            }
        }
    }

    fun bind(githubRepo: GithubRepo) {
        this@GithubRepoViewHolder.githubRepo = githubRepo
        binding.tvRepositoryName.text = githubRepo.name
        binding.tvStarCount.text = githubRepo.stargazersCount.toString()
        binding.tvForkCount.text = githubRepo.forksCount.toString()
        binding.tvOwnerName.text = githubRepo.owner.login
        binding.imgOwner.loadImage(githubRepo.owner.avatarUrl)
    }
}
