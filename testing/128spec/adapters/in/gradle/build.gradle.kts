plugins {
  id("rbt.adapter.inbound.gradle")
}

group = "com.github.c128lib.retro-assembler.testing.128spec.in"

dependencies {
  implementation(project(":testing:128spec"))
  implementation(project(":shared:gradle"))
}
