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
import com.example.berberappointment.R
import com.example.berberappointment.databinding.ActivityBerberBinding
import com.google.firebase.database.FirebaseDatabase
import com.example.berberappointment.utils.Utils.Companion.MIN_PHONE_LENGTH

class Berber : AppCompatActivity() {
    private lateinit var design: ActivityBerberBinding
    private lateinit var build: AlertDialog
    private var firebase = FirebaseDatabase.getInstance()
    private var referenceBerber = firebase.getReference("CreateBerber")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design = DataBindingUtil.setContentView(this@Berber, R.layout.activity_berber)
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
            val shopName =
                designPopup.findViewById<EditText>(R.id.createPopupSN)
            val shopAddress =
                designPopup.findViewById<EditText>(R.id.createPopupSA)

            val berberName = name.text
            val berberLName = lastName.text
            val berberPhoneN = phoneNumber.text
            val berberShopN = shopName.text
            val berberShopA = shopAddress.text

            if (berberName.isNotBlank() && berberLName.isNotBlank() && berberPhoneN.isNotBlank() && berberShopN.isNotBlank() && berberShopA.isNotBlank()) {
                if (berberPhoneN.length < MIN_PHONE_LENGTH || !berberPhoneN.toString().startsWith("05")) {
                    Toast.makeText(this@Berber, "Invalid Phone Number. Phone Number Must Start With 05", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@Berber, "Berber Created", Toast.LENGTH_LONG).show()
                    createBerber(
                        berberName.toString(),
                        berberLName.toString(),
                        berberPhoneN.toString().toLong(),
                        berberShopN.toString(),
                        berberShopA.toString()
                    )

                    build.dismiss()
                }
            } else {
                Toast.makeText(this@Berber, "Berber Could not be Created", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun createBerber(
        berberName: String,
        berberLastN: String,
        berberPhoneN: Long,
        berberShopN: String,
        berberShopA: String,
    ) {
        val createBerber =
            CreateBerber(berberName, berberLastN, berberPhoneN, berberShopN, berberShopA)

        referenceBerber.push().setValue(createBerber)
    }

}