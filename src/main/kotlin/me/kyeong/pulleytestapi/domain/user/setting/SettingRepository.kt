package me.kyeong.pulleytestapi.domain.user.setting

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface SettingRepository : JpaRepository<SettingEntity, Long> {

    @Query("select s from SettingEntity s left join fetch s.gradings where s.id = :id")
    fun findSettingByIdFetchJoinGrading(id: Long): Optional<SettingEntity>
}