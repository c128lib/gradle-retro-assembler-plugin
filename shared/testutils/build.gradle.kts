plugins {
  id("rbt.kotlin")
}

group = "com.github.c128lib.retro-assembler"

dependencies {
  implementation(project(":shared:binary-utils"))
  implementation(project(":shared:processor"))
}
