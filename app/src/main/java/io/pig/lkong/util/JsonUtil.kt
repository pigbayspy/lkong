package io.pig.lkong.util

import android.util.Log
import com.google.gson.Gson

/**
 * @author yinhang
 */
object JsonUtil {

    private val tag = JsonUtil.javaClass.name
    private val gson = Gson()

    fun <T> fromJsonOfNull(str: String?, clazz: Class<T>): T? {
        if (str.isNullOrEmpty()) {
            return null
        }
        return try {
            gson.fromJson(str, clazz)
        } catch (e: Exception) {
            Log.e(tag, "json deserialize fail, cause: ${e.message}")
            null
        }
    }

    fun <T> fromJson(str:String, clazz: Class<T>):T {
        return gson.fromJson(str, clazz)
    }
}