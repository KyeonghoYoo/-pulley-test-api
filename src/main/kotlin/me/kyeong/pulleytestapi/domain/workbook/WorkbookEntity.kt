package me.kyeong.pulleytestapi.domain.workbook

import jakarta.persistence.*
import me.kyeong.pulleytestapi.domain.workbook.inclusion.InclusionEntity

/**
 * 학습지 엔티티
 */
@Entity
@Table(name = "workbook")
class WorkbookEntity(
    /**
     * 학습지 이름
     */
    var name: String,

    /**
     * 만든 사람 정보(회원 엔티티와의 연관관계 매핑)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: me.kyeong.pulleytestapi.domain.user.UsersEntity,
    @OneToMany(mappedBy = "workbook")
    var inclusions: MutableList<InclusionEntity> = ArrayList(),
    @OneToMany(mappedBy = "workbook")
    var settings: MutableList<me.kyeong.pulleytestapi.domain.user.setting.SettingEntity> = ArrayList(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workbook_id", nullable = false)
    var id: Long? = null
) {
    init {
        require(name.isNotBlank()) { "이름은 비어있을 수 없습니다." }
    }
}