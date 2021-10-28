package com.vdemelo.allstarktrepos.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */
data class Owner (
    @field:SerializedName("login" ) val login : String = "Repository Owner",
    @field:SerializedName("avatar_url" ) val avatarUrl : String = "",
)