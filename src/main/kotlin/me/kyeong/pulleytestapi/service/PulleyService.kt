package me.kyeong.pulleytestapi.service

import me.kyeong.pulleytestapi.dto.request.ProblemSearchCondition
import me.kyeong.pulleytestapi.dto.request.WorkBookCreateRequest
import me.kyeong.pulleytestapi.dto.response.ProblemResponse
import me.kyeong.pulleytestapi.dto.response.WorkbookResponse

interface PulleyService {

    fun getProblems(searchCondition: ProblemSearchCondition): ProblemResponse
    fun createWorkbook(request: WorkBookCreateRequest): WorkbookResponse
}