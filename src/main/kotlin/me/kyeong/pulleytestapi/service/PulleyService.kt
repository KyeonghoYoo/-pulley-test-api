package me.kyeong.pulleytestapi.service

import me.kyeong.pulleytestapi.dto.request.ProblemSearchCondition
import me.kyeong.pulleytestapi.dto.request.SettingWorkbookGradeRequest
import me.kyeong.pulleytestapi.dto.request.WorkBookCreateRequest
import me.kyeong.pulleytestapi.dto.response.ProblemResponse
import me.kyeong.pulleytestapi.dto.response.SettingResponse
import me.kyeong.pulleytestapi.dto.response.WorkbookResponse

interface PulleyService {

    fun getProblems(searchCondition: ProblemSearchCondition): ProblemResponse
    fun createWorkbook(request: WorkBookCreateRequest): WorkbookResponse
    fun setWorkbook(workbookId: Long, studentIds: List<Long>): SettingResponse
    fun getProblemsInSettingWorkbook(settingId: Long): WorkbookResponse
    fun gradeSettingWorkbook(settingId: Long, request: SettingWorkbookGradeRequest)
}