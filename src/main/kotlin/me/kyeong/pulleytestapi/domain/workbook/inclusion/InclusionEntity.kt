package me.kyeong.pulleytestapi.domain.workbook.inclusion

import jakarta.persistence.*
import me.kyeong.pulleytestapi.domain.problem.ProblemEntity
import me.kyeong.pulleytestapi.domain.workbook.WorkbookEntity

/**
 * 문제 포함 엔티티
 */
@Entity
@SequenceGenerator(
    name = "INCLUSION_SEQ_GENERATOR",
    sequenceName = "INCLUSION_SEQ",
    initialValue = 1,
    allocationSize = 100
)
@Table(name = "inclusion")
class InclusionEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    var workbook: WorkbookEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    var problem: ProblemEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INCLUSION_SEQ_GENERATOR")
    @Column(name = "inclusion_id")
    var id: Long? = null
) {

}