package org.remdev.android.commander.models;

import org.remdev.android.commander.ResultCodes
import org.remdev.android.commander.annotations.POJO
import org.remdev.android.commander.utils.ConversionUtils
import org.remdev.timlog.Log
import org.remdev.timlog.LogFactory

open class InteractionResult<T: Any?> : JsonSerializable<InteractionResult<T>> {

    var code = 0
    var errorCode = 0
    @POJO
    var payload : T? = null

    override fun toJson(): String {
        val map : MutableMap<String, String> = mutableMapOf(
                FIELD_CODE to code.toString(),
                FIELD_ERROR_CODE to errorCode.toString()
        )
        payload?.let {
            map.put(FIELD_PAYLOAD_CLASS, ConversionUtils.getClass(it!!).name)
            map.put(FIELD_PAYLOAD, ConversionUtils.toJson(it))
        }
        return ConversionUtils.toJson(map)
    }

    companion object {
        val log : Log = LogFactory.create(InteractionResult::class.java)
        const val FIELD_CODE = "code"
        const val FIELD_ERROR_CODE = "errorCode"
        const val FIELD_PAYLOAD_CLASS = "payloadClass"
        const val FIELD_PAYLOAD = "payload"

        fun success() : InteractionResult<Any?> = success(null)

        fun <R> success(payload: R) : InteractionResult<R> {
            val res = InteractionResult<R>()
            res.code = ResultCodes.CODE_OK
            res.errorCode = 0
            res.payload = null
            return res
        }

        fun error() : InteractionResult<Any?> = error(0)

        fun error(errorCode: Int): InteractionResult<Any?> = error(errorCode, null)

        fun <R> error(@POJO payload: R?): InteractionResult<Any?> = error(0, payload)

        fun <R> error(errorCode: Int, @POJO payload: R?): InteractionResult<R> {
            val result = InteractionResult<R>()
            result.code = ResultCodes.CODE_ERROR
            result.errorCode = errorCode
            result.payload = payload
            return result
        }

        fun <T> parseFromJson(json: String?) : InteractionResult<T>? {
            if (json == null) {
                return null
            }
            val map = ConversionUtils.fromJson(json, Map::class.java)
            if (map == null) {
                return null;
            }
            val code = (map[FIELD_CODE] as String?)?.toInt()
            val errorCode = (map[FIELD_ERROR_CODE] as String?)?.toInt()
            val className = map[FIELD_PAYLOAD_CLASS] as String?
            val payloadJson = map[FIELD_PAYLOAD] as String?
            if (code == null || errorCode == null) {
                throw IllegalArgumentException("Invalid json: " + json);
            }
            var payload : T? = null
            if (className != null) {
                try {
                    @Suppress("UNCHECKED_CAST")
                    val clazz = Class.forName(className) as Class<T>
                    payload = ConversionUtils.fromJson(payloadJson, clazz)
                } catch (e: ClassNotFoundException) {
                    log.e("Could not found class %s", className)
                    throw IllegalArgumentException("Invalid json: "+ json)
                }
            }
            val res = InteractionResult<T>();
            res.code = code
            res.errorCode = errorCode
            res.payload = payload
            return res;
        }
    }
}