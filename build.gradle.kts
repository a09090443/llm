plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.allopen") version "2.0.10"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-resteasy-reactive-kotlin")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-websockets-next")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-mailer")

    implementation("io.quarkiverse.langchain4j:quarkus-langchain4j-openai:0.19.0")
    implementation("io.quarkiverse.langchain4j:quarkus-langchain4j-ollama:0.19.0")
//    implementation("io.quarkiverse.langchain4j:quarkus-langchain4j-chroma:0.19.0")
//    implementation("io.quarkiverse.langchain4j:quarkus-langchain4j-milvus:0.19.0")
//    implementation("io.quarkiverse.langchain4j:quarkus-langchain4j-easy-rag:0.19.0")

    implementation("dev.langchain4j:langchain4j-easy-rag:0.35.0")
    implementation("dev.langchain4j:langchain4j-chroma:0.35.0")
    implementation("dev.langchain4j:langchain4j-milvus:0.35.0")
    implementation("dev.langchain4j:langchain4j-web-search-engine-google-custom:0.35.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.7.3")

    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "ai.llm"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}
