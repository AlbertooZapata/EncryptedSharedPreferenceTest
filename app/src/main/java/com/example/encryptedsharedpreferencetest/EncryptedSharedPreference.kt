package com.zapata.security

import android.content.Context
import com.zapata.security.enryptor.Encryptor

/*
 * @author azapata
 *  nov 2021
 */
class EncryptedSharedPreference( private val context: Context) {

    fun saveValue(key: String, value: String){

        val sharedPref = SharedPreferences(context)
        val keyHash = Encryptor.encryptSHA256(key)
        val encryptedValue = Encryptor.encryptSharedPreferenceContent(context, value, key)
        sharedPref.setValueFromSP(keyHash, encryptedValue)
    }

    fun getValue(key: String): String{

        val sharedPref = SharedPreferences(context)
        val keyHash = Encryptor.encryptSHA256(key)
        val encryptedValue = sharedPref.getValueFromSP(keyHash, defaultValue = "")
        return Encryptor.decryptSharedPreferenceContent(context, encryptedValue, key)
    }
}