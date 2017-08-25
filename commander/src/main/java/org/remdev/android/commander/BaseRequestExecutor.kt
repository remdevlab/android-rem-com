package org.remdev.android.commander

import okhttp3.ResponseBody
import org.remdev.android.commander.models.InteractionResult
import org.remdev.android.commander.utils.fromJson
import org.remdev.timlog.LogFactory
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException

abstract class BaseRequestExecutor {

    private fun doCall(call: Call<ResponseBody>): CallResult {
        val callResult = CallResult()
        try {
            val response = call.execute()
            callResult.isConnected = true
            callResult.response = response
        } catch (ste: NoRouteToHostException) {
            callResult.isConnected = false
            log.e("Socket timeout exception while sending request: %s", ste.message)
        } catch (ste: SocketTimeoutException) {
            callResult.isConnected = false
            log.e("Socket timeout exception while sending request: %s", ste.message)
        } catch (ioe: IOException) {
            callResult.isConnected = false
            log.e("IOE while sending request: %s", ioe.message)
        } catch (e: Throwable) {
            callResult.isConnected = false
            log.e(e, "Error while sending request: %s", e.message)
        }

        return callResult
    }

    protected fun <T> parseBody(call: Call<ResponseBody>, expectedBodyClass: Class<T>): ReadBodyResult<T> {
        val result = ReadBodyResult<T>()
        val callResult = doCall(call)
        if (callResult.isConnected == false) {
            result.code = ResultCodes.ERROR_CODE_CONNECTION_FAILS
            result.isConnectionError = true
            result.isSuccess = false
            return result
        }

        try {
            result.code = callResult.response!!.code()
            val responseBody = callResult.response!!.body()
            var stringBody: String? = null
            if (responseBody != null) {
                stringBody = responseBody.string()
            }
            log.d("The raw response is: %s", stringBody)

            if (callResult.response!!.code() == HttpURLConnection.HTTP_UNAUTHORIZED || isUnauthorizedResponse(stringBody)) {
                result.isSuccess = false
                result.code = HttpURLConnection.HTTP_UNAUTHORIZED
                return result
            }

            if (hasErrors(result)) {
                log.d("Result with code %d was recognized as error result", result.code)
                result.isSuccess = false
                return result
            }
            try {
                log.d("Converting %s to %s class instance", stringBody, expectedBodyClass.name)
                var bean: Any? = null
                if (expectedBodyClass == ByteArray::class.java) {
                    bean = stringBody?.toByteArray()
                } else if (expectedBodyClass == String::class.java) {
                    bean = stringBody
                } else {
                    bean = fromJson(stringBody, expectedBodyClass)
                }
                @Suppress("UNCHECKED_CAST")
                result.body = bean as T?
                result.isBadResponseBody = false
                result.isSuccess = callResult.response!!.isSuccessful
            } catch (e: Exception) {
                log.d("Conversion failed. Reason: %s", e.message)
                result.isBadResponseBody = true
                result.isSuccess = false
            }

        } catch (e: IOException) {
            log.e("IOE while reading response. Reason: %s", e.message)
            result.isSuccess = false
            result.isConnectionError = true
        }

        return result
    }

    protected abstract fun isUnauthorizedResponse(stringBody: String?): Boolean

    protected fun <T, R> processCheckingBasicErrors(call: Call<ResponseBody>, targetBody: Class<T>, errorResult: R,
                                                    consumer: Consumer<ReadBodyResult<T>, InteractionResult<R>>): InteractionResult<R> {

        val result = parseBody(call, targetBody)
        return if (hasErrors(result)) {
            getErrorResult(result, errorResult)
        } else consumer.consume(result)
    }

    protected fun <T, R> processCheckingBasicErrors(call: Call<ResponseBody>, targetBody: Class<T>,
                                                    errorResult: R, successResult: R): InteractionResult<R> {
        val result = parseBody(call, targetBody)
        return if (hasErrors(result)) {
            getErrorResult(result, errorResult)
        } else InteractionResult.success(successResult)
    }

    protected fun hasErrors(result: ReadBodyResult<*>): Boolean {
        return result.isConnectionError
                || result.authError()
                || result.iseError()
                || result.notFoundError()
                || result.requestError()
    }

    protected fun <T> getErrorResult(result: ReadBodyResult<*>): InteractionResult<T> =
            getErrorResult(result, null)

    protected fun <T> getErrorResult(result: ReadBodyResult<*>, payload: T?): InteractionResult<T> {
        if (result.isConnectionError) {
            return InteractionResult.error(ResultCodes.ERROR_CODE_CONNECTION_FAILS, payload)
        }

        if (result.iseError()) {
            return InteractionResult.error(result.code, payload)
        }

        if (result.authError()) {
            return InteractionResult.error(ResultCodes.ERROR_CODE_UNAUTHORIZED, payload)
        }

        if (result.notFoundError()) {
            return InteractionResult.error(ResultCodes.ERROR_CODE_NOT_FOUND, payload)
        }

        return if (result.isBadResponseBody) {
            InteractionResult.error(ResultCodes.ERROR_CODE_UNEXPECTED_RESPONSE, payload)
        } else InteractionResult.error(ResultCodes.ERROR_CODE_UNKNOWN, payload)

    }

    protected inner class CallResult {
        var isConnected: Boolean = false
        var response: Response<ResponseBody>? = null
    }

    protected inner class ReadBodyResult<T> {
        var isSuccess: Boolean = false
        var isConnectionError: Boolean = false
        var isBadResponseBody: Boolean = false
        var code: Int = 0
        var body: T? = null

        fun emptyBody(): Boolean = body == null

        fun authError(): Boolean = code == HttpURLConnection.HTTP_UNAUTHORIZED

        fun requestError(): Boolean {
            return code == HttpURLConnection.HTTP_BAD_REQUEST || code >= HttpURLConnection.HTTP_PAYMENT_REQUIRED
                    && code <= HttpURLConnection.HTTP_UNSUPPORTED_TYPE
                    // not found is considered separately
                    && code != HttpURLConnection.HTTP_NOT_FOUND
        }

        fun iseError(): Boolean =
                code >= HttpURLConnection.HTTP_INTERNAL_ERROR && code <= HttpURLConnection.HTTP_VERSION

        fun notFoundError(): Boolean = code == HttpURLConnection.HTTP_NOT_FOUND
    }

    protected interface Consumer<I, O> {
        fun consume(input: I): O
    }

    companion object {

        private val log = LogFactory.create(BaseRequestExecutor::class.java!!)
    }
}
