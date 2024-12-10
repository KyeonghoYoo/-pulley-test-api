package me.kyeong.pulleytestapi.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UsersEntity, Long> {
}