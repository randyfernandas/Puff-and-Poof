package com.example.mcs_lab_project.model

data class User(
    var username: String,
    var email: String,
    var password: String,
    var phoneNumber: String,
    var userId: Int,
    var gender: String
)

object Users { // Singleton object to hold the corresponding array list
    var loggedUsers: ArrayList<User> = ArrayList()
    fun registerUser(username: String, email: String, password: String, phoneNumber: String, userId: Int, gender: String) {
        val userId = loggedUsers.size + 1
        loggedUsers.add(User(username, email, password, phoneNumber, userId, gender))
    }
}



