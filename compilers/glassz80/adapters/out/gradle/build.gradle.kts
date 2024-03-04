plugins {
  id("rbt.adapter.outbound.gradle")
}

group = "com.github.c128lib.retro-assembler.compilers.glassz80.out"

dependencies {
  implementation(project(":compilers:glassz80"))
  implementation(project(":shared:domain"))
}
