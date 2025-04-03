package kr.co.docusnap.presentation.utils

import com.google.gson.Gson

object GsonUtils {

    fun toJson(value: Any?): String {
        return Gson().toJson(value)
    }

    inline fun <reified  T> fromJson(value: String?): T? {
        return runCatching {    // 에러가 나더라도 null 값으로 안전하게 가져오게 하기 위함
            Gson().fromJson(value, T::class.java)
        }.getOrNull()
    }
}