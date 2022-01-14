package com.jose.hellodgs.datafetchers

// Types are available as part of the package specified by the packageName.types, where you specify the value of packageName as a configuration in your build.gradle file.
import com.jose.hellodgs.types.Show
import com.jose.hellodgs.types.Actor
import com.jose.hellodgs.DgsConstants
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.schema.DataFetchingEnvironment

class ActorsService {
    fun getActorsByShowTitle(showTitle: String?): List<Actor> {
        return listOf(
            Actor("Michael C. Hall", 42),
            Actor("Jennifer Carpenter", 38),
            Actor("Joey Quinn", 28)
        )
    }
}

@DgsComponent
class ShowsDataFetcher {
    private val shows = listOf(
        Show("000", "Stranger Things", 2016),
        Show("001","Ozark", 2017),
        Show("002", "The Crown", 2016),
        Show("003","Dead to Me", 2019),
        Show(id = "004","Orange is the New Black", 2013)
    )

    private val actorsService = ActorsService()

    @DgsQuery
    fun shows(@InputArgument titleFilter : String?): List<Show> {
        return if (titleFilter != null) {
            shows.filter { show ->  show.title!!.contains(titleFilter) }
            // Can also be written in this form.
            // shows.filter { it.title.contains(titleFilter) }
        } else {
            shows
        }
    }

    // What if there is an extra cost to specific fields? For example, what if loading actors for a show requires an extra query? It would be wasteful to run the extra query to load actors if the actors field doesn't get returned to the user.
    // In such scenarios, it's better to create a separate datafetcher for the expensive field.

    // https://netflix.github.io/dgs/datafetching/
    // The actors datafetcher only gets executed when the actors field is included in the query. The actors datafetcher also introduces a new concept; the DgsDataFetchingEnvironment. The DgsDataFetchingEnvironment gives access to the context, the query itself, data loaders, and the source object. The source object is the object that contains the field. For this example, the source is the Show object, which you can use to get the show's identifier to use in the query for actors.
//    @DgsData(parentType = "Show", field = "actors")
//    fun actors(dataFetchingEnvironment: DataFetchingEnvironment?): List<Actor> {
//        val show = dataFetchingEnvironment?.getSource<Show>()
//        val actors = actorsService.getActorsByShowTitle(show?.title)
//        return actors
//    }

    // The benefit of using constants is that you can detect issues between your schema and datafetchers at compile time.
    // https://netflix.github.io/dgs/datafetching/#codegen-constants
    @DgsData(parentType = DgsConstants.SHOW.TYPE_NAME, field = DgsConstants.SHOW.Actors)
    fun actors(dataFetchingEnvironment: DataFetchingEnvironment?): List<Actor> {
        val show = dataFetchingEnvironment?.getSource<Show>()
        val actors = actorsService.getActorsByShowTitle(show?.title)
        return actors
    }

    // If the field parameter is not set, the method name will be used as the field name. The @DgsQuery, @DgsMutation
    // and @DgsSubscription annotations are shorthands to define datafetchers on the Query, Mutation and
    // Subscription types. The following definitions are all equivalent.
//    @DgsData(parentType = "Query", field = "shows")
//    fun getShows(dataFetchingEnvironment: DataFetchingEnvironment?,
//                 @InputArgument titleFilter : String?): List<Show?>? {
//        return if (titleFilter != null) {
//            shows.filter { show -> show.title!!.contains(titleFilter)  }
//            // Can also be written in this form.
//            // shows.filter { it.title.contains(titleFilter) }
//        } else {
//            shows
//        }
//    }
}
