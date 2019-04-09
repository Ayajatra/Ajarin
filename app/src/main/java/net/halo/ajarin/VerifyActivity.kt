package net.halo.ajarin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.synthetic.main.activity_verify.*
import java.util.concurrent.TimeUnit

class VerifyActivity : AppCompatActivity() {

    var storedVerificationId: String? = null
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        val intent = intent
        val phone: String = intent.getStringExtra("phone")
        sendVerificationCode(phone)

        btnSignIn.setOnClickListener {
            val code = txtVerification.text.toString().trim()
            if (code.isEmpty() || code.length < 6) {
                txtVerification.error = "Enter valid code"
                txtVerification.requestFocus()
                return@setOnClickListener
            }

            verifyVerificationCode(code)
        }
    }

    private fun sendVerificationCode(phone: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+62$phone",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks
        )
    }

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()  {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(phoneAuthCredential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(VerifyActivity(), "Invalid Request", Toast.LENGTH_SHORT).show()
            }
            else if (e is FirebaseTooManyRequestsException) {
                Toast.makeText(VerifyActivity(), "SMS quota has been reached", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken) {
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    private fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)

        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    val db = FirebaseFirestore.getInstance()

                    db.collection("users").document(auth.currentUser!!.uid)
                        .get()
                        .addOnSuccessListener {
                            val intent =
                                if(it.exists()) {
                                    Intent(this, CoreActivity::class.java)
                                } else {
                                    Intent(this, RegistrationActivity::class.java)
                                }

                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                }
                else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(VerifyActivity(), "Invalid verification code", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
