package me.kyeong.pulleytestapi.dto

import com.querydsl.core.annotations.QueryProjection
import me.kyeong.pulleytestapi.domain.problem.ProblemEntity

data class ProblemDto(
    val id: Long? = null,
    val answer: String,
    val unitCode: String,
    val level: Int,
    val problemType: String
) {
    @QueryProjection constructor(problemEntity: ProblemEntity) : this(
        id = problemEntity.id,
        answer = problemEntity.answer,
        unitCode = problemEntity.unitCode,
        level = problemEntity.level.value,
        problemType = problemEntity.type.name

    )
    companion object {
        fun of(problemEntity: ProblemEntity): ProblemDto {
            return ProblemDto(
                id = problemEntity.id,
                answer = problemEntity.answer,
                unitCode = problemEntity.unitCode,
                level = problemEntity.level.value,
                problemType = problemEntity.type.name
            )
        }
    }
}