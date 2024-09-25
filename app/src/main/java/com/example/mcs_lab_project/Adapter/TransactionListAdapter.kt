package com.example.mcs_lab_project.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcs_lab_project.MenuActivity
import com.example.mcs_lab_project.R
import com.example.mcs_lab_project.helper.DatabaseHelper
import com.example.mcs_lab_project.model.Transaction
import com.example.mcs_lab_project.model.Transactions.listTransactions


class TransactionListAdapter(var dataset: ArrayList<Transaction>) : RecyclerView.Adapter<TransactionListAdapter.Holder>(){
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var transactions: Transaction

        var imgDollBuy : ImageView = itemView.findViewById(R.id.imgDollBuy)
        var txtDollBuy : TextView = itemView.findViewById(R.id.txtDollBuy)
        var txtDollBuyQty: TextView = itemView.findViewById(R.id.txtDollBuyQty)
        var txtDollBuyPrice: TextView = itemView.findViewById(R.id.txtDollBuyPrice)

        fun bind(transaction: Transaction)
        {
            this.transactions = transaction


            Glide.with(itemView.context)
                .load(transaction.dollBuyImg)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(imgDollBuy)
            txtDollBuy.text = transactions.dollName
            txtDollBuyQty.text = transactions.transactionQty.toString()
            txtDollBuyPrice.text = transactions.transactionPrice.toString()



            val updateButton = itemView.findViewById<Button>(R.id.UpdateDollBtn)
            updateButton.setOnClickListener {
                navigateToDollDetails(transaction.dollName, transaction.transactionQty)
            }

            val deleteButton = itemView.findViewById<Button>(R.id.DeleteDollBtn)
            deleteButton.setOnClickListener {
                removeTransaction(transaction)

            }

        }
        fun navigateToDollDetails(dollName: String, transactionQty: Int) {
            val intent = Intent(itemView.context, MenuActivity::class.java)
            itemView.context.startActivity(intent)
        }

        fun removeTransaction(transaction: Transaction) {

            listTransactions.remove(transaction)
            val intent = Intent(itemView.context, MenuActivity::class.java)
            itemView.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_transaction_item,parent,false)
        return TransactionListAdapter.Holder(view)
    }


    override fun getItemCount(): Int {
        return listTransactions.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var transactions = dataset.get(position)
        holder.bind(transactions)
    }
}

