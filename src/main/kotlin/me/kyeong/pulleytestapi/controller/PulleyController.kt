package me.kyeong.pulleytestapi.controller

import org.springframework.web.bind.annotation.*

@RestController
class PulleyController(

) {
    /**
     * 문제 조회
     */
    @GetMapping("/problems")
    fun getProblems() {
        // TODO 로직 작성 예정
    }

    /**
     * 학습지 생성
     */
    @PostMapping("/piece")
    fun createWorkbook() {
        // TODO 로직 작성 예정
    }

    /**
     * 학생에게 학습지 출제하기
     */
    @PostMapping("/piece/{pieceId}")
    fun setWorkbook(@PathVariable pieceId: String) {
        // TODO 로직 작성 예정
    }

    /**
     * 학습지의 문제 조회하기
     */
    @GetMapping("/piece/problems")
    fun getProblemsInWorkbook() {
        // TODO 로직 작성 예정
    }

    /**
     * 채점하가
     */
    @PutMapping("/piece/problems")
    fun gradeWorkbook(@RequestBody pieceId: String) {
        // TODO 로직 작성 예정
    }

    /**
     * 학습지 학습 통계 분석하기
     */
    @GetMapping("/piece/analyze")
    fun getAnalyzeOfWorkbook() {
        // TODO 로직 작성 예정
    }
}