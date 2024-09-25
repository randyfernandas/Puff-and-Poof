package com.example.mcs_lab_project.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mcs_lab_project.ClosingActivity
import com.example.mcs_lab_project.LoginActivity
import com.example.mcs_lab_project.MapsActivity
import com.example.mcs_lab_project.R
import com.example.mcs_lab_project.helper.DatabaseHelper
import com.example.mcs_lab_project.model.User

class ProfileFragment : Fragment() {

    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var loggedOutButton: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        usernameTextView = view.findViewById(R.id.txtUsernameField)
        emailTextView = view.findViewById(R.id.txtEmailField)
        phoneNumberTextView = view.findViewById(R.id.txtPhoneNumberField)
        loggedOutButton = view.findViewById(R.id.btnLogout)

        databaseHelper = DatabaseHelper(requireActivity())

        // Get all valid users
        val users = databaseHelper.getAllValidUsers()

        if (users.isNotEmpty()) {
            val firstUser = users[0] // Assuming you want to display the first user
            usernameTextView.text = firstUser.username
            emailTextView.text = firstUser.email
            phoneNumberTextView.text = firstUser.phoneNumber
        } else {
            // Handle case where no users are found
            usernameTextView.text = "No users found"
            emailTextView.text = ""
            phoneNumberTextView.text = ""
        }

        loggedOutButton.setOnClickListener {
            val intent = Intent(requireActivity(), ClosingActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
