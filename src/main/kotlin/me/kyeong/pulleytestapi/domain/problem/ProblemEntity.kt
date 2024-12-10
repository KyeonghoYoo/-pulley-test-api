package me.kyeong.pulleytestapi.domain.problem

import jakarta.persistence.*
import me.kyeong.pulleytestapi.config.ProblemLevelConverter
import me.kyeong.pulleytestapi.domain.workbook.inclusion.InclusionEntity

/**
 * 문제 엔티티
 */
@Entity
@Table(name = "problem")
class ProblemEntity(
    /**
     * 유형코드
     */
    var unitCode: String,
    /**
     * 난이도, 높은 숫자일수록 어려움, { 1, 2, 3, ,4, 5 }
     * @see ProblemLevel
     */
    @Convert(converter = ProblemLevelConverter::class)
    var level: ProblemLevel,
    /**
     * 문제유형, 전체, 주관식, 객관식, { ALL, SUBJECTIVE, SELECTION }
     * @see ProblemType
     */
    @Enumerated(EnumType.STRING)
    var type: ProblemType,
    /**
     * 정답
     */
    var answer: String,

    @OneToMany(mappedBy = "problem")
    var inclusions: MutableList<InclusionEntity> = ArrayList(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id", nullable = false)
    var id: Long? = null
) {

}