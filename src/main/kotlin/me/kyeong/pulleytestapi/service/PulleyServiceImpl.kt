package me.kyeong.pulleytestapi.service

import me.kyeong.pulleytestapi.domain.problem.ProblemRepository
import me.kyeong.pulleytestapi.domain.user.UserRepository
import me.kyeong.pulleytestapi.domain.user.setting.SettingEntity
import me.kyeong.pulleytestapi.domain.user.setting.SettingRepository
import me.kyeong.pulleytestapi.domain.workbook.WorkbookEntity
import me.kyeong.pulleytestapi.domain.workbook.WorkbookRepository
import me.kyeong.pulleytestapi.domain.workbook.inclusion.InclusionEntity
import me.kyeong.pulleytestapi.domain.workbook.inclusion.InclusionRepository
import me.kyeong.pulleytestapi.dto.request.ProblemSearchCondition
import me.kyeong.pulleytestapi.dto.request.WorkBookCreateRequest
import me.kyeong.pulleytestapi.dto.response.ProblemResponse
import me.kyeong.pulleytestapi.dto.response.WorkbookResponse
import me.kyeong.pulleytestapi.repository.problem.ProblemQueryRepository
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
    private val inclusionRepository: InclusionRepository,
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository
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

    @Transactional
    override fun setWorkbook(workbookId: Long, studentIds: List<Long>) {
        val workbookEntity = workbookRepository.findByIdOrElseThrow(workbookId)
        val studentEntities = userRepository.findAllById(studentIds)
        settingRepository.saveAll(
            studentEntities
                .filter { student -> student.settings.none { setting -> setting.workbook.id == workbookEntity.id } }
                .map { SettingEntity(it, workbookEntity) }
        )
    }
}