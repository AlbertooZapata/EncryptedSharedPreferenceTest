package com.zapata.security

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.encryptedsharedpreferencetest.R
import com.zapata.security.diffieHellman.DiffieHellman
import com.zapata.security.enryptor.Encryptor

//import io.card.payment.CardIOActivity


class MainActivity : AppCompatActivity() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var imgContainer: ImageView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveOnSharedP()
        getFromSharedP()

    }

    private fun shTest(){


        val spKey = SharedPreferences.KEY
        val encryptedKey = Encryptor.encryptSHA256(SharedPreferences.KEY)
//        val androidIDKey = Encryptor.getAndroidIDKey(this)
//        val password = SharedPreferencesUtil.KEY + androidIDKey
        val textToSP = "This is the text to encrypt in SP"
        val contentEncrypted = Encryptor.encryptSharedPreferenceContent(this, textToSP, SharedPreferences.KEY)
        val sharedPreferencesUtil = SharedPreferences(this)
        sharedPreferencesUtil.setValueFromSP(encryptedKey, contentEncrypted)

        val contentEncryptedFromSP = sharedPreferencesUtil.getValueFromSP(encryptedKey, "")
        val decryptedValueFromSP = Encryptor.decryptSharedPreferenceContent(this, contentEncryptedFromSP, SharedPreferences.KEY)

        Log.d("MainActivity", "Key: ${SharedPreferences.KEY}")
        Log.d("MainActivity", "encryptedKey: $encryptedKey")
        Log.d("MainActivity", "decryptedValueFromSP: $decryptedValueFromSP")



    }
    private fun dhTestMain(){

//        val serverPublicKeyStr = "MIIBIzCBmQYJKoZIhvcNAQMBMIGLAoGBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7OZTgf//////////AgECAgICAAOBhAACgYAPD+auFKF6R6uC3HXalv+ZF4iZO16teVdiVmUMsMts/mMhKddMX8es4vtmKOG7vJ4rMYtyG52lW6yK3k+deXgtu/MyAEq+4WO4sH+NpbGAdcBmonezqysZokFjLOZw4YA2GZxK34WH2Xt6LjunUjkPgNmoRnTDfxw+oxfUMJg2IQ=="
        val serverPublicKeyStr = "MIIBJDCBmQYJKoZIhvcNAQMBMIGLAoGBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7OZTgf//////////AgECAgICAAOBhQACgYEA7d9WAFhWjqdkjOhGCQYmvzWm/gPOi+2RaWVnckEe7txTCOno8sJTuBeGUcVWfdzDTc3tUY2WlGYkDzUJjrKhLos6kjq41MjpzWc2YVr/gpp3UvV0sqojyYiGI1nE4mjiIJlh8Q6FUIAetBY27Dld6EKLmLzi6ddAFZOggjLnKQk="
        val myPrivateKey = "MIHkAgEAMIGZBgkqhkiG9w0BAwEwgYsCgYEA///////////JD9qiIWjCNMTGYouA3BzRKQJOCIpnzHQCC76mOxObIlFKCHmONATd75UZs806QxswKwpt8l8UN0/hNW1tUcJF5IW1dmJefsb0TELppjftawv/XLb0Brft7jhr+1qJn6WunyQRfEsf5kkoZlHs5lOB//////////8CAQICAgIABEMCQQClTz9W7BzaAMqcJKf/TZBKa/8myR05IiyywJB9igHCqGCYUKoQIED/DE37ddPaXsIprqhPCgJ6XyANlp4aYvrm"
        val myPublicKey = "MIIBJDCBmQYJKoZIhvcNAQMBMIGLAoGBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7OZTgf//////////AgECAgICAAOBhQACgYEA80GyAFKGCUA+UWO9X+NxRPjdiOZnVgs1rCjSrGbBqvXsNTxGpHnXP9I36NVIpaLdSoUTtY7ryOjiAxT95ObyfCTCArvAEys62O1s3Eo4VjrqkTsohQytOthuu32GHIoHnb2HDGpNqB7nkGGYy7lkD9PKzSd1trxYKRIwH219V/Q="

        val dh = DiffieHellman()
        dh.setKeyPar(myPublicKey, myPrivateKey)
        dh.setServerKey(serverPublicKeyStr)
        dh.generateCommonSecretKey()

        val secretKey = dh.getSecretKey()

        val textMessage = "Hi, this is the text to encrypt"

        val encryptor = Encryptor()
        val encryptedText = encryptor.encrypt(textMessage, secretKey!!)
        val decryptedText = encryptor.decrypt(encryptedText, secretKey)

        Log.d("MainActivity",decryptedText!!)
    }

    private fun saveOnSharedP(){

        val value = "Value to save"
        val encryptedSP = EncryptedSharedPreference(this)

        encryptedSP.saveValue(SharedPreferences.KEY, value)

    }

    private fun getFromSharedP(){

        val encryptedSP = EncryptedSharedPreference(this)
        val keySP = encryptedSP.getValue(SharedPreferences.KEY)
        Log.d("AZ", keySP)
    }





//
//    private fun scanCard() {
//
//        activityResultLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.StartActivityForResult(),
//                ActivityResultCallback { activityResult ->
//                    if (activityResult.resultCode == RESULT_OK && activityResult.data != null) {
//
//                        val scanResult = activityResult.data!!.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
//
//                        Log.i("MainActivity","Card Number: " + scanResult!!.redactedCardNumber)
//
//
//                    }
//                })
//
//        val scanIntent = Intent(this, CardIOActivity::class.java)
//
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
//
//        val btnScanCard = findViewById<Button>(R.id.btnScanCard)
//        btnScanCard.setOnClickListener {
//
//            activityResultLauncher.launch(scanIntent)
//        }
//    }
//
//    private fun pickPicture() {
//
//        val getImage =
//            registerForActivityResult(
//                ActivityResultContracts.GetContent(),
//                ActivityResultCallback { uri ->
//                    imgContainer.setImageURI(uri)
//                })
//
//
//        val btnPickPhoto = findViewById<Button>(R.id.btnPickPhoto)
//
//        btnPickPhoto.setOnClickListener {
//            getImage.launch("image/*")
//        }
//    }
//
//    //  Open camera to take a picture
//    private fun takePicture() {
//
//        activityResultLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.StartActivityForResult(),
//                ActivityResultCallback { activityResult ->
//                    if (activityResult.resultCode == RESULT_OK && activityResult.data != null) {
//
//                        val bundle = activityResult.data!!.extras
//                        val bitmap: Bitmap = bundle!!.get("data") as Bitmap
//                        imgContainer.setImageBitmap(bitmap)
//
//                    }
//                })
//        val btnTakePhoto = findViewById<Button>(R.id.btnTakePhoto)
//        btnTakePhoto.setOnClickListener {
//
//            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // take picture
//            activityResultLauncher.launch(intentCamera)
//        }
//    }
}