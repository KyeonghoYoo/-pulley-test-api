package me.kyeong.pulleytestapi.domain.problem

import org.springframework.data.jpa.repository.JpaRepository

interface ProblemEntityRepository : JpaRepository<ProblemEntity, Long> {
}