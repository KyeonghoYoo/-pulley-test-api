package me.kyeong.pulleytestapi.domain.problem

enum class ProblemType {
    ALL,
    SUBJECTIVE,
    SELECTION,
    ;

    companion object {
        fun valueOf(value: String): ProblemType = entries.single { value.equals(it.name, true) }
    }
}