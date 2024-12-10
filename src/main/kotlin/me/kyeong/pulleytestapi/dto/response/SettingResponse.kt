package me.kyeong.pulleytestapi.dto.response

import me.kyeong.pulleytestapi.domain.user.setting.SettingEntity

class SettingResponse(
    val settingList: List<SettingDto>
) {
    companion object {
        fun of(settingEntities: List<SettingEntity>): SettingResponse {
            return SettingResponse(settingEntities.map(SettingDto::of))
        }
    }

    data class SettingDto(
        val settingId: Long?,
        val userId: Long?,
        val workbookId: Long?
    ) {
        companion object {
            fun of(setting: SettingEntity): SettingDto {
                return SettingDto(setting.id, setting.users.id, setting.workbook.id)
            }
        }
    }
}