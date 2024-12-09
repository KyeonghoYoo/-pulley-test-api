package me.kyeong.pulleytestapi.domain.workbook.inclusion

import jakarta.persistence.*
import me.kyeong.pulleytestapi.domain.problem.ProblemEntity
import me.kyeong.pulleytestapi.domain.workbook.WorkbookEntity

/**
 * 문제 포함 엔티티
 */
@Entity
@Table(name = "inclusion")
class InclusionEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    var workbook: WorkbookEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    var problem: ProblemEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inclusion_id")
    var id: Long? = null
) {

}