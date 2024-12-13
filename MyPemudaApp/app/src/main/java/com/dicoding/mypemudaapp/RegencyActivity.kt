package com.dicoding.mypemudaapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegencyActivity : AppCompatActivity() {
    // Map untuk menyimpan hubungan antara id dan nama regency
    private val allRegions = mapOf(
        1 to "Denpasar",
        2 to "Bangli",
        3 to "Badung",
        4 to "Klungkung",
        5 to "Gianyar",
        6 to "Jembrana",
        7 to "Karangasem",
        8 to "Buleleng",
        9 to "Tabanan"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regency)

        val searchBar = findViewById<EditText>(R.id.searchBar)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val btnLanjutkan = findViewById<Button>(R.id.btnLanjutkan)

        // Function to populate the RadioGroup with a filtered list
        fun populateRadioGroup(filteredRegions: Map<Int, String>) {
            radioGroup.removeAllViews() // Clear existing radio buttons
            for ((id, region) in filteredRegions) {
                val radioButton = RadioButton(this).apply {
                    text = region
                    tag = id // Simpan id di tag RadioButton
                    textSize = 16f
                }
                radioGroup.addView(radioButton)
            }
        }

        // Initial population of all regions
        populateRadioGroup(allRegions)

        // Add a TextWatcher to searchBar to filter the regions dynamically
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                val filteredRegions = if (query.isEmpty()) {
                    allRegions
                } else {
                    allRegions.filter { it.value.contains(query, ignoreCase = true) }
                }
                populateRadioGroup(filteredRegions)
            }
        })

        btnLanjutkan.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                if (selectedRadioButton != null) {
                    val selectedId = selectedRadioButton.tag as Int // Ambil id dari tag
                    val selectedRegion = selectedRadioButton.text.toString()

                    // Pindah ke SkillActivity
                    val intent = Intent(this, SkillActivity::class.java)
                    intent.putExtra("selectedRegionId", selectedId) // Kirim id regency
                    intent.putExtra("selectedRegionName", selectedRegion) // Kirim nama regency
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Terjadi kesalahan pada pemilihan daerah.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Pilih salah satu daerah terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
