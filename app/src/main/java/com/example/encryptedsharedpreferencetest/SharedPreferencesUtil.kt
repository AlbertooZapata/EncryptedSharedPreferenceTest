package com.example.encryptedsharedpreferencetest

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson

/*
 * @author Alberto Zapata
 *  nov 2021
 */
class SharedPreferencesUtil(context: Context) {

    companion object {

        const val KEY = "myKey"
    }

    val sharedPref: SharedPreferences = context.getSharedPreferences(
        "${context.packageName}_${this.javaClass.simpleName}",
        MODE_PRIVATE
    )

    inline fun <reified T : Any> getValueFromSP(key: String, defaultValue: T? = null): T {

        return when (T::class) {
            Boolean::class -> sharedPref.getBoolean(
                key,
                defaultValue as? Boolean? ?: false
            ) as T
            Float::class -> sharedPref.getFloat(key, defaultValue as? Float? ?: 0.0f) as T
            Int::class -> sharedPref.getInt(key, defaultValue as? Int? ?: 0) as T
            Long::class -> sharedPref.getLong(key, defaultValue as? Long? ?: 0L) as T
            String::class -> sharedPref.getString(key, defaultValue as? String? ?: "") as T
            else -> {
                val typeName = T::class.java.simpleName
                throw Error("Unable to get sharedPreference with value type '$typeName'. Use getObject")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> setValueFromSP(key: String, defaultValue: T? = null) {
        with(sharedPref.edit()) {
            when (T::class) {
                Boolean::class -> putBoolean(key, defaultValue as Boolean)
                Float::class -> putFloat(key, defaultValue as Float)
                Int::class -> putInt(key, defaultValue as Int)
                Long::class -> putLong(key, defaultValue as Long)
                String::class -> putString(key, defaultValue as String)
                else -> {
                    if (defaultValue is Set<*>) {
                        putStringSet(key, defaultValue as Set<String>)
                    } else {
                        val json = Gson().toJson(defaultValue)
                        putString(key, json)
                    }
                }
            }
            commit()
        }
    }
    fun removeAllValues(){
        sharedPref.edit {
            this.clear()
        }
    }
}
