package me.kyeong.pulleytestapi.dto.request

data class SettingWorkbookGradeRequest (
    val gradingList: List<GradeRequest>
) {

    data class GradeRequest(
        val problemId: Long,
        val grade: Boolean
    )
}