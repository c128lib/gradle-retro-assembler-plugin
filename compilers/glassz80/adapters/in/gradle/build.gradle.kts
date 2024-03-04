plugins {
  id("rbt.adapter.inbound.gradle")
}

group = "com.github.c128lib.retro-assembler.compilers.glassz80.in"

dependencies {
  implementation(project(":compilers:glassz80"))
  implementation(project(":shared:gradle"))
  implementation(project(":shared:domain"))
}
