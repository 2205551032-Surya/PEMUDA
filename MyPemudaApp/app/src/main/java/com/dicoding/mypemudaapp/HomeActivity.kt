package com.dicoding.mypemudaapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var rvRekomendasiPekerjaan: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var barChart: BarChart

    private var rekomendasiPekerjaan = mutableListOf<JobOffer>()
    private lateinit var rekomendasiAdapter: SimpleAdapter

    private val baseUrl = "http://34.101.101.213:3000"
    private lateinit var apiService: JobApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvRekomendasiPekerjaan = findViewById(R.id.rvRekomendasiPekerjaan)
        etSearch = findViewById(R.id.etSearch)
        barChart = findViewById(R.id.barChart)

        // Setup RecyclerView
        rekomendasiAdapter = SimpleAdapter(rekomendasiPekerjaan)

        rvRekomendasiPekerjaan.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = rekomendasiAdapter
        }

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(JobApiService::class.java)

        // Get data from Intent
        val idRegency = intent.getIntExtra("selectedRegionId", -1)
        val skills = intent.getStringExtra("selectedSkills") ?: ""

        if (idRegency == -1 && skills.isEmpty()) {
            Toast.makeText(this, "Data tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch job offers
        fetchJobOffers(idRegency, skills)

        // Handle search functionality
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                filterData(query)
            }
        })

        val imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack.setOnClickListener {
            finish() // Kembali ke halaman sebelumnya
        }
    }

    private fun fetchJobOffers(idRegency: Int, skills: String) {
        apiService.getJobOffers(idRegency, skills).enqueue(object : Callback<JobOfferResponse> {
            override fun onResponse(call: Call<JobOfferResponse>, response: Response<JobOfferResponse>) {
                if (response.isSuccessful) {
                    val jobs = response.body()?.data ?: emptyList() // Ambil data dari key `data`
                    rekomendasiPekerjaan.clear()
                    rekomendasiPekerjaan.addAll(jobs)
                    rekomendasiAdapter.updateData(rekomendasiPekerjaan)
                } else {
                    Toast.makeText(this@HomeActivity, "Gagal memuat data pekerjaan.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JobOfferResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterData(query: String) {
        val filteredRekomendasi = if (query.isEmpty()) rekomendasiPekerjaan
        else rekomendasiPekerjaan.filter {
            it.position.contains(query, ignoreCase = true)
        }

        rekomendasiAdapter.updateData(filteredRekomendasi)

        // Update Bar Chart
        setupBarChart(filteredRekomendasi)
    }

    private fun setupBarChart(data: List<JobOffer>) {
        val jobCounts = data.groupingBy { it.position }.eachCount()

        val entries = jobCounts.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        // Buat warna berbeda untuk setiap pekerjaan
        val colors = jobCounts.entries.map {
            android.graphics.Color.rgb(
                (0..255).random(), // Red
                (0..255).random(), // Green
                (0..255).random()  // Blue
            )
        }

        val dataSet = BarDataSet(entries, "Jumlah Pekerjaan")
        dataSet.colors = colors // Set warna berbeda untuk setiap entri
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.description.isEnabled = false

        // Konfigurasikan sumbu X untuk tidak menampilkan label pekerjaan di bawah bar
        val xAxis = barChart.xAxis
        xAxis.setDrawLabels(false) // Hilangkan label pada sumbu X
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 12f

        // Konfigurasikan sumbu Y
        barChart.axisLeft.setDrawGridLines(true)
        barChart.axisRight.isEnabled = false

        // Konfigurasikan legenda untuk menampilkan nama pekerjaan
        val legend = barChart.legend
        legend.isEnabled = true
        legend.setCustom(jobCounts.entries.mapIndexed { index, entry ->
            LegendEntry(entry.key, Legend.LegendForm.SQUARE, 10f, 2f, null, colors[index])
        })
        legend.textSize = 12f
        legend.formSize = 10f
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.VERTICAL

        // Refresh chart untuk menampilkan data baru
        barChart.invalidate()
    }

    private fun navigateToJobDetail(jobId: Int) {
        val intent = Intent(this, JobDetailActivity::class.java)
        intent.putExtra("jobId", jobId)
        startActivity(intent)
    }
}
