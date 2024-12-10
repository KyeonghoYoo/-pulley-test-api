package me.kyeong.pulleytestapi.dto.request

data class ProblemSearchCondition(
    /**
     * 총 문제수(최대 문제수)
     */
    val totalCount: Int = 20,
    /**
     * 유형 코드 리스트
     */
    val unitCodeList: List<String>? = null,
    /**
     * 문제 유형
     */
    val problemType: String? = null,
    /**
     * 난이도 { 상(HIGH), 중(MIDDLE), 하(LOW) }
     */
    val level: String? = "MIDDLE",
)