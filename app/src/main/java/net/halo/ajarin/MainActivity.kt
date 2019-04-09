package net.halo.ajarin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val intent = Intent(this, CoreActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        btnContinue.setOnClickListener {
            val phone = txtPhone.text.toString().trim()
            if (phone.isEmpty() || phone.length !in 9..12) {
                txtPhone.error = "Enter a valid phone number"
                txtPhone.requestFocus()
                return@setOnClickListener
            }

            val intent = Intent(this, VerifyActivity::class.java)
            intent.putExtra("phone", phone)
            startActivity(intent)
        }
    }
}
