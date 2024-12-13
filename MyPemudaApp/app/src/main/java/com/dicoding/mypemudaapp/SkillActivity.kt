package com.dicoding.mypemudaapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SkillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skill)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        val skillContainer = findViewById<LinearLayout>(R.id.skillContainer)
        val btnLanjutkan = findViewById<Button>(R.id.btnLanjutkan)

        // Daftar semua skill yang tersedia
        val allSkills = listOf(
            "Figma", "Adobe XD", "Sketch", "HTML", "CSS", "Typography", "Color Theory",
            "Strategic Thinking", "Digital Marketing Expertise", "Market Research", "Software Architecture",
            "Design Patterns", "Java", "Golang", "C++", "C", "Python",
            "Testing Frameworks Selenium", "Testing Frameworks Appium", "Manual Testing",
            "Project Management Tool Asana", "Project Management Tool Trello", "Roadmap Planning",
            "Unified Modeling Language UML", "Entity Relationship Diagram ERD", "Excel",
            "Statistics", "Mathematics", "Algorithm", "Machine Learning Algorithms",
            "Data Science", "Artificial Intelligence", "PHP", "JavaScript", "Laravel",
            "React", "Angular", "VueJS", "NodeJS"
        )

        // Tampilkan hanya 10 skill pertama secara default
        val initialSkills = allSkills.take(10)

        // Function to populate the skills dynamically
        fun populateSkills(filteredSkills: List<String>) {
            skillContainer.removeAllViews()
            for (skill in filteredSkills) {
                val checkBox = CheckBox(this).apply {
                    text = skill
                    textSize = 16f
                    setPadding(16, 16, 16, 16)
                }
                skillContainer.addView(checkBox)
            }
        }

        // Populate initial skills
        populateSkills(initialSkills)

        // Handle Search Bar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                val filteredSkills = if (query.isEmpty()) {
                    initialSkills // Tampilkan 10 skill pertama jika query kosong
                } else {
                    allSkills.filter { it.contains(query, ignoreCase = true) }
                }
                populateSkills(filteredSkills)
            }
        })

        // Handle Back Button
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Handle Lanjutkan Button
        btnLanjutkan.setOnClickListener {
            val selectedSkills = mutableListOf<String>()
            for (i in 0 until skillContainer.childCount) {
                val checkBox = skillContainer.getChildAt(i) as CheckBox
                if (checkBox.isChecked) {
                    selectedSkills.add(checkBox.text.toString())
                }
            }

            if (selectedSkills.isEmpty()) {
                // Tampilkan pesan jika tidak ada skill yang dipilih
                Toast.makeText(this, "Pilih minimal 1 keahlian!", Toast.LENGTH_SHORT).show()
            } else {
                // Gabungkan skill yang dipilih menjadi string dengan format yang dapat digunakan
                val selectedSkillsString = selectedSkills.joinToString(", ")

                // Pindah ke halaman HomeActivity
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("selectedSkills", selectedSkillsString) // Kirim skill yang dipilih
                }
                startActivity(intent)
                finish() // Tutup SkillActivity
            }
        }
    }
}
