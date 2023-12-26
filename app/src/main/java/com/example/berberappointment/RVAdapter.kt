package com.example.berberappointment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.berberappointment.berber.CreateBerber
import de.hdodenhof.circleimageview.CircleImageView

class RVAdapter(private val mContext: Context, private val berberList: List<CreateBerber>) :
    RecyclerView.Adapter<RVAdapter.BerberShopList>() {

    inner class BerberShopList(view: View) : RecyclerView.ViewHolder(view) {
        var shopImage: CircleImageView
        var shopName: TextView
        var updateButton: Button

        init {
            shopImage = view.findViewById(R.id.shopImage)
            shopName = view.findViewById(R.id.shopName)
            updateButton = view.findViewById(R.id.updateButton)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.BerberShopList {
        val design = LayoutInflater.from(mContext).inflate(R.layout.berber_list_card, parent, false)

        return BerberShopList(design)
    }

    override fun getItemCount(): Int {
        return berberList.size
    }

    override fun onBindViewHolder(holder: RVAdapter.BerberShopList, position: Int) {
        val berberShop = berberList[position]

        holder.shopName.text = berberShop.berberShopN

    }
}