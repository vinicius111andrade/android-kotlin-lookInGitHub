package com.vdemelo.allstarktrepos.data.model

import com.google.gson.annotations.SerializedName
import com.vdemelo.allstarktrepos.data.model.Owner

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */
data class GithubRepo (
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String = "",
    @field:SerializedName("full_name") val fullName: String = "",
    @field:SerializedName("owner") val owner: Owner = Owner(),
    @field:SerializedName("stargazers_count") val stargazersCount : Int = 0,
    @field:SerializedName("forks_count") val forksCount: Int = 0,
    @field:SerializedName("description") val description: String = "",
    @field:SerializedName("html_url") val html_url: String = "",
    @field:SerializedName("language") val language: String = ""
)