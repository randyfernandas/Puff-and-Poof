package com.example.mcs_lab_project.model

data class Doll(
    var desc : String,
    var name : String,
    var size : String,
    var price : Int,
    var rating : Double,
    var imageLink : String,
    var dollId : Int
)

object Dolls{
    var listDolls : ArrayList<Doll> = ArrayList()
    fun addDoll(desc: String , name: String, size: String,price: Int, rating: Double,  imageLink: String)
    {
        val dollId = listDolls.size + 1
        listDolls.add(Doll(desc, name, size, price, rating, imageLink, dollId))
    }
}

