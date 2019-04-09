package net.halo.ajarin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    var profesi = ""
    var pendidikan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val adapterProfesi = ArrayAdapter.createFromResource(this, R.array.profesi_array, android.R.layout.simple_spinner_item)
        val adapterPendidikan = ArrayAdapter.createFromResource(this, R.array.pendidikan_array, android.R.layout.simple_spinner_item)
        adapterProfesi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterPendidikan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProfesi.adapter = adapterProfesi
        spinnerPendidikan.adapter = adapterPendidikan

        spinnerProfesi.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nothing for now
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                checkSelectedItem()
                profesi = spinnerProfesi.selectedItem.toString()
            }

        }

        spinnerPendidikan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nothing for now
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                checkSelectedItem()
                pendidikan = spinnerPendidikan.selectedItem.toString()
            }

        }

        btnSelesai.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            writeNewUser(auth.currentUser!!.uid,
                txtNama.text.toString(),
                auth.currentUser!!.phoneNumber!!,
                pendidikan,
                profesi)

            val intent = Intent(this, CoreActivity::class.java)
            startActivity(intent)
        }
    }

    fun checkSelectedItem() {
        btnSelesai.isEnabled = !(spinnerProfesi.selectedItem.toString() == "Anda mendaftar sebagai?" ||
                spinnerPendidikan.selectedItem.toString() == "Pendidikan Anda?")
    }

    private fun writeNewUser(id: String, name: String, phone: String, education: String, profesion: String) {
        val user = User(name, phone, education, profesion)
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(id).set(user)
    }

    @IgnoreExtraProperties
    data class User(
        var name: String? = "",
        var phone: String? = "",
        var education: String? = "",
        var profesion: String? = ""
    )
}
