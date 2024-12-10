package me.kyeong.pulleytestapi.domain.user.grading

import jakarta.persistence.*
import me.kyeong.pulleytestapi.domain.problem.ProblemEntity
import me.kyeong.pulleytestapi.domain.user.setting.SettingEntity

/**
 * 채점 엔티티
 */
@Entity
@Table(name = "grading")
class GradingEntity(
    /**
     * 채점 결과 { true = 맞음, false = 틀림 }
     */
    var correct: Boolean?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id", nullable = false)
    var setting: SettingEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    var problem: ProblemEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grading_id")
    var id: Long? = null
) {

    fun updateCorrect(correct: Boolean?) {
        this.correct = correct
    }
}