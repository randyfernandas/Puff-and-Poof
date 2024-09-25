package com.example.mcs_lab_project.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcs_lab_project.DollsDetailsActivity
import com.example.mcs_lab_project.R
import com.example.mcs_lab_project.model.Doll

class DollsListAdapter(var dolls: ArrayList<Doll>) : RecyclerView.Adapter<DollsListAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var doll: Doll

        var imgDoll: ImageView = itemView.findViewById(R.id.imgDoll)
        var txtDollName: TextView = itemView.findViewById(R.id.txtDollName)

        fun bind(doll: Doll) {
            this.doll = doll

            Glide.with(itemView.context)
                .load(doll.imageLink)
                .placeholder(R.drawable.img_placeholder) // placeholder image
                .error(R.drawable.gloria)
                .into(imgDoll)

            txtDollName.text = doll.name

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DollsDetailsActivity::class.java)
                intent.putExtra("cover", doll.imageLink)
                intent.putExtra("name", doll.name)
                intent.putExtra("size", doll.size)
                intent.putExtra("rating", doll.rating)
                intent.putExtra("price", doll.price)
                intent.putExtra("desc", doll.desc)
                intent.putExtra("id", doll.dollId)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_doll_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return dolls.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val doll = dolls[position]
        holder.bind(doll)
    }
}
