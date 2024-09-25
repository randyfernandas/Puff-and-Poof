

package com.example.mcs_lab_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mcs_lab_project.Adapter.DollsListAdapter
import com.example.mcs_lab_project.R
import com.example.mcs_lab_project.helper.DatabaseHelper
import com.example.mcs_lab_project.model.Doll

import org.json.JSONException

class HomeFragment : Fragment() {

    private lateinit var rvDollsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rvDollsList = view.findViewById(R.id.rvDollsList)
        rvDollsList.layoutManager = LinearLayoutManager(context)

        fetchDollsData()

        return view
    }

    private fun fetchDollsData() {
        val queue = Volley.newRequestQueue(requireContext())
        val url = "https://api.npoint.io/9d7f4f02be5d5631a664"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val dolls = parseDollsJson(response)
                rvDollsList.adapter = DollsListAdapter(DatabaseHelper(requireContext()).getDollsFromDatabase())
            },
            { error ->
                Log.e("HomeFragment", "Error fetching doll data: $error")
            }
        )

        queue.add(request)
    }

    private fun parseDollsJson(response: org.json.JSONObject): ArrayList<Doll> {
        val dolls = ArrayList<Doll>()
        try {
            val dollsArray = response.getJSONArray("dolls")

            for (i in 0 until dollsArray.length()) {
                val dollObject = dollsArray.getJSONObject(i)
                val doll = Doll(
                    name = dollObject.getString("name"),
                    imageLink = dollObject.getString("imageLink"),
                    size = dollObject.getString("size"),
                    price = dollObject.getInt("price"),
                    rating = dollObject.getDouble("rating"),
                    desc = dollObject.getString("desc"),
                    dollId = i
                )
                dolls.add(doll)
            }
            DatabaseHelper(requireContext()).insertDolls(dolls)
        } catch (e: JSONException) {
            Log.e("HomeFragment", "Error parsing doll's data: $e")
        }
        return dolls
    }



}


