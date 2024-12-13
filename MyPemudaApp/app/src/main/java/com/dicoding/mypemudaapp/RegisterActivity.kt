package com.dicoding.mypemudaapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private var isPasswordVisible = false // Status visibilitas password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailField = findViewById<EditText>(R.id.emailField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val togglePasswordVisibility = findViewById<ImageView>(R.id.togglePasswordVisibility)
        val registerButton = findViewById<Button>(R.id.signupButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        // Handle Password Visibility Toggle
        togglePasswordVisibility.setOnClickListener {
            if (isPasswordVisible) {
                // Jika password sedang terlihat, ubah menjadi tidak terlihat
                passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off) // Ganti ke ikon mata tertutup
            } else {
                // Jika password sedang tidak terlihat, ubah menjadi terlihat
                passwordField.inputType = InputType.TYPE_CLASS_TEXT
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility) // Ganti ke ikon mata terbuka
            }
            isPasswordVisible = !isPasswordVisible // Toggle status visibilitas
            passwordField.setSelection(passwordField.text.length) // Pindahkan cursor ke akhir teks
        }

        // Handle Login Link
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Handle Register Button
        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
                retro.register(email, password).enqueue(object : Callback<UserResponse> {
                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Log.e("Error", t.message.toString())
                        Toast.makeText(this@RegisterActivity, "Pendaftaran gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "Pendaftaran berhasil! Silakan login.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Pendaftaran gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }
}