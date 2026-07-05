plugins {
    id("java-library")
    id("maven-publish")
    id("jacoco")
}

allprojects {
    group = "com.ottertui"
    version = "0.1.0"

    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "jacoco")

    jacoco {
        toolVersion = "0.8.14"
    }

    java {
        toolchain.languageVersion = JavaLanguageVersion.of(21)
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:unchecked")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
        finalizedBy(tasks.named("jacocoTestReport"))
    }

    tasks.named<JacocoReport>("jacocoTestReport") {
        dependsOn(tasks.named("test"))
        reports {
            xml.required = true
            html.required = true
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                pom {
                    name = "ottertui-${project.name}"
                    description = "OtterTUI - A modern Java terminal UI library"
                    url = "https://github.com/ottertui/ottertui"
                    licenses {
                        license {
                            name = "Apache-2.0"
                            url = "https://www.apache.org/licenses/LICENSE-2.0"
                        }
                    }
                }
            }
        }
    }
}
