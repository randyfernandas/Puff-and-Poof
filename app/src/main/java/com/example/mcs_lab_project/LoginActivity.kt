///*
//*
//* MOBILE COMMUNITY SOLUTION LAB. PROJECT
//* GROUP 1
//*
//* */

package com.example.mcs_lab_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mcs_lab_project.helper.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    // UI elements
    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        databaseHelper = DatabaseHelper(this)

        // Initialize UI elements
        usernameEditText = findViewById(R.id.etUsername)
        passwordEditText = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)

        // Login button click event
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validation
            var isValid = true
            if (username.isEmpty()) {
                isValid = false
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            }
            if (password.isEmpty()) {
                isValid = false
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            }

            if (isValid) {
                val foundUser = databaseHelper.UserValid(username, password)
                if (foundUser != null) {
                    val intent = Intent(this, OTPActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Navigate to Register Activity on button click
        val tvRegister = findViewById<TextView>(R.id.txtRegister)
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
