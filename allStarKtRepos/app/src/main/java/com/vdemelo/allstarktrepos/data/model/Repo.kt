package com.vdemelo.allstarktrepos.data.model

import com.google.gson.annotations.SerializedName
import com.vdemelo.allstarktrepos.data.model.Owner

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */
data class Repo (
    @SerializedName("name") val name: String = "",
    @SerializedName("owner") val owner: Owner = Owner(),
    @SerializedName("stargazers_count") val stargazersCount : Int = 0,
    @SerializedName("forks_count") val forksCount: Int = 0
)