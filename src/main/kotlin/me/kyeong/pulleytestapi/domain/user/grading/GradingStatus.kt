package me.kyeong.pulleytestapi.domain.user.grading

enum class GradingStatus {
    NONE,
    CORRECT,
    WRONG,
    ;

    companion object {
        fun valueOf(value: String): GradingStatus = entries.single { it.name.equals(value, true) }
    }
}