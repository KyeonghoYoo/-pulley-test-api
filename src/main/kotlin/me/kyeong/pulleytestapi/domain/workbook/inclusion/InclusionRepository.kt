package me.kyeong.pulleytestapi.domain.workbook.inclusion

import org.springframework.data.jpa.repository.JpaRepository

interface InclusionRepository : JpaRepository<InclusionEntity, Long> {
    fun findByWorkbookIdAndProblemIdIn(workbookId: Long?, problemIds: Collection<Long>): List<InclusionEntity>
}