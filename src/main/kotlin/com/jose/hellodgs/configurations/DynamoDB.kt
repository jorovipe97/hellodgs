package com.jose.hellodgs.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
open class DynamoDB(
    @Value("\${aws.dynamo.region}") private val region: String,
    @Value("\${aws.dynamo.endpoint}") private val endpoint: String?
) {

    // by default, Spring beans are Singletons. they are created once and injected each time they are autowired.
    // However, if you'd like a new instance of the bean each time it is autowired, then you need to specify
    // a "scope" of type "prototype"
    // https://stackoverflow.com/a/64764733/4086981
    @Bean
    open fun dynamoEnhancedClient(): DynamoDbEnhancedClient {
        val ddb = DynamoDbClient.builder()
            .region(Region.of(region))
//            .endpointOverride(URI.create(endpoint))
            .build()

        val enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build()

        return enhancedClient
    }

}