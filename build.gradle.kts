import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
	id("com.netflix.dgs.codegen") version "5.1.16"
	id("org.springframework.boot") version "2.6.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.allopen") version "1.4.32"
}

group = "com.jose"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

// About the allOpen plugin
// https://stackoverflow.com/a/58470332
// Basically marks all clases anotated with these annotations
// to be open, by default kotlin classes are sealed.
allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")
	// The Bill of material (BOM) of the awssdk.
	// https://mvnrepository.com/artifact/software.amazon.awssdk
	// sdk v2 does not support the Document API which is a easier client for dynamodb.
	// however it supports the enhanced client which maps items to classes.
	//	https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-enhanced.html
	// https://github.com/aws/aws-sdk-java-v2/issues/34

	implementation(platform("software.amazon.awssdk:bom:2.17.112"))
	implementation("software.amazon.awssdk:dynamodb")
	// How to install the enhanced dynamodb client.
	implementation("software.amazon.awssdk:dynamodb-enhanced")


	// developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Follow this tutorial to setup the DynamoDB sdk official docs dont explain how to do it with kotlin
// https://medium.com/swlh/how-to-build-a-reactive-microservice-api-with-spring-boot-spring-webflux-and-dynamodb-using-kotlin-e1be3e99b15e
//configure<DependencyManagementExtension> {
//	imports {
//		mavenBom("software.amazon.awssdk:bom:2.17.112")
//	}
//}

tasks {
	generateJava {
		// List of directories containing schema files
		// https://stackoverflow.com/a/60859378/4086981
		schemaPaths = listOf("${projectDir}/src/main/resources/schema").toMutableList()
		packageName = "com.jose.hellodgs" // The package name to use to generate sources
		generateClient = true // Enable generating the type safe query API
		language = "kotlin"
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
