package com.vdemelo.allstarktrepos.data.model

import com.google.gson.annotations.SerializedName

class Owner (
    @field:SerializedName("login" ) val login : String = "Repository Owner",
    @field:SerializedName("avatar_url" ) val avatarUrl : String = "",
)
