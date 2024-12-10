package me.kyeong.pulleytestapi.util

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun <ID> failOnFindingById(id: ID): Nothing {
    throw IllegalArgumentException("ID '$id'를 가진 엔티티는 존재하지 않습니다.")
}

fun <T, ID> JpaRepository<T, ID>.findByIdOrElseThrow(id: ID): T {
    return this.findByIdOrNull(id) ?: failOnFindingById(id)
}