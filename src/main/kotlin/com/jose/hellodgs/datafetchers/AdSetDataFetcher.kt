package com.jose.hellodgs.datafetchers

import com.jose.hellodgs.entities.AdSet
import com.jose.hellodgs.entities.AdSetProperties
import com.jose.hellodgs.repositories.AdSetRepository
import com.jose.hellodgs.types.AdSetPropertiesInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import java.util.*

// Check examnple:
// https://gitlab.betgenius.net/aws-portfolio-devops/gsg-gsm/reap/kotlin-services/-/blob/master/services/syndication-api/src/main/kotlin/com/geniussports/syndication/controllers/XandrController.kt
// https://spring.io/guides/gs/accessing-data-jpa/

@DgsComponent
class AdSetDataFetcher(
    val adSetRepository: AdSetRepository
) {

    @DgsMutation
    fun newAdSet(
        @InputArgument advertiserName: String,
        @InputArgument modifiedBy: String,
        @InputArgument properties: AdSetPropertiesInput
    ): AdSet {
        val adSet = adSetRepository.save(AdSet(
            advertiserName = advertiserName,
            lastModifiedBy = modifiedBy,
            properties = AdSetProperties(
                workbenchName = properties.workbenchName,
                bookmaker = properties.bookmaker
            )
        ))
        return adSet
    }

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