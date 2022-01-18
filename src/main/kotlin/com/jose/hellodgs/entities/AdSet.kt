package com.jose.hellodgs.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import javax.persistence.*


// example
// https://spring.io/guides/gs/accessing-data-jpa/

// TODO: Use generic classes.
@Converter(autoApply = true)
class JpaConverterJson : AttributeConverter<AdSetProperties?, String?> {
    override fun convertToDatabaseColumn(meta: AdSetProperties?): String? {
        return try {
            objectMapper.writeValueAsString(meta)
        } catch (ex: JsonProcessingException) {
            null
            // or throw an error
        }
    }

    override fun convertToEntityAttribute(dbData: String?): AdSetProperties? {
        return try {
            objectMapper.readValue(dbData, AdSetProperties::class.java)
        } catch (ex: IOException) {
            // logger.error("Unexpected IOEx decoding json from database: " + dbData);
            null
        }
    }

    companion object {
        private val objectMapper = ObjectMapper()
    }
}

@Entity
@Table(name = "adsets")
data class AdSet (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "advertiser_name")
    val advertiserName: String? = null,
    @Column(name = "last_modified_by")
    val lastModifiedBy: String? = null,
    @Column(name = "properties")
    @Convert(converter = JpaConverterJson::class)
    val properties: AdSetProperties? = null
) {
    override fun toString(): String {
        return "Customer[id='$id', advertiserName='$advertiserName', lastModifiedBy='$lastModifiedBy', properties='$properties']"
    }
}

data class AdSetProperties(
    @JsonProperty("workbenchName")
    var workbenchName: String?,
    @JsonProperty("bookmaker")
    var bookmaker: String?
)
