package com.example.berberappointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.berberappointment.berber.CreateBerber
import com.example.berberappointment.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private var firebase = FirebaseDatabase.getInstance()
    private var referenceBerber = firebase.getReference("CreateBerber")
    private lateinit var design: ActivityMainBinding
    private lateinit var berberShopList: ArrayList<CreateBerber>
    private lateinit var adapter: RVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)


        getBerberList()

    }

    private fun getBerberList() {
        design.recyclerView1.setHasFixedSize(true)
        design.recyclerView1.layoutManager = LinearLayoutManager(this@MainActivity)

        referenceBerber.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val tempBerberShopList = ArrayList<CreateBerber>()
                for (child in snapshot.children) {
                    val berber = child.getValue(CreateBerber::class.java)
                    if (berber != null) {
                        tempBerberShopList.add(berber)
                    }
                }
                berberShopList = tempBerberShopList
                adapter = RVAdapter(this@MainActivity, berberShopList)
                design.recyclerView1.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}