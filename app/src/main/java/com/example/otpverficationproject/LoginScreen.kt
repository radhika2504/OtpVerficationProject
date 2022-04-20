package com.example.otpverficationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginScreen : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    val btn_getotp: Button = findViewById (R.id.btn_getotp)
    val et_phone: EditText = findViewById (R.id.et_phone)
    val et_otp:EditText = findViewById (R.id.et_otp)
    val btn_signup: Button = findViewById (R.id.btn_signup)
    val phone_layout:LinearLayout = findViewById (R.id.phone_layout)
    val otp_layout: LinearLayout = findViewById (R.id.otp_layout)
    private lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        auth= FirebaseAuth.getInstance()
        btn_getotp.setOnClickListener{
            val pno=et_phone.text.toString().trim()

            if(!pno.isEmpty())
            {
                sendVerificationcode("+91$pno")
            }
            else
            {
                Toast.makeText(applicationContext,"Please Enter Phone Number",Toast.LENGTH_LONG).show()
            }
        }
        btn_signup.setOnClickListener{
            val otp=et_otp.text.toString().trim()

            if(!otp.isEmpty())
            {
                verifyVerificationcode(otp)
            }
            else
            {
                Toast.makeText(applicationContext,"Please Enter OTP",Toast.LENGTH_LONG).show()
            }
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code = credential.smsCode
                if (code != null) {
                    et_otp.setText(code)
                }
            }


            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext,"Auth Failed",Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {



                storedVerificationId = verificationId
                resendToken = token
                phone_layout.visibility=View.GONE
               otp_layout.visibility=View.VISIBLE
            }


    }



}

    private fun sendVerificationcode(pno: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            pno,60,TimeUnit.SECONDS,this,callbacks
        )
    }
    private fun verifyVerificationcode(code:String)
    {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signUp(credential)
    }
    private fun signUp(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    Toast.makeText(applicationContext,"Successfully SignUp",Toast.LENGTH_LONG).show()
                    val intent=Intent(applicationContext,MessageScreen::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(applicationContext,"Code Enter Incorrect",Toast.LENGTH_LONG).show()
                        et_otp.setText("")
                    }

                }
            }
    }

    }
