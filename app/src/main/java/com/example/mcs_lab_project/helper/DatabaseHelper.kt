package com.example.mcs_lab_project.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mcs_lab_project.model.Doll
import com.example.mcs_lab_project.model.Transaction
import com.example.mcs_lab_project.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "doll.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateDoll = "CREATE TABLE IF NOT EXISTS Doll (" +
                "dollId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "imgLink TEXT," +
                "dollName TEXT," +
                "size TEXT," +
                "rating DOUBLE," +
                "description TEXT," +
                "price INTEGER" +
                ")"
        db?.execSQL(queryCreateDoll)

        val queryCreateUser = "CREATE TABLE IF NOT EXISTS User (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "password TEXT," +
                "email TEXT," +
                "gender TEXT," +
                "phoneNumber INTEGER" +
                ")"
        db?.execSQL(queryCreateUser)

        val queryCreateTransaction = "CREATE TABLE IF NOT EXISTS Transactions (" +
                "transactionId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dollBuyImg TEXT," +
                "transactionDate DATE," +
                "dollName TEXT," +
                "transactionQty INTEGER," +
                "transactionPrice INTEGER," +
                // Foreign key for Doll table
                "dollId INTEGER REFERENCES Doll(dollId) ON DELETE CASCADE," +
                // Foreign key for User table
                "userId INTEGER REFERENCES User(userId) ON DELETE CASCADE"  +
                ")"

        db?.execSQL(queryCreateTransaction)
    }
    // Doll
    fun insertDolls(dolls: ArrayList<Doll>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (doll in dolls) {
                val values = ContentValues().apply {
                    put("imgLink", doll.imageLink)
                    put("dollName", doll.name)
                    put("size", doll.size)
                    put("rating", doll.rating)
                    put("description", doll.desc)
                    put("price", doll.price)
                }
                db.insert("Doll", null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }


    fun getDollsFromDatabase(): ArrayList<Doll> {
        val dolls = ArrayList<Doll>()
        val db = readableDatabase
        val query = "SELECT * FROM Doll"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {

                val doll = Doll(desc ="", name =" ", size =" ", price=0, rating=0.0," ", dollId=0)
                doll.dollId = cursor.getInt(cursor.getColumnIndexOrThrow("dollId"))
                doll.imageLink = cursor.getString(cursor.getColumnIndexOrThrow("imgLink"))
                doll.name = cursor.getString(cursor.getColumnIndexOrThrow("dollName"))
                doll.price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                doll.desc = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                doll.size = cursor.getString(cursor.getColumnIndexOrThrow("size"))
                doll.rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating"))
                dolls.add(doll)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return dolls
    }

    // User
    fun insertUser (user: User){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", user.username)
            put("password", user.password)
            put("email", user.email)
            put("gender", user.gender)
            put("phoneNumber", user.phoneNumber)
        }
        db.insert("User", null, values)
        db.close()
    }

    //get user's count
    fun getUsersCount(): Int {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM User"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val count = if (cursor.count > 0) cursor.getInt(0) else 0
        cursor.close()
        db.close()
        return count
    }


    fun UserExist(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM User WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun UserValid(username: String, password: String): User? {
        val db = readableDatabase
        val query = "SELECT * FROM User WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val user: User?

        if (cursor.count > 0) {
            cursor.moveToFirst()
            user = User(
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                gender = cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
            )
        } else {
            user = null
        }

        cursor.close()
        db.close()
        return user
    }

    fun getAllValidUsers(): ArrayList<User> {
        val users = ArrayList<User>()
        val db = readableDatabase
        val query = "SELECT * FROM User"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                val user = User(
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    gender = cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password")), // Include password for display (not recommended for security reasons)
                    phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return users
    }


    //    Transaction
// Function to insert transaction
    fun insertTransaction(transaction: Transaction) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("dollBuyImg", transaction.dollBuyImg)
            put("dollName", transaction.dollName)
            put("transactionDate", transaction.transactionDate)
            put("transactionQty", transaction.transactionQty)
            put("transactionPrice", transaction.transactionPrice)
            put("dollId", transaction.dollId) // Foreign key for Doll table
            put("userId", transaction.userId) // Foreign key for User table
        }
        db.insert("Transactions", null, values)
        db.close()
    }

    fun deleteTransaction(transactionId: Int) {
        val db = writableDatabase
        db.delete("Transactions", "transactionId = ?", arrayOf(transactionId.toString()))
        db.close()
    }

    // Get transactions from database
    fun getTransactions(): ArrayList<Transaction> {
        val transactions = ArrayList<Transaction>()
        val db = readableDatabase
        val query = "SELECT * FROM Transactions"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                val transaction = Transaction(
                    transactionId = cursor.getInt(cursor.getColumnIndexOrThrow("transactionId")),
                    dollBuyImg = cursor.getString(cursor.getColumnIndexOrThrow("dollBuyImg")),
                    transactionDate = cursor.getString(cursor.getColumnIndexOrThrow("transactionDate")),
                    dollName = cursor.getString(cursor.getColumnIndexOrThrow("dollName")),
                    transactionQty = cursor.getInt(cursor.getColumnIndexOrThrow("transactionQty")),
                    transactionPrice = cursor.getInt(cursor.getColumnIndexOrThrow("transactionPrice")),
                    dollId = cursor.getInt(cursor.getColumnIndexOrThrow("dollId")),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"))
                )
                transactions.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return transactions
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS User")
        db?.execSQL("DROP TABLE IF EXISTS Doll")
        db?.execSQL("DROP TABLE IF EXISTS Transactions")

        onCreate(db)
    }

}