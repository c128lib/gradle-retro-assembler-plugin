val kotlinVersion: String by project
val gradleDownloadTaskVersion: String by project
val tagPropertyName = "tag"

plugins {
    kotlin("jvm")
    id("com.diffplug.spotless")
}


allprojects {

    group = "com.github.c128lib"
    version = "1.7.6"

    if (project.hasProperty(tagPropertyName)) {
        version = project.property(tagPropertyName) ?: version
    }

    repositories {
        mavenCentral()
	jcenter {
	    url = uri("https://jcenter.bintray.com/")
	}
    }
}


tasks {
    val collectTestResults by register("collectTestResults") {
        group = "verification"
        doLast {
            fileTree(".") {
                include("**/build/test-results/test/*.xml")
                exclude("build/")
            }.forEach {
                copy {
                    from(it)
                    into("$buildDir/test-results/gradle")
                }
            }
        }
    }
    collectTestResults.dependsOn(named("test"))
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
}
