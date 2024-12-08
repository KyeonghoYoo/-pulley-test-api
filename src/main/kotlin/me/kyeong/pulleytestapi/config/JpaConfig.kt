package me.kyeong.pulleytestapi.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JpaConfig(
    @PersistenceContext private val entityManager: EntityManager
) {

    @Bean
    fun queryDsl(): JPAQueryFactory = JPAQueryFactory(entityManager)
}