package me.kyeong.pulleytestapi.domain.user

import jakarta.persistence.*
import me.kyeong.pulleytestapi.domain.user.setting.SettingEntity

/**
 * 회원 엔티티
 */
@Entity
@Table(name = "users")
class UsersEntity(
    /**
     * 회원 유형 { 학생, 선생님 }
     */
    @Enumerated(EnumType.STRING)
    var type: UserType,

    @OneToMany(mappedBy = "users")
    var settings: MutableList<SettingEntity> = ArrayList(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    var id: Long? = null
) {

    fun isStudent(): Boolean = type == UserType.STUDENT
    fun isTeacher(): Boolean = type == UserType.TEACHER

    companion object Factory {
        fun student(): UsersEntity = UsersEntity(UserType.STUDENT)
        fun teacher(): UsersEntity = UsersEntity(UserType.TEACHER)
    }
}