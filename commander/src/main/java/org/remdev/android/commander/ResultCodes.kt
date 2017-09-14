package org.remdev.android.commander

import java.net.HttpURLConnection

object ResultCodes {
    const val CODE_OK = 0
    const val CODE_ERROR = 1

    const val ERROR_CODE_UNKNOWN = -1
    const val ERROR_CODE_CONNECTION_FAILS = 1
    const val ERROR_CODE_UNAUTHORIZED = HttpURLConnection.HTTP_UNAUTHORIZED
    const val ERROR_CODE_NOT_FOUND = HttpURLConnection.HTTP_NOT_FOUND
    const val ERROR_CODE_UNEXPECTED_RESPONSE = 4
}

