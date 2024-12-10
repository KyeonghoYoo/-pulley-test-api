package me.kyeong.pulleytestapi.domain.user.setting

import org.springframework.data.jpa.repository.JpaRepository

interface SettingRepository : JpaRepository<SettingEntity, Long> {
}