package me.kyeong.pulleytestapi.util

import org.springframework.http.HttpStatus

data class ApiResult<T>(
    val success: Boolean,
    val response: T?,
    val status: Int,
    val message: String
)

fun <T> success(
    response: T? = null,
    message: String? = null,
    apiStatus: ApiStatus = ApiStatus.SUCCESS,
): ApiResult<T> {
    return ApiResult(true, response, apiStatus.status.value(), message ?: apiStatus.message)
}

fun fail(
    message: String? = null,
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
): ApiResult<Unit> {
    return ApiResult(false, null, status.value(), message ?: ApiStatus.INTERNAL_SERVER_ERROR.message)
}