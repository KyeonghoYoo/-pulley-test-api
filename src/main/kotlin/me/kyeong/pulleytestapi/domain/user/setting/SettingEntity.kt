package me.kyeong.pulleytestapi.domain.user.setting

import jakarta.persistence.*
import me.kyeong.pulleytestapi.domain.user.UsersEntity
import me.kyeong.pulleytestapi.domain.user.grading.GradingEntity
import me.kyeong.pulleytestapi.domain.workbook.WorkbookEntity

/**
 * 출제 엔티티
 */
@Entity
@Table(name = "setting")
class SettingEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var users: UsersEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    var workbook: WorkbookEntity,

    @OneToMany(mappedBy = "setting")
    var gradings: MutableList<GradingEntity> = ArrayList(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    var id: Long? = null
) {
}