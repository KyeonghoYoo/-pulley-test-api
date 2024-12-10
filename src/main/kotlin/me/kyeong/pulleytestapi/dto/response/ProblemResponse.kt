package me.kyeong.pulleytestapi.dto.response

import me.kyeong.pulleytestapi.dto.ProblemDto

data class ProblemResponse(
    val problemList: List<ProblemDto>,
    val totalCount: Int = problemList.size
)