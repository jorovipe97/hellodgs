package com.jose.hellodgs.models

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

// Examplew model
// https://betterprogramming.pub/aws-java-sdk-v2-dynamodb-enhanced-client-with-kotlin-spring-boot-application-f880c74193a2
// https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/Customer.java

@DynamoDbBean
data class Advertiser(
    // If you have no idea what the @get annotation does, it is originally a Java annotation that
    // is used to apply some annotation to the getter method of a property.
    // More on that in the Kotlin documentation.
    @get:DynamoDbPartitionKey val name: String? = null,
    val lastUpdatedBy: String? = null,
    val properties: String? = null,
    val widgets: String? = null
)
