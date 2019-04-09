package net.halo.ajarin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_core.*

class CoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_core)
        var userType = ""
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) userType = document.get("profesion").toString()
                if (userType == "Guru") {
                    loadFragment(BerandaGuruFragment())
                }
                else {
                    loadFragment(BerandaFragment())
                }
            }

        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_beranda -> {
                    if (userType == "Guru") {
                        loadFragment(BerandaGuruFragment())
                    }
                    else {
                        loadFragment(BerandaFragment())
                    }
                }
                R.id.action_belajar ->
                    loadFragment(BelajarFragment())
                R.id.action_jadwal ->
                    loadFragment(JadwalFragment())
                R.id.action_langganan ->
                    loadFragment(LanggananFragment())
                else ->
                    loadFragment(AkunFragment())
            }
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()

            return true;
        }
        return false;
    }
}
