package com.dicoding.mypemudaapp

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class JobDetailActivity : AppCompatActivity() {
    private val baseUrl = "http://34.101.101.213:3000"
    private lateinit var apiService: JobApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        val tvJobTitle = findViewById<TextView>(R.id.tvJobTitle)
        val tvCompanyLocation = findViewById<TextView>(R.id.tvCompanyLocation)
        val tvJobDescription = findViewById<TextView>(R.id.tvJobDescription)
        val tvSalary = findViewById<TextView>(R.id.tvIndustry)
        val tvResponsibilities = findViewById<TextView>(R.id.tvWebsite)
        val tvRequiredSkills = findViewById<TextView>(R.id.tvEmployees)
        val btnApply = findViewById<Button>(R.id.btnApply)
        val imgBack = findViewById<ImageView>(R.id.imgBack)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(JobApiService::class.java)

        // Retrieve job ID from Intent
        val jobId = intent.getIntExtra("jobId", -1)
        if (jobId == -1) {
            Toast.makeText(this, "Pekerjaan tidak valid.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Fetch job details from API
        fetchJobDetails(jobId, tvJobTitle, tvCompanyLocation, tvJobDescription, tvSalary, tvResponsibilities, tvRequiredSkills)

        // Apply button functionality
        btnApply.setOnClickListener {
            Toast.makeText(this, "Lamaran untuk pekerjaan ini telah dikirim!", Toast.LENGTH_SHORT).show()
        }

        // Back button functionality
        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchJobDetails(
        jobId: Int,
        tvJobTitle: TextView,
        tvCompanyLocation: TextView,
        tvJobDescription: TextView,
        tvWebsite: TextView,
        tvEmployees: TextView,
        tvIndustry: TextView
    ) {
        apiService.getJobOfferById(jobId).enqueue(object : Callback<JobDetailResponse> {
            override fun onResponse(
                call: Call<JobDetailResponse>,
                response: Response<JobDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val jobDetail = response.body()?.data
                    if (jobDetail != null) {
                        tvJobTitle.text = jobDetail.position
                        tvCompanyLocation.text = "Tabanan"
                        tvJobDescription.text = "Responsible for the ui/ux designer in the technology industry."
                        tvIndustry.text = "Alpha PT Prima Industri Industries"
                        tvEmployees.text = "1729"
                        tvWebsite.text = "www.primaindustri.com"
                    } else {
                        Toast.makeText(this@JobDetailActivity, "Data pekerjaan tidak ditemukan.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@JobDetailActivity, "Gagal memuat detail pekerjaan.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JobDetailResponse>, t: Throwable) {
                Toast.makeText(this@JobDetailActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

// Tambahkan data class untuk response
data class JobDetailResponse(
    val data: JobDetail
)

data class JobDetail(
    val position: String,
    val companyName: String,
    val location: String,
    val description: String,
    val salary: String,
    val responsibilities: String,
    val requiredSkills: String
)
