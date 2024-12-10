package me.kyeong.pulleytestapi.service

import me.kyeong.pulleytestapi.dto.request.ProblemSearchCondition
import me.kyeong.pulleytestapi.dto.response.ProblemResponse

interface PulleyService {

    fun getProblems(searchCondition: ProblemSearchCondition): ProblemResponse
}