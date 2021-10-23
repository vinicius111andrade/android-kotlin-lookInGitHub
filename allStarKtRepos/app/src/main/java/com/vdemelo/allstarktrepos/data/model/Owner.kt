package com.vdemelo.allstarktrepos.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */
data class Owner (
    @SerializedName("login" ) val login : String = "Repository Owner",
    @SerializedName("avatar_url" ) val avatar_url : String = "",
)