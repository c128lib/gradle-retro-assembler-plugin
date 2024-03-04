plugins {
  id("rbt.adapter.outbound.gradle")
}

group = "com.github.c128lib.retro-assembler.compilers.kickass.out"

dependencies {
  implementation(project(":compilers:kickass"))
  implementation(project(":shared:filedownload"))
}
