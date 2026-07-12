plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(findProperty("sonatypeUsername") as String? ?: System.getenv("SONATYPE_USERNAME") ?: "")
            password.set(findProperty("sonatypePassword") as String? ?: System.getenv("SONATYPE_PASSWORD") ?: "")
        }
    }
}

allprojects {
    group = "io.github.ottertui"
    version = "0.1.0"

    repositories {
        mavenCentral()
        maven { url = uri("https://maven.aliyun.com/repository/central") }
    }
}
