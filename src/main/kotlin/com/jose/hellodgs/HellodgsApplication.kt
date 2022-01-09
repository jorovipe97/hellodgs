package com.jose.hellodgs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

import com.netflix.graphql.dgs.DgsComponent
// import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@SpringBootApplication
class HellodgsApplication

// From:
// https://netflix.github.io/dgs/getting-started/

// Data fetchers are responsible for returning data for a query.
// Note that we have a Codegen plugin that can do this automatically, but in this guide we'll manually write the classes.

// Start the application and open a browser to http://localhost:8080/graphiql. GraphiQL is a query editor that comes out of the box with the DGS framework.

// See the official example project here:
// https://github.com/Netflix/dgs-examples-kotlin
// Example datafetcher:
// https://github.com/Netflix/dgs-examples-kotlin/blob/main/src/main/kotlin/com/example/demo/datafetchers/ShowsDataFetcher.kt

@DgsComponent
class ShowsDataFetcher {
	private val shows = listOf(
		Show("Stranger Things", 2016),
		Show("Ozark", 2017),
		Show("The Crown", 2016),
		Show("Dead to Me", 2019),
		Show("Orange is the New Black", 2013))

	@DgsQuery
	fun shows(@InputArgument titleFilter : String?): List<Show> {
		return if(titleFilter != null) {
			shows.filter { it.title.contains(titleFilter) }
		} else {
			shows
		}
	}

	data class Show(val title: String, val releaseYear: Int)
}

fun main(args: Array<String>) {
	runApplication<HellodgsApplication>(*args)
}
