package com.example.mcs_lab_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mcs_lab_project.helper.DatabaseHelper


var otp = 1234

class OTPActivity : AppCompatActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    private val SEND_SMS_PERMISSION_CODE = 100
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        messageEditText = findViewById(R.id.messageEdit)
        sendButton = findViewById(R.id.sendBtn)
        dbHelper = DatabaseHelper(this)

        val users = dbHelper.getAllValidUsers()

        if (users.isEmpty()) {
            Toast.makeText(this, "No users found in database", Toast.LENGTH_SHORT).show()
            return
        }

        val firstUser = users[0]
        val phoneNumber = firstUser.phoneNumber

        if (phoneNumber.isNullOrEmpty()) {
            Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show()
            return
        }

        checkSendSmsPermission()
        requestSendSmsPermission()
        sendOTP(phoneNumber)

        sendButton.setOnClickListener {
            val enteredOTP = messageEditText.text.toString().toInt()

            Handler(mainLooper).postDelayed({
                if (enteredOTP == otp) {
                    Toast.makeText(this, "OTP verification successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MenuActivity::class.java))
                } else {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }, 2000)
        }
    }

    private fun checkSendSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSendSmsPermission() {
        requestPermissions(arrayOf(Manifest.permission.SEND_SMS), SEND_SMS_PERMISSION_CODE)
    }

    private fun sendOTP(phoneNumber: String) {
        otp = (Math.random() * 9000).toInt() + 1000
        val message = "Your OTP is: $otp"

        if (checkSendSmsPermission()) {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "OTP sent to your phone number", Toast.LENGTH_SHORT).show()
        }
    }
}
