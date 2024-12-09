package me.kyeong.pulleytestapi.domain.user.grading

import jakarta.persistence.*
import me.kyeong.pulleytestapi.domain.user.setting.SettingEntity
import me.kyeong.pulleytestapi.domain.workbook.inclusion.InclusionEntity

/**
 * 채점 엔티티
 */
@Entity
@Table(name = "grading")
class GradingEntity(
    /**
     * 채점 상태
     */
    var status: GradingStatus = GradingStatus.NONE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id", nullable = false)
    var setting: SettingEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inclusion_id", nullable = false)
    var inclusion: InclusionEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grading_id")
    var id: Long? = null
) {

    companion object Factory {
        fun correct(setting: SettingEntity, inclusion: InclusionEntity): GradingEntity = GradingEntity(GradingStatus.CORRECT, setting, inclusion)
        fun wrong(entity: SettingEntity, inclusion: InclusionEntity): GradingEntity = GradingEntity(GradingStatus.WRONG, entity, inclusion)
    }
}