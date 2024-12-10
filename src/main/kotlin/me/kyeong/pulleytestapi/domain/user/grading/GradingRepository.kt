package me.kyeong.pulleytestapi.domain.user.grading

import org.springframework.data.jpa.repository.JpaRepository

interface GradingRepository : JpaRepository<GradingEntity, Long> {
}