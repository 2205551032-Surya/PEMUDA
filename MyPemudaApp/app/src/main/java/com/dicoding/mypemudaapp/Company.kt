package com.dicoding.mypemudaapp

import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName ("id_company") val idCompany: Int,
    @SerializedName ("name_company") val companyName: String
)
