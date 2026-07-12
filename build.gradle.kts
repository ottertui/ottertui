plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
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
