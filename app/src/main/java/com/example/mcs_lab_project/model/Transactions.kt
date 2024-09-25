package com.example.mcs_lab_project.model

data class Transaction(
    var dollBuyImg : String,
    var transactionId: Int,
    var transactionQty : Int,
    var transactionDate: String,
    var transactionPrice: Int,
    var dollName : String,
    val userId : Int,
    val dollId : Int
)

object Transactions {
    var listTransactions : ArrayList<Transaction> = ArrayList()

    fun addTransactions(dollBuyImg: String, transactionQty: Int, transactionDate: String,transactionPrice: Int, dollName: String, userId: Int, dollId: Int)
    {
        val transactionId = listTransactions.size + 1
        listTransactions.add(Transaction(dollBuyImg, transactionId,transactionQty,transactionDate,transactionPrice,dollName, userId, dollId))
    }
}
