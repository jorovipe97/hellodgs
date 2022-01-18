package com.jose.hellodgs.datafetchers

import com.jose.hellodgs.entities.AdSet
import com.jose.hellodgs.entities.Advertiser
import com.jose.hellodgs.repositories.AdSetRepository
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.util.*
import javax.persistence.Id

// Check examnple:
// https://gitlab.betgenius.net/aws-portfolio-devops/gsg-gsm/reap/kotlin-services/-/blob/master/services/syndication-api/src/main/kotlin/com/geniussports/syndication/controllers/XandrController.kt

@DgsComponent
class AdSetDataFetcher(
    val adSetRepository: AdSetRepository
) {

    @DgsQuery
    fun adset(
        @InputArgument id: String?
    ): Optional<AdSet?> {

        return adSetRepository.findById(id?.toLongOrNull() ?: 0)
    }

    @DgsQuery
    fun adsetByAdvertiserName(
        @InputArgument name: String?
    ): AdSet? {

        return adSetRepository.findByAdvertiserName(name ?: "")
    }

    @DgsQuery
    fun allAdSetsByAdvertiserName(
        @InputArgument name: String?
    ): List<AdSet?> {

        // TODO(To implement navigation we would need to implement a custom Pageable class
        //  https://stackoverflow.com/a/36365522/4086981)

        return adSetRepository.findAllByAdvertiserName(name ?: "")
    }
}