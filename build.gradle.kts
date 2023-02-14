import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform") version "1.7.10"
    kotlin("kapt") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("org.jetbrains.compose") version "1.2.0"
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    maven("https://jitpack.io/")
}


// JVM
val ktorVersion: String by project
val kotlinxHtmlVersion: String by project
val slf4jVersion: String by project

// npm
val postcssVersion: String by project
val postcssLoaderVersion: String by project
val autoprefixerVersion: String by project
val tailwindcssVersion: String by project

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useFirefox()
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting{
            dependencies{
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.insert-koin:koin-core:3.2.2")

            }
        }
        val commonTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)

                implementation("io.ktor:ktor-server-jetty:$ktorVersion")
                implementation("org.slf4j:slf4j-api:$slf4jVersion")
                runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
                implementation("com.github.mnbjhu:neo4k:0.0.7-alpha")
                implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-host-common-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-server-sessions:$ktorVersion")
                implementation("io.ktor:ktor-server-auth:$ktorVersion")
                implementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-websockets:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.insert-koin:koin-ktor:3.2.2")
                implementation("io.insert-koin:koin-logger-slf4j:3.2.2")

                implementation("org.mindrot:jbcrypt:0.4")

                implementation("com.natpryce:konfig:1.6.10.0")

                implementation("com.github.mnbjhu.KotlinRedisGraph:core:0.9.2")

                configurations.getByName("kapt").dependencies.add(
                    org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency(
                        "com.github.mnbjhu.KotlinRedisGraph",
                        "annotations",
                        "0.9.2"
                    )
                )



            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation("app.softwork:routing-compose:0.2.7")
                implementation("org.jetbrains.compose.web:web-svg-js:1.2.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-websockets:$ktorVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-extensions:1.0.1-pre.256-kotlin-1.5.31")
                implementation("io.ktor:ktor-client-js:$ktorVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxHtmlVersion")

                implementation(npm("postcss", postcssVersion))
                implementation(npm("postcss-loader", postcssLoaderVersion)) // 5.0.0 seems not to work
                implementation(npm("autoprefixer", autoprefixerVersion))
                implementation(npm("tailwindcss", tailwindcssVersion))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

application {
    mainClass.set("ServerKt")
}

tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack") {
    outputFileName = "js.js"
}

tasks.withType(KotlinWebpack::class.java).forEach { it.inputs.files(fileTree("src/jsMain/resources")) }

val jvmJarTask = tasks.getByName<Jar>("jvmJar") {
    dependsOn(tasks.getByName("jsBrowserProductionWebpack"))
    val jsBrowserProductionWebpack = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack")
    from(jsBrowserProductionWebpack.destinationDirectory.resolve(jsBrowserProductionWebpack.outputFileName))
}

tasks.getByName<JavaExec>("run") {
    dependsOn(jvmJarTask)
    classpath(jvmJarTask)
}

// Suppresses a "without declaring an explicit or implicit dependency" warning
tasks.getByName("startScripts").dependsOn("metadataJar")