package me.kyeong.pulleytestapi.config

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import me.kyeong.pulleytestapi.domain.problem.ProblemLevel

@Converter
class ProblemLevelConverter : AttributeConverter<ProblemLevel, Int> {
    override fun convertToDatabaseColumn(attribute: ProblemLevel?): Int? {
        return attribute?.value
    }

    override fun convertToEntityAttribute(dbData: Int?): ProblemLevel? {
        return if (dbData == null) null else ProblemLevel.valueOf(dbData)
    }

}