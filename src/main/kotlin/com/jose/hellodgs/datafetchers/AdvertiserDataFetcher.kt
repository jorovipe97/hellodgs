package com.jose.hellodgs.datafetchers

import com.fasterxml.jackson.databind.ObjectMapper
import com.jose.hellodgs.entities.Advertiser
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException

// https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-enhanced.html
// https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/EnhancedGetItem.java

@DgsComponent
class AdvertiserDataFetcher {

    @DgsQuery
    fun advertiser(
        @InputArgument name: String?
    ): Advertiser? {
        if (name == null) return null

        val region: Region = Region.EU_WEST_1
        val ddb = DynamoDbClient.builder()
            .region(region)
            .build()

        val enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build()

        val result: Advertiser? = getItem(enhancedClient, name)
        println(result?.name)
        println(result?.lastUpdatedBy)
        println(result?.properties)
//        println(result?.getWidgets())

        return result
    }

    fun getItem(enhancedClient: DynamoDbEnhancedClient, advertiserName: String?): Advertiser? {
        if (advertiserName == null) return null

        try {
            // Create a DynamoDbTable object
            val mappedTable: DynamoDbTable<Advertiser> =
                enhancedClient.table("fep-adverts", TableSchema.fromBean(Advertiser::class.java))

            // Create a KEY object
            val key = Key.builder()
                .partitionValue(advertiserName)
                .build()

            // Get the item by using the key
            val result: Advertiser? = mappedTable.getItem({ r: GetItemEnhancedRequest.Builder ->
                r.key(
                    key
                )
            })

            return result
        } catch (e: DynamoDbException) {
            println(e.message)
        }

        return null
    }
}