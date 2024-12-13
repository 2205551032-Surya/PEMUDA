package com.dicoding.mypemudaapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false // Status visibilitas password
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE)

        val emailField = findViewById<EditText>(R.id.emailField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val togglePasswordVisibility = findViewById<ImageView>(R.id.togglePasswordVisibility)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupLink = findViewById<TextView>(R.id.signupLink)
        val ingatAkuCheckbox = findViewById<CheckBox>(R.id.rememberMeCheckBox)

        // Cek apakah sudah ada data email yang disimpan
        val email = sharedPreferences.getString("email", "")
        if (email != "") {
            // Isi field email dengan data yang disimpan
            emailField.setText(email)
        }

        // Cek apakah fitur ingat aku sudah diaktifkan
        val ingatAku = sharedPreferences.getBoolean("ingat_aku", false)
        if (ingatAku) {
            ingatAkuCheckbox.isChecked = true
        }

        // Handle Password Visibility Toggle
        togglePasswordVisibility.setOnClickListener {
            if (isPasswordVisible) {
                passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off) // Ganti ke ikon mata tertutup
            } else {
                passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility) // Ganti ke ikon mata terbuka
            }
            isPasswordVisible = !isPasswordVisible // Toggle status visibilitas
            passwordField.setSelection(passwordField.text.length) // Pindahkan cursor ke akhir teks
        }

        // Handle Signup Link
        signupLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Handle Login Button
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // Simpan data email ke SharedPreferences
            sharedPreferences.edit().putString("email", email).apply()

            // Simpan status centang fitur ingat aku ke SharedPreferences
            sharedPreferences.edit().putBoolean("ingat_aku", ingatAkuCheckbox.isChecked).apply()

            val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
            retro.login(email, password).enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(this@LoginActivity, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        if (userResponse != null) {
                            val message = userResponse.message
                            if (message == "Login successful") {
                                // Arahkan pengguna ke halaman RegencyActivity
                                val intent = Intent(this@LoginActivity, RegencyActivity::class.java)
                                startActivity(intent)
                                finish() // Tutup activity login setelah berpindah
                            } else {
                                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        when (response.code()) {
                            401 -> Toast.makeText(this@LoginActivity, "Invalid password", Toast.LENGTH_SHORT).show()
                            404 -> Toast.makeText(this@LoginActivity, "User    not found", Toast.LENGTH_SHORT).show()
                            500 -> Toast.makeText(this@LoginActivity, "Internal server error", Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}