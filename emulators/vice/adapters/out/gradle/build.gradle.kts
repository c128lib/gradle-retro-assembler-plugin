plugins {
  id("rbt.adapter.outbound.gradle")
}

group = "com.github.c128lib.retro-assembler.emulators.vice"

dependencies {
  implementation(project(":emulators:vice"))
  implementation(project(":shared:domain"))
  implementation(project(":shared:gradle"))
}
