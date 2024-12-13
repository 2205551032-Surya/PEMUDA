package com.dicoding.mypemudaapp

import com.google.gson.annotations.SerializedName

data class JobOfferResponse(
    val data: List<JobOffer>
)

data class JobOffer(
    @SerializedName("id_job_offer") val idJobOffer: Int,
    @SerializedName("position") val position: String,
    @SerializedName("id_regency") val idRegency: Int,
    @SerializedName("job_type") val jobType: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("id_company") val idCompany: Int,
    @SerializedName("req_skill") val requiredSkills: String
)
