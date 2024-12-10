package me.kyeong.pulleytestapi.error.exceptions

import me.kyeong.pulleytestapi.util.ApiResult
import me.kyeong.pulleytestapi.util.ApiStatus
import me.kyeong.pulleytestapi.util.fail
import me.kyeong.pulleytestapi.util.logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

    val log = logger()

    fun newResponse(
        message: String,
        status: HttpStatus,
    ): ResponseEntity<ApiResult<Unit>> {
        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json")
        return ResponseEntity<ApiResult<Unit>>(fail(message, status), headers, status)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException
    ): ResponseEntity<ApiResult<Unit>> {
        log.error("handleIllegalArgumentException", e)
        return newResponse(e.message ?: ApiStatus.BAD_REQUEST.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        e: Exception,
        request: WebRequest
    ): ResponseEntity<ApiResult<Unit>> {
        if (request is ServletWebRequest) {
            log.error("handleException ${request.request.requestURI}", e)
        } else {
            log.error("handleException", e)
        }
        return newResponse(e.message ?: ApiStatus.INTERNAL_SERVER_ERROR.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}