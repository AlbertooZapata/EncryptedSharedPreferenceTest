package com.example.encryptedsharedpreferencetest

import android.content.Context
import android.provider.Settings
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/*
 * @author Alberto Zapata
 *  nov 2021
 */
class EncryptorUtil {

    companion object {

        private const val SALT = "46c3bce9d1a2d1ac"

        private const val SHA256_ALGORITHM = "SHA-256"

        private const val STRING_FORMAT = "%064x"


        fun encryptedSPKey(key: String): String {

            val md = MessageDigest.getInstance(SHA256_ALGORITHM)
            md.update(key.toByteArray(StandardCharsets.UTF_8))
            val digest = md.digest()
            return String.format(STRING_FORMAT, BigInteger(1, digest))
        }

        fun encryptContent(content: String, password: String): String{

            val textEncryptor = Encryptors.text(password, SALT)
            return textEncryptor.encrypt(content)
        }

        fun decryptContent(encryptedText: String, password: String): String{
            val textEncryptor: TextEncryptor = Encryptors.text(password, SALT)
            return textEncryptor.decrypt(encryptedText)
        }

        fun getAndroidIDKey(context: Context): String{
            val androidID: String = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            return androidID

        }
    }
}
