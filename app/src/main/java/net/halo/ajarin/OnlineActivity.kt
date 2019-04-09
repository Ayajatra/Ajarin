package net.halo.ajarin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_online.*

class OnlineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online)

        val pelajaran = intent.getStringArrayListExtra("pelajaran")
        txtPelajaran.text = pelajaran.joinToString(", ")

        val adapterWaktu = ArrayAdapter.createFromResource(this, R.array.waktu_array, android.R.layout.simple_spinner_item)
        val adapterJangka = ArrayAdapter.createFromResource(this, R.array.lama_array, android.R.layout.simple_spinner_item)
        adapterWaktu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterJangka.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerWaktu.adapter = adapterWaktu
        spinnerJangka.adapter = adapterJangka

        val guruList = ArrayList<String>()
        val guruListId = ArrayList<String>()
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("profesion", "Guru")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    guruListId.add(document.id)
                    guruList.add(document.get("name").toString())
                }

                val guruAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, guruList)
                guruAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerGuru.adapter = guruAdapter
            }

        btnKirim.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val hari = ArrayList<String>()
            if (cbSen.isChecked) {
                hari.add("senin")
            }
            if (cbSel.isChecked) {
                hari.add("selasa")
            }
            if (cbRab.isChecked) {
                hari.add("rabu")
            }
            if (cbKam.isChecked) {
                hari.add("kamis")
            }
            if (cbJum.isChecked) {
                hari.add("jumat")
            }
            if (cbSab.isChecked) {
                hari.add("sabtu")
            }
            if (cbMin.isChecked) {
                hari.add("minggu")
            }

            val langgananOnline = HashMap<String, Any>()
            langgananOnline["murid id"] = auth.currentUser!!.uid
            langgananOnline["guru id"] = guruListId[spinnerGuru.selectedItemPosition]
            langgananOnline["waktu"] = spinnerWaktu.selectedItem.toString()
            langgananOnline["jangka"] = spinnerJangka.selectedItem.toString()
            langgananOnline["harga"] = txtHarga.text.toString()
            langgananOnline["hari"] = hari
            langgananOnline["mata pelajaran"] = txtPelajaran.text.toString().split(", ")

            db.collection("langganan online").add(langgananOnline)
                .addOnSuccessListener {
                    Toast.makeText(this, "Permintaan Anda telah terkirim", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CoreActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
        }
    }
}