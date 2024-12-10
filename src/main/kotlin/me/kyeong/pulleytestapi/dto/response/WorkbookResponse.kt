package me.kyeong.pulleytestapi.dto.response

import me.kyeong.pulleytestapi.domain.user.UsersEntity
import me.kyeong.pulleytestapi.domain.workbook.WorkbookEntity
import me.kyeong.pulleytestapi.dto.ProblemDto

data class WorkbookResponse(
    val workbookId: Long?,
    val workbookName: String,
    val userInfo: UserInfoDto,
    val problemList: List<ProblemDto>
) {
    companion object {
        fun of(workbookEntity: WorkbookEntity): WorkbookResponse {
            return WorkbookResponse(
                workbookEntity.id,
                workbookEntity.name,
                UserInfoDto.of(workbookEntity.user),
                workbookEntity.inclusions.map { ProblemDto(it.problem)}
            )
        }
    }

    data class UserInfoDto(
        val userId: Long?,
        val type: String
    ) {
        companion object {
            fun of(usersEntity: UsersEntity): UserInfoDto {
                return UserInfoDto(usersEntity.id, usersEntity.type.name)
            }
        }
    }
}