import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.netflix.dgs.codegen") version "5.1.16"

	id("org.springframework.boot") version "2.6.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

group = "com.jose"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

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
