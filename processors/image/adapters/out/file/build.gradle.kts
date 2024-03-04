plugins {
  id("rbt.adapter.outbound.gradle")
}

group = "com.github.c128lib.retro-assembler.processors.image"

dependencies {
    implementation(project(":processors:image"))
    implementation(project(":shared:domain"))
    implementation(project(":shared:gradle"))
}
