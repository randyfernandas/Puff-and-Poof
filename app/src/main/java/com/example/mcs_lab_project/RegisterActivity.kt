package com.example.mcs_lab_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.mcs_lab_project.helper.DatabaseHelper
import com.example.mcs_lab_project.model.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    //setup variables for register needs
    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var phoneNumberEditText: EditText
    lateinit var registerButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        databaseHelper = DatabaseHelper(this)

        //initialize variables
        usernameEditText = findViewById(R.id.etUsername)
        passwordEditText = findViewById(R.id.etPassword)
        emailEditText = findViewById(R.id.etEmail)
        phoneNumberEditText = findViewById(R.id.etPhoneNumber)
        registerButton = findViewById(R.id.btnRegister)

        //set btn events
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            // validations
            var isValid = true
            if (username.isEmpty()) {
                isValid = false
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            }
            if (password.isEmpty()) {
                isValid = false
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (password.length < 8) {
                isValid = false
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            }
            if (email.isEmpty()) {
                isValid = false
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (!email.endsWith("@puff.com")) {
                isValid = false
                Toast.makeText(this, "Email must end with @puff.com", Toast.LENGTH_SHORT).show()
            }
            if (phoneNumber.isEmpty()) {
                isValid = false
                Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (phoneNumber.length < 11 || phoneNumber.length > 13) {
                isValid = false
                Toast.makeText(this, "Phone number must be between 11 and 13 characters", Toast.LENGTH_SHORT).show()
            }

            if (isValid) {
                val usernameExists = databaseHelper.UserExist(username)
                if (usernameExists) {
                    isValid = false
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            }

            if (isValid) {
                val userId = databaseHelper.getUsersCount() + 1  // Generate ID based on existing users
                val gender = if (findViewById<RadioButton>(R.id.rbtnMale).isChecked) "Male" else "Female"

                // Create user object
                val user = User(username,  email, password, phoneNumber,userId, gender)

                // Register user in database
                databaseHelper.insertUser(user)

                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val tvLogin = findViewById<TextView>(R.id.txtLogin)
        tvLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }


}
