package me.kyeong.pulleytestapi.service

import me.kyeong.pulleytestapi.dto.request.ProblemSearchCondition
import me.kyeong.pulleytestapi.dto.response.ProblemResponse
import me.kyeong.pulleytestapi.repository.problem.ProblemQueryRepository
import me.kyeong.pulleytestapi.util.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PulleyServiceImpl(
    private val problemQueryRepository: ProblemQueryRepository
) : PulleyService {

    val log = logger()

    override fun getProblems(searchCondition: ProblemSearchCondition): ProblemResponse {
        val totalCount = searchCondition.totalCount
        val level = searchCondition.level

        var lowLevelProblemCount: Long
        var middleLevelProblemCount: Long
        var highLevelProblemCount: Long

        when (level?.uppercase()) {
            "LOW" -> {
                lowLevelProblemCount = (totalCount * 0.5).toLong()
                middleLevelProblemCount = (totalCount * 0.3).toLong()
                highLevelProblemCount = (totalCount * 0.2).toLong()
            }
            "MIDDLE" -> {
                lowLevelProblemCount = (totalCount * 0.25).toLong()
                middleLevelProblemCount = (totalCount * 0.5).toLong()
                highLevelProblemCount = (totalCount * 0.25).toLong()
            }
            "HIGH" -> {
                lowLevelProblemCount = (totalCount * 0.2).toLong()
                middleLevelProblemCount = (totalCount * 0.3).toLong()
                highLevelProblemCount = (totalCount * 0.5).toLong()
            }
            else ->
                throw IllegalArgumentException("난이도(level) '$level'은 유효한 값이 아닙니다.")
        }

        if (lowLevelProblemCount + middleLevelProblemCount + highLevelProblemCount < totalCount) {
            // 하, 중, 상 문제수를 합친 숫자가 총문제수보다 작은 경우, 그 차이만큼 중 문제수에 더함.
            middleLevelProblemCount += totalCount - (lowLevelProblemCount + middleLevelProblemCount + highLevelProblemCount)
        }

        return ProblemResponse(problemQueryRepository.getProblems(
                searchCondition,
                lowLevelProblemCount,
                middleLevelProblemCount,
                highLevelProblemCount
            ))
    }
}