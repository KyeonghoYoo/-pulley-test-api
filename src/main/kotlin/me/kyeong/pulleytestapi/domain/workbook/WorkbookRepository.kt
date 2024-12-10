package me.kyeong.pulleytestapi.domain.workbook

import org.springframework.data.jpa.repository.JpaRepository

interface WorkbookRepository : JpaRepository<WorkbookEntity, Long> {
}