package me.kyeong.pulleytestapi.domain.user

enum class UserType {
    STUDENT, // 학생
    TEACHER, // 선생님
    ;

    companion object {
        fun valueOf(value: String): UserType = entries.single { it.name.equals(value, true) }
    }
}