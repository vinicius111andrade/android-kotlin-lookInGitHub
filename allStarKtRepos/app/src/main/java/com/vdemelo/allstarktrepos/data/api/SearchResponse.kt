package com.vdemelo.allstarktrepos.data.api

import com.google.gson.annotations.SerializedName
import com.vdemelo.allstarktrepos.data.model.GithubRepo

 class SearchResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<GithubRepo> = emptyList()
)
