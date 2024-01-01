package com.example.berberappointment.berber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.berberappointment.R
import com.example.berberappointment.databinding.ActivityBerberBinding
import com.example.berberappointment.register.Register
import com.google.firebase.database.FirebaseDatabase
import com.example.berberappointment.utils.Utils.Companion.sha256
import com.example.berberappointment.utils.Utils.Companion.MIN_PHONE_LENGTH
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class Berber : AppCompatActivity() {
    private lateinit var design: ActivityBerberBinding
    private lateinit var build: AlertDialog
    private lateinit var createBerberList: ArrayList<CreateBerber>
    private lateinit var adapter: RVAdapter
    private var firebase = FirebaseDatabase.getInstance()
    private var referenceBerber = firebase.getReference("CreateBerber")
    private var referenceRegister = firebase.getReference("Register")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design = DataBindingUtil.setContentView(this@Berber, R.layout.activity_berber)

        getBerberList()
        createBerberPopup()

        //Delete ve Update Buttonlarını Çalışır Hale getir
    }

    private fun createBerber(
        berberName: String,
        berberLastN: String,
        berberPhoneN: Long,
        berberPassword: String,
        berberShopN: String,
        berberShopA: String,
    ): CreateBerber {
        val createBerber =
            CreateBerber(
                berberName,
                berberLastN,
                berberPhoneN,
                berberPassword,
                berberShopN,
                berberShopA
            )
        referenceBerber.push().setValue(createBerber)
        return createBerber
    }

    private fun createBerberPopup() {

        val builder = AlertDialog.Builder(this@Berber)

        val designPopup =
            LayoutInflater.from(this@Berber).inflate(R.layout.berber_creat_popup, null)
        builder.setView(designPopup)

        val button = designPopup.findViewById<Button>(R.id.createPopupBTN)
        val closeButton = designPopup.findViewById<ImageView>(R.id.createPopupCBTN)

        build = builder.create()
        design.openPopupFAB.setOnClickListener {
            build.show()

            closeButton.setOnClickListener {
                build.dismiss()
            }
        }
        button.setOnClickListener {
            val name =
                designPopup.findViewById<EditText>(R.id.createPopupN)
            val lastName =
                designPopup.findViewById<EditText>(R.id.createPopupLN)
            val phoneNumber =
                designPopup.findViewById<EditText>(R.id.createPopupPN)
            val password =
                designPopup.findViewById<EditText>(R.id.createPopupPS)
            val shopName =
                designPopup.findViewById<EditText>(R.id.createPopupSN)
            val shopAddress =
                designPopup.findViewById<EditText>(R.id.createPopupSA)

            val berberName = name.text
            val berberLName = lastName.text
            val berberPhoneN = phoneNumber.text
            val berberPassword = password.text
            val berberShopN = shopName.text
            val berberShopA = shopAddress.text

            if (berberName.isNotBlank() && berberLName.isNotBlank() && berberPhoneN.isNotBlank() && berberShopN.isNotBlank() && berberShopA.isNotBlank()) {
                if (berberPhoneN.length < MIN_PHONE_LENGTH || !berberPhoneN.toString()
                        .startsWith("05")
                ) {
                    Toast.makeText(
                        this@Berber,
                        "Invalid Phone Number. Phone Number Must Start With 05",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this@Berber, "Berber Created", Toast.LENGTH_SHORT).show()
                    val hashedPassword = sha256(berberPassword.toString()) //SHA-256
                    val berber = createBerber(
                        berberName.toString(),
                        berberLName.toString(),
                        berberPhoneN.toString().toLong(),
                        hashedPassword,
                        berberShopN.toString(),
                        berberShopA.toString()
                    )
                    val register = Register.fromCreateBerber(berber)
                    referenceRegister.push().setValue(register)
                    build.dismiss()

                    berberName.clear()
                    berberLName.clear()
                    berberPhoneN.clear()
                    berberShopN.clear()
                    berberShopA.clear()
                }
            } else {
                Toast.makeText(this@Berber, "Berber Could not be Created", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getBerberList() {
        design.recyclerView.setHasFixedSize(true)
        design.recyclerView.layoutManager = LinearLayoutManager(this@Berber)

        referenceBerber.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val tempBerberList = ArrayList<CreateBerber>()
                for (child in snapshot.children) {
                    val berber = child.getValue(CreateBerber::class.java)
                    if (berber != null) {
                        tempBerberList.add(berber)
                    }
                }
                createBerberList = tempBerberList
                adapter = RVAdapter(this@Berber, createBerberList)
                design.recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
