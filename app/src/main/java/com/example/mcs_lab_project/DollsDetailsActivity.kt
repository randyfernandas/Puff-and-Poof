package com.example.mcs_lab_project

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mcs_lab_project.helper.DatabaseHelper
import com.example.mcs_lab_project.model.Transaction
import com.example.mcs_lab_project.model.Transactions
import com.example.mcs_lab_project.model.User

import java.text.SimpleDateFormat
import java.util.*

lateinit var dollNameTextView: TextView
lateinit var dollSizeTextView: TextView
lateinit var dollRatingTextView: TextView
lateinit var dollPriceTextView: TextView
lateinit var dollDescTextView: TextView
lateinit var dollCoverImageView: ImageView
lateinit var backbtn : ImageButton
lateinit var etBuyDoll: EditText
lateinit var removebtn: ImageButton
lateinit var addbtn: ImageButton
lateinit var buybtn : Button

//initial doll quantity
var quantity = 1

val TRANSACTION_ADDED = "transaction_added"

class DollsDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dolls_details)

        dollNameTextView = findViewById(R.id.txtDollDNameField)
        dollSizeTextView = findViewById(R.id.txtDollDSizeField)
        dollRatingTextView = findViewById(R.id.txtDollDRatingField)
        dollPriceTextView = findViewById(R.id.txtDollDPriceField)
        dollDescTextView = findViewById(R.id.txtDollDDescField)
        dollCoverImageView = findViewById(R.id.imgDollDetail)
        backbtn = findViewById(R.id.back_button)

        etBuyDoll = findViewById(R.id.etBuyDoll)
        removebtn = findViewById(R.id.removebtn)
        addbtn = findViewById(R.id.addbtn)

        etBuyDoll.setText(quantity.toString())

        // remove button click
        removebtn.setOnClickListener {
            if(quantity == 1)
            {
                Toast.makeText(this,"Doll amount cannot be zero!",Toast.LENGTH_SHORT).show()
            }
            if (quantity > 1) {
                quantity--
                etBuyDoll.setText(quantity.toString())
            }
        }

        // add button click
        addbtn.setOnClickListener {
            quantity++
            etBuyDoll.setText(quantity.toString())
        }


        val intent = intent

        val coverUrl = intent.getStringExtra("cover") // Get image URL

        if (coverUrl != null) {
            Glide.with(this)
                .load(coverUrl)
                .placeholder(R.drawable.kingsley)
                .error(R.drawable.gloria)
                .into(dollCoverImageView)
        }
        if (intent.hasExtra("name")) {
            dollNameTextView.text = intent.getStringExtra("name")
        }
        if (intent.hasExtra("size")) {
            dollSizeTextView.text = intent.getStringExtra("size")
        }
        if (intent.hasExtra("rating")) {
            val rating = intent.getDoubleExtra("rating", 0.0)
            dollRatingTextView.text = rating.toString()
        }
        if (intent.hasExtra("price")) {
            val priceText = intent.getIntExtra("price", 0)
            dollPriceTextView.text = priceText.toString()
        }
        if (intent.hasExtra("desc")) {
            dollDescTextView.text = intent.getStringExtra("desc")
        }

        val dollId = intent.getIntExtra("id", 0)




        val buybtn = findViewById<Button>(R.id.buybtn)
        buybtn.setOnClickListener {
            val dollName = dollNameTextView.text.toString()
            val dollPriceText = dollPriceTextView.text.toString()
            val dollQuantity = quantity
            val dollPrice = quantity * dollPriceText.toInt()
            val currentDate = SimpleDateFormat("dd-MM-yyyy").format(Date()) as String
            val user = getLoggedInUser()
            val userId = user.userId

            if (coverUrl != null) {
                Transactions.addTransactions(coverUrl, dollQuantity, currentDate, dollPrice, dollName, userId, dollId)
            }
            val newTransactionId = System.currentTimeMillis().toInt()
            val transaction = coverUrl?.let { it1 ->
                Transaction(
                    transactionId = newTransactionId,
                    dollBuyImg = it1,
                    transactionDate = currentDate,
                    dollName = dollName,
                    transactionQty = quantity,
                    transactionPrice = dollPrice,
                    dollId = dollId,
                    userId = user.userId
                )
            }

            val dbHelper = DatabaseHelper(this)
            if (transaction != null) {
                dbHelper.insertTransaction(transaction)
            }
            val intentNavigate = Intent(this, MenuActivity::class.java)
            startActivity(intentNavigate)


        }


        //back button
        backbtn.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

    }
    fun getLoggedInUser(): User {
        val sharedPref = getSharedPreferences("user_login", MODE_PRIVATE)
        val username = sharedPref.getString("username", null)
        val password = sharedPref.getString("password", null)

        if (username != null && password != null) {
            val db = DatabaseHelper(this)
            val user = db.UserValid(username, password)

            return user ?: User(username = "", email = "", password = "", phoneNumber = "", userId = 0, gender = "")
        } else {
            // No logged-in user found
            return User(username = "", email = "", password = "", phoneNumber = "", userId = 0, gender = "")
        }
    }
}





