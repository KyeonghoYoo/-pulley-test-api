package me.kyeong.pulleytestapi.dto.request

import jakarta.validation.constraints.Max


data class WorkBookCreateRequest(
    @Max(50L) val userId: Long,
    val workbookName: String,
    val problemIdList: List<Long>
) {
}