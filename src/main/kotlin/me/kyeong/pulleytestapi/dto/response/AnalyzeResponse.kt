package me.kyeong.pulleytestapi.dto.response

import com.querydsl.core.annotations.QueryProjection

data class AnalyzeResponse(
    val workbookId: Long?,
    val workbookName: String?,
    val studentInfoList: List<StudentInfo>?,
    val correctAnswerRateList: List<CorrectAnswerRate>?
) {

    data class StudentInfo(
        val studentId: Long?,
        val correctAnswerCount: Long?,
        val totalAnswerCount: Long?,
        val workbookCorrectAnswerRate: Long?,
    ) {
        @QueryProjection constructor(
            studentId: Long?,
            correctAnswerCount: Long?,
            totalAnswerCount: Long?
        ) : this(
            studentId,
            correctAnswerCount,
            totalAnswerCount,
            totalAnswerCount?.let { total ->
            if (total != 0L)
                correctAnswerCount?.let { if (it != 0L) (it.times(100L)).div(total) else 0L}
            else
                0L
            }
        )
    }
    data class CorrectAnswerRate(
        val problemId: Long?,
        val correctAnswerCount: Long?,
        val totalAnswerCount: Long?,
        val correctAnswerRate: Long?
    ) {
        @QueryProjection constructor(
            problemId: Long?,
            correctAnswerCount: Long?,
            totalAnswerCount: Long?
        ) : this(
            problemId,
            correctAnswerCount,
            totalAnswerCount,
            totalAnswerCount?.let { total ->
                if (total != 0L)
                    correctAnswerCount?.let { if (it != 0L) (it.times(100L)).div(total) else 0L}
                else
                    0L
            }
        )
    }
}