package com.jose.hellodgs.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.io.UncheckedIOException

// Examplew model
// https://betterprogramming.pub/aws-java-sdk-v2-dynamodb-enhanced-client-with-kotlin-spring-boot-application-f880c74193a2
// https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/Customer.java

// Custom attribute converters
// https://stackoverflow.com/questions/65742825/dynamodb-attribute-converter-provider-for-enhanced-type-extending-hashmap

open class JacksonMapAttributeConverter<T> : AttributeConverter<T> {
    private val clazz: Class<T>

    constructor(clazz: Class<T>) {
        this.clazz = clazz
    }

    /**
     * Used on write operations to dynamodb table.
     */
    override fun transformFrom(input: T): AttributeValue {
        return try {
            AttributeValue
                .builder()
                .s(mapper.writeValueAsString(input))
                .build()
        } catch (e: JsonProcessingException) {
            throw UncheckedIOException("Unable to serialize object", e)
        }
    }

    /**
     * Used on read operations from dynamodb table.
     */
    override fun transformTo(input: AttributeValue): T {
        return try {
            println("original map: ${input.m()}")

            // Convert Map<String, AttributeValue> to Map<String, String>
            val newMap = mutableMapOf<String, String>()
            for ((key: String, value: AttributeValue) in input.m()) {
                println("${key}: ${value.s()}")
                newMap[key] = value.s() ?: "is not an string."
            }

            return mapper.convertValue(newMap, clazz)
        } catch (e: JsonProcessingException) {
            throw UncheckedIOException("Unable to parse object", e)
        }
    }

    override fun type(): EnhancedType<T>? {
        return EnhancedType.of(clazz)
    }

    override fun attributeValueType(): AttributeValueType {
        return AttributeValueType.S
    }

    // Static member.
    companion object {
        private val mapper = ObjectMapper()

        init {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
        }
    }
}

// Check converter example here https://github.com/aws/aws-sdk-java-v2/issues/2162#issuecomment-807852377
// https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/AttributeConverter.html
class PropertiesConverter : JacksonMapAttributeConverter<Properties> {
    constructor() : super(Properties::class.java)
}

@DynamoDbBean
data class Advertiser(
    // If you have no idea what the @get annotation does, it is originally a Java annotation that
    // is used to apply some annotation to the getter method of a property.
    // More on that in the Kotlin documentation.
    @get:DynamoDbPartitionKey
    var name: String? = null,
    var lastUpdatedBy: String? = null,

    // To support Map and List object you may use this:
    // https://github.com/aws/aws-sdk-java-v2/issues/1902
    // https://github.com/aws/aws-sdk-java-v2/blob/2.13.46/services-custom/dynamodb-enhanced/README.md#control-attribute-conversion
    @get:DynamoDbConvertedBy(PropertiesConverter::class)
    var properties: Properties? = null,
    var widgets: String? = null
)

// to understand the @JsonProperty anotration.
// https://stackoverflow.com/a/53192674/4086981
data class Properties(
    @JsonProperty("globalAssets")
    var globalAssets: String?,
    @JsonProperty("oddsFormat")
    var oddsFormat: String?,
    @JsonProperty("customerAssets")
    var customerAssets: String?,
    @JsonProperty("workbenchName")
    var workbenchName: String?,
    @JsonProperty("bookmaker")
    var bookmaker: String?,
    @JsonProperty("HIGHEST_PRICE_SELECTED")
    var highestPriceSelected: String?
)
