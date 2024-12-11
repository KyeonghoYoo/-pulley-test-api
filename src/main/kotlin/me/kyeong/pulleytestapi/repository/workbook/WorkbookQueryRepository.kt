package me.kyeong.pulleytestapi.repository.workbook

import com.querydsl.jpa.JPAExpressions.select
import com.querydsl.jpa.impl.JPAQueryFactory
import me.kyeong.pulleytestapi.domain.problem.QProblemEntity.problemEntity
import me.kyeong.pulleytestapi.domain.user.grading.QGradingEntity.gradingEntity
import me.kyeong.pulleytestapi.domain.user.setting.QSettingEntity.settingEntity
import me.kyeong.pulleytestapi.domain.workbook.QWorkbookEntity.workbookEntity
import me.kyeong.pulleytestapi.domain.workbook.inclusion.QInclusionEntity.inclusionEntity
import me.kyeong.pulleytestapi.dto.response.AnalyzeResponse
import me.kyeong.pulleytestapi.dto.response.QAnalyzeResponse_CorrectAnswerRate
import me.kyeong.pulleytestapi.dto.response.QAnalyzeResponse_StudentInfo
import org.springframework.stereotype.Repository

@Repository
class WorkbookQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    /**
     * workbookId에 해당하는 학습지를 출제 받은 학생들의 학습 통계 데이터 조회
     */
    fun analyzeWorkbookStudentInfo(workbookId: Long): List<AnalyzeResponse.StudentInfo>? {
        return queryFactory
            .select(QAnalyzeResponse_StudentInfo(
                settingEntity.users.id,
                select(gradingEntity.correct.count())
                    .from(gradingEntity)
                    .where(
                        gradingEntity.setting.id.eq(settingEntity.id),
                        gradingEntity.correct.eq(true)
                    ),
                gradingEntity.id.count()
            ))
            .from(workbookEntity)
            .leftJoin(workbookEntity.settings, settingEntity)
            .leftJoin(settingEntity.gradings, gradingEntity)
            .groupBy(settingEntity.id)
            .fetch()
    }
    /**
     * workbookId에 해당하는 학습지에 포함된 문제들의 통계 데이터(문제별 정답률) 조회
     */
    fun analyzeWorkbookCorrectAnswerRate(workbookId: Long): List<AnalyzeResponse.CorrectAnswerRate>? {
        return queryFactory
            .select(QAnalyzeResponse_CorrectAnswerRate(
                    problemEntity.id,
                    select(gradingEntity.correct.count())
                        .from(gradingEntity)
                        .where(
                            gradingEntity.problem.id.eq(problemEntity.id),
                            gradingEntity.correct.eq(true)
                        ),
                    gradingEntity.id.count()
                ))
            .from(workbookEntity)
                .leftJoin(workbookEntity.inclusions, inclusionEntity)
                .leftJoin(inclusionEntity.problem, problemEntity)
                .leftJoin(problemEntity.gradings, gradingEntity)
            .groupBy(problemEntity.id)
            .fetch()
    }
}