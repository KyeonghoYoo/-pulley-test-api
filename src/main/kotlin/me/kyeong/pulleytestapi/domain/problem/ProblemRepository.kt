package me.kyeong.pulleytestapi.domain.problem

import org.springframework.data.jpa.repository.JpaRepository

interface ProblemRepository : JpaRepository<ProblemEntity, Long> {
}