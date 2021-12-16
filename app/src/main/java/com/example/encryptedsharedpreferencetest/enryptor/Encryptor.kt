package com.zapata.security.enryptor

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.zapata.security.diffieHellman.DiffieHellman
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.springframework.security.crypto.keygen.KeyGenerators




/* 
 * @author azapata
 *  nov 2021
 */
class Encryptor {

    private var secretKeySpec: SecretKeySpec? = null

    companion object {

        private val TAG = this::class.java.canonicalName

        //sharedPreferences
        private const val SALT = "46c3bce9d1a2d1ac"

        private const val SHA256_ALGORITHM = "SHA-256"

        private const val STRING_FORMAT = "%064x"

        //requests
        const val ALGORITHM_AES = "AES"

        const val CIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding"


        fun encryptSHA256(key: String): String {
            val md = MessageDigest.getInstance(SHA256_ALGORITHM)
            md.update(key.toByteArray(StandardCharsets.UTF_8))
            val digest = md.digest()
            return String.format(STRING_FORMAT, BigInteger(1, digest))
        }

        fun encryptSharedPreferenceContent(
            context: Context,
            content: String,
            key: String
        ): String {
            val deviceId = Encryptor().getAndroidIDKey(context)
            val password = key + deviceId
            val textEncryptor = Encryptors.text(password, SALT)
            return textEncryptor.encrypt(content)
        }

        fun decryptSharedPreferenceContent(
            context: Context,
            encryptedText: String,
            key: String
        ): String {

            var originalText = ""
            if (encryptedText.isNotEmpty()) {

                val deviceId = Encryptor().getAndroidIDKey(context)
                val password = key + deviceId
                val textEncryptor: TextEncryptor = Encryptors.text(password, SALT)
                originalText = textEncryptor.decrypt(encryptedText)
            }
            return originalText
        }
    }

    private fun getSalt(): String = KeyGenerators.string().generateKey()


    fun getAndroidIDKey(context: Context): String =
        Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)


    @SuppressLint("GetInstance")
    fun encrypt(strToEncrypt: String, secretKey: ByteArray): String? =
        try {
            setKey(secretKey)
            val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            android.util.Base64.encodeToString(
                cipher.doFinal(
                    strToEncrypt.toByteArray(
                        charset("UTF-8")
                    )
                ), android.util.Base64.NO_WRAP
            )
        } catch (ex: Exception) {
            Log.d(TAG, "Error while encrypting. ${ex.message}")
            null
        }

    @SuppressLint("GetInstance")
    fun decrypt(strToDecrypt: String?, secretKey: ByteArray): String? =
        try {
            setKey(secretKey)
            val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            String(
                cipher.doFinal(
                    android.util.Base64.decode(
                        strToDecrypt,
                        android.util.Base64.NO_WRAP
                    )
                )
            )
        } catch (ex: Exception) {
            Log.d(TAG, "Error while decrypting. ${ex.message}")
            null
        }


    private fun setKey(_secretKey: ByteArray) {
        val sha: MessageDigest?
        try {
            var secretKey = _secretKey
            sha = MessageDigest.getInstance("SHA-1")
            secretKey = sha.digest(secretKey)
            secretKey = secretKey.copyOf(16)
            secretKeySpec = SecretKeySpec(secretKey, ALGORITHM_AES)
        } catch (ex: NoSuchAlgorithmException) {
            Log.d(TAG,  ex.message.toString())
        } catch (ex: UnsupportedEncodingException) {
            Log.d(TAG,  ex.message.toString())
        }
    }
}