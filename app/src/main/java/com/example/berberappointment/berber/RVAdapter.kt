package com.example.berberappointment.berber

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.berberappointment.R
import com.example.berberappointment.berber.CreateBerber as FirebaseCreateBerber

class RVAdapter(private val mContext: Context, private val berberList: List<FirebaseCreateBerber>) :
    RecyclerView.Adapter<RVAdapter.CreateBerber>() {

    inner class CreateBerber(view: View) : RecyclerView.ViewHolder(view) {

        var shopCName: TextView
        var shopCOwner: TextView
        var updateButton: Button
        var deleteButton: Button

        init {
            shopCName = view.findViewById(R.id.shopCName)
            shopCOwner = view.findViewById(R.id.shopCOwner)
            updateButton = view.findViewById(R.id.updateButton)
            deleteButton = view.findViewById(R.id.deleteButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateBerber {

        val design = LayoutInflater.from(mContext).inflate(R.layout.berber_creat,parent,false)

        return CreateBerber(design)
    }

    override fun getItemCount(): Int {
        return berberList.size
    }

    override fun onBindViewHolder(holder: CreateBerber, position: Int) {
        val berber = berberList[position]

        holder.shopCName.text = berber.berberShopN
        holder.shopCOwner.text = berber.berberName

    }
}