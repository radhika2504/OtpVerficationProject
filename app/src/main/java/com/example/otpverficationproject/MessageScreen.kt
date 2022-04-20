package com.example.otpverficationproject

import android.Manifest.permission.RECEIVE_SMS
import android.Manifest.permission.SEND_SMS
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MessageScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_screen)

        if(ActivityCompat.checkSelfPermission(this,RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(RECEIVE_SMS,SEND_SMS),111)
        }
        else
        {
            receiveMsg()
        }
        val smsButton: Button = findViewById (R.id.SmsButton)
        val phoneNum:TextView=findViewById (R.id.phoneNumberTextField)
        val otp:TextView=findViewById (R.id.smsText)
        smsButton.setOnClickListener {
            val sms=SmsManager.getDefault()
            sms.sendTextMessage(phoneNum.text.toString(),"ME",otp.text.toString(),null,null)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            receiveMsg()
    }
    private fun receiveMsg()
    {
        val br=object :BroadcastReceiver(){
            override  fun onReceive(p0:Context?,p1: Intent?)
            {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
                {
                    for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1))
                    {
                        Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }
}