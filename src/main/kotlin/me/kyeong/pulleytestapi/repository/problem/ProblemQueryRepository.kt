package me.kyeong.pulleytestapi.repository.problem

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import me.kyeong.pulleytestapi.domain.problem.ProblemLevel
import me.kyeong.pulleytestapi.domain.problem.ProblemType
import me.kyeong.pulleytestapi.domain.problem.QProblemEntity.problemEntity
import me.kyeong.pulleytestapi.dto.ProblemDto
import me.kyeong.pulleytestapi.dto.QProblemDto
import me.kyeong.pulleytestapi.dto.request.ProblemSearchCondition
import org.springframework.stereotype.Repository
import org.springframework.util.CollectionUtils.isEmpty

@Repository
class ProblemQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun getProblems(
        searchCondition: ProblemSearchCondition,
        lowLevelProblemCount: Long,
        middleLevelProblemCount: Long,
        highLevelProblemCount: Long
    ): List<ProblemDto> {

        // 유저가 요청한 조건에 해당하는 문제들이 데이터베이스에 난이도별로 실제로 몇개씩 존재하는지 카운트를 조회함.
        val result = queryFactory.select(problemEntity.level, problemEntity.id.count())
            .from(problemEntity)
            .where(
                unitCodeListIn(searchCondition.unitCodeList),
                problemTypeEq(searchCondition.problemType),
            )
            .groupBy(problemEntity.level)
            .fetch()

        // 조회된 문제수를 상, 중, 하 별로 구분하여 Map 생성
        val problemCountMap = result.groupBy(
                keySelector = { tuple ->
                    when (tuple[0, ProblemLevel::class.java]) {
                        ProblemLevel.ONE -> "LOW"
                        ProblemLevel.TWO, ProblemLevel.THREE, ProblemLevel.FOUR -> "MIDDLE"
                        ProblemLevel.FIVE -> "HIGH"
                        null -> ""
                    }
                },
                valueTransform = { tuple -> tuple[1, Long::class.java] }
            )
            .mapValues { entry -> entry.value.sumOf { it!! } }

        val lowDiff = if (problemCountMap["LOW"] != null) {
            lowLevelProblemCount - problemCountMap["LOW"]!!
        } else {
            null
        }
        val middleDiff = if (problemCountMap["MIDDLE"] != null) {
            middleLevelProblemCount - problemCountMap["MIDDLE"]!!
        } else {
            null
        }
        val highDiff = if (problemCountMap["HIGH"] != null) {
            highLevelProblemCount - problemCountMap["HIGH"]!!
        } else {
            null
        }

        /**
         * 가능한 totalCount에 맞는 문제수를 맞추기 위한 limit 숫자 생성 지역 함수
         */
        fun makeProblemLimit(aCount: Long, bDiff: Long?, bCount: Long, cDiff: Long?, cCount: Long): Long {
            return if (bDiff != null) {
                if (bDiff > 0) {
                    if (cDiff != null) {
                        if (cDiff > 0) {
                            aCount + bDiff + cDiff
                        } else {
                            aCount + bDiff
                        }
                    } else {
                        aCount + bDiff + cCount
                    }
                } else {
                    aCount
                }
            } else {
                if (cDiff != null) {
                    aCount + bCount
                } else {
                    aCount + bCount + cCount
                }
            }
        }

        val lowLimit = makeProblemLimit(lowLevelProblemCount, middleDiff, middleLevelProblemCount, highDiff, highLevelProblemCount)
        val middleLimit = makeProblemLimit(middleLevelProblemCount, lowDiff, lowLevelProblemCount, highDiff, highLevelProblemCount)
        val highLimit = makeProblemLimit(highLevelProblemCount, middleDiff, middleLevelProblemCount, lowDiff, lowLevelProblemCount)

        val lowProblemDtoList = queryFactory
            .select(QProblemDto(problemEntity))
            .from(problemEntity)
            .where(
                unitCodeListIn(searchCondition.unitCodeList),
                problemTypeEq(searchCondition.problemType),
                problemEntity.level.eq(ProblemLevel.ONE)
            )
            .orderBy(Expressions.numberTemplate(Double::class.java, "function('rand')").asc())
            .limit(lowLimit)
            .fetch()
        val middleProblemDtoList = queryFactory
            .select(QProblemDto(problemEntity))
            .from(problemEntity)
            .where(
                unitCodeListIn(searchCondition.unitCodeList),
                problemTypeEq(searchCondition.problemType),
                problemEntity.level.`in`(ProblemLevel.TWO, ProblemLevel.THREE, ProblemLevel.FOUR)
            )
            .orderBy(Expressions.numberTemplate(Double::class.java, "function('rand')").asc())
            .limit(middleLimit)
            .fetch()
        val highProblemDtoList = queryFactory
            .select(QProblemDto(problemEntity))
            .from(problemEntity)
            .where(
                unitCodeListIn(searchCondition.unitCodeList),
                problemTypeEq(searchCondition.problemType),
                problemEntity.level.eq(ProblemLevel.FIVE)
            )
            .orderBy(Expressions.numberTemplate(Double::class.java, "function('rand')").asc())
            .limit(highLimit)
            .fetch()

        // 각 난이도 별 ProblemDto를 조회한 뒤, list를 합쳐 반환함.
        return listOf(lowProblemDtoList, middleProblemDtoList, highProblemDtoList).flatten()
    }
}

private fun unitCodeListIn(uniCodeList: List<String>?): BooleanExpression? {
    return if (isEmpty(uniCodeList)) null else problemEntity.unitCode.`in`(uniCodeList)
}
private fun problemTypeEq(problemType: String?): BooleanExpression? {
    return when (problemType) {
        ProblemType.SELECTION.name -> problemEntity.type.eq(ProblemType.SELECTION)
        ProblemType.SUBJECTIVE.name -> problemEntity.type.eq(ProblemType.SUBJECTIVE)
        else -> null
    }
}
