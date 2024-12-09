package me.kyeong.pulleytestapi.domain.problem

enum class ProblemLevel(
    val level: Int
) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    ;

    companion object {
        fun valueOf(value: String): ProblemLevel = entries.single { it.name.equals(value, true) }
        fun valueOf(value: Int): ProblemLevel = entries.single { it.level == value }
    }
}