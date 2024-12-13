package com.dicoding.mypemudaapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JobApiService {
    @GET("/job-offers")
    fun getJobOffers(
        @Query("id_regency") idRegency: Int,
        @Query("req_skills") skills: String
    ): Call<JobOfferResponse>

    @GET("/companies")
    fun getAllCompanies(
    ): Call<List<Company>>

    @GET("/job-offers/{id_job_offer}")
    fun getJobOfferById(@Path("id_job_offer") jobId: Int): Call<JobDetailResponse>

    @GET("/companies/{id_company}")
    fun getCompanyById(@Path("id_company") companyId: Int): Call<JobDetailResponse>
}