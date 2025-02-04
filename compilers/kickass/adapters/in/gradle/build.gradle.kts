plugins {
  id("rbt.adapter.inbound.gradle")
}

group = "com.github.c128lib.retro-assembler.compilers.kickass.in"

dependencies {
  implementation(project(":compilers:kickass"))
  implementation(project(":shared:gradle"))
  implementation(project(":shared:domain"))
}
