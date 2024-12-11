package me.kyeong.pulleytestapi.controller

import jakarta.validation.Valid
import me.kyeong.pulleytestapi.dto.request.ProblemSearchCondition
import me.kyeong.pulleytestapi.dto.request.SettingWorkbookGradeRequest
import me.kyeong.pulleytestapi.dto.request.WorkBookCreateRequest
import me.kyeong.pulleytestapi.dto.response.AnalyzeResponse
import me.kyeong.pulleytestapi.dto.response.ProblemResponse
import me.kyeong.pulleytestapi.dto.response.SettingResponse
import me.kyeong.pulleytestapi.dto.response.WorkbookResponse
import me.kyeong.pulleytestapi.service.PulleyService
import me.kyeong.pulleytestapi.util.ApiResult
import me.kyeong.pulleytestapi.util.ApiStatus
import me.kyeong.pulleytestapi.util.logger
import me.kyeong.pulleytestapi.util.success
import org.springframework.web.bind.annotation.*

@RestController
class PulleyController(
    val pulleyService: PulleyService
) {
    val log = logger()

    /**
     * 문제 조회
     */
    @GetMapping("/problems")
    fun getProblems(
        @ModelAttribute searchCondition: ProblemSearchCondition
    ): ApiResult<ProblemResponse> {
        return success(pulleyService.getProblems(searchCondition))
    }

    /**
     * 학습지 생성
     */
    @PostMapping("/piece")
    fun createWorkbook(
        @Valid @RequestBody request: WorkBookCreateRequest
    ): ApiResult<WorkbookResponse> {
        return success(response = pulleyService.createWorkbook(request), apiStatus = ApiStatus.CREATED)
    }

    /**
     * 학생에게 학습지 출제하기
     */
    @PostMapping("/piece/{pieceId}")
    fun setWorkbook(
        @PathVariable pieceId: Long,
        @RequestParam studentIds: List<Long>
    ): ApiResult<SettingResponse> {
        return success(response = pulleyService.setWorkbook(pieceId, studentIds), apiStatus = ApiStatus.CREATED)
    }

    /**
     * 학습지의 문제 조회하기
     */
    @GetMapping("/piece/problems")
    fun getProblemsInSettingWorkbook(
        @RequestParam pieceId: Long
    ): ApiResult<WorkbookResponse> {
        return success(pulleyService.getProblemsInSettingWorkbook(pieceId))
    }

    /**
     * 채점하기
     */
    @PutMapping("/piece/problems")
    fun gradeSettingWorkbook(
        @RequestBody request: SettingWorkbookGradeRequest,
        @RequestParam pieceId: Long
    ): ApiResult<Unit> {
        pulleyService.gradeSettingWorkbook(pieceId, request)
        return success()
    }

    /**
     * 학습지 학습 통계 분석하기
     */
    @GetMapping("/piece/analyze")
    fun getAnalyzeOfWorkbook(
        @RequestParam pieceId: Long
    ): ApiResult<AnalyzeResponse> {
        return success(pulleyService.getAnalyzeOfWorkbook(pieceId))
    }
}