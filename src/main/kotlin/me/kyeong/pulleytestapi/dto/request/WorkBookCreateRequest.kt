package me.kyeong.pulleytestapi.dto.request


data class WorkBookCreateRequest(
    val userId: Long,
    val workbookName: String,
    val problemIdList: List<Long>
) {
}