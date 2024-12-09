package me.kyeong.pulleytestapi.util

import org.springframework.http.HttpStatus

enum class ApiStatus (
    val message: String,
    val status: HttpStatus
){
    //// 2 Series
    SUCCESS("작업을 완료하였습니다.", HttpStatus.OK),
    CREATED("생성을 완료하였습니다.", HttpStatus.CREATED),
    //// 4 Series
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    //// 5 Series
    INTERNAL_SERVER_ERROR("요청 중 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
}