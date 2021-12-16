package com.zapata.security.diffieHellman

import android.util.Log
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement


class DiffieHellman {

    companion object {

        private val TAG = this::class.java.canonicalName

        const val ALGORITHM_DH = "DH"

        const val KEY_SIZE = 1024

        const val SECRET_KEY_SIZE = 16
    }

    private var keyPair: KeyPair? = null

    private var secretKey: ByteArray? = null

    private var serverPublicKey: PublicKey? = null

    @Throws(Exception::class)
    fun generateKeys() {
        try {
            val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_DH)
            keyPairGenerator.initialize(KEY_SIZE)
            keyPair = keyPairGenerator.generateKeyPair()
        } catch (ex: Exception) {
            Log.d(TAG, "There was a error generating the KeyPair. ${ex.message}")
        }
    }

    fun setServerKey(serverPublicKeyStr: String) {
        val byteArrayOfServerPublicKey: ByteArray = android.util.Base64.decode(
            serverPublicKeyStr.toByteArray(),
            android.util.Base64.NO_WRAP
        )
        val keyFactory: KeyFactory = KeyFactory.getInstance(ALGORITHM_DH)
        serverPublicKey = keyFactory.generatePublic(X509EncodedKeySpec(byteArrayOfServerPublicKey))
    }

    fun setKeyPar(publicKeyStr: String, privateKeyStr: String) {
        val byteArrayOfPublicKey: ByteArray =
            android.util.Base64.decode(publicKeyStr.toByteArray(), android.util.Base64.NO_WRAP)
        val byteArrayOfPrivateKey: ByteArray =
            android.util.Base64.decode(privateKeyStr.toByteArray(), android.util.Base64.NO_WRAP)

        val keyFactory: KeyFactory = KeyFactory.getInstance(ALGORITHM_DH)
        val publicKey: PublicKey =
            keyFactory.generatePublic(X509EncodedKeySpec(byteArrayOfPublicKey))
        val privateKey: PrivateKey =
            keyFactory.generatePrivate(PKCS8EncodedKeySpec(byteArrayOfPrivateKey))

        keyPair = KeyPair(publicKey, privateKey)
    }

    fun generateCommonSecretKey() {
        try {
            val keyAgreement: KeyAgreement = KeyAgreement.getInstance(ALGORITHM_DH)
            keyAgreement.init(keyPair!!.private)
            keyAgreement.doPhase(serverPublicKey, true)
            secretKey = shortenSecretKey(keyAgreement.generateSecret())
        } catch (ex: Exception) {
            Log.d(TAG, "There was a error generating common secret key. ${ex.message}")
        }
    }

    private fun shortenSecretKey(longKey: ByteArray): ByteArray? =
        try {
            val shortenedKey = ByteArray(SECRET_KEY_SIZE)
            System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.size)
            shortenedKey
        } catch (ex: Exception) {
            Log.d(TAG, "There was a error generating shorten key. ${ex.message}")
            null
        }

    fun getPublicKey(): PublicKey? =
        if (keyPair != null) {
            keyPair!!.public
        } else {
            null
        }

    fun getPrivateKey(): PrivateKey? =
        if (keyPair != null) {
            keyPair!!.private
        } else {
            null
        }

    fun getSecretKey(): ByteArray? = secretKey

}






