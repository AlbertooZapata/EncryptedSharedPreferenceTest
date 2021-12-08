package com.example.encryptedsharedpreferencetest

import android.os.Bundle
import android.provider.Settings.Secure
import android.provider.Settings.System
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ALGORITHM_TRANSFORMATION = "AES/CBC/PKCS5Padding"
        private const val ALGORITHM = "AES"

        private const val TOKEN_KEY = "fqJfdzGDvfwbedsKSUGty3VZ9taXxMVw"
        private const val BYTE_ARRAY_SIZE = 16

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val encryptedKey = EncryptorUtil.encryptedSPKey(SharedPreferencesUtil.KEY)
        val androidIDKey = EncryptorUtil.getAndroidIDKey(this)
        val password = SharedPreferencesUtil.KEY + androidIDKey
        val contentEncrypted = EncryptorUtil.encryptContent("This is the text to encrypt", password)
        val sharedPreferencesUtil = SharedPreferencesUtil(this)
        sharedPreferencesUtil.setValueFromSP(encryptedKey, contentEncrypted)

        val contentFromSP = sharedPreferencesUtil.getValueFromSP(encryptedKey, "")
        val decryptedValueFromSP = EncryptorUtil.decryptContent(contentEncrypted, password)


        Log.d("MainActivity", "encryptedKey: $encryptedKey")
        Log.d("MainActivity", "androidID: $androidIDKey")
        Log.d("MainActivity", "decryptedValueFromSP: $decryptedValueFromSP")

    }


}