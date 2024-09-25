package com.example.mcs_lab_project.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mcs_lab_project.Adapter.TransactionListAdapter
import com.example.mcs_lab_project.R
import com.example.mcs_lab_project.TRANSACTION_ADDED
import com.example.mcs_lab_project.helper.DatabaseHelper

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class TransactionFragment : Fragment() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == TRANSACTION_ADDED) {
                val adapter = requireView().findViewById<RecyclerView>(R.id.rvTransactionsList)?.adapter as TransactionListAdapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_transaction, container, false)
        lateinit var rvTransactionsList: RecyclerView

        rvTransactionsList = view.findViewById(R.id.rvTransactionsList)

        rvTransactionsList.layoutManager = LinearLayoutManager(context)

        rvTransactionsList.adapter = TransactionListAdapter(DatabaseHelper(requireContext()).getTransactions())

        return view

    }

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(receiver, IntentFilter(TRANSACTION_ADDED))
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(receiver)
    }
}