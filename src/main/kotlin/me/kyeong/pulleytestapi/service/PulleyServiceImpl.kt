package me.kyeong.pulleytestapi.service

import me.kyeong.pulleytestapi.domain.problem.ProblemRepository
import me.kyeong.pulleytestapi.domain.user.UserRepository
import me.kyeong.pulleytestapi.domain.user.grading.GradingEntity
import me.kyeong.pulleytestapi.domain.user.grading.GradingRepository
import me.kyeong.pulleytestapi.domain.user.setting.SettingEntity
import me.kyeong.pulleytestapi.domain.user.setting.SettingRepository
import me.kyeong.pulleytestapi.domain.workbook.WorkbookEntity
import me.kyeong.pulleytestapi.domain.workbook.WorkbookRepository
import me.kyeong.pulleytestapi.domain.workbook.inclusion.InclusionEntity
import me.kyeong.pulleytestapi.domain.workbook.inclusion.InclusionRepository
import me.kyeong.pulleytestapi.dto.request.ProblemSearchCondition
import me.kyeong.pulleytestapi.dto.request.SettingWorkbookGradeRequest
import me.kyeong.pulleytestapi.dto.request.WorkBookCreateRequest
import me.kyeong.pulleytestapi.dto.response.AnalyzeResponse
import me.kyeong.pulleytestapi.dto.response.ProblemResponse
import me.kyeong.pulleytestapi.dto.response.SettingResponse
import me.kyeong.pulleytestapi.dto.response.WorkbookResponse
import me.kyeong.pulleytestapi.repository.problem.ProblemQueryRepository
import me.kyeong.pulleytestapi.repository.workbook.WorkbookQueryRepository
import me.kyeong.pulleytestapi.util.failOnFindingById
import me.kyeong.pulleytestapi.util.findByIdOrElseThrow
import me.kyeong.pulleytestapi.util.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PulleyServiceImpl(
    private val userResponse: UserRepository,
    private val problemRepository: ProblemRepository,
    private val problemQueryRepository: ProblemQueryRepository,
    private val workbookRepository: WorkbookRepository,
    private val workbookQueryRepository: WorkbookQueryRepository,
    private val inclusionRepository: InclusionRepository,
    private val settingRepository: SettingRepository,
    private val gradingRepository: GradingRepository,
    private val userRepository: UserRepository
) : PulleyService {

    val log = logger()

    /**
     * 문제 조회
     */
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

    /**
     * 학습지 생성
     */
    @Transactional
    override fun createWorkbook(request: WorkBookCreateRequest): WorkbookResponse {
        val (userId, workbookName, problemIdList) = request
        val usersEntity = userResponse.findByIdOrElseThrow(userId)
        val workbookEntity = workbookRepository.save(WorkbookEntity(name = workbookName, user = usersEntity))

        val inclusionEntities = inclusionRepository.saveAll(
            problemRepository.findAllById(problemIdList)
                .map { problemEntity -> InclusionEntity(workbookEntity, problemEntity) }
        )
        workbookEntity.addAllInclusions(inclusionEntities)

        return WorkbookResponse.of(workbookEntity)
    }

    /**
     * 학생에게 학습지 출제하기
     */
    @Transactional
    override fun setWorkbook(workbookId: Long, studentIds: List<Long>): SettingResponse {
        val workbookEntity = workbookRepository.findByIdOrElseThrow(workbookId)
        val studentEntities = userRepository.findAllById(studentIds)
        val settingEntities = settingRepository.saveAll(
            studentEntities
                .filter { student -> student.settings.none { setting -> setting.workbook.id == workbookEntity.id } }
                .map { SettingEntity(it, workbookEntity) }
        )
        return SettingResponse.of(settingEntities)
    }

    /**
     * 학습지의 문제 조회하기
     */
    override fun getProblemsInSettingWorkbook(settingId: Long): WorkbookResponse {
        val settingEntity = settingRepository.findByIdOrElseThrow(settingId)
        return WorkbookResponse.of(settingEntity.workbook)
    }

    /**
     * 채점하기
     */
    @Transactional
    override fun gradeSettingWorkbook(settingId: Long, request: SettingWorkbookGradeRequest) {
        val settingEntity = settingRepository.findSettingByIdFetchJoinGrading(settingId)
            .orElseThrow { failOnFindingById(settingId) }
        val gradingList = request.gradingList

        val gradingMap = mutableMapOf(*gradingList.map { it.problemId to it.grade }.toTypedArray())
        println(gradingMap)

        val inclusionEntities =
            inclusionRepository.findByWorkbookIdAndProblemIdIn(settingEntity.workbook.id, gradingMap.keys)
        require (inclusionEntities.map { it.problem.id }.containsAll(gradingMap.keys)) {
            "학습지에 존재하지 않는 문제가 포함되어 있습니다."
        }

        settingEntity.gradings
            .filter { gradingMap.keys.contains(it.problem.id) }
            .forEach {
                it.updateCorrect(gradingMap[it.problem.id])
                gradingMap.remove(it.problem.id)
            }
        gradingRepository.saveAll(
            gradingMap.entries
                .map { GradingEntity(it.value, settingEntity, problemRepository.getReferenceById(it.key)) }
        )
    }

    override fun getAnalyzeOfWorkbook(workbookId: Long): AnalyzeResponse {
        val workbookEntity = workbookRepository.findByIdOrElseThrow(workbookId)
        return AnalyzeResponse(
            workbookEntity.id,
            workbookEntity.name,
            workbookQueryRepository.analyzeWorkbookStudentInfo(workbookId),
            workbookQueryRepository.analyzeWorkbookCorrectAnswerRate(workbookId)
        )
    }
}