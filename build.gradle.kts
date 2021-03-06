import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.js.resolve.JsPlatform

plugins {
    kotlin("jvm") version "1.3.0"
    antlr
    `maven-publish`
}

group = "com.suushiemaniac"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    antlr("org.antlr:antlr4:4.7+")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    create<Jar>("sourcesJar") {
        classifier = "sources"

        from(sourceSets["main"].output)
        dependsOn("classes")
    }

    getByName<AntlrTask>("generateGrammarSource") {
        arguments = arguments + listOf("-visitor", "-no-listener", "-package", "com.suushiemaniac.lang.json.antlr")
    }

    getByName<KotlinCompile>("compileKotlin") {
        dependsOn("generateGrammarSource")
    }
}

kotlin.sourceSets {
    forEach {
        it.kotlin.exclude("Main.kt")
    }
}

publishing.publications {
    create<MavenPublication>("mavenJava") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
    }
}
