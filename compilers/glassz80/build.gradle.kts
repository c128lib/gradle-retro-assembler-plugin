plugins {
  id("rbt.domain")
}

group = "com.github.c128lib.retro-assembler"

dependencies {
  implementation(project(":shared:domain"))
  implementation(project(":shared:processor"))
}
